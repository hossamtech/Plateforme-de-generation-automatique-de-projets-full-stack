package com.example.Untitled_6_spring.dto;

import java.util.Date;
import lombok.Data;

@Data
public class MatchDto {

    private Long idmatch;
    private Date datematch;
    private Date heurematch;
    private Long Idarbitre;
    private Long Idstade;
    private Long equipe1Id;
    private Long equipe2Id;
}
