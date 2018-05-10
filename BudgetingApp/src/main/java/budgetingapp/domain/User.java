/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.domain;

/**
 * Luokka kuvaa käyttäjistä luotavia käyttäjiä User-olioina
 *
 * @author oona
 */
public class User {

    private int id;
    private String name;
    private String username;
    private Account account;

    /**
     * Luo User-olion nimen ja käyttäjänimen perusteella
     *
     * @param name nimi
     * @param username käyttäjänimi
     */
    public User(String name, String username) {
        this.name = name;
        this.username = username;
        this.account = null;
    }

    /**
     * User-olion luominen id:n, nimen ja käyttäjänimen perusteella
     *
     * @param id käyttäjään liittyvä id
     * @param name nimi
     * @param username käyttäjänimi
     */
    public User(int id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.account = null;

    }

    /**
     * Tyhjän User-olion luominen
     */
    public User() {
        this.name = null;
        this.id = 0;
        this.username = null;
        this.account = null;
    }

    /**
     * Nimen asettaminen
     *
     * @param name nimi
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Käyttäjänimen asettaminen
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Tilin liittäminen
     *
     * @param a Account-olio
     */
    public void setAccount(Account a) {
        this.account = a;
    }

    /**
     * Id:n asettaminen
     *
     * @param id käyttäjään liittyvä id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Id:n palauttaminen
     *
     * @return käyttäjään liittyvä id int-muotoisena
     */
    public int getId() {
        return this.id;
    }

    /**
     * Nimen palauttaminen
     *
     * @return nimi String-oliona
     */
    public String getName() {
        return this.name;
    }

    /**
     * Käyttäjänimen palauttaminen
     *
     * @return käyttäjänimi String-olioma
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Tilin saldon palauttaminen
     *
     * @return saldo doublena
     */
    public double getAccountBalance() {
        return this.account.getBalance();
    }

    /**
     * Käyttäjään liittyvän tilin palauttaminen
     *
     * @return Account-olio
     */
    public Account getAccount() {
        return this.account;
    }

}
