package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "title", schema = "voting_system", catalog = "")
public class TitleEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "title", fetch = FetchType.LAZY)
    private List<ConstraintEntity> constraints;
}
