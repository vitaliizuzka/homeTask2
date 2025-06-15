package ru.aston.home_task2.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.home_task2.dao.UserDao;
import ru.aston.home_task2.dao.impl.UserDaoImpl;
import ru.aston.home_task2.exceptions.SaveUserException;
import ru.aston.home_task2.model.entity.User;
import ru.aston.home_task2.service.UserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Integer save(User user) {

        Integer id = null;
        logger.info("trying to save user {} service level", user);
        try {
            id = userDao.save(user);
            logger.info("user saved successfully service level");
            return id;
        } catch (SaveUserException e) {
            logger.error("user not saved service level, null returned");
        }
        return null;
    }

    @Override
    public Optional<User> findById(int id) {
        logger.info("trying to find user by id {} service level", id);
        Optional<User> optionalUser = userDao.findById(id);
        return optionalUser
                .map(user -> {
                    logger.info("user found successfully service level");
                    return Optional.of(user);
                })
                .orElseGet(() -> {
                    logger.error("user not found service level");
                    return Optional.empty();
                });

    }

    @Override
    public List<User> findAll() {
        logger.info("trying to find all users service level");
        List<User> users = userDao.findAll();
        if (!users.isEmpty()) {
            logger.info("users found successfully service level");
        } else {
            logger.error("users weren't found service level");
        }
        return users;
    }

    @Override
    public boolean removeById(int id) {
        logger.info("trying to delete user by id {} service level", id);
        boolean wasRemoved = userDao.removeById(id);
        if (wasRemoved) {
            logger.info("user was deleted successfully service level");
        } else {
            logger.error("user wasn't to deleted service level");
        }
        return wasRemoved;
    }

    @Override
    public Integer update(User user) {
        logger.info("trying to change user by id {} service level", user.getId());
        Integer id = userDao.update(user);
        if (id != null) {
            logger.info("user was saved or updated successfully service level: {}", user);
        } else {
            logger.error("user wasn't updated or save service level");
        }
        return id;
    }
}
