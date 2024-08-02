package com.example.application.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "reason", schema = "voting_system", catalog = "")
public class ReasonEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "id", insertable = false, updatable = false)
    private StatusEntity status;

    @Override
    public String toString() {
        return "ReasonEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
