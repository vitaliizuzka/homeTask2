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

import java.util.Optional;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {

        UserDao userDao = new UserDaoImpl(ConnectionManager.getSessionFactory());
        UserService userService = new UserServiceImpl(userDao);

        boolean fExit = false;
        Scanner scanner = new Scanner(System.in);
        int operationNumber = 0;
        Integer id = 0;
        String name = null;
        String email = null;
        int age = 0;

        while (!fExit) {

            System.out.println("Input the number of operation");
            System.out.println("1. Creating");
            System.out.println("2. Show all");
            System.out.println("3. Find by id");
            System.out.println("4. Updating");
            System.out.println("5. Deleting");
            System.out.println("6. Exit");

            try {
                operationNumber = scanner.nextInt();
                scanner.skip("\n");
                switch (operationNumber) {
                    case 1:
                        System.out.println("Input the name");
                        name = scanner.nextLine();
                        System.out.println("Input the email");
                        email = scanner.nextLine();
                        System.out.println("Input the age");
                        age = scanner.nextInt();

                        userService.save(new User(name, email, age));

                        break;
                    case 2:
                        System.out.println("User list:");
                        for (User user : userService.findAll()) {
                            System.out.println(user);
                        }
                        break;
                    case 3:
                        System.out.println("Input the user's id");
                        id = scanner.nextInt();
                        scanner.skip("\n");
                        Optional<User> optionalUser = userService.findById(id);
                        optionalUser.ifPresent(System.out::println);
                        break;
                    case 4:
                        System.out.println("Input the information for user update");
                        System.out.println("Input the user's id");
                        id = scanner.nextInt();
                        scanner.skip("\n");
                        System.out.println("Input the name for updating");
                        name = scanner.nextLine();
                        System.out.println("Input the email for updating");
                        email = scanner.nextLine();
                        System.out.println("Input the age for updating");
                        age = scanner.nextInt();
                        userService.update(new User(id, name, email, age));
                        break;
                    case 5:
                        System.out.println("Input the user's id for deleting");
                        id = scanner.nextInt();
                        scanner.skip("\n");
                        userService.removeById(id);
                        break;
                    case 6:
                        fExit = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Input error, try again");
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }
        }
    }

}
