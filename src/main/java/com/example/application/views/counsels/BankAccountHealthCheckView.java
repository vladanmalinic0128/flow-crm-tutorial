package com.example.application.views.counsels;

import com.example.application.entities.MemberEntity;
import com.example.application.entities.MentorEntity;
import com.example.application.entities.PresidentEntity;
import com.example.application.entities.VotingCouncelEntity;
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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@PageTitle("Bankovni racuni")
@PermitAll
@Route(value = "health-check-by-bank-accounts", layout = MainLayout.class)
public class BankAccountHealthCheckView extends VerticalLayout {
    private final MemberRepository memberRepository;
    private final JMBGValidator jmbgValidator;
    private final BankAccountValidator bankAccountValidator;
    private final ObserverRepository observerRepository;
    private final PresidentRepository presidentRepository;
    private final CyrillicToLatinConverter cyrillicToLatinConverter;
    private final VotingCouncelRepository votingCouncelRepository;

    public BankAccountHealthCheckView(MemberRepository memberRepository, JMBGValidator jmbgValidator, BankAccountValidator bankAccountValidator, ObserverRepository observerRepository, PresidentRepository presidentRepository, CyrillicToLatinConverter cyrillicToLatinConverter, VotingCouncelRepository votingCouncelRepository) {
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

        Accordion mainAccordion = new Accordion();
        mainAccordion.setWidthFull();
        mainAccordion.getStyle().set("max-width", "1400px");
        mainAccordion.getStyle().set("margin", "0 auto");

        // All three lists are fetched once, each in a single SQL query with JOIN FETCH for the associations the
        // loop below needs (mentor / constraint->votingCouncel / votingCouncel), instead of the previous per-mentor
        // traversal of the lazy `constraints`/`presidents` collections on VotingCouncelEntity - which, done 4x per
        // mentor with no batch-fetch-size configured, issued a separate SQL query per voting council per pass.
        Map<MentorEntity, List<VotingCouncelEntity>> groupedByMentor = votingCouncelRepository.findAllWithMentor().stream()
                .collect(Collectors.groupingBy(VotingCouncelEntity::getMentor));

        Map<Long, List<MemberEntity>> membersByVotingCouncelId = memberRepository.findAllWithConstraintDetails().stream()
                .collect(Collectors.groupingBy(m -> m.getConstraint().getVotingCouncel().getId()));

        Map<Long, List<PresidentEntity>> presidentsByVotingCouncelId = presidentRepository.findAllWithVotingCouncelDetails().stream()
                .collect(Collectors.groupingBy(p -> p.getVotingCouncel().getId()));

        for (MentorEntity mentor : groupedByMentor.keySet()) {
            Accordion mentorAccordion = new Accordion();
            String mentorName = mentor.getFullname();

            List<MemberEntity> members = groupedByMentor.get(mentor).stream()
                    .map(VotingCouncelEntity::getId)
                    .flatMap(id -> membersByVotingCouncelId.getOrDefault(id, List.of()).stream())
                    .filter(m -> m != null && m.isEmpty() == false)
                    .collect(Collectors.toList());

            List<PresidentEntity> presidents = groupedByMentor.get(mentor).stream()
                    .map(VotingCouncelEntity::getId)
                    .flatMap(id -> presidentsByVotingCouncelId.getOrDefault(id, List.of()).stream())
                    .filter(p -> p != null && p.isEmpty() == false)
                    .collect(Collectors.toList());

            List<MemberEntity> membersWithUnknownStatus = members.stream()
                    .filter(m -> m.getIsAcknowledged() == null)
                    .collect(Collectors.toList());
            addMemberSection(mentorAccordion, "Članovi bez evidencije", membersWithUnknownStatus);

            // Most rows leave isAcknowledged unset (null) rather than explicitly true - requiring true was
            // excluding almost every member, hiding real invalid account numbers. A missing bank number is
            // also flagged here (isValidAccountNumber(null) is false) rather than excluded, since this tab
            // is specifically about bank account problems and a missing account is one of them.
            List<MemberEntity> membersWithInvalidBankNumbers = members.stream()
                    .filter(m -> m.getIsAcknowledged() == null || m.getIsAcknowledged())
                    .filter(m -> !bankAccountValidator.isValidAccountNumber(m.getBankNumber()))
                    .collect(Collectors.toList());
            addMemberSection(mentorAccordion, "Članovi sa nevalidnim bankovnim računom", membersWithInvalidBankNumbers);

            List<PresidentEntity> presidentsWithUnknownStatus = presidents.stream()
                    .filter(p -> p.getIsAcknowledged() == null)
                    .collect(Collectors.toList());
            addPresidentSection(mentorAccordion, "Predsjednici bez evidencije", presidentsWithUnknownStatus);

            List<PresidentEntity> presidentsWithInvalidBankNumbers = presidents.stream()
                    .filter(p -> p.getIsAcknowledged() == null || p.getIsAcknowledged())
                    .filter(p -> !bankAccountValidator.isValidAccountNumber(p.getBankNumber()))
                    .collect(Collectors.toList());
            addPresidentSection(mentorAccordion, "Predsjednici sa nevalidnim bankovnim računom", presidentsWithInvalidBankNumbers);

            mainAccordion.add(mentorName, mentorAccordion);
        }

        add(mainAccordion);
    }

    private void addMemberSection(Accordion mentorAccordion, String label, List<MemberEntity> rows) {
        addSection(mentorAccordion, label, rows, grid -> {
            grid.addColumn(this::memberFullName).setHeader("Ime i prezime").setSortable(true).setAutoWidth(true);
            grid.addColumn(MemberEntity::getJmbg).setHeader("JMBG").setSortable(true).setAutoWidth(true);
            grid.addColumn(MemberEntity::getBankNumber).setHeader("Žiro račun").setAutoWidth(true);
            grid.addColumn(this::memberVotingCouncelLabel).setHeader("Biračko mjesto").setAutoWidth(true);
        });
    }

    private void addPresidentSection(Accordion mentorAccordion, String label, List<PresidentEntity> rows) {
        addSection(mentorAccordion, label, rows, grid -> {
            grid.addColumn(this::presidentFullName).setHeader("Ime i prezime").setSortable(true).setAutoWidth(true);
            grid.addColumn(PresidentEntity::getJmbg).setHeader("JMBG").setSortable(true).setAutoWidth(true);
            grid.addColumn(PresidentEntity::getBankNumber).setHeader("Žiro račun").setAutoWidth(true);
            grid.addColumn(this::presidentVotingCouncelLabel).setHeader("Biračko mjesto").setAutoWidth(true);
        });
    }

    /** Builds a grid (or a "no errors" message when {@code rows} is empty) and adds it under an accordion panel titled "{@code label} (rows.size())". */
    private <T> void addSection(Accordion accordion, String label, List<T> rows, Consumer<Grid<T>> columnConfigurer) {
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
        accordion.add(label + " (" + rows.size() + ")", content);
    }

    private String memberFullName(MemberEntity member) {
        return cyrillicToLatinConverter.convert(member.getFullname()).toUpperCase();
    }

    private String memberVotingCouncelLabel(MemberEntity member) {
        return cyrillicToLatinConverter.convert(member.getConstraint().getVotingCouncel().getCode()
                + ", " + member.getConstraint().getVotingCouncel().getName()).toUpperCase();
    }

    private String presidentFullName(PresidentEntity president) {
        return cyrillicToLatinConverter.convert(president.getFullname()).toUpperCase();
    }

    private String presidentVotingCouncelLabel(PresidentEntity president) {
        return cyrillicToLatinConverter.convert(president.getVotingCouncel().getCode()
                + ", " + president.getVotingCouncel().getName()).toUpperCase();
    }
}
