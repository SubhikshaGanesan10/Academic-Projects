package com.ecinemax.controller;

import com.ecinemax.dto.MovieDto;
import com.ecinemax.dto.MovieRequest;
import com.ecinemax.service.MovieService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Everything under /api/admin/** requires ROLE_ADMIN - enforced centrally in
// SecurityConfig, not repeated in every controller method here.
@RestController
@RequestMapping("/api/admin/movies")
public class AdminMovieController {

    private final MovieService movieService;

    public AdminMovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public MovieDto createMovie(@RequestBody MovieRequest request) {
        return movieService.createMovie(request);
    }

    @PutMapping("/{id}")
    public MovieDto updateMovie(@PathVariable Long id, @RequestBody MovieRequest request) {
        return movieService.updateMovie(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }
}
