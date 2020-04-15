package com.sorbSoft.CabAcademie.Services.Dtos.Info;

import lombok.Data;

@Data
public class RolInfo {
    private Long id;
    private String name;
    private String description;
    public boolean generated;
}

