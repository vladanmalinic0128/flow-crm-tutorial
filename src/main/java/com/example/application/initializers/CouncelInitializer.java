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
        MentorEntity mentor = new MentorEntity();

        // Set properties
        mentor.setFirstname("John");
        mentor.setLastname("Doe");
        mentor.setEmail("john.doe@example.com");

        // If needed, initialize the list of VotingCouncelEntity objects
        mentor.setVotingCouncels(new ArrayList<VotingCouncelEntity>());
        mentorRepository.save(mentor);

        TitleEntity clanTitle = new TitleEntity();
        clanTitle.setName("ČLAN");
        titleRepository.save(clanTitle);

        // Create and save "ZAMJENIK CLANA" TitleEntity
        TitleEntity zamjenikClanaTitle = new TitleEntity();
        zamjenikClanaTitle.setName("ZAMJENIK ČLANA");
        titleRepository.save(zamjenikClanaTitle);

        String[][] array = {
                {"034Б001", "АГИНО СЕЛО", "ПШ \"ВОЈИСЛАВ ИЛИЋ\", Агино Село бб, уч. 1", "5+5"},
                {"034Б002", "АДА - 1", "ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб,уч 1.", "5+5"},
                {"034Б003", "АДА - 2", "ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб,уч 2.", "5+5"},
                {"034Б004", "АДА - 3", "ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч 3.", "5+5"},
                {"034Б005", "АДА - 4", "ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч 4.", "5+5"},
                {"034Б006", "АДА - 5", "ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч 5.", "5+5"},
                {"034Б007", "АДА - 6", "ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч 6.", "5+5"},
                {"034Б008", "БИСТРИЦА", "ОШ \"МИРОСЛАВ АНТИЋ\", Бистрица, уч.1", "5+5"},
                {"034Б009", "БОРИК I /1", "ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч.1", "5+5"},
                {"034Б010", "БОРИК I /2", "ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 2", "5+5"},
                {"034Б011", "БОРИК I /3", "ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 3", "5+5"},
                {"034Б012", "БОРИК I /4", "ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 4", "5+5"},
                {"034Б013", "БОРИК I /5", "ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 5", "5+5"},
                {"034Б014", "БОРИК I /6", "ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 6", "5+5"},
                {"034Б015", "БОРИК I /7", "ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 7", "5+5"},
                {"034Б016", "БОРИК I /8", "ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 8", "5+5"},
                {"034Б017", "БОРИК I /9", "ОШ \"ВУК С. КАРАЏИЋ\", Саве Ковачевића бб, уч. 9", "5+5"},
                {"034Б018", "БОРИК II/1", "ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 1", "5+5"},
                {"034Б019", "БОРИК II/2", "ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 2", "5+5"},
                {"034Б020", "БОРИК II/3", "ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 3", "5+5"},
                {"034Б021", "БОРИК II/4", "ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 4", "5+5"},
                {"034Б022", "БОРИК II/5", "ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 5", "5+5"},
                {"034Б023", "БОРИК II/6", "ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 6", "5+5"},
                {"034Б024", "БОРИК II/7", "ОШ \"БРАНКО ЋОПИЋ\", Мише Ступара 24, уч. 7", "5+5"},
                {"034Б025", "БОРКОВИЋИ - 1", "МЗ БОРКОВИЋИ, сала 1", "5+5"},
                {"034Б026", "БОРКОВИЋИ - 2 /СЛАВИЋКА", "Продавница \"ПЛАЗМА\" Славићка", "5+5"},
                {"034Б027 A", "БОЧАЦ", "ПШ \"ВОЈИСЛАВ ИЛИЋ\", Бочац, уч. 1", "5+5"},
                {"034Б027 B", "БОЧАЦ", "ПШ \"ВОЈИСЛАВ ИЛИЋ\", Бочац, уч. 2", "5+5"},
                {"034Б028", "БРОНЗАНИ МАЈДАН - 1", "МЗ БРОНЗАНИ МАЈДАН, сала 1", "5+5"},
                {"034Б029", "БРОНЗАНИ МАЈДАН - 2 /МЕЛИНА", "ПШ \"МЛАДЕН СТОЈАНОВИЋ\" Мелина, уч. 1", "5+5"},
                {"034Б030", "БРОНЗАНИ МАЈДАН - 3/ОБРОВАЦ", "ПШ „МЛАДЕН СТОЈАНОВИЋ“ Обровац, уч.1.", "5+5"},
                {"034Б031", "БУЛЕВАР - 1", "ГИМНАЗИЈА, Змај Јовина 13, уч. 1", "5+5"},
                {"034Б032", "БУЛЕВАР - 2", "ГИМНАЗИЈА, Змај Јовина 13, уч. 2", "5+5"},
                {"034Б033", "БУЛЕВАР - 3", "МЗ БУЛЕВАР, Симеуна Ђака 15, сала 1", "5+5"},
                {"034Б034", "БУЛЕВАР - 4", "ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 1", "5+5"},
                {"034Б035", "БУЛЕВАР - 5", "ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 2", "5+5"},
                {"034Б036", "БУЛЕВАР - 6", "ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 3", "5+5"},
                {"034Б037", "БУЛЕВАР - 7", "ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 4", "5+5"},
                {"034Б038", "ВЕРИЋИ - 1", "ПШ „МИЛУТИН БОЈИЋ“, Верићи, уч.1.", "5+5"},
                {"034Б039", "ВЕРИЋИ - 2", "ПШ \"МИЛУТИН БОЈИЋ\", Верићи, уч. 2", "5+5"},
                {"034Б040", "ВРБАЊА - 1", "МЗ ВРБАЊА, Станка Божића Кобре бб, сала 1", "5+5"},
                {"034Б041", "ВРБАЊА - 2", "ОШ \"СТАНКО РАКИТА\", Јове Г. Поповића 9, уч. 1", "5+5"},
                {"034Б042", "ВРБАЊА - 3", "ОШ \"СТАНКО РАКИТА\", Јове Г. Поповића 9, уч. 2", "5+5"},
                {"034Б043", "ВРБАЊА - 4", "ОШ \"СТАНКО РАКИТА\", Јове Г. Поповића 9, уч. 3", "5+5"},
                {"034Б044", "ГОЛЕШИ - 1 /ДОЊИ ПЕРВАН", "ПШ \"МИРОСЛАВ АНТИЋ\", Доњи Перван, уч. 1", "5+5"},
                {"034Б045", "ВРБАЊА - 5", "ОШ \"СТАНКО РАКИТА\", Јове Г. Поповића 9, уч. 4.", "5+5"},
                {"034Б046", "ГОРЊА ПИСКАВИЦА", "ПШ \"ЋИРИЛО И МЕТОДИЈЕ\", Горња Пискавица, уч. 1", "5+5"},
                {"034Б047", "ДЕБЕЉАЦИ - 1", "ПШ \"СТАНКО РАКИТА\", Тешана Подруговића бб, уч. 1", "5+5"},
                {"034Б048", "ДЕБЕЉАЦИ - 2", "ПШ \"СТАНКО РАКИТА\", Тешана Подруговића бб, уч. 2", "5+5"},
                {"034Б049", "ДОЊА КОЛА - 1", "МЗ Доња Кола, сала 1", "5+5"},
                {"034Б050", "ДОЊА КОЛА - 2", "МЗ Доња Кола, сала 2", "5+5"},
                {"034Б051 A", "ДРАГОЧАЈ – 1", "ОШ \"ДЕСАНКА МАКСИМОВИЋ\", Драгочај, уч. 1", "5+5"},
                {"034Б051 Б", "ДРАГОЧАЈ – 1", "ОШ \"ДЕСАНКА МАКСИМОВИЋ\", Драгочај, уч. 2", "5+5"},
                {"034Б051 Ц", "ДРАГОЧАЈ – 1", "ОШ \"ДЕСАНКА МАКСИМОВИЋ\", Драгочај, уч. 3", "5+5"},
                {"034Б052 А", "ДРАГОЧАЈ - 2 / РАМИЋИ", "Друштвене просторије Рамићи, сала 1", "5+5"},
                {"034Б052 Б", "ДРАГОЧАЈ - 2 / РАМИЋИ", "Друштвене просторије Рамићи, сала 2", "5+5"},
                {"034Б053", "ДРАГОЧАЈ - 3 / БАРЛОВЦИ", "ПШ \"ЈОВАН ДУЧИЋ\", Барловци, уч. 1", "5+5"},
                {"034Б054", "ДРАКУЛИЋ - 1", "МЗ ДРАГУЛИЋ, Ђурђа Гламочанина 1, сала 1", "5+5"},
                {"034Б055", "ДРАКУЛИЋ - 2", "ПШ \"АЛЕКСА ШАНТИЋ\", Дракулић, 7. фебруара бб, уч. 1", "5+5"},
                {"034Б056", "ДРАКУЛИЋ - 3", "КИНОЛОШКИ САВЕЗ БАЊА ЛУКА, Битољска бб, сала 1", "5+5"},
                {"034Б057", "ДРАКУЛИЋ - 4", "ПШ \"АЛЕКСА ШАНТИЋ\", Дракулић, 7. фебруара бб, уч. 2.", "5+5"},
                {"034Б058", "ДРАКУЛИЋ - 5", "КИНОЛОШКИ САВЕЗ БАЊА ЛУКА, Битољска бб, сала 2", "5+5"},
                {"034Б059", "ДРАКУЛИЋ - 6", "КИНОЛОШКИ САВЕЗ БАЊА ЛУКА, Битољска бб, сала 3", "5+5"},
                {"034Б060", "ЗАЛУЖАНИ - 1", "ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 1", "5+5"},
                {"034Б061", "ЗАЛУЖАНИ - 2", "ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 2", "5+5"},
                {"034Б062", "ЗАЛУЖАНИ - 3", "ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 3", "5+5"},
                {"034Б063", "ЗАЛУЖАНИ - 4", "ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 4", "5+5"},
                {"034Б064", "ЗАЛУЖАНИ - 5", "ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 5", "5+5"},
                {"034Б065", "ЗАЛУЖАНИ - 6", "ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 6", "5+5"},
                {"034Б066", "ЗАЛУЖАНИ - 7", "ОШ \"ЈОВАН ДУЧИЋ\", Ненада Костића 7, уч. 7", "5+5"},
                {"034Б067", "КАРАНОВАЦ – 1", "ОШ \"МИЛАН РАКИЋ\", Карановац, уч. 1", "5+5"},
                {"034Б068", "КАРАНОВАЦ - 2", "ОШ \"МИЛАН РАКИЋ\", Карановац, уч. 2", "5+5"},
                {"034Б069", "КМЕЋАНИ", "ПШ \"МЛАДЕН СТОЈАНОВИЋ\", Кмећани, уч. 1", "3+3"},
                {"034Б070", "КОЛА - 1", "ОШ \"ПЕТАР КОЧИЋ\", Кола, уч. 1", "5+5"},
                {"034Б071", "КОЧИЋЕВ ВИЈЕНАЦ - 1", "ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 1", "5+5"},
                {"034Б072", "КОЧИЋЕВ ВИЈЕНАЦ - 2", "ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 2", "5+5"},
                {"034Б073", "КОЧИЋЕВ ВИЈЕНАЦ - 3", "ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 3", "5+5"},
                {"034Б074", "КОЧИЋЕВ ВИЈЕНАЦ - 4", "ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 4", "5+5"},
                {"034Б075", "КОЧИЋЕВ ВИЈЕНАЦ - 5", "ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 5", "5+5"},
                {"034Б076", "КОЧИЋЕВ ВИЈЕНАЦ - 6", "ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 6", "5+5"},
                {"034Б077", "КОЧИЋЕВ ВИЈЕНАЦ - 7", "ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 7", "5+5"},
                {"034Б078", "КОЧИЋЕВ ВИЈЕНАЦ - 8", "ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 8", "5+5"},
                {"034Б079", "КОЧИЋЕВ ВИЈЕНАЦ - 9", "ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 9", "5+5"},
                {"034Б080", "КОЧИЋЕВ ВИЈЕНАЦ - 10", "ОШ \"ЗМАЈ Ј. ЈОВАНОВИЋ\", Бранка Загорца 1, уч. 10", "5+5"},
                {"034Б081", "КРМИНЕ", "ПШ \"ВОЈИСЛАВ ИЛИЋ\", Крмине, уч. 1", "5+5"},
                {"034Б082", "КРУПА НА ВРБАСУ – 1", "ОШ \"ВОЈИСЛАВ ИЛИЋ\", Крупа на Врбасу, уч. 1", "5+5"},
                {"034Б083", "КРУПА НА ВРБАСУ - 2 / ЛЕДЕНИЦЕ", "ПШ \"ВОЈИСЛАВ ИЛИЋ\", Леденице, уч. 1", "5+5"},
                {"034Б084", "КУЉАНИ - 1", "ПШ \"ЈОВАН ДУЧИЋ\" (НОВИ ОБЈЕКАТ), Куљани, уч. 1", "5+5"},
                {"034Б085", "ЛАЗАРЕВО I / 1", "ПОЉОПОРИВРЕДНА ШКОЛА, Књаза Милоша 9, уч. 1", "5+5"},
                {"034Б086", "ЛАЗАРЕВО I / 2", "ПОЉОПОРИВРЕДНА ШКОЛА, Књаза Милоша 9, уч. 2", "5+5"},
                {"034Б087", "ЛАЗАРЕВО I / 3", "ПОЉОПОРИВРЕДНА ШКОЛА, Књаза Милоша 9, уч. 3", "5+5"},
                {"034Б088", "ЛАЗАРЕВО I / 4", "ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 1", "5+5"},
                {"034Б089", "ЛАЗАРЕВО I / 5", "ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч.1", "5+5"},
                {"034Б090", "ЛАЗАРЕВО I / 6", "ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 2", "5+5"},
                {"034Б091", "ЛАЗАРЕВО I / 7", "ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 3", "5+5"},
                {"034Б092", "ЛАЗАРЕВО I / 8", "ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 4", "5+5"},
                {"034Б093", "ЛАЗАРЕВО I / 9", "ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 5", "5+5"},
                {"034Б094", "ЛАЗАРЕВО I / 10", "ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 6", "5+5"},
                {"034Б095", "ЛАЗАРЕВО II / 1", "ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 7", "5+5"},
                {"034Б096", "ЛАЗАРЕВО II / 2", "ОШ \"БОРИСАВ СТАНКОВИЋ\", Ивана Косанчића 2, уч. 8", "5+5"},
                {"034Б097", "ЛАЗАРЕВО II / 3", "ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 2", "5+5"},
                {"034Б098", "ЛАЗАРЕВО II / 4", "ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 3", "5+5"},
                {"034Б099", "ЛАЗАРЕВО II / 5", "ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 4", "5+5"},
                {"034Б100", "ЛАЗАРЕВО II / 6", "ОШ \"ИВАН Г. КОВАЧИЋ\", Марка Липовца 1, уч. 5", "5+5"},
                {"034Б101", "ЛАУШ I / 1", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 1", "5+5"},
                {"034Б102", "ЛАУШ I / 2", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 2", "5+5"},
                {"034Б103", "ЛАУШ I / 3", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 3", "5+5"},
                {"034Б104", "ЛАУШ I / 4", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 4", "5+5"},
                {"034Б105", "ЛАУШ I / 5", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 5", "5+5"},
                {"034Б106", "ЛАУШ I / 6", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 6", "5+5"},
                {"034Б107", "ЛАУШ I / 7", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 7", "5+5"},
                {"034Б108", "ЛАУШ I / 8", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 8", "5+5"},
                {"034Б109", "ЛАУШ I / 9", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 9", "5+5"},
                {"034Б110", "ЛАУШ II /1", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 10", "5+5"},
                {"034Б111", "ЛАУШ II /2", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 11", "5+5"},
                {"034Б112", "ЛАУШ II /3", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 12", "5+5"},
                {"034Б113", "ЛАУШ II /4", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 13", "5+5"},
                {"034Б114", "ЛАУШ II /5", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 14", "5+5"},
                {"034Б115", "ЛАУШ II /6", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 15", "5+5"},
                {"034Б116", "ЛАУШ II / 7", "ОШ \"СВЕТИ САВА\", Ужичка бб, уч. 16", "5+5"},
                {"034Б117", "ЉУБАЧЕВО", "ПШ \"МИЛАН РАКИЋ\", Љубачево, уч. 1", "5+5"},
                {"034Б118", "МИШИН ХАН - 1", "ПШ \"МИЛУТИН БОЈИЋ\", Мишин Хан, уч.1", "5+5"},
                {"034Б119", "МОТИКЕ - 1", "ПШ \"МИЛОШ ЦРЊАНСКИ\", Мотике, уч. 1", "5+5"},
                {"034Б120", "МОТИКЕ - 2", "ПШ \"МИЛОШ ЦРЊАНСКИ“, Мотике, уч.2.", "5+5"},
                {"034Б121", "НОВА ВАРОШ - 1", "ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 1", "5+5"},
                {"034Б122", "НОВА ВАРОШ - 2", "ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 2", "5+5"},
                {"034Б123", "НОВА ВАРОШ - 3", "ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 3", "5+5"},
                {"034Б124", "НОВА ВАРОШ - 4", "ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 4", "5+5"},
                {"034Б125", "НОВА ВАРОШ - 5", "ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 5", "5+5"},
                {"034Б126", "ОБИЛИЋЕВО I / 1", "ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 1", "5+5"},
                {"034Б127", "ОБИЛИЋЕВО I / 2", "ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 2", "5+5"},
                {"034Б128", "ОБИЛИЋЕВО I / 3", "ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 3", "5+5"},
                {"034Б129", "ОБИЛИЋЕВО I / 4", "ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 4", "5+5"},
                {"034Б130", "ОБИЛИЋЕВО I / 5", "ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 5", "5+5"},
                {"034Б131", "ОБИЛИЋЕВО I / 6", "ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 6", "5+5"},
                {"034Б132", "ОБИЛИЋЕВО I / 7", "ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 7", "5+5"},
                {"034Б133", "ОБИЛИЋЕВО I / 8", "ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 8", "5+5"},
                {"034Б134", "ОБИЛИЋЕВО I / 9", "ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 9", "5+5"},
                {"034Б135", "ОБИЛИЋЕВО I / 10", "ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 10", "5+5"},
                {"034Б136", "ОБИЛИЋЕВО I / 11", "ОШ \"ПЕТАР П. ЊЕГОШ\", Бул. В. С. Степановића 28, уч. 11", "5+5"},
                {"034Б137", "ОБИЛИЋЕВО II / 1", "ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 1", "5+5"},
                {"034Б138", "ОБИЛИЋЕВО II / 2", "ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 2", "5+5"},
                {"034Б139", "ОБИЛИЋЕВО II / 3", "ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 3", "5+5"},
                {"034Б140", "ОБИЛИЋЕВО II / 4", "ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 4", "5+5"},
                {"034Б141", "ОБИЛИЋЕВО II / 5", "ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 5", "5+5"},
                {"034Б142", "ОБИЛИЋЕВО II / 6", "ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 6", "5+5"},
                {"034Б143", "ОБИЛИЋЕВО II / 7", "ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 7", "5+5"},
                {"034Б144", "ОБИЛИЋЕВО II / 8", "ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 8", "5+5"},
                {"034Б145", "ОБИЛИЋЕВО II / 9", "ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 9", "5+5"},
                {"034Б146", "ОБИЛИЋЕВО II / 10", "ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 10", "5+5"},
                {"034Б147", "ОБИЛИЋЕВО II / 11", "ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 11", "5+5"},
                {"034Б148", "ОБИЛИЋЕВО II / 12", "ОШ \"ДОСИТЕЈ ОБРАДОВИЋ\", Мирка Ковачевића 27, уч. 12", "5+5"},
                {"034Б149", "ПАВИЋИ", "ПШ \"ПЕТАР КОЧИЋ\", Павићи, уч. 1", "5+5"},
                {"034Б150", "РОСУЉЕ - 1", "ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 1", "5+5"},
                {"034Б151", "ПЕТРИЋЕВАЦ - 1", "ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 1", "5+5"},
                {"034Б152", "ПАПРИКОВАЦ 1", "ШКОЛА ЗА СЛУШНО ОШТЕЋЕНЕ, Др Ј. Рашковића 28, уч. 1", "5+5"},
                {"034Б153", "ПАПРИКОВАЦ 2", "ШКОЛА ЗА СЛУШНО ОШТЕЋЕНЕ, Др Ј. Рашковића 28, уч. 2", "5+5"},
                {"034Б154", "ПАПРИКОВАЦ 3", "ШКОЛА ЗА СЛУШНО ОШТЕЋЕНЕ, Др Ј. Рашковића 28, уч. 3", "5+5"},
                {"034Б155", "ПАПРИКОВАЦ 4", "ШКОЛА ЗА СЛУШНО ОШТЕЋЕНЕ, Др Ј. Рашковића 28, уч. 4", "5+5"},
                {"034Б156", "ПАПРИКОВАЦ 5", "ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 6", "5+5"},
                {"034Б157", "ПАПРИКОВАЦ 6", "ОШ \"ГЕОРГИ С. РАКОВСКИ\", Драгише Васића 19, уч. 7", "5+5"},
                {"034Б158", "ПАПРИКОВАЦ 7", "ЗАВОД ЗА ДИСТРОФИЧАРЕ, Војвођанска бб, сала 1", "5+5"},
                {"034Б159", "ПАПРИКОВАЦ 8", "ЗАВОД ЗА ДИСТРОФИЧАРЕ, Војвођанска бб, сала 2", "5+5"},
                {"034Б160", "ПЕТРИЋЕВАЦ 2", "ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 2", "5+5"},
                {"034Б161", "ПЕТРИЋЕВАЦ 3", "ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 3", "5+5"},
                {"034Б162", "ПЕТРИЋЕВАЦ 4", "ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 4", "5+5"},
                {"034Б163", "ПЕТРИЋЕВАЦ 5", "ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 5", "5+5"},
                {"034Б164", "ПЕТРИЋЕВАЦ 6", "ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 6", "5+5"},
                {"034Б165", "ПЕТРИЋЕВАЦ 7", "ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 7", "5+5"},
                {"034Б166", "ПЕТРИЋЕВАЦ 8", "ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 8", "5+5"},
                {"034Б167", "ПЕТРИЋЕВАЦ 9", "ОШ \"МИЛОШ ЦРЊАНСКИ\", Зоре Ковачевић бб, уч. 9", "5+5"},
                {"034Б168", "ПИСКАВИЦА 1", "ОШ \"ЋИРИЛО И МЕТОДИЈЕ\", Пискавица, уч. 1", "5+5"},
                {"034Б169", "ПИСКАВИЦА 2", "ОШ \"ЋИРИЛО И МЕТОДИЈЕ\", Пискавица, уч. 2", "5+5"},
                {"034Б170", "ПОБРЂЕ 1", "ОШ \"ЈОВАН ЦВИЈИЋ\", Ђуре Јакшића 12, уч. 6", "5+5"},
                {"034Б171", "ПОБРЂЕ 2", "ГИМНАЗИЈА, Змај Јовина 13, уч. 3", "5+5"},
                {"034Б172", "ПОБРЂЕ 3", "ГИМНАЗИЈА, Змај Јовина 13, уч. 4", "5+5"},
                {"034Б173 А", "ПОТКОЗАРЈЕ", "ОШ \"МИЛУТИН БОЈИЋ\", Поткозарје, уч. 1", "5+5"},
                {"034Б173 Б", "ПОТКОЗАРЈЕ", "ОШ \"МИЛУТИН БОЈИЋ\", Поткозарје, уч. 2", "5+5"},
                {"034Б174", "ПРИЈАКОВЦИ", "МЗ ПРИЈАКОВЦИ, сала 1", "5+5"},
                {"034Б175", "ПРИЈЕЧАНИ - 1", "ПШ \"ЈОВАН ДУЧИЋ\", Пријечани, уч. 1", "5+5"},
                {"034Б176", "РЕКАВИЦЕ I", "ПШ \"БРАНИСЛАВ НУШИЋ\", Рекавице I, уч. 1", "5+5"},
                {"034Б177", "РЕКАВИЦЕ II", "МЗ РЕКАВИЦЕ II, сала 1", "5+5"},
                {"034Б178", "РОСУЉЕ 2", "МЗ РОСУЉЕ, Др Младена Стојановића 10, сала 1", "5+5"},
                {"034Б179", "РОСУЉЕ 3", "МЗ РОСУЉЕ, Др Младена Стојановића 10, сала 2", "5+5"},
                {"034Б180", "РОСУЉЕ 4", "ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 2", "5+5"},
                {"034Б181", "РОСУЉЕ 5", "ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 3", "5+5"},
                {"034Б182", "РОСУЉЕ 6", "ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 4", "5+5"},
                {"034Б183", "РОСУЉЕ 7", "ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 5", "5+5"},
                {"034Б184", "РОСУЉЕ 8", "ОШ \"АЛЕКСА ШАНТИЋ\", Триве Амелице 24, уч. 6", "5+5"},
                {"034Б185", "РОСУЉЕ 9", "КИНОЛОШКИ САВЕЗ БАЊА ЛУКА, Битољска бб, сала 4", "5+5"},
                {"034Б186", "САРАЧИЦА 1", "ДРУШТВЕНИ ДОМ САРАЧИЦА, сала 1", "5+5"},
                {"034Б187", "САРАЧИЦА 2 / ПАВЛОВАЦ 1", "ПШ \"СВЕТИ САВА\", Павловац, уч. 1", "5+5"},
                {"034Б188", "СРПСКЕ ТОПЛИЦЕ 1", "ОШ \"БРАНИСЛАВ НУШИЋ\", Мањачких устаника 32, уч. 1", "5+5"},
                {"034Б189", "СРПСКЕ ТОПЛИЦЕ 2", "ОШ \"БРАНИСЛАВ НУШИЋ\", Мањачких устаника 32, уч. 2", "5+5"},
                {"034Б190", "СРПСКЕ ТОПЛИЦЕ 3", "ОШ \"БРАНИСЛАВ НУШИЋ\", Мањачких устаника 32, уч. 3", "5+5"},
                {"034Б191", "СРПСКЕ ТОПЛИЦЕ - 4 / НОВОСЕЛИЈА 1", "ПШ \"МИЛАН РАКИЋ\", Краг. краљ. жртава 59, уч. 1", "5+5"},
                {"034Б192", "СРПСКЕ ТОПЛИЦЕ - 5 / НОВОСЕЛИЈА 2", "ПШ \"МИЛАН РАКИЋ\", Краг. краљ. жртава 59, уч. 2", "5+5"},
                {"034Б193", "СТАРЧЕВИЦА 1", "МЗ СТРАЧЕВИЦА, Др В. Ђ. Кецмановића 1, сала 1", "5+5"},
                {"034Б194", "СТАРЧЕВИЦА 2", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 1", "5+5"},
                {"034Б195", "СТАРЧЕВИЦА 3", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 2", "5+5"},
                {"034Б196", "СТАРЧЕВИЦА 4", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 3", "5+5"},
                {"034Б197", "СТАРЧЕВИЦА 5", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 4", "5+5"},
                {"034Б198", "СТАРЧЕВИЦА 6", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 5", "5+5"},
                {"034Б199", "СТАРЧЕВИЦА 7", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 6", "5+5"},
                {"034Б200", "СТАРЧЕВИЦА 8", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 7", "5+5"},
                {"034Б201", "СТАРЧЕВИЦА 9", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 8", "5+5"},
                {"034Б202", "СТАРЧЕВИЦА 10", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 9", "5+5"},
                {"034Б203", "СТАРЧЕВИЦА 11", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 10", "5+5"},
                {"034Б204", "СТАРЧЕВИЦА 12", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 11", "5+5"},
                {"034Б205", "СТАРЧЕВИЦА 13", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 12", "5+5"},
                {"034Б206", "СТАРЧЕВИЦА 14", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 13", "5+5"},
                {"034Б207", "СТАРЧЕВИЦА 15", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 14", "5+5"},
                {"034Б208", "СТАРЧЕВИЦА 16", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 15", "5+5"},
                {"034Б209", "СТАРЧЕВИЦА 17", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 16", "5+5"},
                {"034Б210", "СТАРЧЕВИЦА 18", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 17", "5+5"},
                {"034Б211", "СТАРЧЕВИЦА 19", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 18", "5+5"},
                {"034Б212", "СТАРЧЕВИЦА 20", "ОШ \"БРАНКО РАДИЧЕВИЋ\", Булев. В. С. Степановића 116, уч. 19", "5+5"},
                {"034Б213", "СТРАТИНСКА", "ПШ \"МЛАДЕН СТОЈАНОВИЋ\", Стратинска, уч. 1", "3+3"},
                {"034Б214", "СТРИЧИЋИ", "МЗ СТРИЧИЋИ, сала 1", "5+5"},
                {"034Б215", "САРАЧИЦА - 2 /ПАВЛОВАЦ 2", "ПШ \"СВЕТИ САВА\", Павловац, уч. 2", "5+5"},
                {"034Б216", "ЦЕНТАР I / 1", "ШКОЛА УЧЕНИКА У ПРИВРЕДИ, Николе Пашића 11а, уч. 1", "5+5"},
                {"034Б217", "ЦЕНТАР I / 2", "МЗ ЦЕНТАР I, Симе Шолаје 7, сала 1", "5+5"},
                {"034Б218", "ЦЕНТАР I / 3", "ШКОЛА УЧЕНИКА У ПРИВРЕДИ, Николе Пашића 11а, уч. 2", "5+5"},
                {"034Б219", "ЦЕНТАР I / 4", "ШКОЛА УЧЕНИКА У ПРИВРЕДИ, Николе Пашића 11а, уч. 3", "5+5"},
                {"034Б220", "ЦЕНТАР I / 5", "ЕКОНОМСКА ШКОЛА, Краља Алфонса XIII 34, уч. 1", "5+5"},
                {"034Б221", "ЦЕНТАР I / 6", "ЕКОНОМСКА ШКОЛА, Краља Алфонса XIII 34, уч. 2", "5+5"},
                {"034Б222", "ЦЕНТАР I / 7", "ЕКОНОМСКА ШКОЛА, Краља Алфонса XIII 34, уч. 3", "5+5"},
                {"034Б223", "ЦЕНТАР I / 8", "ЕКОНОМСКА ШКОЛА, Краља Алфонса XIII 34, уч. 4", "5+5"},
                {"034Б224", "ЦЕНТАР II / 1", "ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, фискултурна сала 1", "5+5"},
                {"034Б225", "ЦЕНТАР II / 2", "ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 1", "5+5"},
                {"034Б226", "ЦЕНТАР II / 3", "ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 2", "5+5"},
                {"034Б227", "ЦЕНТАР II / 4", "ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 3", "5+5"},
                {"034Б228", "ЦЕНТАР II / 5", "ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 4", "5+5"},
                {"034Б229", "ЦЕНТАР II / 6", "ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 5", "5+5"},
                {"034Б230", "ЦЕНТАР II / 7", "ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 6", "5+5"},
                {"034Б231", "ЦЕНТАР II / 8", "ОШ \"ИВО АНДРИЋ\", Бранка Радичевића 16, уч. 7", "5+5"},
                {"034Б232", "ЧЕСМА 1", "ПРИВАТНИ ОБЈЕКАТ,вл.МАРТИНОВИЋ ДАМИР, Петра Великог 50", "5+5"},
                {"034Б233", "ЧЕСМА 2", "МЗ ЧЕСМА, Петра Великог 30, сала 1", "5+5"},
                {"034Б234", "ЧЕСМА 3", "ПШ \"ВУК С. КАРАЏИЋ\", Петра Великог 32, уч. 1", "5+5"},
                {"034Б235", "ЧЕСМА 4", "ПШ \"ВУК С. КАРАЏИЋ\", Петра Великог 32, уч. 2", "5+5"},
                {"034Б236", "ЧОКОРСКА ПОЉА", "Коњички клуб, објекат \"Салаш\", сала 1", "5+5"},
                {"034Б237", "ШАРГОВАЦ 1", "ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 1", "5+5"},
                {"034Б238", "ШАРГОВАЦ 2", "ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 2", "5+5"},
                {"034Б239", "ШАРГОВАЦ 3", "ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 3", "5+5"},
                {"034Б240", "ШАРГОВАЦ 4", "ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 4", "5+5"},
                {"034Б241", "ШИМИЋИ", "МЗ Шимићи, сала 1", "3+3"},
                {"034Б242", "АДА-7", "ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч 7", "5+5"},
                {"034Б243", "АДА -8", "ПШ \"ВУК С. КАРАЏИЋ\", Вељка Млађеновића бб, уч 8", "5+5"},
                {"034Б244", "КУЉАНИ - 2", "ПШ \"ЈОВАН ДУЧИЋ\" (нови објекат), Куљани, уч. 2", "5+5"},
                {"034Б245", "КУЉАНИ - 3", "ПШ \"ЈОВАН ДУЧИЋ\" (нови објекат), Куљани, уч. 3", "5+5"},
                {"034Б246", "КУЉАНИ - 4", "ПШ \"ЈОВАН ДУЧИЋ\" (нови објекат), Куљани, уч. 4", "5+5"},
                {"034Б247", "КУЉАНИ - 5", "ПШ \"ЈОВАН ДУЧИЋ\" (НОВИ ОБЈЕКАТ), Куљани, уч. 5", "5+5"},
                {"034Б248", "ПРИЈЕЧАНИ 2", "ПШ \"ЈОВАН ДУЧИЋ\", Пријечани, уч. 2", "5+5"},
                {"034Б249", "ПРИЈЕЧАНИ - 3", "ПШ \"ЈОВАН ДУЧИЋ\", Пријечани, уч. 3", "5+5"},
                {"034Б250", "КОЛА - 2", "ОШ \"ПЕТАР КОЧИЋ\", Kола, уч. 2", "5+5"},
                {"034Б251", "ШАРГОВАЦ 5", "ОШ \"ЂУРА ЈАКШИЋ\", Суботичка 28, уч. 5", "5+5"},
                {"034Б252", "МИШИН ХАН - 2", "ПШ \"МИЛУТИН БОЈИЋ\", Мишин Хан, уч. 2", "5+5"},
                {"034Б253", "ДЕБЕЉАЦИ - 3", "ПШ \"СТАНКО РАКИТА\", Тешана Подруговића бб, уч. 3", "5+5"},
                {"034Б254", "КАРАНОВАЦ - 3", "ОШ \"МИЛАН РАКИЋ\", Карановац, уч. 3", "5+5"},
                {"034Б501", "ОДСУСТВО", "Раднички универзитет, Грчка 4, сала 1", "3+3"},
                {"034БННН", "НЕПОТВРЂЕНИ", "Раднички универзитет, Грчка 4, сала 2", "3+3"}
        };

        Random random = new Random();
        for (String[] row : array) {
            // Create an instance of VotingCouncelEntity
            VotingCouncelEntity votingCouncel = new VotingCouncelEntity();
            // Set fields based on the provided data
            votingCouncel.setCode(row[0]);
            votingCouncel.setName(row[1]);
            votingCouncel.setLocation(row[2]);

            if("5+5".equals(row[3]))
                votingCouncel.setNumberOfMembers(4);
            else if("3+3".equals(row[3]))
                votingCouncel.setNumberOfMembers(2);

            int numberOfVoters = 100 + random.nextInt(901);
            votingCouncel.setNumberOfVoters(numberOfVoters);

            // Set mentor
            votingCouncel.setMentor(mentor);

            // Save the entity to the repository
            votingCouncelRepository.save(votingCouncel);
        }


    }
}
