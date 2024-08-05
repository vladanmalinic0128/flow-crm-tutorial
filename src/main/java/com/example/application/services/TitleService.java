package com.example.application.services;

import com.example.application.entities.TitleEntity;
import com.example.application.enums.TitleEnum;
import com.example.application.repositories.TitleRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Service
public class TitleService {
    private final TitleRepository titleRepository;

    public Map<TitleEnum, TitleEntity> getTitles() {
        Map<TitleEnum, TitleEntity> titles = new HashMap<>();

        List<TitleEntity> titleEntities = titleRepository.findAll();

        for(TitleEntity title: titleEntities) {
            if(title.getId() == 1)
                titles.put(TitleEnum.MEMBER, title);
            else if(title.getId() == 2)
                titles.put(TitleEnum.MEMBER_DEPUTY, title);
        }

        return titles;
    }
}
