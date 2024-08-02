package com.example.application.services;

import com.example.application.repositories.VotingCouncelRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class VotingCouncelService {
    private final VotingCouncelRepository votingCouncelRepository;
}
