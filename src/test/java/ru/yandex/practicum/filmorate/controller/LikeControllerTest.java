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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikeControllerTest {

    private final FilmController filmController;
    private final LikeController likeController;
    private final UserController userController;
    private final JdbcTemplate jdbcTemplate;

    Mpa mpa;
    Genre genre;
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
        createdUser = userController.createUser(user);
        assertTrue(likeController.getPopularFilms(1).isEmpty());
        likeController.addLike(createdFilm.getId(), createdUser.getId());
        assertEquals(createdFilm, likeController.getPopularFilms(1).get(0));
    }

    @Test
    void deleteLike() {
        createdFilm = filmController.createFilm(film);
        createdUser = userController.createUser(user);
        assertTrue(likeController.getPopularFilms(1).isEmpty());
        likeController.addLike(createdFilm.getId(), createdUser.getId());
        assertEquals(createdFilm, likeController.getPopularFilms(1).getFirst());
        likeController.deleteLike(createdFilm.getId(), createdUser.getId());
        assertTrue(likeController.getPopularFilms(1).isEmpty());
    }

    @Test
    void getPopularFilms() {
        createdFilm = filmController.createFilm(film);
        createdUser = userController.createUser(user);
        anotherUser = userController.createUser(anotherUser);
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

    private void deleteFilms() {
        jdbcTemplate.update("delete from film_genres");
        jdbcTemplate.update("delete from film_likes");
        jdbcTemplate.update("delete from films");
    }
}