package com.ecinemax.repository;

import com.ecinemax.entity.ShowtimeSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShowtimeSeatRepository extends JpaRepository<ShowtimeSeat, Long> {

    // The underscores mark where to traverse into a related entity's fields
    // (seat.rowLabel, seat.seatNumber) - avoids ambiguity in how Spring Data
    // would otherwise have to guess where property names split.
    List<ShowtimeSeat> findByShowtimeIdOrderBySeat_RowLabelAscSeat_SeatNumberAsc(Long showtimeId);

    Optional<ShowtimeSeat> findByShowtimeIdAndSeatId(Long showtimeId, Long seatId);

    List<ShowtimeSeat> findByBookingId(Long bookingId);
}
