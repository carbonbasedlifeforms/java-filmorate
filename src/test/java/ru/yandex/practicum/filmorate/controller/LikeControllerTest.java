package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LikeControllerTest {
    FilmStorage filmStorage;
    UserStorage userStorage;
    FilmService filmService;
    LikeService likeService;
    FilmController filmController;
    LikeController likeController;
    Film film;
    Film createdFilm;
    Film anotherFilm;
    User user;
    User createdUser;
    User anotherUser;
    LocalDate filmDate = LocalDate.now();
    LocalDate birthday = LocalDate.now().minusYears(45);

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
        likeService = new LikeService(filmStorage, userStorage);
        likeController = new LikeController(likeService);

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

        user = new User();
        user.setName("testName");
        user.setEmail("test@test.com");
        user.setLogin("testLogin");
        user.setBirthday(birthday);

        anotherUser = new User();
        anotherUser.setEmail("anotherTest@test.com");
        anotherUser.setLogin("anotherTestLogin");
        anotherUser.setBirthday(birthday);
    }

    @Test
    void addLike() {
        createdFilm = filmController.createFilm(film);
        createdUser = userStorage.createUser(user);
        assertTrue(likeController.getPopularFilms(1).isEmpty());
        likeController.addLike(createdFilm.getId(), createdUser.getId());
        assertEquals(createdFilm, likeController.getPopularFilms(1).get(0));
    }

    @Test
    void getPopularFilms() {
        createdFilm = filmController.createFilm(film);
        createdUser = userStorage.createUser(user);
        anotherUser = userStorage.createUser(anotherUser);
        anotherFilm = filmController.createFilm(anotherFilm);
        assertTrue(likeController.getPopularFilms(1).isEmpty());
        likeController.addLike(createdFilm.getId(), createdUser.getId());
        assertEquals(createdFilm, likeController.getPopularFilms(1).get(0));
        likeController.addLike(anotherFilm.getId(), createdUser.getId());
        likeController.addLike(anotherFilm.getId(), anotherUser.getId());
        assertEquals(anotherFilm, likeController.getPopularFilms(1).get(0));
        likeController.deleteLike(anotherFilm.getId(), createdUser.getId());
        likeController.deleteLike(anotherFilm.getId(), anotherUser.getId());
        assertEquals(createdFilm, likeController.getPopularFilms(1).get(0));
    }
}