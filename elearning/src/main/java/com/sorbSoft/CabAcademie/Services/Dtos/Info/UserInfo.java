package com.sorbSoft.CabAcademie.Services.Dtos.Info;

import lombok.Data;
@Data
public class UserInfo {
    private Long id;
    private String name;
    private String username;
    private boolean professor;
    private boolean admin;
}
