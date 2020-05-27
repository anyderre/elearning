package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import lombok.Data;

@Data
public class SocialData {

    private String accessToken;
    private SocialNetwork socialNetwork;
    private String oauthToken;
    private String oauthVerifier;
}
