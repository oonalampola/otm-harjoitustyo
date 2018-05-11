<h1>Arkkitehtuurikuvaus</h1>

<h2>Rakenne</h2>

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

<h2>Päätoiminnallisuudet</h2>

<h3>Käyttäjän kirjautuminen</h3>

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/sekvenssikaavioSignIn.png">
