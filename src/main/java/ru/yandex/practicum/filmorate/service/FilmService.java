package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getFilms() {
        log.info("get all films");
        return filmStorage.getFilms();
    }

    public Film getFilm(long filmId) {
        log.info("get film by id {}", filmId);
        return filmStorage.getFilmById(filmId);
    }

    public Film createFilm(Film film) {
        log.info("adding film {}", film.getName());
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        filmStorage.getFilmById(film.getId());
        log.info("updating film {}", film.getName());
        return filmStorage.updateFilm(film);
    }
}
