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

    @OneToMany(mappedBy = "constraint", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberEntity> members = new ArrayList<>();
}

