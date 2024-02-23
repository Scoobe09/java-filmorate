package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAllUsers() {
        String sqlQuery = "SELECT *" +
                "FROM users";

        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        return users.stream()
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "INSERT INTO users (NAME, EMAIL, LOGIN, BIRTHDAY) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));

            return stmt;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users " +
                "SET name    = ?," +
                "    email    = ?," +
                "    login     = ?," +
                "    birthday = ? " +
                "WHERE id = ?";


        int update = jdbcTemplate.update(sqlQuery, user.getName(), user.getEmail(),
                user.getLogin(), user.getBirthday(), user.getId());

        if (update == 0) {
            throw new InvalidIdException("Пользователь не найден", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sqlQuery = "DELETE " +
                "FROM users " +
                "WHERE id = ?";


        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public User findById(Integer id) {
        String sqlQuery = "SELECT *" +
                "FROM users" +
                " WHERE ID = ?";

        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
        return users.get(0);
    }

    @Override
    public boolean isExist(Integer id) {
        String sqlQuery = "SELECT EXISTS (SELECT 1 FROM users WHERE id = ?)";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id));
    }

    @Override
    public List<User> getUserFriends(Integer id) {
        String sqlQuery = "SELECT u.* " +
                "FROM users u " +
                "WHERE u.id IN " +
                "(SELECT f.friend_id " +
                "FROM friendships f " +
                "WHERE f.user_id = ? " +

                "UNION " +
                "SELECT f.user_id " +
                "FROM friendships f   " +
                "WHERE f.friend_id = ? " +
                "AND f.status = TRUE)";

        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, id);
        return users.stream()
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findMutualFriends(Integer userId, Integer idFriend) {
        String sqlQuery = "SELECT * " +
                "FROM users " +
                "WHERE id IN (SELECT * " +
                "             FROM ((SELECT f.friend_id" +
                "                    FROM friendships f" +
                "                    WHERE f.user_id = ?" +
                "                    UNION" +
                "                    SELECT f.user_id " +
                "                    FROM friendships f " +
                "                    WHERE f.friend_id = ?" +
                "                      AND f.status = TRUE)" +
                "                   INTERSECT" +
                "                   (SELECT f.friend_id" +
                "                    FROM friendships f" +
                "                    WHERE f.user_id = ? " +
                "                    UNION" +
                "                    SELECT f.user_id " +
                "                    FROM friendships f " +
                "                    WHERE f.friend_id = ?" +
                "                      AND f.status = TRUE)));";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, userId, idFriend, idFriend);
    }

    @Override
    public boolean addFriend(Integer userId, Integer idFriend) {
        String sqlQuery = "INSERT INTO friendships(USER_ID, FRIEND_ID) " +
                "SELECT ?, ? " +
                "FROM dual " +
                "WHERE NOT EXISTS " +
                "(SELECT 1 FROM " +
                "friendships " +
                "WHERE (USER_ID = ? AND FRIEND_ID = ?) " +
                "OR (USER_ID = ? AND FRIEND_ID = ?))";
        return jdbcTemplate.update(sqlQuery, userId, idFriend, userId, idFriend, idFriend, userId) > 0;
    }

    @Override
    public boolean removeFriend(Integer userId, Integer idFriend) {
        String sqlQuery = "DELETE FROM friendships " +
                "WHERE (USER_ID = ? AND FRIEND_ID = ?) " +
                "OR (USER_ID = ? AND FRIEND_ID = ?)";
        return jdbcTemplate.update(sqlQuery, userId, idFriend, idFriend, userId) > 0;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
