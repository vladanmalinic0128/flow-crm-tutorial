package com.example.application.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "associate", schema = "voting_system", catalog = "")
public class AssociateEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "order_number")
    private Integer orderNumber;

    @Basic
    @Column(name = "jmbg")
    private String jmbg;

    @Basic
    @Column(name = "firstname")
    private String firstname;

    @Basic
    @Column(name = "lastname")
    private String lastname;

    @Basic
    @Column(name = "residence")
    private String residence;

    @Basic
    @Column(name = "bankNumber")
    private String bankNumber;

    @Basic
    @Column(name = "bankName")
    private String bankName;

    @Basic
    @Column(name = "price")
    private Double price;

    @Basic
    @Column(name = "contract_date")
    private Date contractDate;

    @Basic
    @Column(name = "report_date")
    private Date reportDate;

    @Basic
    @Column(name = "start_date")
    private Date startDate;

    @Basic
    @Column(name = "end_date")
    private Date endDate;

    @Basic
    @Column(name = "isExtern")
    private Boolean isExtern;

    @ManyToOne
    @JoinColumn(name = "associate_status_id", referencedColumnName = "id", nullable = false)
    private AssociateStatusEntity status;
}
