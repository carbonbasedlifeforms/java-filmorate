package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    Optional<User> getUserById(Long id);

    Collection<User> getUsers();

    List<User> getFriends(User user);

    User createUser(User user);

    User updateUser(User user);

    void addFriend(User user, User friend);

    void delFriend(User user, User friend);

    List<User> getCommonFriends(User user, User otherUser);
}
