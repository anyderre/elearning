package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment;

import com.sorbSoft.CabAcademie.Entities.AppointmentPrice;
import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SlotAddRequestModel {

    private Long id;

    private Long teacherId;

    private Date dateFrom;

    private Date dateTo;

    private List<AppointmentPrice> prices;

    private Long maxAttendees;

    private AppointmentType timeSlotType;

    private Long minMinutes;

    private Integer bookBeforeMinutes;

}
