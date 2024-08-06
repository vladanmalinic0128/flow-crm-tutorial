package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
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

    @Basic
    @Column(name = "bankName")
    private String bankName;

    @OneToOne
    @JoinColumn(name = "constraint_id", referencedColumnName = "id", nullable = false)
    private ConstraintEntity constraint;

    @ManyToMany
    @JoinTable(
            name = "member_status_join",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "status_id")
    )
    private List<MemberStatusEntity> statuses = new ArrayList<>();

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

    public boolean isEmpty() {
        return isForced == null
                && isGik == null
                && isMale == null
                && bankName == null
                && bankName == null
                && bankNumber == null
                && firstname == null
                && lastname == null
                && phoneNumber == null
                && previousExperience == null
                && qualifications == null;
    }
}
