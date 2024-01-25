package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
    public List<User> findAllUsers() {
        log.info("Получен список всех пользователей.");
        return new ArrayList<>(users.values());

    }

    @Override
    public User createUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Создали пользователя");
        return user;
    }

    @Override
    public Optional<User> updateUser(User user) {
        Integer userId = user.getId();
        if (isExist(userId)) {
            users.put(userId, user);
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Integer id) {
        users.remove(id);
    }

    @Override
    public Optional<User> findById(Integer id) {
        if (isExist(id)) {
            return Optional.of(users.get(id));
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

    @Override
    public boolean isExist(Integer id) {
        return users.containsKey(id);
    }
}