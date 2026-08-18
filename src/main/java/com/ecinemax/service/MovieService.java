package com.ecinemax.service;

import com.ecinemax.dto.MovieDto;
import com.ecinemax.dto.ShowtimeDto;
import com.ecinemax.entity.Movie;
import com.ecinemax.entity.MovieStatus;
import com.ecinemax.entity.Showtime;
import com.ecinemax.repository.MovieRepository;
import com.ecinemax.repository.ShowtimeRepository;
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
