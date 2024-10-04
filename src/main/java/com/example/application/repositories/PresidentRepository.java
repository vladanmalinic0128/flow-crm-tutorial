package com.example.application.repositories;

import com.example.application.entities.PresidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PresidentRepository extends JpaRepository<PresidentEntity, Long> {
    boolean existsByVotingCouncel_Code(String code);
    Optional<PresidentEntity> findByVotingCouncel_CodeAndIsPresident(String code, boolean isPresident);

    boolean existsByJmbg(String jmbg);
    Optional<PresidentEntity> findByJmbg(String jmbg);
}
