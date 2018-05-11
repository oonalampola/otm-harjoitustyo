# Käyttöohje

Lataa tiedosto [budgetingapp.jar](url)

## Konfigurointi



## Ohjelman käynnistäminen

Ohjelma käynnistetään komennolla 

```
java -jar budgetingapp.jar
```

## Kirjautuminen

Sovellus käynnistyy kirjautumisnäkymään:

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/Kirjautumisnakyma.PNG" width="400">

Sovellukseen kirjaudutaan kirjoittamalla syötekenttään käyttäjätunnus ja painamalla _Sign in_. Käyttäjätunnuksen tulee olla aiemmin luotu.

## Uuden käyttäjän luominen

Aloitusnäkymästä pääsee luomaan uuden käyttäjän klikkaamalla _Create new user_.

Kirjoittamalla kenttiin tarvittavat tiedot, voi uuden käyttäjän luoda painamalla _Create new user_.

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/Uusikayttisnakyma.PNG" width="350">

Jos luominen onnistuu, palaa ohjelma kirjautumisnäkymään ja luodulla käyttäjällä voi kirjautua sisään.

## Tapahtumien tarkastelu ja luominen

Kirjautumisen jälkeen siirrytään näkymään, jossa vasemmalle muodostuu ympyrädiagrammi menojen jakautumisesta 
ja oikealle lista kaikista lisätyistä tapahtumista.
Mikäli käyttäjä on uusi eikä tapahtumia ole vielä lisätty, on näkymä seuraavanlainen:

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/kirjautunutUusi.PNG" width="600">

Uuden tapahtuman pääsee lisäämään yläpalkin painikkeella _Add new event_. Näkymä vaihtuu tapahtumanluomisnäkymäksi. 
Kaikissa tekstikentissä tulee olla oikeanlaista tietoa, jotta tapahtuman lisääminen onnistuu.

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/Uusitapahtuma.PNG" width="300">

Kun tapahtumia lisätään, muodostuu niistä ympyrädiagrammi sekä lista.

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/mikkomallikasTapahtumia.PNG" width="600">

Valitsemalla alapalkista ajanjakson, saa näkyville tietyn kuukauden tapahtumat.

<img src="https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/kuvat/mikkoApril.PNG" width="600">

## Muita toimintoja

- Yläpalkin painikkeet
  - _Delete all events_ poistaa käyttäjältä kaikki tapahtumat
  - _Clear balance_ nollaa saldon
  - _Sign out_ kirjaa käyttäjän ulos ja palaa sisäänkirjautumisnäkymään
  
 -Alapalkin painikkeet
  - _Show_ näyttää valitun ajankohdan tapahtumat
  - _Show all_ näyttää kaikki käyttäjätilin tapahtumat

