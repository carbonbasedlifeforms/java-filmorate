package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Slf4j
@Repository
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {
    public static final String SELECT_GENRES = "select * from genres";
    public static final String SELECT_GENRE_BY_ID = "select * from genres where id = ?";
    public static final String SELECT_FILM_GENRES = "select * from genres where id in " +
            "(select genres_id from film_genres where films_id = ?)";

    public GenreDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Genre> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Genre getGenreById(Integer id) {
        log.info("get genre by id {}", id);
        return getOne(SELECT_GENRE_BY_ID, id)
                .orElseThrow(() -> new NotFoundException("Genre with id " + id + " not found"));
    }

    @Override
    public List<Genre> getGenres() {
        log.info("get all genres");
        return getMany(SELECT_GENRES);
    }

    @Override
    public List<Genre> getFilmGenres(long filmId) {
        List<Genre> genres = getMany(SELECT_FILM_GENRES, filmId);
        log.info("get genres of film with id {} ", filmId);
        return genres;

    }
}
