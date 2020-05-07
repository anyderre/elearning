package com.sorbSoft.CabAcademie.Entities;

import com.sorbSoft.CabAcademie.Entities.Enums.TimeSlotStatus;
import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentType;
import com.sorbSoft.CabAcademie.Entities.Enums.TimeSlotType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="appointment_time_slots")
@Getter @Setter
public class TimeSlot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Attendee> attendees;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_from")
    private Date dateFrom;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_to")
    private Date dateTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TimeSlotStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AppointmentType type;

    @Column(name = "max_attendees")
    private Long maxAttendees;

    @Column(name = "approved_attendees")
    private Long approvedAttendee;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentPrice> prices;

    @Column(name = "min_minutes")
    private Long minMinutes;

    //@Column(name = "agreed_price")
    @OneToOne
    private AppointmentPrice agreedPrice;

    //@Column(name = "suggested_price")
    @OneToOne
    private AppointmentPrice suggestedPrice;

    @Column(name = "book_before_minutes")
    private Integer bookBeforeMinutes;

}
