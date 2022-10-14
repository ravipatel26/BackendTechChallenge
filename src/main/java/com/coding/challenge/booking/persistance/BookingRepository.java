package com.coding.challenge.booking.persistance;

import com.coding.challenge.booking.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("select b from booking b where b.arrivalDate >= ?1 and b.departureDate < ?2")
    List<BookingEntity> getAllBookedDatesBetween(LocalDate startDate, LocalDate endDate);
}
