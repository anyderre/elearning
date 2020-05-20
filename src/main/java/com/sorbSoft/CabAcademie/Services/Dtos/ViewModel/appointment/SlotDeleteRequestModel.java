package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment;

import com.sorbSoft.CabAcademie.Entities.AppointmentPrice;
import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SlotDeleteRequestModel {

    private Long teacherId;

    private Date dateFrom;

    private Date dateTo;

}
