package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {

    private final UserController userController;
    private final JdbcTemplate jdbcTemplate;
    User user;
    User createdUser;
    User anotherUser;

    LocalDate birthday = LocalDate.now().minusYears(50);

    @BeforeEach
    void setUp() {
        deleteUsers();
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
    void getUsers() {
        assertEquals(0, userController.getUsers().size());
        userController.createUser(user);
        userController.createUser(anotherUser);
        assertEquals(2, userController.getUsers().size());
    }

    @Test
    void createUser() {
        createdUser = userController.createUser(user);
        assertEquals(1, userController.getUsers().size());
        assertTrue(userController.getUsers().contains(createdUser));

        createdUser = userController.createUser(anotherUser);
        assertEquals(2, userController.getUsers().size());
        assertTrue(userController.getUsers().contains(createdUser));
        assertEquals(createdUser.getName(), userController.getUsers().stream()
                .filter(x -> x.getName().equals(createdUser.getName()))
                .map(User::getLogin)
                .findFirst()
                .get());
    }

    @Test
    void updateUser() {
        createdUser = userController.createUser(user);
        createdUser.setName("newName");
        userController.updateUser(createdUser);
        assertEquals(userController.getUserById(createdUser.getId()).getName(), "newName");
    }

    private void deleteUsers() {
        jdbcTemplate.update("delete from friends");
        jdbcTemplate.update("delete from users");
    }
}