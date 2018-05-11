/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetinapp.domain;

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
public class EventTest {

    Event event;

    public EventTest() {
    }

    @Before
    public void setUp() {
        event = new Event(300, 0);
    }

    @Test
    public void constructorCreatesEvent() {
        assertTrue(null != event);
    }

    @Test
    public void settingTimeSetsTime() {

        event.setTime(5, 2018);

        assertEquals(5, event.getMonth());
        assertEquals(2018, event.getYear());
    }

    @Test
    public void categoryIsSetted() {

        event.setCategory(2);
        assertTrue(2 == event.getCategory());
    }

    @Test
    public void amountIsCorrect() {
        assertTrue(300 == event.getAmount());
    }

    @After
    public void tearDown() {
    }

}
