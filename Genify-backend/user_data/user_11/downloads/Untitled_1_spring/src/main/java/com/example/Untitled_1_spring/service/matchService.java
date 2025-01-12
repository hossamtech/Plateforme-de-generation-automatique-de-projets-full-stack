package com.example.Untitled_1_spring.service;

import com.example.Untitled_1_spring.models.match;
import com.example.Untitled_1_spring.dto.matchDto;
import com.example.Untitled_1_spring.repository.matchRepository;
import com.example.Untitled_1_spring.mapper.matchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.Untitled_1_spring.models.arbitre;
import com.example.Untitled_1_spring.repository.arbitreRepository;
import com.example.Untitled_1_spring.models.stade;
import com.example.Untitled_1_spring.repository.stadeRepository;

@Service
public class matchService {

    @Autowired
    private matchRepository matchRepository;
    @Autowired
    private arbitreRepository arbitreRepository;
    @Autowired
    private stadeRepository stadeRepository;
    @Autowired
    private matchMapper mapper;

    public matchDto addmatch(matchDto dto) {
        match entity = mapper.mapToEntity(dto);
        if (dto.getarbitreId() != null) {
            arbitre relatedarbitre = arbitreRepository.findById(dto.getarbitreId())
                .orElseThrow(() -> new RuntimeException("arbitre not found"));
            entity.setarbitre(relatedarbitre);
        }
        if (dto.getstadeId() != null) {
            stade relatedstade = stadeRepository.findById(dto.getstadeId())
                .orElseThrow(() -> new RuntimeException("stade not found"));
            entity.setstade(relatedstade);
        }
        return mapper.mapToDTO(matchRepository.save(entity));
    }

    public matchDto updatematch(matchDto dto) {
        Optional<match> optionalEntity = matchRepository.findById(dto.getId());
        if (optionalEntity.isPresent()) {
            match existingEntity = optionalEntity.get();
            match updatedEntity = mapper.mapToEntity(dto);
            existingEntity.setIdMatch(updatedEntity.getIdMatch());
            existingEntity.setDateMatch(updatedEntity.getDateMatch());
            existingEntity.setHeureMatch(updatedEntity.getHeureMatch());
            if (dto.getarbitreId() != null) {
                arbitre relatedarbitre = arbitreRepository.findById(dto.getarbitreId())
                    .orElseThrow(() -> new RuntimeException("arbitre not found"));
                existingEntity.setarbitre(relatedarbitre);
            }
            if (dto.getstadeId() != null) {
                stade relatedstade = stadeRepository.findById(dto.getstadeId())
                    .orElseThrow(() -> new RuntimeException("stade not found"));
                existingEntity.setstade(relatedstade);
            }
            return mapper.mapToDTO(matchRepository.save(existingEntity));
        } else {
            throw new RuntimeException("Entity not found");
        }
    }

    public void deletematch(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new RuntimeException("match not found");
        }
        matchRepository.deleteById(id);
    }

    public Optional<matchDto> getById(Long id) {
        return matchRepository.findById(id)
            .map(mapper::mapToDTO);
    }

    public List<matchDto> getAll() {
        return matchRepository.findAll().stream()
            .map(mapper::mapToDTO)
            .collect(Collectors.toList());
    }
}
