package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        log.info("get all users");
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        try {
            checkUsernameEmptyUseLogin(user);
            user.setId(getNextId());
            users.put(user.getId(), user);
            log.info("new user {} added", user.getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        User existsUser = null;
        try {
            existsUser = users.entrySet().stream()
                    .filter(x -> x.getKey() == user.getId())
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElseThrow(() -> new ValidationException("this user is not exists"));
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        existsUser.setLogin(user.getLogin());
        existsUser.setEmail(user.getEmail());
        existsUser.setName(checkUsernameEmptyUseLogin(user));
        existsUser.setBirthday(user.getBirthday());
        log.info("user {} updated", existsUser.getName());
        return existsUser;
    }

    private String checkUsernameEmptyUseLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("username is empty, using login");
        }
        return user.getName();
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
