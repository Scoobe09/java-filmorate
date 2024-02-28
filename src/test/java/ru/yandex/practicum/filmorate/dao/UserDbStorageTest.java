package ru.yandex.practicum.filmorate.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDbStorageTest {
    final JdbcTemplate jdbcTemplate;
    User newUser = User.builder().build();

    @BeforeEach
    public void startMethod() {
        newUser = User.builder()
                .id(1)
                .name("Ilon Mask ")
                .login("IlonKrutoi322")
                .email("IlonMask1974@email.ru")
                .birthday(LocalDate.of(1974, 6, 28))
                .build();
    }

    @Test
    @DisplayName("Поиск юезра по айди")
    public void findById_Test() {

        UserDbStorage dao = new UserDbStorage(jdbcTemplate);
        dao.createUser(newUser);

        // вызываем тестируемый метод
        User savedUser = dao.findById(newUser.getId());

        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    @DisplayName("Поиск всех пользователей")
    public void findAll_Test() {
        UserDbStorage dao = new UserDbStorage(jdbcTemplate);
        User faruh = User.builder()
                .id(2)
                .name("Faruh")
                .login("Faruh228")
                .email("faruh@mail.ru")
                .birthday(LocalDate.of(1917, 3, 28))
                .build();
        User bebra = User.builder()
                .id(2)
                .name("Faruh")
                .login("Faruh228")
                .email("faruh@mail.ru")
                .birthday(LocalDate.of(1917, 3, 28))
                .build();
        dao.createUser(faruh);
        dao.createUser(bebra);
        List<User> users = dao.findAllUsers();
        assertEquals(users.size(), 2);
    }

    @Test
    @DisplayName("Обновление юзера")
    public void updateUser_Test() {
        UserDbStorage dao = new UserDbStorage(jdbcTemplate);
        User faruh = User.builder()
                .id(2)
                .name("Faruh")
                .login("Faruh228")
                .email("faruh@mail.ru")
                .birthday(LocalDate.of(1917, 3, 28))
                .build();
        dao.createUser(faruh);
        faruh.toBuilder()
                .id(2)
                .name("freokferoi")
                .email("bebra@mail.ru")
                .login("bebbrochka")
                .birthday(LocalDate.of(1930, 1, 1));
        dao.updateUser(faruh);
        assertThat(faruh)
                .usingRecursiveComparison()
                .isEqualTo(faruh);
    }

    @Test
    @DisplayName("получение списка друзей")
    public void getUserFriends_Test() {
        UserDbStorage dao = new UserDbStorage(jdbcTemplate);
        User faruh = User.builder()
                .name("Faruh")
                .login("Faruh228")
                .email("faruh@mail.ru")
                .birthday(LocalDate.of(1917, 3, 28))
                .build();
        User friends = dao.createUser(faruh);
        assertThat(friends).isNotNull();
        List<User> emptyList = dao.getUserFriends(friends.getId());
        assertNotNull(emptyList);
        assertEquals(0, emptyList.size());

        User friend = User.builder()
                .name("Misha")
                .email("misha@mail.ru")
                .login("misha121232")
                .birthday(LocalDate.of(1995, 4, 7))
                .build();
        User user1 = dao.createUser(friend);
        assertThat(user1).isNotNull();
        dao.addFriend(faruh.getId(), friend.getId());
        List<User> userList = new ArrayList<>(dao.getUserFriends(faruh.getId()));
        assertNotNull(userList);
        assertEquals("Misha", userList.get(0).getName());
    }

    @Test
    @DisplayName("получение общих друзей")
    public void getCommonFriends_Test() {
        UserDbStorage dao = new UserDbStorage(jdbcTemplate);
        User userFriend = User.builder()
                .name("Artem")
                .email("yandex@mail.ru")
                .login("artemmm")
                .birthday(LocalDate.of(1987, 3, 3))
                .build();
        User user = dao.createUser(userFriend);
        assertThat(user).isNotNull();

        User user4 = User.builder()
                .name("name4")
                .email("name4@mail.ru")
                .login("nameLogin4")
                .birthday(LocalDate.of(1945, 5, 9))
                .build();
        User userStr = dao.createUser(user4);
        assertThat(userStr).isNotNull();

        User user6 = User.builder()
                .name("name5")
                .email("name@tmail.ru")
                .login("nameLogin5")
                .birthday(LocalDate.of(2000, 6, 6))
                .build();
        User user6Opt = dao.createUser(user6);
        assertThat(user6Opt).isNotNull();
        dao.addFriend(user.getId(), user6Opt.getId());
        dao.addFriend(userStr.getId(), user6Opt.getId());
        List<User> friendsOfUser = List.of(user6);
        List<User> commonFriends = dao.findMutualFriends(user.getId(), userStr.getId());
        assertArrayEquals(friendsOfUser.toArray(), commonFriends.toArray());
    }

    @Test
    @DisplayName("удаление пользователя")
    public void deleteUser_Test() {
        UserDbStorage dao = new UserDbStorage(jdbcTemplate);
        User user4 = User.builder()
                .name("name4")
                .email("name4@mail.ru")
                .login("nameLogin4")
                .birthday(LocalDate.of(1945, 5, 9))
                .build();
        User userOptional = dao.createUser(user4);
        boolean idDeleted = dao.deleteById(userOptional.getId());
        assertTrue(idDeleted);
    }
}