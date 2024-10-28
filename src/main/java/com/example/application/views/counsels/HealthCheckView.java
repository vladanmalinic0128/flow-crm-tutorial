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
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        accordion.setWidth("800px");
        accordion.getStyle().set("margin", "0 auto");

        {
            //Invalid jmbg
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setWidthFull();
            verticalLayout.setAlignItems(Alignment.START);
            verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            List<MemberEntity> memberEntityList = memberRepository.findAll().stream()
                    .filter(m -> m.isEmpty() == false)
                    .filter(m -> jmbgValidator.isValidJMBG(m.getJmbg()) == false)
                    .collect(Collectors.toList());

            for (MemberEntity memberEntity : memberEntityList) {
                Span votingCouncelName = new Span("BM: " + cyrillicToLatinConverter.convert(memberEntity.getConstraint().getVotingCouncel().getCode() + ", " + memberEntity.getConstraint().getVotingCouncel().getName()).toUpperCase());
                Span mentor = new Span("Mentor: " + cyrillicToLatinConverter.convert(memberEntity.getConstraint().getVotingCouncel().getMentor().getFullname()).toUpperCase());
                Span jmbg = new Span("JMBG: " + memberEntity.getJmbg());

                VerticalLayout content = new VerticalLayout(jmbg, votingCouncelName, mentor);
                content.setSpacing(false);
                content.setPadding(false);

                Details details = new Details(cyrillicToLatinConverter.convert(memberEntity.getFullname()).toUpperCase(), content);
                details.setOpened(false);
                styleDetails(details);

                verticalLayout.add(details);
            }
            Long jmbgCount = memberEntityList.stream().count();
            accordion.add("Nevalidan JMBG (" + jmbgCount + ")", verticalLayout);
        }
        {
            // Invalid bank number
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setWidthFull();
            verticalLayout.setAlignItems(Alignment.START);
            verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            List<MemberEntity> memberEntityList = memberRepository.findAll().stream()
                    .filter(m -> m.isEmpty() == false)
                    .filter(m -> m.getIsAcknowledged() != null && m.getIsAcknowledged())
                    .filter(m -> bankAccountValidator.isValidAccountNumber(m.getBankNumber()) == false)
                    .collect(Collectors.toList());

            for (MemberEntity memberEntity : memberEntityList) {
                Span votingCouncelName = new Span("BM: " + cyrillicToLatinConverter.convert(memberEntity.getConstraint().getVotingCouncel().getCode() + ", " + memberEntity.getConstraint().getVotingCouncel().getName()).toUpperCase());
                Span mentor = new Span("Mentor: " + cyrillicToLatinConverter.convert(memberEntity.getConstraint().getVotingCouncel().getMentor().getFullname()).toUpperCase());
                Span bankNumber = new Span("Žiro račun: " + memberEntity.getBankNumber());
                Span jmbg = new Span("Jmbg: " + memberEntity.getJmbg());

                VerticalLayout content = new VerticalLayout(jmbg, votingCouncelName, mentor, bankNumber);
                content.setSpacing(false);
                content.setPadding(false);

                Details details = new Details(cyrillicToLatinConverter.convert(memberEntity.getFullname()).toUpperCase(), content);
                details.setOpened(false);
                details.getStyle().set("background-color", "#3190f6");
                styleDetails(details);

                verticalLayout.add(details);
            }
            Long jmbgCount = memberEntityList.stream().count();
            accordion.add("Nevalidan žiro račun (" + jmbgCount + ")", verticalLayout);
        }
        {
            // Already a observer
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setWidthFull();
            verticalLayout.setAlignItems(Alignment.START);
            verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            List<MemberEntity> memberEntityList = memberRepository.findAll().stream()
                    .filter(m -> m.isEmpty() == false)
                    .filter(m -> {
                        if (observerRepository.existsByJmbg(m.getJmbg()) == false)
                            return false;
                        Optional<ObserverEntity> optional = observerRepository.findFirstByJmbgAndStatus_Id(m.getJmbg(), 1);
                        if (optional.isEmpty())
                            return false;
                        return optional.get().getStatus().getSuccess() == true;
                    })
                    .collect(Collectors.toList());

            for (MemberEntity memberEntity : memberEntityList) {
                Optional<ObserverEntity> optional = observerRepository.findFirstByJmbgAndStatus_Id(memberEntity.getJmbg(), 1);
                if (optional.isEmpty())
                    continue;
                ObserverEntity observer = optional.get();

                Span votingCouncelName = new Span("BM: " + cyrillicToLatinConverter.convert(memberEntity.getConstraint().getVotingCouncel().getCode() + ", " + memberEntity.getConstraint().getVotingCouncel().getName()).toUpperCase());
                Span mentor = new Span("Mentor: " + cyrillicToLatinConverter.convert(memberEntity.getConstraint().getVotingCouncel().getMentor().getFullname()).toUpperCase());
                Span jmbg = new Span("Jmbg: " + memberEntity.getJmbg());
                Span fullName = new Span("Ime i prezime: " + cyrillicToLatinConverter.convert(memberEntity.getFullname()).toUpperCase());
                Span decisionNumber = new Span("Broj odluke: " + observer.getStack().getDecisionNumber());
                Span politicalOrganization = new Span("Politički subjekat: " + cyrillicToLatinConverter.convert(observer.getStack().getPoliticalOrganization().getName()).toUpperCase());
                Span documentNumber = new Span("Redni broj: " + observer.getDocumentNumber());


                VerticalLayout content = new VerticalLayout(jmbg, votingCouncelName, mentor, fullName, decisionNumber, politicalOrganization, documentNumber);
                content.setSpacing(false);
                content.setPadding(false);

                Details details = new Details(cyrillicToLatinConverter.convert(memberEntity.getFullname()).toUpperCase(), content);
                details.setOpened(false);

                verticalLayout.add(details);
            }
            Long jmbgCount = memberEntityList.stream().count();
            accordion.add("Posmatrači (" + jmbgCount + ")", verticalLayout);
        }
        {
            // Already a president
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setWidthFull();
            verticalLayout.setAlignItems(Alignment.START);
            verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            List<MemberEntity> memberEntityList = memberRepository.findAll().stream()
                    .filter(m -> m.isEmpty() == false)
                    .filter(m -> this.presidentRepository.existsByJmbg(m.getJmbg()))
                    .collect(Collectors.toList());

            for (MemberEntity memberEntity : memberEntityList) {
                if (memberEntity.getJmbg() == null)
                    continue;
                Optional<PresidentEntity> optional = presidentRepository.findByJmbg(memberEntity.getJmbg());
                if (optional.isEmpty())
                    continue;
                PresidentEntity president = optional.get();

                Span votingCouncelName = new Span("BM: " + cyrillicToLatinConverter.convert(memberEntity.getConstraint().getVotingCouncel().getCode() + ", " + memberEntity.getConstraint().getVotingCouncel().getName()).toUpperCase());
                Span mentor = new Span("Mentor: " + cyrillicToLatinConverter.convert(memberEntity.getConstraint().getVotingCouncel().getMentor().getFullname()).toUpperCase());
                Span jmbg = new Span("Jmbg: " + memberEntity.getJmbg());
                Span fullName = new Span("Ime i prezime: " + cyrillicToLatinConverter.convert(memberEntity.getFullname()).toUpperCase());
                Span presidentCodeNumber;
                if (president.getIsPresident())
                    presidentCodeNumber = new Span("Predsjednik na: " + president.getVotingCouncel().getCode());
                else
                    presidentCodeNumber = new Span("Zamjenik predsjednika na: " + president.getVotingCouncel().getCode());

                VerticalLayout content = new VerticalLayout(jmbg, votingCouncelName, mentor, fullName, presidentCodeNumber);
                content.setSpacing(false);
                content.setPadding(false);

                Details details = new Details(cyrillicToLatinConverter.convert(memberEntity.getFullname()).toUpperCase(), content);
                details.setOpened(false);

                verticalLayout.add(details);
            }
            Long jmbgCount = memberEntityList.stream().count();
            accordion.add("Predsjednici (" + jmbgCount + ")", verticalLayout);
        }
        {
            // Identify duplicates
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setWidthFull();
            verticalLayout.setAlignItems(Alignment.START);
            verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            // Group members by jmbg
            Map<String, List<MemberEntity>> groupedByJmbg = memberRepository.findAll().stream()
                    .filter(m -> m.getJmbg() != null && m.getJmbg().isBlank() == false)
                    .filter(m -> m.isEmpty() == false)
                    .collect(Collectors.groupingBy(MemberEntity::getJmbg));

            // Filter groups to find where the list size is greater than one
            Map<String, List<MemberEntity>> duplicates = groupedByJmbg.entrySet().stream()
                    .filter(entry -> entry.getValue().size() > 1)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            for (Map.Entry<String, List<MemberEntity>> duplicate : duplicates.entrySet()) {
                String jmbg = duplicate.getKey();
                Span jmbgSpan = new Span("Jmbg: " + jmbg);
                VerticalLayout content = new VerticalLayout(jmbgSpan);
                content.setSpacing(false);
                content.setPadding(false);

                for (MemberEntity member : duplicate.getValue()) {
                    String message = String.format("BM: %s, pozicija (%s, %s)", cyrillicToLatinConverter.convert(member.getConstraint().getVotingCouncel().getCode()).toUpperCase(), member.getConstraint().getPoliticalOrganization().getCode(), cyrillicToLatinConverter.convert(member.getConstraint().getTitle().getName()).toUpperCase());
                    Span votingCouncelsSpan = new Span(message);
                    content.add(votingCouncelsSpan);
                }

                Details details = new Details(jmbg, content);
                details.setOpened(false);
                styleDetails(details);

                verticalLayout.add(details);

            }

            Long jmbgCount = duplicates.entrySet().stream().count();
            accordion.add("Duplikati (" + jmbgCount + ")", verticalLayout);
        }
        {
            //Invalid data
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setWidthFull();
            verticalLayout.setAlignItems(Alignment.START);
            verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            Long jmbgCount = 0L;

            List<MemberEntity> allMembers = memberRepository.findAll();

            List<MemberEntity> missingNamesMembers = allMembers.stream()
                    .filter(m -> m.isEmpty() == false)
                    .filter(m -> m.getFirstname() == null || m.getLastname() == null)
                    .collect(Collectors.toList());

            addDetailsForMissingData(missingNamesMembers, "Nedostaju ime ili prezime: ", verticalLayout, false, true);
            jmbgCount += missingNamesMembers.stream().count();

            List<MemberEntity> missingGender = allMembers.stream()
                    .filter(m -> m.isEmpty() == false)
                    .filter(m -> m.getIsMale() == null)
                    .collect(Collectors.toList());

            addDetailsForMissingData(missingGender, "Nedostaje pol: ", verticalLayout, true, true);
            jmbgCount += missingGender.stream().count();

            List<MemberEntity> missingQualifications = allMembers.stream()
                    .filter(m -> m.isEmpty() == false)
                    .filter(m -> m.getQualifications() == null)
                    .collect(Collectors.toList());

            addDetailsForMissingData(missingQualifications, "Nedostaje stručna sprema: ", verticalLayout, true, true);
            jmbgCount += missingQualifications.stream().count();

            List<MemberEntity> missingJmbg = allMembers.stream()
                    .filter(m -> m.isEmpty() == false)
                    .filter(m -> m.getJmbg() == null)
                    .collect(Collectors.toList());

            addDetailsForMissingData(missingJmbg, "Nedostaje JMBG: ", verticalLayout, true, false);
            jmbgCount += missingJmbg.stream().count();

            List<MemberEntity> missingPhone = allMembers.stream()
                    .filter(m -> m.isEmpty() == false)
                    .filter(m -> m.getPhoneNumber() == null)
                    .collect(Collectors.toList());

            addDetailsForMissingData(missingPhone, "Nedostaje broj telefona: ", verticalLayout, true, true);
            jmbgCount += missingPhone.stream().count();

            List<MemberEntity> missingBankNumber = allMembers.stream()
                    .filter(m -> m.isEmpty() == false)
                    .filter(m -> m.getIsAcknowledged() == null || m.getIsAcknowledged())
                    .filter(m -> m.getBankNumber() == null)
                    .collect(Collectors.toList());

            addDetailsForMissingData(missingBankNumber, "Nedostaje broj žiro računa: ", verticalLayout, true, true);
            jmbgCount += missingBankNumber.stream().count();

            List<MemberEntity> missingBankName = allMembers.stream()
                    .filter(m -> m.isEmpty() == false)
                    .filter(m -> m.getIsAcknowledged() == null || m.getIsAcknowledged())
                    .filter(m -> m.getBankName() == null)
                    .collect(Collectors.toList());

            addDetailsForMissingData(missingBankName, "Nedostaje naziv banke: ", verticalLayout, true, true);
            jmbgCount += missingBankName.stream().count();

            accordion.add("Nedostaju podaci (" + jmbgCount + ")", verticalLayout);
        }
        {
            // Identify duplicates
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setWidthFull();
            verticalLayout.setAlignItems(Alignment.START);
            verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            // Group members by jmbg
            Map<String, List<MemberEntity>> groupedByBankNumber = memberRepository.findAll().stream()
                    .filter(m -> m.getBankNumber() != null && m.getBankNumber().isBlank() == false)
                    .filter(m -> m.isEmpty() == false)
                    .collect(Collectors.groupingBy(MemberEntity::getBankNumber));

            // Filter groups to find where the list size is greater than one
            Map<String, List<MemberEntity>> duplicates = groupedByBankNumber.entrySet().stream()
                    .filter(entry -> entry.getValue().size() > 1)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            for (Map.Entry<String, List<MemberEntity>> duplicate : duplicates.entrySet()) {
                String bankNumber = duplicate.getKey();
                Span bankNumberSpan = new Span("Bankovni račun: " + bankNumber);
                VerticalLayout content = new VerticalLayout(bankNumberSpan);
                content.setSpacing(false);
                content.setPadding(false);

                for (MemberEntity member : duplicate.getValue()) {
                    String message = String.format("BM: %s, pozicija (%s, %s)", cyrillicToLatinConverter.convert(member.getConstraint().getVotingCouncel().getCode()).toUpperCase(), member.getConstraint().getPoliticalOrganization().getCode(), cyrillicToLatinConverter.convert(member.getConstraint().getTitle().getName()).toUpperCase());
                    Span votingCouncelsSpan = new Span(message);
                    content.add(votingCouncelsSpan);
                }

                Details details = new Details(bankNumber, content);
                details.setOpened(false);
                styleDetails(details);

                verticalLayout.add(details);

            }

            Long jmbgCount = duplicates.entrySet().stream().count();
            accordion.add("Duplikati (" + jmbgCount + ")", verticalLayout);
        }
        {
            // Identify bank number duplicates
            // Initialize the layout
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setWidthFull();
            verticalLayout.setAlignItems(Alignment.START);
            verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            // Retrieve all members and presidents
            List<MemberEntity> allMembers = memberRepository.findAll().stream().filter(m -> m.getIsAcknowledged() == null || m.getIsAcknowledged()).collect(Collectors.toList());
            List<PresidentEntity> allPresidents = presidentRepository.findAll().stream().filter(p -> p.getIsAcknowledged() == null || p.getIsAcknowledged()).collect(Collectors.toList());

            // Combine both lists into one stream and group by bank number
            Map<String, List<Object>> groupedByBankNumber = Stream.concat(
                    allMembers.stream().filter(m -> m.getBankNumber() != null && !m.getBankNumber().isBlank()),
                    allPresidents.stream().filter(p -> p.getBankNumber() != null && !p.getBankNumber().isBlank())
            ).collect(Collectors.groupingBy(
                    entity -> {
                        if (entity instanceof MemberEntity) {
                            return ((MemberEntity) entity).getBankNumber();
                        } else {
                            return ((PresidentEntity) entity).getBankNumber();
                        }
                    }
            ));

            // Filter groups to find where the list size is greater than one (i.e., duplicates)
            Map<String, List<Object>> duplicates = groupedByBankNumber.entrySet().stream()
                    .filter(entry -> entry.getValue().size() > 1)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            for (Map.Entry<String, List<Object>> duplicate : duplicates.entrySet()) {
                String bankNumber = duplicate.getKey();
                Span bankNumberSpan = new Span("Bankovni račun: " + bankNumber);
                VerticalLayout content = new VerticalLayout(bankNumberSpan);
                content.setSpacing(false);
                content.setPadding(false);

                for (Object entity : duplicate.getValue()) {
                    String message;
                    if (entity instanceof MemberEntity) {
                        MemberEntity member = (MemberEntity) entity;
                        message = String.format("BM: %s, pozicija (%s, %s)",
                                cyrillicToLatinConverter.convert(member.getConstraint().getVotingCouncel().getCode()).toUpperCase(),
                                member.getConstraint().getPoliticalOrganization().getCode(),
                                cyrillicToLatinConverter.convert(member.getConstraint().getTitle().getName()).toUpperCase());
                    } else {
                        PresidentEntity president = (PresidentEntity) entity;
                        message = String.format("BM: %s, pozicija (%s, %s)",
                                cyrillicToLatinConverter.convert(president.getVotingCouncel().getCode()).toUpperCase(),
                                president.getIsPresident() ? "Predsjednik" : "Zamjenik predsjednika");
                    }
                    Span votingCouncelsSpan = new Span(message);
                    content.add(votingCouncelsSpan);
                }

                Details details = new Details(bankNumber, content);
                details.setOpened(false);
                styleDetails(details);

                verticalLayout.add(details);
            }

            Long duplicateCount = (long) duplicates.size();
            accordion.add("Duplikati (" + duplicateCount + ")", verticalLayout);
        }
        add(accordion);
    }

    private void addDetailsForMissingData(List<MemberEntity> missingDataList, String message, VerticalLayout verticalLayout, boolean showFullName, boolean showJmbg) {
        for (MemberEntity memberEntity : missingDataList) {
            Span jmbg = null, fullName = null;
            Span votingCouncelName = new Span("BM: " + cyrillicToLatinConverter.convert(memberEntity.getConstraint().getVotingCouncel().getCode()).toUpperCase() + ", " + cyrillicToLatinConverter.convert(memberEntity.getConstraint().getVotingCouncel().getName()).toUpperCase());
            Span mentor = new Span("Mentor: " + cyrillicToLatinConverter.convert(memberEntity.getConstraint().getVotingCouncel().getMentor().getFullname()).toUpperCase());
            if (showJmbg)
                jmbg = new Span("JMBG: " + memberEntity.getJmbg());
            if (showFullName)
                fullName = new Span("Ime i prezime: " + cyrillicToLatinConverter.convert(memberEntity.getFullname()).toUpperCase());

            VerticalLayout content = new VerticalLayout();
            if (showJmbg)
                content.add(jmbg);
            if (showFullName)
                content.add(fullName);
            content.add(votingCouncelName);
            content.add(mentor);

            content.setSpacing(false);
            content.setPadding(false);

            Details details = new Details(message + (showJmbg ? memberEntity.getJmbg() : memberEntity.getFullname()), content);
            details.setOpened(false);
            styleDetails(details);

            verticalLayout.add(details);
        }
    }

    private void styleDetails(Details details) {
        details.getStyle()
                .set("background-color", "#f0f0f0")
                .set("border", "1px solid #ccc")
                .set("border-radius", "5px")
                .set("padding", "10px")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");
    }
}
