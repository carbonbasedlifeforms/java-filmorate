package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate CINEMA_BDAY = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("get all films");
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        try {
            validateReleaseDate(film);
            film.setId(getNextId());
            films.put(film.getId(), film);
            log.info("film {} added", film.getName());
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film existFilm;
        try {
            existFilm = films.entrySet().stream()
                    .filter(x -> x.getKey() == film.getId())
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElseThrow(() -> new ValidationException("this film is not exists"));
            validateReleaseDate(film);
            existFilm.setReleaseDate(film.getReleaseDate());
            existFilm.setDescription(film.getDescription());
            existFilm.setName(film.getName());
            existFilm.setDuration(film.getDuration());
            log.info("film {} updated", film.getName());
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return existFilm;
    }

    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(CINEMA_BDAY))
            throw new ValidationException("Release date is not correct");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
