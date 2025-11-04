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
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Map;
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

//    {
//
//        Map<MentorEntity, List<VotingCouncelEntity>> groupedByMentor = votingCouncelRepository.findAll().stream()
//                .collect(Collectors.groupingBy(VotingCouncelEntity::getMentor));
//
//        for (MentorEntity mentor : groupedByMentor.keySet()) {
//            List<MemberEntity> membersWithUnknownStatus = groupedByMentor.get(mentor).stream()
//                    .map(VotingCouncelEntity::getConstraints)
//                    .flatMap(List::stream).map(c -> c.getMember()).filter(m -> m.getIsAcknowledged() == null).collect(Collectors.toList());
//
//            membersWithUnknownStatus.size();
//
//            for (MemberEntity memberEntity : membersWithUnknownStatus) ;
//
//            List<MemberEntity> membersWithInvalidBankNumbers = groupedByMentor.get(mentor).stream()
//                    .map(VotingCouncelEntity::getConstraints)
//                    .flatMap(List::stream).map(c -> c.getMember()).filter(m -> m.getIsAcknowledged() != null && m.getIsAcknowledged() && bankAccountValidator.isValidAccountNumber(m.getBankNumber())).collect(Collectors.toList());
//
//            membersWithInvalidBankNumbers.size();
//
//            for (MemberEntity memberEntity : membersWithInvalidBankNumbers) ;
//
//            List<PresidentEntity> presidentsWithUnknownStatus = groupedByMentor.get(mentor).stream()
//                    .map(VotingCouncelEntity::getPresidents).flatMap(List::stream)
//                    .filter(m -> m.getIsAcknowledged() == null).collect(Collectors.toList());
//
//            presidentsWithUnknownStatus.size();
//
//            for (PresidentEntity presidentEntity : presidentsWithUnknownStatus) ;
//
//            List<PresidentEntity> presidentsWithInvalidBankNumbers = groupedByMentor.get(mentor).stream()
//                    .map(VotingCouncelEntity::getPresidents).flatMap(List::stream)
//                    .filter(m -> m.getIsAcknowledged() == null).collect(Collectors.toList());
//
//            presidentsWithInvalidBankNumbers.size();
//
//            for (PresidentEntity presidentEntity : presidentsWithInvalidBankNumbers) ;
//        }
//    }

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
        mainAccordion.setWidth("800px");
        mainAccordion.getStyle().set("margin", "0 auto");

        Map<MentorEntity, List<VotingCouncelEntity>> groupedByMentor = votingCouncelRepository.findAll().stream()
                .collect(Collectors.groupingBy(VotingCouncelEntity::getMentor));

        for (MentorEntity mentor : groupedByMentor.keySet()) {
            // Create an Accordion for this mentor
            Accordion mentorAccordion = new Accordion();
            String mentorName = mentor.getFullname();
            {
                // Members with unknown status
                List<MemberEntity> membersWithUnknownStatus = groupedByMentor.get(mentor).stream()
                        .map(VotingCouncelEntity::getConstraints)
                        .flatMap(List::stream)
                        .map(c -> c.getMember())
                        .filter(m -> m != null && m.isEmpty() == false)
                        .filter(m -> m.getIsAcknowledged() == null)
                        .collect(Collectors.toList());

                String unknownStatusLabel = "Članovi bez evidencije (" + membersWithUnknownStatus.size() + ")";
                VerticalLayout unknownStatusLayout = new VerticalLayout();
                for (MemberEntity memberEntity : membersWithUnknownStatus) {
                    // Create AccordionPanel for each member
                    Span votingCouncelName = new Span("BM: " + cyrillicToLatinConverter.convert(
                            memberEntity.getConstraint().getVotingCouncel().getCode() + ", " +
                                    memberEntity.getConstraint().getVotingCouncel().getName()).toUpperCase());

                    Span mentorSpan = new Span("Mentor: " + cyrillicToLatinConverter.convert(
                            memberEntity.getConstraint().getVotingCouncel().getMentor().getFullname()).toUpperCase());

                    Span bankNumber = new Span("Žiro račun: " + memberEntity.getBankNumber());
                    Span jmbg = new Span("JMBG: " + memberEntity.getJmbg());

                    // Create content layout for the member
                    VerticalLayout content = new VerticalLayout(jmbg, votingCouncelName, mentorSpan, bankNumber);
                    content.setSpacing(false);
                    content.setPadding(false);

                    // Create AccordionPanel for each member and add content
                    AccordionPanel memberPanel = new AccordionPanel(
                            cyrillicToLatinConverter.convert(memberEntity.getFullname()).toUpperCase(),
                            content
                    );

                    // Optional: Apply styling to the AccordionPanel (similar to how you were styling Details)
                    memberPanel.getElement().getStyle().set("background-color", "#3190f6");
                    styleAccordionPanel(memberPanel); // Custom styling function

                    // Add the member panel to the layout
                    unknownStatusLayout.add(memberPanel);
                }
                mentorAccordion.add(unknownStatusLabel, unknownStatusLayout);
            }
            {
                // Members with invalid bank numbers
                List<MemberEntity> membersWithInvalidBankNumbers = groupedByMentor.get(mentor).stream()
                        .map(VotingCouncelEntity::getConstraints)
                        .flatMap(List::stream)
                        .map(c -> c.getMember())
                        .filter(m -> m != null && m.isEmpty() == false)
                        .filter(m -> /*m.getIsAcknowledged() != null && m.getIsAcknowledged() &&*/ !bankAccountValidator.isValidAccountNumber(m.getBankNumber()))
                        .collect(Collectors.toList());

                String invalidBankLabel = "Članovi sa nevalidnim bankovnim računom (" + membersWithInvalidBankNumbers.size() + ")";
                VerticalLayout invalidBankLayout = new VerticalLayout();

                for (MemberEntity memberEntity : membersWithInvalidBankNumbers) {
                    // Create content for each member with invalid bank number
                    Span votingCouncelName = new Span("BM: " + cyrillicToLatinConverter.convert(
                            memberEntity.getConstraint().getVotingCouncel().getCode() + ", " +
                                    memberEntity.getConstraint().getVotingCouncel().getName()).toUpperCase());

                    Span mentorSpan = new Span("Mentor: " + cyrillicToLatinConverter.convert(
                            memberEntity.getConstraint().getVotingCouncel().getMentor().getFullname()).toUpperCase());

                    Span bankNumber = new Span("Žiro račun: " + memberEntity.getBankNumber());
                    Span jmbg = new Span("JMBG: " + memberEntity.getJmbg());

                    // Create content layout for the member
                    VerticalLayout content = new VerticalLayout(jmbg, votingCouncelName, mentorSpan, bankNumber);
                    content.setSpacing(false);
                    content.setPadding(false);

                    // Create AccordionPanel for each member and add content
                    AccordionPanel memberPanel = new AccordionPanel(
                            cyrillicToLatinConverter.convert(memberEntity.getFullname()).toUpperCase(),
                            content
                    );

                    // Optional: Apply styling to the AccordionPanel
                    memberPanel.getElement().getStyle().set("background-color", "#f6d030"); // Example: different color for invalid bank accounts
                    styleAccordionPanel(memberPanel); // Custom styling function

                    // Add the member panel to the layout
                    invalidBankLayout.add(memberPanel);
                }

                // Add the list of members with invalid bank numbers to the mentor accordion
                mentorAccordion.add(invalidBankLabel, invalidBankLayout);
            }
            {
                // Presidents with unknown status
                List<PresidentEntity> presidentsWithUnknownStatus = groupedByMentor.get(mentor).stream()
                        .map(VotingCouncelEntity::getPresidents)
                        .flatMap(List::stream)
                        .filter(p -> p != null && p.isEmpty() == false)
                        .filter(p -> p.getIsAcknowledged() == null)
                        .collect(Collectors.toList());

                String unknownPresidentsLabel = "Predsjednici bez evidencije (" + presidentsWithUnknownStatus.size() + ")";
                VerticalLayout unknownPresidentsLayout = new VerticalLayout();

                for (PresidentEntity presidentEntity : presidentsWithUnknownStatus) {
                    // Create content for each president with unknown status
                    Span votingCouncelName = new Span("BM: " + cyrillicToLatinConverter.convert(
                            presidentEntity.getVotingCouncel().getCode() + ", " +
                                    presidentEntity.getVotingCouncel().getName()).toUpperCase());

                    Span mentorSpan = new Span("Mentor: " + cyrillicToLatinConverter.convert(
                            presidentEntity.getVotingCouncel().getMentor().getFullname()).toUpperCase());

                    Span bankNumber = new Span("Žiro račun: " + presidentEntity.getBankNumber());
                    Span jmbg = new Span("JMBG: " + presidentEntity.getJmbg());

                    // Create content layout for the president
                    VerticalLayout content = new VerticalLayout(jmbg, votingCouncelName, mentorSpan, bankNumber);
                    content.setSpacing(false);
                    content.setPadding(false);

                    // Create AccordionPanel for each president and add content
                    AccordionPanel presidentPanel = new AccordionPanel(
                            cyrillicToLatinConverter.convert(presidentEntity.getFullname()).toUpperCase(),
                            content
                    );

                    // Optional: Apply styling to the AccordionPanel
                    presidentPanel.getElement().getStyle().set("background-color", "#ffcccb"); // Example: custom color for unknown status
                    styleAccordionPanel(presidentPanel); // Custom styling function

                    // Add the president panel to the layout
                    unknownPresidentsLayout.add(presidentPanel);
                }

                // Add the list of presidents with unknown status to the mentor accordion
                mentorAccordion.add(unknownPresidentsLabel, unknownPresidentsLayout);

            }
            {
                // Presidents with invalid bank numbers
                List<PresidentEntity> presidentsWithInvalidBankNumbers = groupedByMentor.get(mentor).stream()
                        .map(VotingCouncelEntity::getPresidents)
                        .flatMap(List::stream)
                        .filter(p -> p != null && p.isEmpty() == false)
                        .filter(p -> p.getIsAcknowledged() != null && p.getIsAcknowledged() && !bankAccountValidator.isValidAccountNumber(p.getBankNumber()))
                        .collect(Collectors.toList());

                String invalidPresidentsLabel = "Predsjednici sa nevalidnim bankovnim računom (" + presidentsWithInvalidBankNumbers.size() + ")";
                VerticalLayout invalidPresidentsLayout = new VerticalLayout();

                for (PresidentEntity presidentEntity : presidentsWithInvalidBankNumbers) {
                    // Create content for each president with an invalid bank number
                    Span votingCouncelName = new Span("BM: " + cyrillicToLatinConverter.convert(
                            presidentEntity.getVotingCouncel().getCode() + ", " +
                                    presidentEntity.getVotingCouncel().getName()).toUpperCase());

                    Span mentorSpan = new Span("Mentor: " + cyrillicToLatinConverter.convert(
                            presidentEntity.getVotingCouncel().getMentor().getFullname()).toUpperCase());

                    Span bankNumber = new Span("Žiro račun: " + presidentEntity.getBankNumber());
                    Span jmbg = new Span("JMBG: " + presidentEntity.getJmbg());

                    // Create content layout for the president
                    VerticalLayout content = new VerticalLayout(jmbg, votingCouncelName, mentorSpan, bankNumber);
                    content.setSpacing(false);
                    content.setPadding(false);

                    // Create AccordionPanel for each president and add content
                    AccordionPanel presidentPanel = new AccordionPanel(
                            cyrillicToLatinConverter.convert(presidentEntity.getFullname()).toUpperCase(),
                            content
                    );

                    // Optional: Apply styling to the AccordionPanel
                    presidentPanel.getElement().getStyle().set("background-color", "#f6d030"); // Example: different color for invalid bank accounts
                    styleAccordionPanel(presidentPanel); // Custom styling function

                    // Add the president panel to the layout
                    invalidPresidentsLayout.add(presidentPanel);
                }

                // Add the list of presidents with invalid bank numbers to the mentor accordion
                mentorAccordion.add(invalidPresidentsLabel, invalidPresidentsLayout);

            }
            // Add the mentor's accordion to the main accordion
            mainAccordion.add(mentorName, mentorAccordion);
        }

        add(mainAccordion);
    }

    private void styleAccordionPanel(AccordionPanel accordionPanel) {
        accordionPanel.getStyle()
                .set("background-color", "#f0f0f0")
                .set("border", "1px solid #ccc")
                .set("border-radius", "5px")
                .set("padding", "10px")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");
    }
}
