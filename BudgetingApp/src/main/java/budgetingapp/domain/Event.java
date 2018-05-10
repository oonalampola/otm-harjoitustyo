/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.domain;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Luokka kuvaa tapahtumista luotavia Event-olioita
 *
 * @author oona
 */
public class Event {

    private int id;
    private double amount;
    private int category;
    private int accountId;
    private int month;
    private int year;

    /**
     * Luo Event-olion
     *
     * @param amount summa
     * @param accountId tiliin liittyvä id
     */
    public Event(double amount, int accountId) {

        this.amount = amount;
        this.category = 0;
        this.accountId = accountId;
    }

    /**
     * Luo Event-olion (varmaankin turha)
     *
     * @param id
     * @param amount
     * @param accountId
     */
    public Event(int id, double amount, int accountId) {
        this.id = id;
        this.amount = amount;
        this.category = 0;
        this.accountId = accountId;

    }

    /**
     * Ajan asettaminen
     *
     * @param month kuukausi
     * @param year vuosi
     */
    public void setTime(int month, int year) {
        this.month = month;
        this.year = year;
    }

    /**
     * Kategorian asettaminen
     *
     * @param category kategorian tnnus
     */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * Id:n asettaminen
     *
     * @param id id
     */
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;

    }

    /**
     * Summan palauttaminen
     *
     * @return tapahtumaan liittyvä summa doublena
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * Tiliin liittyvän id:n palauttaminen
     *
     * @return id
     */
    public int getAccountId() {
        return this.accountId;
    }

    /**
     * Ktegorian tunnuksen palauttaminen
     *
     * @return category int-muotoisena
     */
    public int getCategory() {
        return this.category;
    }

    /**
     * Kuukauden palauttaminen
     *
     * @return month int-muotoisena
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Vuoden palauttaminen
     *
     * @return year int-muotoisena
     */
    public int getYear() {
        return this.year;
    }

}
