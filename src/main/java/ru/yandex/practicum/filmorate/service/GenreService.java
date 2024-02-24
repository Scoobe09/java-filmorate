package ru.yandex.practicum.filmorate.service;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDao genreDao;

    public Genre getGenre(Integer id) {
        log.info("Request to receive the user has benn received");
        return genreDao.findById(id)
                .orElseThrow(() -> new InvalidIdException("The genre was not found", HttpStatus.NOT_FOUND));
    }

    public List<Genre> getAllGenres() {
        log.info("Request to receive all the genres has been received");
        return genreDao.findAll();
    }
}
