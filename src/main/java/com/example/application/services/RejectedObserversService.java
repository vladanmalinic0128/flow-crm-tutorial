package com.example.application.services;

import com.example.application.entities.MemberEntity;
import com.example.application.entities.ObserverEntity;
import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.entities.PresidentEntity;
import com.example.application.entities.VotingCouncelEntity;
import com.example.application.repositories.MemberRepository;
import com.example.application.repositories.ObserverRepository;
import com.example.application.repositories.PresidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Builds the "rejected observers" overview: every rejected observer together with the reason and,
 * where the reason is a match against other data (president, member, already accredited observer),
 * where that match was found.
 */
@Service
@RequiredArgsConstructor
public class RejectedObserversService {
    public static final String NO_DATA = "Nemam dostupne podatke";

    // Ids of the rejection statuses, see StatusInitializer / AddingObserversForm
    private static final int STATUS_ALREADY_ACCREDITED = 3;
    private static final int STATUS_MEMBER = 4;
    private static final int STATUS_PRESIDENT = 9;
    private static final int STATUS_SUCCESS = 1;

    private final ObserverRepository observerRepository;
    private final PresidentRepository presidentRepository;
    private final MemberRepository memberRepository;

    public record RejectedObserverRow(Integer documentNumber, String fullName, String jmbg, String reason, String details) {
    }

    /** A stack (decision) with its political organization and the observers rejected in it. */
    public record StackRejections(String title, List<RejectedObserverRow> rows) {
    }

    /** Rejected observers grouped by stack, stacks ordered by decision number, then stack id. */
    @Transactional(readOnly = true)
    public List<StackRejections> getRejectedByStack() {
        List<ObserverEntity> rejected = observerRepository.findAllRejectedWithDetails();
        if (rejected.isEmpty())
            return List.of();

        Set<Integer> reasons = rejected.stream().map(o -> o.getStatus().getId()).collect(Collectors.toSet());

        // Only load what's actually needed to explain the rejections that exist
        Map<String, List<PresidentEntity>> presidentsByJmbg = reasons.contains(STATUS_PRESIDENT)
                ? presidentRepository.findAllWithVotingCouncelDetails().stream()
                    .filter(p -> p.getJmbg() != null)
                    .collect(Collectors.groupingBy(PresidentEntity::getJmbg))
                : Map.of();
        Map<String, List<MemberEntity>> membersByJmbg = reasons.contains(STATUS_MEMBER)
                ? memberRepository.findAllWithConstraintDetails().stream()
                    .filter(m -> m.getJmbg() != null)
                    .collect(Collectors.groupingBy(MemberEntity::getJmbg))
                : Map.of();
        Map<String, List<ObserverEntity>> accreditedByJmbg = reasons.contains(STATUS_ALREADY_ACCREDITED)
                ? observerRepository.findAllWithDetailsByStatusId(STATUS_SUCCESS).stream()
                    .filter(o -> o.getJmbg() != null)
                    .collect(Collectors.groupingBy(ObserverEntity::getJmbg))
                : Map.of();

        Comparator<ObserverEntity> ordering = Comparator
                .comparing((ObserverEntity o) -> o.getStack().getDecisionNumber(), RejectedObserversService::compareNatural)
                .thenComparing(o -> o.getStack().getId())
                .thenComparing(ObserverEntity::getDocumentNumber, Comparator.nullsLast(Comparator.naturalOrder()));

        Map<Integer, StackRejections> result = new LinkedHashMap<>();
        for (ObserverEntity observer : rejected.stream().sorted(ordering).toList()) {
            String details = switch (observer.getStatus().getId()) {
                case STATUS_PRESIDENT -> describePresidents(presidentsByJmbg.get(observer.getJmbg()));
                case STATUS_MEMBER -> describeMembers(membersByJmbg.get(observer.getJmbg()));
                case STATUS_ALREADY_ACCREDITED -> describeAccredited(accreditedByJmbg.get(observer.getJmbg()), observer);
                default -> "";
            };
            result.computeIfAbsent(observer.getStack().getId(), k -> new StackRejections(stackTitle(observer), new ArrayList<>()))
                    .rows().add(new RejectedObserverRow(
                            observer.getDocumentNumber(),
                            observer.getFullName(),
                            observer.getJmbg(),
                            observer.getStatus().getName(),
                            details));
        }
        return new ArrayList<>(result.values());
    }

    /** Compares strings so that numbers inside them are ordered by value ("2/26" before "10/26"); nulls last. */
    private static int compareNatural(String a, String b) {
        if (a == null || b == null)
            return a == b ? 0 : (a == null ? 1 : -1);
        int i = 0, j = 0;
        while (i < a.length() && j < b.length()) {
            if (Character.isDigit(a.charAt(i)) && Character.isDigit(b.charAt(j))) {
                int si = i, sj = j;
                while (i < a.length() && Character.isDigit(a.charAt(i))) i++;
                while (j < b.length() && Character.isDigit(b.charAt(j))) j++;
                int cmp = new java.math.BigInteger(a.substring(si, i)).compareTo(new java.math.BigInteger(b.substring(sj, j)));
                if (cmp != 0)
                    return cmp;
            } else {
                int cmp = Character.compare(a.charAt(i++), b.charAt(j++));
                if (cmp != 0)
                    return cmp;
            }
        }
        return Integer.compare(a.length() - i, b.length() - j);
    }

    private String stackTitle(ObserverEntity observer) {
        return observer.getStack().getDecisionNumber() + " (" + observer.getStack().getPoliticalOrganization().getCode() + ": "
                + observer.getStack().getPoliticalOrganization().getName() + ")";
    }

    private String stackLabel(ObserverEntity observer) {
        String date = observer.getStack().convertDecisionDate();
        return observer.getStack().getDecisionNumber() + (date != null ? " (" + date + ")" : "");
    }

    private String describePresidents(List<PresidentEntity> presidents) {
        if (presidents == null || presidents.isEmpty())
            return NO_DATA;
        return presidents.stream()
                .map(p -> (Boolean.TRUE.equals(p.getIsPresident()) ? "Predsjednik" : "Zamjenik predsjednika")
                        + " biračkog odbora " + councelLabel(p.getVotingCouncel()))
                .collect(Collectors.joining("; "));
    }

    private String describeMembers(List<MemberEntity> members) {
        if (members == null || members.isEmpty())
            return NO_DATA;
        return members.stream()
                .map(m -> "Član biračkog odbora " + councelLabel(m.getConstraint().getVotingCouncel())
                        + ", pozicija " + positionLabel(m.getConstraint().getPoliticalOrganization())
                        + (Boolean.TRUE.equals(m.getIsGik()) ? ", GIK" : ", nije GIK"))
                .collect(Collectors.joining("; "));
    }

    private String describeAccredited(List<ObserverEntity> accredited, ObserverEntity rejected) {
        if (accredited == null)
            return NO_DATA;
        List<ObserverEntity> others = accredited.stream().filter(o -> !Objects.equals(o.getId(), rejected.getId())).toList();
        if (others.isEmpty())
            return NO_DATA;
        return others.stream()
                .map(o -> "PS " + o.getStack().getPoliticalOrganization().getCode() + " - "
                        + o.getStack().getPoliticalOrganization().getName()
                        + ", odluka " + stackLabel(o)
                        + ", redni broj " + o.getDocumentNumber())
                .collect(Collectors.joining("; "));
    }

    // The member's position belongs to the political organization of its constraint
    private String positionLabel(PoliticalOrganizationEntity organization) {
        if (organization == null)
            return NO_DATA;
        return organization.getCode() + " (" + organization.getName() + ")";
    }

    private String councelLabel(VotingCouncelEntity councel) {
        if (councel == null)
            return NO_DATA;
        return councel.getCode() + " (" + councel.getName() + ")";
    }
}
