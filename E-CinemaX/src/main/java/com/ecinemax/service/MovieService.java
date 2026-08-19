package com.ecinemax.service;

import com.ecinemax.dto.MovieDto;
import com.ecinemax.dto.MovieRequest;
import com.ecinemax.dto.ShowtimeDto;
import com.ecinemax.entity.Movie;
import com.ecinemax.entity.MovieStatus;
import com.ecinemax.entity.Showtime;
import com.ecinemax.repository.MovieRepository;
import com.ecinemax.repository.ShowtimeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;

    public MovieService(MovieRepository movieRepository, ShowtimeRepository showtimeRepository) {
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
    }

    public List<MovieDto> getMovies(MovieStatus status) {
        List<Movie> movies = (status != null)
                ? movieRepository.findByStatus(status)
                : movieRepository.findAll();

        return movies.stream().map(this::toDto).toList();
    }

    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found: " + id));
        return toDto(movie);
    }

    public List<ShowtimeDto> getShowtimesForMovie(Long movieId) {
        // 404 if the movie itself doesn't exist, so callers get a clear error
        // instead of silently getting an empty showtimes list either way.
        if (!movieRepository.existsById(movieId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found: " + movieId);
        }

        List<Showtime> showtimes = showtimeRepository.findByMovieIdOrderByShowDateAscShowTimeAsc(movieId);
        return showtimes.stream()
                .map(st -> new ShowtimeDto(st.getId(), st.getShowDate(), st.getShowTime()))
                .toList();
    }

    public MovieDto createMovie(MovieRequest request) {
        Movie movie = new Movie();
        applyRequest(movie, request);
        movieRepository.save(movie);
        return toDto(movie);
    }

    public MovieDto updateMovie(Long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found: " + id));
        applyRequest(movie, request);
        movieRepository.save(movie);
        return toDto(movie);
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found: " + id);
        }
        try {
            movieRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // The movie has showtimes (and possibly bookings) referencing it -
            // refuse rather than let a raw database error reach the admin.
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete a movie that has scheduled showtimes");
        }
    }

    private void applyRequest(Movie movie, MovieRequest request) {
        movie.setTitle(request.getTitle());
        movie.setGenre(request.getGenre());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setCast(request.getCast());
        movie.setDirector(request.getDirector());
        movie.setProducer(request.getProducer());
        movie.setDescription(request.getDescription());
        movie.setRating(request.getRating());
        movie.setYoutubeTrailerId(request.getYoutubeTrailerId());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setStatus(request.getStatus());
    }

    private MovieDto toDto(Movie movie) {
        MovieDto dto = new MovieDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setGenre(movie.getGenre());
        dto.setDurationMinutes(movie.getDurationMinutes());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setCast(movie.getCast());
        dto.setDirector(movie.getDirector());
        dto.setProducer(movie.getProducer());
        dto.setDescription(movie.getDescription());
        dto.setRating(movie.getRating());
        dto.setYoutubeTrailerId(movie.getYoutubeTrailerId());
        dto.setPosterUrl(movie.getPosterUrl());
        dto.setStatus(movie.getStatus());
        return dto;
    }
}
