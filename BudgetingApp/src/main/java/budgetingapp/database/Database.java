/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author oona
 */
public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> tables = tables();

        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            for (String table : tables) {
                st.executeUpdate(table);
            }

        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }

    }

    private List<String> tables() {
        ArrayList<String> list = new ArrayList<>();

        list.add("CREATE TABLE IF NOT EXISTS User(id integer PRIMARY KEY, name varchar(200), username varchar(200));");
        list.add("CREATE TABLE IF NOT EXISTS Account(user_id integer PRIMARY KEY, balance floa");
        list.add("CREATE TABLE IF NOT EXISTS Event(amount float, month integer, year integer, category integer, account_id integer, FOREIGN KEY (account_id) REFERENCES Account);");

        return list;

    }
}
