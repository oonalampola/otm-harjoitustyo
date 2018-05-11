/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.dao;

import budgetingapp.database.Database;
import budgetingapp.domain.User;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.*;

/**
 *
 * @author iskä
 */
public class UserDaoTest {

    UserDao userDao;
    Database database;
    User testuser;

    public UserDaoTest() {

    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ClassNotFoundException {

        File file = new File("test.db");
        file.delete();
        database = new Database("jdbc:sqlite:test.db");
        userDao = new UserDao(database);
        testuser = new User("Test User", "testuser");

    }

    @After
    public void tearDown() throws SQLException {
        userDao.delete("testuser");
        File file = new File("test.db");
        file.delete();
    }

    @Test
    public void deletingUser() throws SQLException {

        userDao.save(testuser);
        userDao.delete(testuser.getUsername());
        User user = userDao.findByUsername(testuser.getUsername());
        assertEquals(null, user);
    }

    @Test
    public void saveUser() throws SQLException {

        userDao.save(testuser);
        User foundUser = userDao.findByUsername(testuser.getUsername());
        assertEquals("testuser", foundUser.getUsername());

    }

    @Test
    public void findByUsername() throws SQLException {
        userDao.save(testuser);
        User foundUser = userDao.findByUsername("testuser");
        assertEquals("testuser", foundUser.getUsername());
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
