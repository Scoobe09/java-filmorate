package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 0;

    private Integer generateId() {
        return ++id;
    }

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        Integer filmId = film.getId();
        if (isExist(filmId)) {
            films.put(filmId, film);
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Integer id) {
        films.remove(id);
    }

    @Override
    public Film findById(Integer id) {
        if (isExist(id)) {
            return films.get(id);
        }
        throw new InvalidIdException("Не удалось найти фильм", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Film> findMostPopularFilms(Integer count) {
        return films.values().stream()
                .sorted(Comparator.comparingInt(value -> -value.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isExist(Integer id) {
        return films.containsKey(id);
    }
}