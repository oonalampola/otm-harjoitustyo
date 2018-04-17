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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author oona
 */
public class AccountDao implements Dao<Account, Integer> {

    Database database;

    public AccountDao(Database database) {
        this.database = database;
    }

    public void createNewAccount(int userId) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("INSERT INTO Account (balance, user_id) VALUES (?, ?)");
        stmt2.setInt(1, 0);
        stmt2.setInt(2, userId);
        stmt2.execute();
        stmt2.close();
        c.close();
    }

    public Account findByUserId(int id) throws SQLException {

        Connection c = database.getConnection();
        System.out.println("accoundao yhteys otettu");
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Account WHERE id = ?");
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        System.out.println("kysely tehty");
        boolean hasOne = rs.next();
        System.out.println(hasOne);
        if (!hasOne) {
            System.out.println("palautetaan null account");
            return null;
        }
        int foundId = rs.getInt("id");
        int balance = rs.getInt("balance");
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
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Event WHERE account_id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Event e = new Event(rs.getInt("amount"), rs.getBoolean("inOrPay"), rs.getInt("account_id"));
            list.add(e);
            System.out.println(e.getAmount());
        }
        stmt.close();
        c.close();
        rs.close();
        return list;
    }

    public boolean addEvent(Event e) throws SQLException {

        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("INSERT INTO Event (amount, inorpay, account_id) VALUES (?, ?, ?)");
        stmt2.setInt(1, e.getAmount());
        stmt2.setBoolean(2, e.getInOrPay());
        stmt2.setInt(3, e.getAccountId());
        stmt2.execute();
        
        stmt2.close();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
