package com.example.application.services;

import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.repositories.PoliticalOrganizationRepository;
import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class PoliticalOrganizationService {
    private final PoliticalOrganizationRepository politicalOrganizationRepository;

    @Transactional
    public List<PoliticalOrganizationEntity> getAll() {
        Sort sort = Sort.by(Sort.Order.asc("code"));
        return politicalOrganizationRepository.findAll(sort);
    }

    public List<PoliticalOrganizationEntity> getAllDrawed() {
        return politicalOrganizationRepository.findAll()
                .stream()
                .filter(po -> po.getDrawNumber() != null && po.getDrawNumber() > 0)
                .sorted(Comparator.comparing(PoliticalOrganizationEntity::getCode))
                .collect(Collectors.toList());
    }
}
