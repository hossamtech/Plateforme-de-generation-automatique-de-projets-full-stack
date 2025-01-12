package com.example.Untitled_9_spring.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idmatch;

    private Date datematch;

    private Date heurematch;

    @ManyToOne
    private Arbitre arbitre;

    @ManyToOne
    private Equipe equipe1;
    @ManyToOne
    private Equipe equipe2;

    @ManyToOne
    private Stade stade;

}
