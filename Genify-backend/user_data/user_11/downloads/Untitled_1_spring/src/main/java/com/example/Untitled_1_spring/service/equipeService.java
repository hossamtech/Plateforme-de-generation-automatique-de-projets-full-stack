package com.example.Untitled_1_spring.service;

import com.example.Untitled_1_spring.models.equipe;
import com.example.Untitled_1_spring.dto.equipeDto;
import com.example.Untitled_1_spring.repository.equipeRepository;
import com.example.Untitled_1_spring.mapper.equipeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class equipeService {

    @Autowired
    private equipeRepository equipeRepository;
    @Autowired
    private equipeMapper mapper;

    public equipeDto addequipe(equipeDto dto) {
        equipe entity = mapper.mapToEntity(dto);
        return mapper.mapToDTO(equipeRepository.save(entity));
    }

    public equipeDto updateequipe(equipeDto dto) {
        Optional<equipe> optionalEntity = equipeRepository.findById(dto.getId());
        if (optionalEntity.isPresent()) {
            equipe existingEntity = optionalEntity.get();
            equipe updatedEntity = mapper.mapToEntity(dto);
            existingEntity.setIdEquipe(updatedEntity.getIdEquipe());
            existingEntity.setNomEquipe(updatedEntity.getNomEquipe());
            existingEntity.setPays(updatedEntity.getPays());
            return mapper.mapToDTO(equipeRepository.save(existingEntity));
        } else {
            throw new RuntimeException("Entity not found");
        }
    }

    public void deleteequipe(Long id) {
        if (!equipeRepository.existsById(id)) {
            throw new RuntimeException("equipe not found");
        }
        equipeRepository.deleteById(id);
    }

    public Optional<equipeDto> getById(Long id) {
        return equipeRepository.findById(id)
            .map(mapper::mapToDTO);
    }

    public List<equipeDto> getAll() {
        return equipeRepository.findAll().stream()
            .map(mapper::mapToDTO)
            .collect(Collectors.toList());
    }
}
