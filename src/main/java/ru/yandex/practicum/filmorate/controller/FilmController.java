package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 0;

    private Integer generateId() {
        return ++id;
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        log.info("Не удалось получить список всех фильмов.");
        return films.values();
    }


    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Не удалось создать фильм.");
        return film;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Не удалось поместить фильм");
            return film;
        } else {
            log.error("Не удалось поместить фильм");
            throw new InvalidIdException(String.format("Фильм с ID:`%d` не обновлён", film.getId()), HttpStatus.NOT_FOUND);
        }
    }
}
