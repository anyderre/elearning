package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment;

import com.sorbSoft.CabAcademie.Entities.Enums.AttendeeStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AttendeeModel {

    private Long userId;

    private String firstName;

    private String lastName;

    private String username;

    private String photoURL;

    private Long timeSlotId;

    private AttendeeStatus status;

}
