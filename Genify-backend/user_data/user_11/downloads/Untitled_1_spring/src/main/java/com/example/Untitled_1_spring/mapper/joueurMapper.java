package com.example.Untitled_1_spring.mapper;

import com.example.Untitled_1_spring.dto.joueurDto;
import com.example.Untitled_1_spring.models.joueur;
import org.springframework.stereotype.Component;

@Component
public class joueurMapper {

    public joueurDto mapToDTO(joueur entity) {
        joueurDto dto = new joueurDto();
        dto.setIdJoueur(entity.getIdJoueur());
        dto.setNomJoueur(entity.getNomJoueur());
        dto.setPoste(entity.getPoste());
        if (entity.getequipe() != null) {
            dto.setequipeId(entity.getequipe().getId());
        }
        return dto;
    }

    public joueur mapToEntity(joueurDto dto) {
        joueur entity = new joueur();
        entity.setIdJoueur(dto.getIdJoueur());
        entity.setNomJoueur(dto.getNomJoueur());
        entity.setPoste(dto.getPoste());
        return entity;
    }
}
