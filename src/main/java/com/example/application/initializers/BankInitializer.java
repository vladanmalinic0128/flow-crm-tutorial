package com.example.application.initializers;

import com.example.application.entities.BankEntity;
import com.example.application.repositories.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankInitializer  implements ApplicationRunner {
    private final BankRepository bankRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        bankRepository.save(new BankEntity(null, "Adiko banka Bluka", "552"));
        bankRepository.save(new BankEntity(null, "Atos banka Bluka", "567"));
        bankRepository.save(new BankEntity(null, "Nova banka", "555"));
        bankRepository.save(new BankEntity(null, "Unicredit bank BL", "551"));
        bankRepository.save(new BankEntity(null, "Intesa Sanpaolo bank", "154"));
        bankRepository.save(new BankEntity(null, "NLB", "562"));
        bankRepository.save(new BankEntity(null, "Raiffeisen bank", "161"));
        bankRepository.save(new BankEntity(null, "Unicredit bank dd Mostar", "338"));
        bankRepository.save(new BankEntity(null, "Banka Poštanska Štedionica", "571"));
        bankRepository.save(new BankEntity(null, "Procredit bank", "194"));
        bankRepository.save(new BankEntity(null, "Sparkasse bank", "199"));
        bankRepository.save(new BankEntity(null, "Naša banka", "554"));
        bankRepository.save(new BankEntity(null, "MF banka", "572"));
        bankRepository.save(new BankEntity(null, "Ziraat bank", "186"));
        bankRepository.save(new BankEntity(null, "BBI", "141"));
    }
}
