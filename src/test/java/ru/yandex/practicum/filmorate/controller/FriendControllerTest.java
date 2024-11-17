package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendControllerTest {
    private final FriendController friendController;
    private final UserController userController;
    User user;
    User createdUser;
    User createdFriend;
    User anotherUser;
    User commonFriend;

    LocalDate birthday = LocalDate.now().minusYears(50);

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("testName");
        user.setEmail("test@test.com");
        user.setLogin("testLogin");
        user.setBirthday(birthday);

        anotherUser = new User();
        anotherUser.setEmail("anotherTest@test.com");
        anotherUser.setLogin("anotherTestLogin");
        anotherUser.setBirthday(birthday);

        commonFriend = new User();
        commonFriend.setEmail("commonUserTest@test.com");
        commonFriend.setLogin("commonUserTestLogin");
        commonFriend.setBirthday(birthday);
    }

    @Test
    void addFriend() {
        createdUser = userController.createUser(user);
        createdFriend = userController.createUser(anotherUser);
        assertTrue(friendController.getFriends(createdUser.getId()).isEmpty());
        friendController.addFriend(createdUser.getId(), createdFriend.getId());
        assertTrue(friendController.getFriends(createdUser.getId()).contains(createdFriend));
    }

    @Test
    void getFriends() {
        createdUser = userController.createUser(user);
        createdFriend = userController.createUser(anotherUser);
        assertTrue(friendController.getFriends(createdUser.getId()).isEmpty());
        friendController.addFriend(createdUser.getId(), createdFriend.getId());
        assertEquals(1, friendController.getFriends(createdUser.getId()).size());
    }

    @Test
    void delFriend() {
        createdUser = userController.createUser(user);
        createdFriend = userController.createUser(anotherUser);
        assertTrue(friendController.getFriends(createdUser.getId()).isEmpty());
        friendController.addFriend(createdUser.getId(), createdFriend.getId());
        assertTrue(friendController.getFriends(createdUser.getId()).contains(createdFriend));
        friendController.delFriend(createdUser.getId(), createdFriend.getId());
        assertTrue(friendController.getFriends(createdUser.getId()).isEmpty());
    }

    @Test
    void getCommonFriends() {
        createdUser = userController.createUser(user);
        anotherUser = userController.createUser(anotherUser);
        commonFriend = userController.createUser(commonFriend);
        friendController.addFriend(createdUser.getId(), commonFriend.getId());
        friendController.addFriend(anotherUser.getId(), commonFriend.getId());
        assertTrue(friendController.getCommonFriends(createdUser.getId(), anotherUser.getId()).contains(commonFriend));
    }
}