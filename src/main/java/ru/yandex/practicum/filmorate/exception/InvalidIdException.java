package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

public class InvalidIdException extends RuntimeException {
    private final HttpStatus httpStatus;

    public InvalidIdException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}