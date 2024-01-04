package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmTest {
    Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.usingContext().getValidator();
        }
    }

    @Test
    void validateFilmNameEmpty() {
        Film film = Film.builder()
                .name("")
                .description("Бери ношу по себе, чтоб не падать при ходьбе")
                .releaseDate(LocalDate.of(1997, 12, 12))
                .duration(123)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Название фильма не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void validateFilmDescriptionEmpty() {
        Film film = Film.builder()
                .name("Brat")
                .description("")
                .releaseDate(LocalDate.of(1997, 12, 12))
                .duration(123)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Максимальное количество символов в описании 200, а минимальное 1", violations.iterator().next().getMessage());
    }

    @Test
    void validateFilmWrongData() {
        Film film = Film.builder()
                .name("Brat")
                .description("Бери ношу по себе, чтоб не падать при ходьбе")
                .releaseDate(LocalDate.of(1845, 1, 1))
                .duration(123)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Дата не может быть раньше 28 декабря 1895 года", violations.iterator().next().getMessage());
    }

    @Test
    void validateFilmNegativeDuration() {
        Film film = Film.builder()
                .name("Brat")
                .description("Бери ношу по себе, чтоб не падать при ходьбе")
                .releaseDate(LocalDate.of(1997, 12, 12))
                .duration(-1)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Число должно быть положительное", violations.iterator().next().getMessage());
    }
}