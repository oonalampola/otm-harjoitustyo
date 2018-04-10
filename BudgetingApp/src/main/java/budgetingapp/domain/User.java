/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.domain;

/**
 *
 * @author oona
 */
public class User {
    private int id;
    private String name;
    private String username;
    private Account account;
    
    public User(String name, String username) {
        this.name=name;
        this.username=username;
    }

    public User(int id, String name, String username) {
        this.name = name;
        this.username = username;
        this.account = new Account(id);

    }
    

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }
    public int getAccountBalance(){
        return this.account.getBalance();
    }

 
}