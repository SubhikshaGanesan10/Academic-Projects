package com.ecinemax.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

// One row per movie in the catalog. Mirrors the fields already collected by
// the existing addmovies.html form.
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private Integer durationMinutes;
    private LocalDate releaseDate;
    private String cast;
    private String director;
    private String producer;

    @Column(length = 2000)
    private String description;

    private Double rating;

    // Just the YouTube video ID (e.g. "eHp3MbsCbMg"), not a full URL - the
    // frontend builds both a youtu.be link and an /embed/ link from it.
    private String youtubeTrailerId;
    private String posterUrl;

    @Enumerated(EnumType.STRING)
    private MovieStatus status;

    // JPA requires a no-argument constructor so Hibernate can create instances
    // when loading rows from the database.
    public Movie() {
    }

    public Movie(String title, String genre, Integer durationMinutes, LocalDate releaseDate,
                 String cast, String director, String producer, String description,
                 Double rating, String youtubeTrailerId, String posterUrl, MovieStatus status) {
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.releaseDate = releaseDate;
        this.cast = cast;
        this.director = director;
        this.producer = producer;
        this.description = description;
        this.rating = rating;
        this.youtubeTrailerId = youtubeTrailerId;
        this.posterUrl = posterUrl;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getYoutubeTrailerId() {
        return youtubeTrailerId;
    }

    public void setYoutubeTrailerId(String youtubeTrailerId) {
        this.youtubeTrailerId = youtubeTrailerId;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public MovieStatus getStatus() {
        return status;
    }

    public void setStatus(MovieStatus status) {
        this.status = status;
    }
}
