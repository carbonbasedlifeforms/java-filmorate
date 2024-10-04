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
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final static LocalDate CINEMA_BDAY = LocalDate.of(1895, 12, 28);
    private final static int MAX_DESC = 200;
    Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        try {
            if (film.getReleaseDate().isBefore(CINEMA_BDAY)) {
                throw new ValidationException("Release date is not correct");
            }
            if (film.getDescription().length() > MAX_DESC) {
                throw new ValidationException("Description must be not more 200 symbols");
            }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("film {} added", film.getName());
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film existFilm = null;
        try {
            Optional<Film> optionalFilm = films.entrySet().stream()
                    .filter(x -> x.getKey() == film.getId())
                    .map(Map.Entry::getValue)
                    .findFirst();
            existFilm = optionalFilm.orElseThrow(() -> new ValidationException("this film is not exists"));
            if (film.getReleaseDate().isBefore(CINEMA_BDAY)) {
                throw new ValidationException("Release date is not correct");
            } else {
                existFilm.setReleaseDate(film.getReleaseDate());
            }
            if (film.getDescription().length() > MAX_DESC) {
                throw new ValidationException("Description must be not more 200 symbols");
            } else {
                existFilm.setDescription(film.getDescription());
            }
            existFilm.setName(film.getName());
            existFilm.setDuration(film.getDuration());
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return existFilm;
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
