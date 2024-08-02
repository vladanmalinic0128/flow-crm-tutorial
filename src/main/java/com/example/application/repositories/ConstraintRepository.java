package com.example.application.repositories;

import com.example.application.entities.ConstraintEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstraintRepository extends JpaRepository<ConstraintEntity, Long> {
}
