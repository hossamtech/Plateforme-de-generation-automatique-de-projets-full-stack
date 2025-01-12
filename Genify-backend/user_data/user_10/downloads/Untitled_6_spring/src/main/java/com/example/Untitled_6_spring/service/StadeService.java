package com.example.Untitled_6_spring.service;

import com.example.Untitled_6_spring.models.Stade;
import com.example.Untitled_6_spring.dto.StadeDto;
import com.example.Untitled_6_spring.repository.StadeRepository;
import com.example.Untitled_6_spring.mapper.StadeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class StadeService {

    @Autowired
    private StadeRepository stadeRepository;
    @Autowired
    private StadeMapper mapper;

    public StadeDto addStade(StadeDto dto) {
        Stade entity = mapper.mapToEntity(dto);
        return mapper.mapToDTO(stadeRepository.save(entity));
    }

    public StadeDto updateStade(StadeDto dto) {
        Optional<Stade> optionalEntity = stadeRepository.findById(dto.getIdstade());
        if (optionalEntity.isPresent()) {
            Stade existingEntity = optionalEntity.get();
            Stade updatedEntity = mapper.mapToEntity(dto);
            existingEntity.setIdstade(updatedEntity.getIdstade());
            existingEntity.setNomstade(updatedEntity.getNomstade());
            existingEntity.setVille(updatedEntity.getVille());
            return mapper.mapToDTO(stadeRepository.save(existingEntity));
        } else {
            throw new RuntimeException("Entity not found");
        }
    }

    public void deleteStade(Long id) {
        if (!stadeRepository.existsById(id)) {
            throw new RuntimeException("Stade not found");
        }
        stadeRepository.deleteById(id);
    }

    public Optional<StadeDto> getById(Long id) {
        return stadeRepository.findById(id)
            .map(mapper::mapToDTO);
    }

    public List<StadeDto> getAll() {
        return stadeRepository.findAll().stream()
            .map(mapper::mapToDTO)
            .collect(Collectors.toList());
    }
}
