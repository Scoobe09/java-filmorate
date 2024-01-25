package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.ValidationUtil;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final ValidationUtil valid;

    @Override
    public List<Film> findAllFilms() {
        log.info("Получен список всех фильмов.");
        return filmStorage.findAllFilms();
    }

    @Override
    public Film createFilm(Film film) {
        valid.getRequiestValid(film);
        log.info("Добавили фильм.");
        return this.filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        valid.getRequiestValid(film);
        log.info("Поместили фильм");
        return this.filmStorage.updateFilm(film).orElseThrow(() -> new InvalidIdException("Не удалось найти фильм", HttpStatus.NOT_FOUND));
    }

    @Override
    public void deleteById(Integer id) {
        log.info("Получен запрос на удаление фильма.");
        if (!filmStorage.isExist(id)) {
            throw new InvalidIdException("Не получиилось удалить фильм.", HttpStatus.NOT_FOUND);
        }
        filmStorage.deleteById(id);
    }

    @Override
    public Film findById(Integer id) {
        log.info("Получен фильм");
        return filmStorage.findById(id).orElseThrow(() -> new InvalidIdException("Не удалось найти фильм", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Film> findMostPopularFilms(Integer count) {
        log.info("Получено " + count + " самых популярных фильмов");
        return filmStorage.findMostPopularFilms(count);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        checkId(filmId, userId);
        if (!filmStorage.addLike(filmId, userId)) {
            throw new InvalidIdException("Лайк уже существует", HttpStatus.BAD_REQUEST);
        }
        log.info("Добавлен лайк");
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        checkId(filmId, userId);
        if (!filmStorage.removeLike(filmId, userId)) {
            throw new InvalidIdException("Лайка не существует", HttpStatus.BAD_REQUEST);
        }
        log.info("Лайк удалён");
    }

    private void checkId(Integer filmId, Integer userId) {
        boolean existFilm = filmStorage.isExist(filmId);
        boolean existUser = userStorage.isExist(userId);
        if (!existFilm && !existUser) {
            throw new InvalidIdException("Не найден фильм по айди: " + filmId + ". Не найден пользователь по айди: " + userId, HttpStatus.NOT_FOUND);
        }
        if (!existFilm) {
            throw new InvalidIdException("Не найден фильм по айди: " + filmId, HttpStatus.NOT_FOUND);
        }
        if (!existUser) {
            throw new InvalidIdException("Не найден пользователь по айди: " + userId, HttpStatus.NOT_FOUND);
        }
    }
}
