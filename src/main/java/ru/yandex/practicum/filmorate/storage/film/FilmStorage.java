package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> getFilmById(long id);

    Collection<Film> getFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    List<Film> getPopularFilms(int count);
}
