package com.example.application.initializers;

import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.repositories.PoliticalOrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PoliticalOrganizationInitializer2 /*implements ApplicationRunner*/ {
    private final PoliticalOrganizationRepository politicalOrganizationRepository;

//    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[][] organizations = {
                {"03037", "REPUBLIČKA STRANKA SRPSKE - RSS"},
                {"03510", "VOLJA NARODA SRPSKE - DR VLADO ĐAJIĆ"}
        };

        // Initialize and save PoliticalOrganizationEntities
        for (String[] org : organizations) {
            PoliticalOrganizationEntity entity = new PoliticalOrganizationEntity();
            entity.setCode(org[0]);
            entity.setName(org[1]);
            politicalOrganizationRepository.save(entity);
        }
    }
}
