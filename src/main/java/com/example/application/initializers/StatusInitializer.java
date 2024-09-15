package com.example.application.initializers;

import com.example.application.entities.StatusEntity;
import com.example.application.repositories.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatusInitializer /*implements ApplicationRunner*/ {
    private final StatusRepository statusRepository;

    //@Override
    public void run(ApplicationArguments args) throws Exception {
        List<StatusEntity> statuses = new ArrayList<>();

        // Successful statuses with a null message
        StatusEntity successStatus = new StatusEntity();
        successStatus.setId(1);
        successStatus.setSuccess(true);
        successStatus.setName(null);
        statuses.add(successStatus);

        StatusEntity failureStatus0 = new StatusEntity();
        failureStatus0.setId(2);
        failureStatus0.setSuccess(false);
        failureStatus0.setName("JMBG nije validan");
        statuses.add(failureStatus0);

        StatusEntity failureStatus1 = new StatusEntity();
        failureStatus1.setId(3);
        failureStatus1.setSuccess(false);
        failureStatus1.setName("Osoba je već akreditovana kao posmatrač");
        statuses.add(failureStatus1);

        StatusEntity failureStatus2 = new StatusEntity();
        failureStatus2.setId(4);
        failureStatus2.setSuccess(false);
        failureStatus2.setName("Osoba je akreditovana kao član biračkog odbora");
        statuses.add(failureStatus2);

        StatusEntity failureStatus4 = new StatusEntity();
        failureStatus4.setId(5);
        failureStatus4.setSuccess(false);
        failureStatus4.setName("Nije unesen broj dokumenta");
        statuses.add(failureStatus4);

        StatusEntity failureStatus5 = new StatusEntity();
        failureStatus5.setId(6);
        failureStatus5.setSuccess(false);
        failureStatus5.setName("Nije uneseno ime");
        statuses.add(failureStatus5);

        StatusEntity failureStatus6 = new StatusEntity();
        failureStatus6.setId(7);
        failureStatus6.setSuccess(false);
        failureStatus6.setName("Nije uneseno prezime");
        statuses.add(failureStatus6);

        StatusEntity failureStatus7 = new StatusEntity();
        failureStatus7.setId(8);
        failureStatus7.setSuccess(false);
        failureStatus7.setName("Nije unesen redni broj u dokumentu");
        statuses.add(failureStatus7);

        StatusEntity failureStatus8 = new StatusEntity();
        failureStatus8.setId(9);
        failureStatus8.setSuccess(false);
        failureStatus8.setName("Osoba je akreditovana kao predsjednik biračkog odbora");
        statuses.add(failureStatus8);

        for(StatusEntity status: statuses)
            statusRepository.save(status);

    }
}
