package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ValidationUtil valid;
    private final UserStorage storage;

    @Override
    public List<User> findAllUsers() {
        log.info("Запрос на получение списка всех пользователей.");
        log.info("Получен список всех пользователей.");
        return storage.findAllUsers();
    }

    @Override
    public User createUser(User user) {
        log.info("Запрос на создание пользователя: ID - `{}` Имя - `{}`", user.getId(), user.getName());
        valid.getRequiestValid(user);
        valid.validateName(user);
        log.info("Пользователь создан: ID - `{}` Имя - `{}`", user.getId(), user.getName());
        return storage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Запрос на обновление пользователя: ID — `{}` Имя – `{}`", user.getId(), user.getName());
        valid.getRequiestValid(user);
        valid.validateName(user);
        log.info("Пользователь обновлен: ID — `{}` Имя – `{}`", user.getId(), user.getName());
        return storage.updateUser(user).orElseThrow(() -> new InvalidIdException("Не удалось найти пользователя по id/", HttpStatus.NOT_FOUND));
    }

    @Override
    public void deleteById(Integer id) {
        log.info("Получен запрос на удаление пользователя по ID - `{}`", id);
        if (!storage.isExist(id)) {
            throw new InvalidIdException("Не получиилось удалить пользователя.", HttpStatus.NOT_FOUND);
        }
        storage.deleteById(id);
        log.info("Пользователь с ID - `{}` удалён", id);
    }

    @Override
    public User findById(Integer id) {
        log.info("Запрос на получение пользователя по ID - `{}`", id);
        log.info("Получен пользователь: ID - `{}`", id);
        if (!storage.isExist(id)) {
            throw new InvalidIdException("Не удалось найти пользователя", HttpStatus.NOT_FOUND);
        }
        return storage.findById(id);
    }

    @Override
    public void addFriend(Integer userId, Integer idFriend) {
        log.info("Получен запрос на добавление пользователя с ID - `{}` в друзья пользователя с ID - `{}`", userId, idFriend);
        checkId(userId, idFriend);
        boolean addedFriendUser = storage.findById(userId).getFriends().add(idFriend);
        storage.findById(idFriend).getFriends().add(userId);
        if (!addedFriendUser) {
            throw new InvalidIdException("Пользователи уже друзья", HttpStatus.BAD_REQUEST);
        }

        log.info("Друг добавлен: ID - `{}`", idFriend);
    }

    @Override
    public List<User> getUserFriends(Integer id) {
        log.info("Запрос списка друзей пользователя с ID — `{}`", id);
        if (storage.isExist(id)) {
            return storage.findById(id).getFriends()
                    .stream()
                    .map(storage::findById)
                    .collect(Collectors.toList());
        }
        throw new InvalidIdException("Не найден пользователь", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<User> findMutualFriends(Integer userId, Integer idFriend) {
        log.info("Запрос на список общих друзей пользователей с идентификаторами `{}` и `{}`.", userId, idFriend);
        checkId(userId, idFriend);
        log.info("Получен список общих друзей");
        List<Integer> friendsId = new ArrayList<>(storage.findById(userId).getFriends());
        return storage.findById(idFriend).getFriends()
                .stream()
                .filter(friendsId::contains)
                .map(storage::findById)
                .collect(Collectors.toList());
    }

    @Override
    public void removeFriend(Integer userId, Integer idFriend) {
        log.info("Запрос на удаление пользователя с ID - `{}`" +
                "из списка друзей пользователя с ID - `{}`.", userId, idFriend);
        checkId(userId, idFriend);
        if (!storage.findById(userId).getFriends().remove(idFriend) && !storage.findById(idFriend).getFriends().remove(userId)) {
            throw new InvalidIdException("Не являются друзьями", HttpStatus.BAD_REQUEST);
        }
        log.info("Друг удалён: ID - `{}`", idFriend);
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
