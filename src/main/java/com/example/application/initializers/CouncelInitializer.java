package com.example.application.initializers;

import com.example.application.entities.MentorEntity;
import com.example.application.entities.TitleEntity;
import com.example.application.entities.VotingCouncelEntity;
import com.example.application.repositories.MentorRepository;
import com.example.application.repositories.TitleRepository;
import com.example.application.repositories.VotingCouncelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class CouncelInitializer implements ApplicationRunner {
    private final MentorRepository mentorRepository;
    private final VotingCouncelRepository votingCouncelRepository;
    private final TitleRepository titleRepository;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        //Dodavanje clanova
        MentorEntity dubravko = new MentorEntity();
        dubravko.setFirstname("Dubravko");
        dubravko.setLastname("Malinić");
        dubravko.setEmail("john.doe@example.com");
        dubravko.setVotingCouncels(new ArrayList<VotingCouncelEntity>());
        mentorRepository.save(dubravko);

        MentorEntity bojana = new MentorEntity();
        bojana.setFirstname("Bojana");
        bojana.setLastname("Ajder");
        bojana.setEmail("john.doe@example.com");
        bojana.setVotingCouncels(new ArrayList<VotingCouncelEntity>());
        mentorRepository.save(bojana);

        MentorEntity dusko = new MentorEntity();
        dusko.setFirstname("Duško");
        dusko.setLastname("Radivojević");
        dusko.setEmail("john.doe@example.com");
        dusko.setVotingCouncels(new ArrayList<VotingCouncelEntity>());
        mentorRepository.save(dusko);

        MentorEntity graba = new MentorEntity();
        graba.setFirstname("Dragan");
        graba.setLastname("Grabovica");
        graba.setEmail("john.doe@example.com");
        graba.setVotingCouncels(new ArrayList<VotingCouncelEntity>());
        mentorRepository.save(graba);

        MentorEntity leona = new MentorEntity();
        leona.setFirstname("Leona");
        leona.setLastname("Lihović");
        leona.setEmail("john.doe@example.com");
        leona.setVotingCouncels(new ArrayList<VotingCouncelEntity>());
        mentorRepository.save(leona);

        MentorEntity dujko = new MentorEntity();
        dujko.setFirstname("Dujko");
        dujko.setLastname("Komljenović");
        dujko.setEmail("john.doe@example.com");
        dujko.setVotingCouncels(new ArrayList<VotingCouncelEntity>());
        mentorRepository.save(dujko);

        MentorEntity igor = new MentorEntity();
        igor.setFirstname("Igor");
        igor.setLastname("Vidović");
        igor.setEmail("john.doe@example.com");
        igor.setVotingCouncels(new ArrayList<VotingCouncelEntity>());
        mentorRepository.save(igor);


        //Dodavanje titula
        TitleEntity clanTitle = new TitleEntity();
        clanTitle.setName("ČLAN");
        titleRepository.save(clanTitle);

        // Create and save "ZAMJENIK CLANA" TitleEntity
        TitleEntity zamjenikClanaTitle = new TitleEntity();
        zamjenikClanaTitle.setName("ZAMJENIK ČLANA");
        titleRepository.save(zamjenikClanaTitle);


        Random random = new Random();

        VotingCouncelEntity votingCouncel001 = new VotingCouncelEntity();
        votingCouncel001.setCode("034Б001");
        votingCouncel001.setName("АГИНО СЕЛО");
        votingCouncel001.setLocation("ПШ \"ВОЈИСЛАВ ИЛИЋ\", Агино Село бб, уч. 1");
        votingCouncel001.setNumberOfMembers(4);
        votingCouncel001.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel001.setMentor(leona);
        votingCouncelRepository.save(votingCouncel001);

        VotingCouncelEntity votingCouncel002 = new VotingCouncelEntity();
        votingCouncel002.setCode("034Б002");
        votingCouncel002.setName("АДА - 1");
        votingCouncel002.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб,уч 1.");
        votingCouncel002.setNumberOfMembers(4);
        votingCouncel002.setMentor(bojana);
        votingCouncel002.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelRepository.save(votingCouncel002);

        VotingCouncelEntity votingCouncel003 = new VotingCouncelEntity();
        votingCouncel003.setCode("034Б003");
        votingCouncel003.setName("АДА - 2");
        votingCouncel003.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб,уч 2.");
        votingCouncel003.setNumberOfMembers(4);
        votingCouncel003.setMentor(bojana);
        votingCouncel003.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelRepository.save(votingCouncel003);

        VotingCouncelEntity votingCouncel004 = new VotingCouncelEntity();
        votingCouncel004.setCode("034Б004");
        votingCouncel004.setName("АДА - 3");
        votingCouncel004.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч 3.");
        votingCouncel004.setNumberOfMembers(4);
        votingCouncel004.setMentor(bojana);
        votingCouncel004.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelRepository.save(votingCouncel004);

        VotingCouncelEntity votingCouncel005 = new VotingCouncelEntity();
        votingCouncel005.setCode("034Б005");
        votingCouncel005.setName("АДА - 4");
        votingCouncel005.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч 4.");
        votingCouncel005.setNumberOfMembers(4);
        votingCouncel005.setMentor(bojana);
        votingCouncel005.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelRepository.save(votingCouncel005);

        VotingCouncelEntity votingCouncel006 = new VotingCouncelEntity();
        votingCouncel006.setCode("034Б006");
        votingCouncel006.setName("АДА - 5");
        votingCouncel006.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч 5.");
        votingCouncel006.setNumberOfMembers(4);
        votingCouncel006.setMentor(bojana);
        votingCouncel006.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelRepository.save(votingCouncel006);

        VotingCouncelEntity votingCouncel007 = new VotingCouncelEntity();
        votingCouncel007.setCode("034Б007");
        votingCouncel007.setName("АДА - 6");
        votingCouncel007.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч 6.");
        votingCouncel007.setNumberOfMembers(4);
        votingCouncel007.setMentor(bojana);
        votingCouncel007.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelRepository.save(votingCouncel007);

        VotingCouncelEntity votingCouncel008А = new VotingCouncelEntity();
        votingCouncel008А.setCode("034Б008 А");
        votingCouncel008А.setName("БИСТРИЦА");
        votingCouncel008А.setLocation("ОШ \"МИРОСЛАВ АНТИЋ\", Бистрица, уч.1");
        votingCouncel008А.setNumberOfMembers(4);
        votingCouncel008А.setMentor(graba);
        votingCouncel008А.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelRepository.save(votingCouncel008А);

        VotingCouncelEntity votingCouncel008B = new VotingCouncelEntity();
        votingCouncel008B.setCode("034Б008 Б");
        votingCouncel008B.setName("БИСТРИЦА");
        votingCouncel008B.setLocation("ОШ \"МИРОСЛАВ АНТИЋ\", Бистрица, уч.2");
        votingCouncel008B.setNumberOfMembers(4);
        votingCouncel008B.setMentor(graba);
        votingCouncel008B.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelRepository.save(votingCouncel008B);

        VotingCouncelEntity votingCouncel009 = new VotingCouncelEntity();
        votingCouncel009.setCode("034Б009");
        votingCouncel009.setName("БОРИК I /1");
        votingCouncel009.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч.1");
        votingCouncel009.setNumberOfMembers(4);
        votingCouncel009.setMentor(graba);
        votingCouncel009.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelRepository.save(votingCouncel009);

        VotingCouncelEntity votingCouncel010 = new VotingCouncelEntity();
        votingCouncel010.setCode("034Б010");
        votingCouncel010.setName("БОРИК I /2");
        votingCouncel010.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 2");
        votingCouncel010.setNumberOfMembers(4);
        votingCouncel010.setMentor(graba);
        votingCouncel010.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncelRepository.save(votingCouncel010);

        VotingCouncelEntity votingCouncel011 = new VotingCouncelEntity();
        votingCouncel011.setCode("034Б011");
        votingCouncel011.setName("БОРИК I /3");
        votingCouncel011.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 3");
        votingCouncel011.setNumberOfMembers(4);
        votingCouncel011.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel011.setMentor(graba);
        votingCouncelRepository.save(votingCouncel011);

        VotingCouncelEntity votingCouncel012 = new VotingCouncelEntity();
        votingCouncel012.setCode("034Б012");
        votingCouncel012.setName("БОРИК I /4");
        votingCouncel012.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 4");
        votingCouncel012.setNumberOfMembers(4);
        votingCouncel012.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel012.setMentor(graba);
        votingCouncelRepository.save(votingCouncel012);

        VotingCouncelEntity votingCouncel013 = new VotingCouncelEntity();
        votingCouncel013.setCode("034Б013");
        votingCouncel013.setName("БОРИК I /5");
        votingCouncel013.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 5");
        votingCouncel013.setNumberOfMembers(4);
        votingCouncel013.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel013.setMentor(graba);
        votingCouncelRepository.save(votingCouncel013);

        VotingCouncelEntity votingCouncel014 = new VotingCouncelEntity();
        votingCouncel014.setCode("034Б014");
        votingCouncel014.setName("БОРИК I /6");
        votingCouncel014.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 6");
        votingCouncel014.setNumberOfMembers(4);
        votingCouncel014.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel014.setMentor(graba);
        votingCouncelRepository.save(votingCouncel014);

        VotingCouncelEntity votingCouncel015 = new VotingCouncelEntity();
        votingCouncel015.setCode("034Б015");
        votingCouncel015.setName("БОРИК I /7");
        votingCouncel015.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 7");
        votingCouncel015.setNumberOfMembers(4);
        votingCouncel015.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel015.setMentor(graba);
        votingCouncelRepository.save(votingCouncel015);

        VotingCouncelEntity votingCouncel016 = new VotingCouncelEntity();
        votingCouncel016.setCode("034Б016");
        votingCouncel016.setName("БОРИК I /8");
        votingCouncel016.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 8");
        votingCouncel016.setNumberOfMembers(4);
        votingCouncel016.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel016.setMentor(graba);
        votingCouncelRepository.save(votingCouncel016);

        VotingCouncelEntity votingCouncel017 = new VotingCouncelEntity();
        votingCouncel017.setCode("034Б017");
        votingCouncel017.setName("БОРИК I /9");
        votingCouncel017.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 9");
        votingCouncel017.setNumberOfMembers(4);
        votingCouncel017.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel017.setMentor(graba);
        votingCouncelRepository.save(votingCouncel017);

        VotingCouncelEntity votingCouncel018 = new VotingCouncelEntity();
        votingCouncel018.setCode("034Б018");
        votingCouncel018.setName("БОРИК II/1");
        votingCouncel018.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 1");
        votingCouncel018.setNumberOfMembers(4);
        votingCouncel018.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel018.setMentor(igor);
        votingCouncelRepository.save(votingCouncel018);

        VotingCouncelEntity votingCouncel019 = new VotingCouncelEntity();
        votingCouncel019.setCode("034Б019");
        votingCouncel019.setName("БОРИК II/2");
        votingCouncel019.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 2");
        votingCouncel019.setNumberOfMembers(4);
        votingCouncel019.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel019.setMentor(igor);
        votingCouncelRepository.save(votingCouncel019);

        VotingCouncelEntity votingCouncel020 = new VotingCouncelEntity();
        votingCouncel020.setCode("034Б020");
        votingCouncel020.setName("БОРИК II/3");
        votingCouncel020.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 3");
        votingCouncel020.setNumberOfMembers(4);
        votingCouncel020.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel020.setMentor(igor);
        votingCouncelRepository.save(votingCouncel020);

        VotingCouncelEntity votingCouncel021 = new VotingCouncelEntity();
        votingCouncel021.setCode("034Б021");
        votingCouncel021.setName("БОРИК II/4");
        votingCouncel021.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 4");
        votingCouncel021.setNumberOfMembers(4);
        votingCouncel021.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel021.setMentor(igor);
        votingCouncelRepository.save(votingCouncel021);

        VotingCouncelEntity votingCouncel022 = new VotingCouncelEntity();
        votingCouncel022.setCode("034Б022");
        votingCouncel022.setName("БОРИК II/5");
        votingCouncel022.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 5");
        votingCouncel022.setNumberOfMembers(4);
        votingCouncel022.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel022.setMentor(igor);
        votingCouncelRepository.save(votingCouncel022);

        VotingCouncelEntity votingCouncel023 = new VotingCouncelEntity();
        votingCouncel023.setCode("034Б023");
        votingCouncel023.setName("БОРИК II/6");
        votingCouncel023.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 6");
        votingCouncel023.setNumberOfMembers(4);
        votingCouncel023.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel023.setMentor(igor);
        votingCouncelRepository.save(votingCouncel023);

        VotingCouncelEntity votingCouncel024 = new VotingCouncelEntity();
        votingCouncel024.setCode("034Б024");
        votingCouncel024.setName("БОРИК II/7");
        votingCouncel024.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 7");
        votingCouncel024.setNumberOfMembers(4);
        votingCouncel024.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel024.setMentor(igor);
        votingCouncelRepository.save(votingCouncel024);

        VotingCouncelEntity votingCouncel025 = new VotingCouncelEntity();
        votingCouncel025.setCode("034Б025");
        votingCouncel025.setName("БОРКОВИЋИ - 1");
        votingCouncel025.setLocation("МЗ БОРКОВИЋИ, сала 1");
        votingCouncel025.setNumberOfMembers(4);
        votingCouncel025.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel025.setMentor(graba);
        votingCouncelRepository.save(votingCouncel025);

        VotingCouncelEntity votingCouncel026 = new VotingCouncelEntity();
        votingCouncel026.setCode("034Б026");
        votingCouncel026.setName("БОРКОВИЋИ - 2 /СЛАВИЋКА");
        votingCouncel026.setLocation("Продавница \"ПЛАЗМА\" Славићка");
        votingCouncel026.setNumberOfMembers(4);
        votingCouncel026.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel026.setMentor(graba);
        votingCouncelRepository.save(votingCouncel026);

        VotingCouncelEntity votingCouncel027A = new VotingCouncelEntity();
        votingCouncel027A.setCode("034Б027 А");
        votingCouncel027A.setName("БОЧАЦ");
        votingCouncel027A.setLocation("ПШ \"ВОЈИСЛАВ ИЛИЋ\", Бочац, уч. 1");
        votingCouncel027A.setNumberOfMembers(4);
        votingCouncel027A.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel027A.setMentor(leona);
        votingCouncelRepository.save(votingCouncel027A);

        VotingCouncelEntity votingCouncel027B = new VotingCouncelEntity();
        votingCouncel027B.setCode("034Б027 Б");
        votingCouncel027B.setName("БОЧАЦ");
        votingCouncel027B.setLocation("ПШ \"ВОЈИСЛАВ ИЛИЋ\", Бочац, уч. 2");
        votingCouncel027B.setNumberOfMembers(4);
        votingCouncel027B.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel027B.setMentor(leona);
        votingCouncelRepository.save(votingCouncel027B);

        VotingCouncelEntity votingCouncel028 = new VotingCouncelEntity();
        votingCouncel028.setCode("034Б028");
        votingCouncel028.setName("БРОНЗАНИ МАЈДАН - 1");
        votingCouncel028.setLocation("МЗ БРОНЗАНИ МАЈДАН, сала 1");
        votingCouncel028.setNumberOfMembers(4);
        votingCouncel028.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel028.setMentor(graba);
        votingCouncelRepository.save(votingCouncel028);

        VotingCouncelEntity votingCouncel029 = new VotingCouncelEntity();
        votingCouncel029.setCode("034Б029");
        votingCouncel029.setName("БРОНЗАНИ МАЈДАН - 2 /МЕЛИНА");
        votingCouncel029.setLocation("ПШ \"МЛАДЕН СТОЈАНОВИЋ\" Мелина, уч. 1");
        votingCouncel029.setNumberOfMembers(4);
        votingCouncel029.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel029.setMentor(graba);
        votingCouncelRepository.save(votingCouncel029);

        VotingCouncelEntity votingCouncel030 = new VotingCouncelEntity();
        votingCouncel030.setCode("034Б030");
        votingCouncel030.setName("БРОНЗАНИ МАЈДАН - 3/ОБРОВАЦ");
        votingCouncel030.setLocation("ПШ „МЛАДЕН СТОЈАНОВИЋ“ Обровац, уч.1.");
        votingCouncel030.setNumberOfMembers(4);
        votingCouncel030.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel030.setMentor(graba);
        votingCouncelRepository.save(votingCouncel030);

        VotingCouncelEntity votingCouncel031 = new VotingCouncelEntity();
        votingCouncel031.setCode("034Б031");
        votingCouncel031.setName("БУЛЕВАР - 1");
        votingCouncel031.setLocation("ГИМНАЗИЈА, Змај Јовина 13, уч. 1");
        votingCouncel031.setNumberOfMembers(4);
        votingCouncel031.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel031.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel031);

        VotingCouncelEntity votingCouncel032 = new VotingCouncelEntity();
        votingCouncel032.setCode("034Б032");
        votingCouncel032.setName("БУЛЕВАР - 2");
        votingCouncel032.setLocation("ГИМНАЗИЈА, Змај Јовина 13, уч. 2");
        votingCouncel032.setNumberOfMembers(4);
        votingCouncel032.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel032.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel032);

        VotingCouncelEntity votingCouncel033 = new VotingCouncelEntity();
        votingCouncel033.setCode("034Б033");
        votingCouncel033.setName("БУЛЕВАР - 3");
        votingCouncel033.setLocation("МЗ БУЛЕВАР, Симеуна Ђака 15, сала 1");
        votingCouncel033.setNumberOfMembers(4);
        votingCouncel033.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel033.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel033);

        VotingCouncelEntity votingCouncel034 = new VotingCouncelEntity();
        votingCouncel034.setCode("034Б034");
        votingCouncel034.setName("БУЛЕВАР - 4");
        votingCouncel034.setLocation("ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 1");
        votingCouncel034.setNumberOfMembers(4);
        votingCouncel034.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel034.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034);

        VotingCouncelEntity votingCouncel035 = new VotingCouncelEntity();
        votingCouncel035.setCode("034Б035");
        votingCouncel035.setName("БУЛЕВАР - 5");
        votingCouncel035.setLocation("ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 2");
        votingCouncel035.setNumberOfMembers(4);
        votingCouncel035.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel035.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel035);

        VotingCouncelEntity votingCouncel036 = new VotingCouncelEntity();
        votingCouncel036.setCode("034Б036");
        votingCouncel036.setName("БУЛЕВАР - 6");
        votingCouncel036.setLocation("ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 3");
        votingCouncel036.setNumberOfMembers(4);
        votingCouncel036.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel036.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel036);

        VotingCouncelEntity votingCouncel037 = new VotingCouncelEntity();
        votingCouncel037.setCode("034Б037");
        votingCouncel037.setName("БУЛЕВАР - 7");
        votingCouncel037.setLocation("ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 4");
        votingCouncel037.setNumberOfMembers(4);
        votingCouncel037.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel037.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel037);

        VotingCouncelEntity votingCouncel038 = new VotingCouncelEntity();
        votingCouncel038.setCode("034Б038");
        votingCouncel038.setName("ВЕРИЋИ - 1");
        votingCouncel038.setLocation("ПШ „МИЛУТИН БОЈИЋ“, Верићи, уч.1.");
        votingCouncel038.setNumberOfMembers(4);
        votingCouncel038.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel038.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel038);

        VotingCouncelEntity votingCouncel039 = new VotingCouncelEntity();
        votingCouncel039.setCode("034Б039");
        votingCouncel039.setName("ВЕРИЋИ - 2");
        votingCouncel039.setLocation("ПШ \"МИЛУТИН БОЈИЋ\", Верићи, уч. 2");
        votingCouncel039.setNumberOfMembers(4);
        votingCouncel039.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel039.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel039);

        VotingCouncelEntity votingCouncel040 = new VotingCouncelEntity();
        votingCouncel040.setCode("034Б040");
        votingCouncel040.setName("ВРБАЊА - 1");
        votingCouncel040.setLocation("МЗ ВРБАЊА, Станка Божића Кобре бб, сала 1");
        votingCouncel040.setNumberOfMembers(4);
        votingCouncel040.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel040.setMentor(leona);
        votingCouncelRepository.save(votingCouncel040);

        VotingCouncelEntity votingCouncel041 = new VotingCouncelEntity();
        votingCouncel041.setCode("034Б041");
        votingCouncel041.setName("ВРБАЊА - 2");
        votingCouncel041.setLocation("ОШ \"СТАНКО РАКИТА\", Јове Г. Поповића 9, уч. 1");
        votingCouncel041.setNumberOfMembers(4);
        votingCouncel041.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel041.setMentor(leona);
        votingCouncelRepository.save(votingCouncel041);

        VotingCouncelEntity votingCouncel042 = new VotingCouncelEntity();
        votingCouncel042.setCode("034Б042");
        votingCouncel042.setName("ВРБАЊА - 3");
        votingCouncel042.setLocation("ОШ \"СТАНКО РАКИТА\", Јове Г. Поповића 9, уч. 2");
        votingCouncel042.setNumberOfMembers(4);
        votingCouncel042.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel042.setMentor(leona);
        votingCouncelRepository.save(votingCouncel042);

        VotingCouncelEntity votingCouncel043 = new VotingCouncelEntity();
        votingCouncel043.setCode("034Б043");
        votingCouncel043.setName("ВРБАЊА - 4");
        votingCouncel043.setLocation("ОШ \"СТАНКО РАКИТА\", Јове Г. Поповића 9, уч. 3");
        votingCouncel043.setNumberOfMembers(4);
        votingCouncel043.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel043.setMentor(leona);
        votingCouncelRepository.save(votingCouncel043);

        VotingCouncelEntity votingCouncel044 = new VotingCouncelEntity();
        votingCouncel044.setCode("034Б044");
        votingCouncel044.setName("ГОЛЕШИ - 1 /ДОЊИ ПЕРВАН");
        votingCouncel044.setLocation("ПШ \"МИРОСЛАВ АНТИЋ\", Доњи Перван, уч. 1");
        votingCouncel044.setNumberOfMembers(4);
        votingCouncel044.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel044.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel044);

        VotingCouncelEntity votingCouncel045 = new VotingCouncelEntity();
        votingCouncel045.setCode("034Б045");
        votingCouncel045.setName("ВРБАЊА - 5");
        votingCouncel045.setLocation("ОШ \"СТАНКО РАКИТА\", Јове Г. Поповића 9, уч. 4.");
        votingCouncel045.setNumberOfMembers(4);
        votingCouncel045.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel045.setMentor(leona);
        votingCouncelRepository.save(votingCouncel045);

        VotingCouncelEntity votingCouncel046 = new VotingCouncelEntity();
        votingCouncel046.setCode("034Б046");
        votingCouncel046.setName("ГОРЊА ПИСКАВИЦА");
        votingCouncel046.setLocation("ПШ \"ЋИРИЛО И МЕТОДИЈЕ\", Горња Пискавица, уч. 1");
        votingCouncel046.setNumberOfMembers(4);
        votingCouncel046.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel046.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel046);

        VotingCouncelEntity votingCouncel047 = new VotingCouncelEntity();
        votingCouncel047.setCode("034Б047");
        votingCouncel047.setName("ДЕБЕЉАЦИ - 1");
        votingCouncel047.setLocation("ПШ \"СТАНКО РАКИТА\", Тешана Подруговића бб, уч. 1");
        votingCouncel047.setNumberOfMembers(4);
        votingCouncel047.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel047.setMentor(leona);
        votingCouncelRepository.save(votingCouncel047);

        VotingCouncelEntity votingCouncel048 = new VotingCouncelEntity();
        votingCouncel048.setCode("034Б048");
        votingCouncel048.setName("ДЕБЕЉАЦИ - 2");
        votingCouncel048.setLocation("ПШ \"СТАНКО РАКИТА\", Тешана Подруговића бб, уч. 2");
        votingCouncel048.setNumberOfMembers(4);
        votingCouncel048.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel048.setMentor(leona);
        votingCouncelRepository.save(votingCouncel048);

        VotingCouncelEntity votingCouncel049 = new VotingCouncelEntity();
        votingCouncel049.setCode("034Б049");
        votingCouncel049.setName("ДОЊА КОЛА - 1");
        votingCouncel049.setLocation("МЗ Доња Кола, сала 1");
        votingCouncel049.setNumberOfMembers(4);
        votingCouncel049.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel049.setMentor(leona);
        votingCouncelRepository.save(votingCouncel049);

        VotingCouncelEntity votingCouncel050 = new VotingCouncelEntity();
        votingCouncel050.setCode("034Б050");
        votingCouncel050.setName("ДОЊА КОЛА - 2");
        votingCouncel050.setLocation("МЗ Доња Кола, сала 2");
        votingCouncel050.setNumberOfMembers(4);
        votingCouncel050.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel050.setMentor(leona);
        votingCouncelRepository.save(votingCouncel050);

        VotingCouncelEntity votingCouncel051A = new VotingCouncelEntity();
        votingCouncel051A.setCode("034Б051 А");
        votingCouncel051A.setName("ДРАГОЧАЈ – 1");
        votingCouncel051A.setLocation("ОШ \"ДЕСАНКА МАКСИМОВИЋ\", Драгочај, уч. 1");
        votingCouncel051A.setNumberOfMembers(4);
        votingCouncel051A.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel051A.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel051A);

        VotingCouncelEntity votingCouncel051B = new VotingCouncelEntity();
        votingCouncel051B.setCode("034Б051 Б");
        votingCouncel051B.setName("ДРАГОЧАЈ – 1");
        votingCouncel051B.setLocation("ОШ \"ДЕСАНКА МАКСИМОВИЋ\", Драгочај, уч. 2");
        votingCouncel051B.setNumberOfMembers(4);
        votingCouncel051B.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel051B.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel051B);

        VotingCouncelEntity votingCouncel051C = new VotingCouncelEntity();
        votingCouncel051C.setCode("034Б051 Ц");
        votingCouncel051C.setName("ДРАГОЧАЈ – 1");
        votingCouncel051C.setLocation("ОШ \"ДЕСАНКА МАКСИМОВИЋ\", Драгочај, уч. 3");
        votingCouncel051C.setNumberOfMembers(4);
        votingCouncel051C.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel051C.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel051C);

        VotingCouncelEntity votingCouncel052A = new VotingCouncelEntity();
        votingCouncel052A.setCode("034Б052 А");
        votingCouncel052A.setName("ДРАГОЧАЈ - 2 / РАМИЋИ");
        votingCouncel052A.setLocation("Друштвене просторије Рамићи, сала 1");
        votingCouncel052A.setNumberOfMembers(4);
        votingCouncel052A.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel052A.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel052A);

        VotingCouncelEntity votingCouncel052B = new VotingCouncelEntity();
        votingCouncel052B.setCode("034Б052 Б");
        votingCouncel052B.setName("ДРАГОЧАЈ - 2 / РАМИЋИ");
        votingCouncel052B.setLocation("Друштвене просторије Рамићи, сала 2");
        votingCouncel052B.setNumberOfMembers(4);
        votingCouncel052B.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel052B.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel052B);

        VotingCouncelEntity votingCouncel053 = new VotingCouncelEntity();
        votingCouncel053.setCode("034Б053");
        votingCouncel053.setName("ДРАГОЧАЈ - 3 / БАРЛОВЦИ");
        votingCouncel053.setLocation("ПШ \"ЈОВАН ДУЧИЋ\", Барловци, уч. 1");
        votingCouncel053.setNumberOfMembers(4);
        votingCouncel053.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel053.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel053);

        VotingCouncelEntity votingCouncel054 = new VotingCouncelEntity();
        votingCouncel054.setCode("034Б054");
        votingCouncel054.setName("ДРАКУЛИЋ - 1");
        votingCouncel054.setLocation("МЗ ДРАГУЛИЋ, Ђурђа Гламочанина 1, сала 1");
        votingCouncel054.setNumberOfMembers(4);
        votingCouncel054.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel054.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel054);

        VotingCouncelEntity votingCouncel055 = new VotingCouncelEntity();
        votingCouncel055.setCode("034Б055");
        votingCouncel055.setName("ДРАКУЛИЋ - 2");
        votingCouncel055.setLocation("ПШ \"АЛЕКСА ШАНТИЋ\", Дракулић, 7. фебруара бб, уч. 1");
        votingCouncel055.setNumberOfMembers(4);
        votingCouncel055.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel055.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel055);

        VotingCouncelEntity votingCouncel056 = new VotingCouncelEntity();
        votingCouncel056.setCode("034Б056");
        votingCouncel056.setName("ДРАКУЛИЋ - 3");
        votingCouncel056.setLocation("КИНОЛОШКИ САВЕЗ БАЊА ЛУКА, Битољска бб, сала 1");
        votingCouncel056.setNumberOfMembers(4);
        votingCouncel056.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel056.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel056);

        VotingCouncelEntity votingCouncel057 = new VotingCouncelEntity();
        votingCouncel057.setCode("034Б057");
        votingCouncel057.setName("ДРАКУЛИЋ - 4");
        votingCouncel057.setLocation("ПШ \"АЛЕКСА ШАНТИЋ\", Дракулић, 7. фебруара бб, уч. 2.");
        votingCouncel057.setNumberOfMembers(4);
        votingCouncel057.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel057.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel057);

        VotingCouncelEntity votingCouncel058 = new VotingCouncelEntity();
        votingCouncel058.setCode("034Б058");
        votingCouncel058.setName("ДРАКУЛИЋ - 5");
        votingCouncel058.setLocation("КИНОЛОШКИ САВЕЗ БАЊА ЛУКА, Битољска бб, сала 2");
        votingCouncel058.setNumberOfMembers(4);
        votingCouncel058.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel058.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel058);

        VotingCouncelEntity votingCouncel059 = new VotingCouncelEntity();
        votingCouncel059.setCode("034Б059");
        votingCouncel059.setName("ДРАКУЛИЋ - 6");
        votingCouncel059.setLocation("КИНОЛОШКИ САВЕЗ БАЊА ЛУКА, Битољска бб, сала 3");
        votingCouncel059.setNumberOfMembers(4);
        votingCouncel059.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel059.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel059);

        VotingCouncelEntity votingCouncel060 = new VotingCouncelEntity();
        votingCouncel060.setCode("034Б060");
        votingCouncel060.setName("ЗАЛУЖАНИ - 1");
        votingCouncel060.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 1");
        votingCouncel060.setNumberOfMembers(4);
        votingCouncel060.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel060.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel060);

        VotingCouncelEntity votingCouncel061 = new VotingCouncelEntity();
        votingCouncel061.setCode("034Б061");
        votingCouncel061.setName("ЗАЛУЖАНИ - 2");
        votingCouncel061.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 2");
        votingCouncel061.setNumberOfMembers(4);
        votingCouncel061.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel061.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel061);

        VotingCouncelEntity votingCouncel062 = new VotingCouncelEntity();
        votingCouncel062.setCode("034Б062");
        votingCouncel062.setName("ЗАЛУЖАНИ - 3");
        votingCouncel062.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 3");
        votingCouncel062.setNumberOfMembers(4);
        votingCouncel062.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel062.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel062);

        VotingCouncelEntity votingCouncel063 = new VotingCouncelEntity();
        votingCouncel063.setCode("034Б063");
        votingCouncel063.setName("ЗАЛУЖАНИ - 4");
        votingCouncel063.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 4");
        votingCouncel063.setNumberOfMembers(4);
        votingCouncel063.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel063.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel063);

        VotingCouncelEntity votingCouncel064 = new VotingCouncelEntity();
        votingCouncel064.setCode("034Б064");
        votingCouncel064.setName("ЗАЛУЖАНИ - 5");
        votingCouncel064.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 5");
        votingCouncel064.setNumberOfMembers(4);
        votingCouncel064.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel064.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel064);

        VotingCouncelEntity votingCouncel065 = new VotingCouncelEntity();
        votingCouncel065.setCode("034Б065");
        votingCouncel065.setName("ЗАЛУЖАНИ - 6");
        votingCouncel065.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 6");
        votingCouncel065.setNumberOfMembers(4);
        votingCouncel065.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel065.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel065);

        VotingCouncelEntity votingCouncel066 = new VotingCouncelEntity();
        votingCouncel066.setCode("034Б066");
        votingCouncel066.setName("ЗАЛУЖАНИ - 7");
        votingCouncel066.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 7");
        votingCouncel066.setNumberOfMembers(4);
        votingCouncel066.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel066.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel066);

        VotingCouncelEntity votingCouncel067 = new VotingCouncelEntity();
        votingCouncel067.setCode("034Б067");
        votingCouncel067.setName("КАРАНОВАЦ – 1");
        votingCouncel067.setLocation("ОШ \"МИЛАН РАКИЋ\", Карановац, уч. 1");
        votingCouncel067.setNumberOfMembers(4);
        votingCouncel067.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel067.setMentor(leona);
        votingCouncelRepository.save(votingCouncel067);

        VotingCouncelEntity votingCouncel068 = new VotingCouncelEntity();
        votingCouncel068.setCode("034Б068");
        votingCouncel068.setName("КАРАНОВАЦ - 2");
        votingCouncel068.setLocation("ОШ \"МИЛАН РАКИЋ\", Карановац, уч. 2");
        votingCouncel068.setNumberOfMembers(4);
        votingCouncel068.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel068.setMentor(leona);
        votingCouncelRepository.save(votingCouncel068);

        VotingCouncelEntity votingCouncel069 = new VotingCouncelEntity();
        votingCouncel069.setCode("034Б069");
        votingCouncel069.setName("КМЕЋАНИ");
        votingCouncel069.setLocation("ПШ \"МЛАДЕН СТОЈАНОВИЋ\", Кмећани, уч. 1");
        votingCouncel069.setNumberOfMembers(2);
        votingCouncel069.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel069.setMentor(graba);
        votingCouncelRepository.save(votingCouncel069);

        VotingCouncelEntity votingCouncel070 = new VotingCouncelEntity();
        votingCouncel070.setCode("034Б070");
        votingCouncel070.setName("КОЛА - 1");
        votingCouncel070.setLocation("ОШ \"ПЕТАР КОЧИЋ\", Кола, уч. 1");
        votingCouncel070.setNumberOfMembers(4);
        votingCouncel070.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel070.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel070);

        VotingCouncelEntity votingCouncel071 = new VotingCouncelEntity();
        votingCouncel071.setCode("034Б071");
        votingCouncel071.setName("КОЧИЋЕВ ВИЈЕНАЦ - 1");
        votingCouncel071.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 1");
        votingCouncel071.setNumberOfMembers(4);
        votingCouncel071.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel071.setMentor(leona);
        votingCouncelRepository.save(votingCouncel071);

        VotingCouncelEntity votingCouncel072 = new VotingCouncelEntity();
        votingCouncel072.setCode("034Б072");
        votingCouncel072.setName("КОЧИЋЕВ ВИЈЕНАЦ - 2");
        votingCouncel072.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 2");
        votingCouncel072.setNumberOfMembers(4);
        votingCouncel072.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel072.setMentor(leona);
        votingCouncelRepository.save(votingCouncel072);

        VotingCouncelEntity votingCouncel073 = new VotingCouncelEntity();
        votingCouncel073.setCode("034Б073");
        votingCouncel073.setName("КОЧИЋЕВ ВИЈЕНАЦ - 3");
        votingCouncel073.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 3");
        votingCouncel073.setNumberOfMembers(4);
        votingCouncel073.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel073.setMentor(leona);
        votingCouncelRepository.save(votingCouncel073);

        VotingCouncelEntity votingCouncel074 = new VotingCouncelEntity();
        votingCouncel074.setCode("034Б074");
        votingCouncel074.setName("КОЧИЋЕВ ВИЈЕНАЦ - 4");
        votingCouncel074.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 4");
        votingCouncel074.setNumberOfMembers(4);
        votingCouncel074.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel074.setMentor(leona);
        votingCouncelRepository.save(votingCouncel074);

        VotingCouncelEntity votingCouncel075 = new VotingCouncelEntity();
        votingCouncel075.setCode("034Б075");
        votingCouncel075.setName("КОЧИЋЕВ ВИЈЕНАЦ - 5");
        votingCouncel075.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 5");
        votingCouncel075.setNumberOfMembers(4);
        votingCouncel075.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel075.setMentor(leona);
        votingCouncelRepository.save(votingCouncel075);

        VotingCouncelEntity votingCouncel076 = new VotingCouncelEntity();
        votingCouncel076.setCode("034Б076");
        votingCouncel076.setName("КОЧИЋЕВ ВИЈЕНАЦ - 6");
        votingCouncel076.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 6");
        votingCouncel076.setNumberOfMembers(4);
        votingCouncel076.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel076.setMentor(leona);
        votingCouncelRepository.save(votingCouncel076);

        VotingCouncelEntity votingCouncel077 = new VotingCouncelEntity();
        votingCouncel077.setCode("034Б077");
        votingCouncel077.setName("КОЧИЋЕВ ВИЈЕНАЦ - 7");
        votingCouncel077.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 7");
        votingCouncel077.setNumberOfMembers(4);
        votingCouncel077.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel077.setMentor(leona);
        votingCouncelRepository.save(votingCouncel077);

        VotingCouncelEntity votingCouncel078 = new VotingCouncelEntity();
        votingCouncel078.setCode("034Б078");
        votingCouncel078.setName("КОЧИЋЕВ ВИЈЕНАЦ - 8");
        votingCouncel078.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 8");
        votingCouncel078.setNumberOfMembers(4);
        votingCouncel078.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel078.setMentor(leona);
        votingCouncelRepository.save(votingCouncel078);

        VotingCouncelEntity votingCouncel079 = new VotingCouncelEntity();
        votingCouncel079.setCode("034Б079");
        votingCouncel079.setName("КОЧИЋЕВ ВИЈЕНАЦ - 9");
        votingCouncel079.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 9");
        votingCouncel079.setNumberOfMembers(4);
        votingCouncel079.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel079.setMentor(leona);
        votingCouncelRepository.save(votingCouncel079);

        VotingCouncelEntity votingCouncel080 = new VotingCouncelEntity();
        votingCouncel080.setCode("034Б080");
        votingCouncel080.setName("КОЧИЋЕВ ВИЈЕНАЦ - 10");
        votingCouncel080.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 10");
        votingCouncel080.setNumberOfMembers(4);
        votingCouncel080.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel080.setMentor(leona);
        votingCouncelRepository.save(votingCouncel080);

        VotingCouncelEntity votingCouncel081 = new VotingCouncelEntity();
        votingCouncel081.setCode("034Б081");
        votingCouncel081.setName("КРМИНЕ");
        votingCouncel081.setLocation("ПШ \"ВОЈИСЛАВ ИЛИЋ\", Крмине, уч. 1");
        votingCouncel081.setNumberOfMembers(4);
        votingCouncel081.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel081.setMentor(leona);
        votingCouncelRepository.save(votingCouncel081);

        VotingCouncelEntity votingCouncel082 = new VotingCouncelEntity();
        votingCouncel082.setCode("034Б082");
        votingCouncel082.setName("КРУПА НА ВРБАСУ – 1");
        votingCouncel082.setLocation("ОШ \"ВОЈИСЛАВ ИЛИЋ\", Крупа на Врбасу, уч. 1");
        votingCouncel082.setNumberOfMembers(4);
        votingCouncel082.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel082.setMentor(leona);
        votingCouncelRepository.save(votingCouncel082);

        VotingCouncelEntity votingCouncel083 = new VotingCouncelEntity();
        votingCouncel083.setCode("034Б083");
        votingCouncel083.setName("КРУПА НА ВРБАСУ - 2 / ЛЕДЕНИЦЕ");
        votingCouncel083.setLocation("ПШ \"ВОЈИСЛАВ ИЛИЋ\", Леденице, уч. 1");
        votingCouncel083.setNumberOfMembers(4);
        votingCouncel083.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel083.setMentor(leona);
        votingCouncelRepository.save(votingCouncel083);

        VotingCouncelEntity votingCouncel084 = new VotingCouncelEntity();
        votingCouncel084.setCode("034Б084");
        votingCouncel084.setName("КУЉАНИ - 1");
        votingCouncel084.setLocation("ПШ \"ЈОВАН ДУЧИЋ\" (НОВИ ОБЈЕКАТ), Куљани, уч. 1");
        votingCouncel084.setNumberOfMembers(4);
        votingCouncel084.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel084.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel084);

        VotingCouncelEntity votingCouncel085 = new VotingCouncelEntity();
        votingCouncel085.setCode("034Б085");
        votingCouncel085.setName("ЛАЗАРЕВО I / 1");
        votingCouncel085.setLocation("ПОЉОПОРИВРЕДНА ШКОЛА, Књаза Милоша 9, уч. 1");
        votingCouncel085.setNumberOfMembers(4);
        votingCouncel085.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel085.setMentor(igor);
        votingCouncelRepository.save(votingCouncel085);

        VotingCouncelEntity votingCouncel086 = new VotingCouncelEntity();
        votingCouncel086.setCode("034Б086");
        votingCouncel086.setName("ЛАЗАРЕВО I / 2");
        votingCouncel086.setLocation("ПОЉОПОРИВРЕДНА ШКОЛА, Књаза Милоша 9, уч. 2");
        votingCouncel086.setNumberOfMembers(4);
        votingCouncel086.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel086.setMentor(igor);
        votingCouncelRepository.save(votingCouncel086);

        VotingCouncelEntity votingCouncel087 = new VotingCouncelEntity();
        votingCouncel087.setCode("034Б087");
        votingCouncel087.setName("ЛАЗАРЕВО I / 3");
        votingCouncel087.setLocation("ПОЉОПОРИВРЕДНА ШКОЛА, Књаза Милоша 9, уч. 3");
        votingCouncel087.setNumberOfMembers(4);
        votingCouncel087.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel087.setMentor(igor);
        votingCouncelRepository.save(votingCouncel087);

        VotingCouncelEntity votingCouncel088 = new VotingCouncelEntity();
        votingCouncel088.setCode("034Б088");
        votingCouncel088.setName("ЛАЗАРЕВО I / 4");
        votingCouncel088.setLocation("ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 1");
        votingCouncel088.setNumberOfMembers(4);
        votingCouncel088.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel088.setMentor(igor);
        votingCouncelRepository.save(votingCouncel088);

        VotingCouncelEntity votingCouncel089 = new VotingCouncelEntity();
        votingCouncel089.setCode("034Б089");
        votingCouncel089.setName("ЛАЗАРЕВО I / 5");
        votingCouncel089.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч.1");
        votingCouncel089.setNumberOfMembers(4);
        votingCouncel089.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel089.setMentor(igor);
        votingCouncelRepository.save(votingCouncel089);

        VotingCouncelEntity votingCouncel090 = new VotingCouncelEntity();
        votingCouncel090.setCode("034Б090");
        votingCouncel090.setName("ЛАЗАРЕВО I / 6");
        votingCouncel090.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 2");
        votingCouncel090.setNumberOfMembers(4);
        votingCouncel090.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel090.setMentor(igor);
        votingCouncelRepository.save(votingCouncel090);

        VotingCouncelEntity votingCouncel091 = new VotingCouncelEntity();
        votingCouncel091.setCode("034Б091");
        votingCouncel091.setName("ЛАЗАРЕВО I / 7");
        votingCouncel091.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 3");
        votingCouncel091.setNumberOfMembers(4);
        votingCouncel091.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel091.setMentor(igor);
        votingCouncelRepository.save(votingCouncel091);

        VotingCouncelEntity votingCouncel092 = new VotingCouncelEntity();
        votingCouncel092.setCode("034Б092");
        votingCouncel092.setName("ЛАЗАРЕВО I / 8");
        votingCouncel092.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 4");
        votingCouncel092.setNumberOfMembers(4);
        votingCouncel092.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel092.setMentor(igor);
        votingCouncelRepository.save(votingCouncel092);

        VotingCouncelEntity votingCouncel093 = new VotingCouncelEntity();
        votingCouncel093.setCode("034Б093");
        votingCouncel093.setName("ЛАЗАРЕВО I / 9");
        votingCouncel093.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 5");
        votingCouncel093.setNumberOfMembers(4);
        votingCouncel093.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel093.setMentor(igor);
        votingCouncelRepository.save(votingCouncel093);

        VotingCouncelEntity votingCouncel094 = new VotingCouncelEntity();
        votingCouncel094.setCode("034Б094");
        votingCouncel094.setName("ЛАЗАРЕВО I / 10");
        votingCouncel094.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 6");
        votingCouncel094.setNumberOfMembers(4);
        votingCouncel094.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel094.setMentor(igor);
        votingCouncelRepository.save(votingCouncel094);

        VotingCouncelEntity votingCouncel095 = new VotingCouncelEntity();
        votingCouncel095.setCode("034Б095");
        votingCouncel095.setName("ЛАЗАРЕВО II / 1");
        votingCouncel095.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 7");
        votingCouncel095.setNumberOfMembers(4);
        votingCouncel095.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel095.setMentor(graba);
        votingCouncelRepository.save(votingCouncel095);

        VotingCouncelEntity votingCouncel096 = new VotingCouncelEntity();
        votingCouncel096.setCode("034Б096");
        votingCouncel096.setName("ЛАЗАРЕВО II / 2");
        votingCouncel096.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 8");
        votingCouncel096.setNumberOfMembers(4);
        votingCouncel096.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel096.setMentor(graba);
        votingCouncelRepository.save(votingCouncel096);

        VotingCouncelEntity votingCouncel097 = new VotingCouncelEntity();
        votingCouncel097.setCode("034Б097");
        votingCouncel097.setName("ЛАЗАРЕВО II / 3");
        votingCouncel097.setLocation("ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 2");
        votingCouncel097.setNumberOfMembers(4);
        votingCouncel097.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel097.setMentor(graba);
        votingCouncelRepository.save(votingCouncel097);

        VotingCouncelEntity votingCouncel098 = new VotingCouncelEntity();
        votingCouncel098.setCode("034Б098");
        votingCouncel098.setName("ЛАЗАРЕВО II / 4");
        votingCouncel098.setLocation("ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 3");
        votingCouncel098.setNumberOfMembers(4);
        votingCouncel098.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel098.setMentor(graba);
        votingCouncelRepository.save(votingCouncel098);

        VotingCouncelEntity votingCouncel099 = new VotingCouncelEntity();
        votingCouncel099.setCode("034Б099");
        votingCouncel099.setName("ЛАЗАРЕВО II / 5");
        votingCouncel099.setLocation("ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 4");
        votingCouncel099.setNumberOfMembers(4);
        votingCouncel099.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel099.setMentor(graba);
        votingCouncelRepository.save(votingCouncel099);

        VotingCouncelEntity votingCouncel100 = new VotingCouncelEntity();
        votingCouncel100.setCode("034Б100");
        votingCouncel100.setName("ЛАЗАРЕВО II / 6");
        votingCouncel100.setLocation("ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 5");
        votingCouncel100.setNumberOfMembers(4);
        votingCouncel100.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel100.setMentor(graba);
        votingCouncelRepository.save(votingCouncel100);

        VotingCouncelEntity votingCouncel101 = new VotingCouncelEntity();
        votingCouncel101.setCode("034Б101");
        votingCouncel101.setName("ЛАУШ I / 1");
        votingCouncel101.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 1");
        votingCouncel101.setNumberOfMembers(4);
        votingCouncel101.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel101.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel101);

        VotingCouncelEntity votingCouncel102 = new VotingCouncelEntity();
        votingCouncel102.setCode("034Б102");
        votingCouncel102.setName("ЛАУШ I / 2");
        votingCouncel102.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 2");
        votingCouncel102.setNumberOfMembers(4);
        votingCouncel102.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel102.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel102);

        VotingCouncelEntity votingCouncel103 = new VotingCouncelEntity();
        votingCouncel103.setCode("034Б103");
        votingCouncel103.setName("ЛАУШ I / 3");
        votingCouncel103.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 3");
        votingCouncel103.setNumberOfMembers(4);
        votingCouncel103.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel103.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel103);

        VotingCouncelEntity votingCouncel104 = new VotingCouncelEntity();
        votingCouncel104.setCode("034Б104");
        votingCouncel104.setName("ЛАУШ I / 4");
        votingCouncel104.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 4");
        votingCouncel104.setNumberOfMembers(4);
        votingCouncel104.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel104.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel104);

        VotingCouncelEntity votingCouncel105 = new VotingCouncelEntity();
        votingCouncel105.setCode("034Б105");
        votingCouncel105.setName("ЛАУШ I / 5");
        votingCouncel105.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 5");
        votingCouncel105.setNumberOfMembers(4);
        votingCouncel105.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel105.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel105);

        VotingCouncelEntity votingCouncel106 = new VotingCouncelEntity();
        votingCouncel106.setCode("034Б106");
        votingCouncel106.setName("ЛАУШ I / 6");
        votingCouncel106.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 6");
        votingCouncel106.setNumberOfMembers(4);
        votingCouncel106.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel106.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel106);

        VotingCouncelEntity votingCouncel107 = new VotingCouncelEntity();
        votingCouncel107.setCode("034Б107");
        votingCouncel107.setName("ЛАУШ I / 7");
        votingCouncel107.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 7");
        votingCouncel107.setNumberOfMembers(4);
        votingCouncel107.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel107.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel107);

        VotingCouncelEntity votingCouncel108 = new VotingCouncelEntity();
        votingCouncel108.setCode("034Б108");
        votingCouncel108.setName("ЛАУШ I / 8");
        votingCouncel108.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 8");
        votingCouncel108.setNumberOfMembers(4);
        votingCouncel108.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel108.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel108);

        VotingCouncelEntity votingCouncel109 = new VotingCouncelEntity();
        votingCouncel109.setCode("034Б109");
        votingCouncel109.setName("ЛАУШ I / 9");
        votingCouncel109.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 9");
        votingCouncel109.setNumberOfMembers(4);
        votingCouncel109.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel109.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel109);

        VotingCouncelEntity votingCouncel110 = new VotingCouncelEntity();
        votingCouncel110.setCode("034Б110");
        votingCouncel110.setName("ЛАУШ II / 1");
        votingCouncel110.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 10");
        votingCouncel110.setNumberOfMembers(4);
        votingCouncel110.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel110.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel110);

        VotingCouncelEntity votingCouncel111 = new VotingCouncelEntity();
        votingCouncel111.setCode("034Б111");
        votingCouncel111.setName("ЛАУШ II / 2");
        votingCouncel111.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 11");
        votingCouncel111.setNumberOfMembers(4);
        votingCouncel111.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel111.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel111);

        VotingCouncelEntity votingCouncel112 = new VotingCouncelEntity();
        votingCouncel112.setCode("034Б112");
        votingCouncel112.setName("ЛАУШ II / 3");
        votingCouncel112.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 12");
        votingCouncel112.setNumberOfMembers(4);
        votingCouncel112.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel112.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel112);

        VotingCouncelEntity votingCouncel113 = new VotingCouncelEntity();
        votingCouncel113.setCode("034Б113");
        votingCouncel113.setName("ЛАУШ II / 4");
        votingCouncel113.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 13");
        votingCouncel113.setNumberOfMembers(4);
        votingCouncel113.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel113.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel113);

        VotingCouncelEntity votingCouncel114 = new VotingCouncelEntity();
        votingCouncel114.setCode("034Б114");
        votingCouncel114.setName("ЛАУШ II / 5");
        votingCouncel114.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 14");
        votingCouncel114.setNumberOfMembers(4);
        votingCouncel114.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel114.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel114);

        VotingCouncelEntity votingCouncel115 = new VotingCouncelEntity();
        votingCouncel115.setCode("034Б115");
        votingCouncel115.setName("ЛАУШ II / 6");
        votingCouncel115.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 15");
        votingCouncel115.setNumberOfMembers(4);
        votingCouncel115.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel115.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel115);

        VotingCouncelEntity votingCouncel116 = new VotingCouncelEntity();
        votingCouncel116.setCode("034Б116");
        votingCouncel116.setName("ЛАУШ II / 7");
        votingCouncel116.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 16");
        votingCouncel116.setNumberOfMembers(4);
        votingCouncel116.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel116.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel116);

        VotingCouncelEntity votingCouncel117 = new VotingCouncelEntity();
        votingCouncel117.setCode("034Б117");
        votingCouncel117.setName("ЉУБАЧЕВО");
        votingCouncel117.setLocation("ПШ \"МИЛАН РАКИЋ\", Љубачево, уч. 1");
        votingCouncel117.setNumberOfMembers(4);
        votingCouncel117.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel117.setMentor(leona);
        votingCouncelRepository.save(votingCouncel117);

        VotingCouncelEntity votingCouncel118 = new VotingCouncelEntity();
        votingCouncel118.setCode("034Б118");
        votingCouncel118.setName("МИШИН ХАН - 1");
        votingCouncel118.setLocation("ПШ \"МИЛУТИН БОЈИЋ\", Мишин Хан, уч. 1");
        votingCouncel118.setNumberOfMembers(4);
        votingCouncel118.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel118.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel118);

        VotingCouncelEntity votingCouncel119 = new VotingCouncelEntity();
        votingCouncel119.setCode("034Б119");
        votingCouncel119.setName("МОТИКЕ - 1");
        votingCouncel119.setLocation("ПШ \"МИЛОШ ЦРЊАНСКИ\", Мотике, уч. 1");
        votingCouncel119.setNumberOfMembers(4);
        votingCouncel119.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel119.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel119);

        VotingCouncelEntity votingCouncel120 = new VotingCouncelEntity();
        votingCouncel120.setCode("034Б120");
        votingCouncel120.setName("МОТИКЕ - 2");
        votingCouncel120.setLocation("ПШ \"МИЛОШ ЦРЊАНСКИ\", Мотике, уч. 2");
        votingCouncel120.setNumberOfMembers(4);
        votingCouncel120.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel120.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel120);

        VotingCouncelEntity votingCouncel121 = new VotingCouncelEntity();
        votingCouncel121.setCode("034Б121");
        votingCouncel121.setName("НОВА ВАРОШ - 1");
        votingCouncel121.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 1");
        votingCouncel121.setNumberOfMembers(4);
        votingCouncel121.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel121.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel121);

        VotingCouncelEntity votingCouncel122 = new VotingCouncelEntity();
        votingCouncel122.setCode("034Б122");
        votingCouncel122.setName("НОВА ВАРОШ - 2");
        votingCouncel122.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 2");
        votingCouncel122.setNumberOfMembers(4);
        votingCouncel122.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel122.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel122);

        VotingCouncelEntity votingCouncel123 = new VotingCouncelEntity();
        votingCouncel123.setCode("034Б123");
        votingCouncel123.setName("НОВА ВАРОШ - 3");
        votingCouncel123.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 3");
        votingCouncel123.setNumberOfMembers(4);
        votingCouncel123.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel123.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel123);

        VotingCouncelEntity votingCouncel124 = new VotingCouncelEntity();
        votingCouncel124.setCode("034Б124");
        votingCouncel124.setName("НОВА ВАРОШ - 4");
        votingCouncel124.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 4");
        votingCouncel124.setNumberOfMembers(4);
        votingCouncel124.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel124.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel124);

        VotingCouncelEntity votingCouncel125 = new VotingCouncelEntity();
        votingCouncel125.setCode("034Б125");
        votingCouncel125.setName("НОВА ВАРОШ - 5");
        votingCouncel125.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 5");
        votingCouncel125.setNumberOfMembers(4);
        votingCouncel125.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel125.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel125);

        VotingCouncelEntity votingCouncel126 = new VotingCouncelEntity();
        votingCouncel126.setCode("034Б126");
        votingCouncel126.setName("ОБИЛИЋЕВО I / 1");
        votingCouncel126.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 1");
        votingCouncel126.setNumberOfMembers(4);
        votingCouncel126.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel126.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel126);

        VotingCouncelEntity votingCouncel127 = new VotingCouncelEntity();
        votingCouncel127.setCode("034Б127");
        votingCouncel127.setName("ОБИЛИЋЕВО I / 2");
        votingCouncel127.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 2");
        votingCouncel127.setNumberOfMembers(4);
        votingCouncel127.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel127.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel127);

        VotingCouncelEntity votingCouncel128 = new VotingCouncelEntity();
        votingCouncel128.setCode("034Б128");
        votingCouncel128.setName("ОБИЛИЋЕВО I / 3");
        votingCouncel128.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 3");
        votingCouncel128.setNumberOfMembers(4);
        votingCouncel128.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel128.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel128);

        VotingCouncelEntity votingCouncel129 = new VotingCouncelEntity();
        votingCouncel129.setCode("034Б129");
        votingCouncel129.setName("ОБИЛИЋЕВО I / 4");
        votingCouncel129.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 4");
        votingCouncel129.setNumberOfMembers(4);
        votingCouncel129.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel129.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel129);

        VotingCouncelEntity votingCouncel130 = new VotingCouncelEntity();
        votingCouncel130.setCode("034Б130");
        votingCouncel130.setName("ОБИЛИЋЕВО I / 5");
        votingCouncel130.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 5");
        votingCouncel130.setNumberOfMembers(4);
        votingCouncel130.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel130.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel130);

        VotingCouncelEntity votingCouncel131 = new VotingCouncelEntity();
        votingCouncel131.setCode("034Б131");
        votingCouncel131.setName("ОБИЛИЋЕВО I / 6");
        votingCouncel131.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 6");
        votingCouncel131.setNumberOfMembers(4);
        votingCouncel131.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel131.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel131);

        VotingCouncelEntity votingCouncel132 = new VotingCouncelEntity();
        votingCouncel132.setCode("034Б132");
        votingCouncel132.setName("ОБИЛИЋЕВО I / 7");
        votingCouncel132.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 7");
        votingCouncel132.setNumberOfMembers(4);
        votingCouncel132.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel132.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel132);

        VotingCouncelEntity votingCouncel133 = new VotingCouncelEntity();
        votingCouncel133.setCode("034Б133");
        votingCouncel133.setName("ОБИЛИЋЕВО I / 8");
        votingCouncel133.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 8");
        votingCouncel133.setNumberOfMembers(4);
        votingCouncel133.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel133.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel133);

        VotingCouncelEntity votingCouncel134 = new VotingCouncelEntity();
        votingCouncel134.setCode("034Б134");
        votingCouncel134.setName("ОБИЛИЋЕВО I / 9");
        votingCouncel134.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 9");
        votingCouncel134.setNumberOfMembers(4);
        votingCouncel134.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel134.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel134);

        VotingCouncelEntity votingCouncel135 = new VotingCouncelEntity();
        votingCouncel135.setCode("034Б135");
        votingCouncel135.setName("ОБИЛИЋЕВО I / 10");
        votingCouncel135.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 10");
        votingCouncel135.setNumberOfMembers(4);
        votingCouncel135.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel135.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel135);

        VotingCouncelEntity votingCouncel136 = new VotingCouncelEntity();
        votingCouncel136.setCode("034Б136");
        votingCouncel136.setName("ОБИЛИЋЕВО I / 11");
        votingCouncel136.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 11");
        votingCouncel136.setNumberOfMembers(4);
        votingCouncel136.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel136.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel136);

        VotingCouncelEntity votingCouncel137 = new VotingCouncelEntity();
        votingCouncel137.setCode("034Б137");
        votingCouncel137.setName("ОБИЛИЋЕВО II / 1");
        votingCouncel137.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 1");
        votingCouncel137.setNumberOfMembers(4);
        votingCouncel137.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel137.setMentor(graba);
        votingCouncelRepository.save(votingCouncel137);

        VotingCouncelEntity votingCouncel138 = new VotingCouncelEntity();
        votingCouncel138.setCode("034Б138");
        votingCouncel138.setName("ОБИЛИЋЕВО II / 2");
        votingCouncel138.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 2");
        votingCouncel138.setNumberOfMembers(4);
        votingCouncel138.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel138.setMentor(graba);
        votingCouncelRepository.save(votingCouncel138);

        VotingCouncelEntity votingCouncel139 = new VotingCouncelEntity();
        votingCouncel139.setCode("034Б139");
        votingCouncel139.setName("ОБИЛИЋЕВО II / 3");
        votingCouncel139.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 3");
        votingCouncel139.setNumberOfMembers(4);
        votingCouncel139.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel139.setMentor(graba);
        votingCouncelRepository.save(votingCouncel139);

        VotingCouncelEntity votingCouncel140 = new VotingCouncelEntity();
        votingCouncel140.setCode("034Б140");
        votingCouncel140.setName("ОБИЛИЋЕВО II / 4");
        votingCouncel140.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 4");
        votingCouncel140.setNumberOfMembers(4);
        votingCouncel140.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel140.setMentor(graba);
        votingCouncelRepository.save(votingCouncel140);

        VotingCouncelEntity votingCouncel141 = new VotingCouncelEntity();
        votingCouncel141.setCode("034Б141");
        votingCouncel141.setName("ОБИЛИЋЕВО II / 5");
        votingCouncel141.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 5");
        votingCouncel141.setNumberOfMembers(4);
        votingCouncel141.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel141.setMentor(graba);
        votingCouncelRepository.save(votingCouncel141);

        VotingCouncelEntity votingCouncel142 = new VotingCouncelEntity();
        votingCouncel142.setCode("034Б142");
        votingCouncel142.setName("ОБИЛИЋЕВО II / 6");
        votingCouncel142.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 6");
        votingCouncel142.setNumberOfMembers(4);
        votingCouncel142.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel142.setMentor(graba);
        votingCouncelRepository.save(votingCouncel142);

        VotingCouncelEntity votingCouncel143 = new VotingCouncelEntity();
        votingCouncel143.setCode("034Б143");
        votingCouncel143.setName("ОБИЛИЋЕВО II / 7");
        votingCouncel143.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 7");
        votingCouncel143.setNumberOfMembers(4);
        votingCouncel143.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel143.setMentor(graba);
        votingCouncelRepository.save(votingCouncel143);

        VotingCouncelEntity votingCouncel144 = new VotingCouncelEntity();
        votingCouncel144.setCode("034Б144");
        votingCouncel144.setName("ОБИЛИЋЕВО II / 8");
        votingCouncel144.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 8");
        votingCouncel144.setNumberOfMembers(4);
        votingCouncel144.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel144.setMentor(graba);
        votingCouncelRepository.save(votingCouncel144);

        VotingCouncelEntity votingCouncel145 = new VotingCouncelEntity();
        votingCouncel145.setCode("034Б145");
        votingCouncel145.setName("ОБИЛИЋЕВО II / 9");
        votingCouncel145.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 9");
        votingCouncel145.setNumberOfMembers(4);
        votingCouncel145.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel145.setMentor(graba);
        votingCouncelRepository.save(votingCouncel145);

        VotingCouncelEntity votingCouncel146 = new VotingCouncelEntity();
        votingCouncel146.setCode("034Б146");
        votingCouncel146.setName("ОБИЛИЋЕВО II / 10");
        votingCouncel146.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 10");
        votingCouncel146.setNumberOfMembers(4);
        votingCouncel146.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel146.setMentor(graba);
        votingCouncelRepository.save(votingCouncel146);

        VotingCouncelEntity votingCouncel147 = new VotingCouncelEntity();
        votingCouncel147.setCode("034Б147");
        votingCouncel147.setName("ОБИЛИЋЕВО II / 11");
        votingCouncel147.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 11");
        votingCouncel147.setNumberOfMembers(4);
        votingCouncel147.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel147.setMentor(graba);
        votingCouncelRepository.save(votingCouncel147);

        VotingCouncelEntity votingCouncel148 = new VotingCouncelEntity();
        votingCouncel148.setCode("034Б148");
        votingCouncel148.setName("ОБИЛИЋЕВО II / 12");
        votingCouncel148.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 12");
        votingCouncel148.setNumberOfMembers(4);
        votingCouncel148.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel148.setMentor(graba);
        votingCouncelRepository.save(votingCouncel148);

        VotingCouncelEntity votingCouncel149 = new VotingCouncelEntity();
        votingCouncel149.setCode("034Б149");
        votingCouncel149.setName("ПАВИЋИ");
        votingCouncel149.setLocation("ПШ \"ПЕТАР КОЧИЋ\", Павићи, уч. 1");
        votingCouncel149.setNumberOfMembers(4);
        votingCouncel149.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel149.setMentor(igor);
        votingCouncelRepository.save(votingCouncel149);

        VotingCouncelEntity votingCouncel150 = new VotingCouncelEntity();
        votingCouncel150.setCode("034Б150");
        votingCouncel150.setName("РОСУЉЕ - 1");
        votingCouncel150.setLocation("ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 1");
        votingCouncel150.setNumberOfMembers(4);
        votingCouncel150.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel150.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel150);

        VotingCouncelEntity votingCouncel151 = new VotingCouncelEntity();
        votingCouncel151.setCode("034Б151");
        votingCouncel151.setName("ПЕТРИЋЕВАЦ - 1");
        votingCouncel151.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 1");
        votingCouncel151.setNumberOfMembers(4);
        votingCouncel151.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel151.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel151);

        VotingCouncelEntity votingCouncel152 = new VotingCouncelEntity();
        votingCouncel152.setCode("034Б152");
        votingCouncel152.setName("ПАПРИКОВАЦ 1");
        votingCouncel152.setLocation("ШКОЛА ЗА СЛУШНО ОШТЕЋЕНЕ, Др Ј. Рашковића 28, уч. 1");
        votingCouncel152.setNumberOfMembers(4);
        votingCouncel152.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel152.setMentor(igor);
        votingCouncelRepository.save(votingCouncel152);

        VotingCouncelEntity votingCouncel153 = new VotingCouncelEntity();
        votingCouncel153.setCode("034Б153");
        votingCouncel153.setName("ПАПРИКОВАЦ 2");
        votingCouncel153.setLocation("ШКОЛА ЗА СЛУШНО ОШТЕЋЕНЕ, Др Ј. Рашковића 28, уч. 2");
        votingCouncel153.setNumberOfMembers(4);
        votingCouncel153.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel153.setMentor(igor);
        votingCouncelRepository.save(votingCouncel153);

        VotingCouncelEntity votingCouncel154 = new VotingCouncelEntity();
        votingCouncel154.setCode("034Б154");
        votingCouncel154.setName("ПАПРИКОВАЦ 3");
        votingCouncel154.setLocation("ШКОЛА ЗА СЛУШНО ОШТЕЋЕНЕ, Др Ј. Рашковића 28, уч. 3");
        votingCouncel154.setNumberOfMembers(4);
        votingCouncel154.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel154.setMentor(igor);
        votingCouncelRepository.save(votingCouncel154);

        VotingCouncelEntity votingCouncel155 = new VotingCouncelEntity();
        votingCouncel155.setCode("034Б155");
        votingCouncel155.setName("ПАПРИКОВАЦ 4");
        votingCouncel155.setLocation("ШКОЛА ЗА СЛУШНО ОШТЕЋЕНЕ, Др Ј. Рашковића 28, уч. 4");
        votingCouncel155.setNumberOfMembers(4);
        votingCouncel155.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel155.setMentor(igor);
        votingCouncelRepository.save(votingCouncel155);

        VotingCouncelEntity votingCouncel156 = new VotingCouncelEntity();
        votingCouncel156.setCode("034Б156");
        votingCouncel156.setName("ПАПРИКОВАЦ 5");
        votingCouncel156.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 6");
        votingCouncel156.setNumberOfMembers(4);
        votingCouncel156.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel156.setMentor(igor);
        votingCouncelRepository.save(votingCouncel156);

        VotingCouncelEntity votingCouncel157 = new VotingCouncelEntity();
        votingCouncel157.setCode("034Б157");
        votingCouncel157.setName("ПАПРИКОВАЦ 6");
        votingCouncel157.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 7");
        votingCouncel157.setNumberOfMembers(4);
        votingCouncel157.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel157.setMentor(igor);
        votingCouncelRepository.save(votingCouncel157);

        VotingCouncelEntity votingCouncel158 = new VotingCouncelEntity();
        votingCouncel158.setCode("034Б158");
        votingCouncel158.setName("ПАПРИКОВАЦ 7");
        votingCouncel158.setLocation("ЗАВОД ЗА ДИСТРОФИЧАРЕ, Војвођанска бб, сала 1");
        votingCouncel158.setNumberOfMembers(4);
        votingCouncel158.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel158.setMentor(igor);
        votingCouncelRepository.save(votingCouncel158);

        VotingCouncelEntity votingCouncel159 = new VotingCouncelEntity();
        votingCouncel159.setCode("034Б159");
        votingCouncel159.setName("ПАПРИКОВАЦ 8");
        votingCouncel159.setLocation("ЗАВОД ЗА ДИСТРОФИЧАРЕ, Војвођанска бб, сала 2");
        votingCouncel159.setNumberOfMembers(4);
        votingCouncel159.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel159.setMentor(igor);
        votingCouncelRepository.save(votingCouncel159);

        VotingCouncelEntity votingCouncel160 = new VotingCouncelEntity();
        votingCouncel160.setCode("034Б160");
        votingCouncel160.setName("ПЕТРИЋЕВАЦ 2");
        votingCouncel160.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 2");
        votingCouncel160.setNumberOfMembers(4);
        votingCouncel160.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel160.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel160);

        VotingCouncelEntity votingCouncel161 = new VotingCouncelEntity();
        votingCouncel161.setCode("034Б161");
        votingCouncel161.setName("ПЕТРИЋЕВАЦ 3");
        votingCouncel161.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 3");
        votingCouncel161.setNumberOfMembers(4);
        votingCouncel161.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel161.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel161);

        VotingCouncelEntity votingCouncel162 = new VotingCouncelEntity();
        votingCouncel162.setCode("034Б162");
        votingCouncel162.setName("ПЕТРИЋЕВАЦ 4");
        votingCouncel162.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 4");
        votingCouncel162.setNumberOfMembers(4);
        votingCouncel162.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel162.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel162);

        VotingCouncelEntity votingCouncel163 = new VotingCouncelEntity();
        votingCouncel163.setCode("034Б163");
        votingCouncel163.setName("ПЕТРИЋЕВАЦ 5");
        votingCouncel163.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 5");
        votingCouncel163.setNumberOfMembers(4);
        votingCouncel163.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel163.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel163);

        VotingCouncelEntity votingCouncel164 = new VotingCouncelEntity();
        votingCouncel164.setCode("034Б164");
        votingCouncel164.setName("ПЕТРИЋЕВАЦ 6");
        votingCouncel164.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 6");
        votingCouncel164.setNumberOfMembers(4);
        votingCouncel164.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel164.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel164);

        VotingCouncelEntity votingCouncel165 = new VotingCouncelEntity();
        votingCouncel165.setCode("034Б165");
        votingCouncel165.setName("ПЕТРИЋЕВАЦ 7");
        votingCouncel165.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 7");
        votingCouncel165.setNumberOfMembers(4);
        votingCouncel165.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel165.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel165);

        VotingCouncelEntity votingCouncel166 = new VotingCouncelEntity();
        votingCouncel166.setCode("034Б166");
        votingCouncel166.setName("ПЕТРИЋЕВАЦ 8");
        votingCouncel166.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 8");
        votingCouncel166.setNumberOfMembers(4);
        votingCouncel166.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel166.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel166);

        VotingCouncelEntity votingCouncel167 = new VotingCouncelEntity();
        votingCouncel167.setCode("034Б167");
        votingCouncel167.setName("ПЕТРИЋЕВАЦ 9");
        votingCouncel167.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 9");
        votingCouncel167.setNumberOfMembers(4);
        votingCouncel167.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel167.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel167);

        VotingCouncelEntity votingCouncel168 = new VotingCouncelEntity();
        votingCouncel168.setCode("034Б168");
        votingCouncel168.setName("ПИСКАВИЦА 1");
        votingCouncel168.setLocation("ОШ \"ЋИРИЛО И МЕТОДИЈЕ\", Пискавица, уч. 1");
        votingCouncel168.setNumberOfMembers(4);
        votingCouncel168.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel168.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel168);

        VotingCouncelEntity votingCouncel169 = new VotingCouncelEntity();
        votingCouncel169.setCode("034Б169");
        votingCouncel169.setName("ПИСКАВИЦА 2");
        votingCouncel169.setLocation("ОШ \"ЋИРИЛО И МЕТОДИЈЕ\", Пискавица, уч. 2");
        votingCouncel169.setNumberOfMembers(4);
        votingCouncel169.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel169.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel169);

        VotingCouncelEntity votingCouncel170 = new VotingCouncelEntity();
        votingCouncel170.setCode("034Б170");
        votingCouncel170.setName("ПОБРЂЕ 1");
        votingCouncel170.setLocation("ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 6");
        votingCouncel170.setNumberOfMembers(4);
        votingCouncel170.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel170.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel170);

        VotingCouncelEntity votingCouncel171 = new VotingCouncelEntity();
        votingCouncel171.setCode("034Б171");
        votingCouncel171.setName("ПОБРЂЕ 2");
        votingCouncel171.setLocation("ГИМНАЗИЈА, Змај Јовина 13, уч. 3");
        votingCouncel171.setNumberOfMembers(4);
        votingCouncel171.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel171.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel171);

        VotingCouncelEntity votingCouncel172 = new VotingCouncelEntity();
        votingCouncel172.setCode("034Б172");
        votingCouncel172.setName("ПОБРЂЕ 3");
        votingCouncel172.setLocation("ГИМНАЗИЈА, Змај Јовина 13, уч. 4");
        votingCouncel172.setNumberOfMembers(4);
        votingCouncel172.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel172.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel172);

        VotingCouncelEntity votingCouncel173A = new VotingCouncelEntity();
        votingCouncel173A.setCode("034Б173 А");
        votingCouncel173A.setName("ПОТКОЗАРЈЕ");
        votingCouncel173A.setLocation("ОШ \"МИЛУТИН БОЈИЋ\", Поткозарје, уч. 1");
        votingCouncel173A.setNumberOfMembers(4);
        votingCouncel173A.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel173A.setMentor(igor);
        votingCouncelRepository.save(votingCouncel173A);

        VotingCouncelEntity votingCouncel173B = new VotingCouncelEntity();
        votingCouncel173B.setCode("034Б173 Б");
        votingCouncel173B.setName("ПОТКОЗАРЈЕ");
        votingCouncel173B.setLocation("ОШ \"МИЛУТИН БОЈИЋ\", Поткозарје, уч. 2");
        votingCouncel173B.setNumberOfMembers(4);
        votingCouncel173B.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel173B.setMentor(igor);
        votingCouncelRepository.save(votingCouncel173B);

        VotingCouncelEntity votingCouncel174 = new VotingCouncelEntity();
        votingCouncel174.setCode("034Б174");
        votingCouncel174.setName("ПРИЈАКОВЦИ");
        votingCouncel174.setLocation("МЗ ПРИЈАКОВЦИ, сала 1");
        votingCouncel174.setNumberOfMembers(4);
        votingCouncel174.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel174.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel174);

        VotingCouncelEntity votingCouncel175 = new VotingCouncelEntity();
        votingCouncel175.setCode("034Б175");
        votingCouncel175.setName("ПРИЈЕЧАНИ - 1");
        votingCouncel175.setLocation("ПШ \"ЈОВАН ДУЧИЋ\", Пријечани, уч. 1");
        votingCouncel175.setNumberOfMembers(4);
        votingCouncel175.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel175.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel175);

        VotingCouncelEntity votingCouncel176 = new VotingCouncelEntity();
        votingCouncel176.setCode("034Б176");
        votingCouncel176.setName("РЕКАВИЦЕ I");
        votingCouncel176.setLocation("ПШ \"БРАНИСЛАВ НУШИЋ\", Рекавице I, уч. 1");
        votingCouncel176.setNumberOfMembers(4);
        votingCouncel176.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel176.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel176);

        VotingCouncelEntity votingCouncel177 = new VotingCouncelEntity();
        votingCouncel177.setCode("034Б177");
        votingCouncel177.setName("РЕКАВИЦЕ II");
        votingCouncel177.setLocation("МЗ РЕКАВИЦЕ II, сала 1");
        votingCouncel177.setNumberOfMembers(4);
        votingCouncel177.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel177.setMentor(leona);
        votingCouncelRepository.save(votingCouncel177);

        VotingCouncelEntity votingCouncel178 = new VotingCouncelEntity();
        votingCouncel178.setCode("034Б178");
        votingCouncel178.setName("РОСУЉЕ 2");
        votingCouncel178.setLocation("МЗ РОСУЉЕ, Др Младена Стојановића 10, сала 1");
        votingCouncel178.setNumberOfMembers(4);
        votingCouncel178.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel178.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel178);

        VotingCouncelEntity votingCouncel179 = new VotingCouncelEntity();
        votingCouncel179.setCode("034Б179");
        votingCouncel179.setName("РОСУЉЕ 3");
        votingCouncel179.setLocation("МЗ РОСУЉЕ, Др Младена Стојановића 10, сала 2");
        votingCouncel179.setNumberOfMembers(4);
        votingCouncel179.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel179.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel179);

        VotingCouncelEntity votingCouncel180 = new VotingCouncelEntity();
        votingCouncel180.setCode("034Б180");
        votingCouncel180.setName("РОСУЉЕ 4");
        votingCouncel180.setLocation("ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 2");
        votingCouncel180.setNumberOfMembers(4);
        votingCouncel180.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel180.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel180);

        VotingCouncelEntity votingCouncel181 = new VotingCouncelEntity();
        votingCouncel181.setCode("034Б181");
        votingCouncel181.setName("РОСУЉЕ 5");
        votingCouncel181.setLocation("ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 3");
        votingCouncel181.setNumberOfMembers(4);
        votingCouncel181.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel181.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel181);

        VotingCouncelEntity votingCouncel182 = new VotingCouncelEntity();
        votingCouncel182.setCode("034Б182");
        votingCouncel182.setName("РОСУЉЕ 6");
        votingCouncel182.setLocation("ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 4");
        votingCouncel182.setNumberOfMembers(4);
        votingCouncel182.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel182.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel182);

        VotingCouncelEntity votingCouncel183 = new VotingCouncelEntity();
        votingCouncel183.setCode("034Б183");
        votingCouncel183.setName("РОСУЉЕ 7");
        votingCouncel183.setLocation("ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 5");
        votingCouncel183.setNumberOfMembers(4);
        votingCouncel183.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel183.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel183);

        VotingCouncelEntity votingCouncel184 = new VotingCouncelEntity();
        votingCouncel184.setCode("034Б184");
        votingCouncel184.setName("РОСУЉЕ 8");
        votingCouncel184.setLocation("ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 6");
        votingCouncel184.setNumberOfMembers(4);
        votingCouncel184.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel184.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel184);

        VotingCouncelEntity votingCouncel185 = new VotingCouncelEntity();
        votingCouncel185.setCode("034Б185");
        votingCouncel185.setName("РОСУЉЕ 9");
        votingCouncel185.setLocation("КИНОЛОШКИ САВЕЗ БАЊА ЛУКА, Битољска бб, сала 4");
        votingCouncel185.setNumberOfMembers(4);
        votingCouncel185.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel185.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel185);

        VotingCouncelEntity votingCouncel186 = new VotingCouncelEntity();
        votingCouncel186.setCode("034Б186");
        votingCouncel186.setName("САРАЧИЦА 1");
        votingCouncel186.setLocation("ДРУШТВЕНИ ДОМ САРАЧИЦА, сала 1");
        votingCouncel186.setNumberOfMembers(4);
        votingCouncel186.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel186.setMentor(graba);
        votingCouncelRepository.save(votingCouncel186);

        VotingCouncelEntity votingCouncel187 = new VotingCouncelEntity();
        votingCouncel187.setCode("034Б187");
        votingCouncel187.setName("САРАЧИЦА 2 / ПАВЛОВАЦ 1");
        votingCouncel187.setLocation("ПШ \"СВЕТИ САВА\", Павловац, уч. 1");
        votingCouncel187.setNumberOfMembers(4);
        votingCouncel187.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel187.setMentor(graba);
        votingCouncelRepository.save(votingCouncel187);

        VotingCouncelEntity votingCouncel188 = new VotingCouncelEntity();
        votingCouncel188.setCode("034Б188");
        votingCouncel188.setName("СРПСКЕ ТОПЛИЦЕ 1");
        votingCouncel188.setLocation("ОШ \"БРАНИСЛАВ НУШИЋ\", Мањачких устаника 32, уч. 1");
        votingCouncel188.setNumberOfMembers(4);
        votingCouncel188.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel188.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel188);

        VotingCouncelEntity votingCouncel189 = new VotingCouncelEntity();
        votingCouncel189.setCode("034Б189");
        votingCouncel189.setName("СРПСКЕ ТОПЛИЦЕ 2");
        votingCouncel189.setLocation("ОШ \"БРАНИСЛАВ НУШИЋ\", Мањачких устаника 32, уч. 2");
        votingCouncel189.setNumberOfMembers(4);
        votingCouncel189.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel189.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel189);

        VotingCouncelEntity votingCouncel190 = new VotingCouncelEntity();
        votingCouncel190.setCode("034Б190");
        votingCouncel190.setName("СРПСКЕ ТОПЛИЦЕ 3");
        votingCouncel190.setLocation("ОШ \"БРАНИСЛАВ НУШИЋ\", Мањачких устаника 32, уч. 3");
        votingCouncel190.setNumberOfMembers(4);
        votingCouncel190.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel190.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel190);

        VotingCouncelEntity votingCouncel191 = new VotingCouncelEntity();
        votingCouncel191.setCode("034Б191");
        votingCouncel191.setName("СРПСКЕ ТОПЛИЦЕ - 4 / НОВОСЕЛИЈА 1");
        votingCouncel191.setLocation("ПШ \"МИЛАН РАКИЋ\", Краг. краљ. жртава 59, уч. 1");
        votingCouncel191.setNumberOfMembers(4);
        votingCouncel191.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel191.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel191);

        VotingCouncelEntity votingCouncel192 = new VotingCouncelEntity();
        votingCouncel192.setCode("034Б192");
        votingCouncel192.setName("СРПСКЕ ТОПЛИЦЕ - 5 / НОВОСЕЛИЈА 2");
        votingCouncel192.setLocation("ПШ \"МИЛАН РАКИЋ\", Краг. краљ. жртава 59, уч. 2");
        votingCouncel192.setNumberOfMembers(4);
        votingCouncel192.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel192.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel192);

        VotingCouncelEntity votingCouncel193 = new VotingCouncelEntity();
        votingCouncel193.setCode("034Б193");
        votingCouncel193.setName("СТАРЧЕВИЦА 1");
        votingCouncel193.setLocation("МЗ СТРАЧЕВИЦА, Др В. Ђ. Кецмановића 1, сала 1");
        votingCouncel193.setNumberOfMembers(4);
        votingCouncel193.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel193.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel193);

        VotingCouncelEntity votingCouncel194 = new VotingCouncelEntity();
        votingCouncel194.setCode("034Б194");
        votingCouncel194.setName("СТАРЧЕВИЦА 2");
        votingCouncel194.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 1");
        votingCouncel194.setNumberOfMembers(4);
        votingCouncel194.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel194.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel194);

        VotingCouncelEntity votingCouncel195 = new VotingCouncelEntity();
        votingCouncel195.setCode("034Б195");
        votingCouncel195.setName("СТАРЧЕВИЦА 3");
        votingCouncel195.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 2");
        votingCouncel195.setNumberOfMembers(4);
        votingCouncel195.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel195.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel195);

        VotingCouncelEntity votingCouncel196 = new VotingCouncelEntity();
        votingCouncel196.setCode("034Б196");
        votingCouncel196.setName("СТАРЧЕВИЦА 4");
        votingCouncel196.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 3");
        votingCouncel196.setNumberOfMembers(4);
        votingCouncel196.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel196.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel196);

        VotingCouncelEntity votingCouncel197 = new VotingCouncelEntity();
        votingCouncel197.setCode("034Б197");
        votingCouncel197.setName("СТАРЧЕВИЦА 5");
        votingCouncel197.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 4");
        votingCouncel197.setNumberOfMembers(4);
        votingCouncel197.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel197.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel197);

        VotingCouncelEntity votingCouncel198 = new VotingCouncelEntity();
        votingCouncel198.setCode("034Б198");
        votingCouncel198.setName("СТАРЧЕВИЦА 6");
        votingCouncel198.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 5");
        votingCouncel198.setNumberOfMembers(4);
        votingCouncel198.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel198.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel198);

        VotingCouncelEntity votingCouncel199 = new VotingCouncelEntity();
        votingCouncel199.setCode("034Б199");
        votingCouncel199.setName("СТАРЧЕВИЦА 7");
        votingCouncel199.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 6");
        votingCouncel199.setNumberOfMembers(4);
        votingCouncel199.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel199.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel199);

        VotingCouncelEntity votingCouncel200 = new VotingCouncelEntity();
        votingCouncel200.setCode("034Б200");
        votingCouncel200.setName("СТАРЧЕВИЦА 8");
        votingCouncel200.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 7");
        votingCouncel200.setNumberOfMembers(4);
        votingCouncel200.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel200.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel200);

        VotingCouncelEntity votingCouncel201 = new VotingCouncelEntity();
        votingCouncel201.setCode("034Б201");
        votingCouncel201.setName("СТАРЧЕВИЦА 9");
        votingCouncel201.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 8");
        votingCouncel201.setNumberOfMembers(4);
        votingCouncel201.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel201.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel201);

        VotingCouncelEntity votingCouncel202 = new VotingCouncelEntity();
        votingCouncel202.setCode("034Б202");
        votingCouncel202.setName("СТАРЧЕВИЦА 10");
        votingCouncel202.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 9");
        votingCouncel202.setNumberOfMembers(4);
        votingCouncel202.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel202.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel202);

        VotingCouncelEntity votingCouncel203 = new VotingCouncelEntity();
        votingCouncel203.setCode("034Б203");
        votingCouncel203.setName("СТАРЧЕВИЦА 11");
        votingCouncel203.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 10");
        votingCouncel203.setNumberOfMembers(4);
        votingCouncel203.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel203.setMentor(igor);
        votingCouncelRepository.save(votingCouncel203);

        VotingCouncelEntity votingCouncel204 = new VotingCouncelEntity();
        votingCouncel204.setCode("034Б204");
        votingCouncel204.setName("СТАРЧЕВИЦА 12");
        votingCouncel204.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 11");
        votingCouncel204.setNumberOfMembers(4);
        votingCouncel204.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel204.setMentor(igor);
        votingCouncelRepository.save(votingCouncel204);

        VotingCouncelEntity votingCouncel205 = new VotingCouncelEntity();
        votingCouncel205.setCode("034Б205");
        votingCouncel205.setName("СТАРЧЕВИЦА 13");
        votingCouncel205.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 12");
        votingCouncel205.setNumberOfMembers(4);
        votingCouncel205.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel205.setMentor(igor);
        votingCouncelRepository.save(votingCouncel205);

        VotingCouncelEntity votingCouncel206 = new VotingCouncelEntity();
        votingCouncel206.setCode("034Б206");
        votingCouncel206.setName("СТАРЧЕВИЦА 14");
        votingCouncel206.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 13");
        votingCouncel206.setNumberOfMembers(4);
        votingCouncel206.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel206.setMentor(igor);
        votingCouncelRepository.save(votingCouncel206);

        VotingCouncelEntity votingCouncel207 = new VotingCouncelEntity();
        votingCouncel207.setCode("034Б207");
        votingCouncel207.setName("СТАРЧЕВИЦА 15");
        votingCouncel207.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 14");
        votingCouncel207.setNumberOfMembers(4);
        votingCouncel207.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel207.setMentor(igor);
        votingCouncelRepository.save(votingCouncel207);

        VotingCouncelEntity votingCouncel208 = new VotingCouncelEntity();
        votingCouncel208.setCode("034Б208");
        votingCouncel208.setName("СТАРЧЕВИЦА 16");
        votingCouncel208.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 15");
        votingCouncel208.setNumberOfMembers(4);
        votingCouncel208.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel208.setMentor(igor);
        votingCouncelRepository.save(votingCouncel208);

        VotingCouncelEntity votingCouncel209 = new VotingCouncelEntity();
        votingCouncel209.setCode("034Б209");
        votingCouncel209.setName("СТАРЧЕВИЦА 17");
        votingCouncel209.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 16");
        votingCouncel209.setNumberOfMembers(4);
        votingCouncel209.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel209.setMentor(igor);
        votingCouncelRepository.save(votingCouncel209);

        VotingCouncelEntity votingCouncel210 = new VotingCouncelEntity();
        votingCouncel210.setCode("034Б210");
        votingCouncel210.setName("СТАРЧЕВИЦА 18");
        votingCouncel210.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 17");
        votingCouncel210.setNumberOfMembers(4);
        votingCouncel210.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel210.setMentor(igor);
        votingCouncelRepository.save(votingCouncel210);

        VotingCouncelEntity votingCouncel211 = new VotingCouncelEntity();
        votingCouncel211.setCode("034Б211");
        votingCouncel211.setName("СТАРЧЕВИЦА 19");
        votingCouncel211.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 18");
        votingCouncel211.setNumberOfMembers(4);
        votingCouncel211.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel211.setMentor(igor);
        votingCouncelRepository.save(votingCouncel211);

        VotingCouncelEntity votingCouncel212 = new VotingCouncelEntity();
        votingCouncel212.setCode("034Б212");
        votingCouncel212.setName("СТАРЧЕВИЦА 20");
        votingCouncel212.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 19");
        votingCouncel212.setNumberOfMembers(4);
        votingCouncel212.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel212.setMentor(igor);
        votingCouncelRepository.save(votingCouncel212);

        VotingCouncelEntity votingCouncel213 = new VotingCouncelEntity();
        votingCouncel213.setCode("034Б213");
        votingCouncel213.setName("СТРАТИНСКА");
        votingCouncel213.setLocation("ПШ \"МЛАДЕН СТОЈАНОВИЋ\", Стратинска, уч. 1");
        votingCouncel213.setNumberOfMembers(2);
        votingCouncel213.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel213.setMentor(graba);
        votingCouncelRepository.save(votingCouncel213);

        VotingCouncelEntity votingCouncel214 = new VotingCouncelEntity();
        votingCouncel214.setCode("034Б214");
        votingCouncel214.setName("СТРИЧИЋИ");
        votingCouncel214.setLocation("МЗ СТРИЧИЋИ, сала 1");
        votingCouncel214.setNumberOfMembers(4);
        votingCouncel214.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel214.setMentor(igor);
        votingCouncelRepository.save(votingCouncel214);

        VotingCouncelEntity votingCouncel215 = new VotingCouncelEntity();
        votingCouncel215.setCode("034Б215");
        votingCouncel215.setName("САРАЧИЦА - 2 /ПАВЛОВАЦ 2");
        votingCouncel215.setLocation("ПШ \"СВЕТИ САВА\", Павловац, уч. 2");
        votingCouncel215.setNumberOfMembers(4);
        votingCouncel215.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel215.setMentor(graba);
        votingCouncelRepository.save(votingCouncel215);

        VotingCouncelEntity votingCouncel216 = new VotingCouncelEntity();
        votingCouncel216.setCode("034Б216");
        votingCouncel216.setName("ЦЕНТАР I / 1");
        votingCouncel216.setLocation("ШКОЛА УЧЕНИКА У ПРИВРЕДИ, Николе Пашића 11а, уч. 1");
        votingCouncel216.setNumberOfMembers(4);
        votingCouncel216.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel216.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel216);

        VotingCouncelEntity votingCouncel217 = new VotingCouncelEntity();
        votingCouncel217.setCode("034Б217");
        votingCouncel217.setName("ЦЕНТАР I / 2");
        votingCouncel217.setLocation("МЗ ЦЕНТАР I, Симе Шолаје 7, сала 1");
        votingCouncel217.setNumberOfMembers(4);
        votingCouncel217.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel217.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel217);

        VotingCouncelEntity votingCouncel218 = new VotingCouncelEntity();
        votingCouncel218.setCode("034Б218");
        votingCouncel218.setName("ЦЕНТАР I / 3");
        votingCouncel218.setLocation("ШКОЛА УЧЕНИКА У ПРИВРЕДИ, Николе Пашића 11а, уч. 2");
        votingCouncel218.setNumberOfMembers(4);
        votingCouncel218.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel218.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel218);

        VotingCouncelEntity votingCouncel219 = new VotingCouncelEntity();
        votingCouncel219.setCode("034Б219");
        votingCouncel219.setName("ЦЕНТАР I / 4");
        votingCouncel219.setLocation("ШКОЛА УЧЕНИКА У ПРИВРЕДИ, Николе Пашића 11а, уч. 3");
        votingCouncel219.setNumberOfMembers(4);
        votingCouncel219.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel219.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel219);

        VotingCouncelEntity votingCouncel220 = new VotingCouncelEntity();
        votingCouncel220.setCode("034Б220");
        votingCouncel220.setName("ЦЕНТАР I / 5");
        votingCouncel220.setLocation("ЕКОНОМСКА ШКОЛА, Краља Алфонса XIII 34, уч. 1");
        votingCouncel220.setNumberOfMembers(4);
        votingCouncel220.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel220.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel220);

        VotingCouncelEntity votingCouncel221 = new VotingCouncelEntity();
        votingCouncel221.setCode("034Б221");
        votingCouncel221.setName("ЦЕНТАР I / 6");
        votingCouncel221.setLocation("ЕКОНОМСКА ШКОЛА, Краља Алфонса XIII 34, уч. 2");
        votingCouncel221.setNumberOfMembers(4);
        votingCouncel221.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel221.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel221);

        VotingCouncelEntity votingCouncel222 = new VotingCouncelEntity();
        votingCouncel222.setCode("034Б222");
        votingCouncel222.setName("ЦЕНТАР I / 7");
        votingCouncel222.setLocation("ЕКОНОМСКА ШКОЛА, Краља Алфонса XIII 34, уч. 3");
        votingCouncel222.setNumberOfMembers(4);
        votingCouncel222.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel222.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel222);

        VotingCouncelEntity votingCouncel223 = new VotingCouncelEntity();
        votingCouncel223.setCode("034Б223");
        votingCouncel223.setName("ЦЕНТАР I / 8");
        votingCouncel223.setLocation("ЕКОНОМСКА ШКОЛА, Краља Алфонса XIII 34, уч. 4");
        votingCouncel223.setNumberOfMembers(4);
        votingCouncel223.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel223.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel223);

        VotingCouncelEntity votingCouncel224 = new VotingCouncelEntity();
        votingCouncel224.setCode("034Б224");
        votingCouncel224.setName("ЦЕНТАР II / 1");
        votingCouncel224.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, фискултурна сала 1");
        votingCouncel224.setNumberOfMembers(4);
        votingCouncel224.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel224.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel224);

        VotingCouncelEntity votingCouncel225 = new VotingCouncelEntity();
        votingCouncel225.setCode("034Б225");
        votingCouncel225.setName("ЦЕНТАР II / 2");
        votingCouncel225.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 1");
        votingCouncel225.setNumberOfMembers(4);
        votingCouncel225.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel225.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel225);

        VotingCouncelEntity votingCouncel226 = new VotingCouncelEntity();
        votingCouncel226.setCode("034Б226");
        votingCouncel226.setName("ЦЕНТАР II / 3");
        votingCouncel226.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 2");
        votingCouncel226.setNumberOfMembers(4);
        votingCouncel226.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel226.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel226);

        VotingCouncelEntity votingCouncel227 = new VotingCouncelEntity();
        votingCouncel227.setCode("034Б227");
        votingCouncel227.setName("ЦЕНТАР II / 4");
        votingCouncel227.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 3");
        votingCouncel227.setNumberOfMembers(4);
        votingCouncel227.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel227.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel227);

        VotingCouncelEntity votingCouncel228 = new VotingCouncelEntity();
        votingCouncel228.setCode("034Б228");
        votingCouncel228.setName("ЦЕНТАР II / 5");
        votingCouncel228.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 4");
        votingCouncel228.setNumberOfMembers(4);
        votingCouncel228.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel228.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel228);

        VotingCouncelEntity votingCouncel229 = new VotingCouncelEntity();
        votingCouncel229.setCode("034Б229");
        votingCouncel229.setName("ЦЕНТАР II / 6");
        votingCouncel229.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 5");
        votingCouncel229.setNumberOfMembers(4);
        votingCouncel229.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel229.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel229);

        VotingCouncelEntity votingCouncel230 = new VotingCouncelEntity();
        votingCouncel230.setCode("034Б230");
        votingCouncel230.setName("ЦЕНТАР II / 7");
        votingCouncel230.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 6");
        votingCouncel230.setNumberOfMembers(4);
        votingCouncel230.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel230.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel230);

        VotingCouncelEntity votingCouncel231 = new VotingCouncelEntity();
        votingCouncel231.setCode("034Б231");
        votingCouncel231.setName("ЦЕНТАР II / 8");
        votingCouncel231.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 7");
        votingCouncel231.setNumberOfMembers(4);
        votingCouncel231.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel231.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel231);

        VotingCouncelEntity votingCouncel232 = new VotingCouncelEntity();
        votingCouncel232.setCode("034Б232");
        votingCouncel232.setName("ЧЕСМА 1");
        votingCouncel232.setLocation("ПРИВАТНИ ОБЈЕКАТ,вл.МАРТИНОВИЋ ДАМИР, Петра Великог 50");
        votingCouncel232.setNumberOfMembers(4);
        votingCouncel232.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel232.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel232);

        VotingCouncelEntity votingCouncel233 = new VotingCouncelEntity();
        votingCouncel233.setCode("034Б233");
        votingCouncel233.setName("ЧЕСМА 2");
        votingCouncel233.setLocation("МЗ ЧЕСМА, Петра Великог 30, сала 1");
        votingCouncel233.setNumberOfMembers(4);
        votingCouncel233.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel233.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel233);

        VotingCouncelEntity votingCouncel234 = new VotingCouncelEntity();
        votingCouncel234.setCode("034Б234");
        votingCouncel234.setName("ЧЕСМА 3");
        votingCouncel234.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Петра Великог 32, уч. 1");
        votingCouncel234.setNumberOfMembers(4);
        votingCouncel234.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel234.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel234);

        VotingCouncelEntity votingCouncel235 = new VotingCouncelEntity();
        votingCouncel235.setCode("034Б235");
        votingCouncel235.setName("ЧЕСМА 4");
        votingCouncel235.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Петра Великог 32, уч. 2");
        votingCouncel235.setNumberOfMembers(4);
        votingCouncel235.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel235.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel235);

        VotingCouncelEntity votingCouncel236 = new VotingCouncelEntity();
        votingCouncel236.setCode("034Б236");
        votingCouncel236.setName("ЧОКОРСКА ПОЉА");
        votingCouncel236.setLocation("Коњички клуб, објекат \"Салаш\", сала 1");
        votingCouncel236.setNumberOfMembers(4);
        votingCouncel236.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel236.setMentor(graba);
        votingCouncelRepository.save(votingCouncel236);

        VotingCouncelEntity votingCouncel237 = new VotingCouncelEntity();
        votingCouncel237.setCode("034Б237");
        votingCouncel237.setName("ШАРГОВАЦ 1");
        votingCouncel237.setLocation("ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 1");
        votingCouncel237.setNumberOfMembers(4);
        votingCouncel237.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel237.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel237);

        VotingCouncelEntity votingCouncel238 = new VotingCouncelEntity();
        votingCouncel238.setCode("034Б238");
        votingCouncel238.setName("ШАРГОВАЦ 2");
        votingCouncel238.setLocation("ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 2");
        votingCouncel238.setNumberOfMembers(4);
        votingCouncel238.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel238.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel238);

        VotingCouncelEntity votingCouncel239 = new VotingCouncelEntity();
        votingCouncel239.setCode("034Б239");
        votingCouncel239.setName("ШАРГОВАЦ 3");
        votingCouncel239.setLocation("ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 3");
        votingCouncel239.setNumberOfMembers(4);
        votingCouncel239.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel239.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel239);

        VotingCouncelEntity votingCouncel240 = new VotingCouncelEntity();
        votingCouncel240.setCode("034Б240");
        votingCouncel240.setName("ШАРГОВАЦ 4");
        votingCouncel240.setLocation("ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 4");
        votingCouncel240.setNumberOfMembers(4);
        votingCouncel240.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel240.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel240);

        VotingCouncelEntity votingCouncel241 = new VotingCouncelEntity();
        votingCouncel241.setCode("034Б241");
        votingCouncel241.setName("ШИМИЋИ");
        votingCouncel241.setLocation("МЗ Шимићи, сала 1");
        votingCouncel241.setNumberOfMembers(2);
        votingCouncel241.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel241.setMentor(igor);
        votingCouncelRepository.save(votingCouncel241);

        VotingCouncelEntity votingCouncel242 = new VotingCouncelEntity();
        votingCouncel242.setCode("034Б242");
        votingCouncel242.setName("АДА-7");
        votingCouncel242.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч 7");
        votingCouncel242.setNumberOfMembers(4);
        votingCouncel242.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel242.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel242);

        VotingCouncelEntity votingCouncel243 = new VotingCouncelEntity();
        votingCouncel243.setCode("034Б243");
        votingCouncel243.setName("АДА -8");
        votingCouncel243.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч 8");
        votingCouncel243.setNumberOfMembers(4);
        votingCouncel243.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel243.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel243);

        VotingCouncelEntity votingCouncel244 = new VotingCouncelEntity();
        votingCouncel244.setCode("034Б244");
        votingCouncel244.setName("КУЉАНИ - 2");
        votingCouncel244.setLocation("ПШ \"ЈОВАН ДУЧИЋ\" (нови објекат), Куљани, уч. 2");
        votingCouncel244.setNumberOfMembers(4);
        votingCouncel244.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel244.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel244);

        VotingCouncelEntity votingCouncel245 = new VotingCouncelEntity();
        votingCouncel245.setCode("034Б245");
        votingCouncel245.setName("КУЉАНИ - 3");
        votingCouncel245.setLocation("ПШ \"ЈОВАН ДУЧИЋ\" (нови објекат), Куљани, уч. 3");
        votingCouncel245.setNumberOfMembers(4);
        votingCouncel245.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel245.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel245);

        VotingCouncelEntity votingCouncel246 = new VotingCouncelEntity();
        votingCouncel246.setCode("034Б246");
        votingCouncel246.setName("КУЉАНИ - 4");
        votingCouncel246.setLocation("ПШ \"ЈОВАН ДУЧИЋ\" (нови објекат), Куљани, уч. 4");
        votingCouncel246.setNumberOfMembers(4);
        votingCouncel246.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel246.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel246);

        VotingCouncelEntity votingCouncel247 = new VotingCouncelEntity();
        votingCouncel247.setCode("034Б247");
        votingCouncel247.setName("КУЉАНИ - 5");
        votingCouncel247.setLocation("ПШ \"ЈОВАН ДУЧИЋ\" (НОВИ ОБЈЕКАТ), Куљани, уч. 5");
        votingCouncel247.setNumberOfMembers(4);
        votingCouncel247.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel247.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel247);

        VotingCouncelEntity votingCouncel248 = new VotingCouncelEntity();
        votingCouncel248.setCode("034Б248");
        votingCouncel248.setName("ПРИЈЕЧАНИ 2");
        votingCouncel248.setLocation("ПШ \"ЈОВАН ДУЧИЋ\", Пријечани, уч. 2");
        votingCouncel248.setNumberOfMembers(4);
        votingCouncel248.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel248.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel248);

        VotingCouncelEntity votingCouncel249 = new VotingCouncelEntity();
        votingCouncel249.setCode("034Б249");
        votingCouncel249.setName("ПРИЈЕЧАНИ - 3");
        votingCouncel249.setLocation("ПШ \"ЈОВАН ДУЧИЋ\", Пријечани, уч. 3");
        votingCouncel249.setNumberOfMembers(4);
        votingCouncel249.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel249.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel249);

        VotingCouncelEntity votingCouncel250 = new VotingCouncelEntity();
        votingCouncel250.setCode("034Б250");
        votingCouncel250.setName("КОЛА - 2");
        votingCouncel250.setLocation("ОШ \"ПЕТАР КОЧИЋ\", Kола, уч. 2");
        votingCouncel250.setNumberOfMembers(4);
        votingCouncel250.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel250.setMentor(dujko);
        votingCouncelRepository.save(votingCouncel250);

        VotingCouncelEntity votingCouncel251 = new VotingCouncelEntity();
        votingCouncel251.setCode("034Б251");
        votingCouncel251.setName("ШАРГОВАЦ 5");
        votingCouncel251.setLocation("ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 5");
        votingCouncel251.setNumberOfMembers(4);
        votingCouncel251.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel251.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel251);

        VotingCouncelEntity votingCouncel252 = new VotingCouncelEntity();
        votingCouncel252.setCode("034Б252");
        votingCouncel252.setName("МИШИН ХАН - 2");
        votingCouncel252.setLocation("ПШ \"МИЛУТИН БОЈИЋ\", Мишин Хан, уч. 2");
        votingCouncel252.setNumberOfMembers(4);
        votingCouncel252.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel252.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel252);

        VotingCouncelEntity votingCouncel253 = new VotingCouncelEntity();
        votingCouncel253.setCode("034Б253");
        votingCouncel253.setName("ДЕБЕЉАЦИ - 3");
        votingCouncel253.setLocation("ПШ \"СТАНКО РАКИТА\", Тешана Подруговића бб, уч. 3");
        votingCouncel253.setNumberOfMembers(4);
        votingCouncel253.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel253.setMentor(leona);
        votingCouncelRepository.save(votingCouncel253);

        VotingCouncelEntity votingCouncel254 = new VotingCouncelEntity();
        votingCouncel254.setCode("034Б254");
        votingCouncel254.setName("КАРАНОВАЦ - 3");
        votingCouncel254.setLocation("ОШ \"МИЛАН РАКИЋ\", Карановац, уч. 3");
        votingCouncel254.setNumberOfMembers(4);
        votingCouncel254.setNumberOfVoters(100 + random.nextInt(901));
        votingCouncel254.setMentor(leona);
        votingCouncelRepository.save(votingCouncel254);

        VotingCouncelEntity votingCouncel501 = new VotingCouncelEntity();
        votingCouncel501.setCode("034Б501");
        votingCouncel501.setName("ОДСУСТВО");
        votingCouncel501.setLocation("Раднички универзитет, Грчка 4, сала 1");
        votingCouncel501.setNumberOfMembers(2);
        votingCouncel501.setNumberOfVoters(3);
        votingCouncel501.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel501);

        VotingCouncelEntity votingCouncelNNNN = new VotingCouncelEntity();
        votingCouncelNNNN.setCode("034БННН");
        votingCouncelNNNN.setName("НЕПОТВРЂЕНИ");
        votingCouncelNNNN.setLocation("Раднички универзитет, Грчка 4, сала 2");
        votingCouncelNNNN.setNumberOfMembers(2);
        votingCouncelNNNN.setNumberOfVoters(3);
        votingCouncelNNNN.setMentor(bojana);
        votingCouncelRepository.save(votingCouncelNNNN);
    }
}
