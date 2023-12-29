package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.usingContext().getValidator();
        }
    }

    @Test
    void validateUserEmailEmpty() {
        User user = User.builder()
                .email("")
                .login("bogrov97")
                .name("Данила Багров")
                .birthday(LocalDate.of(1971, 12, 27))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Email не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void validateUserEmailWrong() {
        User user = User.builder()
                .email("sfdfsfd")
                .login("bogrov97")
                .name("Данила Багров")
                .birthday(LocalDate.of(1971, 12, 27))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Некорректный формат данных email", violations.iterator().next().getMessage());
    }

    @Test
    void validateUserLoginEmpty() {
        User user = User.builder()
                .email("nagibator228@yandex.ru")
                .login("")
                .name("Данила Багров")
                .birthday(LocalDate.of(1971, 12, 27))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Логин не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void validateUserLoginWrong() {
        User user = User.builder()
                .email("nagibator228@yandex.ru")
                .login("bogrov 97")
                .name("Данила Багров")
                .birthday(LocalDate.of(1971, 12, 27))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("В логине не может быть пробелов", violations.iterator().next().getMessage());
    }

    @Test
    void validateUserDataWrong() {
        User user = User.builder()
                .email("nagibator228@yandex.ru")
                .login("bogrov97")
                .name("Данила Багров")
                .birthday(LocalDate.of(2971, 12, 27))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Дата рождения не может быть в будущем", violations.iterator().next().getMessage());
    }
}