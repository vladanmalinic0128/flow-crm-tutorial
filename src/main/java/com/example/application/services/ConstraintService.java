package com.example.application.services;

import com.example.application.repositories.ConstraintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConstraintService {
    private final ConstraintRepository constraintRepository;
}
