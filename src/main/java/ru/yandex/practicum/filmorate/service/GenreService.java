package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<Genre> getGenres() {
        log.info("get all genres");
        return genreStorage.getGenres();
    }

    public Genre getGenreById(Integer id) {
        log.info("get genre by id {}", id);
        return genreStorage.getGenreById(id);
    }
}
