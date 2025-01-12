package com.example.Untitled_7_spring.dto;

import java.util.Date;
import lombok.Data;

@Data
public class MatchDto {

    private Long idmatch;
    private Date datematch;
    private Date heurematch;
    private Long arbitreId;
    private Long stadeId;
    private Long equipe_1Id;
    private Long equipe_2Id;
}
