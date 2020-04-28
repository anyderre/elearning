package com.sorbSoft.CabAcademie.Entities;

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

}
