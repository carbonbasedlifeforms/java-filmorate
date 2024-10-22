package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(long filmId) {
        return checkFilmExists(filmId);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        checkFilmExists(film.getId());
        return filmStorage.updateFilm(film);
    }

    public void addLike(long filmId, long userId) {
        filmStorage.addLike(checkFilmExists(filmId), checkUserExists(userId));
    }

    public void deleteLike(long filmId, long userId) {
        filmStorage.deleteLike(checkFilmExists(filmId), checkUserExists(userId));
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    private Film checkFilmExists(long filmId) {
        final Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("film with id: " + filmId + " is not exists"));
        return film;
    }

    private User checkUserExists(long userId) {
        final User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not exists"));
        return user;
    }

}
