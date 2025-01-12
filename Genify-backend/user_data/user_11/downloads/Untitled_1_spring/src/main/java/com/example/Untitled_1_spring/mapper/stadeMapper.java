package com.example.Untitled_1_spring.mapper;

import com.example.Untitled_1_spring.dto.stadeDto;
import com.example.Untitled_1_spring.models.stade;
import org.springframework.stereotype.Component;

@Component
public class stadeMapper {

    public stadeDto mapToDTO(stade entity) {
        stadeDto dto = new stadeDto();
        dto.setIdStade(entity.getIdStade());
        dto.setNomStade(entity.getNomStade());
        dto.setVille(entity.getVille());
        return dto;
    }

    public stade mapToEntity(stadeDto dto) {
        stade entity = new stade();
        entity.setIdStade(dto.getIdStade());
        entity.setNomStade(dto.getNomStade());
        entity.setVille(dto.getVille());
        return entity;
    }
}
