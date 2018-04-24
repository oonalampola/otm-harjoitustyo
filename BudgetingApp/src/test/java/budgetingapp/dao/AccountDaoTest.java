/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.dao;

import budgetingapp.database.Database;
import budgetingapp.domain.Account;
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

        File file = new File("db", "budappdata.db");
        database = new Database("jdbc:sqlite:" + file.getAbsolutePath());
        accountDao = new AccountDao(database);
        accountDao.delete(0);
        testAccount = new Account(0, 500);
    }

    @After
    public void tearDown() throws SQLException {
        accountDao.delete(0);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
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

}
