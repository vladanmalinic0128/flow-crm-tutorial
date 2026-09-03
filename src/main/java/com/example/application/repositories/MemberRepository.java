package com.example.application.repositories;

import com.example.application.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsByJmbg(String jmbg);
    boolean existsByJmbgAndIsGikFalseOrIsGikIsNull(String jmbg);

    // Loads every member together with constraint -> votingCouncel -> mentor / politicalOrganization / title
    // in a single SQL query (JOIN), instead of letting Hibernate issue a separate SELECT per hop per member.
    @Query("SELECT m FROM MemberEntity m " +
            "JOIN FETCH m.constraint c " +
            "JOIN FETCH c.votingCouncel vc " +
            "JOIN FETCH vc.mentor " +
            "JOIN FETCH c.politicalOrganization " +
            "JOIN FETCH c.title")
    List<MemberEntity> findAllWithConstraintDetails();
}
