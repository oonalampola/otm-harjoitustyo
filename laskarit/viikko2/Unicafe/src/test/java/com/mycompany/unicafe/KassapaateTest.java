/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lampoona
 */
public class KassapaateTest {

    Kassapaate kassa;
    Maksukortti kortti;

    public KassapaateTest() {
    }

    @Before
    public void setUp() {
        kassa = new Kassapaate();
        kortti = new Maksukortti(1000);
    }

    @Test
    public void rahamaaraJaMyytyjenLounaidenMaaraOikein() {
        assertTrue(kassa.kassassaRahaa() == 100000);
        assertTrue(kassa.edullisiaLounaitaMyyty() == 0 && kassa.maukkaitaLounaitaMyyty() == 0);

    }

    @Test
    public void syoEdullisestiKateisostoToimii() {

        //edulliset lounaat
        assertTrue(kassa.syoEdullisesti(300) == 60);
        assertTrue(kassa.kassassaRahaa() == 100240);
        assertTrue(kassa.maukkaitaLounaitaMyyty() + kassa.edullisiaLounaitaMyyty() == 1);
        assertTrue(kassa.syoEdullisesti(200) == 200);

    }

    @Test
    public void syoMaukkaastiKateisostoToimii() {

        //vaihtoraha
        assertTrue(kassa.syoMaukkaasti(500) == 100);
        //ei saa ostettua, jos rahat ei riitä ja raha palautetaan
        assertTrue(kassa.syoMaukkaasti(120) == 120);
        //kassan raha kasvaa
        assertTrue(kassa.kassassaRahaa() == 100400);
        assertTrue(kassa.maukkaitaLounaitaMyyty() + kassa.edullisiaLounaitaMyyty() == 1);
    }

    @Test
    public void korttimaksuToimiiKunSillaOnRahaa() {
        //edulliset
        assertTrue(kassa.syoEdullisesti(kortti));
        assertTrue(kortti.saldo() == 760);
        assertTrue(kassa.maukkaitaLounaitaMyyty() + kassa.edullisiaLounaitaMyyty() == 1);
        assertTrue(kassa.kassassaRahaa() == 100000);

    }

    @Test
    public void korttimaksuEiMeneLapiJosEiRahaa() {

        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        assertEquals(false, kassa.syoEdullisesti(kortti));
        assertEquals(false, kassa.syoMaukkaasti(kortti));
        assertTrue(kortti.saldo() == 200);
        assertTrue(kassa.maukkaitaLounaitaMyyty() + kassa.edullisiaLounaitaMyyty() == 2);

    }

    @Test
    public void rahanLatausToimii() {
        int ladattavaSumma = 1000;
        assertTrue(ladattavaSumma >= 0);
        kassa.lataaRahaaKortille(kortti, ladattavaSumma);

        assertTrue(kortti.saldo() == 2000);
        assertTrue(kassa.kassassaRahaa() == 101000);
        kassa.lataaRahaaKortille(kortti, -100);
        assertTrue(kortti.saldo() == 2000);
        assertTrue(kassa.kassassaRahaa() == 101000);

    }
}
