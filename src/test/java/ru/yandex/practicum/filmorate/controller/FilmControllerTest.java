package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {
    private final FilmController filmController;
    private final JdbcTemplate jdbcTemplate;
    Film film;
    Film createdFilm;
    Film anotherFilm;
    Mpa mpa;
    Genre genre;
    LocalDate filmDate = LocalDate.now();

    @BeforeEach
    void setUp() {
        deleteFilms();
        mpa = new Mpa(1, "testMpa");
        genre = new Genre();
        genre.setId(1);
        genre.setName("testGenre");

        film = new Film();
        film.setName("testFilm");
        film.setDescription("testDescription");
        film.setReleaseDate(filmDate);
        film.setDuration(100);
        film.setMpa(mpa);
        film.setGenres(List.of(genre));

        anotherFilm = new Film();
        anotherFilm.setName("testAnotherFilm");
        anotherFilm.setDescription("testAnotherDescription");
        anotherFilm.setReleaseDate(filmDate);
        anotherFilm.setDuration(150);
        anotherFilm.setMpa(mpa);
        anotherFilm.setGenres(List.of(genre));
    }

    @Test
    void getFilms() {
        filmController.createFilm(film);
        filmController.createFilm(anotherFilm);
        assertEquals(2, filmController.getFilms().size());
    }

    @Test
    void createFilm() {
        createdFilm = filmController.createFilm(film);
        assertEquals(film.getId(), createdFilm.getId());
        assertTrue(filmController.getFilms().contains(createdFilm));
    }

    @Test
    void updateFilm() {
        filmController.createFilm(film);
        film.setName("testUpdatedFilm");
        film.setDescription("testUpdatedDescription");
        film.setReleaseDate(filmDate.minusDays(1));
        film.setDuration(123);
        filmController.updateFilm(film);
        createdFilm = filmController.getFilms().stream().findFirst().get();
        assertEquals("testUpdatedFilm", createdFilm.getName());
        assertEquals("testUpdatedDescription", createdFilm.getDescription());
        assertEquals(filmDate.minusDays(1), createdFilm.getReleaseDate());
        assertEquals(123, createdFilm.getDuration());
    }

    private void deleteFilms() {
        jdbcTemplate.update("delete from film_genres");
        jdbcTemplate.update("delete from films");
    }
}
