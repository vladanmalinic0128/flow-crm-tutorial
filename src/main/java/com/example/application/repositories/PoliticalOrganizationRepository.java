package com.example.application.repositories;

import com.example.application.entities.PoliticalOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoliticalOrganizationRepository extends JpaRepository<PoliticalOrganizationEntity, Long> {
}
