package com.example.application.services;

import com.example.application.repositories.ObserverRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class ObserverService {
    private final ObserverRepository observerRepository;
}
