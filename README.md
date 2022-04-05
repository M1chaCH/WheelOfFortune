# WheelOfFortune
Dies ist ein Schulprojekt. Es geht darum eine Webseite zu implementieren. Die Technologien können frei gewählt werden. Ich habe mich für PostgreSQL, Spring & AngularJS entschieden. 

# LB Aufgaben
## Aufgabe 2
### Tier 1 Presentation
Mein Presentations Tier ist sehr dynamisch. Je nach Spielstand wird komplett etwas anderes angezeigt. Das HTML wird dynamisch generiert.
### Tier 2 Webserver
Mein Webserver Tier ist auch einigermaßen dynamisch. Mann kann selber wählen welchen Webserver man verwenden möchte um das Frontend zu hosten. Der Webserver muss einfach NodeJS und Angular Routes unterstützen. 
### Tier 3 Application Server
Mein Application Server ist überhaupt nicht dynamisch. Das Framework welches ich verwende hat einen selbst eingebauten Application Server. Diesen kann man nicht so einfach austauschen. Und auch die allgemeine API ist relativ hardcoded und muss von hand erweitert oder verändert werden.
### Tier 4 Data-Server 
Mein Data-Server kann auch relativ dynamisch ausgetauscht werden. Man muss einfach die neue Connection Daten im `backend/src/main/resources/application.properties` eintragen.

## Aufgabe 3
### Tier 1 Presentation
Für die Presentation habe ich das Framework Angular gebraucht. Für die Webseite an sich habe ich normales HTML 5 verwendet. Als styling Sprache habe ich SCSS verwendet. Und als script Sprache habe ich TypeScript verwendet.  
### Tier 2 Webserver
NodeJS dient als mein webserver. Diesen habe ich aber fast nicht angefasst, weil ich nichts daran konfigurieren musste. 
### Tier 3 Application Server
Mein Application Server ist eine Java Spring Applikation. Spring selber verwendet einen grossen Teil von Tomcat, hat aber selber darauf noch recht viel aufgebaut.
### Tier 4 Data-Server 
Als Data Server habe ich jetzt PostgreSQL verwendet. Da aber Spring Data JPA recht flexibel ist, kann dieser auch recht schnell durch einen anderen SQL Data Server ersetzt werden.

## Aufgabe 4
Ich steure meine Daten via Spring Data JPA. Das ist ein ORM von Spring basierend auf dem Java JPA interface. 

Spring Data JPA funktioniert mit herkömmlichen Java Entities welche mit JPA Annotations verziert wurden. Die Verknüpfung zur Datenbank funktioniert aber über ein Java Interface. Dieses wird als Repository bezeichnet. Die Implementation für das Repository wird von Spring bei Build-Time generiert. 

## Aufgabe 5
Hier ist mein Wireframe: (Erstellt am Dienstag 15 März 2022) 
![Wireframe Visualisation](Wireframe.png "Wireframe Visualisation")

## Aufgabe 8
### Anforderungsanalyse

**Anforderungsnummer**: 1 <br>
**Typ / Kategorie**: Funktionale Anforderung <br>
**Beschreibung**: Administratoren müssen sich durch Username und Passwort authentifizieren.

**Anforderungsnummer**: 2 <br>
**Typ / Kategorie**: Funktionale Anforderung <br>
**Beschreibung**:  Administratoren können einzelne Einträge der Highscoreliste löschen.

**Anforderungsnummer**: 3 <br>
**Typ / Kategorie**: Funktionale Anforderung <br>
**Beschreibung**:  Der Kandidat oder die Kandidatin müssen einen Namen eingeben können, der auf der Highscoreliste erscheint.

**Anforderungsnummer**: 4 <br>
**Typ / Kategorie**: Funktionale Anforderung <br>
**Beschreibung**:  Die Anzahl der Spielrunden soll gezählt werden.

**Anforderungsnummer**: 5 <br>
**Typ / Kategorie**: Nicht-Funktionale Anforderung <br>
**Beschreibung**:  Einfache Formulareingaben, wie leere Textfelder etc., sollen auf Client- und Serverseite geprüft werden.

### Testfallspezifikation

**Anforderungsnummer**: 1 <br>
**Testfallnummer**: 1.1 <br>
**Voraussetzungen**: Die Webseite ist auf der /home route geöffnet. <br>
**1. Eingabe**: Oben rechts auf den Login Knopf drücken.<br>
**1. Ausgabe**: Login-Seite erscheint<br>
**2. Eingabe**: Username und Passwort eingeben ("Admin", "Adm1nUser")<br>
**2. Ausgabe**: Admin-Seite erscheint<br>

**Anforderungsnummer**: 2 <br>
**Testfallnummer**: 2.1 <br>
**Voraussetzungen**: Der Admin ist eingeloggt, ist auf der Admin Route & es gibt ein Paar HighScore Einträge <br>
**1. Eingabe**: Zum zu löschenden HighScore Eintrag hin scrollen<br>
**2. Eingabe**: Auf der rechten Seite des HighScore Eintrags den Abfalleimer drücken<br>
**1. Ausgabe**: Eine Nachricht erscheint zuunterst auf dem Bildschirm<br>
**2. Ausgabe**: Die HighScore List lädt neu & und der Eintrag ist weg<br>

**Anforderungsnummer**: 3 <br>
**Testfallnummer**: 3.1 <br>
**Voraussetzungen**: Route home ist offen, kein Spiel wurde gestartet <br>
**1. Eingabe**: Benutzername und gewünschte Kategorie angeben.<br>
**2. Eingabe**: Das Spiel durchspielen (Spielen und einmal Beenden).<br>
**3. Eingabe**: Die Seite neu laden<br>
**1. Ausgabe**: Der eingegebene Benutzername erscheint auf der HighScore list. (Ausser das Spiel war das schlechteste und es sind schon 9999 Einträge in der List)<br>

**Anforderungsnummer**: 4 <br>
**Testfallnummer**: 4.1 <br>
**Voraussetzungen**: Die Webseite ist offen und ein Spiel wurde gerade beendet. <br>
**1. Ausgabe**: In der Statistiken Liste wird die Anzahl gespielte Runden angezeigt. <br>

**Anforderungsnummer**: 5 <br>
**Testfallnummer**: 5.1 <br>
**Voraussetzungen**: Der Webserver und der App-Server sind gestartet & die Admin-Route ist offen. <br>
**1. Eingabe**: Ein neuer Satz erstellen, mit dem Satz: "Dies ist ein ;;; Invalider Satz" <br>
**1. Ausgabe**: Satz Feld wird rot und speichern Knopf kann nicht gedrückt werden. <br>
**2. Eingabe**: Ein POST request an den API Endpunkt "/game/data/sentence" mit dem Satz: "Dies ist ein ;;; Invalider Satz" senden <br>
**2. Ausgabe**: "{ error: EntityInvalidException -> ... }" <br>

### Testprotokoll
| Testfall Nummer | Test Nummer | OK / NOK | Bemerkungen | Tester       | Datum      | Unterschrift |
|-----------------|-------------|----------|-------------|--------------|------------|--------------|
| 1.1             | 1           | `OK `    | /           | M. Schweizer | 05.04.2022 | `M. Sch`     | 
| 2.1             | 1           | `OK `    | /           | M. Schweizer | 05.04.2022 | `M. Sch`     | 
| 3.1             | 1           | `OK`     | /           | M. Schweizer | 05.04.2022 | `M. Sch`     | 
| 4.1             | 1           | `OK`     | /           | M. Schweizer | 05.04.2022 | `M. Sch`     | 
| 5.1             | 1           | `OK `    | /           | M. Schweizer | 05.04.2022 | `M. Sch`     | 

### Testbericht

Guten Tag Herr Projektabnehmer

Ich habe Ihre Applikation nach den vorgegebenen Testfällen getestet. Dabei konnte ich keine Fehler finden, die entsprechenden Resultate des Testes entnehmen Sie dem Testprotokoll. So empfehle ich Ihnen, gemäss Planung im Projekt fortzufahren.
Ich freue mich über eine Rückmeldung.

Mit Freundliche Grüssen
Micha Schweizer
