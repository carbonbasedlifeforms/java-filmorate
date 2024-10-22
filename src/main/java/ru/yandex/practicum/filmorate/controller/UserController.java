package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        log.info("get all users");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        log.info("get user by id: {} ", id);
        return userService.getUserByIs(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("create user with name {}", user.getName());
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("update user with name {}", user.getName());
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("adding friend id: {} to user id: ", friendId, id);
        userService.addFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        log.info("get friends of user id: {}", id);
        return userService.getFriends(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void delFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("deleting friend id: {} from user id: {}", friendId, id);
        userService.delFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("get common friends of user id {} with other user id {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
