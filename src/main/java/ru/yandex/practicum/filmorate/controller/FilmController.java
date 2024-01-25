package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.constant.FilmConstants.COUNT_OF_POPULAR_FILMS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @GetMapping
    public List<Film> findAllFilms() {
        return service.findAllFilms();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return service.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return service.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        service.deleteById(id);
    }

    @GetMapping("/popular")
    public List<Film> findMostPopularFilms(@RequestParam(name = "count", defaultValue = COUNT_OF_POPULAR_FILMS) Integer count) {
        return service.findMostPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
         service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
         service.removeLike(id, userId);
    }
}
