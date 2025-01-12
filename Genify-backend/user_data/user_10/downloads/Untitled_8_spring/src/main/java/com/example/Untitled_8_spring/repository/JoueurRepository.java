package com.example.Untitled_8_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Untitled_8_spring.models.Joueur;

public interface JoueurRepository extends JpaRepository<Joueur, Long> {
}
