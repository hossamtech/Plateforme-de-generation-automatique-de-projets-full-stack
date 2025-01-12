package com.example.Untitled_1_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Untitled_1_spring.service.matchService;
import com.example.Untitled_1_spring.dto.matchDto;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matchs")
public class matchController {

    @Autowired
    private matchService service;

    @PostMapping
    public matchDto add(@RequestBody matchDto dto) {
        return service.addmatch(dto);
    }

    @PutMapping
    public matchDto update(@RequestBody matchDto dto) {
        return service.updatematch(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletematch(id);
    }

    @GetMapping("/{id}")
    public Optional<matchDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<matchDto> getAll() {
        return service.getAll();
    }
}
