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
import budgetingapp.domain.User;
import java.io.File;
import java.sql.SQLException;
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
        File file = new File("db", "budappdata.db");
        database = new Database("jdbc:sqlite:" + file.getAbsolutePath());
        userDao = new UserDao(database);
        accountDao = new AccountDao(database);

        budService = new BudgetingService(userDao, accountDao);

    }

    @Test
    public void signInSignsIn() throws SQLException {
        String name = "Test User";
        String username = "testuser9999";
        budService.createUser(name, username);
        budService.signIn(username);
        
        assertTrue(null != budService.getSignedIn());
    }

    @Test
    public void CreateUserCreatesUserAndAccount() throws SQLException {
        String name = "Test User";
        String username = "testuser9999";
        budService.createUser(name, username);

        User u = userDao.findByUsername("testuser9999");
        Account a = u.getAccount();

        assertEquals("testuser9999", u.getUsername());
        assertTrue(u.getId() == a.getUserId());

    }

    @After
    public void tearDown() throws SQLException {
        User user = userDao.findByUsername("testuser9999");
        accountDao.delete(user.getId());

        userDao.delete("testuser9999");
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
