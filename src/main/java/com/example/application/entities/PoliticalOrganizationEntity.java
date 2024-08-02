package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "political_organization", schema = "voting_system", catalog = "")
public class PoliticalOrganizationEntity {
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
    @Column(name = "draw_number")
    private Integer drawNumber;
    @OneToMany(mappedBy = "politicalOrganization", fetch = FetchType.LAZY)
    private List<StackEntity> stacks;
    @OneToMany(mappedBy = "politicalOrganization", fetch = FetchType.LAZY)
    private List<ConstraintEntity> constraints;
}
