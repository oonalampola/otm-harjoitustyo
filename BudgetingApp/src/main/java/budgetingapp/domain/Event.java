/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.domain;

/**
 *
 * @author iskä
 */
public class Event {

    private int id;
    private int amount;
    private boolean inOrPay;
    private int category;
    private int accountId;

    public Event(int id, int amount, boolean inOrPay, int accountId) {
        this.id = id;
        this.amount = amount;
        this.inOrPay = inOrPay;
        this.category = 0;
        this.accountId = accountId;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getId() {
        return this.id;

    }

    public int getAmount() {
        return this.amount;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public int getCategory() {
        return this.category;
    }

    public boolean getInOrPay() {
        return this.inOrPay;
    }

}
