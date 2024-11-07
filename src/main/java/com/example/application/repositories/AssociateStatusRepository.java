package com.example.application.repositories;

import com.example.application.entities.AssociateStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssociateStatusRepository extends JpaRepository<AssociateStatusEntity, Long> {
    public AssociateStatusEntity findById(Integer id);
    Optional<AssociateStatusEntity> findByOrderNumber(Integer orderNumber);
}
