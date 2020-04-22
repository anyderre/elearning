package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class AppointmentSlotViewModel {

    private Long id;

    private Long userId;

    private Date from;

    private Date to;

}
