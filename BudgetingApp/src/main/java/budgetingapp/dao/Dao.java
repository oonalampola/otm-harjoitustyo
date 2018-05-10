/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.dao;

import java.sql.*;

import java.util.*;

/**
 * Rajapinta, jonka UserDao ja AccountDao toteuttavat
 *
 * @author oona
 */
public interface Dao<T, K> {

    void save(T object) throws SQLException;

    void update(T object) throws SQLException;

    void delete(K key) throws SQLException;

}
