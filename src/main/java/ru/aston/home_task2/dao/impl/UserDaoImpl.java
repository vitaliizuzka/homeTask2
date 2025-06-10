package ru.aston.home_task2.dao.impl;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.home_task2.dao.UserDao;
import ru.aston.home_task2.exceptions.SaveUserException;
import ru.aston.home_task2.model.entity.User;
import ru.aston.home_task2.utils.ConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private final String HQL_ALL_USERS = "from User";
    private SessionFactory sessionFactory;
    private Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Integer save(User user) throws SaveUserException {
        logger.info("try to save user {} dao level", user);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(user);
                transaction.commit();
                logger.info("user saved successfully dao level");
            } catch (Exception e) {
                logger.error("user wasn't to save dao level");
                transaction.rollback();
                throw new SaveUserException();
            }
        }
        return user.getId();
    }

    @Override
    public Optional<User> findById(int id) {
        logger.info("try to find user by id {} dao level", id);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User user = session.find(User.class, id);
                if (user == null) throw new EntityNotFoundException();
                transaction.commit();
                logger.info("user found successfully dao level");
                return Optional.of(user);
            } catch (Exception e) {
                logger.error("user wasn't to find dao level", e);
                transaction.rollback();
                return Optional.empty();
            }
        }

    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        logger.info("try to find all users dao level");
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                users.addAll(session.createQuery(HQL_ALL_USERS, User.class).list());
                transaction.commit();
                logger.info("users found successfully dao level");
            } catch (Exception e) {
                transaction.rollback();
                logger.error("users wasn't to find dao level, error: ", e);
                return users;
            }
        }
        return users;
    }

    @Override
    public boolean removeById(int id) {
        logger.info("try to delete user by id {} dao level", id);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Optional<User> optionalUser = findById(id);
                if (optionalUser.isEmpty()) throw new EntityNotFoundException();
                User user = optionalUser.get();
                session.remove(user);
                transaction.commit();
                logger.info("user was deleted successfully dao level");
            } catch (Exception e) {
                transaction.rollback();
                logger.error("user wasn't to deleted dao level ", e);
                return false;
            }
            return true;
        }
    }

    @Override
    public Integer update(User user) {
        logger.info("try to change user by id {} dao level", user.getId());
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                user = session.merge(user);
                transaction.commit();
                logger.info("user was saved or updated successfully dao level: {}", user);
            } catch (Exception e) {
                transaction.rollback();
                logger.error("user wasn't updated or save dao level, error: " + e.getMessage());
                return null;
            }
            return user.getId();
        }
    }
}
