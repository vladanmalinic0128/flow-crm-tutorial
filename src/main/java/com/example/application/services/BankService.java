package com.example.application.services;

import com.example.application.repositories.BankRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class BankService {
    private final BankRepository bankRepository;
}
