package com.example.Untitled_6_spring.service;

import com.example.Untitled_6_spring.models.Joueur;
import com.example.Untitled_6_spring.dto.JoueurDto;
import com.example.Untitled_6_spring.repository.JoueurRepository;
import com.example.Untitled_6_spring.mapper.JoueurMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.Untitled_6_spring.models.Equipe;
import com.example.Untitled_6_spring.repository.EquipeRepository;

@Service
public class JoueurService {

    @Autowired
    private JoueurRepository joueurRepository;
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private JoueurMapper mapper;

    public JoueurDto addJoueur(JoueurDto dto) {
        Joueur entity = mapper.mapToEntity(dto);
        if (dto.getIdequipe() != null) {
            Equipe relatedEquipe = equipeRepository.findById(dto.getIdequipe())
                .orElseThrow(() -> new RuntimeException("Equipe not found"));
            entity.setEquipe(relatedEquipe);
        }
        return mapper.mapToDTO(joueurRepository.save(entity));
    }

    public JoueurDto updateJoueur(JoueurDto dto) {
        Optional<Joueur> optionalEntity = joueurRepository.findById(dto.getIdjoueur());
        if (optionalEntity.isPresent()) {
            Joueur existingEntity = optionalEntity.get();
            Joueur updatedEntity = mapper.mapToEntity(dto);
            existingEntity.setIdjoueur(updatedEntity.getIdjoueur());
            existingEntity.setNomjoueur(updatedEntity.getNomjoueur());
            existingEntity.setPoste(updatedEntity.getPoste());
            if (dto.getIdequipe() != null) {
                Equipe relatedEquipe = equipeRepository.findById(dto.getIdequipe())
                    .orElseThrow(() -> new RuntimeException("Equipe not found"));
                existingEntity.setEquipe(relatedEquipe);
            }
            return mapper.mapToDTO(joueurRepository.save(existingEntity));
        } else {
            throw new RuntimeException("Entity not found");
        }
    }

    public void deleteJoueur(Long id) {
        if (!joueurRepository.existsById(id)) {
            throw new RuntimeException("Joueur not found");
        }
        joueurRepository.deleteById(id);
    }

    public Optional<JoueurDto> getById(Long id) {
        return joueurRepository.findById(id)
            .map(mapper::mapToDTO);
    }

    public List<JoueurDto> getAll() {
        return joueurRepository.findAll().stream()
            .map(mapper::mapToDTO)
            .collect(Collectors.toList());
    }
}
