package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment;

import com.sorbSoft.CabAcademie.Entities.AppointmentPrice;
import com.sorbSoft.CabAcademie.Entities.Attendee;
import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentType;
import com.sorbSoft.CabAcademie.Entities.Enums.TimeSlotStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SlotsResponseModel {

    private Long timeSlotId;

    private Long appointmentId;

    private Long teacherId;

    private List<AttendeeModel> attendees;

    private Date dateFrom;

    private Date dateTo;

    private List<AppointmentPrice> prices;

    private Long minMinutes;

    private Long maxAttendees;

    private Long enrolledAttendees;

    private Integer bookBeforeMinutes;

    private TimeSlotStatus status;

    private AppointmentType type;

}
