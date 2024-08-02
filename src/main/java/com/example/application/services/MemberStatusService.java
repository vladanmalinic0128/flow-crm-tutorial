package com.example.application.services;

import com.example.application.repositories.MemberStatusRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class MemberStatusService {
    private final MemberStatusRepository memberStatusRepository;
}
