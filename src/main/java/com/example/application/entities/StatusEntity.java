package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "status", schema = "voting_system", catalog = "")
public class StatusEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "success")
    private Boolean success;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "status", fetch = FetchType.EAGER)
    private Collection<ReasonEntity> reasons;
}
