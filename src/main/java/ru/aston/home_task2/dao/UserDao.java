package ru.aston.home_task2.dao;

import ru.aston.home_task2.exceptions.SaveUserException;
import ru.aston.home_task2.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Integer save(User user) throws SaveUserException;
    Optional<User> findById(int id);
    List<User> findAll();
    boolean removeById(int id);
    Integer update(User user);

}
