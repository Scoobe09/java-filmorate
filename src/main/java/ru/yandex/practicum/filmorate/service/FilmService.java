package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    List<Film> findAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteById(Integer id);

    Film findById(Integer id);

    List<Film> findMostPopularFilms(Integer count);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);
}
