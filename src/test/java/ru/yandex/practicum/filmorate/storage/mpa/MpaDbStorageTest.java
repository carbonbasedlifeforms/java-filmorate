package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    void getAllMpa() {
        assertEquals(mpaDbStorage.getAllMpa().size(), 5);
    }

    @Test
    void getMpaById() {
        assertEquals(mpaDbStorage.getMpaById(1).getId(), 1);
        assertEquals(mpaDbStorage.getMpaById(1).getName(), "G");
    }
}