package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    LocalDate releaseDate = LocalDate.now().minusYears(5);
    LocalDate birthDay = LocalDate.now().minusYears(20);

    Film film;
    User user;
    User otherUser;

    @BeforeEach
    void setUp() {
        cleanUp();
        film = new Film();
        film.setName("Test Film");
        film.setDescription("Test film description");
        film.setReleaseDate(releaseDate);
        film.setDuration(100);
        film.setMpa(mpaDbStorage.getMpaById(1));
        film.setGenres(genreDbStorage.getGenres());
        film = filmDbStorage.createFilm(film);

        user = new User();
        user.setName("Test Testovich");
        user.setLogin("test");
        user.setEmail("test@test.org");
        user.setBirthday(birthDay);
        user = userDbStorage.createUser(user);

        otherUser = new User();
        otherUser.setName("Test Petrovich");
        otherUser.setLogin("otherTest");
        otherUser.setEmail("otherTest@test.org");
        otherUser.setBirthday(birthDay);
        otherUser = userDbStorage.createUser(otherUser);
    }

    @Test
    void getFilmById() {
        assertEquals(film, filmDbStorage.getFilmById(film.getId()));
    }

    @Test
    void getFilms() {
        assertEquals(1, filmDbStorage.getFilms().size());
    }

    @Test
    void createFilm() {
        assertThat(film)
                .hasFieldOrPropertyWithValue("id", film.getId())
                        .hasFieldOrPropertyWithValue("name", "Test Film")
                        .hasFieldOrPropertyWithValue("description", "Test film description")
                        .hasFieldOrPropertyWithValue("releaseDate", releaseDate)
                        .hasFieldOrPropertyWithValue("duration", 100)
                        .hasFieldOrPropertyWithValue("mpa", mpaDbStorage.getMpaById(1))
                        .hasFieldOrPropertyWithValue("genres", genreDbStorage.getGenres());
    }

    @Test
    void updateFilm() {
        film.setName("Updated Test Film");
        film.setDescription("Updated film description");
        film.setDuration(200);
        filmDbStorage.updateFilm(film);
        assertThat(film)
                .hasFieldOrPropertyWithValue("name", "Updated Test Film")
                .hasFieldOrPropertyWithValue("description", "Updated film description")
                .hasFieldOrPropertyWithValue("duration", 200);
    }

    @Test
    void addLike() {
        filmDbStorage.addLike(film, user);
        assertThat(filmDbStorage.getPopularFilms(1))
                .hasSize(1)
                .contains(film);
    }

    @Test
    void deleteLike() {
        filmDbStorage.addLike(film, user);
        assertThat(filmDbStorage.getPopularFilms(1))
                .hasSize(1)
                .contains(film);
        filmDbStorage.deleteLike(film, user);
        assertEquals(0, filmDbStorage.getPopularFilms(1).size());
    }

    @Test
    void getPopularFilms() {
        assertEquals(0, filmDbStorage.getPopularFilms(1).size());
        filmDbStorage.addLike(film, user);
        assertThat(filmDbStorage.getPopularFilms(1))
                .hasSize(1)
                .contains(film);
        filmDbStorage.addLike(film, otherUser);
    }

    private void cleanUp() {
        jdbcTemplate.update("delete from film_genres");
        jdbcTemplate.update("delete from film_likes");
        jdbcTemplate.update("delete from films");
        jdbcTemplate.update("delete from users");
    }
}