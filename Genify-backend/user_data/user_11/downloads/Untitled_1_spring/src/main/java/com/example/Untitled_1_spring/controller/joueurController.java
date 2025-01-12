package com.example.Untitled_1_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Untitled_1_spring.service.joueurService;
import com.example.Untitled_1_spring.dto.joueurDto;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/joueurs")
public class joueurController {

    @Autowired
    private joueurService service;

    @PostMapping
    public joueurDto add(@RequestBody joueurDto dto) {
        return service.addjoueur(dto);
    }

    @PutMapping
    public joueurDto update(@RequestBody joueurDto dto) {
        return service.updatejoueur(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletejoueur(id);
    }

    @GetMapping("/{id}")
    public Optional<joueurDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<joueurDto> getAll() {
        return service.getAll();
    }
}
