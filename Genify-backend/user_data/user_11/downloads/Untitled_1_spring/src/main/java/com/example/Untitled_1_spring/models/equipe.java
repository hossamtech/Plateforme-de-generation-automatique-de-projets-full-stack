package com.example.Untitled_1_spring.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class equipe {

    private Integer idEquipe;

    private String nomEquipe;

    private String pays;

    @ManyToMany
    private List<match> matchs;

    @OneToMany(mappedBy = "null")
    private List<joueur> joueurs;

}
