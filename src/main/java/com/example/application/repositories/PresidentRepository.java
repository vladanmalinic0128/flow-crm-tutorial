package com.example.application.repositories;

import com.example.application.entities.PresidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PresidentRepository extends JpaRepository<PresidentEntity, Long> {
    boolean existsByVotingCouncel_Code(String code);
    Optional<PresidentEntity> findByVotingCouncel_CodeAndIsPresident(String code, boolean isPresident);

    boolean existsByJmbg(String jmbg);
    Optional<PresidentEntity> findByJmbg(String jmbg);

    // Loads every president together with votingCouncel -> mentor in a single SQL query.
    @Query("SELECT p FROM PresidentEntity p JOIN FETCH p.votingCouncel vc JOIN FETCH vc.mentor")
    List<PresidentEntity> findAllWithVotingCouncelDetails();
}
