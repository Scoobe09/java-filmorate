package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDao mpaDao;

    public Mpa getMpa(Integer id) {
        log.info(String.format("Mpa with id %d founded ", id));
        return mpaDao.findById(id)
                .orElseThrow(() -> new InvalidIdException("The mpa was not found", HttpStatus.NOT_FOUND));
    }

    public List<Mpa> getAllMpa() {
        log.info("Request to receive all the mpa has been received");
        return mpaDao.findAll();
    }
}
