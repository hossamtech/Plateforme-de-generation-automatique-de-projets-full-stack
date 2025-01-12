package com.example.Untitled_6_spring.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idstade;

    private String nomstade;

    private String ville;

    @OneToMany(mappedBy = "stade")
    private List<Match> matchs;

}
