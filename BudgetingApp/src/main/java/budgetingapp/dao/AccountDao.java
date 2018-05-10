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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * Luokka etsii ja palauttaa tileihin ja tapahtumiin liittyviä tietoja
 * tietokannasta
 *
 * @author oona
 */
public class AccountDao implements Dao<Account, Integer> {

    Database database;
    Date date;

    /**
     * Luo AccountDao-olion
     *
     * @param database Tietokanta, jota luokka tarkastelee
     */
    public AccountDao(Database database) {
        this.database = database;
    }

    /**
     * Uuden käyttäjätilin luominen
     *
     * @param userId Tiliin liittyvän käyttäjän id
     */
    public void createNewAccount(int userId) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("INSERT INTO Account (user_id, balance) VALUES (?, ?)");
        stmt2.setInt(1, userId);
        stmt2.setInt(2, 0);
        stmt2.execute();
        stmt2.close();
        c.close();
    }

    /**
     * Käyttäjätilin etsiminen id:n avulla
     *
     * @param id Käyttäjän ja tilin yhteen liittävä id (käyttäjän id)
     * @return Account-olio, joka liittyy haettuun id:n
     */
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

    /**
     * Käyttäjätiliin liittyvien tapahtumien hakeminen
     *
     * @param id Käyttäjän ja tilin yhteen liittävä id
     * @return Lista Event-olioita, jotka liittyvät haettuun id:n
     */
    public List findAccountEvents(int id) throws SQLException {

        List<Event> list = new ArrayList<>();
        Connection c = database.getConnection();
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Event WHERE account_id = ? ORDER BY year, month");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Event e = new Event(rs.getDouble("amount"), rs.getInt("account_id"));
            e.setCategory(rs.getInt("category"));
            e.setTime(rs.getInt("month"), rs.getInt("year"));
            list.add(e);
            System.out.println(e.getAmount());
        }
        stmt.close();
        c.close();
        rs.close();
        return list;
    }

    /**
     * Tapahtuman lisääminen tilille
     *
     * @param e Event-olio, tapahtuma, joka halutaan liittää tiliin
     * @return true, jos lisääminen onnistui ja false, jos lisääminen
     * epäonnistui
     */
    public boolean addEvent(Event e) throws SQLException {

        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("INSERT INTO Event (amount, month, year, category, account_id) VALUES (?, ?, ?, ?, ?)");
        stmt2.setDouble(1, e.getAmount());
        stmt2.setInt(2, e.getMonth());
        stmt2.setInt(3, e.getYear());
        stmt2.setInt(4, e.getCategory());
        stmt2.setInt(5, e.getAccountId());

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

    /**
     * Uuden tilin lisääminen tietokantaan
     *
     * @param a Account-olio, joka halutaan lisättävän
     */
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

    /**
     * Tilin poistaminen tietokannasta
     *
     * @param key id, joka halutaan poistaa
     */
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

    /**
     * Tilin saldon päivittäminen
     *
     * @param a Account-olio, tili, jonka saldo halutaan muuttaa
     * @param amount Summa, jonka verran saldoa halutaan muuttaa
     *
     * @return Account-olio, jonka saldo on päivitetty
     */
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

    /**
     * Kategorioihin liittyvien tapahtumien summien yhteenlaskeminen
     *
     * @param a Account-olio, tili, jonka summat halutaan laskea kategorioittain
     * @return Lista, joka sisältää kategorioiden summat lisäämisjärjestyksessä
     */
    public List<Double> countCategories(Account a) throws SQLException {

        List<Double> amounts = new ArrayList<>();
        amounts.add(countLiving(a));
        amounts.add(countFood(a));
        amounts.add(countGoods(a));
        amounts.add(countSpareTime(a));

        return amounts;

    }

    /**
     * Kategorian 1 eli asumiskustannusten laskeminen
     *
     * @param a Account-olio, tili, jonka asumiskustannukset halutaan laskea
     * @return Asumiskustannuksien yhteenlaskettu summa
     */
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

    /**
     * Kategorian 2 eli ruokakustannuksien laskeminen
     *
     * @param a Account-olio, tili, jonka ruokakustannukset halutaan laskea
     * @return Ruokakustannuksien yhteenlaskettu summa
     */
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

    /**
     * Kategorian 3 eli hyödykekustannusten laskeminen
     *
     * @param a Account-olio, tili, jonka hyödykekustannukset halutaan laskea
     * @return Hyödykekustannuksien yhteenlaskettu summa
     */
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

    /**
     * Kategorian 4 eli vapaa-ajan kustannusten laskeminen
     *
     * @param a Account-olio, tili, jonka vapaa-ajan kustannukset halutaan
     * laskea
     * @return Vapaa-ajan kustannuksien yhteenlaskettu summa
     */
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

    /**
     * Tapahtumien poistaminen yhdeltä tililtä
     *
     * @param id Tiliin ja käyttäjään liittyvä id
     */
    public void deleteEvents(int id) throws SQLException {

        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("DELETE FROM Event WHERE account_id=?");

        stmt2.setInt(1, id);
        stmt2.executeUpdate();

        stmt2.close();

        c.close();

    }

    /**
     * Saldon nollaaminen
     *
     * @param a Account-olio, tili, jonka saldo halutaan nollata
     */
    public void clearBalance(Account a) throws SQLException {

        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("UPDATE Account SET balance = ? WHERE user_id= ?");

        stmt2.setDouble(1, 0);
        stmt2.setInt(2, a.getUserId());
        stmt2.executeUpdate();

        stmt2.close();

        c.close();

    }

    /**
     * Tapahtumien etsiminen kuukausitasolla
     *
     * @param id Tiliin ja käyttäjään liittyvä id
     * @param month kuukausi
     * @param year vuosi
     *
     * @return Lista, joka sisältää Event-olioita
     */
    public List getMonthlyEvents(int id, int month, int year) throws SQLException {

        List<Event> list = new ArrayList<>();
        Connection c = database.getConnection();
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Event WHERE account_id = ? AND month = ? AND year = ?");
        stmt.setInt(1, id);
        stmt.setInt(2, month);
        stmt.setInt(3, year);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Event e = new Event(rs.getDouble("amount"), rs.getInt("account_id"));
            e.setCategory(rs.getInt("category"));
            e.setTime(rs.getInt("month"), rs.getInt("year"));
            list.add(e);
        }
        stmt.close();
        c.close();
        rs.close();
        return list;

    }

    /**
     * Kategorioihin liittyvien tapahtumien summien yhteenlaskeminen
     * kuukausitasolla
     *
     * @param id Tiliin ja käyttäjään liittyvä id
     * @param month kuukausi
     * @param year vuosi
     * @return Lista, joka sisältää kategorioiden summat lisäämisjärjestyksessä
     * halutulla kuukausitasolla
     */
    public List<Double> countMonthlyCategories(int id, int month, int year) throws SQLException {
        List<Double> amounts = new ArrayList<>();
        amounts.clear();
        amounts.add(countMonthlyLiving(id, month, year));
        amounts.add(countMonthlyFood(id, month, year));
        amounts.add(countMonthlyGoods(id, month, year));
        amounts.add(countMonthlySpareTime(id, month, year));

        return amounts;
    }

    /**
     * Kategorian 1 eli asumiskustannusten laskeminen kukausitasolla
     *
     * @param id Tiliin ja käyttäjään liittyvä id
     * @param month kuukausi
     * @param year vuosi
     *
     * @return Asumiskustannuksien yhteenlaskettu summa annetuilla aikamääreillä
     */
    public double countMonthlyLiving(int id, int month, int year) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("SELECT SUM(amount) FROM Event WHERE category = 1 AND account_id = ? AND month = ? AND year = ?");

        stmt2.setInt(1, id);
        stmt2.setInt(2, month);
        stmt2.setInt(3, year);

        ResultSet rs = stmt2.executeQuery();

        if (!rs.next()) {
            return 0;
        }
        double amount = rs.getDouble(1);

        stmt2.close();
        rs.close();
        c.close();
        System.out.println("kuukauden eläminen " + amount);
        return amount;
    }

    /**
     * Kategorian 2 eli ruokakustannusten laskeminen kukausitasolla
     *
     * @param id Tiliin ja käyttäjään liittyvä id
     * @param month kuukausi
     * @param year vuosi
     * @return Ruokakustannuksien yhteenlaskettu summa annetuilla aikamääreillä
     */
    public double countMonthlyFood(int id, int month, int year) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("SELECT SUM(amount) FROM Event WHERE category = 2 AND account_id = ? AND month = ? AND year = ?");

        stmt2.setInt(1, id);
        stmt2.setInt(2, month);
        stmt2.setInt(3, year);

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

    /**
     * Kategorian 3 eli hyödykekustannusten laskeminen kukausitasolla
     *
     * @param id Tiliin ja käyttäjään liittyvä id
     * @param month kuukausi
     * @param year vuosi
     * @return Hyödykekustannuksien yhteenlaskettu summa annetuilla
     * aikamääreillä
     */
    public double countMonthlyGoods(int id, int month, int year) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("SELECT SUM(amount) FROM Event WHERE category = 3 AND account_id = ? AND month = ? AND year = ?");

        stmt2.setInt(1, id);
        stmt2.setInt(2, month);
        stmt2.setInt(3, year);

        ResultSet rs = stmt2.executeQuery();

        if (!rs.next()) {
            return 0;
        }
        double amount = rs.getDouble(1);
        System.out.println("Goods Monthly: " + amount);
        stmt2.close();
        rs.close();
        c.close();

        return amount;
    }

    /**
     * Kategorian 4 eli Vapaa-ajan kustannusten laskeminen kukausitasolla
     *
     * @param id Tiliin ja käyttäjään liittyvä id
     * @param month kuukausi
     * @param year vuosi
     * @return Vapaa-ajan kustannuksien yhteenlaskettu summa annetuilla
     * aikamääreillä
     */
    public double countMonthlySpareTime(int id, int month, int year) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt2 = c.prepareStatement("SELECT SUM(amount) FROM Event WHERE category = 4 AND account_id = ? AND month = ? AND year = ?");

        stmt2.setInt(1, id);
        stmt2.setInt(2, month);
        stmt2.setInt(3, year);

        ResultSet rs = stmt2.executeQuery();

        if (!rs.next()) {
            return 0;
        }
        double amount = rs.getDouble(1);
        System.out.println("Spare time monthly " + amount);
        stmt2.close();
        rs.close();
        c.close();

        return amount;

    }
}
