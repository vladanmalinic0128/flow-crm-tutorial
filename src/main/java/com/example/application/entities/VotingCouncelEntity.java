package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "voting_councel", schema = "voting_system", catalog = "")
public class VotingCouncelEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "code")
    private String code;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "location")
    private String location;

    @Basic
    @Column(name = "number_of_voters")
    private Integer numberOfVoters;

    @Basic
    @Column(name = "number_of_members")
    private Integer numberOfMembers;

    @OneToMany(mappedBy = "votingCouncel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConstraintEntity> constraints = new ArrayList<>();

    @OneToMany(mappedBy = "votingCouncel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PresidentEntity> presidents = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "mentor_id", referencedColumnName = "id", nullable = false)
    private MentorEntity mentor;

    /**
     * The voting council this one is attached to, if any (e.g. a personal-voting or "LIČNO" council
     * conducted at another council's location). Null for a standalone/primary council.
     */
    @ManyToOne
    @JoinColumn(name = "primary_voting_councel_id", referencedColumnName = "id")
    private VotingCouncelEntity primaryVotingCouncel;

    /** Voting councils attached to this one - see {@link #primaryVotingCouncel}. */
    @OneToMany(mappedBy = "primaryVotingCouncel")
    private List<VotingCouncelEntity> attachedVotingCouncels = new ArrayList<>();

    /**
     * Number of voters for this council plus any attached to it (e.g. a "LIČNO" council conducted
     * at the same location) - the figure that should appear wherever the voter count for this
     * council is officially reported.
     */
    public int getTotalNumberOfVoters() {
        int total = numberOfVoters != null ? numberOfVoters : 0;
        for (VotingCouncelEntity attached : attachedVotingCouncels) {
            Integer attachedVoters = attached.getNumberOfVoters();
            if (attachedVoters != null) {
                total += attachedVoters;
            }
        }
        return total;
    }

    /**
     * This council's name, combined with the names of any attached councils (e.g.
     * "ЦЕНТАР 2-1 / ЛИЧНО") - the label that should appear wherever this council is officially
     * identified by name.
     */
    public String getDisplayName() {
        String display = name;
        for (VotingCouncelEntity attached : attachedVotingCouncels) {
            display += " / " + attached.getName();
        }
        return display;
    }

    @Override
    public String toString() {
        return "VotingCouncelEntity{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", numberOfVoters=" + numberOfVoters +
                ", numberOfMembers=" + numberOfMembers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VotingCouncelEntity that = (VotingCouncelEntity) o;
        return getId().equals(that.getId()) && getCode().equals(that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCode());
    }
}

