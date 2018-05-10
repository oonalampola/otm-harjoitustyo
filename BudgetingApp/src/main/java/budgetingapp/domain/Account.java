/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.domain;

import budgetingapp.dao.UserDao;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author oona
 */
/**
 * Luokka kuvaa käyttäjään liittyvää Account-oliota
 */
public class Account {

//    private int id; <- sama kuin userId
    private double balance;
    private int userId;
    private List<Event> events;

    /**
     * Account-olion luominen
     *
     * @param userId Käyttäjän id
     */
    public Account(int userId) {

        this.userId = userId;
        this.balance = 0;
        this.events = new ArrayList();

    }

    /**
     * Account-olion luominen tietyllä saldolla
     *
     * @param userId Käyttäjän id
     * @param balance Tilin saldo
     */
    public Account(int userId, double balance) {
        this.userId = userId;
        this.balance = balance;
        this.events = new ArrayList();
    }
//
//    public int getId() {
//        return this.id;
//    }

    /**
     *
     * Tilin saldon palauttaminen
     *
     *
     * @return Tilin saldo doublena
     */
    public double getBalance() {
        return this.balance;
    }

    /**
     * Tiliin liittyvän käyttäjän tunnuksen palauttaminen
     *
     * @return Tiliin liittyvän käyttäjän id
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * Tilin tapahtumien palauttaminen
     *
     * @return Lista tiliin liittyviä tapahtumia
     */
    public List getEvents() {
        return this.events;
    }

    /**
     * Tilin saldon asettaminen
     *
     * @param amount Tilin saldoksi asetettava summa
     */
    public void setBalance(double amount) {
        this.balance = amount;
    }

    /**
     * Tapahtumien lisääminen tilille
     *
     * @param eventslist Lista Event-olioita
     */
    public void addEvents(List<Event> eventslist) {
        for (int i = 0; i < eventslist.size(); i++) {
            this.events.add(eventslist.get(i));
        }
        for (int i = 0; i < events.size(); i++) {
            this.balance -= events.get(i).getAmount();
        }
    }

    /**
     * Yhden tapahtuman lisääminen tilille
     *
     * @param e Event-olio
     *
     */
    public void addOneEvent(Event e) {
        this.events.add(e);
        this.balance -= e.getAmount();
    }
}
