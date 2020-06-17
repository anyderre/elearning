package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment;

import com.sorbSoft.CabAcademie.Entities.AppointmentPrice;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SlotsGetRequestModel {

    private Long id;

    private Long teacherId;

    private Date dateFrom;

    private Date dateTo;

    private Long requesterId;

}
