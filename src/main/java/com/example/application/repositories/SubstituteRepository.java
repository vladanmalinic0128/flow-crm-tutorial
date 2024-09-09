package com.example.application.repositories;

import com.example.application.entities.SubstituteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubstituteRepository extends JpaRepository<SubstituteEntity, Long> {
    Optional<SubstituteEntity> findFirstByJmbg(String jmbg);

}
