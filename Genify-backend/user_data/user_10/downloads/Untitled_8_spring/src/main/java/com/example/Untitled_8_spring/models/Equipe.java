package com.example.Untitled_8_spring.models;

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

    @ManyToMany(mappedBy = "equipe")
    private List<Match> matchs;

    @OneToMany(mappedBy = "equipe")
    private List<Joueur> joueurs;

}
