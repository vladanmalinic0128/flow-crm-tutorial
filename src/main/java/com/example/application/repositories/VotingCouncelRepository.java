package com.example.application.repositories;

import com.example.application.entities.ObserverEntity;
import com.example.application.entities.VotingCouncelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingCouncelRepository extends JpaRepository<VotingCouncelEntity, Long> {
}
