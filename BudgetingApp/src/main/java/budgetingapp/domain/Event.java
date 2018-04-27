/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.domain;

import java.sql.Date;
import java.sql.Timestamp;

/**
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

    public Event(double amount, int accountId) {

        this.amount = amount;
        this.category = 0;
        this.accountId = accountId;
    }

    public Event(int id, double amount, int accountId) {
        this.id = id;
        this.amount = amount;
        this.category = 0;
        this.accountId = accountId;
        
    }
    public void setTime(int month, int year){
        this.month = month;
        this.year = year;
    }
    public void setCategory(int category) {
        this.category = category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;

    }

    public double getAmount() {
        return this.amount;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public int getCategory() {
        return this.category;
    }
    public int getMonth(){
        return this.month;
    }
    public int getYear(){
        return this.year;
    }

}
