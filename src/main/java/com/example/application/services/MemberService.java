package com.example.application.services;

import com.example.application.repositories.MemberRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class MemberService {
    private final MemberRepository memberRepository;
}
