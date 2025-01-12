package com.example.Untitled_1_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Untitled_1_spring.service.equipeService;
import com.example.Untitled_1_spring.dto.equipeDto;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/equipes")
public class equipeController {

    @Autowired
    private equipeService service;

    @PostMapping
    public equipeDto add(@RequestBody equipeDto dto) {
        return service.addequipe(dto);
    }

    @PutMapping
    public equipeDto update(@RequestBody equipeDto dto) {
        return service.updateequipe(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteequipe(id);
    }

    @GetMapping("/{id}")
    public Optional<equipeDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<equipeDto> getAll() {
        return service.getAll();
    }
}
