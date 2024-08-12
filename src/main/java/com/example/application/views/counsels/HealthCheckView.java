package com.example.application.views.counsels;

import com.example.application.entities.MemberEntity;
import com.example.application.entities.ObserverEntity;
import com.example.application.repositories.MemberRepository;
import com.example.application.repositories.ObserverRepository;
import com.example.application.services.BankAccountValidator;
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

@PageTitle("Provjera grešaka")
@PermitAll
@Route(value = "health-check", layout = MainLayout.class)
public class HealthCheckView extends VerticalLayout {
    private final MemberRepository memberRepository;
    private final JMBGValidator jmbgValidator;
    private final BankAccountValidator bankAccountValidator;
    private final ObserverRepository observerRepository;

    public HealthCheckView(MemberRepository memberRepository, JMBGValidator jmbgValidator, BankAccountValidator bankAccountValidator, ObserverRepository observerRepository) {
        this.memberRepository = memberRepository;
        this.jmbgValidator = jmbgValidator;
        this.bankAccountValidator = bankAccountValidator;
        this.observerRepository = observerRepository;

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

            for(MemberEntity memberEntity: memberEntityList) {
                Span votingCouncelName = new Span("BM: " + memberEntity.getConstraint().getVotingCouncel().getCode() + ", " + memberEntity.getConstraint().getVotingCouncel().getName());
                Span email = new Span("Mentor: " + memberEntity.getConstraint().getVotingCouncel().getMentor().getFullname());
                Span phone = new Span("JMBG: " + memberEntity.getJmbg());

                VerticalLayout content = new VerticalLayout(votingCouncelName, email, phone);
                content.setSpacing(false);
                content.setPadding(false);

                Details details = new Details(memberEntity.getFullname(), content);
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
                    .filter(m -> bankAccountValidator.isValidAccountNumber(m.getBankNumber()) == false)
                    .collect(Collectors.toList());

            for(MemberEntity memberEntity: memberEntityList) {
                Span votingCouncelName = new Span("BM: " + memberEntity.getConstraint().getVotingCouncel().getCode() + ", " + memberEntity.getConstraint().getVotingCouncel().getName());
                Span mentor = new Span("Mentor: " + memberEntity.getConstraint().getVotingCouncel().getMentor().getFullname());
                Span bankNumber = new Span("Žiro račun: " + memberEntity.getJmbg());
                Span jmbg = new Span("Jmbg: " + memberEntity.getJmbg());

                VerticalLayout content = new VerticalLayout(votingCouncelName, mentor, bankNumber, jmbg);
                content.setSpacing(false);
                content.setPadding(false);

                Details details = new Details(memberEntity.getFullname(), content);
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
                        if(observerRepository.existsByJmbg(m.getJmbg()) == false)
                            return false;
                        Optional<ObserverEntity> optional = observerRepository.findByJmbg(m.getJmbg());
                        if(optional.isEmpty())
                            return false;
                        return optional.get().getStatus().getSuccess() == true;
                    })
                    .collect(Collectors.toList());

            for(MemberEntity memberEntity: memberEntityList) {
                Optional<ObserverEntity> optional = observerRepository.findByJmbg(memberEntity.getJmbg());
                if(optional.isEmpty())
                    continue;
                ObserverEntity observer = optional.get();

                Span votingCouncelName = new Span("BM: " + memberEntity.getConstraint().getVotingCouncel().getCode() + ", " + memberEntity.getConstraint().getVotingCouncel().getName());
                Span email = new Span("Mentor: " + memberEntity.getConstraint().getVotingCouncel().getMentor().getFullname());
                Span phone = new Span("Jmbg: " + memberEntity.getJmbg());
                Span fullName = new Span("Ime i prezime: " + memberEntity.getFullname());
                Span decisionNumber = new Span("Broj odluke: " + observer.getStack().getDecisionNumber());
                Span politicalOrganization = new Span("Politički subjekat: " + observer.getStack().getPoliticalOrganization().getName());
                Span documentNumber = new Span("Redni broj: " + observer.getDocumentNumber());


                VerticalLayout content = new VerticalLayout(votingCouncelName, email, phone, fullName, decisionNumber, politicalOrganization, documentNumber);
                content.setSpacing(false);
                content.setPadding(false);

                Details details = new Details(memberEntity.getFullname(), content);
                details.setOpened(false);

                verticalLayout.add(details);
            }
            Long jmbgCount = memberEntityList.stream().count();
            accordion.add("Posmatrači (" + jmbgCount + ")", verticalLayout);
        }
        {
            // Identify duplicates
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setWidthFull();
            verticalLayout.setAlignItems(Alignment.START);
            verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            // Group members by jmbg
            Map<String, List<MemberEntity>> groupedByJmbg = memberRepository.findAll().stream()
                    .filter(m -> m.isEmpty() == false)
                    .collect(Collectors.groupingBy(MemberEntity::getJmbg));

            // Filter groups to find where the list size is greater than one
            Map<String, List<MemberEntity>> duplicates = groupedByJmbg.entrySet().stream()
                    .filter(entry -> entry.getValue().size() > 1)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            for(Map.Entry<String, List<MemberEntity>> duplicate: duplicates.entrySet()) {
                String jmbg = duplicate.getKey();

                VerticalLayout content = new VerticalLayout();
                content.setSpacing(false);
                content.setPadding(false);

                for(MemberEntity member: duplicate.getValue()) {
                    String message = String.format("BM: %s, pozicija (%s, %s)", member.getConstraint().getVotingCouncel().getCode(), member.getConstraint().getPoliticalOrganization().getCode(), member.getConstraint().getTitle().getName());
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
                    .filter(m -> m.getBankNumber() == null)
                    .collect(Collectors.toList());

            addDetailsForMissingData(missingBankNumber, "Nedostaje broj žiro računa: ", verticalLayout, true, true);
            jmbgCount += missingBankNumber.stream().count();

            List<MemberEntity> missingBankName = allMembers.stream()
                    .filter(m -> m.isEmpty() == false)
                    .filter(m -> m.getBankName() == null)
                    .collect(Collectors.toList());

            addDetailsForMissingData(missingBankName, "Nedostaje naziv banke: ", verticalLayout, true, true);
            jmbgCount += missingBankName.stream().count();

            accordion.add("Nedostaju podaci (" + jmbgCount + ")", verticalLayout);
        }
        add(accordion);
    }

    private void addDetailsForMissingData(List<MemberEntity> missingDataList, String message, VerticalLayout verticalLayout, boolean showFullName, boolean showJmbg) {
        for(MemberEntity memberEntity: missingDataList) {
            Span jmbg = null, fullName = null;
            Span votingCouncelName = new Span("BM: " + memberEntity.getConstraint().getVotingCouncel().getCode() + ", " + memberEntity.getConstraint().getVotingCouncel().getName());
            Span mentor = new Span("Mentor: " + memberEntity.getConstraint().getVotingCouncel().getMentor().getFullname());
            if(showJmbg)
                jmbg = new Span("JMBG: " + memberEntity.getJmbg());
            if(showFullName)
                fullName = new Span("Ime i prezime: " + memberEntity.getFullname());

            VerticalLayout content = new VerticalLayout(votingCouncelName, mentor);
            if(showJmbg)
                content.add(jmbg);
            if(showFullName)
                content.add(fullName);

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
