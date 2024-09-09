package com.example.application.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CollaboratorsXlsxService {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";
    private final CyrillicToLatinConverter cyrillicToLatinConverter;
    private final LatinToCyrillicConverter latinToCyrillicConverter;
}
