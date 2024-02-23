package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAllFilms() {

        String sqlQuery = "SELECT f.*, m.name as MPA_NAME, g.id AS GENRE_ID, g.name as GENRE_NAME " +
                "FROM films as f" +
                "         LEFT JOIN film_genre AS fg ON f.id = fg.FILM_ID" +
                "         LEFT JOIN mpa AS m ON f.mpa_id = m.id" +
                "         LEFT JOIN genres AS g ON g.id = fg.genre_id ";

        Map<Integer, Film> films = new HashMap<>();
        List<Film> str = jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> {
            Film film = mapRowToFilm(resultSet, rowNum);
            Integer filmId = film.getId();
            if (!films.containsKey(filmId)) {
                films.put(film.getId(), film);
            } else {
                Film filmFromMap = films.get(filmId);
                if (filmFromMap.getGenres() == null) {
                    filmFromMap.setGenres(new HashSet<>());
                }
                filmFromMap.getGenres().addAll(film.getGenres());
            }
            return film;
        });

        return str.stream().sorted(Comparator.comparing(Film::getId)).collect(Collectors.toList());
    }

    private void saveGenre(Film film) {
        String sqlQuery = "INSERT INTO film_genre(genre_id, film_id)" +
                "VALUES (?,?)";

        if (film.getGenres() == null) {
            return;
        }
        List<Genre> genres = new ArrayList<>(film.getGenres());
        genres.sort(Comparator.comparingInt(Genre::getId));
        deleteFilmGenres(film.getId());
        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, genres.get(i).getId());
                ps.setInt(2, film.getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, releaseDate, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        saveGenre(film);
        return findById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films " +
                "SET name = ?," +
                "description = ?," +
                "releaseDate = ?," +
                "duration = ?," +
                "mpa_id = ?" +
                "WHERE id = ?";

        int update = jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (update == 0) {
            throw new InvalidIdException("Фильм не найден", HttpStatus.NOT_FOUND);
        }
        saveGenre(film);

        return findById(film.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        String sqlQuery = "DELETE " +
                "FROM films " +
                "WHERE id = ?";

        jdbcTemplate.update(sqlQuery, id);
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public Film findById(Integer id) {
        String sqlQuery = "SELECT f.*, m.name as MPA_NAME, g.id AS GENRE_ID, g.name as GENRE_NAME " +
                "FROM films as f" +
                "         LEFT JOIN film_genre as fg on f.id = fg.film_id" +
                "         LEFT JOIN mpa as m on f.mpa_id = m.id" +
                "         LEFT JOIN genres as g on g.id = fg.genre_id " +
                "WHERE f.id = ?";

        Map<Integer, Film> films = new HashMap<>();
        jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> {
            Film film = mapRowToFilm(resultSet, rowNum);
            Integer filmId = film.getId();
            if (!films.containsKey(filmId)) {
                films.put(film.getId(), film);
            } else {
                Film filmFromMap = films.get(filmId);
                if (filmFromMap.getGenres() == null) {
                    filmFromMap.setGenres(new HashSet<>());
                }
                filmFromMap.getGenres().addAll(film.getGenres());
            }
            return film;
        }, id);
        return films.get(id);
    }

    @Override
    public List<Film> findMostPopularFilms(Integer count) {
        String sqlQuery = "SELECT f.*, m.name as MPA_NAME, g.id AS GENRE_ID, g.name as GENRE_NAME " +
                "FROM films as f" +
                "         LEFT JOIN film_genre as fg on f.id = fg.FILM_ID" +
                "         LEFT JOIN mpa as m on f.mpa_id = m.id" +
                "         LEFT JOIN genres as g on g.id = fg.genre_id " +
                "LEFT JOIN LIKES as l on l.film_id = f.id " +
                "GROUP BY f.id, GENRE_ID " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?";

        Map<Integer, Film> films = new HashMap<>();
        List<Film> str = jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> {
            Film film = mapRowToFilm(resultSet, rowNum);
            Integer filmId = film.getId();
            if (!films.containsKey(filmId)) {
                films.put(film.getId(), film);
            } else {
                films.get(filmId).getGenres().addAll(film.getGenres());
            }
            return film;
        }, count);
        return str.stream()
                .sorted(Comparator.comparingInt(f -> -f.getLikes().size()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isExist(Integer id) {
        String sqlQuery = "SELECT EXISTS (SELECT 1 FROM films WHERE id = ?)";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id));
    }

    @Override
    public boolean addLike(Integer filmId, Integer userId) {
        String sqlQuery = "INSERT INTO likes(film_id, user_id)" +
                " VALUES(?,?)";

        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    @Override
    public boolean deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "DELETE" +
                " FROM likes" +
                " WHERE film_id = ?" +
                " and user_id = ?";

        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {

        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mapRowToMpa(resultSet))
                .genres(mapRowToGenre(resultSet))
                .build();
    }


    private Mpa mapRowToMpa(ResultSet resultSet) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }

    private Set<Genre> mapRowToGenre(ResultSet resultSet) throws SQLException {
        Genre genre = Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
        Set<Genre> genres = new HashSet<>();
        if (genre.getName() == null) {
            return genres;
        }
        genres.add(genre);
        return genres;
    }

    private void deleteFilmGenres(Integer filmID) {
        String sqlDelete = "DELETE  FROM FILM_GENRE WHERE FILM_ID = ? ";
        jdbcTemplate.update(sqlDelete, filmID);
    }
}