package com.example.application.initializers;

import com.example.application.entities.MentorEntity;
import com.example.application.entities.VotingCouncelEntity;
import com.example.application.repositories.MentorRepository;
import com.example.application.repositories.TitleRepository;
import com.example.application.repositories.VotingCouncelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class MobileTeamsInitializer implements ApplicationRunner {
    private final MentorRepository mentorRepository;
    private final VotingCouncelRepository votingCouncelRepository;
    private final TitleRepository titleRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Random random = new Random();

        Optional<MentorEntity> mentor = mentorRepository.findById(5L);
        if(mentor.isEmpty())
            throw new RuntimeException("Njie dodijeljen mentor za mobilne timove");

        MentorEntity leona = mentor.get();

        VotingCouncelEntity votingCouncelSpecialTeam001 = new VotingCouncelEntity();
        votingCouncelSpecialTeam001.setCode("034МТ001");
        votingCouncelSpecialTeam001.setName("МОБИЛНИ ТИМ I");
        votingCouncelSpecialTeam001.setLocation(null);
        votingCouncelSpecialTeam001.setNumberOfMembers(2);
        votingCouncelSpecialTeam001.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam001.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam001);

        VotingCouncelEntity votingCouncelSpecialTeam002 = new VotingCouncelEntity();
        votingCouncelSpecialTeam002.setCode("034МТ002");
        votingCouncelSpecialTeam002.setName("МОБИЛНИ ТИМ II");
        votingCouncelSpecialTeam002.setLocation(null);
        votingCouncelSpecialTeam002.setNumberOfMembers(2);
        votingCouncelSpecialTeam002.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam002.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam002);

        VotingCouncelEntity votingCouncelSpecialTeam003 = new VotingCouncelEntity();
        votingCouncelSpecialTeam003.setCode("034МТ003");
        votingCouncelSpecialTeam003.setName("МОБИЛНИ ТИМ III");
        votingCouncelSpecialTeam003.setLocation(null);
        votingCouncelSpecialTeam003.setNumberOfMembers(2);
        votingCouncelSpecialTeam003.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam003.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam003);

        VotingCouncelEntity votingCouncelSpecialTeam004 = new VotingCouncelEntity();
        votingCouncelSpecialTeam004.setCode("034МТ004");
        votingCouncelSpecialTeam004.setName("МОБИЛНИ ТИМ IV");
        votingCouncelSpecialTeam004.setLocation(null);
        votingCouncelSpecialTeam004.setNumberOfMembers(2);
        votingCouncelSpecialTeam004.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam004.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam004);

        VotingCouncelEntity votingCouncelSpecialTeam005 = new VotingCouncelEntity();
        votingCouncelSpecialTeam005.setCode("034МТ005");
        votingCouncelSpecialTeam005.setName("МОБИЛНИ ТИМ V");
        votingCouncelSpecialTeam005.setLocation(null);
        votingCouncelSpecialTeam005.setNumberOfMembers(2);
        votingCouncelSpecialTeam005.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam005.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam005);

        VotingCouncelEntity votingCouncelSpecialTeam006 = new VotingCouncelEntity();
        votingCouncelSpecialTeam006.setCode("034МТ006");
        votingCouncelSpecialTeam006.setName("МОБИЛНИ ТИМ VI");
        votingCouncelSpecialTeam006.setLocation(null);
        votingCouncelSpecialTeam006.setNumberOfMembers(2);
        votingCouncelSpecialTeam006.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam006.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam006);

        VotingCouncelEntity votingCouncelSpecialTeam007 = new VotingCouncelEntity();
        votingCouncelSpecialTeam007.setCode("034МТ007");
        votingCouncelSpecialTeam007.setName("МОБИЛНИ ТИМ VII");
        votingCouncelSpecialTeam007.setLocation(null);
        votingCouncelSpecialTeam007.setNumberOfMembers(2);
        votingCouncelSpecialTeam007.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam007.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam007);

        VotingCouncelEntity votingCouncelSpecialTeam008 = new VotingCouncelEntity();
        votingCouncelSpecialTeam008.setCode("034МТ008");
        votingCouncelSpecialTeam008.setName("МОБИЛНИ ТИМ VIII");
        votingCouncelSpecialTeam008.setLocation(null);
        votingCouncelSpecialTeam008.setNumberOfMembers(2);
        votingCouncelSpecialTeam008.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam008.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam008);

        VotingCouncelEntity votingCouncelSpecialTeam009 = new VotingCouncelEntity();
        votingCouncelSpecialTeam009.setCode("034МТ009");
        votingCouncelSpecialTeam009.setName("МОБИЛНИ ТИМ IX");
        votingCouncelSpecialTeam009.setLocation(null);
        votingCouncelSpecialTeam009.setNumberOfMembers(2);
        votingCouncelSpecialTeam009.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam009.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam009);

        VotingCouncelEntity votingCouncelSpecialTeam010 = new VotingCouncelEntity();
        votingCouncelSpecialTeam010.setCode("034МТ010");
        votingCouncelSpecialTeam010.setName("МОБИЛНИ ТИМ X");
        votingCouncelSpecialTeam010.setLocation(null);
        votingCouncelSpecialTeam010.setNumberOfMembers(2);
        votingCouncelSpecialTeam010.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam010.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam010);

        VotingCouncelEntity votingCouncelSpecialTeam011 = new VotingCouncelEntity();
        votingCouncelSpecialTeam011.setCode("034МТ011");
        votingCouncelSpecialTeam011.setName("МОБИЛНИ ТИМ XI");
        votingCouncelSpecialTeam011.setLocation(null);
        votingCouncelSpecialTeam011.setNumberOfMembers(2);
        votingCouncelSpecialTeam011.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam011.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam011);

        VotingCouncelEntity votingCouncelSpecialTeam012 = new VotingCouncelEntity();
        votingCouncelSpecialTeam012.setCode("034МТ012");
        votingCouncelSpecialTeam012.setName("МОБИЛНИ ТИМ XII");
        votingCouncelSpecialTeam012.setLocation(null);
        votingCouncelSpecialTeam012.setNumberOfMembers(2);
        votingCouncelSpecialTeam012.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam012.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam012);

        VotingCouncelEntity votingCouncelSpecialTeam013 = new VotingCouncelEntity();
        votingCouncelSpecialTeam013.setCode("034МТ013");
        votingCouncelSpecialTeam013.setName("МОБИЛНИ ТИМ XIII");
        votingCouncelSpecialTeam013.setLocation(null);
        votingCouncelSpecialTeam013.setNumberOfMembers(2);
        votingCouncelSpecialTeam013.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam013.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam013);

        VotingCouncelEntity votingCouncelSpecialTeam014 = new VotingCouncelEntity();
        votingCouncelSpecialTeam014.setCode("034МТ014");
        votingCouncelSpecialTeam014.setName("МОБИЛНИ ТИМ XIV");
        votingCouncelSpecialTeam014.setLocation(null);
        votingCouncelSpecialTeam014.setNumberOfMembers(2);
        votingCouncelSpecialTeam014.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam014.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam014);

        VotingCouncelEntity votingCouncelSpecialTeam015 = new VotingCouncelEntity();
        votingCouncelSpecialTeam015.setCode("034МТ015");
        votingCouncelSpecialTeam015.setName("МОБИЛНИ ТИМ XV");
        votingCouncelSpecialTeam015.setLocation(null);
        votingCouncelSpecialTeam015.setNumberOfMembers(2);
        votingCouncelSpecialTeam015.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam015.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam015);

        VotingCouncelEntity votingCouncelSpecialTeam016 = new VotingCouncelEntity();
        votingCouncelSpecialTeam016.setCode("034МТ016");
        votingCouncelSpecialTeam016.setName("МОБИЛНИ ТИМ XVI");
        votingCouncelSpecialTeam016.setLocation(null);
        votingCouncelSpecialTeam016.setNumberOfMembers(2);
        votingCouncelSpecialTeam016.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam016.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam016);

        VotingCouncelEntity votingCouncelSpecialTeam017 = new VotingCouncelEntity();
        votingCouncelSpecialTeam017.setCode("034МТ017");
        votingCouncelSpecialTeam017.setName("МОБИЛНИ ТИМ XVII");
        votingCouncelSpecialTeam017.setLocation(null);
        votingCouncelSpecialTeam017.setNumberOfMembers(2);
        votingCouncelSpecialTeam017.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam017.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam017);

        VotingCouncelEntity votingCouncelSpecialTeam018 = new VotingCouncelEntity();
        votingCouncelSpecialTeam018.setCode("034МТ018");
        votingCouncelSpecialTeam018.setName("МОБИЛНИ ТИМ XVIII");
        votingCouncelSpecialTeam018.setLocation(null);
        votingCouncelSpecialTeam018.setNumberOfMembers(2);
        votingCouncelSpecialTeam018.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam018.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam018);

        VotingCouncelEntity votingCouncelSpecialTeam019 = new VotingCouncelEntity();
        votingCouncelSpecialTeam019.setCode("034МТ019");
        votingCouncelSpecialTeam019.setName("МОБИЛНИ ТИМ XIX");
        votingCouncelSpecialTeam019.setLocation(null);
        votingCouncelSpecialTeam019.setNumberOfMembers(2);
        votingCouncelSpecialTeam019.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam019.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam019);

        VotingCouncelEntity votingCouncelSpecialTeam020 = new VotingCouncelEntity();
        votingCouncelSpecialTeam020.setCode("034МТ020");
        votingCouncelSpecialTeam020.setName("МОБИЛНИ ТИМ XX");
        votingCouncelSpecialTeam020.setLocation(null);
        votingCouncelSpecialTeam020.setNumberOfMembers(2);
        votingCouncelSpecialTeam020.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam020.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam020);

        VotingCouncelEntity votingCouncelSpecialTeam021 = new VotingCouncelEntity();
        votingCouncelSpecialTeam021.setCode("034МТ021");
        votingCouncelSpecialTeam021.setName("МОБИЛНИ ТИМ XXI");
        votingCouncelSpecialTeam021.setLocation(null);
        votingCouncelSpecialTeam021.setNumberOfMembers(2);
        votingCouncelSpecialTeam021.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam021.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam021);

        VotingCouncelEntity votingCouncelSpecialTeam022 = new VotingCouncelEntity();
        votingCouncelSpecialTeam022.setCode("034МТ022");
        votingCouncelSpecialTeam022.setName("МОБИЛНИ ТИМ XXII");
        votingCouncelSpecialTeam022.setLocation(null);
        votingCouncelSpecialTeam022.setNumberOfMembers(2);
        votingCouncelSpecialTeam022.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam022.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam022);

        VotingCouncelEntity votingCouncelSpecialTeam023 = new VotingCouncelEntity();
        votingCouncelSpecialTeam023.setCode("034МТ023");
        votingCouncelSpecialTeam023.setName("МОБИЛНИ ТИМ XXIII");
        votingCouncelSpecialTeam023.setLocation(null);
        votingCouncelSpecialTeam023.setNumberOfMembers(2);
        votingCouncelSpecialTeam023.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam023.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam023);

        VotingCouncelEntity votingCouncelSpecialTeam024 = new VotingCouncelEntity();
        votingCouncelSpecialTeam024.setCode("034МТ024");
        votingCouncelSpecialTeam024.setName("МОБИЛНИ ТИМ XXIV");
        votingCouncelSpecialTeam024.setLocation(null);
        votingCouncelSpecialTeam024.setNumberOfMembers(2);
        votingCouncelSpecialTeam024.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam024.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam024);

        VotingCouncelEntity votingCouncelSpecialTeam025 = new VotingCouncelEntity();
        votingCouncelSpecialTeam025.setCode("034МТ025");
        votingCouncelSpecialTeam025.setName("МОБИЛНИ ТИМ XXV");
        votingCouncelSpecialTeam025.setLocation(null);
        votingCouncelSpecialTeam025.setNumberOfMembers(2);
        votingCouncelSpecialTeam025.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelSpecialTeam025.setMentor(leona);
        votingCouncelRepository.save(votingCouncelSpecialTeam025);

    }
}
