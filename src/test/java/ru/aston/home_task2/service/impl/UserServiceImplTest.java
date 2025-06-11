package ru.aston.home_task2.service.impl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.home_task2.dao.UserDao;
import ru.aston.home_task2.exceptions.SaveUserException;
import ru.aston.home_task2.model.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)//version 5+
class UserServiceImplTest {

    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserServiceImpl userService;

    private User userWithFirstId;

    @BeforeEach
    public void setup() {
        userWithFirstId = new User("Ignat", "ignat@gmail.com", 35);
        userWithFirstId.setId(1);
    }

    @Test
    @SneakyThrows
    void saveShouldSaveUser() {
        User testUser = new User("Ignat", "ignat@gmail.com", 35);
        when(userDao.save(testUser)).thenReturn(userWithFirstId.getId());
        Integer savedUserId = userService.save(testUser);
        assertEquals(userWithFirstId.getId(), savedUserId);
        verify(userDao, times(1)).save(testUser);
    }

    @Test
    @SneakyThrows
    void saveShouldNotSaveUser() {
        when(userDao.save(userWithFirstId)).thenThrow(new SaveUserException());
        assertNull(userService.save(userWithFirstId));
        verify(userDao, times(1)).save(userWithFirstId);
    }

    @Test
    void getUserByIdShouldFindUser() {

        when(userDao.findById(1)).thenReturn(Optional.of(userWithFirstId));

        Optional<User> foundUser = userService.findById(1);

        assertTrue(foundUser.isPresent());
        assertEquals(userWithFirstId.getName(), foundUser.get().getName());
        verify(userDao, times(1)).findById(1);
    }

    @Test
    void getUserByIdShouldNotFindUser() {

        when(userDao.findById(2)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findById(2);

        assertFalse(foundUser.isPresent());
        verify(userDao, times(1)).findById(2);
    }

    @Test
    void updateShouldUpdateUser() {
        //when(userDao.findById(1)).thenReturn(Optional.of(userWithFirstId));
        User user = userWithFirstId;
        user.setName("Nick");
        user.setAge(60);
        when(userDao.update(user)).thenReturn(user.getId());

        Integer updatedUserId = userService.update(user);

        assertEquals(user.getId(), updatedUserId);
        verify(userDao, times(1)).update(user);
    }

    @Test
    void updateShouldSaveUser() {

        User uppdateUser = new User("Vika", "vika@gmail.com", 20);
        assertNull(uppdateUser.getId());
        when(userDao.update(uppdateUser)).thenReturn(2);

        Integer savedUserId = userService.update(uppdateUser);
        assertNotNull(savedUserId);
        verify(userDao, times(1)).update(uppdateUser);
    }

    @Test
    void updateShouldNotUpdateWithUniqueEmailUser() {

        userWithFirstId.setEmail("vika@gmail.com");
        when(userDao.update(userWithFirstId)).thenReturn(null);

        Integer updatedUserId = userService.update(userWithFirstId);

        assertNull(updatedUserId);
        verify(userDao, times(1)).update(userWithFirstId);
    }


    @Test
    void removeByIdShouldRemoveUser() {

        when(userDao.removeById(1)).thenReturn(true);

        boolean isDeleted = userService.removeById(1);

        assertTrue(isDeleted);
        verify(userDao, times(1)).removeById(1);
    }

    @Test
    void removeByIdShouldNotRemoveUser() {

        when(userDao.removeById(10)).thenReturn(false);

        boolean isDeleted = userService.removeById(10);

        assertFalse(isDeleted);
        verify(userDao, times(1)).removeById(10);
    }


}