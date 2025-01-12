package com.example.Untitled_1_spring.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class joueur {

    private Integer idJoueur;

    private String nomJoueur;

    private String poste;

    @ManyToOne
    private equipe equipe;

}
