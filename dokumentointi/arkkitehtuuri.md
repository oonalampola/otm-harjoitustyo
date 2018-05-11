<h1>Arkkitehtuurikuvaus</h1>

<h2>Rakenne</h2>

Ohjelman pakkausrakenne:

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/pakkauskaavio.png" width="200">
<h2>Käyttöliittymä</h2>

- budgetingapp.ui sisältää JavaFX:lla toteutetun käyttöliittymän
- budgetingapp.domain sisältää sovelluslogiikan
- budgetinapp.dao sisältää tallennuksesta vastaavan koodin
- budgetinapp-database sisältää tietokannan luomiseen liittyvän koodin

Käyttöliittymä sisältää tällä hetkellä neljä erilaista näkymää

- Sisäänkirjautuminen
- Uuden käyttäjän luominen
- Sisäänkirjautuneen käyttäjän näkymä
- Uuden tapahtuman luominen

Käyttöliittymä on rakennettu luokassa budgetingapp.ui.BudgetingUi. Eri näkymät on toteutettu omina Scene-olioinaan, joiden sisänen asettelu on toteutettu hyödyntämällä esimerkiksi GridPane, ScrollPane ja BorderPane -olioita. 

Käyttöliittymä sisältää metodeita, joiden avulla luodaan sisältö graafisille esityksille tapahtumista. Sovelluslogiikka on pyritty pitämään mahdollisimman paljon erillään käyttöliittymää rakentavasta koodista.

<h2>Sovelluslogiikka</h2>

Sovelluksen logiikkamallin muodostavat luokat User, Account ja Event

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/LUOKAT.png" width="350">

Sovelluksen toiminnallisuudesta vastaa yksi BudgetingService-olio. Se tarjoaa metodeja käyttäjien, tilien ja tapahtumien hallintaan. Metodeja ovat mm.

- boolean createUser(String name, String username)
- boolean signIn(String username)
- List getEvents(int id)
- void deleteEvents(int id)

_BudgetingService_ käyttää käyttäjät ja muut tiedot säilövää tietokantaa Dao-luokkien avulla. Dao-luokat tallentavat ja hakevat tietoa tietokannasta ja palauttavat sitä takaisin BudgetingServicelle.

Sovelluksen osien suhteita kuvaava luokka/pakkauskaavio:

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/pakkausluokkakaavo.png" width="400">

<h2>Tietojen pysyväistallennus</h2>

Pakkauksen budgetingapp.dao luokat UserDao ja AccountDao huolehtivat tietojen tallennuksesta tietokantaan. Tietojen tallennukseen käytetään SQL-tietokantaa. 

<h3>Tietokantataulut</h3>

```
CREATE TABLE User(
	  id integer PRIMARY KEY, 
  	name varchar(200), 
  	username varchar(200
 ```
 
 ```
CREATE TABLE Account(
	  user_id integer PRIMARY KEY, 
   	balance float);

```

```
CREATE TABLE Event(
  	amount float,
	  month integer,
  	year integer,
	  category integer, 
  	account_id integer, 
	  FOREIGN KEY (account_id) REFERENCES Account);
	```

<h2>Päätoiminnallisuudet</h2>

<h3>Käyttäjän kirjautuminen</h3>

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/sekvenssikaavioSignIn.png">
