package com.example.application.initializers;

import com.example.application.entities.AssociateStatusEntity;
import com.example.application.repositories.AssociateStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AssociateStatusInitializer /*implements ApplicationRunner*/ {
    private final AssociateStatusRepository associateStatusRepository;

    //@Override
    public void run(ApplicationArguments args) throws Exception {
        List<AssociateStatusEntity> statuses = new ArrayList<>();

        // Popunjavanje liste sa podacima
        AssociateStatusEntity status1 = new AssociateStatusEntity();
        status1.setId(1);
        status1.setOrderNumber(1);
        status1.setName("Административно-технички послови");
        statuses.add(status1);

        AssociateStatusEntity status2 = new AssociateStatusEntity();
        status2.setId(2);
        status2.setOrderNumber(2);
        status2.setName("Информатички послови");
        statuses.add(status2);

        AssociateStatusEntity status3 = new AssociateStatusEntity();
        status3.setId(3);
        status3.setOrderNumber(3);
        status3.setName("Послови услужне делатности");
        statuses.add(status3);

        AssociateStatusEntity status4 = new AssociateStatusEntity();
        status4.setId(4);
        status4.setOrderNumber(4);
        status4.setName("Физички послови");
        statuses.add(status4);

        AssociateStatusEntity status5 = new AssociateStatusEntity();
        status5.setId(5);
        status5.setOrderNumber(5);
        status5.setName("Технички послови");
        statuses.add(status5);

        AssociateStatusEntity status6 = new AssociateStatusEntity();
        status6.setId(6);
        status6.setOrderNumber(6);
        status6.setName("Власници приватних објеката");
        statuses.add(status6);

        AssociateStatusEntity status7 = new AssociateStatusEntity();
        status7.setId(7);
        status7.setOrderNumber(7);
        status7.setName("Правни послови");
        statuses.add(status7);

        for(AssociateStatusEntity status: statuses)
            associateStatusRepository.save(status);
    }
}
