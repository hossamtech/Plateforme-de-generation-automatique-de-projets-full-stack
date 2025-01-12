package com.example.Untitled_9_spring.mapper;

import com.example.Untitled_9_spring.dto.MatchDto;
import com.example.Untitled_9_spring.models.Match;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    public MatchDto mapToDTO(Match entity) {
        MatchDto dto = new MatchDto();
        dto.setIdmatch(entity.getIdmatch());
        dto.setDatematch(entity.getDatematch());
        dto.setHeurematch(entity.getHeurematch());
        if (entity.getArbitre() != null) {
            dto.setIdarbitre(entity.getArbitre().getIdarbitre());
        }
        if (entity.getEquipe1() != null) {
            dto.setEquipe1Id(entity.getEquipe1().getIdequipe());
        }
        if (entity.getEquipe2() != null) {
            dto.setEquipe2Id(entity.getEquipe2().getIdequipe());
        }
        if (entity.getStade() != null) {
            dto.setIdstade(entity.getStade().getIdstade());
        }
        return dto;
    }

    public Match mapToEntity(MatchDto dto) {
        Match entity = new Match();
        entity.setIdmatch(dto.getIdmatch());
        entity.setDatematch(dto.getDatematch());
        entity.setHeurematch(dto.getHeurematch());
        return entity;
    }
}
