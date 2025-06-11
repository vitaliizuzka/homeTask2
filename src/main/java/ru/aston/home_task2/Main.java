package ru.aston.home_task2;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.home_task2.dao.UserDao;
import ru.aston.home_task2.dao.impl.UserDaoImpl;
import ru.aston.home_task2.model.entity.User;
import ru.aston.home_task2.service.UserService;
import ru.aston.home_task2.service.impl.UserServiceImpl;
import ru.aston.home_task2.utils.ConnectionManager;

public class Main {



    public static void main(String[] args) {

        UserDao userDao = new UserDaoImpl(ConnectionManager.getSessionFactory());
        UserService userService = new UserServiceImpl(userDao);

        User user1 = new User("Ignat", "ignatkoka@gmail.com", 25);
        userService.save(user1);
        user1.setAge(50);
        user1.setName("Nick");
        User user2 = new User("Nick", "ignatkoka@gmail.com", 50);
        System.out.println(userService.update(user1));
        System.out.println("-----------------");
        System.out.println(userService.findById(13));
        System.out.println(userService.findById(100));

        System.out.println("-----------");
        System.out.println(userService.findAll());
        System.out.println("-----------");
        System.out.println(userService.removeById(1));

    }
}
