package com.example.Untitled_7_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Untitled_7_spring.models.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
