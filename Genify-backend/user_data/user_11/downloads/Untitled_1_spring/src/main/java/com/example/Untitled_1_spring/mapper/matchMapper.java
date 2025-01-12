package com.example.Untitled_1_spring.mapper;

import com.example.Untitled_1_spring.dto.matchDto;
import com.example.Untitled_1_spring.models.match;
import org.springframework.stereotype.Component;

@Component
public class matchMapper {

    public matchDto mapToDTO(match entity) {
        matchDto dto = new matchDto();
        dto.setIdMatch(entity.getIdMatch());
        dto.setDateMatch(entity.getDateMatch());
        dto.setHeureMatch(entity.getHeureMatch());
        if (entity.getarbitre() != null) {
            dto.setarbitreId(entity.getarbitre().getId());
        }
        if (entity.getstade() != null) {
            dto.setstadeId(entity.getstade().getId());
        }
        return dto;
    }

    public match mapToEntity(matchDto dto) {
        match entity = new match();
        entity.setIdMatch(dto.getIdMatch());
        entity.setDateMatch(dto.getDateMatch());
        entity.setHeureMatch(dto.getHeureMatch());
        return entity;
    }
}
