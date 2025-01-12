package com.example.Untitled_1_spring.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class match {

    private Integer idMatch;

    private Date dateMatch;

    private Date heureMatch;

    @ManyToOne
    private arbitre arbitre;

    @ManyToMany
    private List<equipe> equipes;

    @ManyToOne
    private stade stade;

}
