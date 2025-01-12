package com.example.Untitled_5_spring.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Joueur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idjoueur;

    private String nomjoueur;

    private String poste;

    @ManyToOne
    private Equipe equipe;

}
