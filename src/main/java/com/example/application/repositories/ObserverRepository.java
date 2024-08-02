package com.example.application.repositories;

import com.example.application.entities.ObserverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObserverRepository extends JpaRepository<ObserverEntity, Long> {
    boolean existsByJmbg(String jmbg);
}
