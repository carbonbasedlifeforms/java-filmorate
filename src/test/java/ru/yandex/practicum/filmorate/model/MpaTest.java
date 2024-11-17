package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MpaTest {
    Mpa mpa;
    Mpa otherMpa;

    @BeforeEach
    void setUp() {
        mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("XXX");

        otherMpa = new Mpa();
        otherMpa.setId(1);
        otherMpa.setName("YYY");
    }

    @Test
    void getId() {
        assertEquals(1, mpa.getId());
    }

    @Test
    void getName() {
        assertEquals("XXX", mpa.getName());
    }

    @Test
    void setId() {
        mpa.setId(2);
        assertEquals(2, mpa.getId());
    }

    @Test
    void setName() {
        mpa.setName("YYY");
        assertEquals("YYY", mpa.getName());
    }

    @Test
    void testEquals() {
        otherMpa.setName("XXX");
        assertEquals(mpa, otherMpa);
    }

    @Test
    void canEqual() {
        otherMpa.setName("XXX");
        assertTrue(mpa.canEqual(otherMpa));
    }

    @Test
    void testHashCode() {
        otherMpa.setName("XXX");
        assertEquals(mpa.hashCode(), otherMpa.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(mpa.toString(), "Mpa(id=1, name=XXX)");
    }
}