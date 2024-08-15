package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "substitute", schema = "voting_system", catalog = "")
public class SubstituteEntity {
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
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Basic
    @Column(name = "isMale")
    private Boolean isMale;

    @Basic
    @Column(name = "qualifications")
    private String qualifications;

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
    @JoinColumn(name = "mentor_id", referencedColumnName = "id", nullable = false)
    private MentorEntity mentor;

    public boolean isEmpty() {
        return isMale == null
                && bankName == null
                && bankNumber == null
                && firstname == null
                && lastname == null
                && phoneNumber == null
                && qualifications == null;
    }

    public String getFullname() {
        return this.firstname + " " + this.lastname;
    }

    @Override
    public String toString() {
        return "MemberEntity{" +
                "id=" + id +
                ", jmbg='" + jmbg + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isMale=" + isMale +
                ", qualifications='" + qualifications + '\'' +
                ", bankNumber='" + bankNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubstituteEntity entity = (SubstituteEntity) o;
        return Objects.equals(getJmbg(), entity.getJmbg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJmbg());
    }
}
