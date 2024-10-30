package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> friends = new HashMap<>();

    @Override
    public User getUserById(Long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not exists"));
    }

    @Override
    public Collection<User> getUsers() {
        log.info("get all users");
        return users.values();
    }

    @Override
    public List<User> getFriends(User user) {
        final Set<Long> userFriends = Optional.ofNullable(friends.get(user.getId())).orElse(Set.of());
        log.info("get friends for user with name {} ", user.getName());
        return userFriends.stream()
                .map(x -> Optional.ofNullable(users.get(x)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public List<User> getCommonFriends(User user, User otherUser) {
        final Set<Long> userFriends = friends.get(user.getId());
        final Set<Long> otherUserFriends = friends.get(otherUser.getId());
        log.info("get common friends of user with name {} and other user with name {} ",
                user.getName(), otherUser.getName());
        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(users::get)
                .toList();
    }

    @Override
    public User createUser(User user) {
        checkUsernameEmptyUseLogin(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("user with name {} created", user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        checkUsernameEmptyUseLogin(user);
        users.put(user.getId(), user);
        log.info("user with name {} updated", user.getName());
        return user;
    }

    @Override
    public void addFriend(User user, User friend) {
        prepareSet(user).add(friend.getId());
        log.info("friend {} add to user {}", friend.getName(), user.getName());
        prepareSet(friend).add(user.getId());
        log.info("user {} add to friend {} as friend", user.getName(), friend.getName());
    }

    @Override
    public void delFriend(User user, User friend) {
        prepareSet(user).remove(friend.getId());
        log.info("friend {} delete from user {}", friend.getName(), user.getName());
        prepareSet(friend).remove(user.getId());
        log.info("user {} delete from friend {} as friend", friend.getName(), user.getName());
    }

    private void checkUsernameEmptyUseLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("username is empty, using login");
        }
    }

    private Set<Long> prepareSet(User user) {
        return friends.computeIfAbsent(user.getId(), friendId -> new HashSet<>());
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
