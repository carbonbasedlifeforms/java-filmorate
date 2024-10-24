package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LikeService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(long filmId, long userId) {
        log.info("add like with userId {} for film with id {}", userId, filmId);
        filmStorage.addLike(filmStorage.getFilmById(filmId), userStorage.getUserById(userId));
    }

    public void deleteLike(long filmId, long userId) {
        log.info("delete like with userId {} for film with id {}", userId, filmId);
        filmStorage.deleteLike(filmStorage.getFilmById(filmId), userStorage.getUserById(userId));
    }

    public List<Film> getPopularFilms(int count) {
        log.info("return top {} popular films", count);
        return filmStorage.getPopularFilms(count);
    }
}
