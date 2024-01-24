package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 0;

    private Integer generateId() {
        return ++id;
    }

    @Override
    public Collection<User> findAllUsers() {
        log.info("Получен список всех пользователей.");
        return users.values();

    }

    @Override
    public User createUser(User user) {
        validateName(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Создали пользователя");
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateName(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new InvalidIdException(String.format("Пользователь с ID:`%d` не обновлён", user.getId()), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteById(Integer id1) {
            users.remove(id1);
    }

    @Override
    public Optional<User> findById(Integer id1) {
        if(isExist(id)) {
            return Optional.of(users.get(id1));
        }
        return Optional.empty();
    }

    @Override
    public boolean addFriend(Integer userId, Integer idFriend) {
        return users.get(userId).getFriends().add(idFriend) && users.get(idFriend).getFriends().add(userId);
    }

    @Override
    public List<User> getUserFriends(Integer id) {
        return users.get(id).getFriends()
                .stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findMutualFriends(Integer userId, Integer idFriendShip) {
        List<Integer> usersId = new ArrayList<>(users.get(userId).getFriends());
        return users.get(idFriendShip).getFriends()
                .stream()
                .filter(usersId::contains)
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public boolean removeFriend(Integer userId, Integer idFriend) {
        return users.get(userId).getFriends().remove(idFriend) && users.get(idFriend).getFriends().remove(userId);
    }

    private void validateName(User user) {
        String name = user.getName();
        if (name == null || name.isEmpty()) {
            user.setName(user.getLogin());
            log.info("Имя не задано, присвоено значение логина");
        }
    }
    @Override
    public boolean isExist(Integer id){
        return users.containsKey(id);
    }
}