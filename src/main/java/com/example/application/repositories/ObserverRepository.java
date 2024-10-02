package com.example.application.repositories;

import com.example.application.entities.ObserverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObserverRepository extends JpaRepository<ObserverEntity, Long> {
    boolean existsByJmbg(String jmbg);
    Optional<ObserverEntity> findByJmbg(String jmbg);
    Optional<ObserverEntity> findByJmbgAndStatus_Id(String jmbg, Integer id);
    Optional<ObserverEntity> findFirstByJmbgAndStatus_Id(String jmbg, Integer id);
}
