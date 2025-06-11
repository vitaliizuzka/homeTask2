package ru.aston.home_task2.dao.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.aston.home_task2.dao.UserDao;
import ru.aston.home_task2.exceptions.SaveUserException;
import ru.aston.home_task2.model.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDaoImplIntegrationTest {

    private static final Properties testProperties;
    @Container
    private static final PostgreSQLContainer<?> postgres;
    private static SessionFactory sessionFactory;
    private static UserDao userDao;


    static {
        testProperties = loadTestProperties();
        postgres = new PostgreSQLContainer<>(
                testProperties.getProperty("testcontainers.postgres.image"))
                .withDatabaseName(testProperties.getProperty("hibernate.connection.dbname"))
                .withUsername(testProperties.getProperty("hibernate.connection.username"))
                .withPassword(testProperties.getProperty("hibernate.connection.password"));
    }

    private static Properties loadTestProperties() {
        Properties properties = new Properties();
        try (InputStream input = UserDaoImplIntegrationTest.class.getClassLoader().getResourceAsStream("test.properties")) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test properties", e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        Configuration configuration = new Configuration();
        configuration.setProperties(testProperties);
        configuration.addAnnotatedClass(User.class);
        sessionFactory = configuration.buildSessionFactory();
        userDao = new UserDaoImpl(sessionFactory);
    }

    @Test
    @Order(1)
    @SneakyThrows
    void saveShouldSaveUser() {
        User user = new User("Ignat", "Ignat@gmail.com", 35);
        Integer id = userDao.save(user);
        assertNotNull(id);
    }

    @Test
    @Order(2)
    void saveShouldNotSaveUser() {
        User user = new User("Ignatko", "Ignat@gmail.com", 20);
        assertThrows(SaveUserException.class, () -> userDao.save(user));
    }

    @Test
    @Order(3)
    void findByIdShouldFindUser() {
        User user = userDao.findById(1).get();
        assertNotNull(user);
    }

    @Test
    @Order(4)
    void findByIdShouldNotFindUser() {
       assertTrue(userDao.findById(10).isEmpty());
    }

    @Test
    @Order(5)
    void updateShouldUpdateUser() {
        User user = userDao.findById(1).get();
        user.setName("Nick");
        user.setAge(60);
        Integer idBefore = user.getId();
        Integer idAfter = userDao.update(user);
        assertEquals(idBefore, idAfter);
    }

    @Test
    @Order(6)
    void updateShouldSaveUser() {
        User uppdateUser = new User("Vika", "vika@gmail.com", 20);
        assertNull(uppdateUser.getId());
        Integer idBefore = uppdateUser.getId();
        Integer idAfter = userDao.update(uppdateUser);
        assertNotNull(idAfter);
    }

    @Test
    @Order(7)
    void updateShouldNotUpdateWithUniqueEmailUser() {
        User user = userDao.findById(1).get();
        user.setEmail("vika@gmail.com");
        assertNull(userDao.update(user));
    }

    @Test
    @Order(8)
    void sizeShouldSizeEqualsTwo() {
        List<User> users = userDao.findAll();
        assertEquals(users.size(), 2);
    }

    @Test
    @Order(9)
    void removeByIdShouldRemoveUser() {
        assertTrue(userDao.removeById(1));
    }

    @Test
    @Order(10)
    void sizeShouldSizeEqualsOne() {
        List<User> users = userDao.findAll();
        assertEquals(users.size(), 1);
    }

    @Test
    @Order(11)
    void removeByIdShouldNotRemoveUser() {
        assertFalse(userDao.removeById(10));
    }


}