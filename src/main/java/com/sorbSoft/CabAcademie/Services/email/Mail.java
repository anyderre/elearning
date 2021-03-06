package com.sorbSoft.CabAcademie.Services.email;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Date;

@Getter @Setter
public class Mail {

    private String mailFrom;

    private String mailTo;

    private String mailCc;

    private String mailBcc;

    private String mailSubject;

    private String mailContent;

    private String templateName;

    private String contentType;

    private File iCal;

    public Mail() {
        contentType = "text/html";
    }
}
