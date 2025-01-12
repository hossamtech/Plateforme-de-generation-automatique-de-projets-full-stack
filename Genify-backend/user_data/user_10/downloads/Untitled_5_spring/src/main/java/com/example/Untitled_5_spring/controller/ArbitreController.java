package com.example.Untitled_5_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Untitled_5_spring.service.ArbitreService;
import com.example.Untitled_5_spring.dto.ArbitreDto;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/arbitres")
public class ArbitreController {

    @Autowired
    private ArbitreService service;

    @PostMapping
    public ArbitreDto add(@RequestBody ArbitreDto dto) {
        return service.addArbitre(dto);
    }

    @PutMapping
    public ArbitreDto update(@RequestBody ArbitreDto dto) {
        return service.updateArbitre(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteArbitre(id);
    }

    @GetMapping("/{id}")
    public Optional<ArbitreDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ArbitreDto> getAll() {
        return service.getAll();
    }
}
