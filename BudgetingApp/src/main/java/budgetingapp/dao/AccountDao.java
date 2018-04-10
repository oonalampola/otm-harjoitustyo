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

    public Account findByUserId(int id) throws SQLException {

        Connection c = database.getConnection();
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Account WHERE id = ?");
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            System.out.println("palautetaan null");
            return null;
        }

        Account a = new Account(rs.getInt("id"), rs.getInt("balance"));
        a.addEvents(findAccountEvents(id));
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
        
        while(rs.next()){
            list.add(new Event(rs.getInt("id"), rs.getInt("amount"), rs.getBoolean("inOrPay"), rs.getInt("account_id")));
            
        }
        return list;
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
    public void save(Account object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
