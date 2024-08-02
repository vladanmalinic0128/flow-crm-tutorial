package com.example.application.services;

import com.example.application.entities.ObserverEntity;
import com.example.application.entities.StackEntity;
import com.example.application.repositories.ObserverRepository;
import com.example.application.repositories.StackRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Data
@Service
public class StackService {
    private final StackRepository stackRepository;
    private final ObserverRepository observerRepository;

    @Transactional
    public void deleteStack(StackEntity entity) {
        for(ObserverEntity observer: entity.getObservers()) {
            System.out.println("Id: " + observer.getId());
            observerRepository.delete(observer);
        }
        stackRepository.delete(entity);
    }
}
