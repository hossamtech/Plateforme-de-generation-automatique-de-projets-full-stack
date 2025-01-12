package com.example.Untitled_9_spring.service;

import com.example.Untitled_9_spring.models.Match;
import com.example.Untitled_9_spring.dto.MatchDto;
import com.example.Untitled_9_spring.repository.MatchRepository;
import com.example.Untitled_9_spring.mapper.MatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.Untitled_9_spring.models.Arbitre;
import com.example.Untitled_9_spring.repository.ArbitreRepository;
import com.example.Untitled_9_spring.models.Equipe;
import com.example.Untitled_9_spring.repository.EquipeRepository;
import com.example.Untitled_9_spring.models.Stade;
import com.example.Untitled_9_spring.repository.StadeRepository;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private ArbitreRepository arbitreRepository;
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private StadeRepository stadeRepository;
    @Autowired
    private MatchMapper mapper;

    public MatchDto addMatch(MatchDto dto) {
        Match entity = mapper.mapToEntity(dto);
        if (dto.getIdarbitre() != null) {
            Arbitre relatedArbitre = arbitreRepository.findById(dto.getIdarbitre())
                .orElseThrow(() -> new RuntimeException("Arbitre not found"));
            entity.setArbitre(relatedArbitre);
        }
        if (dto.getEquipe1Id() != null) {
            Equipe relatedEquipe1 = equipeRepository.findById(dto.getEquipe1Id())
                .orElseThrow(() -> new RuntimeException("Equipe not found"));
            entity.setEquipe1(relatedEquipe1);
        }
        if (dto.getEquipe2Id() != null) {
            Equipe relatedEquipe2 = equipeRepository.findById(dto.getEquipe2Id())
                .orElseThrow(() -> new RuntimeException("Equipe not found"));
            entity.setEquipe2(relatedEquipe2);
        }
        if (dto.getIdstade() != null) {
            Stade relatedStade = stadeRepository.findById(dto.getIdstade())
                .orElseThrow(() -> new RuntimeException("Stade not found"));
            entity.setStade(relatedStade);
        }
        return mapper.mapToDTO(matchRepository.save(entity));
    }

    public MatchDto updateMatch(MatchDto dto) {
        Optional<Match> optionalEntity = matchRepository.findById(dto.getIdmatch());
        if (optionalEntity.isPresent()) {
            Match existingEntity = optionalEntity.get();
            Match updatedEntity = mapper.mapToEntity(dto);
            existingEntity.setIdmatch(updatedEntity.getIdmatch());
            existingEntity.setDatematch(updatedEntity.getDatematch());
            existingEntity.setHeurematch(updatedEntity.getHeurematch());
            if (dto.getIdarbitre() != null) {
                Arbitre relatedArbitre = arbitreRepository.findById(dto.getIdarbitre())
                    .orElseThrow(() -> new RuntimeException("Arbitre not found"));
                existingEntity.setArbitre(relatedArbitre);
            }
        if (dto.getEquipe1Id() != null) {
            Equipe relatedEquipe1 = equipeRepository.findById(dto.getEquipe1Id())
                .orElseThrow(() -> new RuntimeException("Equipe not found"));
            existingEntity.setEquipe1(relatedEquipe1);
        }
        if (dto.getEquipe2Id() != null) {
            Equipe relatedEquipe2 = equipeRepository.findById(dto.getEquipe2Id())
                .orElseThrow(() -> new RuntimeException("Equipe not found"));
            existingEntity.setEquipe2(relatedEquipe2);
        }
            if (dto.getIdstade() != null) {
                Stade relatedStade = stadeRepository.findById(dto.getIdstade())
                    .orElseThrow(() -> new RuntimeException("Stade not found"));
                existingEntity.setStade(relatedStade);
            }
            return mapper.mapToDTO(matchRepository.save(existingEntity));
        } else {
            throw new RuntimeException("Entity not found");
        }
    }

    public void deleteMatch(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new RuntimeException("Match not found");
        }
        matchRepository.deleteById(id);
    }

    public Optional<MatchDto> getById(Long id) {
        return matchRepository.findById(id)
            .map(mapper::mapToDTO);
    }

    public List<MatchDto> getAll() {
        return matchRepository.findAll().stream()
            .map(mapper::mapToDTO)
            .collect(Collectors.toList());
    }
}
