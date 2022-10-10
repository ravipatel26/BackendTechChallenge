package com.coding.challenge.booking.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "test")
@Data
public class BookingEntity {

    @Id
    private Long id;
}
