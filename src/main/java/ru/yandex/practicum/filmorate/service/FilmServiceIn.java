package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmServiceIn {
     Collection<Film> findAllFilms();

     Film createFilm(Film film1);

     Film updateFilm(Film film1);

     void deleteById(Integer id);

     Film findById(Integer id);

     List<Film> findMostPopularFilms(Integer count);

     boolean addLike(Integer filmId, Integer userId);

     boolean removeLike(Integer filmId, Integer userId);
}
