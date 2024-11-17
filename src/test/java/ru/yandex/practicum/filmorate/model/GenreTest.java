package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {
    Genre genre;
    Genre otherGenre;

    @BeforeEach
    void setUp() {
        genre = new Genre();
        genre.setId(1);
        genre.setName("Comedy");
        otherGenre = new Genre();
        otherGenre.setId(2);
        otherGenre.setName("Drama");
    }

    @Test
    void getId() {
        assertEquals(1, genre.getId());
    }

    @Test
    void getName() {
        assertEquals("Comedy", genre.getName());
    }

    @Test
    void setId() {
        genre.setId(3);
        assertEquals(3, genre.getId());
    }

    @Test
    void setName() {
        genre.setName("Thriller");
        assertEquals("Thriller", genre.getName());
    }

    @Test
    void testEquals() {
        otherGenre.setId(1);
        otherGenre.setName("Comedy");
        assertEquals(genre, otherGenre);
    }

    @Test
    void canEqual() {
        otherGenre.setId(1);
        otherGenre.setName("Comedy");
        assertTrue(genre.canEqual(otherGenre));
    }

    @Test
    void testHashCode() {
        otherGenre.setId(1);
        otherGenre.setName("Comedy");
        assertEquals(genre.hashCode(), otherGenre.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Genre(id=1, name=Comedy)", genre.toString());
    }
}