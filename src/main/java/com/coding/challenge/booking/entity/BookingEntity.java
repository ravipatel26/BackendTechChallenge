package com.coding.challenge.booking.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "booking")
@Data
public class BookingEntity {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Version
    private Long version;
}
