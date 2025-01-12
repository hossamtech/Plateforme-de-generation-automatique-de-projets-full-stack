package com.example.Untitled_1_spring.service;

import com.example.Untitled_1_spring.models.stade;
import com.example.Untitled_1_spring.dto.stadeDto;
import com.example.Untitled_1_spring.repository.stadeRepository;
import com.example.Untitled_1_spring.mapper.stadeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class stadeService {

    @Autowired
    private stadeRepository stadeRepository;
    @Autowired
    private stadeMapper mapper;

    public stadeDto addstade(stadeDto dto) {
        stade entity = mapper.mapToEntity(dto);
        return mapper.mapToDTO(stadeRepository.save(entity));
    }

    public stadeDto updatestade(stadeDto dto) {
        Optional<stade> optionalEntity = stadeRepository.findById(dto.getId());
        if (optionalEntity.isPresent()) {
            stade existingEntity = optionalEntity.get();
            stade updatedEntity = mapper.mapToEntity(dto);
            existingEntity.setIdStade(updatedEntity.getIdStade());
            existingEntity.setNomStade(updatedEntity.getNomStade());
            existingEntity.setVille(updatedEntity.getVille());
            return mapper.mapToDTO(stadeRepository.save(existingEntity));
        } else {
            throw new RuntimeException("Entity not found");
        }
    }

    public void deletestade(Long id) {
        if (!stadeRepository.existsById(id)) {
            throw new RuntimeException("stade not found");
        }
        stadeRepository.deleteById(id);
    }

    public Optional<stadeDto> getById(Long id) {
        return stadeRepository.findById(id)
            .map(mapper::mapToDTO);
    }

    public List<stadeDto> getAll() {
        return stadeRepository.findAll().stream()
            .map(mapper::mapToDTO)
            .collect(Collectors.toList());
    }
}
