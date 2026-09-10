package com.example.application.views.counsels;

import com.example.application.entities.MemberEntity;
import com.example.application.entities.ObserverEntity;
import com.example.application.entities.PresidentEntity;
import com.example.application.repositories.MemberRepository;
import com.example.application.repositories.ObserverRepository;
import com.example.application.repositories.PresidentRepository;
import com.example.application.repositories.VotingCouncelRepository;
import com.example.application.services.BankAccountValidator;
import com.example.application.services.CyrillicToLatinConverter;
import com.example.application.services.JMBGValidator;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PageTitle("Provjera grešaka")
@PermitAll
@Route(value = "health-check", layout = MainLayout.class)
public class HealthCheckView extends VerticalLayout {
    private final MemberRepository memberRepository;
    private final JMBGValidator jmbgValidator;
    private final BankAccountValidator bankAccountValidator;
    private final ObserverRepository observerRepository;
    private final PresidentRepository presidentRepository;
    private final CyrillicToLatinConverter cyrillicToLatinConverter;
    private final VotingCouncelRepository votingCouncelRepository;

    public HealthCheckView(MemberRepository memberRepository, JMBGValidator jmbgValidator, BankAccountValidator bankAccountValidator, ObserverRepository observerRepository, PresidentRepository presidentRepository, CyrillicToLatinConverter cyrillicToLatinConverter, VotingCouncelRepository votingCouncelRepository) {
        this.memberRepository = memberRepository;
        this.jmbgValidator = jmbgValidator;
        this.bankAccountValidator = bankAccountValidator;
        this.observerRepository = observerRepository;
        this.cyrillicToLatinConverter = cyrillicToLatinConverter;
        this.presidentRepository = presidentRepository;
        this.votingCouncelRepository = votingCouncelRepository;

        // Main layout
        this.setWidth("100%");
        this.getStyle().set("margin", "0 auto");

        Accordion accordion = new Accordion();
        accordion.setWidthFull();
        accordion.getStyle().set("max-width", "1400px");
        accordion.getStyle().set("margin", "0 auto");

        // Fetched once and reused by every check below - constraint/votingCouncel/mentor/politicalOrganization/title
        // are pulled in the same query (JOIN FETCH) instead of Hibernate issuing a separate SELECT per hop per
        // member, and instead of re-running findAll() (with its own N+1) for every single check.
        List<MemberEntity> allMembers = memberRepository.findAllWithConstraintDetails();
        List<PresidentEntity> allPresidents = presidentRepository.findAllWithVotingCouncelDetails();

        addInvalidJmbgSection(accordion, allMembers);
        addAlreadyObserverSection(accordion, allMembers);
        addAlreadyPresidentSection(accordion, allMembers, allPresidents);
        addDuplicatesSection(accordion, allMembers);
        addMissingDataSection(accordion, allMembers);
        addDuplicateBankNumbersSection(accordion, allMembers, allPresidents);
        addInvalidBankNumberMemberSection(accordion, allMembers);
        addInvalidBankNumberPresidentSection(accordion, allPresidents);

        add(accordion);
    }

    private void addInvalidJmbgSection(Accordion accordion, List<MemberEntity> allMembers) {
        List<MemberEntity> rows = allMembers.stream()
                .filter(m -> m.isEmpty() == false)
                // A position with no ime, prezime, or JMBG isn't a wrong JMBG - it's just an unfilled
                // slot, so it's excluded here even though isEmpty() alone wouldn't catch it (that only
                // looks at fields unrelated to identity, like isForced/bankNumber/qualifications).
                .filter(m -> isBlank(m.getFirstname()) == false || isBlank(m.getLastname()) == false || isBlank(m.getJmbg()) == false)
                .filter(m -> jmbgValidator.isValidJMBG(m.getJmbg()) == false)
                .collect(Collectors.toList());

        addSection(accordion, "Nevalidan JMBG", rows, grid -> {
            grid.addColumn(this::fullName).setHeader("Ime i prezime").setSortable(true).setAutoWidth(true);
            grid.addColumn(MemberEntity::getJmbg).setHeader("JMBG").setSortable(true).setAutoWidth(true);
            grid.addColumn(this::votingCouncelLabel).setHeader("Biračko mjesto").setAutoWidth(true);
            grid.addColumn(this::mentorLabel).setHeader("Mentor").setAutoWidth(true);
        });
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private void addAlreadyObserverSection(Accordion accordion, List<MemberEntity> allMembers) {
        // One bulk query for every "successful" observer, then an in-memory jmbg lookup below -
        // instead of 2-3 repository round trips per member (existsByJmbg + findFirstByJmbgAndStatus_Id, twice).
        Map<String, ObserverEntity> observerByJmbg = observerRepository.findAllWithDetailsByStatusId(1).stream()
                .filter(o -> o.getJmbg() != null)
                .filter(o -> o.getStatus() != null && o.getStatus().getSuccess() == true)
                .collect(Collectors.toMap(ObserverEntity::getJmbg, o -> o, (first, second) -> first));

        List<MemberEntity> rows = allMembers.stream()
                .filter(m -> m.isEmpty() == false)
                .filter(m -> m.getJmbg() != null && observerByJmbg.containsKey(m.getJmbg()))
                .collect(Collectors.toList());

        addSection(accordion, "Posmatrači", rows, grid -> {
            grid.addColumn(this::fullName).setHeader("Ime i prezime").setSortable(true).setAutoWidth(true);
            grid.addColumn(MemberEntity::getJmbg).setHeader("JMBG").setSortable(true).setAutoWidth(true);
            grid.addColumn(this::votingCouncelLabel).setHeader("Biračko mjesto").setAutoWidth(true);
            grid.addColumn(this::mentorLabel).setHeader("Mentor").setAutoWidth(true);
            grid.addColumn(m -> observerByJmbg.get(m.getJmbg()).getStack().getDecisionNumber())
                    .setHeader("Broj odluke").setAutoWidth(true);
            grid.addColumn(m -> cyrillicToLatinConverter.convert(observerByJmbg.get(m.getJmbg()).getStack().getPoliticalOrganization().getName()).toUpperCase())
                    .setHeader("Politički subjekat").setAutoWidth(true);
            grid.addColumn(m -> observerByJmbg.get(m.getJmbg()).getDocumentNumber())
                    .setHeader("Redni broj").setAutoWidth(true);
        });
    }

    private void addAlreadyPresidentSection(Accordion accordion, List<MemberEntity> allMembers, List<PresidentEntity> allPresidents) {
        // One bulk query for every president, then an in-memory jmbg lookup below - instead of
        // existsByJmbg + findByJmbg per member.
        Map<String, PresidentEntity> presidentByJmbg = allPresidents.stream()
                .filter(p -> p.getJmbg() != null)
                .collect(Collectors.toMap(PresidentEntity::getJmbg, p -> p, (first, second) -> first));

        List<MemberEntity> rows = allMembers.stream()
                .filter(m -> m.isEmpty() == false)
                .filter(m -> m.getJmbg() != null && presidentByJmbg.containsKey(m.getJmbg()))
                .collect(Collectors.toList());

        addSection(accordion, "Predsjednici", rows, grid -> {
            grid.addColumn(this::fullName).setHeader("Ime i prezime").setSortable(true).setAutoWidth(true);
            grid.addColumn(MemberEntity::getJmbg).setHeader("JMBG").setSortable(true).setAutoWidth(true);
            grid.addColumn(this::votingCouncelLabel).setHeader("Biračko mjesto").setAutoWidth(true);
            grid.addColumn(this::mentorLabel).setHeader("Mentor").setAutoWidth(true);
            grid.addColumn(m -> {
                PresidentEntity president = presidentByJmbg.get(m.getJmbg());
                return president.getIsPresident()
                        ? "Predsjednik na: " + president.getVotingCouncel().getCode()
                        : "Zamjenik predsjednika na: " + president.getVotingCouncel().getCode();
            }).setHeader("Uloga").setAutoWidth(true);
        });
    }

    private void addDuplicatesSection(Accordion accordion, List<MemberEntity> allMembers) {
        Map<String, List<MemberEntity>> groupedByJmbg = allMembers.stream()
                .filter(m -> m.getJmbg() != null && m.getJmbg().isBlank() == false)
                .filter(m -> m.isEmpty() == false)
                .collect(Collectors.groupingBy(MemberEntity::getJmbg));

        List<List<MemberEntity>> duplicateGroups = groupedByJmbg.values().stream()
                .filter(group -> group.size() > 1)
                .collect(Collectors.toList());

        // One row per duplicate (i.e. per JMBG) instead of one row per occurrence, each occurrence's
        // name/voting council/position/mentor stacked on its own line within that row's cells - so a
        // person duplicated across two committees is one row showing both, lined up with each other,
        // instead of two separate rows that look unrelated or one unreadably long line of text. Name
        // is per-occurrence too, not shared like the JMBG: a duplicate JMBG can just as easily mean
        // the wrong JMBG was entered for two different people, so they shouldn't be assumed identical.
        List<DuplicateRow> rows = duplicateGroups.stream()
                .map(group -> new DuplicateRow(group.get(0).getJmbg(), group))
                .sorted(Comparator.comparing(DuplicateRow::jmbg))
                .collect(Collectors.toList());

        addSection(accordion, "Duplikati", rows, grid -> {
            grid.addColumn(DuplicateRow::jmbg).setHeader("JMBG").setSortable(true).setAutoWidth(true);
            grid.addColumn(new ComponentRenderer<>(row -> multilineCell(row.occurrences(), this::safeFullName)))
                    .setHeader("Ime i prezime").setAutoWidth(true);
            grid.addColumn(new ComponentRenderer<>(row -> multilineCell(row.occurrences(), this::votingCouncelLabel)))
                    .setHeader("Biračka mjesta").setAutoWidth(true);
            grid.addColumn(new ComponentRenderer<>(row -> multilineCell(row.occurrences(), this::politicalOrganizationLabel)))
                    .setHeader("Politički subjekti").setAutoWidth(true);
            grid.addColumn(new ComponentRenderer<>(row -> multilineCell(row.occurrences(),
                    m -> cyrillicToLatinConverter.convert(m.getConstraint().getTitle().getName()).toUpperCase())))
                    .setHeader("Pozicije").setAutoWidth(true);
            grid.addColumn(new ComponentRenderer<>(row -> multilineCell(row.occurrences(), this::mentorLabel)))
                    .setHeader("Mentori").setAutoWidth(true);
        });
    }

    private record DuplicateRow(String jmbg, List<MemberEntity> occurrences) {
    }

    /** One line per occurrence, in the same order across every column of a duplicate-group row so they line up. */
    private <T> Component multilineCell(List<T> occurrences, Function<T, String> label) {
        Div cell = new Div();
        cell.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "var(--lumo-space-xs)");
        occurrences.stream().map(label).map(Div::new).forEach(cell::add);
        return cell;
    }

    // Nested under one "Nedostaju podaci" panel instead of sitting flat alongside the other
    // top-level sections - each check gets its own sub-panel inside, so opening e.g.
    // "Nedostaje naziv banke" doesn't also surface every other row mixed into the same list,
    // without cluttering the outer accordion with extra top-level entries.
    private void addMissingDataSection(Accordion accordion, List<MemberEntity> allMembers) {
        Accordion missingDataAccordion = new Accordion();
        missingDataAccordion.setWidthFull();

        int total = 0;
        total += addMissingDataSubsection(missingDataAccordion, allMembers, "Nedostaje ime ili prezime", m -> m.getFirstname() == null || m.getLastname() == null);
        total += addMissingDataSubsection(missingDataAccordion, allMembers, "Nedostaje pol", m -> m.getIsMale() == null);
        total += addMissingDataSubsection(missingDataAccordion, allMembers, "Nedostaje stručna sprema", m -> m.getQualifications() == null);
        total += addMissingDataSubsection(missingDataAccordion, allMembers, "Nedostaje JMBG", m -> m.getJmbg() == null);
        total += addMissingDataSubsection(missingDataAccordion, allMembers, "Nedostaje broj telefona", m -> m.getPhoneNumber() == null);
        total += addMissingDataSubsection(missingDataAccordion, allMembers, "Nedostaje broj žiro računa",
                m -> (m.getIsAcknowledged() == null || m.getIsAcknowledged()) && m.getBankNumber() == null);
        // Only flagged once there's a bank number to attach a name to - a missing bank number is
        // already caught by the "Nedostaje broj žiro računa" check above, and shouldn't also show
        // up here as a missing bank name.
        total += addMissingDataSubsection(missingDataAccordion, allMembers, "Nedostaje naziv banke",
                m -> (m.getIsAcknowledged() == null || m.getIsAcknowledged()) && isBlank(m.getBankNumber()) == false && m.getBankName() == null);

        accordion.add("Nedostaju podaci (" + total + ")", missingDataAccordion);
    }

    /** Adds one sub-panel to {@code missingDataAccordion} and returns how many rows it holds, so the caller can total them for the outer panel's title. */
    private int addMissingDataSubsection(Accordion missingDataAccordion, List<MemberEntity> allMembers, String label, Predicate<MemberEntity> isMissing) {
        List<MemberEntity> rows = allMembers.stream()
                .filter(m -> m.isEmpty() == false)
                // A position with no ime, prezime, or JMBG isn't missing data - it's just an unfilled
                // slot, so it's excluded here even though isEmpty() alone wouldn't catch it (see the
                // same filter in addInvalidJmbgSection).
                .filter(m -> isBlank(m.getFirstname()) == false || isBlank(m.getLastname()) == false || isBlank(m.getJmbg()) == false)
                .filter(isMissing)
                .sorted(Comparator.comparing(m -> m.getJmbg() != null ? m.getJmbg() : ""))
                .collect(Collectors.toList());

        addSection(missingDataAccordion, label, rows, grid -> {
            grid.addColumn(this::safeFullName).setHeader("Ime i prezime").setAutoWidth(true);
            grid.addColumn(MemberEntity::getJmbg).setHeader("JMBG").setAutoWidth(true);
            grid.addColumn(this::votingCouncelLabel).setHeader("Biračko mjesto").setAutoWidth(true);
            grid.addColumn(this::mentorLabel).setHeader("Mentor").setAutoWidth(true);
        });

        return rows.size();
    }

    private void addInvalidBankNumberMemberSection(Accordion accordion, List<MemberEntity> allMembers) {
        List<MemberEntity> rows = allMembers.stream()
                .filter(m -> m.isEmpty() == false)
                // Same "not explicitly declined" reading of isAcknowledged as the missing-data checks
                // above - most rows have it unset (null) rather than explicitly true, and requiring
                // true here was excluding almost every member, hiding real invalid account numbers.
                .filter(m -> m.getIsAcknowledged() == null || m.getIsAcknowledged())
                .filter(m -> isBlank(m.getBankNumber()) == false && bankAccountValidator.isValidAccountNumber(m.getBankNumber()) == false)
                .collect(Collectors.toList());

        addSection(accordion, "Nevalidan žiro račun - Članovi", rows, grid -> {
            grid.addColumn(this::safeFullName).setHeader("Ime i prezime").setSortable(true).setAutoWidth(true);
            grid.addColumn(MemberEntity::getJmbg).setHeader("JMBG").setSortable(true).setAutoWidth(true);
            grid.addColumn(MemberEntity::getBankNumber).setHeader("Žiro račun").setAutoWidth(true);
            grid.addColumn(this::votingCouncelLabel).setHeader("Biračko mjesto").setAutoWidth(true);
            grid.addColumn(this::mentorLabel).setHeader("Mentor").setAutoWidth(true);
        });
    }

    private void addInvalidBankNumberPresidentSection(Accordion accordion, List<PresidentEntity> allPresidents) {
        List<PresidentEntity> rows = allPresidents.stream()
                .filter(p -> p.isEmpty() == false)
                .filter(p -> p.getIsAcknowledged() == null || p.getIsAcknowledged())
                .filter(p -> isBlank(p.getBankNumber()) == false && bankAccountValidator.isValidAccountNumber(p.getBankNumber()) == false)
                .collect(Collectors.toList());

        addSection(accordion, "Nevalidan žiro račun - Predsjednici", rows, grid -> {
            grid.addColumn(this::presidentSafeFullName).setHeader("Ime i prezime").setSortable(true).setAutoWidth(true);
            grid.addColumn(PresidentEntity::getJmbg).setHeader("JMBG").setSortable(true).setAutoWidth(true);
            grid.addColumn(PresidentEntity::getBankNumber).setHeader("Žiro račun").setAutoWidth(true);
            grid.addColumn(this::presidentVotingCouncelLabel).setHeader("Biračko mjesto").setAutoWidth(true);
            grid.addColumn(this::presidentMentorLabel).setHeader("Mentor").setAutoWidth(true);
        });
    }

    private void addDuplicateBankNumbersSection(Accordion accordion, List<MemberEntity> allMembers, List<PresidentEntity> allPresidents) {
        // Combine both lists into one stream and group by bank number
        Map<String, List<Object>> groupedByBankNumber = Stream.concat(
                allMembers.stream().filter(m -> m.getBankNumber() != null && !m.getBankNumber().isBlank()),
                allPresidents.stream().filter(p -> p.getBankNumber() != null && !p.getBankNumber().isBlank())
        ).collect(Collectors.groupingBy(this::bankNumberOf));

        List<List<Object>> duplicateGroups = groupedByBankNumber.values().stream()
                .filter(group -> group.size() > 1)
                .collect(Collectors.toList());

        // One row per duplicate (i.e. per bank number) instead of one row per occurrence, each
        // occurrence's name/JMBG/voting council/position/mentor stacked on its own line within that
        // row's cells - same layout as the JMBG "Duplikati" section above, and for the same reason:
        // a shared bank number can just as easily mean two different people, not one person twice.
        List<BankDuplicateRow> rows = duplicateGroups.stream()
                .map(group -> new BankDuplicateRow(bankNumberOf(group.get(0)),
                        group.stream().map(this::toBankOccurrence).collect(Collectors.toList())))
                .sorted(Comparator.comparing(BankDuplicateRow::bankNumber))
                .collect(Collectors.toList());

        addSection(accordion, "Dupli bankovni računi", duplicateGroups.size(), rows, grid -> {
            grid.addColumn(BankDuplicateRow::bankNumber).setHeader("Žiro račun").setSortable(true).setAutoWidth(true);
            grid.addColumn(new ComponentRenderer<>(row -> multilineCell(row.occurrences(), BankOccurrence::fullName)))
                    .setHeader("Ime i prezime").setAutoWidth(true);
            grid.addColumn(new ComponentRenderer<>(row -> multilineCell(row.occurrences(), BankOccurrence::jmbg)))
                    .setHeader("JMBG").setAutoWidth(true);
            grid.addColumn(new ComponentRenderer<>(row -> multilineCell(row.occurrences(), BankOccurrence::votingCouncel)))
                    .setHeader("Biračka mjesta").setAutoWidth(true);
            grid.addColumn(new ComponentRenderer<>(row -> multilineCell(row.occurrences(), BankOccurrence::organization)))
                    .setHeader("Politički subjekti").setAutoWidth(true);
            grid.addColumn(new ComponentRenderer<>(row -> multilineCell(row.occurrences(), BankOccurrence::position)))
                    .setHeader("Pozicije").setAutoWidth(true);
            grid.addColumn(new ComponentRenderer<>(row -> multilineCell(row.occurrences(), BankOccurrence::mentor)))
                    .setHeader("Mentori").setAutoWidth(true);
        });
    }

    private String bankNumberOf(Object entity) {
        return entity instanceof MemberEntity member ? member.getBankNumber() : ((PresidentEntity) entity).getBankNumber();
    }

    private BankOccurrence toBankOccurrence(Object entity) {
        if (entity instanceof MemberEntity member) {
            return new BankOccurrence(
                    safeFullName(member),
                    member.getJmbg(),
                    votingCouncelLabel(member),
                    politicalOrganizationLabel(member),
                    cyrillicToLatinConverter.convert(member.getConstraint().getTitle().getName()).toUpperCase(),
                    mentorLabel(member));
        }
        PresidentEntity president = (PresidentEntity) entity;
        String name = Stream.of(president.getFirstname(), president.getLastname())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        return new BankOccurrence(
                cyrillicToLatinConverter.convert(name).toUpperCase(),
                president.getJmbg(),
                cyrillicToLatinConverter.convert(president.getVotingCouncel().getCode()).toUpperCase(),
                "GIK",
                president.getIsPresident() ? "Predsjednik" : "Zamjenik predsjednika",
                cyrillicToLatinConverter.convert(president.getVotingCouncel().getMentor().getFullname()).toUpperCase());
    }

    private record BankDuplicateRow(String bankNumber, List<BankOccurrence> occurrences) {
    }

    private record BankOccurrence(String fullName, String jmbg, String votingCouncel, String organization,
                                   String position, String mentor) {
    }

    private String fullName(MemberEntity member) {
        return cyrillicToLatinConverter.convert(member.getFullname()).toUpperCase();
    }

    /**
     * Same as {@link #fullName}, but safe to use when firstname/lastname may be null (e.g. the
     * "missing data" grid, which includes members missing exactly those fields) -
     * {@link MemberEntity#getFullname()} concatenates unconditionally and would render literally
     * as "null X" in that case.
     */
    private String safeFullName(MemberEntity member) {
        String combined = Stream.of(member.getFirstname(), member.getLastname())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        return cyrillicToLatinConverter.convert(combined).toUpperCase();
    }

    private String votingCouncelLabel(MemberEntity member) {
        return cyrillicToLatinConverter.convert(member.getConstraint().getVotingCouncel().getCode()
                + ", " + member.getConstraint().getVotingCouncel().getName()).toUpperCase();
    }

    private String mentorLabel(MemberEntity member) {
        return cyrillicToLatinConverter.convert(member.getConstraint().getVotingCouncel().getMentor().getFullname()).toUpperCase();
    }

    private String politicalOrganizationLabel(MemberEntity member) {
        String code = member.getConstraint().getPoliticalOrganization().getCode();
        return Boolean.TRUE.equals(member.getIsGik()) ? "GIK (" + code + ")" : code;
    }

    /** Same as {@link #safeFullName}, but for {@link PresidentEntity}. */
    private String presidentSafeFullName(PresidentEntity president) {
        String combined = Stream.of(president.getFirstname(), president.getLastname())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        return cyrillicToLatinConverter.convert(combined).toUpperCase();
    }

    private String presidentVotingCouncelLabel(PresidentEntity president) {
        return cyrillicToLatinConverter.convert(president.getVotingCouncel().getCode()
                + ", " + president.getVotingCouncel().getName()).toUpperCase();
    }

    private String presidentMentorLabel(PresidentEntity president) {
        return cyrillicToLatinConverter.convert(president.getVotingCouncel().getMentor().getFullname()).toUpperCase();
    }

    /** Builds a grid (or a "no errors" message when {@code rows} is empty) and adds it under an accordion panel titled "{@code label} (rows.size())". */
    private <T> void addSection(Accordion accordion, String label, List<T> rows, Consumer<Grid<T>> columnConfigurer) {
        addSection(accordion, label, rows.size(), rows, columnConfigurer);
    }

    /** Same as the other overload, but with an explicit count for the accordion panel title - used where it must differ from the row count (e.g. number of duplicate groups rather than total rows). */
    private <T> void addSection(Accordion accordion, String label, int count, List<T> rows, Consumer<Grid<T>> columnConfigurer) {
        Component content;
        if (rows.isEmpty()) {
            content = new Span("Nema grešaka.");
        } else {
            Grid<T> grid = new Grid<>();
            columnConfigurer.accept(grid);
            grid.setItems(rows);
            grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);
            grid.setAllRowsVisible(true);
            content = grid;
        }
        accordion.add(label + " (" + count + ")", content);
    }
}
