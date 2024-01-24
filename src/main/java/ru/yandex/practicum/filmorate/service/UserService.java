package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.ValidationUtil;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserServiceIn {

    private final ValidationUtil valid;
    private final InMemoryUserStorage storage;

    @Override
    public Collection<User> findAllUsers() {
        log.info("Получен список всех пользователей.");
        return storage.findAllUsers();
    }

    @Override
    public User createUser(User user1) {
        valid.getRequiestValid(user1);
        log.info("Создали пользователя");
        return storage.createUser(user1);
    }

    @Override
    public User updateUser(User user1) {
        valid.getRequiestValid(user1);
        log.info("Поместили фильм");
        return storage.updateUser(user1);
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
    public User findById(Integer id1) {
        log.info("Получен пользователь");
        return storage.findById(id1).orElseThrow(() -> new InvalidIdException("Не удалось найти пользователя", HttpStatus.NOT_FOUND));
    }

    @Override
    public void addFriend(Integer userId, Integer idFriend) {
        log.info("Друг добавлен");
        storage.addFriend(userId, idFriend);
    }

    @Override
    public List<User> getUserFriends(Integer id) {
        log.info("Получен список друзей");
        if(storage.isExist(id)){
            return storage.getUserFriends(id);
        }
        throw new InvalidIdException("Не найден пользователь", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<User> findMutualFriends(Integer userId, Integer idFriend) {
        log.info("Получен список общих друзей");
        if(!storage.isExist(userId) && storage.isExist(idFriend)){
            throw new InvalidIdException("Не найдено", HttpStatus.NOT_FOUND);
        }
        return storage.findMutualFriends(userId, idFriend);
    }

    @Override
    public boolean removeFriend(Integer userId, Integer idFriend) {
        log.info("Друг удалён");
        return storage.removeFriend(userId, idFriend);
    }
}
