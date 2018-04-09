/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.dao;

import budgetingapp.database.Database;
import budgetingapp.domain.User;
import java.sql.*;

import java.util.*;

/**
 *
 * @author oona
 */
public class UserDao implements Dao<User, Integer> {

    private Database database;

    public UserDao(Database database) {
        this.database = database;
    }

//EI TOTEUTETTU
    @Override
    public User findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public User findByUsername(String username) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt = c.prepareStatement("SELECT name AS name, username AS username FROM User WHERE username = ?");
        stmt.setString(1, username);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if(!hasOne){
            return null;
        }
        
        User user = new User(rs.getString("name"), rs.getString("username"));
        stmt.close();
        c.close();
        rs.close();
        
        return user;
    }


//EI TOTEUTETTU

    @Override
    public List<User> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save(User user) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt = c.prepareStatement("INSERT INTO User (name, username) VALUES(?, ?");
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getUsername());

        stmt.executeUpdate();
        c.close();
    }
//EI TOTEUTETTU

    @Override
    public void update(User object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
//EI TOTEUTETTU

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
