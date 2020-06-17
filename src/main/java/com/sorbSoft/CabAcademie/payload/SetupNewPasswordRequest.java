package com.sorbSoft.CabAcademie.payload;

import lombok.Data;

@Data
public class SetupNewPasswordRequest {

    private String code;

    private String password;

    private String confirmPassword;
}
