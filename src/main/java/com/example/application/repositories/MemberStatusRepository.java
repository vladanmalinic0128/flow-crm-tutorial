package com.example.application.repositories;

import com.example.application.entities.MemberStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberStatusRepository extends JpaRepository<MemberStatusEntity, Long> {
}
