package com.ecinemax.controller;

import com.ecinemax.dto.MovieDto;
import com.ecinemax.dto.ShowtimeDto;
import com.ecinemax.entity.MovieStatus;
import com.ecinemax.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // GET /api/movies                     -> every movie
    // GET /api/movies?status=NOW_SHOWING   -> just the Now Showing ones
    @GetMapping
    public List<MovieDto> getMovies(@RequestParam(required = false) MovieStatus status) {
        return movieService.getMovies(status);
    }

    // GET /api/movies/3
    @GetMapping("/{id}")
    public MovieDto getMovie(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    // GET /api/movies/3/showtimes
    @GetMapping("/{id}/showtimes")
    public List<ShowtimeDto> getShowtimes(@PathVariable Long id) {
        return movieService.getShowtimesForMovie(id);
    }
}
