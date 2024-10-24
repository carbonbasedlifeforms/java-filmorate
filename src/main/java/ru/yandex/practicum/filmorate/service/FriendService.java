package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FriendService {

    private final UserStorage userStorage;

    public List<User> getFriends(long userId) {
        final User user = userStorage.getUserById(userId);
        log.info("get friends for user id {} ", userId);
        return userStorage.getFriends(user);
    }

    public void addFriend(long userId, long friendId) {
        log.info("adding friend id: {} to user id: {}", friendId, userId);
        userStorage.addFriend(userStorage.getUserById(userId), userStorage.getUserById(friendId));
    }

    public void delFriend(long userId, long friendId) {
        log.info("deleting friend id: {} from user id: {}", friendId, userId);
        userStorage.delFriend(userStorage.getUserById(userId), userStorage.getUserById(friendId));
    }

    public List<User> getCommonFriends(long id, long otherId) {
        log.info("get common friends of user id {} with other user id {}", id, otherId);
        return userStorage.getCommonFriends(userStorage.getUserById(id), userStorage.getUserById(otherId));
    }
}
