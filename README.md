
<h1>BudgetingApp</h1>


<h2>Dokumentaatio</h2>

[Vaatimusmäärittely](https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/vaatimusmaarittely.md)

[Työaikakirjanpito](https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/tyoaikakirjanpito.md)

[Arkkitehtuurikuvaus](https://github.com/oonalampola/otm-harjoitustyo/blob/master/dokumentointi/arkkitehtuuri.md)

<h2>Releaset</h2>

[Viikko 5](https://github.com/oonalampola/otm-harjoitustyo/releases)
[Loppupalautus](]https://github.com/oonalampola/otm-harjoitustyo/releases/tag/loppupalautus)
https://github.com/oonalampola/otm-harjoitustyo/releases
<h2>Komentorivitoiminnot</h2>

<h3>Testaus</h3>

Testit suoritetaan komennolla

```
mvn test
```

Testikattavuusraportti luodaan komennolla

```
mvn jacoco:report
```

Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto _target/site/jacoco/index.html_

<h3>Checkstyle</h3>

Tiedostoon [checkstyle.xml](https://github.com/mluukkai/OtmTodoApp/blob/master/checkstyle.xml) määrittelemät tarkistukset suoritetaan komennolla

```
 mvn jxr:jxr checkstyle:checkstyle
```

Mahdolliset virheilmoitukset selviävät avaamalla selaimella tiedosto _target/site/checkstyle.html_

### Suoritettavan jarin generointi

Komento

```
mvn package
```

generoi hakemistoon _target_ suoritettavan jar-tiedoston _BudgetingApp-1.0-SNAPSHOT.jar_

### JavaDoc

JavaDoc generoidaan komennolla

```
mvn javadoc:javadoc
```

JavaDocia voi tarkastella avaamalla selaimella tiedosto _target/site/apidocs/index.html_
