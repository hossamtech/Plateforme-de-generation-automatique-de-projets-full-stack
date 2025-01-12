package com.example.Untitled_5_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Untitled_5_spring.service.StadeService;
import com.example.Untitled_5_spring.dto.StadeDto;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stades")
public class StadeController {

    @Autowired
    private StadeService service;

    @PostMapping
    public StadeDto add(@RequestBody StadeDto dto) {
        return service.addStade(dto);
    }

    @PutMapping
    public StadeDto update(@RequestBody StadeDto dto) {
        return service.updateStade(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteStade(id);
    }

    @GetMapping("/{id}")
    public Optional<StadeDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<StadeDto> getAll() {
        return service.getAll();
    }
}
