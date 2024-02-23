package ru.yandex.practicum.filmorate.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreDao {
    JdbcTemplate jdbcTemplate;

    public Optional<Genre> findById(Integer id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ? ";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowGenre, id);
        if (genres.size() != 1) {
            throw new InvalidIdException(String.format("Not found genre with id %d", id), HttpStatus.NOT_FOUND);
        }
        return Optional.ofNullable(genres.get(0));
    }

    public List<Genre> findAll() {
        String sqlQuery = "SELECT * FROM genres" +
                " ORDER BY id";
        return jdbcTemplate.query(sqlQuery, this::mapRowGenre);
    }

    private Genre mapRowGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
