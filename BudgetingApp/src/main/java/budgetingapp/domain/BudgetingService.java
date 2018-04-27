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
import java.util.List;
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
        this.accountDao = accountDao;
        this.signedIn = null;
        this.account = null;
    }

    public boolean createUser(String name, String username) throws SQLException {
        User u = userDao.findByUsername(username);
        if (u == null) {

            User user = new User(name, username);
            userDao.save(user);
            user = userDao.findByUsername(username);
            accountDao.createNewAccount(user.getId());
            return true;

        }
        return false;

    }

    public boolean signIn(String username) throws SQLException {
        this.signedIn = userDao.findByUsername(username);
        if (this.signedIn == null) {
            return false;
        }

        int id = this.signedIn.getId();
        //System.out.println(account);
        this.account = accountDao.findByUserId(id);
        signedIn.setAccount(this.account);

        return true;
    }

    public User updateUserInfo(String username) throws SQLException {
        this.signedIn = userDao.findByUsername(username);
        return this.signedIn;
    }

    public void signOut() {
        this.signedIn = null;
    }

    public User getSignedIn() {
        return this.signedIn;
    }

    public List getEvents(int id) throws SQLException {
        List<Event> events = accountDao.findAccountEvents(id);
        return events;
    }

    public List getMonthlyEvents(int id, int month, int year) throws SQLException {
        List<Event> list = accountDao.getMonthlyEvents(id, month, year);
        return list;

    }

    public boolean addEvent(Event e) throws SQLException {
        this.account.addOneEvent(e);

        return accountDao.addEvent(e);

    }

    public List<Double> getCategoryAmounts(Account a) throws SQLException {
        List<Double> list = accountDao.countCategories(a);
        return list;
    }

    public List<Double> getMonthlyCategoryAmounts(int id, int month, int year) throws SQLException {
        List<Double> list = accountDao.countMonthlyCategories(id, month, year);
        return list;

    }

    public void deleteEvents(int id) throws SQLException {
        accountDao.deleteEvents(id);
    }

    public void clearBalance() throws SQLException {
        accountDao.clearBalance(account);
        account.setBalance(0);
    }

}
