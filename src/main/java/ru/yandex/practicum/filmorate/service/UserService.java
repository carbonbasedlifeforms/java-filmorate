package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getUsers() {
        log.info("get all users");
        return userStorage.getUsers();
    }

    public User getUserById(long id) {
        log.info("get user by id: {} ", id);
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        log.info("creating user with name {}", user.getName());
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        userStorage.getUserById(user.getId());
        log.info("updating user with name {}", user.getName());
        return userStorage.updateUser(user);
    }
}
