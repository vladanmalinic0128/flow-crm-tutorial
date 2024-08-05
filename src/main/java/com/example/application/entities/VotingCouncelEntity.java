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

    @ManyToOne
    @JoinColumn(name = "mentor_id", referencedColumnName = "id", nullable = false)
    private MentorEntity mentor;

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

