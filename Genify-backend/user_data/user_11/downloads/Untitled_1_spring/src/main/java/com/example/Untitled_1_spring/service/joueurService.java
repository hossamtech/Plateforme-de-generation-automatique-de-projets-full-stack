package com.example.Untitled_1_spring.service;

import com.example.Untitled_1_spring.models.joueur;
import com.example.Untitled_1_spring.dto.joueurDto;
import com.example.Untitled_1_spring.repository.joueurRepository;
import com.example.Untitled_1_spring.mapper.joueurMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.Untitled_1_spring.models.equipe;
import com.example.Untitled_1_spring.repository.equipeRepository;

@Service
public class joueurService {

    @Autowired
    private joueurRepository joueurRepository;
    @Autowired
    private equipeRepository equipeRepository;
    @Autowired
    private joueurMapper mapper;

    public joueurDto addjoueur(joueurDto dto) {
        joueur entity = mapper.mapToEntity(dto);
        if (dto.getequipeId() != null) {
            equipe relatedequipe = equipeRepository.findById(dto.getequipeId())
                .orElseThrow(() -> new RuntimeException("equipe not found"));
            entity.setequipe(relatedequipe);
        }
        return mapper.mapToDTO(joueurRepository.save(entity));
    }

    public joueurDto updatejoueur(joueurDto dto) {
        Optional<joueur> optionalEntity = joueurRepository.findById(dto.getId());
        if (optionalEntity.isPresent()) {
            joueur existingEntity = optionalEntity.get();
            joueur updatedEntity = mapper.mapToEntity(dto);
            existingEntity.setIdJoueur(updatedEntity.getIdJoueur());
            existingEntity.setNomJoueur(updatedEntity.getNomJoueur());
            existingEntity.setPoste(updatedEntity.getPoste());
            if (dto.getequipeId() != null) {
                equipe relatedequipe = equipeRepository.findById(dto.getequipeId())
                    .orElseThrow(() -> new RuntimeException("equipe not found"));
                existingEntity.setequipe(relatedequipe);
            }
            return mapper.mapToDTO(joueurRepository.save(existingEntity));
        } else {
            throw new RuntimeException("Entity not found");
        }
    }

    public void deletejoueur(Long id) {
        if (!joueurRepository.existsById(id)) {
            throw new RuntimeException("joueur not found");
        }
        joueurRepository.deleteById(id);
    }

    public Optional<joueurDto> getById(Long id) {
        return joueurRepository.findById(id)
            .map(mapper::mapToDTO);
    }

    public List<joueurDto> getAll() {
        return joueurRepository.findAll().stream()
            .map(mapper::mapToDTO)
            .collect(Collectors.toList());
    }
}
