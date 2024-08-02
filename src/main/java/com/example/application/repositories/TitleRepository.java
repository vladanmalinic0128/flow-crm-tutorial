package com.example.application.repositories;

import com.example.application.entities.ObserverEntity;
import com.example.application.entities.TitleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TitleRepository extends JpaRepository<TitleEntity, Long> {
    Optional<TitleEntity> findById(Long id);
}
