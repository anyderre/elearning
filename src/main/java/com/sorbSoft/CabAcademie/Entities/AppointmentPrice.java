package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name="appointment_price")
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AppointmentPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "minutes")
    private Integer minutes;

    @Column(name = "price_cents")
    private Long price;

    @Column(name = "currency")
    private String currency;
}
