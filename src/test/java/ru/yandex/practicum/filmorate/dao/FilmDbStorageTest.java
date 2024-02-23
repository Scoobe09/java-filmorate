package ru.yandex.practicum.filmorate.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmDbStorageTest {
    final JdbcTemplate jdbcTemplate;


    @Test
    @DisplayName("Популярные фильмы")
    public void getPopularFilm_Test() {
        UserDbStorage userDao = new UserDbStorage(jdbcTemplate);
        FilmDbStorage dao = new FilmDbStorage(jdbcTemplate);
        Film film = Film.builder()
                .name("brother")
                .description("Power in love")
                .releaseDate(LocalDate.of(1997, 7, 14))
                .duration(240)
                .mpa(Mpa.builder().id(3).build())
                .build();
        Film film1 = dao.createFilm(film);
        assertThat(film1).isNotNull();

        Film film2 = Film.builder()
                .name("name1")
                .description("eurifh")
                .releaseDate(LocalDate.of(1995, 3, 3))
                .duration(480)
                .mpa(Mpa.builder().id(4).build())
                .build();
        Film film3 = dao.createFilm(film2);
        assertThat(film3).isNotNull();

        User user = User.builder()
                .name("name1")
                .email("nanana@mail.ru")
                .login("login1")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();
        User user1Opt = userDao.createUser(user);
        assertThat(user1Opt).isNotNull();

        User user1 = User.builder()
                .name("name2")
                .email("zipfig@mail.ru")
                .login("login2")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();
        User user2Opt = userDao.createUser(user1);
        assertThat(user2Opt).isNotNull();
        boolean isAddedTrue1 = dao.addLike(film3.getId(), user1Opt.getId());

        boolean isAddedTrue2 = dao.addLike(film3.getId(), user2Opt.getId());
        assertTrue(isAddedTrue1);
        assertTrue(isAddedTrue2);

        dao.addLike(film1.getId(), user1Opt.getId());
        List<Film> popularFilm = dao.findMostPopularFilms(1);
        for (Film film5 : popularFilm) {
            System.out.println(film5);
        }
        assertNotNull(popularFilm);
        assertEquals(1, popularFilm.size());
    }

    @Test
    @DisplayName("Добавление лайкаи и его удаление")
    public void addLikeAndRemove_Test() {
        UserDbStorage userDao = new UserDbStorage(jdbcTemplate);
        FilmDbStorage dao = new FilmDbStorage(jdbcTemplate);
        Film film = Film.builder()
                .name("brother")
                .description("Power in knowledge")
                .releaseDate(LocalDate.of(1997, 7, 14))
                .duration(240)
                .mpa(Mpa.builder().id(3).build())
                .build();
        Film film1 = dao.createFilm(film);
        User user = User.builder()
                .name("name1")
                .email("nanana@mail.ru")
                .login("login1")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();
        User user1Opt = userDao.createUser(user);
        boolean isAddedLike = dao.addLike(film1.getId(), user1Opt.getId());
        boolean removeLike = dao.deleteLike(film1.getId(), user1Opt.getId());
        assertTrue(isAddedLike);
        assertTrue(removeLike);
    }


    @Test
    @DisplayName("поиск фильма по его айди")
    public void findById_Test() {
        FilmDbStorage dao = new FilmDbStorage(jdbcTemplate);
        Film film = Film.builder()
                .name("brother")
                .description("Power in love")
                .releaseDate(LocalDate.of(1997, 7, 14))
                .duration(240)
                .mpa(Mpa.builder().id(3).build())
                .build();
        Film film1 = dao.createFilm(film);
        Film film2 = dao.findById(film1.getId());
        assertThat(film1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film2);
    }

    @Test
    @DisplayName("Обновление фльма")
    public void updateFilm_Test() {
        FilmDbStorage dao = new FilmDbStorage(jdbcTemplate);
        Film film = Film.builder()
                .name("brother")
                .description("Power in love")
                .releaseDate(LocalDate.of(1997, 7, 14))
                .duration(240)
                .mpa(Mpa.builder().id(3).build())
                .build();
        Film film1 = dao.createFilm(film);

        film = Film.builder()
                .id(10)
                .name("name1")
                .description("eurifh")
                .releaseDate(LocalDate.of(1495, 3, 3))
                .duration(480)
                .mpa(Mpa.builder().id(4).build())
                .build();
        Film firstFilm = film;
        Film film2 = dao.updateFilm(film1);
        assertThat(film2)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film2);
        assertNotEquals(firstFilm, film2);

    }

    @Test
    @DisplayName("удаление фильма")
    public void deleteFilm_Test() {
        FilmDbStorage dao = new FilmDbStorage(jdbcTemplate);
        Film film = Film.builder()
                .name("brother")
                .description("Power in love")
                .releaseDate(LocalDate.of(1997, 7, 14))
                .duration(240)
                .mpa(Mpa.builder().id(3).build())
                .build();
        Film film1 = dao.createFilm(film);
        boolean isDeletedFilm = dao.deleteById(film1.getId());
        assertTrue(!isDeletedFilm); // проверяю что фильм действительно обвновился
    }
}