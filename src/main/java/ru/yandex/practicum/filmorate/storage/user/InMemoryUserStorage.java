package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> friends = new HashMap<>();

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public List<User> getFriends(User user) {
        final Set<Long> userFriends = Optional.ofNullable(friends.get(user.getId())).orElse(Set.of());
        return userFriends.stream()
                .map(x -> Optional.ofNullable(users.get(x)))
                .filter(x -> x.isPresent())
                .map(x -> x.get())
                .toList();
    }

    @Override
    public List<User> getCommonFriends(User user, User otherUser) {
        final Set<Long> userFriends = friends.get(user.getId());
        final Set<Long> otherUserfriends = friends.get(otherUser.getId());
        return userFriends.stream()
                .filter(x -> otherUserfriends.contains(x))
                .map(x -> users.get(x))
                .toList();
    }

    @Override
    public User createUser(User user) {
        checkUsernameEmptyUseLogin(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        checkUsernameEmptyUseLogin(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(User user, User friend) {
        prepareSet(user).add(friend.getId());
        prepareSet(friend).add(user.getId());
    }

    @Override
    public void delFriend(User user, User friend) {
        prepareSet(user).remove(friend.getId());
        prepareSet(friend).remove(user.getId());
    }

    private String checkUsernameEmptyUseLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("username is empty, using login");
        }
        return user.getName();
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
