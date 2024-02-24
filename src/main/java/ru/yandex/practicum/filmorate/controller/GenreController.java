package ru.yandex.practicum.filmorate.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
   private final GenreService genreService;

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable Integer id) {
        return genreService.getGenre(id);
    }

    @GetMapping
    public List<Genre> getAll() {
        return genreService.getAllGenres();
    }
}
