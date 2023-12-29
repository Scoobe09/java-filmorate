package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.annotations.MinimumDate;
import ru.yandex.practicum.filmorate.constant.FilmConstants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MinimumDateValidator implements ConstraintValidator<MinimumDate, LocalDate> {
    private LocalDate minimumDate;

    @Override
    public void initialize(MinimumDate constraintAnnotation) {
        minimumDate = LocalDate.parse(constraintAnnotation.value(), FilmConstants.FORMATTER);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || !value.isBefore(minimumDate);
    }
}
