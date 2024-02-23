package ru.yandex.practicum.filmorate.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MpaController {
    MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable Integer id) {
        return mpaService.getMpa(id);
    }

    @GetMapping
    public List<Mpa> getAll() {
        return mpaService.getAllMpa();
    }
}