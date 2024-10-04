package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        try {
            if (user.getName() == null) {
                user.setName(user.getLogin());
                log.info("username is empty, using login");
            }
            user.setId(getNextId());
            users.put(user.getId(), user);
            log.info("new user {} added",user.getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        User existsUser = null;
        try {
            Optional<User> optionalUser = users.entrySet().stream()
                    .filter(x -> x.getKey() == user.getId())
                    .map(Map.Entry::getValue)
                    .findFirst();
            existsUser = optionalUser.orElseThrow(() -> new ValidationException("this user is not exists"));
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        existsUser.setLogin(user.getLogin());
        existsUser.setEmail(user.getEmail());
        existsUser.setName(Optional.ofNullable(user.getName()).orElse(user.getLogin()));
        existsUser.setBirthday(user.getBirthday());
        log.info("user {} updated", existsUser.getName());
        return existsUser;
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
