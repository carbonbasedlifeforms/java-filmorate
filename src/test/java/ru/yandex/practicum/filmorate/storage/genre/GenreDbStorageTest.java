package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final MpaDbStorage mpaDbStorage;
    LocalDate localDate = LocalDate.now();
    Film film;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setId(1L);
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(localDate);
        film.setDuration(100);
        film.setMpa(mpaDbStorage.getMpaById(1));
        film.setGenres(genreDbStorage.getGenres());
        filmDbStorage.createFilm(film);
    }

    @Test
    void getGenreById() {
        assertEquals(genreDbStorage.getGenreById(1).getId(), 1);
        assertEquals(genreDbStorage.getGenreById(1).getName(), "Комедия");
    }

    @Test
    void getGenres() {
        assertEquals(genreDbStorage.getGenres().size(), 6);
    }

    @Test
    void getFilmGenres() {
        assertEquals(genreDbStorage.getFilmGenres(film.getId()).size(), 6);
    }
}