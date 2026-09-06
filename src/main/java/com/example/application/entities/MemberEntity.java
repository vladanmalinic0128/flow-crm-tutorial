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
    @Column(name = "isAcknowledged")
    private Boolean isAcknowledged;

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

    @Basic
    @Column(name = "price")
    private Integer price;

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

    /**
     * Lombok's default {@code @Data} equals/hashCode would hash every field, including
     * {@link #constraint} - which itself hashes back to this member (see {@link
     * ConstraintEntity#equals}) - causing infinite recursion (StackOverflowError) the moment a
     * MemberEntity is used as a HashMap/HashSet key or Grid item. Restricting to id+jmbg (same
     * pattern as {@link PresidentEntity#equals}) avoids that cycle.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberEntity that = (MemberEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getJmbg(), that.getJmbg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getJmbg());
    }

    public boolean isEmpty() {
        return isForced == null
                && isGik == null
                && isMale == null
                && bankName == null
                && bankNumber == null
                && firstname == null
                && lastname == null
                && phoneNumber == null
                && previousExperience == null
                && qualifications == null;
    }

    public String getFullname() {
        if(this.firstname != null || this.lastname != null)
        return this.firstname + " " + this.lastname;
        else return "";
    }

    /**
     * Falls back to deriving gender from {@link #jmbg} (same scheme as {@link PresidentEntity
     * #getIsMale()}) when it wasn't explicitly set - a JMBG's 10th-12th digits are 000-499 for male,
     * 500-999 for female.
     */
    public Boolean getIsMale() {
        if (isMale != null) {
            return isMale;
        }
        if (jmbg == null || jmbg.length() != 13) {
            return null;
        }
        try {
            int genderIndicator = Integer.parseInt(jmbg.substring(9, 12));
            return genderIndicator <= 499;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
