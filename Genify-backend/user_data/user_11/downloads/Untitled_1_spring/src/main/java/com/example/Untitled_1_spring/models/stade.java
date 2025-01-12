package com.example.Untitled_1_spring.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class stade {

    private Integer idStade;

    private String nomStade;

    private String ville;

    @OneToMany(mappedBy = "null")
    private List<match> matchs;

}
