package ru.aston.home_task2.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.aston.home_task2.model.entity.User;

public class ConnectionManager {

    private static Configuration configuration;
    private static SessionFactory sessionFactory;

    static {
        configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void shutdownSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }

}
