package com.example.Untitled_9_spring.mapper;

import com.example.Untitled_9_spring.dto.EquipeDto;
import com.example.Untitled_9_spring.models.Equipe;
import org.springframework.stereotype.Component;

@Component
public class EquipeMapper {

    public EquipeDto mapToDTO(Equipe entity) {
        EquipeDto dto = new EquipeDto();
        dto.setIdequipe(entity.getIdequipe());
        dto.setNomequipe(entity.getNomequipe());
        dto.setPays(entity.getPays());
        return dto;
    }

    public Equipe mapToEntity(EquipeDto dto) {
        Equipe entity = new Equipe();
        entity.setIdequipe(dto.getIdequipe());
        entity.setNomequipe(dto.getNomequipe());
        entity.setPays(dto.getPays());
        return entity;
    }
}
