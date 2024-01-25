package ru.yandex.practicum.filmorate.validators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationUtil {
    private final Validator validator;

    public <T> void getRequiestValid(T t) {
        if (t != null) {
            Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
            if (!constraintViolations.isEmpty()) {
                String string = constraintViolations
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .reduce((s1, s2) -> s1 + ". " + s2).orElse("");
                log.error("The transmitted request in JSON format is not valid, validation error: {}", string);
                throw new ValidateException("The transmitted request in JSON format is not valid, validation error: " +
                        string);
            }
        }
    }

    public void validateName(User user) {
        String name = user.getName();
        if (name == null || name.isEmpty()) {
            user.setName(user.getLogin());
            log.info("Имя не задано, присвоено значение логина");
        }
    }
}
