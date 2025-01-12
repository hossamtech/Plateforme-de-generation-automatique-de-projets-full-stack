package com.example.Untitled_6_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Untitled_6_spring.service.EquipeService;
import com.example.Untitled_6_spring.dto.EquipeDto;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    @Autowired
    private EquipeService service;

    @PostMapping
    public EquipeDto add(@RequestBody EquipeDto dto) {
        return service.addEquipe(dto);
    }

    @PutMapping
    public EquipeDto update(@RequestBody EquipeDto dto) {
        return service.updateEquipe(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteEquipe(id);
    }

    @GetMapping("/{id}")
    public Optional<EquipeDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<EquipeDto> getAll() {
        return service.getAll();
    }
}
