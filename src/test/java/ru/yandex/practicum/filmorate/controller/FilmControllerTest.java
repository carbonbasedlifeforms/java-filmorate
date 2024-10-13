package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmControllerTest {
    FilmController filmController;
    Film film;
    Film createdFilm;
    Film anotherFilm;
    LocalDate filmDate = LocalDate.now();

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film = new Film();
        film.setName("testFilm");
        film.setDescription("testDescription");
        film.setReleaseDate(filmDate);
        film.setDuration(100);
        anotherFilm = new Film();
        anotherFilm.setName("testAnotherFilm");
        anotherFilm.setDescription("testAnotherDescription");
        anotherFilm.setReleaseDate(filmDate);
        anotherFilm.setDuration(150);
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
        assertEquals(1, film.getId());
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
}
