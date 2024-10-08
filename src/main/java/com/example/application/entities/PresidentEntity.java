package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "president", schema = "voting_system", catalog = "")
public class PresidentEntity {
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
    @Column(name = "bankNumber")
    private String bankNumber;

    @Basic
    @Column(name = "bankName")
    private String bankName;

    @Basic
    @Column(name = "isPresident")
    private Boolean isPresident;

    @ManyToOne
    @JoinColumn(name = "voting_councel_id", referencedColumnName = "id")
    private VotingCouncelEntity votingCouncel;


    @Override
    public String toString() {
        return "PresidentEntity{" +
                "id=" + id +
                ", jmbg='" + jmbg + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", bankNumber='" + bankNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PresidentEntity that = (PresidentEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getJmbg(), that.getJmbg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getJmbg());
    }

    public String getFullname() {
        return this.firstname + " " + this.lastname;
    }
}
