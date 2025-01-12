package com.example.Untitled_7_spring.mapper;

import com.example.Untitled_7_spring.dto.MatchDto;
import com.example.Untitled_7_spring.models.Match;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    public MatchDto mapToDTO(Match entity) {
        MatchDto dto = new MatchDto();
        dto.setIdmatch(entity.getIdmatch());
        dto.setDatematch(entity.getDatematch());
        dto.setHeurematch(entity.getHeurematch());
        if (entity.getArbitre() != null) {
            dto.setArbitreId(entity.getArbitre().getIdarbitre());
        }
        if (entity.getEquipe1() != null) {
            dto.setEquipe_1Id(entity.getEquipe1().getIdequipe());
        }
        if (entity.getEquipe2() != null) {
            dto.setEquipe_2Id(entity.getEquipe2().getIdequipe());
        }
        if (entity.getStade() != null) {
            dto.setStadeId(entity.getStade().getIdstade());
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
