package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.Collection;

@Slf4j
@Repository
public class MpaDbStorage extends BaseRepository<Mpa> implements MpaStorage {
    private static final String SELECT_ALL = "select * from rating_mpa";
    private static final String SELECT_BY_ID = "select * from rating_mpa where id = ?";

    public MpaDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Mpa> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        log.info("get all mpa");
        return getMany(SELECT_ALL);
    }

    @Override
    public Mpa getMpaById(Integer id) {
        log.info("get mpa by id {}", id);
        return getOne(SELECT_BY_ID, id)
                .orElseThrow(() -> new NotFoundException("rating map with id " + id + " not found"));
    }
}
