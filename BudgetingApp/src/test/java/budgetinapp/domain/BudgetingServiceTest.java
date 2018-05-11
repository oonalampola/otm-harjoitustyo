/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetinapp.domain;

import budgetingapp.dao.AccountDao;
import budgetingapp.dao.UserDao;
import budgetingapp.database.Database;
import budgetingapp.domain.Account;
import budgetingapp.domain.BudgetingService;
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
 * @author iskä
 */
public class BudgetingServiceTest {

    BudgetingService budService;
    UserDao userDao;
    AccountDao accountDao;
    Database database;
    File file;

    public BudgetingServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ClassNotFoundException {
        File file = new File("test.db");
        database = new Database("jdbc:sqlite:test.db");
        database.init();
        userDao = new UserDao(database);
        accountDao = new AccountDao(database);

        budService = new BudgetingService(userDao, accountDao);

    }

    @Test
    public void signInSignsInIfUserExists() throws SQLException {
        String name = "Test User";
        String username = "testuser";
        budService.createUser(name, username);
        budService.signIn(username);

        assertTrue(null != budService.getSignedIn());
        budService.signIn("invisibleuser");
        assertFalse(budService.signIn("invisibleuser"));

    }

    @Test
    public void CreateUserCreatesUserAndAccount() throws SQLException {
        String name = "Test User";

        String username = "testuser";
        budService.createUser(name, username);

        User u = userDao.findByUsername("testuser");
        Account a = accountDao.findByUserId(u.getId());

        assertEquals("testuser", u.getUsername());
        assertTrue(u.getId() == a.getUserId());

    }

    @Test
    public void clearingBalanceSetsBalanceToZero() throws SQLException {
        String name = "Testing Testing";
        String username = "testinguser";

        budService.createUser(name, username);
        budService.signIn(username);

        Event e = new Event(200, budService.getSignedIn().getId());
        assertTrue(budService.addEvent(e));
        User u = userDao.findByUsername("testinguser");

        budService.clearBalance();
        assertTrue(0 == budService.account.getBalance());
        Account a = accountDao.findByUserId(u.getId());

        assertTrue(0 == a.getBalance());
    }

    @Test
    public void updatingUserInfo() throws SQLException {
        String name = "Testing User";
        String username = "testuser2";

        budService.createUser(name, username);

        budService.signIn("testuser2");

        User u = budService.getSignedIn();
        Account a = u.getAccount();
        Event e = new Event(200, budService.getSignedIn().getId());
        budService.addEvent(e);
        u = budService.updateUserInfo("testuser2");

        assertTrue(200.0 == u.getAccountBalance());
        userDao.delete(u.getUsername());
        accountDao.delete(u.getId());
    }

    @Test
    public void addEventAdds() throws SQLException {
        String name = "Testing User";
        String username = "testuser2";

        budService.createUser(name, username);
        budService.signIn("testuser2");
        Event e = new Event(500, budService.getSignedIn().getId());
        budService.addEvent(e);

        assertTrue(budService.getEvents(budService.getSignedIn().getId()).get(0) != null);

    }

    @Test
    public void getCategroies() throws SQLException {
        String name = "Testing User";
        String username = "testuser2";

        budService.createUser(name, username);
        budService.signIn("testuser2");
        Event e = new Event(500, budService.getSignedIn().getId());
        Event e2 = new Event(200, budService.getSignedIn().getId());
        e.setCategory(1);
        e2.setCategory(2);

        budService.addEvent(e);
        budService.addEvent(e2);
        Account a = budService.account;
        List<Double> list = budService.getCategoryAmounts(a);

        assertTrue(500 == list.get(0));
        assertTrue(200 == list.get(1));

    }

    @Test
    public void getMonthlyCategories() throws SQLException {
        String name = "Testing User";
        String username = "testuser2";

        budService.createUser(name, username);
        budService.signIn("testuser2");
        Event e = new Event(500, budService.getSignedIn().getId());
        Event e2 = new Event(200, budService.getSignedIn().getId());
        e.setCategory(1);
        e2.setCategory(2);
        e.setTime(5, 2018);
        e2.setTime(4, 2018);
        budService.addEvent(e);
        budService.addEvent(e2);

        Account a = budService.account;
        List<Double> list = budService.getMonthlyCategoryAmounts(a.getUserId(), 4, 2018);

        assertTrue(200 == list.get(1));
        assertTrue(list.get(0) == 0);
    }

    @After
    public void tearDown() throws SQLException {
        File file = new File("test.db");
        file.delete();
        budService.signOut();

        userDao.delete("testuser2");
        userDao.delete("testinguser");

    }

}
