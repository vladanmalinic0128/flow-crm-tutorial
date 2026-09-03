package com.example.application.repositories;

import com.example.application.entities.ObserverEntity;
import com.example.application.entities.VotingCouncelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VotingCouncelRepository extends JpaRepository<VotingCouncelEntity, Long> {
    Optional<VotingCouncelEntity> findByCode(String code);

    // Loads every voting council together with its mentor in a single SQL query.
    @Query("SELECT vc FROM VotingCouncelEntity vc JOIN FETCH vc.mentor")
    List<VotingCouncelEntity> findAllWithMentor();
}
