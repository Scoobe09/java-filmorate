package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> findAllFilms();

    Film createFilm(Film film);

    Optional<Film> updateFilm(Film film);

    void deleteById(Integer id);

    Film findById(Integer id);

    List<Film> findMostPopularFilms(Integer count);

    boolean isExist(Integer id);
}
