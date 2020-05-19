package com.sorbSoft.CabAcademie.Entities;

import com.sorbSoft.CabAcademie.Entities.Enums.AttendeeStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "attendees")
@Getter @Setter
public class Attendee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @ManyToOne
    private TimeSlot timeSlot;

    @Enumerated(EnumType.STRING)
    private AttendeeStatus status;

    @Column(name = "approval_uid")
    private String approvalUid;

    @Column(name = "decline_uid")
    private String declineUid;

}
