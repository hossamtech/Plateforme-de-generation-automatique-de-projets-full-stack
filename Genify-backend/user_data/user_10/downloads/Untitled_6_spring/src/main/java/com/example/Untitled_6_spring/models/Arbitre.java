package com.example.Untitled_6_spring.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Arbitre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idarbitre;

    private String nom;

    private String nationalite;

    @OneToMany(mappedBy = "arbitre")
    private List<Match> matchs;

}
