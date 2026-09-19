package com.example.application.repositories;

import com.example.application.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsByJmbg(String jmbg);

    // Derived-name equivalent parses as (jmbg=? AND isGik=false) OR isGik IS NULL, which is
    // table-wide and ignores jmbg for the null case - isGik must be scoped to the matching row.
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MemberEntity m " +
            "WHERE m.jmbg = :jmbg AND (m.isGik = false OR m.isGik IS NULL)")
    boolean existsByJmbgAndIsGikFalseOrIsGikIsNull(@Param("jmbg") String jmbg);

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
