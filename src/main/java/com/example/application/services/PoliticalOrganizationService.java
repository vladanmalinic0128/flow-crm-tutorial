package com.example.application.services;

import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.repositories.PoliticalOrganizationRepository;
import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Data
@Service
public class PoliticalOrganizationService {
    private final PoliticalOrganizationRepository politicalOrganizationRepository;

    @Transactional
    public List<PoliticalOrganizationEntity> getAll() {
        Sort sort = Sort.by(Sort.Order.asc("code"));
        return politicalOrganizationRepository.findAll(sort);
    }
}
