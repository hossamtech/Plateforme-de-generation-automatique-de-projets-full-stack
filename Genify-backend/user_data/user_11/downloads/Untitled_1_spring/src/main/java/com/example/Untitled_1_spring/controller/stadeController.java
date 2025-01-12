package com.example.Untitled_1_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Untitled_1_spring.service.stadeService;
import com.example.Untitled_1_spring.dto.stadeDto;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stades")
public class stadeController {

    @Autowired
    private stadeService service;

    @PostMapping
    public stadeDto add(@RequestBody stadeDto dto) {
        return service.addstade(dto);
    }

    @PutMapping
    public stadeDto update(@RequestBody stadeDto dto) {
        return service.updatestade(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletestade(id);
    }

    @GetMapping("/{id}")
    public Optional<stadeDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<stadeDto> getAll() {
        return service.getAll();
    }
}
