package com.example.application.initializers;

import com.example.application.entities.BankEntity;
import com.example.application.repositories.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankInitializer  /*implements ApplicationRunner*/ {
    private final BankRepository bankRepository;

    //@Override
    public void run(ApplicationArguments args) throws Exception {
        bankRepository.save(new BankEntity(null, "Addiko bank","13", "552"));
        bankRepository.save(new BankEntity(null, "Atos banka", "8", "567"));
        bankRepository.save(new BankEntity(null, "Nova banka", "47", "555"));
        bankRepository.save(new BankEntity(null, "Unicredit bank BL", "7", "551"));
        bankRepository.save(new BankEntity(null, "Intesa Sanpaolo bank", "17","154"));
        bankRepository.save(new BankEntity(null, "NLB", "6", "562"));
        bankRepository.save(new BankEntity(null, "Raiffeisen bank", "9", "161"));
        bankRepository.save(new BankEntity(null, "Unicredit bank dd Mostar", "5", "338"));
        bankRepository.save(new BankEntity(null, "Banka Poštanska Štedionica", "20", "571"));
        bankRepository.save(new BankEntity(null, "Procredit bank", "23", "194"));
        bankRepository.save(new BankEntity(null, "Sparkasse bank", "38", "199"));
        bankRepository.save(new BankEntity(null, "Naša banka", "47", "554"));
        bankRepository.save(new BankEntity(null, "MF banka", "39", "572"));
        bankRepository.save(new BankEntity(null, "Ziraat bank", "49", "186"));
        bankRepository.save(new BankEntity(null, "BBI", "2821", "141"));
    }
}
