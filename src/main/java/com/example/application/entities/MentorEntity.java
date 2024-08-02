package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "mentor", schema = "voting_system", catalog = "")
public class MentorEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "firstname")
    private String firstname;

    @Basic
    @Column(name = "lastname")
    private String lastname;

    @Basic
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "mentor", fetch = FetchType.LAZY)
    private List<VotingCouncelEntity> votingCouncels;
}

