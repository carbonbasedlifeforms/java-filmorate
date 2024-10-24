package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FriendControllerTest {
    UserStorage userStorage;
    UserService userService;
    FriendService friendService;
    FriendController friendController;
    User user;
    User createdUser;
    User createdFriend;
    User anotherUser;
    User commonFriend;

    LocalDate birthday = LocalDate.now().minusYears(50);

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        friendService = new FriendService(userStorage);
        friendController = new FriendController(friendService);
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
        createdUser = userService.createUser(user);
        createdFriend = userService.createUser(anotherUser);
        assertTrue(friendController.getFriends(createdUser.getId()).isEmpty());
        friendController.addFriend(createdUser.getId(), createdFriend.getId());
        assertTrue(friendController.getFriends(createdUser.getId()).contains(createdFriend));
        assertTrue(friendController.getFriends(createdFriend.getId()).contains(createdUser));
    }

    @Test
    void getFriends() {
        createdUser = userService.createUser(user);
        createdFriend = userService.createUser(anotherUser);
        assertTrue(friendController.getFriends(createdUser.getId()).isEmpty());
        friendController.addFriend(createdUser.getId(), createdFriend.getId());
        assertEquals(1, friendController.getFriends(createdUser.getId()).size());
    }

    @Test
    void delFriend() {
        createdUser = userService.createUser(user);
        createdFriend = userService.createUser(anotherUser);
        assertTrue(friendController.getFriends(createdUser.getId()).isEmpty());
        friendController.addFriend(createdUser.getId(), createdFriend.getId());
        assertTrue(friendController.getFriends(createdUser.getId()).contains(createdFriend));
        friendController.delFriend(createdUser.getId(), createdFriend.getId());
        assertTrue(friendController.getFriends(createdUser.getId()).isEmpty());
    }

    @Test
    void getCommonFriends() {
        createdUser = userService.createUser(user);
        anotherUser = userService.createUser(anotherUser);
        commonFriend = userService.createUser(commonFriend);
        friendController.addFriend(createdUser.getId(), commonFriend.getId());
        friendController.addFriend(anotherUser.getId(), commonFriend.getId());
        assertTrue(friendController.getCommonFriends(createdUser.getId(), anotherUser.getId()).contains(commonFriend));
    }
}