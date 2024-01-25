package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.ValidationUtil;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ValidationUtil valid;
    private final UserStorage storage;

    @Override
    public List<User> findAllUsers() {
        log.info("Получен список всех пользователей.");
        return storage.findAllUsers();
    }

    @Override
    public User createUser(User user) {
        valid.getRequiestValid(user);
        valid.validateName(user);
        log.info("Создали пользователя");
        return storage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        valid.getRequiestValid(user);
        valid.validateName(user);
        log.info("Поместили фильм");
        return storage.updateUser(user).orElseThrow(() -> new InvalidIdException("Не удалось найти пользователя по id/", HttpStatus.NOT_FOUND));
    }

    @Override
    public void deleteById(Integer id) {
        log.info("Получен запрос на удаление юзера.");
        if (!storage.isExist(id)) {
            throw new InvalidIdException("Не получиилось удалить юзера.", HttpStatus.NOT_FOUND);
        }
        storage.deleteById(id);
    }

    @Override
    public User findById(Integer id) {
        log.info("Получен пользователь");
        return storage.findById(id).orElseThrow(() -> new InvalidIdException("Не удалось найти пользователя", HttpStatus.NOT_FOUND));
    }

    @Override
    public void addFriend(Integer userId, Integer idFriend) {
        checkId(userId, idFriend);
        if (!storage.addFriend(userId, idFriend)) {
            throw new InvalidIdException("Пользователи уже друзья", HttpStatus.BAD_REQUEST);
        }
        log.info("Друг добавлен");
    }

    @Override
    public List<User> getUserFriends(Integer id) {
        log.info("Получен список друзей");
        if (storage.isExist(id)) {
            return storage.getUserFriends(id);
        }
        throw new InvalidIdException("Не найден пользователь", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<User> findMutualFriends(Integer userId, Integer idFriend) {
        checkId(userId, idFriend);
        log.info("Получен список общих друзей");
        return storage.findMutualFriends(userId, idFriend);
    }

    @Override
    public void removeFriend(Integer userId, Integer idFriend) {
        checkId(userId, idFriend);
        if (!storage.removeFriend(userId, idFriend)) {
            throw new InvalidIdException("Не являются друзьями", HttpStatus.BAD_REQUEST);
        }
        log.info("Друг удалён");
    }

    private void checkId(Integer id1, Integer id2) {
        boolean existUser1 = storage.isExist(id1);
        boolean existUser2 = storage.isExist(id2);
        if (!existUser1 && !existUser2) {
            throw new InvalidIdException("Не найдены пользователи по айдишникам: " + id1 + ", " + id2, HttpStatus.NOT_FOUND);
        }
        if (!existUser1) {
            throw new InvalidIdException("Не найден пользователь по айди: " + id1, HttpStatus.NOT_FOUND);
        }
        if (!existUser2) {
            throw new InvalidIdException("Не найден пользователь по айди: " + id2, HttpStatus.NOT_FOUND);
        }
    }
}
