package com.example.Untitled_1_spring.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class arbitre {

    private Integer idArbitre;

    private String nom;

    private String Nationalite;

    @OneToMany(mappedBy = "null")
    private List<match> matchs;

}
