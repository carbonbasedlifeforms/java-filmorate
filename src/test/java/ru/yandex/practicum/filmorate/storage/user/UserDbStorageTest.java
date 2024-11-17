package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    LocalDate birthDay = LocalDate.parse("2001-10-02");
    User user;
    User otherUser;
    User commonFriend;

    @BeforeEach
    void setUp() {
        cleanUp();
        user = new User();
        user.setName("Test Testovich");
        user.setLogin("test");
        user.setEmail("test@test.org");
        user.setBirthday(birthDay);
        user = userDbStorage.createUser(user);

        otherUser = new User();
        otherUser.setName("Test Petrovich");
        otherUser.setLogin("otherTest");
        otherUser.setEmail("otherTest@test.org");
        otherUser.setBirthday(birthDay);
        otherUser = userDbStorage.createUser(otherUser);

        commonFriend = new User();
        commonFriend.setName("Test Common Friend");
        commonFriend.setLogin("commonFriendTest");
        commonFriend.setEmail("commonFriendTest@test.org");
        commonFriend.setBirthday(birthDay);
        commonFriend = userDbStorage.createUser(commonFriend);
    }

    @Test
    void getUserById() {
        assertEquals(user, userDbStorage.getUserById(user.getId()));
    }

    @Test
    void getUsers() {
        assertThat(userDbStorage.getUsers())
                .hasSize(3)
                .contains(user);
    }

    @Test
    void createUser() {
        assertThat(user)
                .hasFieldOrPropertyWithValue("id", user.getId())
                .hasFieldOrPropertyWithValue("name", "Test Testovich")
                .hasFieldOrPropertyWithValue("email", "test@test.org")
                .hasFieldOrPropertyWithValue("birthday", birthDay)
                .hasFieldOrPropertyWithValue("login", "test");
    }

    @Test
    void updateUser() {
        user.setName("Testovich Test");
        user.setEmail("org@test.test");
        user.setBirthday(birthDay.minusYears(1));
        user.setLogin("tset");
        userDbStorage.updateUser(user);
        assertThat(user)
                .hasFieldOrPropertyWithValue("id", user.getId())
                .hasFieldOrPropertyWithValue("name", "Testovich Test")
                .hasFieldOrPropertyWithValue("email", "org@test.test")
                .hasFieldOrPropertyWithValue("birthday", birthDay.minusYears(1))
                .hasFieldOrPropertyWithValue("login", "tset");
    }

    @Test
    void addFriend() {
        userDbStorage.addFriend(user, otherUser);
        assertThat(userDbStorage.getFriends(user))
                .hasSize(1)
                .contains(otherUser);

    }

    @Test
    void delFriend() {
        userDbStorage.addFriend(user, otherUser);
        userDbStorage.delFriend(user, otherUser);
        assertThat(userDbStorage.getFriends(user))
                .hasSize(0);
    }

    @Test
    void getFriends() {
        userDbStorage.addFriend(user, otherUser);
        userDbStorage.addFriend(user, commonFriend);
        assertThat(userDbStorage.getFriends(user))
                .hasSize(2)
                .contains(otherUser)
                .contains(commonFriend);
    }

    @Test
    void getCommonFriends() {
        userDbStorage.addFriend(user, commonFriend);
        userDbStorage.addFriend(otherUser, commonFriend);
        assertThat(userDbStorage.getCommonFriends(user, otherUser))
                .hasSize(1)
                .contains(commonFriend);
    }

    private void cleanUp() {
        jdbcTemplate.update("delete from friends");
        jdbcTemplate.update("delete from users");
    }
}