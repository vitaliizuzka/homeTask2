package ru.aston.home_task2.service;

import ru.aston.home_task2.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Integer save(User user);
    Optional<User> findById(int id);
    List<User> findAll();
    boolean removeById(int id);
    Integer update(User user);
}
