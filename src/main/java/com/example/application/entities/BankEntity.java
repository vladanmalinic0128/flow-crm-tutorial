package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "bank", schema = "voting_system", catalog = "")
public class BankEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "prefix")
    private String prefix;
}
