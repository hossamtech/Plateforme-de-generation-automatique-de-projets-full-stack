package com.example.Untitled_6_spring.service;

import com.example.Untitled_6_spring.models.Arbitre;
import com.example.Untitled_6_spring.dto.ArbitreDto;
import com.example.Untitled_6_spring.repository.ArbitreRepository;
import com.example.Untitled_6_spring.mapper.ArbitreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ArbitreService {

    @Autowired
    private ArbitreRepository arbitreRepository;
    @Autowired
    private ArbitreMapper mapper;

    public ArbitreDto addArbitre(ArbitreDto dto) {
        Arbitre entity = mapper.mapToEntity(dto);
        return mapper.mapToDTO(arbitreRepository.save(entity));
    }

    public ArbitreDto updateArbitre(ArbitreDto dto) {
        Optional<Arbitre> optionalEntity = arbitreRepository.findById(dto.getIdarbitre());
        if (optionalEntity.isPresent()) {
            Arbitre existingEntity = optionalEntity.get();
            Arbitre updatedEntity = mapper.mapToEntity(dto);
            existingEntity.setIdarbitre(updatedEntity.getIdarbitre());
            existingEntity.setNom(updatedEntity.getNom());
            existingEntity.setNationalite(updatedEntity.getNationalite());
            return mapper.mapToDTO(arbitreRepository.save(existingEntity));
        } else {
            throw new RuntimeException("Entity not found");
        }
    }

    public void deleteArbitre(Long id) {
        if (!arbitreRepository.existsById(id)) {
            throw new RuntimeException("Arbitre not found");
        }
        arbitreRepository.deleteById(id);
    }

    public Optional<ArbitreDto> getById(Long id) {
        return arbitreRepository.findById(id)
            .map(mapper::mapToDTO);
    }

    public List<ArbitreDto> getAll() {
        return arbitreRepository.findAll().stream()
            .map(mapper::mapToDTO)
            .collect(Collectors.toList());
    }
}
