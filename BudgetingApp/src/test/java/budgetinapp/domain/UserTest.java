/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetinapp.domain;

import budgetingapp.database.Database;
import budgetingapp.domain.User;
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
public class UserTest {

    User testuser;
    Database database;

    public UserTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.testuser = new User("Test User", "testuser");

    }

    @After
    public void tearDown() {
    }

    @Test
    public void createdUserExists() {
        assertTrue(testuser != null);
    }

    @Test
    public void constructorSetsName() {
        assertEquals("Test User", testuser.getName());
    }

    @Test
    public void contructorsSetsUsername() {
        assertEquals("testuser", testuser.getUsername());
    }
}
