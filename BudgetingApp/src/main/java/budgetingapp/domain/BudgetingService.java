/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.domain;

import budgetingapp.dao.AccountDao;
import budgetingapp.dao.UserDao;
import budgetingapp.database.Database;
import java.io.File;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author oona
 */

public class BudgetingService {

    private Database database;
    private UserDao userDao;
    private AccountDao accountDao;
    private Scanner scanner;
    private User signedIn;
    private Account account;
    

    public BudgetingService(UserDao userDao, AccountDao accountDao) throws ClassNotFoundException {
        this.userDao = userDao;
        
        this.scanner = scanner;
        this.signedIn = null;
        this.account = null;
    }

    public boolean createUser(String name, String username) throws SQLException {

        User u = userDao.findByUsername(username);
        if (u == null) {
            try {
                User user = new User(name, username);
                userDao.save(user);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
//TEKSTIKÄYTTÖLIITTYMÄ
//    public void start() throws SQLException {
//        System.out.println("Welcome to BudgetingApp");
//        System.out.println("");
//        int action = 1;
//        while (action == 1 || action == 2) {
//            System.out.println("To sign in, enter 1 " + "\n"
//                    + "To create a new user, enter 2");
//            action = Integer.parseInt(scanner.nextLine());
//
//            if (action == 1) {
//                System.out.print("Enter username: ");
//                String username = scanner.nextLine();
//                signIn(username);
//
//                //Kirjautumisen jälkeinen toiminta
//            }
//            if (action == 2) {
//                System.out.println("What is your name? ");
//                String name = scanner.nextLine();
//                System.out.println("Username: ");
//                String username = scanner.nextLine();
//
//                boolean created = createUser(name, username);
//
//                if (created) {
//                    System.out.println("Username created, you can now sign in");
//                    continue;
//                } else {
//                    System.out.println("Username already exists, pick another one");
//                }
//
//            }
//        }
//    }

    public boolean signIn(String username) throws SQLException {
        User u = userDao.findByUsername(username);
        if (u == null) {
            System.out.println("User not found");
            return false;
        }
        System.out.println("Welcome, " + u.getName());
        this.signedIn = u;
        this.account = accountDao.findByUserId(u.getId());
        
        return true;
    }
    public void signOut(){
        this.signedIn = null;
    }
    public User getSignedIn(){
        return this.signedIn;
    }
    public void addEvent(Event e){
        account.addOneEvent(e);
        
        
    }

}
