package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
//    @Qualifier("UserDbStorage")
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
        checkUsernameEmptyUseLogin(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        userStorage.getUserById(user.getId());
        checkUsernameEmptyUseLogin(user);
        log.info("updating user with name {}", user.getName());
        return userStorage.updateUser(user);
    }

    private void checkUsernameEmptyUseLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("username is empty, using login");
        }
    }
}
