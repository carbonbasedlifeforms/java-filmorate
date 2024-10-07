package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmTest {
    LocalDate localDate = LocalDate.now();
    Film film;
    Film anotherFilm;

    @BeforeEach
    void setUp() {

        film = new Film();
        film.setId(1L);
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(localDate);
        film.setDuration(100);

        anotherFilm = new Film();
        anotherFilm.setId(1L);
        anotherFilm.setName("name");
        anotherFilm.setDescription("description");
        anotherFilm.setReleaseDate(localDate);
        anotherFilm.setDuration(100);

    }

    @Test
    void getId() {
        assertEquals(1L, film.getId());
    }

    @Test
    void getName() {
        assertEquals("name", film.getName());
    }

    @Test
    void getDescription() {
        assertEquals("description", film.getDescription());
    }

    @Test
    void getReleaseDate() {
        assertEquals(localDate, film.getReleaseDate());
    }

    @Test
    void getDuration() {
        assertEquals(100, film.getDuration());
    }

    @Test
    void setId() {
        film.setId(2L);
        assertEquals(2L, film.getId());
    }

    @Test
    void setName() {
        film.setName("nextname");
        assertEquals("nextname", film.getName());
    }

    @Test
    void setDescription() {
        film.setDescription("nextdescription");
        assertEquals("nextdescription", film.getDescription());
    }

    @Test
    void setReleaseDate() {
        film.setReleaseDate(localDate.minusDays(1));
        assertEquals(localDate.minusDays(1), film.getReleaseDate());
    }

    @Test
    void setDuration() {
        film.setDuration(200);
        assertEquals(200, film.getDuration());
    }

    @Test
    void testEquals() {
        assertEquals(film, anotherFilm);
    }

    @Test
    void testHashCode() {
        assertEquals(film.hashCode(), anotherFilm.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Film(id=1, name=name, description=description, releaseDate="
                + localDate.toString() + ", duration=100)", film.toString());
    }
}