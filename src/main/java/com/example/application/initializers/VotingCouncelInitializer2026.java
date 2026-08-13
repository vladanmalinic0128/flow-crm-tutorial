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

@Component
@RequiredArgsConstructor
public class VotingCouncelInitializer2026 /*implements ApplicationRunner*/ {
    private final MentorRepository mentorRepository;
    private final VotingCouncelRepository votingCouncelRepository;
    private final TitleRepository titleRepository;

//    @Override
    public void run(ApplicationArguments args) throws Exception {

        //Додавање ментора
        MentorEntity dubravko = new MentorEntity();
        dubravko.setFirstname("Дубравко");
        dubravko.setLastname("Малинић");
        dubravko.setEmail("john.doe@example.com");
        dubravko.setVotingCouncels(new ArrayList<>());
        mentorRepository.save(dubravko);

        MentorEntity bojana = new MentorEntity();
        bojana.setFirstname("Бојана");
        bojana.setLastname("Ајдер");
        bojana.setEmail("john.doe@example.com");
        bojana.setVotingCouncels(new ArrayList<>());
        mentorRepository.save(bojana);

        MentorEntity dusko = new MentorEntity();
        dusko.setFirstname("Душко");
        dusko.setLastname("Радивојевић");
        dusko.setEmail("john.doe@example.com");
        dusko.setVotingCouncels(new ArrayList<>());
        mentorRepository.save(dusko);

        MentorEntity graba = new MentorEntity();
        graba.setFirstname("Драган");
        graba.setLastname("Грабовица");
        graba.setEmail("john.doe@example.com");
        graba.setVotingCouncels(new ArrayList<>());
        mentorRepository.save(graba);

        MentorEntity dino = new MentorEntity();
        dino.setFirstname("Дино");
        dino.setLastname("Вујичић");
        dino.setEmail("john.doe@example.com");
        dino.setVotingCouncels(new ArrayList<>());
        mentorRepository.save(dino);

        MentorEntity nada = new MentorEntity();
        nada.setFirstname("Нада");
        nada.setLastname("Батинар");
        nada.setEmail("john.doe@example.com");
        nada.setVotingCouncels(new ArrayList<>());
        mentorRepository.save(nada);

        MentorEntity igor = new MentorEntity();
        igor.setFirstname("Игор");
        igor.setLastname("Видовић");
        igor.setEmail("john.doe@example.com");
        igor.setVotingCouncels(new ArrayList<>());
        mentorRepository.save(igor);

        //Додавање титула
        TitleEntity clanTitle = new TitleEntity();
        clanTitle.setName("ЧЛАН");
        titleRepository.save(clanTitle);

        TitleEntity zamjenikClanaTitle = new TitleEntity();
        zamjenikClanaTitle.setName("ЗАМЈЕНИК ЧЛАНА");
        titleRepository.save(zamjenikClanaTitle);

        VotingCouncelEntity votingCouncel034B000 = new VotingCouncelEntity();
        votingCouncel034B000.setCode("034Б000");
        votingCouncel034B000.setName("ЛИЧНО");
        votingCouncel034B000.setLocation("ОШ \"ИВО АНДРИЋ\" ФИСКУЛТУРНА САЛА 1");
        votingCouncel034B000.setNumberOfMembers(4);
        votingCouncel034B000.setNumberOfVoters(1634);
        votingCouncel034B000.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B000);

        VotingCouncelEntity votingCouncel034B001 = new VotingCouncelEntity();
        votingCouncel034B001.setCode("034Б001");
        votingCouncel034B001.setName("АГИНО СЕЛО");
        votingCouncel034B001.setLocation("ПШ \"ВОЈИСЛАВ ИЛИЋ\", Агино Село бб, уч. 1");
        votingCouncel034B001.setNumberOfMembers(4);
        votingCouncel034B001.setNumberOfVoters(429);
        votingCouncel034B001.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B001);

        VotingCouncelEntity votingCouncel034B002 = new VotingCouncelEntity();
        votingCouncel034B002.setCode("034Б002");
        votingCouncel034B002.setName("АДА - 1");
        votingCouncel034B002.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч. 1");
        votingCouncel034B002.setNumberOfMembers(4);
        votingCouncel034B002.setNumberOfVoters(901);
        votingCouncel034B002.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B002);

        VotingCouncelEntity votingCouncel034B003 = new VotingCouncelEntity();
        votingCouncel034B003.setCode("034Б003");
        votingCouncel034B003.setName("АДА - 2");
        votingCouncel034B003.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч. 2");
        votingCouncel034B003.setNumberOfMembers(4);
        votingCouncel034B003.setNumberOfVoters(803);
        votingCouncel034B003.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B003);

        VotingCouncelEntity votingCouncel034B004 = new VotingCouncelEntity();
        votingCouncel034B004.setCode("034Б004");
        votingCouncel034B004.setName("АДА - 3");
        votingCouncel034B004.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч. 3");
        votingCouncel034B004.setNumberOfMembers(4);
        votingCouncel034B004.setNumberOfVoters(858);
        votingCouncel034B004.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B004);

        VotingCouncelEntity votingCouncel034B005 = new VotingCouncelEntity();
        votingCouncel034B005.setCode("034Б005");
        votingCouncel034B005.setName("АДА - 4");
        votingCouncel034B005.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч. 4");
        votingCouncel034B005.setNumberOfMembers(4);
        votingCouncel034B005.setNumberOfVoters(903);
        votingCouncel034B005.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B005);

        VotingCouncelEntity votingCouncel034B006 = new VotingCouncelEntity();
        votingCouncel034B006.setCode("034Б006");
        votingCouncel034B006.setName("АДА - 5");
        votingCouncel034B006.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч. 5");
        votingCouncel034B006.setNumberOfMembers(4);
        votingCouncel034B006.setNumberOfVoters(825);
        votingCouncel034B006.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B006);

        VotingCouncelEntity votingCouncel034B007 = new VotingCouncelEntity();
        votingCouncel034B007.setCode("034Б007");
        votingCouncel034B007.setName("АДА - 6");
        votingCouncel034B007.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч. 6");
        votingCouncel034B007.setNumberOfMembers(4);
        votingCouncel034B007.setNumberOfVoters(946);
        votingCouncel034B007.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B007);

        VotingCouncelEntity votingCouncel034B008 = new VotingCouncelEntity();
        votingCouncel034B008.setCode("034Б008");
        votingCouncel034B008.setName("БИСТРИЦА");
        votingCouncel034B008.setLocation("ОШ \"МИРОСЛАВ АНТИЋ\", Бистрица, уч. 1");
        votingCouncel034B008.setNumberOfMembers(4);
        votingCouncel034B008.setNumberOfVoters(984);
        votingCouncel034B008.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B008);

        VotingCouncelEntity votingCouncel034B009 = new VotingCouncelEntity();
        votingCouncel034B009.setCode("034Б009");
        votingCouncel034B009.setName("БОРИК I /1");
        votingCouncel034B009.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 1");
        votingCouncel034B009.setNumberOfMembers(4);
        votingCouncel034B009.setNumberOfVoters(769);
        votingCouncel034B009.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B009);

        VotingCouncelEntity votingCouncel034B010 = new VotingCouncelEntity();
        votingCouncel034B010.setCode("034Б010");
        votingCouncel034B010.setName("БОРИК I /2");
        votingCouncel034B010.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 2");
        votingCouncel034B010.setNumberOfMembers(4);
        votingCouncel034B010.setNumberOfVoters(691);
        votingCouncel034B010.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B010);

        VotingCouncelEntity votingCouncel034B011 = new VotingCouncelEntity();
        votingCouncel034B011.setCode("034Б011");
        votingCouncel034B011.setName("БОРИК I /3");
        votingCouncel034B011.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 3");
        votingCouncel034B011.setNumberOfMembers(4);
        votingCouncel034B011.setNumberOfVoters(811);
        votingCouncel034B011.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B011);

        VotingCouncelEntity votingCouncel034B012 = new VotingCouncelEntity();
        votingCouncel034B012.setCode("034Б012");
        votingCouncel034B012.setName("БОРИК I /4");
        votingCouncel034B012.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 4");
        votingCouncel034B012.setNumberOfMembers(4);
        votingCouncel034B012.setNumberOfVoters(667);
        votingCouncel034B012.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B012);

        VotingCouncelEntity votingCouncel034B013 = new VotingCouncelEntity();
        votingCouncel034B013.setCode("034Б013");
        votingCouncel034B013.setName("БОРИК I /5");
        votingCouncel034B013.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 5");
        votingCouncel034B013.setNumberOfMembers(4);
        votingCouncel034B013.setNumberOfVoters(752);
        votingCouncel034B013.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B013);

        VotingCouncelEntity votingCouncel034B014 = new VotingCouncelEntity();
        votingCouncel034B014.setCode("034Б014");
        votingCouncel034B014.setName("БОРИК I /6");
        votingCouncel034B014.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 6");
        votingCouncel034B014.setNumberOfMembers(4);
        votingCouncel034B014.setNumberOfVoters(711);
        votingCouncel034B014.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B014);

        VotingCouncelEntity votingCouncel034B015 = new VotingCouncelEntity();
        votingCouncel034B015.setCode("034Б015");
        votingCouncel034B015.setName("БОРИК I /7");
        votingCouncel034B015.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 7");
        votingCouncel034B015.setNumberOfMembers(4);
        votingCouncel034B015.setNumberOfVoters(612);
        votingCouncel034B015.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B015);

        VotingCouncelEntity votingCouncel034B016 = new VotingCouncelEntity();
        votingCouncel034B016.setCode("034Б016");
        votingCouncel034B016.setName("БОРИК I /8");
        votingCouncel034B016.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 8");
        votingCouncel034B016.setNumberOfMembers(4);
        votingCouncel034B016.setNumberOfVoters(987);
        votingCouncel034B016.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B016);

        VotingCouncelEntity votingCouncel034B017 = new VotingCouncelEntity();
        votingCouncel034B017.setCode("034Б017");
        votingCouncel034B017.setName("БОРИК I /9");
        votingCouncel034B017.setLocation("ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 9");
        votingCouncel034B017.setNumberOfMembers(4);
        votingCouncel034B017.setNumberOfVoters(685);
        votingCouncel034B017.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B017);

        VotingCouncelEntity votingCouncel034B018 = new VotingCouncelEntity();
        votingCouncel034B018.setCode("034Б018");
        votingCouncel034B018.setName("БОРИК II/1");
        votingCouncel034B018.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 1");
        votingCouncel034B018.setNumberOfMembers(4);
        votingCouncel034B018.setNumberOfVoters(621);
        votingCouncel034B018.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B018);

        VotingCouncelEntity votingCouncel034B019 = new VotingCouncelEntity();
        votingCouncel034B019.setCode("034Б019");
        votingCouncel034B019.setName("БОРИК II/2");
        votingCouncel034B019.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 2");
        votingCouncel034B019.setNumberOfMembers(4);
        votingCouncel034B019.setNumberOfVoters(763);
        votingCouncel034B019.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B019);

        VotingCouncelEntity votingCouncel034B020 = new VotingCouncelEntity();
        votingCouncel034B020.setCode("034Б020");
        votingCouncel034B020.setName("БОРИК II/3");
        votingCouncel034B020.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 3");
        votingCouncel034B020.setNumberOfMembers(4);
        votingCouncel034B020.setNumberOfVoters(750);
        votingCouncel034B020.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B020);

        VotingCouncelEntity votingCouncel034B021 = new VotingCouncelEntity();
        votingCouncel034B021.setCode("034Б021");
        votingCouncel034B021.setName("БОРИК II/4");
        votingCouncel034B021.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 4");
        votingCouncel034B021.setNumberOfMembers(4);
        votingCouncel034B021.setNumberOfVoters(877);
        votingCouncel034B021.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B021);

        VotingCouncelEntity votingCouncel034B022 = new VotingCouncelEntity();
        votingCouncel034B022.setCode("034Б022");
        votingCouncel034B022.setName("БОРИК II/5");
        votingCouncel034B022.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 5");
        votingCouncel034B022.setNumberOfMembers(4);
        votingCouncel034B022.setNumberOfVoters(818);
        votingCouncel034B022.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B022);

        VotingCouncelEntity votingCouncel034B023 = new VotingCouncelEntity();
        votingCouncel034B023.setCode("034Б023");
        votingCouncel034B023.setName("БОРИК II/6");
        votingCouncel034B023.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 6");
        votingCouncel034B023.setNumberOfMembers(4);
        votingCouncel034B023.setNumberOfVoters(811);
        votingCouncel034B023.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B023);

        VotingCouncelEntity votingCouncel034B024 = new VotingCouncelEntity();
        votingCouncel034B024.setCode("034Б024");
        votingCouncel034B024.setName("БОРИК II/7");
        votingCouncel034B024.setLocation("ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 7");
        votingCouncel034B024.setNumberOfMembers(4);
        votingCouncel034B024.setNumberOfVoters(889);
        votingCouncel034B024.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B024);

        VotingCouncelEntity votingCouncel034B025 = new VotingCouncelEntity();
        votingCouncel034B025.setCode("034Б025");
        votingCouncel034B025.setName("БОРКОВИЋИ - 1");
        votingCouncel034B025.setLocation("МЗ БОРКОВИЋИ, сала 1");
        votingCouncel034B025.setNumberOfMembers(4);
        votingCouncel034B025.setNumberOfVoters(867);
        votingCouncel034B025.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B025);

        VotingCouncelEntity votingCouncel034B026 = new VotingCouncelEntity();
        votingCouncel034B026.setCode("034Б026");
        votingCouncel034B026.setName("БОРКОВИЋИ - 2 /СЛАВИЋКА");
        votingCouncel034B026.setLocation("Продавница \"ПЛАЗМА\" Славићка");
        votingCouncel034B026.setNumberOfMembers(4);
        votingCouncel034B026.setNumberOfVoters(778);
        votingCouncel034B026.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B026);

        VotingCouncelEntity votingCouncel034B027A = new VotingCouncelEntity();
        votingCouncel034B027A.setCode("034Б027А");
        votingCouncel034B027A.setName("БОЧАЦ");
        votingCouncel034B027A.setLocation("ПШ \"ВОЈИСЛАВ ИЛИЋ\", Бочац, уч. 1");
        votingCouncel034B027A.setNumberOfMembers(4);
        votingCouncel034B027A.setNumberOfVoters(511);
        votingCouncel034B027A.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B027A);

        VotingCouncelEntity votingCouncel034B027B = new VotingCouncelEntity();
        votingCouncel034B027B.setCode("034Б027Б");
        votingCouncel034B027B.setName("БОЧАЦ");
        votingCouncel034B027B.setLocation("ПШ \"ВОЈИСЛАВ ИЛИЋ\", Бочац, уч. 2");
        votingCouncel034B027B.setNumberOfMembers(4);
        votingCouncel034B027B.setNumberOfVoters(504);
        votingCouncel034B027B.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B027B);

        VotingCouncelEntity votingCouncel034B028 = new VotingCouncelEntity();
        votingCouncel034B028.setCode("034Б028");
        votingCouncel034B028.setName("БРОНЗАНИ МАЈДАН - 1");
        votingCouncel034B028.setLocation("МЗ БРОНЗАНИ МАЈДАН, сала 1");
        votingCouncel034B028.setNumberOfMembers(4);
        votingCouncel034B028.setNumberOfVoters(727);
        votingCouncel034B028.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B028);

        VotingCouncelEntity votingCouncel034B029 = new VotingCouncelEntity();
        votingCouncel034B029.setCode("034Б029");
        votingCouncel034B029.setName("БРОНЗАНИ МАЈДАН - 2 /МЕЛИНА");
        votingCouncel034B029.setLocation("ПШ \"МЛАДЕН СТОЈАНОВИЋ\" Мелина, уч. 1");
        votingCouncel034B029.setNumberOfMembers(4);
        votingCouncel034B029.setNumberOfVoters(652);
        votingCouncel034B029.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B029);

        VotingCouncelEntity votingCouncel034B030 = new VotingCouncelEntity();
        votingCouncel034B030.setCode("034Б030");
        votingCouncel034B030.setName("БРОНЗАНИ МАЈДАН - 3/ОБРОВАЦ");
        votingCouncel034B030.setLocation("ПШ \"МЛАДЕН СТОЈАНОВИЋ\", Обровац, уч. 1");
        votingCouncel034B030.setNumberOfMembers(4);
        votingCouncel034B030.setNumberOfVoters(448);
        votingCouncel034B030.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B030);

        VotingCouncelEntity votingCouncel034B031 = new VotingCouncelEntity();
        votingCouncel034B031.setCode("034Б031");
        votingCouncel034B031.setName("БУЛЕВАР - 1");
        votingCouncel034B031.setLocation("ГИМНАЗИЈА, Змај Јовина 13, уч. 1");
        votingCouncel034B031.setNumberOfMembers(4);
        votingCouncel034B031.setNumberOfVoters(607);
        votingCouncel034B031.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B031);

        VotingCouncelEntity votingCouncel034B032 = new VotingCouncelEntity();
        votingCouncel034B032.setCode("034Б032");
        votingCouncel034B032.setName("БУЛЕВАР - 2");
        votingCouncel034B032.setLocation("ГИМНАЗИЈА, Змај Јовина 13, уч. 2");
        votingCouncel034B032.setNumberOfMembers(4);
        votingCouncel034B032.setNumberOfVoters(629);
        votingCouncel034B032.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B032);

        VotingCouncelEntity votingCouncel034B033 = new VotingCouncelEntity();
        votingCouncel034B033.setCode("034Б033");
        votingCouncel034B033.setName("БУЛЕВАР - 3");
        votingCouncel034B033.setLocation("МЗ БУЛЕВАР, Симеуна Ђака 15, сала 1");
        votingCouncel034B033.setNumberOfMembers(4);
        votingCouncel034B033.setNumberOfVoters(732);
        votingCouncel034B033.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B033);

        VotingCouncelEntity votingCouncel034B034 = new VotingCouncelEntity();
        votingCouncel034B034.setCode("034Б034");
        votingCouncel034B034.setName("БУЛЕВАР - 4");
        votingCouncel034B034.setLocation("ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 1");
        votingCouncel034B034.setNumberOfMembers(4);
        votingCouncel034B034.setNumberOfVoters(728);
        votingCouncel034B034.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B034);

        VotingCouncelEntity votingCouncel034B035 = new VotingCouncelEntity();
        votingCouncel034B035.setCode("034Б035");
        votingCouncel034B035.setName("БУЛЕВАР - 5");
        votingCouncel034B035.setLocation("ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 2");
        votingCouncel034B035.setNumberOfMembers(4);
        votingCouncel034B035.setNumberOfVoters(768);
        votingCouncel034B035.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B035);

        VotingCouncelEntity votingCouncel034B036 = new VotingCouncelEntity();
        votingCouncel034B036.setCode("034Б036");
        votingCouncel034B036.setName("БУЛЕВАР - 6");
        votingCouncel034B036.setLocation("ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 3");
        votingCouncel034B036.setNumberOfMembers(4);
        votingCouncel034B036.setNumberOfVoters(766);
        votingCouncel034B036.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B036);

        VotingCouncelEntity votingCouncel034B037 = new VotingCouncelEntity();
        votingCouncel034B037.setCode("034Б037");
        votingCouncel034B037.setName("БУЛЕВАР - 7");
        votingCouncel034B037.setLocation("ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 4");
        votingCouncel034B037.setNumberOfMembers(4);
        votingCouncel034B037.setNumberOfVoters(777);
        votingCouncel034B037.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B037);

        VotingCouncelEntity votingCouncel034B038 = new VotingCouncelEntity();
        votingCouncel034B038.setCode("034Б038");
        votingCouncel034B038.setName("ВЕРИЋИ - 1");
        votingCouncel034B038.setLocation("ПШ \"МИЛУТИН БОЈИЋ\", Верићи, уч. 1");
        votingCouncel034B038.setNumberOfMembers(4);
        votingCouncel034B038.setNumberOfVoters(527);
        votingCouncel034B038.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B038);

        VotingCouncelEntity votingCouncel034B039 = new VotingCouncelEntity();
        votingCouncel034B039.setCode("034Б039");
        votingCouncel034B039.setName("ВЕРИЋИ - 2");
        votingCouncel034B039.setLocation("ПШ \"МИЛУТИН БОЈИЋ\", Верићи, уч. 2");
        votingCouncel034B039.setNumberOfMembers(4);
        votingCouncel034B039.setNumberOfVoters(502);
        votingCouncel034B039.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B039);

        VotingCouncelEntity votingCouncel034B040 = new VotingCouncelEntity();
        votingCouncel034B040.setCode("034Б040");
        votingCouncel034B040.setName("ВРБАЊА - 1");
        votingCouncel034B040.setLocation("МЗ ВРБАЊА, Станка Божића Кобре бб, сала 1");
        votingCouncel034B040.setNumberOfMembers(4);
        votingCouncel034B040.setNumberOfVoters(704);
        votingCouncel034B040.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B040);

        VotingCouncelEntity votingCouncel034B041 = new VotingCouncelEntity();
        votingCouncel034B041.setCode("034Б041");
        votingCouncel034B041.setName("ВРБАЊА - 2");
        votingCouncel034B041.setLocation("ОШ \"СТАНКО РАКИТА\", Јове Г. Поповића 9, уч. 1");
        votingCouncel034B041.setNumberOfMembers(4);
        votingCouncel034B041.setNumberOfVoters(680);
        votingCouncel034B041.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B041);

        VotingCouncelEntity votingCouncel034B042 = new VotingCouncelEntity();
        votingCouncel034B042.setCode("034Б042");
        votingCouncel034B042.setName("ВРБАЊА - 3");
        votingCouncel034B042.setLocation("ОШ \"СТАНКО РАКИТА\", Јове Г. Поповића 9, уч. 2");
        votingCouncel034B042.setNumberOfMembers(4);
        votingCouncel034B042.setNumberOfVoters(720);
        votingCouncel034B042.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B042);

        VotingCouncelEntity votingCouncel034B043 = new VotingCouncelEntity();
        votingCouncel034B043.setCode("034Б043");
        votingCouncel034B043.setName("ВРБАЊА - 4");
        votingCouncel034B043.setLocation("ОШ \"СТАНКО РАКИТА\", Јове Г. Поповића 9, уч. 3");
        votingCouncel034B043.setNumberOfMembers(4);
        votingCouncel034B043.setNumberOfVoters(701);
        votingCouncel034B043.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B043);

        VotingCouncelEntity votingCouncel034B044 = new VotingCouncelEntity();
        votingCouncel034B044.setCode("034Б044");
        votingCouncel034B044.setName("ГОЛЕШИ - 1 /ДОЊИ ПЕРВАН");
        votingCouncel034B044.setLocation("ПШ \"МИРОСЛАВ АНТИЋ\", Доњи Перван, уч. 1");
        votingCouncel034B044.setNumberOfMembers(4);
        votingCouncel034B044.setNumberOfVoters(899);
        votingCouncel034B044.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B044);

        VotingCouncelEntity votingCouncel034B045 = new VotingCouncelEntity();
        votingCouncel034B045.setCode("034Б045");
        votingCouncel034B045.setName("ВРБАЊА - 5");
        votingCouncel034B045.setLocation("ОШ \"СТАНКО РАКИТА\", Јове Г. Поповића 9, уч. 4");
        votingCouncel034B045.setNumberOfMembers(4);
        votingCouncel034B045.setNumberOfVoters(778);
        votingCouncel034B045.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B045);

        VotingCouncelEntity votingCouncel034B046 = new VotingCouncelEntity();
        votingCouncel034B046.setCode("034Б046");
        votingCouncel034B046.setName("ГОРЊА ПИСКАВИЦА");
        votingCouncel034B046.setLocation("ПШ \"ЋИРИЛО И МЕТОДИЈЕ\", Горња Пискавица, уч. 1");
        votingCouncel034B046.setNumberOfMembers(4);
        votingCouncel034B046.setNumberOfVoters(798);
        votingCouncel034B046.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B046);

        VotingCouncelEntity votingCouncel034B047 = new VotingCouncelEntity();
        votingCouncel034B047.setCode("034Б047");
        votingCouncel034B047.setName("ДЕБЕЉАЦИ - 1");
        votingCouncel034B047.setLocation("ПШ \"СТАНКО РАКИТА\", Тешана Подруговића бб, уч. 1");
        votingCouncel034B047.setNumberOfMembers(4);
        votingCouncel034B047.setNumberOfVoters(655);
        votingCouncel034B047.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B047);

        VotingCouncelEntity votingCouncel034B048 = new VotingCouncelEntity();
        votingCouncel034B048.setCode("034Б048");
        votingCouncel034B048.setName("ДЕБЕЉАЦИ - 2");
        votingCouncel034B048.setLocation("ПШ \"СТАНКО РАКИТА\", Тешана Подруговића бб, уч. 2");
        votingCouncel034B048.setNumberOfMembers(4);
        votingCouncel034B048.setNumberOfVoters(755);
        votingCouncel034B048.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B048);

        VotingCouncelEntity votingCouncel034B049 = new VotingCouncelEntity();
        votingCouncel034B049.setCode("034Б049");
        votingCouncel034B049.setName("ДОЊА КОЛА - 1");
        votingCouncel034B049.setLocation("МЗ Доња Кола, сала 1");
        votingCouncel034B049.setNumberOfMembers(4);
        votingCouncel034B049.setNumberOfVoters(823);
        votingCouncel034B049.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B049);

        VotingCouncelEntity votingCouncel034B050 = new VotingCouncelEntity();
        votingCouncel034B050.setCode("034Б050");
        votingCouncel034B050.setName("ДОЊА КОЛА - 2");
        votingCouncel034B050.setLocation("МЗ Доња Кола, сала 2");
        votingCouncel034B050.setNumberOfMembers(4);
        votingCouncel034B050.setNumberOfVoters(689);
        votingCouncel034B050.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B050);

        VotingCouncelEntity votingCouncel034B051A = new VotingCouncelEntity();
        votingCouncel034B051A.setCode("034Б051А");
        votingCouncel034B051A.setName("ДРАГОЧАЈ – 1");
        votingCouncel034B051A.setLocation("ОШ \"ДЕСАНКА МАКСИМОВИЋ\", Драгочај, уч. 1");
        votingCouncel034B051A.setNumberOfMembers(4);
        votingCouncel034B051A.setNumberOfVoters(825);
        votingCouncel034B051A.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B051A);

        VotingCouncelEntity votingCouncel034B051B = new VotingCouncelEntity();
        votingCouncel034B051B.setCode("034Б051Б");
        votingCouncel034B051B.setName("ДРАГОЧАЈ – 1");
        votingCouncel034B051B.setLocation("ОШ \"ДЕСАНКА МАКСИМОВИЋ\", Драгочај, уч. 2");
        votingCouncel034B051B.setNumberOfMembers(4);
        votingCouncel034B051B.setNumberOfVoters(846);
        votingCouncel034B051B.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B051B);

        VotingCouncelEntity votingCouncel034B051C = new VotingCouncelEntity();
        votingCouncel034B051C.setCode("034Б051Ц");
        votingCouncel034B051C.setName("ДРАГОЧАЈ – 1");
        votingCouncel034B051C.setLocation("ОШ \"ДЕСАНКА МАКСИМОВИЋ\", Драгочај, уч. 3");
        votingCouncel034B051C.setNumberOfMembers(4);
        votingCouncel034B051C.setNumberOfVoters(835);
        votingCouncel034B051C.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B051C);

        VotingCouncelEntity votingCouncel034B052A = new VotingCouncelEntity();
        votingCouncel034B052A.setCode("034Б052А");
        votingCouncel034B052A.setName("ДРАГОЧАЈ - 2 / РАМИЋИ");
        votingCouncel034B052A.setLocation("ОШ \"ДЕСАНКА МАКСИМОВИЋ\", Драгочај, уч. 4");
        votingCouncel034B052A.setNumberOfMembers(4);
        votingCouncel034B052A.setNumberOfVoters(725);
        votingCouncel034B052A.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B052A);

        VotingCouncelEntity votingCouncel034B052B = new VotingCouncelEntity();
        votingCouncel034B052B.setCode("034Б052Б");
        votingCouncel034B052B.setName("ДРАГОЧАЈ - 2 / РАМИЋИ");
        votingCouncel034B052B.setLocation("ОШ \"ДЕСАНКА МАКСИМОВИЋ\", Драгочај, уч. 5");
        votingCouncel034B052B.setNumberOfMembers(4);
        votingCouncel034B052B.setNumberOfVoters(820);
        votingCouncel034B052B.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B052B);

        VotingCouncelEntity votingCouncel034B053 = new VotingCouncelEntity();
        votingCouncel034B053.setCode("034Б053");
        votingCouncel034B053.setName("ДРАГОЧАЈ - 3 / БАРЛОВЦИ");
        votingCouncel034B053.setLocation("ПШ \"ЈОВАН ДУЧИЋ\", Барловци, уч. 1");
        votingCouncel034B053.setNumberOfMembers(4);
        votingCouncel034B053.setNumberOfVoters(718);
        votingCouncel034B053.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B053);

        VotingCouncelEntity votingCouncel034B054 = new VotingCouncelEntity();
        votingCouncel034B054.setCode("034Б054");
        votingCouncel034B054.setName("ДРАКУЛИЋ - 1");
        votingCouncel034B054.setLocation("МЗ ДРАКУЛИЋ, Ђурђа Гламочанина 1, сала 1");
        votingCouncel034B054.setNumberOfMembers(4);
        votingCouncel034B054.setNumberOfVoters(709);
        votingCouncel034B054.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B054);

        VotingCouncelEntity votingCouncel034B055 = new VotingCouncelEntity();
        votingCouncel034B055.setCode("034Б055");
        votingCouncel034B055.setName("ДРАКУЛИЋ - 2");
        votingCouncel034B055.setLocation("ПШ \"АЛЕКСА ШАНТИЋ\", Дракулић, 7. фебруара бб, уч. 1");
        votingCouncel034B055.setNumberOfMembers(4);
        votingCouncel034B055.setNumberOfVoters(949);
        votingCouncel034B055.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B055);

        VotingCouncelEntity votingCouncel034B056 = new VotingCouncelEntity();
        votingCouncel034B056.setCode("034Б056");
        votingCouncel034B056.setName("ДРАКУЛИЋ - 3");
        votingCouncel034B056.setLocation("КИНОЛОШКИ САВЕЗ БАЊА ЛУКА, Битољска бб, сала 1");
        votingCouncel034B056.setNumberOfMembers(4);
        votingCouncel034B056.setNumberOfVoters(638);
        votingCouncel034B056.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B056);

        VotingCouncelEntity votingCouncel034B057 = new VotingCouncelEntity();
        votingCouncel034B057.setCode("034Б057");
        votingCouncel034B057.setName("ДРАКУЛИЋ - 4");
        votingCouncel034B057.setLocation("ПШ \"АЛЕКСА ШАНТИЋ\", Дракулић, 7. фебруара бб, уч. 2");
        votingCouncel034B057.setNumberOfMembers(4);
        votingCouncel034B057.setNumberOfVoters(906);
        votingCouncel034B057.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B057);

        VotingCouncelEntity votingCouncel034B058 = new VotingCouncelEntity();
        votingCouncel034B058.setCode("034Б058");
        votingCouncel034B058.setName("ДРАКУЛИЋ - 5");
        votingCouncel034B058.setLocation("КИНОЛОШКИ САВЕЗ БАЊА ЛУКА, Битољска бб, сала 2");
        votingCouncel034B058.setNumberOfMembers(4);
        votingCouncel034B058.setNumberOfVoters(650);
        votingCouncel034B058.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B058);

        VotingCouncelEntity votingCouncel034B059 = new VotingCouncelEntity();
        votingCouncel034B059.setCode("034Б059");
        votingCouncel034B059.setName("ДРАКУЛИЋ - 6");
        votingCouncel034B059.setLocation("КИНОЛОШКИ САВЕЗ БАЊА ЛУКА, Битољска бб, сала 3");
        votingCouncel034B059.setNumberOfMembers(4);
        votingCouncel034B059.setNumberOfVoters(597);
        votingCouncel034B059.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B059);

        VotingCouncelEntity votingCouncel034B060 = new VotingCouncelEntity();
        votingCouncel034B060.setCode("034Б060");
        votingCouncel034B060.setName("ЗАЛУЖАНИ - 1");
        votingCouncel034B060.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 1");
        votingCouncel034B060.setNumberOfMembers(4);
        votingCouncel034B060.setNumberOfVoters(738);
        votingCouncel034B060.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B060);

        VotingCouncelEntity votingCouncel034B061 = new VotingCouncelEntity();
        votingCouncel034B061.setCode("034Б061");
        votingCouncel034B061.setName("ЗАЛУЖАНИ - 2");
        votingCouncel034B061.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 2");
        votingCouncel034B061.setNumberOfMembers(4);
        votingCouncel034B061.setNumberOfVoters(715);
        votingCouncel034B061.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B061);

        VotingCouncelEntity votingCouncel034B062 = new VotingCouncelEntity();
        votingCouncel034B062.setCode("034Б062");
        votingCouncel034B062.setName("ЗАЛУЖАНИ - 3");
        votingCouncel034B062.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 3");
        votingCouncel034B062.setNumberOfMembers(4);
        votingCouncel034B062.setNumberOfVoters(662);
        votingCouncel034B062.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B062);

        VotingCouncelEntity votingCouncel034B063 = new VotingCouncelEntity();
        votingCouncel034B063.setCode("034Б063");
        votingCouncel034B063.setName("ЗАЛУЖАНИ - 4");
        votingCouncel034B063.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 4");
        votingCouncel034B063.setNumberOfMembers(4);
        votingCouncel034B063.setNumberOfVoters(857);
        votingCouncel034B063.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B063);

        VotingCouncelEntity votingCouncel034B064 = new VotingCouncelEntity();
        votingCouncel034B064.setCode("034Б064");
        votingCouncel034B064.setName("ЗАЛУЖАНИ - 5");
        votingCouncel034B064.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 5");
        votingCouncel034B064.setNumberOfMembers(4);
        votingCouncel034B064.setNumberOfVoters(748);
        votingCouncel034B064.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B064);

        VotingCouncelEntity votingCouncel034B065 = new VotingCouncelEntity();
        votingCouncel034B065.setCode("034Б065");
        votingCouncel034B065.setName("ЗАЛУЖАНИ - 6");
        votingCouncel034B065.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 6");
        votingCouncel034B065.setNumberOfMembers(4);
        votingCouncel034B065.setNumberOfVoters(826);
        votingCouncel034B065.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B065);

        VotingCouncelEntity votingCouncel034B066 = new VotingCouncelEntity();
        votingCouncel034B066.setCode("034Б066");
        votingCouncel034B066.setName("ЗАЛУЖАНИ - 7");
        votingCouncel034B066.setLocation("ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 7");
        votingCouncel034B066.setNumberOfMembers(4);
        votingCouncel034B066.setNumberOfVoters(816);
        votingCouncel034B066.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B066);

        VotingCouncelEntity votingCouncel034B067 = new VotingCouncelEntity();
        votingCouncel034B067.setCode("034Б067");
        votingCouncel034B067.setName("КАРАНОВАЦ – 1");
        votingCouncel034B067.setLocation("ОШ \"МИЛАН РАКИЋ\", Карановац, уч. 1");
        votingCouncel034B067.setNumberOfMembers(4);
        votingCouncel034B067.setNumberOfVoters(485);
        votingCouncel034B067.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B067);

        VotingCouncelEntity votingCouncel034B068 = new VotingCouncelEntity();
        votingCouncel034B068.setCode("034Б068");
        votingCouncel034B068.setName("КАРАНОВАЦ - 2");
        votingCouncel034B068.setLocation("ОШ \"МИЛАН РАКИЋ\", Карановац, уч. 2");
        votingCouncel034B068.setNumberOfMembers(4);
        votingCouncel034B068.setNumberOfVoters(829);
        votingCouncel034B068.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B068);

        VotingCouncelEntity votingCouncel034B069 = new VotingCouncelEntity();
        votingCouncel034B069.setCode("034Б069");
        votingCouncel034B069.setName("КМЕЋАНИ");
        votingCouncel034B069.setLocation("ПШ \"МЛАДЕН СТОЈАНОВИЋ\", Кмећани, уч. 1");
        votingCouncel034B069.setNumberOfMembers(2);
        votingCouncel034B069.setNumberOfVoters(218);
        votingCouncel034B069.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B069);

        VotingCouncelEntity votingCouncel034B070 = new VotingCouncelEntity();
        votingCouncel034B070.setCode("034Б070");
        votingCouncel034B070.setName("КОЛА - 1");
        votingCouncel034B070.setLocation("ОШ \"ПЕТАР КОЧИЋ\", Кола, уч. 1");
        votingCouncel034B070.setNumberOfMembers(4);
        votingCouncel034B070.setNumberOfVoters(710);
        votingCouncel034B070.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B070);

        VotingCouncelEntity votingCouncel034B071 = new VotingCouncelEntity();
        votingCouncel034B071.setCode("034Б071");
        votingCouncel034B071.setName("КОЧИЋЕВ ВИЈЕНАЦ - 1");
        votingCouncel034B071.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 1");
        votingCouncel034B071.setNumberOfMembers(4);
        votingCouncel034B071.setNumberOfVoters(902);
        votingCouncel034B071.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B071);

        VotingCouncelEntity votingCouncel034B072 = new VotingCouncelEntity();
        votingCouncel034B072.setCode("034Б072");
        votingCouncel034B072.setName("КОЧИЋЕВ ВИЈЕНАЦ - 2");
        votingCouncel034B072.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 2");
        votingCouncel034B072.setNumberOfMembers(4);
        votingCouncel034B072.setNumberOfVoters(855);
        votingCouncel034B072.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B072);

        VotingCouncelEntity votingCouncel034B073 = new VotingCouncelEntity();
        votingCouncel034B073.setCode("034Б073");
        votingCouncel034B073.setName("КОЧИЋЕВ ВИЈЕНАЦ - 3");
        votingCouncel034B073.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 3");
        votingCouncel034B073.setNumberOfMembers(4);
        votingCouncel034B073.setNumberOfVoters(662);
        votingCouncel034B073.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B073);

        VotingCouncelEntity votingCouncel034B074 = new VotingCouncelEntity();
        votingCouncel034B074.setCode("034Б074");
        votingCouncel034B074.setName("КОЧИЋЕВ ВИЈЕНАЦ - 4");
        votingCouncel034B074.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 4");
        votingCouncel034B074.setNumberOfMembers(4);
        votingCouncel034B074.setNumberOfVoters(659);
        votingCouncel034B074.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B074);

        VotingCouncelEntity votingCouncel034B075 = new VotingCouncelEntity();
        votingCouncel034B075.setCode("034Б075");
        votingCouncel034B075.setName("КОЧИЋЕВ ВИЈЕНАЦ - 5");
        votingCouncel034B075.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 5");
        votingCouncel034B075.setNumberOfMembers(4);
        votingCouncel034B075.setNumberOfVoters(881);
        votingCouncel034B075.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B075);

        VotingCouncelEntity votingCouncel034B076 = new VotingCouncelEntity();
        votingCouncel034B076.setCode("034Б076");
        votingCouncel034B076.setName("КОЧИЋЕВ ВИЈЕНАЦ - 6");
        votingCouncel034B076.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 6");
        votingCouncel034B076.setNumberOfMembers(4);
        votingCouncel034B076.setNumberOfVoters(740);
        votingCouncel034B076.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B076);

        VotingCouncelEntity votingCouncel034B077 = new VotingCouncelEntity();
        votingCouncel034B077.setCode("034Б077");
        votingCouncel034B077.setName("КОЧИЋЕВ ВИЈЕНАЦ - 7");
        votingCouncel034B077.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 7");
        votingCouncel034B077.setNumberOfMembers(4);
        votingCouncel034B077.setNumberOfVoters(669);
        votingCouncel034B077.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B077);

        VotingCouncelEntity votingCouncel034B078 = new VotingCouncelEntity();
        votingCouncel034B078.setCode("034Б078");
        votingCouncel034B078.setName("КОЧИЋЕВ ВИЈЕНАЦ - 8");
        votingCouncel034B078.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 8");
        votingCouncel034B078.setNumberOfMembers(4);
        votingCouncel034B078.setNumberOfVoters(647);
        votingCouncel034B078.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B078);

        VotingCouncelEntity votingCouncel034B079 = new VotingCouncelEntity();
        votingCouncel034B079.setCode("034Б079");
        votingCouncel034B079.setName("КОЧИЋЕВ ВИЈЕНАЦ - 9");
        votingCouncel034B079.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 9");
        votingCouncel034B079.setNumberOfMembers(4);
        votingCouncel034B079.setNumberOfVoters(823);
        votingCouncel034B079.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B079);

        VotingCouncelEntity votingCouncel034B080 = new VotingCouncelEntity();
        votingCouncel034B080.setCode("034Б080");
        votingCouncel034B080.setName("КОЧИЋЕВ ВИЈЕНАЦ - 10");
        votingCouncel034B080.setLocation("ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 10");
        votingCouncel034B080.setNumberOfMembers(4);
        votingCouncel034B080.setNumberOfVoters(721);
        votingCouncel034B080.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B080);

        VotingCouncelEntity votingCouncel034B081 = new VotingCouncelEntity();
        votingCouncel034B081.setCode("034Б081");
        votingCouncel034B081.setName("КРМИНЕ");
        votingCouncel034B081.setLocation("ПШ \"ВОЈИСЛАВ ИЛИЋ\", Крмине, уч. 1");
        votingCouncel034B081.setNumberOfMembers(4);
        votingCouncel034B081.setNumberOfVoters(529);
        votingCouncel034B081.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B081);

        VotingCouncelEntity votingCouncel034B082 = new VotingCouncelEntity();
        votingCouncel034B082.setCode("034Б082");
        votingCouncel034B082.setName("КРУПА НА ВРБАСУ – 1");
        votingCouncel034B082.setLocation("ОШ \"ВОЈИСЛАВ ИЛИЋ\", Крупа на Врбасу, уч. 1");
        votingCouncel034B082.setNumberOfMembers(4);
        votingCouncel034B082.setNumberOfVoters(816);
        votingCouncel034B082.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B082);

        VotingCouncelEntity votingCouncel034B083 = new VotingCouncelEntity();
        votingCouncel034B083.setCode("034Б083");
        votingCouncel034B083.setName("КРУПА НА ВРБАСУ - 2 / ЛЕДЕНИЦЕ");
        votingCouncel034B083.setLocation("ПШ \"ВОЈИСЛАВ ИЛИЋ\", Леденице, уч. 1");
        votingCouncel034B083.setNumberOfMembers(4);
        votingCouncel034B083.setNumberOfVoters(418);
        votingCouncel034B083.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B083);

        VotingCouncelEntity votingCouncel034B084 = new VotingCouncelEntity();
        votingCouncel034B084.setCode("034Б084");
        votingCouncel034B084.setName("КУЉАНИ - 1");
        votingCouncel034B084.setLocation("ПШ \"ЈОВАН ДУЧИЋ\" (НОВИ ОБЈЕКАТ), Куљани, уч. 1");
        votingCouncel034B084.setNumberOfMembers(4);
        votingCouncel034B084.setNumberOfVoters(755);
        votingCouncel034B084.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B084);

        VotingCouncelEntity votingCouncel034B085 = new VotingCouncelEntity();
        votingCouncel034B085.setCode("034Б085");
        votingCouncel034B085.setName("ЛАЗАРЕВО I / 1");
        votingCouncel034B085.setLocation("ПОЉОПОРИВРЕДНА ШКОЛА, Књаза Милоша 9, уч. 1");
        votingCouncel034B085.setNumberOfMembers(4);
        votingCouncel034B085.setNumberOfVoters(689);
        votingCouncel034B085.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B085);

        VotingCouncelEntity votingCouncel034B086 = new VotingCouncelEntity();
        votingCouncel034B086.setCode("034Б086");
        votingCouncel034B086.setName("ЛАЗАРЕВО I / 2");
        votingCouncel034B086.setLocation("ПОЉОПОРИВРЕДНА ШКОЛА, Књаза Милоша 9, уч. 2");
        votingCouncel034B086.setNumberOfMembers(4);
        votingCouncel034B086.setNumberOfVoters(910);
        votingCouncel034B086.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B086);

        VotingCouncelEntity votingCouncel034B087 = new VotingCouncelEntity();
        votingCouncel034B087.setCode("034Б087");
        votingCouncel034B087.setName("ЛАЗАРЕВО I / 3");
        votingCouncel034B087.setLocation("ПОЉОПОРИВРЕДНА ШКОЛА, Књаза Милоша 9, уч. 3");
        votingCouncel034B087.setNumberOfMembers(4);
        votingCouncel034B087.setNumberOfVoters(878);
        votingCouncel034B087.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B087);

        VotingCouncelEntity votingCouncel034B088 = new VotingCouncelEntity();
        votingCouncel034B088.setCode("034Б088");
        votingCouncel034B088.setName("ЛАЗАРЕВО I / 4");
        votingCouncel034B088.setLocation("ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 1");
        votingCouncel034B088.setNumberOfMembers(4);
        votingCouncel034B088.setNumberOfVoters(858);
        votingCouncel034B088.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B088);

        VotingCouncelEntity votingCouncel034B089 = new VotingCouncelEntity();
        votingCouncel034B089.setCode("034Б089");
        votingCouncel034B089.setName("ЛАЗАРЕВО I / 5");
        votingCouncel034B089.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч.1");
        votingCouncel034B089.setNumberOfMembers(4);
        votingCouncel034B089.setNumberOfVoters(602);
        votingCouncel034B089.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B089);

        VotingCouncelEntity votingCouncel034B090 = new VotingCouncelEntity();
        votingCouncel034B090.setCode("034Б090");
        votingCouncel034B090.setName("ЛАЗАРЕВО I / 6");
        votingCouncel034B090.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 2");
        votingCouncel034B090.setNumberOfMembers(4);
        votingCouncel034B090.setNumberOfVoters(707);
        votingCouncel034B090.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B090);

        VotingCouncelEntity votingCouncel034B091 = new VotingCouncelEntity();
        votingCouncel034B091.setCode("034Б091");
        votingCouncel034B091.setName("ЛАЗАРЕВО I / 7");
        votingCouncel034B091.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 3");
        votingCouncel034B091.setNumberOfMembers(4);
        votingCouncel034B091.setNumberOfVoters(777);
        votingCouncel034B091.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B091);

        VotingCouncelEntity votingCouncel034B092 = new VotingCouncelEntity();
        votingCouncel034B092.setCode("034Б092");
        votingCouncel034B092.setName("ЛАЗАРЕВО I / 8");
        votingCouncel034B092.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 4");
        votingCouncel034B092.setNumberOfMembers(4);
        votingCouncel034B092.setNumberOfVoters(815);
        votingCouncel034B092.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B092);

        VotingCouncelEntity votingCouncel034B093 = new VotingCouncelEntity();
        votingCouncel034B093.setCode("034Б093");
        votingCouncel034B093.setName("ЛАЗАРЕВО I / 9");
        votingCouncel034B093.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 5");
        votingCouncel034B093.setNumberOfMembers(4);
        votingCouncel034B093.setNumberOfVoters(666);
        votingCouncel034B093.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B093);

        VotingCouncelEntity votingCouncel034B094 = new VotingCouncelEntity();
        votingCouncel034B094.setCode("034Б094");
        votingCouncel034B094.setName("ЛАЗАРЕВО I / 10");
        votingCouncel034B094.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 6");
        votingCouncel034B094.setNumberOfMembers(4);
        votingCouncel034B094.setNumberOfVoters(621);
        votingCouncel034B094.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B094);

        VotingCouncelEntity votingCouncel034B095 = new VotingCouncelEntity();
        votingCouncel034B095.setCode("034Б095");
        votingCouncel034B095.setName("ЛАЗАРЕВО II / 1");
        votingCouncel034B095.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 7");
        votingCouncel034B095.setNumberOfMembers(4);
        votingCouncel034B095.setNumberOfVoters(828);
        votingCouncel034B095.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B095);

        VotingCouncelEntity votingCouncel034B096 = new VotingCouncelEntity();
        votingCouncel034B096.setCode("034Б096");
        votingCouncel034B096.setName("ЛАЗАРЕВО II / 2");
        votingCouncel034B096.setLocation("ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 8");
        votingCouncel034B096.setNumberOfMembers(4);
        votingCouncel034B096.setNumberOfVoters(974);
        votingCouncel034B096.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B096);

        VotingCouncelEntity votingCouncel034B097 = new VotingCouncelEntity();
        votingCouncel034B097.setCode("034Б097");
        votingCouncel034B097.setName("ЛАЗАРЕВО II / 3");
        votingCouncel034B097.setLocation("ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 2");
        votingCouncel034B097.setNumberOfMembers(4);
        votingCouncel034B097.setNumberOfVoters(715);
        votingCouncel034B097.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B097);

        VotingCouncelEntity votingCouncel034B098 = new VotingCouncelEntity();
        votingCouncel034B098.setCode("034Б098");
        votingCouncel034B098.setName("ЛАЗАРЕВО II / 4");
        votingCouncel034B098.setLocation("ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 3");
        votingCouncel034B098.setNumberOfMembers(4);
        votingCouncel034B098.setNumberOfVoters(684);
        votingCouncel034B098.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B098);

        VotingCouncelEntity votingCouncel034B099 = new VotingCouncelEntity();
        votingCouncel034B099.setCode("034Б099");
        votingCouncel034B099.setName("ЛАЗАРЕВО II / 5");
        votingCouncel034B099.setLocation("ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 4");
        votingCouncel034B099.setNumberOfMembers(4);
        votingCouncel034B099.setNumberOfVoters(896);
        votingCouncel034B099.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B099);

        VotingCouncelEntity votingCouncel034B100 = new VotingCouncelEntity();
        votingCouncel034B100.setCode("034Б100");
        votingCouncel034B100.setName("ЛАЗАРЕВО II / 6");
        votingCouncel034B100.setLocation("ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 5");
        votingCouncel034B100.setNumberOfMembers(4);
        votingCouncel034B100.setNumberOfVoters(625);
        votingCouncel034B100.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B100);

        VotingCouncelEntity votingCouncel034B101 = new VotingCouncelEntity();
        votingCouncel034B101.setCode("034Б101");
        votingCouncel034B101.setName("ЛАУШ I / 1");
        votingCouncel034B101.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 1");
        votingCouncel034B101.setNumberOfMembers(4);
        votingCouncel034B101.setNumberOfVoters(739);
        votingCouncel034B101.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B101);

        VotingCouncelEntity votingCouncel034B102 = new VotingCouncelEntity();
        votingCouncel034B102.setCode("034Б102");
        votingCouncel034B102.setName("ЛАУШ I / 2");
        votingCouncel034B102.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 2");
        votingCouncel034B102.setNumberOfMembers(4);
        votingCouncel034B102.setNumberOfVoters(882);
        votingCouncel034B102.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B102);

        VotingCouncelEntity votingCouncel034B103 = new VotingCouncelEntity();
        votingCouncel034B103.setCode("034Б103");
        votingCouncel034B103.setName("ЛАУШ I / 3");
        votingCouncel034B103.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 3");
        votingCouncel034B103.setNumberOfMembers(4);
        votingCouncel034B103.setNumberOfVoters(815);
        votingCouncel034B103.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B103);

        VotingCouncelEntity votingCouncel034B104 = new VotingCouncelEntity();
        votingCouncel034B104.setCode("034Б104");
        votingCouncel034B104.setName("ЛАУШ I / 4");
        votingCouncel034B104.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 4");
        votingCouncel034B104.setNumberOfMembers(4);
        votingCouncel034B104.setNumberOfVoters(711);
        votingCouncel034B104.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B104);

        VotingCouncelEntity votingCouncel034B105 = new VotingCouncelEntity();
        votingCouncel034B105.setCode("034Б105");
        votingCouncel034B105.setName("ЛАУШ I / 5");
        votingCouncel034B105.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 5");
        votingCouncel034B105.setNumberOfMembers(4);
        votingCouncel034B105.setNumberOfVoters(867);
        votingCouncel034B105.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B105);

        VotingCouncelEntity votingCouncel034B106 = new VotingCouncelEntity();
        votingCouncel034B106.setCode("034Б106");
        votingCouncel034B106.setName("ЛАУШ I / 6");
        votingCouncel034B106.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 6");
        votingCouncel034B106.setNumberOfMembers(4);
        votingCouncel034B106.setNumberOfVoters(767);
        votingCouncel034B106.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B106);

        VotingCouncelEntity votingCouncel034B107 = new VotingCouncelEntity();
        votingCouncel034B107.setCode("034Б107");
        votingCouncel034B107.setName("ЛАУШ I / 7");
        votingCouncel034B107.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 7");
        votingCouncel034B107.setNumberOfMembers(4);
        votingCouncel034B107.setNumberOfVoters(766);
        votingCouncel034B107.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B107);

        VotingCouncelEntity votingCouncel034B108 = new VotingCouncelEntity();
        votingCouncel034B108.setCode("034Б108");
        votingCouncel034B108.setName("ЛАУШ I / 8");
        votingCouncel034B108.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 8");
        votingCouncel034B108.setNumberOfMembers(4);
        votingCouncel034B108.setNumberOfVoters(763);
        votingCouncel034B108.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B108);

        VotingCouncelEntity votingCouncel034B109 = new VotingCouncelEntity();
        votingCouncel034B109.setCode("034Б109");
        votingCouncel034B109.setName("ЛАУШ I / 9");
        votingCouncel034B109.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 9");
        votingCouncel034B109.setNumberOfMembers(4);
        votingCouncel034B109.setNumberOfVoters(895);
        votingCouncel034B109.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B109);

        VotingCouncelEntity votingCouncel034B110 = new VotingCouncelEntity();
        votingCouncel034B110.setCode("034Б110");
        votingCouncel034B110.setName("ЛАУШ II / 1");
        votingCouncel034B110.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 10");
        votingCouncel034B110.setNumberOfMembers(4);
        votingCouncel034B110.setNumberOfVoters(906);
        votingCouncel034B110.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B110);

        VotingCouncelEntity votingCouncel034B111 = new VotingCouncelEntity();
        votingCouncel034B111.setCode("034Б111");
        votingCouncel034B111.setName("ЛАУШ II / 2");
        votingCouncel034B111.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 11");
        votingCouncel034B111.setNumberOfMembers(4);
        votingCouncel034B111.setNumberOfVoters(745);
        votingCouncel034B111.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B111);

        VotingCouncelEntity votingCouncel034B112 = new VotingCouncelEntity();
        votingCouncel034B112.setCode("034Б112");
        votingCouncel034B112.setName("ЛАУШ II / 3");
        votingCouncel034B112.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 12");
        votingCouncel034B112.setNumberOfMembers(4);
        votingCouncel034B112.setNumberOfVoters(762);
        votingCouncel034B112.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B112);

        VotingCouncelEntity votingCouncel034B113 = new VotingCouncelEntity();
        votingCouncel034B113.setCode("034Б113");
        votingCouncel034B113.setName("ЛАУШ II / 4");
        votingCouncel034B113.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 13");
        votingCouncel034B113.setNumberOfMembers(4);
        votingCouncel034B113.setNumberOfVoters(823);
        votingCouncel034B113.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B113);

        VotingCouncelEntity votingCouncel034B114 = new VotingCouncelEntity();
        votingCouncel034B114.setCode("034Б114");
        votingCouncel034B114.setName("ЛАУШ II / 5");
        votingCouncel034B114.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 14");
        votingCouncel034B114.setNumberOfMembers(4);
        votingCouncel034B114.setNumberOfVoters(802);
        votingCouncel034B114.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B114);

        VotingCouncelEntity votingCouncel034B115 = new VotingCouncelEntity();
        votingCouncel034B115.setCode("034Б115");
        votingCouncel034B115.setName("ЛАУШ II / 6");
        votingCouncel034B115.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 15");
        votingCouncel034B115.setNumberOfMembers(4);
        votingCouncel034B115.setNumberOfVoters(810);
        votingCouncel034B115.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B115);

        VotingCouncelEntity votingCouncel034B116 = new VotingCouncelEntity();
        votingCouncel034B116.setCode("034Б116");
        votingCouncel034B116.setName("ЛАУШ II / 7");
        votingCouncel034B116.setLocation("ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 16");
        votingCouncel034B116.setNumberOfMembers(4);
        votingCouncel034B116.setNumberOfVoters(868);
        votingCouncel034B116.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B116);

        VotingCouncelEntity votingCouncel034B117 = new VotingCouncelEntity();
        votingCouncel034B117.setCode("034Б117");
        votingCouncel034B117.setName("ЉУБАЧЕВО");
        votingCouncel034B117.setLocation("ПШ \"МИЛАН РАКИЋ\", Љубачево, уч. 1");
        votingCouncel034B117.setNumberOfMembers(4);
        votingCouncel034B117.setNumberOfVoters(443);
        votingCouncel034B117.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B117);

        VotingCouncelEntity votingCouncel034B118 = new VotingCouncelEntity();
        votingCouncel034B118.setCode("034Б118");
        votingCouncel034B118.setName("МИШИН ХАН - 1");
        votingCouncel034B118.setLocation("ПШ \"МИЛУТИН БОЈИЋ\", Мишин Хан, уч. 1");
        votingCouncel034B118.setNumberOfMembers(4);
        votingCouncel034B118.setNumberOfVoters(553);
        votingCouncel034B118.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B118);

        VotingCouncelEntity votingCouncel034B119 = new VotingCouncelEntity();
        votingCouncel034B119.setCode("034Б119");
        votingCouncel034B119.setName("МОТИКЕ - 1");
        votingCouncel034B119.setLocation("ПШ \"МИЛОШ ЦРЊАНСКИ\", Мотике, уч. 1");
        votingCouncel034B119.setNumberOfMembers(4);
        votingCouncel034B119.setNumberOfVoters(864);
        votingCouncel034B119.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B119);

        VotingCouncelEntity votingCouncel034B120 = new VotingCouncelEntity();
        votingCouncel034B120.setCode("034Б120");
        votingCouncel034B120.setName("МОТИКЕ - 2");
        votingCouncel034B120.setLocation("ПШ \"МИЛОШ ЦРЊАНСКИ\", Мотике, уч. 2");
        votingCouncel034B120.setNumberOfMembers(4);
        votingCouncel034B120.setNumberOfVoters(880);
        votingCouncel034B120.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B120);

        VotingCouncelEntity votingCouncel034B121 = new VotingCouncelEntity();
        votingCouncel034B121.setCode("034Б121");
        votingCouncel034B121.setName("НОВА ВАРОШ - 1");
        votingCouncel034B121.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 1");
        votingCouncel034B121.setNumberOfMembers(4);
        votingCouncel034B121.setNumberOfVoters(704);
        votingCouncel034B121.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B121);

        VotingCouncelEntity votingCouncel034B122 = new VotingCouncelEntity();
        votingCouncel034B122.setCode("034Б122");
        votingCouncel034B122.setName("НОВА ВАРОШ - 2");
        votingCouncel034B122.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 2");
        votingCouncel034B122.setNumberOfMembers(4);
        votingCouncel034B122.setNumberOfVoters(735);
        votingCouncel034B122.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B122);

        VotingCouncelEntity votingCouncel034B123 = new VotingCouncelEntity();
        votingCouncel034B123.setCode("034Б123");
        votingCouncel034B123.setName("НОВА ВАРОШ - 3");
        votingCouncel034B123.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 3");
        votingCouncel034B123.setNumberOfMembers(4);
        votingCouncel034B123.setNumberOfVoters(745);
        votingCouncel034B123.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B123);

        VotingCouncelEntity votingCouncel034B124 = new VotingCouncelEntity();
        votingCouncel034B124.setCode("034Б124");
        votingCouncel034B124.setName("НОВА ВАРОШ - 4");
        votingCouncel034B124.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 4");
        votingCouncel034B124.setNumberOfMembers(4);
        votingCouncel034B124.setNumberOfVoters(793);
        votingCouncel034B124.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B124);

        VotingCouncelEntity votingCouncel034B125 = new VotingCouncelEntity();
        votingCouncel034B125.setCode("034Б125");
        votingCouncel034B125.setName("НОВА ВАРОШ - 5");
        votingCouncel034B125.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 5");
        votingCouncel034B125.setNumberOfMembers(4);
        votingCouncel034B125.setNumberOfVoters(721);
        votingCouncel034B125.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B125);

        VotingCouncelEntity votingCouncel034B126 = new VotingCouncelEntity();
        votingCouncel034B126.setCode("034Б126");
        votingCouncel034B126.setName("ОБИЛИЋЕВО I / 1");
        votingCouncel034B126.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 1");
        votingCouncel034B126.setNumberOfMembers(4);
        votingCouncel034B126.setNumberOfVoters(792);
        votingCouncel034B126.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B126);

        VotingCouncelEntity votingCouncel034B127 = new VotingCouncelEntity();
        votingCouncel034B127.setCode("034Б127");
        votingCouncel034B127.setName("ОБИЛИЋЕВО I / 2");
        votingCouncel034B127.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 2");
        votingCouncel034B127.setNumberOfMembers(4);
        votingCouncel034B127.setNumberOfVoters(688);
        votingCouncel034B127.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B127);

        VotingCouncelEntity votingCouncel034B128 = new VotingCouncelEntity();
        votingCouncel034B128.setCode("034Б128");
        votingCouncel034B128.setName("ОБИЛИЋЕВО I / 3");
        votingCouncel034B128.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 3");
        votingCouncel034B128.setNumberOfMembers(4);
        votingCouncel034B128.setNumberOfVoters(795);
        votingCouncel034B128.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B128);

        VotingCouncelEntity votingCouncel034B129 = new VotingCouncelEntity();
        votingCouncel034B129.setCode("034Б129");
        votingCouncel034B129.setName("ОБИЛИЋЕВО I / 4");
        votingCouncel034B129.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 4");
        votingCouncel034B129.setNumberOfMembers(4);
        votingCouncel034B129.setNumberOfVoters(736);
        votingCouncel034B129.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B129);

        VotingCouncelEntity votingCouncel034B130 = new VotingCouncelEntity();
        votingCouncel034B130.setCode("034Б130");
        votingCouncel034B130.setName("ОБИЛИЋЕВО I / 5");
        votingCouncel034B130.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 5");
        votingCouncel034B130.setNumberOfMembers(4);
        votingCouncel034B130.setNumberOfVoters(724);
        votingCouncel034B130.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B130);

        VotingCouncelEntity votingCouncel034B131 = new VotingCouncelEntity();
        votingCouncel034B131.setCode("034Б131");
        votingCouncel034B131.setName("ОБИЛИЋЕВО I / 6");
        votingCouncel034B131.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 6");
        votingCouncel034B131.setNumberOfMembers(4);
        votingCouncel034B131.setNumberOfVoters(957);
        votingCouncel034B131.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B131);

        VotingCouncelEntity votingCouncel034B132 = new VotingCouncelEntity();
        votingCouncel034B132.setCode("034Б132");
        votingCouncel034B132.setName("ОБИЛИЋЕВО I / 7");
        votingCouncel034B132.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 7");
        votingCouncel034B132.setNumberOfMembers(4);
        votingCouncel034B132.setNumberOfVoters(694);
        votingCouncel034B132.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B132);

        VotingCouncelEntity votingCouncel034B133 = new VotingCouncelEntity();
        votingCouncel034B133.setCode("034Б133");
        votingCouncel034B133.setName("ОБИЛИЋЕВО I / 8");
        votingCouncel034B133.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 8");
        votingCouncel034B133.setNumberOfMembers(4);
        votingCouncel034B133.setNumberOfVoters(659);
        votingCouncel034B133.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B133);

        VotingCouncelEntity votingCouncel034B134 = new VotingCouncelEntity();
        votingCouncel034B134.setCode("034Б134");
        votingCouncel034B134.setName("ОБИЛИЋЕВО I / 9");
        votingCouncel034B134.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 9");
        votingCouncel034B134.setNumberOfMembers(4);
        votingCouncel034B134.setNumberOfVoters(690);
        votingCouncel034B134.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B134);

        VotingCouncelEntity votingCouncel034B135 = new VotingCouncelEntity();
        votingCouncel034B135.setCode("034Б135");
        votingCouncel034B135.setName("ОБИЛИЋЕВО I / 10");
        votingCouncel034B135.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 10");
        votingCouncel034B135.setNumberOfMembers(4);
        votingCouncel034B135.setNumberOfVoters(659);
        votingCouncel034B135.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B135);

        VotingCouncelEntity votingCouncel034B136 = new VotingCouncelEntity();
        votingCouncel034B136.setCode("034Б136");
        votingCouncel034B136.setName("ОБИЛИЋЕВО I / 11");
        votingCouncel034B136.setLocation("ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 11");
        votingCouncel034B136.setNumberOfMembers(4);
        votingCouncel034B136.setNumberOfVoters(675);
        votingCouncel034B136.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B136);

        VotingCouncelEntity votingCouncel034B137 = new VotingCouncelEntity();
        votingCouncel034B137.setCode("034Б137");
        votingCouncel034B137.setName("ОБИЛИЋЕВО II / 1");
        votingCouncel034B137.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 1");
        votingCouncel034B137.setNumberOfMembers(4);
        votingCouncel034B137.setNumberOfVoters(720);
        votingCouncel034B137.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B137);

        VotingCouncelEntity votingCouncel034B138 = new VotingCouncelEntity();
        votingCouncel034B138.setCode("034Б138");
        votingCouncel034B138.setName("ОБИЛИЋЕВО II / 2");
        votingCouncel034B138.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 2");
        votingCouncel034B138.setNumberOfMembers(4);
        votingCouncel034B138.setNumberOfVoters(763);
        votingCouncel034B138.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B138);

        VotingCouncelEntity votingCouncel034B139 = new VotingCouncelEntity();
        votingCouncel034B139.setCode("034Б139");
        votingCouncel034B139.setName("ОБИЛИЋЕВО II / 3");
        votingCouncel034B139.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 3");
        votingCouncel034B139.setNumberOfMembers(4);
        votingCouncel034B139.setNumberOfVoters(657);
        votingCouncel034B139.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B139);

        VotingCouncelEntity votingCouncel034B140 = new VotingCouncelEntity();
        votingCouncel034B140.setCode("034Б140");
        votingCouncel034B140.setName("ОБИЛИЋЕВО II / 4");
        votingCouncel034B140.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 4");
        votingCouncel034B140.setNumberOfMembers(4);
        votingCouncel034B140.setNumberOfVoters(713);
        votingCouncel034B140.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B140);

        VotingCouncelEntity votingCouncel034B141 = new VotingCouncelEntity();
        votingCouncel034B141.setCode("034Б141");
        votingCouncel034B141.setName("ОБИЛИЋЕВО II / 5");
        votingCouncel034B141.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 5");
        votingCouncel034B141.setNumberOfMembers(4);
        votingCouncel034B141.setNumberOfVoters(811);
        votingCouncel034B141.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B141);

        VotingCouncelEntity votingCouncel034B142 = new VotingCouncelEntity();
        votingCouncel034B142.setCode("034Б142");
        votingCouncel034B142.setName("ОБИЛИЋЕВО II / 6");
        votingCouncel034B142.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 6");
        votingCouncel034B142.setNumberOfMembers(4);
        votingCouncel034B142.setNumberOfVoters(675);
        votingCouncel034B142.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B142);

        VotingCouncelEntity votingCouncel034B143 = new VotingCouncelEntity();
        votingCouncel034B143.setCode("034Б143");
        votingCouncel034B143.setName("ОБИЛИЋЕВО II / 7");
        votingCouncel034B143.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 7");
        votingCouncel034B143.setNumberOfMembers(4);
        votingCouncel034B143.setNumberOfVoters(685);
        votingCouncel034B143.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B143);

        VotingCouncelEntity votingCouncel034B144 = new VotingCouncelEntity();
        votingCouncel034B144.setCode("034Б144");
        votingCouncel034B144.setName("ОБИЛИЋЕВО II / 8");
        votingCouncel034B144.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 8");
        votingCouncel034B144.setNumberOfMembers(4);
        votingCouncel034B144.setNumberOfVoters(695);
        votingCouncel034B144.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B144);

        VotingCouncelEntity votingCouncel034B145 = new VotingCouncelEntity();
        votingCouncel034B145.setCode("034Б145");
        votingCouncel034B145.setName("ОБИЛИЋЕВО II / 9");
        votingCouncel034B145.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 9");
        votingCouncel034B145.setNumberOfMembers(4);
        votingCouncel034B145.setNumberOfVoters(695);
        votingCouncel034B145.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B145);

        VotingCouncelEntity votingCouncel034B146 = new VotingCouncelEntity();
        votingCouncel034B146.setCode("034Б146");
        votingCouncel034B146.setName("ОБИЛИЋЕВО II / 10");
        votingCouncel034B146.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 10");
        votingCouncel034B146.setNumberOfMembers(4);
        votingCouncel034B146.setNumberOfVoters(742);
        votingCouncel034B146.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B146);

        VotingCouncelEntity votingCouncel034B147 = new VotingCouncelEntity();
        votingCouncel034B147.setCode("034Б147");
        votingCouncel034B147.setName("ОБИЛИЋЕВО II / 11");
        votingCouncel034B147.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 11");
        votingCouncel034B147.setNumberOfMembers(4);
        votingCouncel034B147.setNumberOfVoters(707);
        votingCouncel034B147.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B147);

        VotingCouncelEntity votingCouncel034B148 = new VotingCouncelEntity();
        votingCouncel034B148.setCode("034Б148");
        votingCouncel034B148.setName("ОБИЛИЋЕВО II / 12");
        votingCouncel034B148.setLocation("ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 12");
        votingCouncel034B148.setNumberOfMembers(4);
        votingCouncel034B148.setNumberOfVoters(724);
        votingCouncel034B148.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B148);

        VotingCouncelEntity votingCouncel034B149 = new VotingCouncelEntity();
        votingCouncel034B149.setCode("034Б149");
        votingCouncel034B149.setName("ПАВИЋИ");
        votingCouncel034B149.setLocation("ПШ \"ПЕТАР КОЧИЋ\", Павићи, уч. 1");
        votingCouncel034B149.setNumberOfMembers(4);
        votingCouncel034B149.setNumberOfVoters(569);
        votingCouncel034B149.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B149);

        VotingCouncelEntity votingCouncel034B150 = new VotingCouncelEntity();
        votingCouncel034B150.setCode("034Б150");
        votingCouncel034B150.setName("РОСУЉЕ - 1");
        votingCouncel034B150.setLocation("ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 1");
        votingCouncel034B150.setNumberOfMembers(4);
        votingCouncel034B150.setNumberOfVoters(970);
        votingCouncel034B150.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B150);

        VotingCouncelEntity votingCouncel034B151 = new VotingCouncelEntity();
        votingCouncel034B151.setCode("034Б151");
        votingCouncel034B151.setName("ПЕТРИЋЕВАЦ - 1");
        votingCouncel034B151.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 1");
        votingCouncel034B151.setNumberOfMembers(4);
        votingCouncel034B151.setNumberOfVoters(731);
        votingCouncel034B151.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B151);

        VotingCouncelEntity votingCouncel034B152 = new VotingCouncelEntity();
        votingCouncel034B152.setCode("034Б152");
        votingCouncel034B152.setName("ПАПРИКОВАЦ 1");
        votingCouncel034B152.setLocation("ШКОЛА ЗА СЛУШНО ОШТЕЋЕНЕ, Др Ј. Рашковића 28, уч. 1");
        votingCouncel034B152.setNumberOfMembers(4);
        votingCouncel034B152.setNumberOfVoters(803);
        votingCouncel034B152.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B152);

        VotingCouncelEntity votingCouncel034B153 = new VotingCouncelEntity();
        votingCouncel034B153.setCode("034Б153");
        votingCouncel034B153.setName("ПАПРИКОВАЦ 2");
        votingCouncel034B153.setLocation("ШКОЛА ЗА СЛУШНО ОШТЕЋЕНЕ, Др Ј. Рашковића 28, уч. 2");
        votingCouncel034B153.setNumberOfMembers(4);
        votingCouncel034B153.setNumberOfVoters(676);
        votingCouncel034B153.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B153);

        VotingCouncelEntity votingCouncel034B154 = new VotingCouncelEntity();
        votingCouncel034B154.setCode("034Б154");
        votingCouncel034B154.setName("ПАПРИКОВАЦ 3");
        votingCouncel034B154.setLocation("ШКОЛА ЗА СЛУШНО ОШТЕЋЕНЕ, Др Ј. Рашковића 28, уч. 3");
        votingCouncel034B154.setNumberOfMembers(4);
        votingCouncel034B154.setNumberOfVoters(666);
        votingCouncel034B154.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B154);

        VotingCouncelEntity votingCouncel034B155 = new VotingCouncelEntity();
        votingCouncel034B155.setCode("034Б155");
        votingCouncel034B155.setName("ПАПРИКОВАЦ 4");
        votingCouncel034B155.setLocation("ШКОЛА ЗА СЛУШНО ОШТЕЋЕНЕ, Др Ј. Рашковића 28, уч. 4");
        votingCouncel034B155.setNumberOfMembers(4);
        votingCouncel034B155.setNumberOfVoters(817);
        votingCouncel034B155.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B155);

        VotingCouncelEntity votingCouncel034B156 = new VotingCouncelEntity();
        votingCouncel034B156.setCode("034Б156");
        votingCouncel034B156.setName("ПАПРИКОВАЦ 5");
        votingCouncel034B156.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 6");
        votingCouncel034B156.setNumberOfMembers(4);
        votingCouncel034B156.setNumberOfVoters(828);
        votingCouncel034B156.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B156);

        VotingCouncelEntity votingCouncel034B157 = new VotingCouncelEntity();
        votingCouncel034B157.setCode("034Б157");
        votingCouncel034B157.setName("ПАПРИКОВАЦ 6");
        votingCouncel034B157.setLocation("ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 7");
        votingCouncel034B157.setNumberOfMembers(4);
        votingCouncel034B157.setNumberOfVoters(807);
        votingCouncel034B157.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B157);

        VotingCouncelEntity votingCouncel034B158 = new VotingCouncelEntity();
        votingCouncel034B158.setCode("034Б158");
        votingCouncel034B158.setName("ПАПРИКОВАЦ 7");
        votingCouncel034B158.setLocation("ЗАВОД ЗА ДИСТРОФИЧАРЕ, Војвођанска бб, сала 1");
        votingCouncel034B158.setNumberOfMembers(4);
        votingCouncel034B158.setNumberOfVoters(724);
        votingCouncel034B158.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B158);

        VotingCouncelEntity votingCouncel034B159 = new VotingCouncelEntity();
        votingCouncel034B159.setCode("034Б159");
        votingCouncel034B159.setName("ПАПРИКОВАЦ 8");
        votingCouncel034B159.setLocation("ЗАВОД ЗА ДИСТРОФИЧАРЕ, Војвођанска бб, сала 2");
        votingCouncel034B159.setNumberOfMembers(4);
        votingCouncel034B159.setNumberOfVoters(752);
        votingCouncel034B159.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B159);

        VotingCouncelEntity votingCouncel034B160 = new VotingCouncelEntity();
        votingCouncel034B160.setCode("034Б160");
        votingCouncel034B160.setName("ПЕТРИЋЕВАЦ 2");
        votingCouncel034B160.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 2");
        votingCouncel034B160.setNumberOfMembers(4);
        votingCouncel034B160.setNumberOfVoters(804);
        votingCouncel034B160.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B160);

        VotingCouncelEntity votingCouncel034B161 = new VotingCouncelEntity();
        votingCouncel034B161.setCode("034Б161");
        votingCouncel034B161.setName("ПЕТРИЋЕВАЦ 3");
        votingCouncel034B161.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 3");
        votingCouncel034B161.setNumberOfMembers(4);
        votingCouncel034B161.setNumberOfVoters(929);
        votingCouncel034B161.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B161);

        VotingCouncelEntity votingCouncel034B162 = new VotingCouncelEntity();
        votingCouncel034B162.setCode("034Б162");
        votingCouncel034B162.setName("ПЕТРИЋЕВАЦ 4");
        votingCouncel034B162.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 4");
        votingCouncel034B162.setNumberOfMembers(4);
        votingCouncel034B162.setNumberOfVoters(717);
        votingCouncel034B162.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B162);

        VotingCouncelEntity votingCouncel034B163 = new VotingCouncelEntity();
        votingCouncel034B163.setCode("034Б163");
        votingCouncel034B163.setName("ПЕТРИЋЕВАЦ 5");
        votingCouncel034B163.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 5");
        votingCouncel034B163.setNumberOfMembers(4);
        votingCouncel034B163.setNumberOfVoters(702);
        votingCouncel034B163.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B163);

        VotingCouncelEntity votingCouncel034B164 = new VotingCouncelEntity();
        votingCouncel034B164.setCode("034Б164");
        votingCouncel034B164.setName("ПЕТРИЋЕВАЦ 6");
        votingCouncel034B164.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 6");
        votingCouncel034B164.setNumberOfMembers(4);
        votingCouncel034B164.setNumberOfVoters(696);
        votingCouncel034B164.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B164);

        VotingCouncelEntity votingCouncel034B165 = new VotingCouncelEntity();
        votingCouncel034B165.setCode("034Б165");
        votingCouncel034B165.setName("ПЕТРИЋЕВАЦ 7");
        votingCouncel034B165.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 7");
        votingCouncel034B165.setNumberOfMembers(4);
        votingCouncel034B165.setNumberOfVoters(746);
        votingCouncel034B165.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B165);

        VotingCouncelEntity votingCouncel034B166 = new VotingCouncelEntity();
        votingCouncel034B166.setCode("034Б166");
        votingCouncel034B166.setName("ПЕТРИЋЕВАЦ 8");
        votingCouncel034B166.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 8");
        votingCouncel034B166.setNumberOfMembers(4);
        votingCouncel034B166.setNumberOfVoters(757);
        votingCouncel034B166.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B166);

        VotingCouncelEntity votingCouncel034B167 = new VotingCouncelEntity();
        votingCouncel034B167.setCode("034Б167");
        votingCouncel034B167.setName("ПЕТРИЋЕВАЦ 9");
        votingCouncel034B167.setLocation("ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 9");
        votingCouncel034B167.setNumberOfMembers(4);
        votingCouncel034B167.setNumberOfVoters(637);
        votingCouncel034B167.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B167);

        VotingCouncelEntity votingCouncel034B168 = new VotingCouncelEntity();
        votingCouncel034B168.setCode("034Б168");
        votingCouncel034B168.setName("ПИСКАВИЦА 1");
        votingCouncel034B168.setLocation("ОШ \"ЋИРИЛО И МЕТОДИЈЕ\", Пискавица, уч. 1");
        votingCouncel034B168.setNumberOfMembers(4);
        votingCouncel034B168.setNumberOfVoters(824);
        votingCouncel034B168.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B168);

        VotingCouncelEntity votingCouncel034B169 = new VotingCouncelEntity();
        votingCouncel034B169.setCode("034Б169");
        votingCouncel034B169.setName("ПИСКАВИЦА 2");
        votingCouncel034B169.setLocation("ОШ \"ЋИРИЛО И МЕТОДИЈЕ\", Пискавица, уч. 2");
        votingCouncel034B169.setNumberOfMembers(4);
        votingCouncel034B169.setNumberOfVoters(808);
        votingCouncel034B169.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B169);

        VotingCouncelEntity votingCouncel034B170 = new VotingCouncelEntity();
        votingCouncel034B170.setCode("034Б170");
        votingCouncel034B170.setName("ПОБРЂЕ 1");
        votingCouncel034B170.setLocation("ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 6");
        votingCouncel034B170.setNumberOfMembers(4);
        votingCouncel034B170.setNumberOfVoters(703);
        votingCouncel034B170.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B170);

        VotingCouncelEntity votingCouncel034B171 = new VotingCouncelEntity();
        votingCouncel034B171.setCode("034Б171");
        votingCouncel034B171.setName("ПОБРЂЕ 2");
        votingCouncel034B171.setLocation("ГИМНАЗИЈА, Змај Јовина 13, уч. 3");
        votingCouncel034B171.setNumberOfMembers(4);
        votingCouncel034B171.setNumberOfVoters(588);
        votingCouncel034B171.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B171);

        VotingCouncelEntity votingCouncel034B172 = new VotingCouncelEntity();
        votingCouncel034B172.setCode("034Б172");
        votingCouncel034B172.setName("ПОБРЂЕ 3");
        votingCouncel034B172.setLocation("ГИМНАЗИЈА, Змај Јовина 13, уч. 4");
        votingCouncel034B172.setNumberOfMembers(4);
        votingCouncel034B172.setNumberOfVoters(617);
        votingCouncel034B172.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B172);

        VotingCouncelEntity votingCouncel034B173A = new VotingCouncelEntity();
        votingCouncel034B173A.setCode("034Б173А");
        votingCouncel034B173A.setName("ПОТКОЗАРЈЕ");
        votingCouncel034B173A.setLocation("ОШ \"МИЛУТИН БОЈИЋ\", Поткозарје, уч. 1");
        votingCouncel034B173A.setNumberOfMembers(4);
        votingCouncel034B173A.setNumberOfVoters(760);
        votingCouncel034B173A.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B173A);

        VotingCouncelEntity votingCouncel034B173B = new VotingCouncelEntity();
        votingCouncel034B173B.setCode("034Б173Б");
        votingCouncel034B173B.setName("ПОТКОЗАРЈЕ");
        votingCouncel034B173B.setLocation("ОШ \"МИЛУТИН БОЈИЋ\", Поткозарје, уч. 2");
        votingCouncel034B173B.setNumberOfMembers(4);
        votingCouncel034B173B.setNumberOfVoters(960);
        votingCouncel034B173B.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B173B);

        VotingCouncelEntity votingCouncel034B174 = new VotingCouncelEntity();
        votingCouncel034B174.setCode("034Б174");
        votingCouncel034B174.setName("ПРИЈАКОВЦИ");
        votingCouncel034B174.setLocation("МЗ ПРИЈАКОВЦИ, сала 1");
        votingCouncel034B174.setNumberOfMembers(4);
        votingCouncel034B174.setNumberOfVoters(792);
        votingCouncel034B174.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B174);

        VotingCouncelEntity votingCouncel034B175 = new VotingCouncelEntity();
        votingCouncel034B175.setCode("034Б175");
        votingCouncel034B175.setName("ПРИЈЕЧАНИ - 1");
        votingCouncel034B175.setLocation("ПШ \"ЈОВАН ДУЧИЋ\", Пријечани, уч. 1");
        votingCouncel034B175.setNumberOfMembers(4);
        votingCouncel034B175.setNumberOfVoters(689);
        votingCouncel034B175.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B175);

        VotingCouncelEntity votingCouncel034B176 = new VotingCouncelEntity();
        votingCouncel034B176.setCode("034Б176");
        votingCouncel034B176.setName("РЕКАВИЦЕ I");
        votingCouncel034B176.setLocation("МЗ РЕКАВИЦЕ I, сала 1");
        votingCouncel034B176.setNumberOfMembers(4);
        votingCouncel034B176.setNumberOfVoters(667);
        votingCouncel034B176.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B176);

        VotingCouncelEntity votingCouncel034B177 = new VotingCouncelEntity();
        votingCouncel034B177.setCode("034Б177");
        votingCouncel034B177.setName("РЕКАВИЦЕ II");
        votingCouncel034B177.setLocation("МЗ РЕКАВИЦЕ II, сала 1");
        votingCouncel034B177.setNumberOfMembers(4);
        votingCouncel034B177.setNumberOfVoters(348);
        votingCouncel034B177.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B177);

        VotingCouncelEntity votingCouncel034B178 = new VotingCouncelEntity();
        votingCouncel034B178.setCode("034Б178");
        votingCouncel034B178.setName("РОСУЉЕ 2");
        votingCouncel034B178.setLocation("МЗ РОСУЉЕ, Др Младена Стојановића 10, сала 1");
        votingCouncel034B178.setNumberOfMembers(4);
        votingCouncel034B178.setNumberOfVoters(739);
        votingCouncel034B178.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B178);

        VotingCouncelEntity votingCouncel034B179 = new VotingCouncelEntity();
        votingCouncel034B179.setCode("034Б179");
        votingCouncel034B179.setName("РОСУЉЕ 3");
        votingCouncel034B179.setLocation("МЗ РОСУЉЕ, Др Младена Стојановића 10, сала 2");
        votingCouncel034B179.setNumberOfMembers(4);
        votingCouncel034B179.setNumberOfVoters(668);
        votingCouncel034B179.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B179);

        VotingCouncelEntity votingCouncel034B180 = new VotingCouncelEntity();
        votingCouncel034B180.setCode("034Б180");
        votingCouncel034B180.setName("РОСУЉЕ 4");
        votingCouncel034B180.setLocation("ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 2");
        votingCouncel034B180.setNumberOfMembers(4);
        votingCouncel034B180.setNumberOfVoters(666);
        votingCouncel034B180.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B180);

        VotingCouncelEntity votingCouncel034B181 = new VotingCouncelEntity();
        votingCouncel034B181.setCode("034Б181");
        votingCouncel034B181.setName("РОСУЉЕ 5");
        votingCouncel034B181.setLocation("ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 3");
        votingCouncel034B181.setNumberOfMembers(4);
        votingCouncel034B181.setNumberOfVoters(723);
        votingCouncel034B181.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B181);

        VotingCouncelEntity votingCouncel034B182 = new VotingCouncelEntity();
        votingCouncel034B182.setCode("034Б182");
        votingCouncel034B182.setName("РОСУЉЕ 6");
        votingCouncel034B182.setLocation("ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 4");
        votingCouncel034B182.setNumberOfMembers(4);
        votingCouncel034B182.setNumberOfVoters(816);
        votingCouncel034B182.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B182);

        VotingCouncelEntity votingCouncel034B183 = new VotingCouncelEntity();
        votingCouncel034B183.setCode("034Б183");
        votingCouncel034B183.setName("РОСУЉЕ 7");
        votingCouncel034B183.setLocation("ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 5");
        votingCouncel034B183.setNumberOfMembers(4);
        votingCouncel034B183.setNumberOfVoters(741);
        votingCouncel034B183.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B183);

        VotingCouncelEntity votingCouncel034B184 = new VotingCouncelEntity();
        votingCouncel034B184.setCode("034Б184");
        votingCouncel034B184.setName("РОСУЉЕ 8");
        votingCouncel034B184.setLocation("ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 6");
        votingCouncel034B184.setNumberOfMembers(4);
        votingCouncel034B184.setNumberOfVoters(716);
        votingCouncel034B184.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B184);

        VotingCouncelEntity votingCouncel034B185 = new VotingCouncelEntity();
        votingCouncel034B185.setCode("034Б185");
        votingCouncel034B185.setName("РОСУЉЕ 9");
        votingCouncel034B185.setLocation("КИНОЛОШКИ САВЕЗ БАЊА ЛУКА, Битољска бб, сала 4");
        votingCouncel034B185.setNumberOfMembers(4);
        votingCouncel034B185.setNumberOfVoters(714);
        votingCouncel034B185.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B185);

        VotingCouncelEntity votingCouncel034B186 = new VotingCouncelEntity();
        votingCouncel034B186.setCode("034Б186");
        votingCouncel034B186.setName("САРАЧИЦА 1");
        votingCouncel034B186.setLocation("ДРУШТВЕНИ ДОМ САРАЧИЦА, сала 1");
        votingCouncel034B186.setNumberOfMembers(4);
        votingCouncel034B186.setNumberOfVoters(844);
        votingCouncel034B186.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B186);

        VotingCouncelEntity votingCouncel034B187 = new VotingCouncelEntity();
        votingCouncel034B187.setCode("034Б187");
        votingCouncel034B187.setName("САРАЧИЦА 2 / ПАВЛОВАЦ 1");
        votingCouncel034B187.setLocation("ПШ \"СВЕТИ САВА\", Павловац, уч. 1");
        votingCouncel034B187.setNumberOfMembers(4);
        votingCouncel034B187.setNumberOfVoters(974);
        votingCouncel034B187.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B187);

        VotingCouncelEntity votingCouncel034B188 = new VotingCouncelEntity();
        votingCouncel034B188.setCode("034Б188");
        votingCouncel034B188.setName("СРПСКЕ ТОПЛИЦЕ 1");
        votingCouncel034B188.setLocation("ОШ \"БРАНИСЛАВ НУШИЋ\", Мањачких устаника 32, уч. 1");
        votingCouncel034B188.setNumberOfMembers(4);
        votingCouncel034B188.setNumberOfVoters(655);
        votingCouncel034B188.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B188);

        VotingCouncelEntity votingCouncel034B189 = new VotingCouncelEntity();
        votingCouncel034B189.setCode("034Б189");
        votingCouncel034B189.setName("СРПСКЕ ТОПЛИЦЕ 2");
        votingCouncel034B189.setLocation("ОШ \"БРАНИСЛАВ НУШИЋ\", Мањачких устаника 32, уч. 2");
        votingCouncel034B189.setNumberOfMembers(4);
        votingCouncel034B189.setNumberOfVoters(721);
        votingCouncel034B189.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B189);

        VotingCouncelEntity votingCouncel034B190 = new VotingCouncelEntity();
        votingCouncel034B190.setCode("034Б190");
        votingCouncel034B190.setName("СРПСКЕ ТОПЛИЦЕ 3");
        votingCouncel034B190.setLocation("ОШ \"БРАНИСЛАВ НУШИЋ\", Мањачких устаника 32, уч. 3");
        votingCouncel034B190.setNumberOfMembers(4);
        votingCouncel034B190.setNumberOfVoters(693);
        votingCouncel034B190.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B190);

        VotingCouncelEntity votingCouncel034B191 = new VotingCouncelEntity();
        votingCouncel034B191.setCode("034Б191");
        votingCouncel034B191.setName("СРПСКЕ ТОПЛИЦЕ - 4 / НОВОСЕЛИЈА 1");
        votingCouncel034B191.setLocation("ПШ \"МИЛАН РАКИЋ\", Краг. краљ. жртава 59, уч. 1");
        votingCouncel034B191.setNumberOfMembers(4);
        votingCouncel034B191.setNumberOfVoters(765);
        votingCouncel034B191.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B191);

        VotingCouncelEntity votingCouncel034B192 = new VotingCouncelEntity();
        votingCouncel034B192.setCode("034Б192");
        votingCouncel034B192.setName("СРПСКЕ ТОПЛИЦЕ - 5 / НОВОСЕЛИЈА 2");
        votingCouncel034B192.setLocation("ПШ \"МИЛАН РАКИЋ\", Краг. краљ. жртава 59, уч. 2");
        votingCouncel034B192.setNumberOfMembers(4);
        votingCouncel034B192.setNumberOfVoters(740);
        votingCouncel034B192.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B192);

        VotingCouncelEntity votingCouncel034B193 = new VotingCouncelEntity();
        votingCouncel034B193.setCode("034Б193");
        votingCouncel034B193.setName("СТАРЧЕВИЦА 1");
        votingCouncel034B193.setLocation("МЗ СТРАЧЕВИЦА, Др В. Ђ. Кецмановића 1, сала 1");
        votingCouncel034B193.setNumberOfMembers(4);
        votingCouncel034B193.setNumberOfVoters(807);
        votingCouncel034B193.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B193);

        VotingCouncelEntity votingCouncel034B194 = new VotingCouncelEntity();
        votingCouncel034B194.setCode("034Б194");
        votingCouncel034B194.setName("СТАРЧЕВИЦА 2");
        votingCouncel034B194.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 1");
        votingCouncel034B194.setNumberOfMembers(4);
        votingCouncel034B194.setNumberOfVoters(811);
        votingCouncel034B194.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B194);

        VotingCouncelEntity votingCouncel034B195 = new VotingCouncelEntity();
        votingCouncel034B195.setCode("034Б195");
        votingCouncel034B195.setName("СТАРЧЕВИЦА 3");
        votingCouncel034B195.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 2");
        votingCouncel034B195.setNumberOfMembers(4);
        votingCouncel034B195.setNumberOfVoters(891);
        votingCouncel034B195.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B195);

        VotingCouncelEntity votingCouncel034B196 = new VotingCouncelEntity();
        votingCouncel034B196.setCode("034Б196");
        votingCouncel034B196.setName("СТАРЧЕВИЦА 4");
        votingCouncel034B196.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 3");
        votingCouncel034B196.setNumberOfMembers(4);
        votingCouncel034B196.setNumberOfVoters(593);
        votingCouncel034B196.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B196);

        VotingCouncelEntity votingCouncel034B197 = new VotingCouncelEntity();
        votingCouncel034B197.setCode("034Б197");
        votingCouncel034B197.setName("СТАРЧЕВИЦА 5");
        votingCouncel034B197.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 4");
        votingCouncel034B197.setNumberOfMembers(4);
        votingCouncel034B197.setNumberOfVoters(675);
        votingCouncel034B197.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B197);

        VotingCouncelEntity votingCouncel034B198 = new VotingCouncelEntity();
        votingCouncel034B198.setCode("034Б198");
        votingCouncel034B198.setName("СТАРЧЕВИЦА 6");
        votingCouncel034B198.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 5");
        votingCouncel034B198.setNumberOfMembers(4);
        votingCouncel034B198.setNumberOfVoters(668);
        votingCouncel034B198.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B198);

        VotingCouncelEntity votingCouncel034B199 = new VotingCouncelEntity();
        votingCouncel034B199.setCode("034Б199");
        votingCouncel034B199.setName("СТАРЧЕВИЦА 7");
        votingCouncel034B199.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 6");
        votingCouncel034B199.setNumberOfMembers(4);
        votingCouncel034B199.setNumberOfVoters(799);
        votingCouncel034B199.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B199);

        VotingCouncelEntity votingCouncel034B200 = new VotingCouncelEntity();
        votingCouncel034B200.setCode("034Б200");
        votingCouncel034B200.setName("СТАРЧЕВИЦА 8");
        votingCouncel034B200.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 7");
        votingCouncel034B200.setNumberOfMembers(4);
        votingCouncel034B200.setNumberOfVoters(703);
        votingCouncel034B200.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B200);

        VotingCouncelEntity votingCouncel034B201 = new VotingCouncelEntity();
        votingCouncel034B201.setCode("034Б201");
        votingCouncel034B201.setName("СТАРЧЕВИЦА 9");
        votingCouncel034B201.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 8");
        votingCouncel034B201.setNumberOfMembers(4);
        votingCouncel034B201.setNumberOfVoters(838);
        votingCouncel034B201.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B201);

        VotingCouncelEntity votingCouncel034B202 = new VotingCouncelEntity();
        votingCouncel034B202.setCode("034Б202");
        votingCouncel034B202.setName("СТАРЧЕВИЦА 10");
        votingCouncel034B202.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 9");
        votingCouncel034B202.setNumberOfMembers(4);
        votingCouncel034B202.setNumberOfVoters(802);
        votingCouncel034B202.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B202);

        VotingCouncelEntity votingCouncel034B203 = new VotingCouncelEntity();
        votingCouncel034B203.setCode("034Б203");
        votingCouncel034B203.setName("СТАРЧЕВИЦА 11");
        votingCouncel034B203.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 10");
        votingCouncel034B203.setNumberOfMembers(4);
        votingCouncel034B203.setNumberOfVoters(822);
        votingCouncel034B203.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B203);

        VotingCouncelEntity votingCouncel034B204 = new VotingCouncelEntity();
        votingCouncel034B204.setCode("034Б204");
        votingCouncel034B204.setName("СТАРЧЕВИЦА 12");
        votingCouncel034B204.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 11");
        votingCouncel034B204.setNumberOfMembers(4);
        votingCouncel034B204.setNumberOfVoters(765);
        votingCouncel034B204.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B204);

        VotingCouncelEntity votingCouncel034B205 = new VotingCouncelEntity();
        votingCouncel034B205.setCode("034Б205");
        votingCouncel034B205.setName("СТАРЧЕВИЦА 13");
        votingCouncel034B205.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 12");
        votingCouncel034B205.setNumberOfMembers(4);
        votingCouncel034B205.setNumberOfVoters(846);
        votingCouncel034B205.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B205);

        VotingCouncelEntity votingCouncel034B206 = new VotingCouncelEntity();
        votingCouncel034B206.setCode("034Б206");
        votingCouncel034B206.setName("СТАРЧЕВИЦА 14");
        votingCouncel034B206.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 13");
        votingCouncel034B206.setNumberOfMembers(4);
        votingCouncel034B206.setNumberOfVoters(894);
        votingCouncel034B206.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B206);

        VotingCouncelEntity votingCouncel034B207 = new VotingCouncelEntity();
        votingCouncel034B207.setCode("034Б207");
        votingCouncel034B207.setName("СТАРЧЕВИЦА 15");
        votingCouncel034B207.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 14");
        votingCouncel034B207.setNumberOfMembers(4);
        votingCouncel034B207.setNumberOfVoters(787);
        votingCouncel034B207.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B207);

        VotingCouncelEntity votingCouncel034B208 = new VotingCouncelEntity();
        votingCouncel034B208.setCode("034Б208");
        votingCouncel034B208.setName("СТАРЧЕВИЦА 16");
        votingCouncel034B208.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 15");
        votingCouncel034B208.setNumberOfMembers(4);
        votingCouncel034B208.setNumberOfVoters(817);
        votingCouncel034B208.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B208);

        VotingCouncelEntity votingCouncel034B209 = new VotingCouncelEntity();
        votingCouncel034B209.setCode("034Б209");
        votingCouncel034B209.setName("СТАРЧЕВИЦА 17");
        votingCouncel034B209.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 16");
        votingCouncel034B209.setNumberOfMembers(4);
        votingCouncel034B209.setNumberOfVoters(748);
        votingCouncel034B209.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B209);

        VotingCouncelEntity votingCouncel034B210 = new VotingCouncelEntity();
        votingCouncel034B210.setCode("034Б210");
        votingCouncel034B210.setName("СТАРЧЕВИЦА 18");
        votingCouncel034B210.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 17");
        votingCouncel034B210.setNumberOfMembers(4);
        votingCouncel034B210.setNumberOfVoters(856);
        votingCouncel034B210.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B210);

        VotingCouncelEntity votingCouncel034B211 = new VotingCouncelEntity();
        votingCouncel034B211.setCode("034Б211");
        votingCouncel034B211.setName("СТАРЧЕВИЦА 19");
        votingCouncel034B211.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 18");
        votingCouncel034B211.setNumberOfMembers(4);
        votingCouncel034B211.setNumberOfVoters(883);
        votingCouncel034B211.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B211);

        VotingCouncelEntity votingCouncel034B212 = new VotingCouncelEntity();
        votingCouncel034B212.setCode("034Б212");
        votingCouncel034B212.setName("СТАРЧЕВИЦА 20");
        votingCouncel034B212.setLocation("ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 19");
        votingCouncel034B212.setNumberOfMembers(4);
        votingCouncel034B212.setNumberOfVoters(979);
        votingCouncel034B212.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B212);

        VotingCouncelEntity votingCouncel034B213 = new VotingCouncelEntity();
        votingCouncel034B213.setCode("034Б213");
        votingCouncel034B213.setName("СТРАТИНСКА");
        votingCouncel034B213.setLocation("ПШ \"МЛАДЕН СТОЈАНОВИЋ\", Стратинска, уч. 1");
        votingCouncel034B213.setNumberOfMembers(2);
        votingCouncel034B213.setNumberOfVoters(221);
        votingCouncel034B213.setMentor(graba);
        votingCouncelRepository.save(votingCouncel034B213);

        VotingCouncelEntity votingCouncel034B214 = new VotingCouncelEntity();
        votingCouncel034B214.setCode("034Б214");
        votingCouncel034B214.setName("СТРИЧИЋИ");
        votingCouncel034B214.setLocation("МЗ СТРИЧИЋИ, сала 1");
        votingCouncel034B214.setNumberOfMembers(4);
        votingCouncel034B214.setNumberOfVoters(576);
        votingCouncel034B214.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B214);

        VotingCouncelEntity votingCouncel034B215 = new VotingCouncelEntity();
        votingCouncel034B215.setCode("034Б215");
        votingCouncel034B215.setName("САРАЧИЦА - 2 /ПАВЛОВАЦ 2");
        votingCouncel034B215.setLocation("ПШ \"СВЕТИ САВА\", Павловац, уч. 2");
        votingCouncel034B215.setNumberOfMembers(4);
        votingCouncel034B215.setNumberOfVoters(976);
        votingCouncel034B215.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B215);

        VotingCouncelEntity votingCouncel034B216 = new VotingCouncelEntity();
        votingCouncel034B216.setCode("034Б216");
        votingCouncel034B216.setName("ЦЕНТАР I / 1");
        votingCouncel034B216.setLocation("ШКОЛА УЧЕНИКА У ПРИВРЕДИ, Николе Пашића 11а, уч. 1");
        votingCouncel034B216.setNumberOfMembers(4);
        votingCouncel034B216.setNumberOfVoters(745);
        votingCouncel034B216.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B216);

        VotingCouncelEntity votingCouncel034B217 = new VotingCouncelEntity();
        votingCouncel034B217.setCode("034Б217");
        votingCouncel034B217.setName("ЦЕНТАР I / 2");
        votingCouncel034B217.setLocation("МЗ ЦЕНТАР I, Симе Шолаје 7, сала 1");
        votingCouncel034B217.setNumberOfMembers(4);
        votingCouncel034B217.setNumberOfVoters(693);
        votingCouncel034B217.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B217);

        VotingCouncelEntity votingCouncel034B218 = new VotingCouncelEntity();
        votingCouncel034B218.setCode("034Б218");
        votingCouncel034B218.setName("ЦЕНТАР I / 3");
        votingCouncel034B218.setLocation("ШКОЛА УЧЕНИКА У ПРИВРЕДИ, Николе Пашића 11а, уч. 2");
        votingCouncel034B218.setNumberOfMembers(4);
        votingCouncel034B218.setNumberOfVoters(754);
        votingCouncel034B218.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B218);

        VotingCouncelEntity votingCouncel034B219 = new VotingCouncelEntity();
        votingCouncel034B219.setCode("034Б219");
        votingCouncel034B219.setName("ЦЕНТАР I / 4");
        votingCouncel034B219.setLocation("ШКОЛА УЧЕНИКА У ПРИВРЕДИ, Николе Пашића 11а, уч. 3");
        votingCouncel034B219.setNumberOfMembers(4);
        votingCouncel034B219.setNumberOfVoters(737);
        votingCouncel034B219.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B219);

        VotingCouncelEntity votingCouncel034B220 = new VotingCouncelEntity();
        votingCouncel034B220.setCode("034Б220");
        votingCouncel034B220.setName("ЦЕНТАР I / 5");
        votingCouncel034B220.setLocation("ЕКОНОМСКА ШКОЛА, Краља Алфонса XIII 34, уч. 1");
        votingCouncel034B220.setNumberOfMembers(4);
        votingCouncel034B220.setNumberOfVoters(754);
        votingCouncel034B220.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B220);

        VotingCouncelEntity votingCouncel034B221 = new VotingCouncelEntity();
        votingCouncel034B221.setCode("034Б221");
        votingCouncel034B221.setName("ЦЕНТАР I / 6");
        votingCouncel034B221.setLocation("ЕКОНОМСКА ШКОЛА, Краља Алфонса XIII 34, уч. 2");
        votingCouncel034B221.setNumberOfMembers(4);
        votingCouncel034B221.setNumberOfVoters(816);
        votingCouncel034B221.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B221);

        VotingCouncelEntity votingCouncel034B222 = new VotingCouncelEntity();
        votingCouncel034B222.setCode("034Б222");
        votingCouncel034B222.setName("ЦЕНТАР I / 7");
        votingCouncel034B222.setLocation("ЕКОНОМСКА ШКОЛА, Краља Алфонса XIII 34, уч. 3");
        votingCouncel034B222.setNumberOfMembers(4);
        votingCouncel034B222.setNumberOfVoters(702);
        votingCouncel034B222.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B222);

        VotingCouncelEntity votingCouncel034B223 = new VotingCouncelEntity();
        votingCouncel034B223.setCode("034Б223");
        votingCouncel034B223.setName("ЦЕНТАР I / 8");
        votingCouncel034B223.setLocation("ЕКОНОМСКА ШКОЛА, Краља Алфонса XIII 34, уч. 4");
        votingCouncel034B223.setNumberOfMembers(4);
        votingCouncel034B223.setNumberOfVoters(844);
        votingCouncel034B223.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B223);

        VotingCouncelEntity votingCouncel034B224 = new VotingCouncelEntity();
        votingCouncel034B224.setCode("034Б224");
        votingCouncel034B224.setName("ЦЕНТАР II / 1");
        votingCouncel034B224.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, фискултурна сала 1");
        votingCouncel034B224.setNumberOfMembers(4);
        votingCouncel034B224.setNumberOfVoters(777);
        votingCouncel034B224.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B224);

        VotingCouncelEntity votingCouncel034B225 = new VotingCouncelEntity();
        votingCouncel034B225.setCode("034Б225");
        votingCouncel034B225.setName("ЦЕНТАР II / 2");
        votingCouncel034B225.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 1");
        votingCouncel034B225.setNumberOfMembers(4);
        votingCouncel034B225.setNumberOfVoters(864);
        votingCouncel034B225.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B225);

        VotingCouncelEntity votingCouncel034B226 = new VotingCouncelEntity();
        votingCouncel034B226.setCode("034Б226");
        votingCouncel034B226.setName("ЦЕНТАР II / 3");
        votingCouncel034B226.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 2");
        votingCouncel034B226.setNumberOfMembers(4);
        votingCouncel034B226.setNumberOfVoters(767);
        votingCouncel034B226.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B226);

        VotingCouncelEntity votingCouncel034B227 = new VotingCouncelEntity();
        votingCouncel034B227.setCode("034Б227");
        votingCouncel034B227.setName("ЦЕНТАР II / 4");
        votingCouncel034B227.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 3");
        votingCouncel034B227.setNumberOfMembers(4);
        votingCouncel034B227.setNumberOfVoters(850);
        votingCouncel034B227.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B227);

        VotingCouncelEntity votingCouncel034B228 = new VotingCouncelEntity();
        votingCouncel034B228.setCode("034Б228");
        votingCouncel034B228.setName("ЦЕНТАР II / 5");
        votingCouncel034B228.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 4");
        votingCouncel034B228.setNumberOfMembers(4);
        votingCouncel034B228.setNumberOfVoters(855);
        votingCouncel034B228.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B228);

        VotingCouncelEntity votingCouncel034B229 = new VotingCouncelEntity();
        votingCouncel034B229.setCode("034Б229");
        votingCouncel034B229.setName("ЦЕНТАР II / 6");
        votingCouncel034B229.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 5");
        votingCouncel034B229.setNumberOfMembers(4);
        votingCouncel034B229.setNumberOfVoters(853);
        votingCouncel034B229.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B229);

        VotingCouncelEntity votingCouncel034B230 = new VotingCouncelEntity();
        votingCouncel034B230.setCode("034Б230");
        votingCouncel034B230.setName("ЦЕНТАР II / 7");
        votingCouncel034B230.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 6");
        votingCouncel034B230.setNumberOfMembers(4);
        votingCouncel034B230.setNumberOfVoters(897);
        votingCouncel034B230.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B230);

        VotingCouncelEntity votingCouncel034B231 = new VotingCouncelEntity();
        votingCouncel034B231.setCode("034Б231");
        votingCouncel034B231.setName("ЦЕНТАР II / 8");
        votingCouncel034B231.setLocation("ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 7");
        votingCouncel034B231.setNumberOfMembers(4);
        votingCouncel034B231.setNumberOfVoters(799);
        votingCouncel034B231.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B231);

        VotingCouncelEntity votingCouncel034B232 = new VotingCouncelEntity();
        votingCouncel034B232.setCode("034Б232");
        votingCouncel034B232.setName("ЧЕСМА 1");
        votingCouncel034B232.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Петра Великог 32, уч. 1");
        votingCouncel034B232.setNumberOfMembers(4);
        votingCouncel034B232.setNumberOfVoters(752);
        votingCouncel034B232.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B232);

        VotingCouncelEntity votingCouncel034B233 = new VotingCouncelEntity();
        votingCouncel034B233.setCode("034Б233");
        votingCouncel034B233.setName("ЧЕСМА 2");
        votingCouncel034B233.setLocation("МЗ ЧЕСМА, Петра Великог 30, сала 1");
        votingCouncel034B233.setNumberOfMembers(4);
        votingCouncel034B233.setNumberOfVoters(860);
        votingCouncel034B233.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B233);

        VotingCouncelEntity votingCouncel034B234 = new VotingCouncelEntity();
        votingCouncel034B234.setCode("034Б234");
        votingCouncel034B234.setName("ЧЕСМА 3");
        votingCouncel034B234.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Петра Великог 32, уч. 2");
        votingCouncel034B234.setNumberOfMembers(4);
        votingCouncel034B234.setNumberOfVoters(761);
        votingCouncel034B234.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B234);

        VotingCouncelEntity votingCouncel034B235 = new VotingCouncelEntity();
        votingCouncel034B235.setCode("034Б235");
        votingCouncel034B235.setName("ЧЕСМА 4");
        votingCouncel034B235.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Петра Великог 32, уч. 3");
        votingCouncel034B235.setNumberOfMembers(4);
        votingCouncel034B235.setNumberOfVoters(671);
        votingCouncel034B235.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B235);

        VotingCouncelEntity votingCouncel034B236 = new VotingCouncelEntity();
        votingCouncel034B236.setCode("034Б236");
        votingCouncel034B236.setName("ЧОКОРСКА ПОЉА");
        votingCouncel034B236.setLocation("Коњички клуб, објекат \"Салаш\", сала 1");
        votingCouncel034B236.setNumberOfMembers(2);
        votingCouncel034B236.setNumberOfVoters(312);
        votingCouncel034B236.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B236);

        VotingCouncelEntity votingCouncel034B237 = new VotingCouncelEntity();
        votingCouncel034B237.setCode("034Б237");
        votingCouncel034B237.setName("ШАРГОВАЦ 1");
        votingCouncel034B237.setLocation("ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 1");
        votingCouncel034B237.setNumberOfMembers(4);
        votingCouncel034B237.setNumberOfVoters(717);
        votingCouncel034B237.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B237);

        VotingCouncelEntity votingCouncel034B238 = new VotingCouncelEntity();
        votingCouncel034B238.setCode("034Б238");
        votingCouncel034B238.setName("ШАРГОВАЦ 2");
        votingCouncel034B238.setLocation("ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 2");
        votingCouncel034B238.setNumberOfMembers(4);
        votingCouncel034B238.setNumberOfVoters(677);
        votingCouncel034B238.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B238);

        VotingCouncelEntity votingCouncel034B239 = new VotingCouncelEntity();
        votingCouncel034B239.setCode("034Б239");
        votingCouncel034B239.setName("ШАРГОВАЦ 3");
        votingCouncel034B239.setLocation("ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 3");
        votingCouncel034B239.setNumberOfMembers(4);
        votingCouncel034B239.setNumberOfVoters(840);
        votingCouncel034B239.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B239);

        VotingCouncelEntity votingCouncel034B240 = new VotingCouncelEntity();
        votingCouncel034B240.setCode("034Б240");
        votingCouncel034B240.setName("ШАРГОВАЦ 4");
        votingCouncel034B240.setLocation("ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 4");
        votingCouncel034B240.setNumberOfMembers(4);
        votingCouncel034B240.setNumberOfVoters(775);
        votingCouncel034B240.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B240);

        VotingCouncelEntity votingCouncel034B241 = new VotingCouncelEntity();
        votingCouncel034B241.setCode("034Б241");
        votingCouncel034B241.setName("ШИМИЋИ");
        votingCouncel034B241.setLocation("МЗ Шимићи, сала 1");
        votingCouncel034B241.setNumberOfMembers(2);
        votingCouncel034B241.setNumberOfVoters(192);
        votingCouncel034B241.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B241);

        VotingCouncelEntity votingCouncel034B242 = new VotingCouncelEntity();
        votingCouncel034B242.setCode("034Б242");
        votingCouncel034B242.setName("АДА-7");
        votingCouncel034B242.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч. 7");
        votingCouncel034B242.setNumberOfMembers(4);
        votingCouncel034B242.setNumberOfVoters(883);
        votingCouncel034B242.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B242);

        VotingCouncelEntity votingCouncel034B243 = new VotingCouncelEntity();
        votingCouncel034B243.setCode("034Б243");
        votingCouncel034B243.setName("АДА -8");
        votingCouncel034B243.setLocation("ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч. 8");
        votingCouncel034B243.setNumberOfMembers(4);
        votingCouncel034B243.setNumberOfVoters(917);
        votingCouncel034B243.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B243);

        VotingCouncelEntity votingCouncel034B244 = new VotingCouncelEntity();
        votingCouncel034B244.setCode("034Б244");
        votingCouncel034B244.setName("КУЉАНИ - 2");
        votingCouncel034B244.setLocation("ПШ \"ЈОВАН ДУЧИЋ\" (нови објекат), Куљани, уч. 2");
        votingCouncel034B244.setNumberOfMembers(4);
        votingCouncel034B244.setNumberOfVoters(841);
        votingCouncel034B244.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B244);

        VotingCouncelEntity votingCouncel034B245 = new VotingCouncelEntity();
        votingCouncel034B245.setCode("034Б245");
        votingCouncel034B245.setName("КУЉАНИ - 3");
        votingCouncel034B245.setLocation("ПШ \"ЈОВАН ДУЧИЋ\" (нови објекат), Куљани, уч. 3");
        votingCouncel034B245.setNumberOfMembers(4);
        votingCouncel034B245.setNumberOfVoters(639);
        votingCouncel034B245.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B245);

        VotingCouncelEntity votingCouncel034B246 = new VotingCouncelEntity();
        votingCouncel034B246.setCode("034Б246");
        votingCouncel034B246.setName("КУЉАНИ - 4");
        votingCouncel034B246.setLocation("ПШ \"ЈОВАН ДУЧИЋ\" (нови објекат), Куљани, уч. 4");
        votingCouncel034B246.setNumberOfMembers(4);
        votingCouncel034B246.setNumberOfVoters(810);
        votingCouncel034B246.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B246);

        VotingCouncelEntity votingCouncel034B247 = new VotingCouncelEntity();
        votingCouncel034B247.setCode("034Б247");
        votingCouncel034B247.setName("КУЉАНИ - 5");
        votingCouncel034B247.setLocation("ПШ \"ЈОВАН ДУЧИЋ\" (нови објекат), Куљани, уч. 5");
        votingCouncel034B247.setNumberOfMembers(4);
        votingCouncel034B247.setNumberOfVoters(852);
        votingCouncel034B247.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B247);

        VotingCouncelEntity votingCouncel034B248 = new VotingCouncelEntity();
        votingCouncel034B248.setCode("034Б248");
        votingCouncel034B248.setName("ПРИЈЕЧАНИ 2");
        votingCouncel034B248.setLocation("ПШ \"ЈОВАН ДУЧИЋ\", Пријечани, уч. 2");
        votingCouncel034B248.setNumberOfMembers(4);
        votingCouncel034B248.setNumberOfVoters(681);
        votingCouncel034B248.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B248);

        VotingCouncelEntity votingCouncel034B249 = new VotingCouncelEntity();
        votingCouncel034B249.setCode("034Б249");
        votingCouncel034B249.setName("ПРИЈЕЧАНИ - 3");
        votingCouncel034B249.setLocation("ПШ \"ЈОВАН ДУЧИЋ\", Пријечани, уч. 3");
        votingCouncel034B249.setNumberOfMembers(4);
        votingCouncel034B249.setNumberOfVoters(661);
        votingCouncel034B249.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B249);

        VotingCouncelEntity votingCouncel034B250 = new VotingCouncelEntity();
        votingCouncel034B250.setCode("034Б250");
        votingCouncel034B250.setName("КОЛА - 2");
        votingCouncel034B250.setLocation("ОШ \"ПЕТАР КОЧИЋ\", Kола, уч. 2");
        votingCouncel034B250.setNumberOfMembers(4);
        votingCouncel034B250.setNumberOfVoters(745);
        votingCouncel034B250.setMentor(igor);
        votingCouncelRepository.save(votingCouncel034B250);

        VotingCouncelEntity votingCouncel034B251 = new VotingCouncelEntity();
        votingCouncel034B251.setCode("034Б251");
        votingCouncel034B251.setName("ШАРГОВАЦ 5");
        votingCouncel034B251.setLocation("ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 5");
        votingCouncel034B251.setNumberOfMembers(4);
        votingCouncel034B251.setNumberOfVoters(779);
        votingCouncel034B251.setMentor(dusko);
        votingCouncelRepository.save(votingCouncel034B251);

        VotingCouncelEntity votingCouncel034B252 = new VotingCouncelEntity();
        votingCouncel034B252.setCode("034Б252");
        votingCouncel034B252.setName("МИШИН ХАН - 2");
        votingCouncel034B252.setLocation("ПШ \"МИЛУТИН БОЈИЋ\", Мишин Хан, уч. 2");
        votingCouncel034B252.setNumberOfMembers(4);
        votingCouncel034B252.setNumberOfVoters(821);
        votingCouncel034B252.setMentor(dubravko);
        votingCouncelRepository.save(votingCouncel034B252);

        VotingCouncelEntity votingCouncel034B253 = new VotingCouncelEntity();
        votingCouncel034B253.setCode("034Б253");
        votingCouncel034B253.setName("ДЕБЕЉАЦИ - 3");
        votingCouncel034B253.setLocation("ПШ \"СТАНКО РАКИТА\", Тешана Подруговића бб, уч. 3");
        votingCouncel034B253.setNumberOfMembers(4);
        votingCouncel034B253.setNumberOfVoters(663);
        votingCouncel034B253.setMentor(dino);
        votingCouncelRepository.save(votingCouncel034B253);

        VotingCouncelEntity votingCouncel034B254 = new VotingCouncelEntity();
        votingCouncel034B254.setCode("034Б254");
        votingCouncel034B254.setName("КАРАНОВАЦ - 3");
        votingCouncel034B254.setLocation("ОШ \"МИЛАН РАКИЋ\", Карановац, уч. 3");
        votingCouncel034B254.setNumberOfMembers(4);
        votingCouncel034B254.setNumberOfVoters(521);
        votingCouncel034B254.setMentor(bojana);
        votingCouncelRepository.save(votingCouncel034B254);

        VotingCouncelEntity votingCouncel034B501_NNN = new VotingCouncelEntity();
        votingCouncel034B501_NNN.setCode("034Б501/ННН");
        votingCouncel034B501_NNN.setName("ОДСУСТВО + НЕПОТВРЂЕНИ");
        votingCouncel034B501_NNN.setLocation("Раднички универзитет, Грчка 4, сала 1");
        votingCouncel034B501_NNN.setNumberOfMembers(2);
        votingCouncel034B501_NNN.setNumberOfVoters(209);
        votingCouncel034B501_NNN.setMentor(nada);
        votingCouncelRepository.save(votingCouncel034B501_NNN);

        //Пoвeзивaњe придружeних бирaчких мjeстa (mentori.xlsx: "034B224/000" - ЦEНTAР 2-1 / ЛИЧНO)
        votingCouncel034B000.setPrimaryVotingCouncel(votingCouncel034B224);
        votingCouncelRepository.save(votingCouncel034B000);
    }
}
