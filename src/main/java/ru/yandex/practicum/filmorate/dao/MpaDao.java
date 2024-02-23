package ru.yandex.practicum.filmorate.dao;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MpaDao {
    JdbcTemplate jdbcTemplate;

    public Optional<Mpa> findById(Integer id) {
        String sqlQuery = "SELECT * FROM mpa WHERE id = ? ";
        List<Mpa> mpaList = jdbcTemplate.query(sqlQuery, this::mapRowToMpa, id);
        if (mpaList.size() != 1) {
            throw new InvalidIdException(String.format("Not found user with id %d", id), HttpStatus.NOT_FOUND);
        }
        return Optional.ofNullable(mpaList.get(0));
    }

    public List<Mpa> findAll() {
        String sqlQuery = "SELECT * FROM mpa" +
                " ORDER BY id";

        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
