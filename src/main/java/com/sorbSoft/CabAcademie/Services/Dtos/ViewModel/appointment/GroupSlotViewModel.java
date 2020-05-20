package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment;

import lombok.Data;

import java.util.Date;

@Data
public class GroupSlotViewModel {

    private Long id;

    private Long teacherId;

    private Date dateFrom;

    private Date dateTo;

    private Double price;

    private Long maxAttendees;

    private Date approvalBefore;

}
