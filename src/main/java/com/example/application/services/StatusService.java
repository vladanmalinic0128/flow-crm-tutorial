package com.example.application.services;

import com.example.application.repositories.StatusRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class StatusService {
    private final StatusRepository statusRepository;
}
