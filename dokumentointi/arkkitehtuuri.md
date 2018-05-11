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

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/LUOKAT.png" width="450">

Sovelluksen toiminnallisuudesta vastaa yksi BudgetingService-olio. Se tarjoaa metodeja käyttäjien, tilien ja tapahtumien hallintaan. Metodeja ovat mm.

- boolean createUser(String name, String username)
- boolean signIn(String username)
- List getEvents(int id)
- void deleteEvents(int id)

_BudgetingService_ käyttää käyttäjät ja muut tiedot säilövää tietokantaa Dao-luokkien avulla. Dao-luokat tallentavat ja hakevat tietoa tietokannasta ja palauttavat sitä takaisin BudgetingServicelle.

Sovelluksen osien suhteita kuvaava luokka/pakkauskaavio:

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/pakkausluokkakaavo.png" width="500">

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
Kaikissa tauluissa hyödynnetään samaa tunnistetta eli id:tä, joka annetaan käyttäjälle automaattisesti luomisen yhteydessä.
Lisäksi käyttäjiä haetaan monessa tilanteessa pelkällä käyttäjänimellä, koska ne ovat uniikkeja.

Tapahtumien aika määritellään kuukausi- ja vuositasolla. Kategoriat erotetaan toisistaan pysyvillä tunnisteilla, jotka ovat:
- 0 Tulo
- 1 Asumiskustannukset
- 2 Ruokakustannukset
- 3 Hyödykekustannukset
- 4 Vapaa-ajan kustannukset

Tietokantataulusta haettaessa tulokset ovat seuraavanlaisia:
```
SELECT * FROM User WHERE username='testuser';
Test User|testuser|1
```
```
SELECT * FROM Account WHERE user_id=1;
500.0|1
```
```
SELECT * FROM Event WHERE account_id=1;
2000|5|2018|0|1
23.0|5|2018|2|1
34.0|5|2018|3|1
550.0|5|2018|1|1
```

<h2>Päätoiminnallisuudet</h2>

<h3>Uuden käyttäjän luominen</h3>

Nappia _Create new user_ paninamalla sovellus etenee seuraavasti:

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/creating%20new%20user.png">

Käyttöliittymä kutsuu _BudgetingServicen_ metodia _createUser()_, joka tarkistaa userDaon avulla, onko käyttäjää olemassa. Mikäli takaisin palautuu _null_-viite, kutsuu _BudgetingService_ _userDao:ta_ uudelleen tällä kertaa _save()_-metodia. Käyttäjän luomisen jälkeen _createUser()_ kutsuu vielä _userDao:n_ _findByUsername()_, jotta _User_-olio saisi oman id:n. Tällä id:llä luodaan _AccountDao:n_ avulla käyttäjätili ja palautetaan lopulta käyttöliittymälle _true_.

<h3>Käyttäjän kirjautuminen</h3>

Sisäänkirjautumisen aikana sovellus etenee seuraavasti:

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/sekvenssikaavioSignIn.png">

Käyttöliittymä kutsuu _BudgetingService:n_ metodia _signIn()_, jolloin _userDao:n_ avulla tarkistetaan, onko käyttäjää olemassa. Jos ei, palautetaan false. Muusssa tapauksessa _BudgetingService_ asettaa löydetyn _User_-olion oliomuuttujaansa _signedInUser_, sekä tähän liittyvän _AccountDao_-luokan avulla löydetyn tilin _account_-muuttujaansa ja palauttaa käyttöliittymälle true.

<h3>Uuden tapahtuman lisääminen</h3>

Uuden tapahtuman lisääminen etenee seuraavasti:

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/adding%20new%20event.png">

Käyttöliittymä luo _Event_-olion, jonka antaa parametriksi _BudgetingServicelle_ kutsuessaan sen metodia _addEvent()_. Metodi _addEvent()_ kutsuu _AccountDao:n_ samannimistä metodia, joka lisää tapahtuman tietokantaan ja päivittää tilin saldon.

<h3>Muut toiminnallisuudet</h3>

Toiminnallisuudet, kuten tapahtumien poistaminen ja saldon nollaaminen tapahtuvat samalla kaavalla, kuin yllä olevat toiminnallisuudet. Käyttöliittymä kutsuu _BudgetingServicen_ metodeita, jotka puolestaan ohjaavat kutsut _Dao_-luokille.

<h2>Ohjelman rakenteeseen jääneet heikkoudet</h2>

Käyttöliittymän toteuttavasta koodista tuli todella pitkä ja joiltakin osilta jopa sekava. Ohjelma pyörii tällä hetkellä, mutta jatkokehityksen kannalta paljon selkeämmäksi alusta asti luotu käyttöliittymän koodi helpottaisi sen muokkaamista. _GuiHelper_-luokka luotiin tätä tarkoitusta ajatellen, mutta toteutus jäi vajaaksi.

Dao-luokkien toteuttama rakapinta jäi lähes turhaksi, koska sen metodeita ei ollut alunperin suunniteltu palvelemaan luokkien tarpeita. Lisäksi AccountDaon olisi jakaa kahdeksi erilliseksi luokaksi, koska tällä hetkellä se hoitaa sekä Account- että Event-taulujen hallintaa. 
