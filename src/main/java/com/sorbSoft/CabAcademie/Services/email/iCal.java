package com.sorbSoft.CabAcademie.Services.email;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class iCal {

    private Date start;

    private Date end;

    private String summary;

    private String url;

    private String description;

    private String timeZone;

    private String organizer;

    private String attendee;

    private String method;
}
