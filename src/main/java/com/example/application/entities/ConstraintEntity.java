package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "t_constraint", schema = "voting_system", catalog = "")
public class ConstraintEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "position")
    private Integer position;

    @ManyToOne
    @JoinColumn(name = "political_organization_id", referencedColumnName = "id", nullable = false)
    private PoliticalOrganizationEntity politicalOrganization;

    @ManyToOne
    @JoinColumn(name = "voting_councel_id", referencedColumnName = "id", nullable = false)
    private VotingCouncelEntity votingCouncel;

    @ManyToOne
    @JoinColumn(name = "title_id", referencedColumnName = "id", nullable = false)
    private TitleEntity title;

    @OneToOne(mappedBy = "constraint", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberEntity member;

    @Override
    public String toString() {
        return "ConstraintEntity{" +
                "id=" + id +
                ", position=" + position +
                ", title=" + title +
                '}';
    }

    /**
     * See {@link MemberEntity#equals} - Lombok's default {@code @Data} hashCode would recurse
     * through {@link #member}, which hashes back to this constraint, causing a StackOverflowError.
     * Restricting to id breaks that cycle.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstraintEntity that = (ConstraintEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}

