package com.example.application.repositories;

import com.example.application.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsByJmbg(String jmbg);
    boolean existsByJmbgAndIsGikFalseOrIsGikIsNull(String jmbg);

}
