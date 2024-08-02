package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "stack", schema = "voting_system", catalog = "")
public class StackEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "decision_number")
    private String decisionNumber;
    @Basic
    @Column(name = "date")
    private Date date;
    @OneToMany(mappedBy = "stack")
    private List<ObserverEntity> observers;
    @ManyToOne
    @JoinColumn(name = "political_organization_id", referencedColumnName = "id", nullable = false)
    private PoliticalOrganizationEntity politicalOrganization;
}
