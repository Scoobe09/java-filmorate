package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.ValidationUtil;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService implements FilmServiceIn {

    private final InMemoryFilmStorage film;
    private final ValidationUtil valid;

    @Override
    public Collection<Film> findAllFilms() {
        log.info("Получен список всех фильмов.");
        return film.findAllFilms();
    }

    @Override
    public Film createFilm(Film film1) {
        valid.getRequiestValid(film1);
        log.info("Добавили фильм.");
        return film.createFilm(film1);
    }

    @Override
    public Film updateFilm(Film film1) {
        valid.getRequiestValid(film1);
        log.info("Поместили фильм");
        return film.updateFilm(film1);
    }

    @Override
    public void deleteById(Integer id) {
        log.info("Получен запрос на удаление фильма.");
        if(!film.isExist(id)){
            throw new InvalidIdException("Не получиилось удалить фильм.", HttpStatus.NOT_FOUND);
        }
        film.deleteById(id);
    }

    @Override
    public Film findById(Integer id) {
        log.info("Получен фильм");
        return film.findById(id);
    }

    @Override
    public List<Film> findMostPopularFilms(Integer count) {
        log.info("Получено 10 самых популярных фильмов");
        return film.findMostPopularFilms(count);
    }

    @Override
    public boolean addLike(Integer filmId, Integer userId) {
        log.info("Добавлен лайк");
        return film.addLike(filmId, userId);
    }

    @Override
    public boolean removeLike(Integer filmId, Integer userId) {
        log.info("Лайк удалён");
        return film.removeLike(filmId, userId);
    }
}
