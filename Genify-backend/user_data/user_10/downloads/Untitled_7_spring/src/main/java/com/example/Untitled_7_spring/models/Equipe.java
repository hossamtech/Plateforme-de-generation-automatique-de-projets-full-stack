package com.example.Untitled_7_spring.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idequipe;

    private String nomequipe;

    private String pays;

    @OneToMany(mappedBy = "equipe1")
    private List<Match> matchsAsEquipe1;

    @OneToMany(mappedBy = "equipe2")
    private List<Match> matchsAsEquipe2;

    @OneToMany(mappedBy = "equipe")
    private List<Joueur> joueurs;

}
