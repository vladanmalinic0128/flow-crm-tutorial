package com.example.application.repositories;

import com.example.application.entities.ObserverEntity;
import com.example.application.entities.StackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObserverRepository extends JpaRepository<ObserverEntity, Long> {
    boolean existsByJmbg(String jmbg);
    Optional<ObserverEntity> findByJmbg(String jmbg);
    Optional<ObserverEntity> findByJmbgAndStatus_Id(String jmbg, Integer id);
    Optional<ObserverEntity> findFirstByJmbgAndStatus_Id(String jmbg, Integer id);
    Optional<ObserverEntity> findFirstByJmbg(String jmbg);
    void deleteByStack(StackEntity stack);

    // Loads every observer with a given status, together with stack -> politicalOrganization and status,
    // in a single SQL query - used to build an in-memory jmbg lookup instead of one query per member.
    @Query("SELECT o FROM ObserverEntity o " +
            "JOIN FETCH o.stack s " +
            "JOIN FETCH s.politicalOrganization " +
            "JOIN FETCH o.status " +
            "WHERE o.status.id = :statusId")
    List<ObserverEntity> findAllWithDetailsByStatusId(@Param("statusId") Integer statusId);
}
