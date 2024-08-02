package com.example.application.repositories;

import com.example.application.entities.MentorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorRepository extends JpaRepository<MentorEntity, Long> {
}
