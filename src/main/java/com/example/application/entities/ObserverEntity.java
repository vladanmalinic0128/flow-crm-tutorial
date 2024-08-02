package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "observer", schema = "voting_system", catalog = "")
public class ObserverEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "lastname")
    private String lastname;
    @Basic
    @Column(name = "firstname")
    private String firstname;
    @Basic
    @Column(name = "jmbg")
    private String jmbg;
    @Basic
    @Column(name = "card_id")
    private String cardId;
    @Basic
    @Column(name = "is_force")
    private Boolean force;
    @Basic
    @Column(name = "document_number")
    private Integer documentNumber;
    @ManyToOne
    @JoinColumn(name = "stack_id", referencedColumnName = "id", nullable = false)
    private StackEntity stack;
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    private StatusEntity status;

    public String getFullName() {
        return firstname + " " + lastname;
    }
}
