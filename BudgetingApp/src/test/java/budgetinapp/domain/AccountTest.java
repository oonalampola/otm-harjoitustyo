/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetinapp.domain;

import budgetingapp.domain.Account;
import budgetingapp.domain.Event;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author iskä
 */
public class AccountTest {

    Account testAccount;
    Event testEvent;
    public AccountTest() {
               
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
         this.testAccount = new Account(1);
         this.testEvent = new Event(500, 1);
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
     @Test
    public void createdAccountExists(){
        assertTrue(testAccount != null);
    }
    @Test
    public void constructorSetsName() {
       assertEquals(1, testAccount.getUserId());
    }

   
    @Test
    public void settingBalanceWorks(){
        testAccount.setBalance(500);
        assertTrue(500 == testAccount.getBalance());
    }
    @Test
    public void addingEventsAddsEvents(){
       
        testAccount.addOneEvent(testEvent);
        Event e = (Event) testAccount.getEvents().get(0);
        assertTrue(testEvent.getAmount() ==  e.getAmount());
    }
}
