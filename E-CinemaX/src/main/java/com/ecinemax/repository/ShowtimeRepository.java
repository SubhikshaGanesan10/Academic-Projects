package com.ecinemax.repository;

import com.ecinemax.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    // Builds "SELECT * FROM showtimes WHERE movie_id = ? ORDER BY show_date, show_time"
    List<Showtime> findByMovieIdOrderByShowDateAscShowTimeAsc(Long movieId);
}
