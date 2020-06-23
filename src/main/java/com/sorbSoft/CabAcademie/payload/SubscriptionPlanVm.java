package com.sorbSoft.CabAcademie.payload;

import com.sorbSoft.CabAcademie.Entities.Enums.OrganizationType;
import com.sorbSoft.CabAcademie.Entities.Enums.SubscriptionPlanLevel;
import lombok.Data;

import java.util.Date;

@Data
public class SubscriptionPlanVm {

    private Long id;

    private String name;

    private OrganizationType type;

    private SubscriptionPlanLevel level;

    private Long maxCoursesPerProfessor;

    private Long maxProfessors;

    private Long maxStudents;

    private Long maxAdmins;

    private Boolean isVideoConferenceAllowed;

    private Double price;

    private String currency;

    private Boolean isActive;

    private Date createdDate;

    private Date updatedDate;

}
