package com.example.application.repositories;

import com.example.application.entities.AssociateEntity;
import com.example.application.entities.AssociateStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssociateRepository extends JpaRepository<AssociateEntity, Long> {
    public AssociateEntity findById(Integer id);
    public List<AssociateEntity> findAllByIsExtern(boolean isExtern);
}
