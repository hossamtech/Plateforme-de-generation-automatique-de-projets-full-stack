package com.example.Untitled_1_spring.service;

import com.example.Untitled_1_spring.models.arbitre;
import com.example.Untitled_1_spring.dto.arbitreDto;
import com.example.Untitled_1_spring.repository.arbitreRepository;
import com.example.Untitled_1_spring.mapper.arbitreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class arbitreService {

    @Autowired
    private arbitreRepository arbitreRepository;
    @Autowired
    private arbitreMapper mapper;

    public arbitreDto addarbitre(arbitreDto dto) {
        arbitre entity = mapper.mapToEntity(dto);
        return mapper.mapToDTO(arbitreRepository.save(entity));
    }

    public arbitreDto updatearbitre(arbitreDto dto) {
        Optional<arbitre> optionalEntity = arbitreRepository.findById(dto.getId());
        if (optionalEntity.isPresent()) {
            arbitre existingEntity = optionalEntity.get();
            arbitre updatedEntity = mapper.mapToEntity(dto);
            existingEntity.setIdArbitre(updatedEntity.getIdArbitre());
            existingEntity.setNom(updatedEntity.getNom());
            existingEntity.setNationalite(updatedEntity.getNationalite());
            return mapper.mapToDTO(arbitreRepository.save(existingEntity));
        } else {
            throw new RuntimeException("Entity not found");
        }
    }

    public void deletearbitre(Long id) {
        if (!arbitreRepository.existsById(id)) {
            throw new RuntimeException("arbitre not found");
        }
        arbitreRepository.deleteById(id);
    }

    public Optional<arbitreDto> getById(Long id) {
        return arbitreRepository.findById(id)
            .map(mapper::mapToDTO);
    }

    public List<arbitreDto> getAll() {
        return arbitreRepository.findAll().stream()
            .map(mapper::mapToDTO)
            .collect(Collectors.toList());
    }
}
