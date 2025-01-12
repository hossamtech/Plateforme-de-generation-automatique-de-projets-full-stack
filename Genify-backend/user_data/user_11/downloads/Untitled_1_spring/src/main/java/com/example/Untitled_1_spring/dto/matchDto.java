package com.example.Untitled_1_spring.dto;

import java.util.Date;
import lombok.Data;

@Data
public class matchDto {

    private Integer idMatch;
    private Date dateMatch;
    private Date heureMatch;
    private Long arbitreId;
    private Long stadeId;
}
