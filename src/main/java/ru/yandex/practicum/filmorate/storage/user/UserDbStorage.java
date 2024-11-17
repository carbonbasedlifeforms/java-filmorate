package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
@Primary
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    public static final String SELECT_USERS = "select * from users";
    public static final String SELECT_USER_BY_ID = "select * from users where id = ?";
    public static final String INSERT_USER = "insert into users (name, email, login, birthday) values (?, ?, ?, ?)";
    public static final String UPDATE_USER = "update users set name = ?, email = ?, login = ?, birthday = ? " +
            "where id = ?";
    public static final String SELECT_FRIENDS = """
              select u.*
              from users u
              join friends f on u.id = f.friends_id
              where f.users_id = ?
            """;
    public static final String INSERT_FRIEND = "insert into friends(users_id, friends_id) values (?, ?)";
    public static final String DELETE_FRIEND = "delete from friends where users_id = ? and friends_id = ?";
    public static final String SELECT_COMMON_FRIENDS = """
                with cte as (
                select friends_id from friends where users_id = ?
                intersect
                select friends_id from friends where users_id = ?
                )
                select u.*
                from users u
                join cte on u.id = cte.friends_id
            """;

    public UserDbStorage(JdbcTemplate jdbcTemplate, RowMapper<User> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public User getUserById(Long id) {
        log.info("Getting user by id {} from DB", id);
        return getOne(SELECT_USER_BY_ID, id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not exists"));
    }

    @Override
    public Collection<User> getUsers() {
        log.info("Getting all users from DB");
        return getMany(SELECT_USERS);
    }

    @Override
    public List<User> getFriends(User user) {
        log.info("Getting friends of user {} from DB", user.getName());
        return getMany(SELECT_FRIENDS, user.getId());
    }

    @Override
    public User createUser(User user) {
        long id = insert(INSERT_USER,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday());
        log.info("Created user with id {} and saved to DB ", id);
        return getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        update(UPDATE_USER,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        log.info("Updated user with id {} and saved to DB ", user.getId());
        return user;
    }

    @Override
    public void addFriend(User user, User friend) {
        log.info("Adding friend {} to user {}", friend.getName(), user.getName());
        insert(INSERT_FRIEND, user.getId(), friend.getId());
    }

    @Override
    public void delFriend(User user, User friend) {
        log.info("Deleting friend {} from user {}", friend.getName(), user.getName());
        delete(DELETE_FRIEND, user.getId(), friend.getId());
    }

    @Override
    public List<User> getCommonFriends(User user, User otherUser) {
        log.info("Getting common friends of user {} and other user {}", user.getName(), otherUser.getName());
        return getMany(SELECT_COMMON_FRIENDS, user.getId(), otherUser.getId());
    }
}
