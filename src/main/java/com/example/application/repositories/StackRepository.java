package com.example.application.repositories;

import com.example.application.entities.StackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StackRepository extends JpaRepository<StackEntity, Long> {
    List<StackEntity> findAllByPoliticalOrganization_Id(Long id);
}
