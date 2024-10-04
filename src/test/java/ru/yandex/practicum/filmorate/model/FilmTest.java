package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmTest {
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
        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("name");
        film2.setDescription("description");
        film2.setReleaseDate(localDate);
        film2.setDuration(100);
        assertEquals(film, film2);
    }

    @Test
    void testHashCode() {
        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("name");
        film2.setDescription("description");
        film2.setReleaseDate(localDate);
        film2.setDuration(100);
        assertEquals(film.hashCode(), film2.hashCode());
    }

    @Test
    void testToString() {
        film.setId(1L);
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(localDate);
        film.setDuration(100);
        assertEquals("Film(id=1, name=name, description=description, releaseDate="
                + localDate.toString() + ", duration=100)", film.toString());
    }
}