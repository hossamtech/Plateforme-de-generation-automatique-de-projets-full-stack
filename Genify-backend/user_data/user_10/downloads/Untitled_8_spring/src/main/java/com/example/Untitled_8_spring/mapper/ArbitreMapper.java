package com.example.Untitled_8_spring.mapper;

import com.example.Untitled_8_spring.dto.ArbitreDto;
import com.example.Untitled_8_spring.models.Arbitre;
import org.springframework.stereotype.Component;

@Component
public class ArbitreMapper {

    public ArbitreDto mapToDTO(Arbitre entity) {
        ArbitreDto dto = new ArbitreDto();
        dto.setIdarbitre(entity.getIdarbitre());
        dto.setNom(entity.getNom());
        dto.setNationalite(entity.getNationalite());
        return dto;
    }

    public Arbitre mapToEntity(ArbitreDto dto) {
        Arbitre entity = new Arbitre();
        entity.setIdarbitre(dto.getIdarbitre());
        entity.setNom(dto.getNom());
        entity.setNationalite(dto.getNationalite());
        return entity;
    }
}
