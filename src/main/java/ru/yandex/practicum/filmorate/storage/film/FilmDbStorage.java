package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
@Primary
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private static final String SELECT_FILMS = """
            select f.id,f.name,f.description,f.release_date,f.duration,f.rating_mpa_id,mpa.name as mpa_name
            from films f
            left join rating_mpa mpa on f.rating_mpa_id = mpa.id
            """;
    private static final String SELECT_FILM_BY_ID = """
            select f.id,f.name,f.description,f.release_date,f.duration,f.rating_mpa_id,mpa.name as mpa_name
            from films f
            left join rating_mpa mpa on f.rating_mpa_id = mpa.id
            where f.id = ?
            """;
    private static final String INSERT_FILM = """
            insert into films(name,description,release_date,duration,rating_mpa_id)
            values(?,?,?,?,?)""";
    private static final String UPDATE_FILM = """
            update films
            set name = ?, description = ?, release_date = ?, duration = ?, rating_mpa_id = ?
            where id = ?
            """;
    private static final String INSERT_FILM_GENRES = "insert into film_genres(films_id,genres_id) values(?,?)";
    private static final String INSERT_FILM_LIKES = "insert into film_likes(films_id,users_id) values(?,?)";
    private static final String DELETE_FILM_LIKES = "delete from film_likes where films_id = ? and users_id = ?";
    private static final String SELECT_POPULAR_FILMS = """
            with cte as (
            select films_id from film_likes
            group by films_id
            order by count(users_id) desc
            limit ?
            )
            select f.id,f.name,f.description,f.release_date,f.duration,f.rating_mpa_id,mpa.name as mpa_name
            from cte
            join films f on f.id = cte.films_id
            left join rating_mpa mpa on f.rating_mpa_id = mpa.id
            """;
    private static final String DELETE_FILM_GENRES = "delete from film_genres where films_id = ?";

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Film getFilmById(long id) {
        log.info("get film by id: {} from DB", id);
        return getOne(SELECT_FILM_BY_ID, id)
                .orElseThrow(() -> new NotFoundException("film with id: " + id + " is not exists"));
    }

    @Override
    public Collection<Film> getFilms() {
        log.info("get all films from DB");
        return getMany(SELECT_FILMS);
    }

    @Override
    public Film createFilm(Film film) {
        long id = insert(INSERT_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        log.info("created film with id: {} and saved to DB", id);
        addFilmGenre(film.getId(), film.getGenres());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        update(UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        deleteFilmGenres(film.getId());
        addFilmGenre(film.getId(), film.getGenres());
        log.info("updated film with id: {} and saved to DB", film.getId());
        return film;
    }

    @Override
    public void addLike(Film film, User user) {
        log.info("add like to film with id: {} and saved to DB", film.getId());
        insert(INSERT_FILM_LIKES, film.getId(), user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        log.info("delete like of film with id: {} from DB", film.getId());
        delete(DELETE_FILM_LIKES, film.getId(), user.getId());
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        log.info("get popular films from DB");
        return getMany(SELECT_POPULAR_FILMS, count);
    }

    private void addFilmGenre(long filmId, List<Genre> genres) {
        if (genres != null) {
            log.info("add genres to film with id: {} from DB", filmId);
            genres.forEach(genre -> insert(INSERT_FILM_GENRES, filmId, genre.getId()));
        }
    }

    private void deleteFilmGenres(long filmId) {
        log.info("delete genres from film with id: {} from DB", filmId);
        delete(DELETE_FILM_GENRES, filmId);
    }
}
