/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.dao;

import budgetingapp.database.Database;
import budgetingapp.domain.Account;
import budgetingapp.domain.Event;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

/**
 *
 * @author oona
 */
public class AccountDao implements Dao<Account, Integer> {

    Database database;
    Timestamp timestamp;

    public AccountDao(Database database) {
        this.database = database;
    }

    public void createNewAccount(int userId) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("INSERT INTO Account (user_id, balance) VALUES (?, ?)");
        stmt2.setInt(1, userId);
        stmt2.setInt(2, 0);
        stmt2.execute();
        stmt2.close();
        c.close();
    }

    public Account findByUserId(int id) throws SQLException {

        Connection c = database.getConnection();
        System.out.println("accoundao yhteys otettu");
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Account WHERE user_id = ?");
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        System.out.println("kysely tehty");
        boolean hasOne = rs.next();
        System.out.println(hasOne);
        if (!hasOne) {
            System.out.println("palautetaan null account");
            return null;
        }
        int foundId = rs.getInt("user_id");
        double balance = rs.getDouble("balance");
        System.out.println("id ja balance: " + foundId + ", " + balance);
        Account a = new Account(foundId, balance);
        System.out.println(a + "accoundao");
        //a.addEvents(findAccountEvents(id));
        stmt.close();
        c.close();
        rs.close();

        return a;
    }

    public List findAccountEvents(int id) throws SQLException {

        List<Event> list = new ArrayList<>();
        Connection c = database.getConnection();
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Event WHERE account_id = ? ORDER BY time");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Event e = new Event(rs.getDouble("amount"), rs.getInt("account_id"));
            list.add(e);
            System.out.println(e.getAmount());
        }
        stmt.close();
        c.close();
        rs.close();
        return list;
    }

    public boolean addEvent(Event e) throws SQLException {
        this.timestamp = new Timestamp(System.currentTimeMillis());

        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("INSERT INTO Event (amount, time, category, account_id) VALUES (?, ?, ?, ?)");
        stmt2.setDouble(1, e.getAmount());
        stmt2.setTimestamp(2, timestamp);
        stmt2.setInt(3, e.getCategory());
        stmt2.setInt(4, e.getAccountId());

        stmt2.execute();

        stmt2.close();
        double eventsAmount = 0;
        if (!(e.getCategory() == 0)) {
            eventsAmount = e.getAmount() * (-1);
        } else {
            eventsAmount = e.getAmount();
        }
        Account a = findByUserId(e.getAccountId());
        updateBalance(a, eventsAmount);

        c.close();

        return true;

    }

    @Override
    public Account findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Account> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save(Account a) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("INSERT INTO Account (balance, user_id) VALUES (?, ?)");
        stmt2.setInt(1, 0);
        stmt2.setInt(2, a.getUserId());
        stmt2.execute();

        stmt2.close();
        c.close();

    }

    @Override
    public void update(Account object) throws SQLException {

    }

    @Override
    public void delete(Integer key) throws SQLException {
        if (findByUserId(key) != null) {
            Connection c = database.getConnection();
            PreparedStatement stmt = c.prepareStatement("DELETE FROM Account WHERE user_id=?");
            stmt.setInt(1, key);
            stmt.execute();
            stmt.close();
            c.close();
        }
    }

    public Account updateBalance(Account a, double amount) throws SQLException {
        double updatedBalance = a.getBalance() + amount;

        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("UPDATE Account SET balance = ? WHERE user_id= ?");

        stmt2.setDouble(1, updatedBalance);
        stmt2.setInt(2, a.getUserId());
        stmt2.executeUpdate();

        stmt2.close();

        c.close();
        a.setBalance(a.getBalance() + amount);
        return a;
    }

    public List<Double> countCategories(Account a) throws SQLException {

        List<Double> amounts = new ArrayList<>();
        amounts.add(countLiving(a));
        amounts.add(countFood(a));
        amounts.add(countGoods(a));
        amounts.add(countSpareTime(a));

        return amounts;

    }

    public double countLiving(Account a) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("SELECT SUM(amount) FROM Event WHERE category = 1 AND account_id = ?");

        stmt2.setInt(1, a.getUserId());

        ResultSet rs = stmt2.executeQuery();

        if (!rs.next()) {
            return 0;
        }
        double amount = rs.getDouble(1);

        System.out.println("Living: " + amount);
        stmt2.close();
        rs.close();
        c.close();

        return amount;
    }

    public double countFood(Account a) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("SELECT SUM(amount) FROM Event WHERE category = 2 AND account_id = ?");

        stmt2.setInt(1, a.getUserId());

        ResultSet rs = stmt2.executeQuery();

        if (!rs.next()) {
            return 0;
        }
        double amount = rs.getDouble(1);
        stmt2.close();
        rs.close();
        c.close();

        return amount;
    }

    public double countGoods(Account a) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("SELECT SUM(amount) FROM Event WHERE category = 3 AND account_id = ?");

        stmt2.setInt(1, a.getUserId());

        ResultSet rs = stmt2.executeQuery();

        if (!rs.next()) {
            return 0;
        }
        double amount = rs.getInt(1);

        stmt2.close();
        rs.close();
        c.close();

        return amount;
    }

    public double countSpareTime(Account a) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("SELECT SUM(amount) FROM Event WHERE category = 4 AND account_id = ?");

        stmt2.setInt(1, a.getUserId());

        ResultSet rs = stmt2.executeQuery();

        if (!rs.next()) {
            return 0;
        }
        double amount = rs.getDouble(1);

        stmt2.close();
        rs.close();
        c.close();

        return amount;
    }

    public void deleteEvents(int id) throws SQLException {

        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("DELETE FROM Event WHERE account_id=?");

        stmt2.setInt(1, id);
        stmt2.executeUpdate();

        stmt2.close();

        c.close();

    }

    public void clearBalance(Account a) throws SQLException {

        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("UPDATE Account SET balance = ? WHERE user_id= ?");

        stmt2.setDouble(1, 0);
        stmt2.setInt(2, a.getUserId());
        stmt2.executeUpdate();

        stmt2.close();

        c.close();

    }

}
