package com.example.Untitled_1_spring.mapper;

import com.example.Untitled_1_spring.dto.arbitreDto;
import com.example.Untitled_1_spring.models.arbitre;
import org.springframework.stereotype.Component;

@Component
public class arbitreMapper {

    public arbitreDto mapToDTO(arbitre entity) {
        arbitreDto dto = new arbitreDto();
        dto.setIdArbitre(entity.getIdArbitre());
        dto.setNom(entity.getNom());
        dto.setNationalite(entity.getNationalite());
        return dto;
    }

    public arbitre mapToEntity(arbitreDto dto) {
        arbitre entity = new arbitre();
        entity.setIdArbitre(dto.getIdArbitre());
        entity.setNom(dto.getNom());
        entity.setNationalite(dto.getNationalite());
        return entity;
    }
}
