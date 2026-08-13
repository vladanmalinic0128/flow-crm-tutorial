package com.example.application.initializers;

import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.repositories.PoliticalOrganizationRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PoliticalOrganizationInitializer /*implements ApplicationRunner*/ {
    private final PoliticalOrganizationRepository politicalOrganizationRepository;

//    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[][] organizations = {
                {"00000", "GRADSKA IZBORNA KOMISIJA"},
                {"00004", "SBIH - STRANKA ZA BOSNU I HERCEGOVINU"},
                {"00008", "SDP - SOCIJALDEMOKRATSKA PARTIJA BOSNE I HERCEGOVINE"},
                {"00018", "SRPSKA DEMOKRATSKA STRANKA"},
                {"00027", "HDZ BIH-HRVATSKA DEMOKRATSKA ZAJEDNICA BOSNE I HERCEGOVINE"},
                {"00036", "MIRNES AJANOVIĆ - BOSS - BOSANSKA STRANKA"},
                {"00074", "SOCIJALISTIČKA PARTIJA"},
                {"00090", "SDA - STRANKA DEMOKRATSKE AKCIJE"},
                {"00440", "PDP - PARTIJA DEMOKRATSKOG PROGRESA"},
                {"00515", "SAVEZ NEZAVISNIH SOCIJALDEMOKRATA - SNSD - MILORAD DODIK"},
                {"00877", "NAŠA STRANKA"},
                {"01290", "HRVATSKA SELJAČKA STRANKA – HSS"},
                {"01702", "ZA PRAVDU I RED - LISTA NEBOJŠE VUKANOVIĆA"},
                {"01706", "SAVEZ ZA NOVU POLITIKU - SNP"},
                {"01718", "NARODNI DEMOKRATSKI POKRET"},
                {"01730", "SRS - SRPSKA RADIKALNA STRANKA"},
                {"01972", "UJEDINJENA SRPSKA"},
                {"02311", "PRVA SRPSKA DEMOKRATSKA STRANKA"},
                {"02320", "PLATFORMA ZA PROGRES"},
                {"02420", "DEMOKRATSKI SAVEZ-DEMOS"},
                {"02447", "SPS - SOCIJALISTIČKA PARTIJA SRPSKE - GORAN SELAK"},
                {"02460", "NARODNI POKRET BANJALUKA ZOVE 18"},
                {"02734", "SRPSKA DEMOKRATSKA STRANKA - VOLJA NARODA"},
                {"03035", "NARODNA PARTIJA SRPSKE-DARKO BANJAC"},
                {"03044", "BOSANSKOHERCEGOVAČKA INICIJATIVA - KASUMOVIĆ FUAD"},
                {"03045", "STRANKA ŽIVOT"},
                {"03068", "ŽELJKO KOMŠIĆ - ZA GRAĐANSKU DRŽAVU - DF/GS"},
                {"03122", "NARODNI FRONT - JELENA TRIVIĆ"},
                {"03187", "DAVOR DRAGIČEVIĆ - NEZAVISNI KANDIDAT"},
                {"03191", "SNAGA NARODA"},
                {"03210", "DAVOR JERILOVIĆ - NEZAVISNI KANDIDAT"},
                {"03218", "NEZAVISNI POKRET \"SVOЈIM PUTEM\" - IGOR RADOЈIČIĆ"},
                {"03291", "DRAGAN ŠAJIĆ - NEZAVISNI KANDIDAT"},
                {"03297", "SAŠA ČUDIĆ - NEZAVISNI KANDIDAT"},
                {"03306", "HOĆEMO EKO BUDUĆNOST"},
                {"03359", "IGOR JOVANOVIĆ - NEZAVISNI KANDIDAT"},
                {"03368", "DRAGAN JOKIĆ - NEZAVISNI KANDIDAT"},
                {"03493", "DNS - NENAD NEŠIĆ - PARTIJA UJEDINJENIH PENZIONERA (PUP)"},
                {"03529", "DNS-NPS BANJAC-NEŠIĆ"},
                {"03532", "NES BIH, PDA, NAPRIJED! - ZA EVROPSKU BIH ZAJEDNO"},
                {"03534", "ZA PRAVDU I RED-LISTA NEBOЈŠE VUKANOVIĆA-PDP RS-IGOR CRNADAK"},
                {"03535", "DRAŠKO STANIVUKOVIĆ - POKRET SIGURNA SRPSKA (PSS,PDP,NPSP..)"},
                {"03542", "HDZ BIH, HSP BIH, HKDU, HSP AS, HDU, HSP HB, HRAST"},
                {"03546", "SPS - GORAN SELAK - POKRET ЈEDINSTVENA SRPSKA"},
                {"03548", "KOALICIJA ZA DRŽAVU"},
                {"03552", "NAROD I PRAVDA, SDBIH"},
                {"03553", "SOCIЈALISTIČKA PARTIЈA PETAR ĐOKIĆ - DEMOS - NDP"},
                {"03555", "HRVATSKA PETORKA HDZ 1990, HRS, HNP, HDS I HSS"}
        };

        // Initialize and save PoliticalOrganizationEntities
        for (String[] org : organizations) {
            PoliticalOrganizationEntity entity = new PoliticalOrganizationEntity();
            entity.setCode(org[0]);
            entity.setName(org[1]);
            politicalOrganizationRepository.save(entity);
        }
    }
}
