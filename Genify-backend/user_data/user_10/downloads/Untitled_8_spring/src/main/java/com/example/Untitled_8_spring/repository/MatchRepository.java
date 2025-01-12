package com.example.Untitled_8_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Untitled_8_spring.models.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
