package com.sorbSoft.CabAcademie.payload;

import lombok.Data;

@Data
public class ChangeBatchPasswordRequest {

    private String newPassword;

    private String confirmPassword;
}
