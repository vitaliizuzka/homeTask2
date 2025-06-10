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
        logger.info("try to save user {} service level", user);
        try {
            id = userDao.save(user);
            logger.info("user saved successfully service level");
            return id;
        } catch (SaveUserException e) {
            logger.error("user wasn't save service level, null returned");
        }
        return null;
    }

    @Override
    public Optional<User> findById(int id) {
        logger.info("try to find user by id {} service level", id);
        Optional<User> optionalUser = userDao.findById(id);
        return optionalUser
                .map(user -> {
                    logger.info("user found successfully service level");
                    return Optional.of(user);
                })
                .orElseGet(() -> {
                    logger.error("user wasn't to find service level");
                    return Optional.empty();
                });

    }

    @Override
    public List<User> findAll() {
       return userDao.findAll();
    }

    @Override
    public boolean removeById(int id) {
        return userDao.removeById(id);
    }

    @Override
    public Integer update(User user) {
        return userDao.update(user);
    }
}
