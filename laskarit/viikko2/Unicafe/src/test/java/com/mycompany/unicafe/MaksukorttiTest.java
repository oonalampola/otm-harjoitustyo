package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti != null);
    }

    @Test
    public void kortinSaldoAlussaOikein() {

        assertTrue(kortti.saldo() == 10);
    }

    @Test
    public void rahanLataaminenKasvattaaSaldoaOikein() {

        kortti.lataaRahaa(10);
        assertTrue(kortti.saldo() == 20);

    }

    @Test
    public void rahanOttaminenToimii() {

        kortti.otaRahaa(2);
        kortti.otaRahaa(10);

        assertTrue(kortti.saldo() == 8);

    }

    @Test
    public void toStringToimii() {
        kortti.lataaRahaa(20);
        String tulostus = kortti.toString();

        assertEquals("saldo: 0.30", tulostus);
    }

}
