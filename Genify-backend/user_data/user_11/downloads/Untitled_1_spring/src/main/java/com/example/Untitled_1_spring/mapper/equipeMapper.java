package com.example.Untitled_1_spring.mapper;

import com.example.Untitled_1_spring.dto.equipeDto;
import com.example.Untitled_1_spring.models.equipe;
import org.springframework.stereotype.Component;

@Component
public class equipeMapper {

    public equipeDto mapToDTO(equipe entity) {
        equipeDto dto = new equipeDto();
        dto.setIdEquipe(entity.getIdEquipe());
        dto.setNomEquipe(entity.getNomEquipe());
        dto.setPays(entity.getPays());
        return dto;
    }

    public equipe mapToEntity(equipeDto dto) {
        equipe entity = new equipe();
        entity.setIdEquipe(dto.getIdEquipe());
        entity.setNomEquipe(dto.getNomEquipe());
        entity.setPays(dto.getPays());
        return entity;
    }
}
