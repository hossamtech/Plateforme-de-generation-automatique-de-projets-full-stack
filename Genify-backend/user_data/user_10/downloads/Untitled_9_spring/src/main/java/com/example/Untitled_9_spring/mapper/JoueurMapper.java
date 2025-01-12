package com.example.Untitled_9_spring.mapper;

import com.example.Untitled_9_spring.dto.JoueurDto;
import com.example.Untitled_9_spring.models.Joueur;
import org.springframework.stereotype.Component;

@Component
public class JoueurMapper {

    public JoueurDto mapToDTO(Joueur entity) {
        JoueurDto dto = new JoueurDto();
        dto.setIdjoueur(entity.getIdjoueur());
        dto.setNomjoueur(entity.getNomjoueur());
        dto.setPoste(entity.getPoste());
        if (entity.getEquipe() != null) {
            dto.setIdequipe(entity.getEquipe().getIdequipe());
        }
        return dto;
    }

    public Joueur mapToEntity(JoueurDto dto) {
        Joueur entity = new Joueur();
        entity.setIdjoueur(dto.getIdjoueur());
        entity.setNomjoueur(dto.getNomjoueur());
        entity.setPoste(dto.getPoste());
        return entity;
    }
}
