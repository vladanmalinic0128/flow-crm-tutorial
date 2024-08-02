package com.example.application.repositories;

import com.example.application.entities.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<StatusEntity, Long> {
    public StatusEntity findById(Integer id);
}
