package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "member_status", schema = "voting_system", catalog = "")
public class MemberStatusEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "success")
    private Boolean success;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "memberStatus")
    private List<MemberEntity> members;

    @Override
    public String toString() {
        return "MemberStatusEntity{" +
                "id=" + id +
                ", success=" + success +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
