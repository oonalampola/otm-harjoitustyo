/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.domain;

import budgetingapp.dao.AccountDao;
import budgetingapp.dao.UserDao;
import budgetingapp.database.Database;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Luokka toimii käyttöliittymän ja sovelluslogiikan välissä
 *
 * @author oona
 */
public class BudgetingService {

    private Database database;
    private UserDao userDao;
    private AccountDao accountDao;
    private Scanner scanner;
    public User signedIn;
    public Account account;

    /**
     * Luo BudgetingService-olion
     *
     * @param userDao UserDao-olio
     * @param accountDao AccountDao-olio
     * @throws ClassNotFoundException
     */
    public BudgetingService(UserDao userDao, AccountDao accountDao) throws ClassNotFoundException {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.signedIn = null;
        this.account = null;
    }

    /**
     * Uuden käyttäjän luominen
     *
     * @param name nimi
     * @param username käyttäjänimi
     * @return true, jos luominen onnistui, muutoin false
     * @throws SQLException
     */
    public boolean createUser(String name, String username) throws SQLException {
        User u = userDao.findByUsername(username);
        if (u == null) {

            User user = new User(name, username);
            userDao.save(user);
            user = userDao.findByUsername(username);
            accountDao.createNewAccount(user.getId());
            return true;

        }
        return false;

    }

    /**
     * Sisäänkirjautuminen
     *
     * @param username käyttäjänimi
     * @return true, jos onnistui, muutoin false
     * @throws SQLException
     */
    public boolean signIn(String username) throws SQLException {
        this.signedIn = userDao.findByUsername(username);
        if (this.signedIn == null) {
            return false;
        }

        int id = this.signedIn.getId();
        this.account = accountDao.findByUserId(id);
        signedIn.setAccount(this.account);

        return true;
    }

    /**
     * Sisäänkirjautuneen USer-olion tietojen päivittäminen tietokannasta
     *
     * @param username käyttäjänimi
     * @return User-olio
     * @throws SQLException
     */
    public User updateUserInfo(String username) throws SQLException {
        this.signedIn = userDao.findByUsername(username);
        this.account = accountDao.findByUserId(signedIn.getId());
        this.signedIn.getAccount().setBalance(this.account.getBalance());

        return this.signedIn;
    }

    /**
     * Uloskirjautuminen, sisäänkirjautunut User-olio nollataan
     */
    public void signOut() {
        this.signedIn = null;
    }

    /**
     * Palauttaa sisäänkirjautuneen käyttäjän User-oliona
     *
     * @return User-olio
     */
    public User getSignedIn() {
        return this.signedIn;
    }

    /**
     * Sisäänkirjautuneen käyttäjän tiliin liittyvien tapahtumien etsiminen
     *
     * @param id käyttäjään ja tiliin liittyvä id
     * @return Lista-olio, joka sisältää Event-olioita
     * @throws SQLException
     */
    public List getEvents(int id) throws SQLException {
        List<Event> events = accountDao.findAccountEvents(id);
        return events;
    }

    /**
     * Sisäänkirjautuneen käyttäjän tiliin liittyvien tapahtumien etsiminen
     * tietyltä ajanjaksolta
     *
     * @param id käyttäjään ja tiliin liittyvä id
     * @param month kuukausi
     * @param year vuosi
     * @return Lista-olio, joka sisältää Event-olioita halutuilla aikamääreillä
     * @throws SQLException
     */
    public List getMonthlyEvents(int id, int month, int year) throws SQLException {
        List<Event> list = accountDao.getMonthlyEvents(id, month, year);
        return list;

    }

    /**
     * Tapahtuman lisääminen
     *
     * @param e Event-olio, joka halutaan lisätä
     * @return true, jos onnistui, muutoin false
     * @throws SQLException
     */
    public boolean addEvent(Event e) throws SQLException {
        this.account.addOneEvent(e);
        return accountDao.addEvent(e);

    }

    /**
     * Kategorioihin liittyvien summien etsiminen
     *
     * @param a Account-olio
     * @return Lista-olio, joka sisältää double-muotoisia summia
     * @throws SQLException
     */
    public List<Double> getCategoryAmounts(Account a) throws SQLException {
        List<Double> list = accountDao.countCategories(a);
        return list;
    }

    /**
     * Kategorioihin liittyvien summien etsiminen kuukausitasolla
     *
     * @param id käyttäjään ja tiliin liittyvä id
     * @param month kuukausi
     * @param year vuosi
     * @return Lista-olio, joka sisältää double-muotoisia summia
     * @throws SQLException
     */
    public List<Double> getMonthlyCategoryAmounts(int id, int month, int year) throws SQLException {
        List<Double> list = accountDao.countMonthlyCategories(id, month, year);
        return list;

    }

    /**
     * Tapahtumien poistaminen
     *
     * @param id käyttäjään ja tiliin liittyvä id
     * @throws SQLException
     */
    public void deleteEvents(int id) throws SQLException {
        accountDao.deleteEvents(id);
    }

    /**
     * Saldon nollaaminen
     *
     * @throws SQLException
     */
    public void clearBalance() throws SQLException {
        accountDao.clearBalance(account);
        account.setBalance(0);
    }

}
