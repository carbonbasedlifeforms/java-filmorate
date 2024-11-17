package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

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
        return filmStorage.createFilm(checkAndEnrichFilm(film));
    }

    public Film updateFilm(Film film) {
        filmStorage.getFilmById(film.getId());
        log.info("updating film {}", film.getName());
        return filmStorage.updateFilm(checkAndEnrichFilm(film));
    }

    private Film checkAndEnrichFilm(Film film) {
        try {
            if (film.getReleaseDate() != null &&
                    film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Release date is not correct");
            }
            film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
            if (film.getGenres() != null) {
                film.setGenres(film.getGenres().stream()
                        .map(genre -> genreStorage.getGenreById(genre.getId()))
                        .toList());
            }
        } catch (NotFoundException e) {
            throw new ValidationException(e.getMessage());
        }
        return film;
    }
}
