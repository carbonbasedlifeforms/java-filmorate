package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserByIs(long id) {
        return checkUserExists(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (userStorage.getUserById(user.getId()).isEmpty()) {
            throw new NotFoundException("User with id " + user.getId() + " not exists");
        }
        return userStorage.updateUser(user);
    }

    public List<User> getFriends(long useriId) {
        final User user = checkUserExists(useriId);
        return userStorage.getFriends(user);
    }

    public void addFriend(long userId, long friendId) {
        userStorage.addFriend(checkUserExists(userId), checkUserExists(friendId));
    }

    public void delFriend(long userId, long friendId) {
        userStorage.delFriend(checkUserExists(userId), checkUserExists(friendId));
    }

    public List<User> getCommonFriends(long id, long otherId) {
        return userStorage.getCommonFriends(checkUserExists(id), checkUserExists(otherId));
    }

    private User checkUserExists(long userId) {
        final User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not exists"));
        return user;
    }
}
