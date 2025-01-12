package com.example.Untitled_9_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Untitled_9_spring.service.MatchService;
import com.example.Untitled_9_spring.dto.MatchDto;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matchs")
public class MatchController {

    @Autowired
    private MatchService service;

    @PostMapping
    public MatchDto add(@RequestBody MatchDto dto) {
        return service.addMatch(dto);
    }

    @PutMapping
    public MatchDto update(@RequestBody MatchDto dto) {
        return service.updateMatch(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteMatch(id);
    }

    @GetMapping("/{id}")
    public Optional<MatchDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<MatchDto> getAll() {
        return service.getAll();
    }
}
