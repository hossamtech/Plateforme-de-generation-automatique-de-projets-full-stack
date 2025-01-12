package com.example.Untitled_9_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Untitled_9_spring.service.JoueurService;
import com.example.Untitled_9_spring.dto.JoueurDto;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/joueurs")
public class JoueurController {

    @Autowired
    private JoueurService service;

    @PostMapping
    public JoueurDto add(@RequestBody JoueurDto dto) {
        return service.addJoueur(dto);
    }

    @PutMapping
    public JoueurDto update(@RequestBody JoueurDto dto) {
        return service.updateJoueur(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteJoueur(id);
    }

    @GetMapping("/{id}")
    public Optional<JoueurDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<JoueurDto> getAll() {
        return service.getAll();
    }
}
