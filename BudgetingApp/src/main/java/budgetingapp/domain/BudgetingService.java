/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.domain;

import budgetingapp.dao.UserDao;
import budgetingapp.database.Database;
import java.io.File;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author oona
 */
//Sovelluslogiikka
public class BudgetingService {

    private Database database;
    private UserDao userDao;
    private Scanner scanner;

    public BudgetingService(UserDao userDao, Scanner scanner) throws ClassNotFoundException {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public boolean createUser(String name, String username) throws SQLException {

        userDao.findByUsername(username);

        if (userDao.findByUsername(username) == null) {
            User user = new User(name, username);
            try {
                userDao.save(user);
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public void start() throws SQLException {
        System.out.println("Welcome to BudgetingApp");
        System.out.println("");
        int action = 1;
        while (action == 1 || action == 2) {
            System.out.println("To sign in, enter 1 " + "\n"
                    + "To create a new user, enter 2");
            action = Integer.parseInt(scanner.nextLine());

            if (action == 1) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();

                //Kirjautumisen jälkeinen toiminta
            }
            if (action == 2) {
                System.out.println("What is your name? ");
                String name = scanner.nextLine();
                System.out.println("Username: ");
                String username = scanner.nextLine();

                boolean created = createUser(name, username);

                if (created) {
                    System.out.println("Username created, you can now sign in");
                } else {
                    System.out.println("Username already exists, pick another one");
                }

            }
        }
    }

}
