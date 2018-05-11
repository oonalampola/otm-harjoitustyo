# Testausdokumentti


## Yksikkötestaus


### Sovelluslogiikka

Pakkauksen [bidgetingapp.domain](https://github.com/oonalampola/otm-harjoitustyo/tree/master/BudgetingApp/src/test/java/budgetinapp/domain) luokkia testaavat [UserTest](https://github.com/oonalampola/otm-harjoitustyo/tree/master/BudgetingApp/src/test/java/budgetinapp/domain/UserTest.java), [AccountTest](https://github.com/oonalampola/otm-harjoitustyo/tree/master/BudgetingApp/src/test/java/budgetinapp/domain/AccountTest.java),
[EventTest](https://github.com/oonalampola/otm-harjoitustyo/tree/master/BudgetingApp/src/test/java/budgetinapp/domain/EventTest.java) sekä [BudgetingServiceTest](https://github.com/oonalampola/otm-harjoitustyo/tree/master/BudgetingApp/src/test/java/budgetinapp/domain/BudgetingServiceTest.java). 
Niiden määrittelevät testitapaukset simuloivat niiden päätoiminnallisuuksia suoraviivaisesti. Luokat sisältävät melko paljon suoraviivaisia gettereitä ja settereitä, lukuunottamatta BudgetingService-luokkaa. 


### DAO-luokat

Dao-luokkien toiminnalisuutta on testattu luomalla testaustietokanta test.db. Tietokantaan tallennetaan ja sieltä haetaan tietoa DAO-luokkien metodien avulla ja testataan thakujen tuloksia.

### Database

Database-luokan kykyä luoda tietokanta testataan omassa testausluokassaan. Testiluokassa luodaan ja poistetaan tietokantaa test.db, sekä testataan Database-luokan tarjoamaa init()-metodia.

### Testikattavuus

Käyttöliittymäluokkaa lukuun ottamatta sovelluksen testien rivikattavuus on 93% ja haarautumakattavuus 71%.
<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/testikattavuus.PNG">


### Asennus ja konfigurointi

Sovellus on haettu ja sitä on testattu [käyttöohjeen](https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kayttoohje.md)  kuvaamalla tavalla Windows- ja Linux-ympäristöissä.

Sovellusta on testattu tilanteessa, jossa tietokantaa ei ole valmiiksi olemassa sekä tilanteessa, jossa se jo löytyy.

### Toiminnallisuudet

Sovelluksen toiminnallisuudet on käyty läpi. Sovellusta on testattu myös tilanteissa, joissa käyttäjä yrittää syöttää sille tyhjää tai virheellisiä arvoja.

## Sovellukseen jääneet laatuongelmat

Sovellus ei anna pyöriessään graafisen käyttöliittymän kautta virheviestiä kaikissa tilanteissa, mutta ei kuitenkan ole myöskään kaatunut, vaan käyttöä on pystynyt jatkamaan normaalisti.
