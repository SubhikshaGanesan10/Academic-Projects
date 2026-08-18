package com.ecinemax.repository;

import com.ecinemax.entity.Booking;
import com.ecinemax.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserIdOrderByBookingDateTimeDesc(Long userId);

    List<Booking> findByStatusAndBookingDateTimeBefore(BookingStatus status, LocalDateTime cutoff);
}
