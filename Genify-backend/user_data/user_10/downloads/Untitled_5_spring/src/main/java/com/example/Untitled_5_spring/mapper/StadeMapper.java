package com.example.Untitled_5_spring.mapper;

import com.example.Untitled_5_spring.dto.StadeDto;
import com.example.Untitled_5_spring.models.Stade;
import org.springframework.stereotype.Component;

@Component
public class StadeMapper {

    public StadeDto mapToDTO(Stade entity) {
        StadeDto dto = new StadeDto();
        dto.setIdstade(entity.getIdstade());
        dto.setNomstade(entity.getNomstade());
        dto.setVille(entity.getVille());
        return dto;
    }

    public Stade mapToEntity(StadeDto dto) {
        Stade entity = new Stade();
        entity.setIdstade(dto.getIdstade());
        entity.setNomstade(dto.getNomstade());
        entity.setVille(dto.getVille());
        return entity;
    }
}
