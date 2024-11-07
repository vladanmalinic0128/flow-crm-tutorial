package com.example.application.entities;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "associateStatus", schema = "voting_system", catalog = "")
public class AssociateStatusEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "order_number")
    private Integer orderNumber;

    @Basic
    @Column(name = "name")
    private String name;
}
