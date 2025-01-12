package com.example.Untitled_1_spring.dto;

import lombok.Data;

@Data
public class joueurDto {

    private Integer idJoueur;
    private String nomJoueur;
    private String poste;
    private Long equipeId;
}
