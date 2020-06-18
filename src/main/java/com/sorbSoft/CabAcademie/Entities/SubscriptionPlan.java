package com.sorbSoft.CabAcademie.Entities;

import com.sorbSoft.CabAcademie.Entities.Enums.SubscriptionPlanLevel;
import com.sorbSoft.CabAcademie.Entities.Enums.OrganizationType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "subscription_plan")
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
