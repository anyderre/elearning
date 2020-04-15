package com.sorbSoft.CabAcademie.Services.Dtos.Info;

import lombok.Data;
@Data
public class UserInfo {
    private Long id;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String roleName;
    private Long roleId;
}

