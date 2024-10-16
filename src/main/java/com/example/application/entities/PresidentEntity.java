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

    @Basic
    @Column(name = "price")
    private Integer price;

    @Basic
    @Column(name = "isAcknowledged")
    private Boolean isAcknowledged;

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

    public MemberEntity mapToMemberEntity() {
        MemberEntity member = new MemberEntity();
        member.setJmbg(this.jmbg);
        member.setFirstname(this.firstname);
        member.setLastname(this.lastname);
        member.setPhoneNumber(this.phoneNumber);
        member.setBankNumber(this.bankNumber);
        member.setBankName(this.bankName);
        member.setIsForced(false);
        member.setIsMale(null);
        member.setPrice(0);

        return member;
    }

    public Boolean getIsMale() {
        if (jmbg == null || jmbg.length() != 13) {
            return null;
        }

        int genderIndicator = Integer.parseInt(jmbg.substring(9, 12));

        if (genderIndicator >= 0 && genderIndicator <= 499) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return bankName == null
                && bankNumber == null
                && firstname == null
                && lastname == null
                && phoneNumber == null;
    }
}
