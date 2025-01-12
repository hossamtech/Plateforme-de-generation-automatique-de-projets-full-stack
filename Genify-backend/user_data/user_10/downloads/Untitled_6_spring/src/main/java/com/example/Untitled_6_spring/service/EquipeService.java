package com.example.Untitled_6_spring.service;

import com.example.Untitled_6_spring.models.Equipe;
import com.example.Untitled_6_spring.dto.EquipeDto;
import com.example.Untitled_6_spring.repository.EquipeRepository;
import com.example.Untitled_6_spring.mapper.EquipeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EquipeService {

    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private EquipeMapper mapper;

    public EquipeDto addEquipe(EquipeDto dto) {
        Equipe entity = mapper.mapToEntity(dto);
        return mapper.mapToDTO(equipeRepository.save(entity));
    }

    public EquipeDto updateEquipe(EquipeDto dto) {
        Optional<Equipe> optionalEntity = equipeRepository.findById(dto.getIdequipe());
        if (optionalEntity.isPresent()) {
            Equipe existingEntity = optionalEntity.get();
            Equipe updatedEntity = mapper.mapToEntity(dto);
            existingEntity.setIdequipe(updatedEntity.getIdequipe());
            existingEntity.setNomequipe(updatedEntity.getNomequipe());
            existingEntity.setPays(updatedEntity.getPays());
            return mapper.mapToDTO(equipeRepository.save(existingEntity));
        } else {
            throw new RuntimeException("Entity not found");
        }
    }

    public void deleteEquipe(Long id) {
        if (!equipeRepository.existsById(id)) {
            throw new RuntimeException("Equipe not found");
        }
        equipeRepository.deleteById(id);
    }

    public Optional<EquipeDto> getById(Long id) {
        return equipeRepository.findById(id)
            .map(mapper::mapToDTO);
    }

    public List<EquipeDto> getAll() {
        return equipeRepository.findAll().stream()
            .map(mapper::mapToDTO)
            .collect(Collectors.toList());
    }
}
