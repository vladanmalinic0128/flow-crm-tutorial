package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "member", schema = "voting_system", catalog = "")
public class MemberEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

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
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Basic
    @Column(name = "isForced")
    private Boolean isForced;

    @Basic
    @Column(name = "isGik")
    private Boolean isGik;

    @Basic
    @Column(name = "isMale")
    private Boolean isMale;

    @Basic
    @Column(name = "qualifications")
    private String qualifications;

    @Basic
    @Column(name = "previousExperience")
    private String previousExperience;

    @Basic
    @Column(name = "bankNumber")
    private String bankNumber;

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "id", nullable = false)
    private MemberStatusEntity memberStatus;

    @ManyToOne
    @JoinColumn(name = "constraint_id", referencedColumnName = "id", nullable = false)
    private ConstraintEntity constraint;

    @Override
    public String toString() {
        return "MemberEntity{" +
                "id=" + id +
                ", jmbg='" + jmbg + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isForced=" + isForced +
                ", isGik=" + isGik +
                ", isMale=" + isMale +
                ", qualifications='" + qualifications + '\'' +
                ", previousExperience='" + previousExperience + '\'' +
                ", bankNumber='" + bankNumber + '\'' +
                '}';
    }
}
