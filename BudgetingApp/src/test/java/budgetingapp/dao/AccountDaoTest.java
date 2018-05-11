/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.dao;

import budgetingapp.database.Database;
import budgetingapp.domain.Account;
import budgetingapp.domain.Event;
import budgetingapp.domain.User;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author oona
 */
public class AccountDaoTest {

    AccountDao accountDao;
    Database database;
    Account testAccount;

    public AccountDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {

        File file = new File("test.db");
        database = new Database("jdbc:sqlite:test.db");
        database.init();
        accountDao = new AccountDao(database);
        accountDao.delete(0);
        testAccount = new Account(0, 500);

    }

    @After
    public void tearDown() throws SQLException {
        accountDao.delete(0);
        File file = new File("test.db");
        file.delete();
    }

    @Test
    public void deletingAccount() throws SQLException {

        accountDao.save(testAccount);
        accountDao.delete(testAccount.getUserId());
        Account account = accountDao.findByUserId(testAccount.getUserId());
        assertEquals(null, account);
    }

    @Test
    public void createNewAccount() throws SQLException {

        accountDao.createNewAccount(testAccount.getUserId());
        Account foundA = accountDao.findByUserId(testAccount.getUserId());
        assertTrue(0 == foundA.getUserId());

    }

    @Test
    public void updateBalance() throws SQLException {
        testAccount = accountDao.updateBalance(testAccount, -100);

        assertTrue(400 == testAccount.getBalance());
    }

    @Test
    public void addEventReturnsTrue() throws SQLException {
        accountDao.save(testAccount);
        Event e = new Event(500, 0);
        assertTrue(accountDao.addEvent(e));

    }

    @Test
    public void countCategoriesReturnsAmounts() throws SQLException {
        accountDao.save(testAccount);
        accountDao.deleteEvents(0);
        Event e = new Event(700, 0);
        e.setCategory(1);
        accountDao.addEvent(e);
        Event e2 = new Event(200, 0);
        e2.setCategory(2);
        accountDao.addEvent(e2);

        List<Double> list = accountDao.countCategories(testAccount);
        assertTrue(700.0 == list.get(0));
        assertTrue(200.0 == list.get(1));

    }

    @Test
    public void countMonthlyCategoriesReturnsAmounts() throws SQLException {
        accountDao.save(testAccount);
        accountDao.deleteEvents(0);
        Event e = new Event(700, 0);
        e.setCategory(1);
        e.setTime(5, 2018);
        accountDao.addEvent(e);
        Event e2 = new Event(200, 0);
        e2.setCategory(2);
        e2.setTime(5, 2018);

        accountDao.addEvent(e2);

        List<Double> list = accountDao.countMonthlyCategories(0, 5, 2018);
        assertTrue(700.0 == list.get(0));
        assertTrue(200.0 == list.get(1));

    }

    @Test
    public void getMonthlyEvents() throws SQLException {

        accountDao.save(testAccount);
        accountDao.deleteEvents(0);
        Event e = new Event(700, 0);
        e.setCategory(1);
        e.setTime(5, 2018);
        accountDao.addEvent(e);
        Event e2 = new Event(200, 0);
        e2.setCategory(2);
        e2.setTime(5, 2018);

        accountDao.addEvent(e2);

        List<Event> list = accountDao.getMonthlyEvents(0, 5, 2018);
        assertEquals(5, list.get(0).getMonth());
        assertEquals(2018, list.get(0).getYear());

    }

    @Test
    public void clearBalanceClears() throws SQLException {
        accountDao.save(testAccount);
        accountDao.clearBalance(testAccount);
        testAccount = accountDao.findByUserId(0);
        assertTrue(0 == testAccount.getBalance());

    }

}
