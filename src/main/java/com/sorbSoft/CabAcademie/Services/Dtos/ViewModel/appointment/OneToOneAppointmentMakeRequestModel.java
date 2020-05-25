package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment;

import lombok.Data;

import java.util.Date;

@Data
public class OneToOneAppointmentMakeRequestModel {

    private Long id;

    private Long teacherId;

    private Long studentId;

    private Date dateFrom;

    private Date dateTo;

    private Long priceId;

}
