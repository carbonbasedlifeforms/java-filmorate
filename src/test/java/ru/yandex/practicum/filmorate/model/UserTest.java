package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    User user;
    LocalDate bday = LocalDate.now().minusYears(40);
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setLogin("test");
        user.setName("Test");
        user.setBirthday(bday);
    }

    @Test
    void getId() {
        assertEquals(1L, user.getId());
    }

    @Test
    void getEmail() {
        assertEquals("test@test.com", user.getEmail());
    }

    @Test
    void getLogin() {
        assertEquals("test", user.getLogin());
    }

    @Test
    void getName() {
        assertEquals("Test", user.getName());
    }

    @Test
    void getBirthday() {
        assertEquals(bday, user.getBirthday());
    }

    @Test
    void setId() {
        user.setId(2L);
        assertEquals(2L, user.getId());
    }

    @Test
    void setEmail() {
        user.setEmail("test2@test.com");
        assertEquals("test2@test.com", user.getEmail());
    }

    @Test
    void setLogin() {
        user.setLogin("test2");
        assertEquals("test2", user.getLogin());
    }

    @Test
    void setName() {
        user.setName("Test2");
        assertEquals("Test2", user.getName());
    }

    @Test
    void setBirthday() {
        user.setBirthday(bday.plusYears(1));
        assertEquals(bday.plusYears(1), user.getBirthday());
    }

    @Test
    void testEquals() {
        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("test@test.com");
        user2.setLogin("test");
        user2.setName("Test");
        user2.setBirthday(bday);
        assertEquals(user, user2);
    }

    @Test
    void testHashCode() {
        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("test@test.com");
        user2.setLogin("test");
        user2.setName("Test");
        user2.setBirthday(bday);
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("User(id=1, email=test@test.com, login=test, name=Test, birthday="
                + bday.toString() + ")", user.toString());
    }
}