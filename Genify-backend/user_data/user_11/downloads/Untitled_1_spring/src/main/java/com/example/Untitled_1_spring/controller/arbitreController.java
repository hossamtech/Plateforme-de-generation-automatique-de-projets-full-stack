package com.example.Untitled_1_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Untitled_1_spring.service.arbitreService;
import com.example.Untitled_1_spring.dto.arbitreDto;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/arbitres")
public class arbitreController {

    @Autowired
    private arbitreService service;

    @PostMapping
    public arbitreDto add(@RequestBody arbitreDto dto) {
        return service.addarbitre(dto);
    }

    @PutMapping
    public arbitreDto update(@RequestBody arbitreDto dto) {
        return service.updatearbitre(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletearbitre(id);
    }

    @GetMapping("/{id}")
    public Optional<arbitreDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<arbitreDto> getAll() {
        return service.getAll();
    }
}
