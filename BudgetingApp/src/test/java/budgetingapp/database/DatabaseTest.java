/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.database;

import budgetingapp.dao.AccountDao;
import budgetingapp.dao.UserDao;
import budgetingapp.database.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
public class DatabaseTest {

    UserDao userDao;
    AccountDao accountDao;
    Database database;

    public DatabaseTest() throws ClassNotFoundException {
        this.database = new Database("jdbc:sqlite:test.db");
    }

    void beforeEach() throws ClassNotFoundException, SQLException {
        File file = new File("db", "test.db");
        file.delete();

        this.database = new Database("jdbc:sqlite:test.db");

        Connection c = database.getConnection();
        initializeDatabase(c);

        userDao = new UserDao(database);
        accountDao = new AccountDao(database);
        c.close();
    }

    public void initializeDatabase(Connection c) throws SQLException {

//        ArrayList<String> list = new ArrayList<>();
//
//        list.add("CREATE TABLE IF NOT EXISTS User(id integer PRIMARY KEY, name varchar(200), username varchar(200));");
//        list.add("CREATE TABLE IF NOT EXISTS Account(user_id integer PRIMARY KEY, balance float)");
//        list.add("CREATE TABLE IF NOT EXISTS Event(amount float, month integer, year integer, category integer, account_id integer, FOREIGN KEY (account_id) REFERENCES Account);");
//
//        for (int i = 0; i < list.size(); i++) {
//            PreparedStatement stmt = c.prepareStatement(list.get(i));
//            stmt.execute();
//            stmt.close();
//        }
        ArrayList<String> list2 = new ArrayList<>();

        list2.add("INSERT INTO User (id, name, username) VALUES (1, 'Test user', 'testuser')");
        list2.add("INSERT INTO Account (user_id, balance) VALUES (1, 0)");
        list2.add("INSERT INTO Event (amount, month, year, category, account_id) VALUES (500, 1, 2018, 1, 1)");

        for (int i = 0; i < list2.size(); i++) {
            PreparedStatement stmt = c.prepareStatement(list2.get(i));
            stmt.execute();
            stmt.close();
        }
    }

    @Test
    public void tableUserExists() throws SQLException {

        Connection c = database.getConnection();
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM User;");
        ResultSet rs = stmt.executeQuery();
        assertTrue(rs.next());
        c.close();
        rs.close();

    }

    @Test
    public void tableAccountExists() throws SQLException {

        Connection c = database.getConnection();
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Account");
        ResultSet rs = stmt.executeQuery();

        assertTrue(rs.next());
        c.close();
        rs.close();
    }

    @Test
    public void tableEventExists() throws SQLException {

        Connection c = database.getConnection();
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Event");
        ResultSet rs = stmt.executeQuery();

        assertTrue(rs.next());
        c.close();
        rs.close();
    }

    @Test
    public void initCreatesTables() throws SQLException {
        database.init();
        tableUserExists();
        tableAccountExists();
        tableEventExists();
    }

    @Test
    public void tablesAreCorrect() {

        List<String> list = database.tables();
        assertEquals("CREATE TABLE IF NOT EXISTS User(id integer "
                + "PRIMARY KEY, name varchar(200), username varchar(200))", list.get(0));

        assertEquals("CREATE TABLE IF NOT EXISTS Account"
                + "(user_id integer PRIMARY KEY, balance float)", list.get(1)); 
        
        assertEquals("CREATE TABLE IF NOT EXISTS Event"
                              + "(amount float, month integer, year integer, category integer, "
                              + "account_id integer, FOREIGN KEY (account_id) REFERENCES Account)", list.get(2));
        
//        assertTrue(list.get(0).equals("CREATE TABLE IF NOT EXISTS User(id integer "
                //                + "PRIMARY KEY, name varchar(200), username varchar(200))"));
                //        assertTrue(list.get(1).equals("CREATE TABLE IF NOT EXISTS Account"
                //                + "(user_id integer PRIMARY KEY, balance float)"));
                //        assertTrue(list.get(2).equals ("CREATE TABLE IF NOT EXISTS Event"
                //                + "(amount float, month integer, year integer, category integer, "
                //                + "account_id integer, FOREIGN KEY (account_id) REFERENCES Account);"));
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {

        //  this.database = new Database("jdbc:sqlite:test.db");
        //      Connection c = database.getConnection();
//        initializeDatabase(c);
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
