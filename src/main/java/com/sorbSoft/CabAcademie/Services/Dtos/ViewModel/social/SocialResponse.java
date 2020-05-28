package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.social;

import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import lombok.Data;

@Data
public class SocialResponse {

    private UserViewModel user;
    private String token;
}
