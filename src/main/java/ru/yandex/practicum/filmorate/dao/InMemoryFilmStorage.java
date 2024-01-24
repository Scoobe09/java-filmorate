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
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            log.error("Не удалось поместить фильм");
            throw new InvalidIdException(String.format("Фильм с ID:`%d` не обновлён", film.getId()), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteById(Integer id1) {
            films.remove(id1);
    }

    @Override
    public Film findById(Integer id1) {
        return films.get(id1);
    }

    @Override
    public List<Film> findMostPopularFilms(Integer count) {
        return films.values()
                .stream()
                .sorted(Comparator.comparingInt(value -> -value.getLikes().size()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean addLike(Integer filmId, Integer userId) {
        return films.get(filmId).getLikes().add(userId);
    }

    @Override
    public boolean removeLike(Integer filmId, Integer userId) {
        return films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public boolean isExist(Integer id){
        return films.containsKey(id);
    }
}