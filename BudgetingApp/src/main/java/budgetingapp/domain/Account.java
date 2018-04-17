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
public class Account {

//    private int id; <- sama kuin userId
    private int balance;
    private int userId;
    private List<Event> events;

    public Account(int userId) {

        this.userId = userId;
        this.balance = 0;
        this.events = new ArrayList();

    }

    public Account(int userId, int balance) {
        this.userId = userId;
        this.balance = balance;
        this.events = new ArrayList();
    }
//
//    public int getId() {
//        return this.id;
//    }

    public int getBalance() {
        return this.balance;
    }

    public int getUserId() {
        return this.userId;
    }

    public List getEvents() {
        return this.events;
    }

    public void setBalance(int amount) {
        this.balance = amount;
    }

    public void addEvents(List<Event> eventslist) {
        for (int i = 0; i < eventslist.size(); i++) {
            this.events.add(eventslist.get(i));
        }
        for (int i = 0; i < events.size(); i++) {
            this.balance -= events.get(i).getAmount();
        }
    }

    public void addOneEvent(Event e) {
        this.events.add(e);
        this.balance -= e.getAmount();
    }
}
