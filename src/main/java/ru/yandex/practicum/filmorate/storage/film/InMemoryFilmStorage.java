package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static final LocalDate CINEMA_BDAY = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Set<Long>> likes = new HashMap<>();

    @Override
    public Film getFilmById(long id) {
        log.info("get film by id {}", id);
        return Optional.ofNullable(films.get(id))
                .orElseThrow(() -> new NotFoundException("film with id: " + id + " is not exists"));
    }

    @Override
    public Collection<Film> getFilms() {
        log.info("get all films");
        return films.values();

    }

    @Override
    public Film createFilm(Film film) {
        validateReleaseDate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("film {} added", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateReleaseDate(film);
        films.put(film.getId(), film);
        log.info("film {} updated", film.getName());
        return film;
    }

    @Override
    public void addLike(Film film, User user) {
        final Set<Long> filmLikes = likes.computeIfAbsent(film.getId(), id -> new HashSet<>());
        filmLikes.add(user.getId());
        log.info("like of user {} for film {} added", user.getName(), film.getName());
    }

    @Override
    public void deleteLike(Film film, User user) {
        final Set<Long> filmLikes = likes.computeIfAbsent(film.getId(), id -> new HashSet<>());
        filmLikes.remove(user.getId());
        log.info("like of user {} for film {} deleted", user.getName(), film.getName());
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        log.info("get top {} popular film",count);
        return likes.entrySet().stream()
                .sorted((x, y) -> (y.getValue().size()) - x.getValue().size())
                .limit(count)
                .map(x -> getFilmById(x.getKey()))
                .toList();
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
