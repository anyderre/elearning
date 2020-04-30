package com.sorbSoft.CabAcademie.Entities;

import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentStatus;
import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentType;
import lombok.*;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
@Table(name="appointment_slots")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AppointmentSlot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_from")
    private Date dateFrom;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_to")
    private Date dateTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AppointmentType type;

}
