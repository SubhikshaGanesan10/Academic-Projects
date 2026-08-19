package com.ecinemax.repository;

import com.ecinemax.entity.Movie;
import com.ecinemax.entity.MovieStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Extending JpaRepository gives us save/findById/findAll/delete/count etc.
// for free - no implementation code needed, Spring generates it at runtime.
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Spring Data JPA parses this method name and builds the query
    // "SELECT * FROM movies WHERE status = ?" automatically.
    List<Movie> findByStatus(MovieStatus status);
}
