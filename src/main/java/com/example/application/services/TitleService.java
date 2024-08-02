package com.example.application.services;

import com.example.application.repositories.TitleRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class TitleService {
    private final TitleRepository titleRepository;
}
