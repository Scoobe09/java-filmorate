package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    boolean deleteById(Integer id);

    Film findById(Integer id);

    List<Film> findMostPopularFilms(Integer count);

    boolean isExist(Integer id);

    boolean addLike(Integer filmId, Integer userId);

    boolean deleteLike(Integer filmId, Integer userId);
}
