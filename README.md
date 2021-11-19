Jameica ![logo](./src/img/hibiscus-icon-64x64.png)
=======
> If you do not unterstand German but want more information or to contribute, please try to contact [the maintainer or one of the contributors](https://github.com/willuhn/jameica/graphs/contributors).

Jameica ist eine _Application_-Plattform, die in Java geschrieben ist.
Sie bietet die Basisfunktionalit�t und GUI-Komponenten, um Dir dabei zu helfen, Dich auf Deine Idee zu konzentrieren!

Es ist die Grundlage f�r Plugins wie [Hibiscus](https://github.com/willuhn/hibiscus) oder [JVerein](https://www.jverein.de/).

![Screenshot](https://www.willuhn.de/products/jameica/screenshots/04.png)

Jameica nutzt SWT f�r die grafische Oberfl�che und bietet als Plattform mehrere Services (GUI-Toolkit, Logging, Security, Backup, Lifecycle-Management, Message-Bus) f�r die installierten Plugins.
Die flexible Plugin-Arbeitsweise ist angelehnt an [OSGi](https://de.wikipedia.org/wiki/OSGi).

![Stack](https://www.willuhn.de/products/jameica/grafx/schema.png)

Schnellstart
----------
Hier eine Anleitung f�r alle, die direkt loslegen wollen.

> :warning: F�r Jameica muss [Java](https://adoptopenjdk.net/) installiert sein!
> Die notwendige Minimalversion ist [hier](https://www.willuhn.de/wiki/doku.php?id=start#kompatibilitaetsmatrix) einsehbar.
> Falls unsicher, �berpr�fe die Version auf Deinem Computer, indem Du `java -version` im Terminal/ der Kommandozeile ausf�hrst.

1. Lade das fertige Paket f�r Dein Betriebssystem.

    | 64bit | 32bit f�r alte Systeme | andere Wege |
    | ----- | ----- | ---------- |
    | [Linux](http://www.willuhn.de/products/jameica/releases/current/jameica/jameica-linux64.zip) | [Linux (32bit)](http://www.willuhn.de/products/jameica/releases/current/jameica/jameica-linux.zip) | [AUR, DEP, RPM, Launchpad, flatpak](https://www.willuhn.de/wiki/doku.php?id=support:bezugsquellen) |
    | [macOS](http://www.willuhn.de/products/jameica/releases/current/jameica/jameica-macos64.zip) | [Mac OS X "Tiger" (10.4) - "Lion" (10.7))](http://www.willuhn.de/products/jameica/releases/current/jameica/jameica-macos32.zip) | [Homebrew](http://brew.sh/): `brew cask install jameica` |
    | [Windows](http://www.willuhn.de/products/jameica/releases/current/jameica/jameica-win64.zip) | [ab Windows 7](http://www.willuhn.de/products/jameica/releases/current/jameica/jameica-win32.zip) | [Chocolatey](https://chocolatey.org/): `choco install jameica` |

    Es gibt auch eine [generische Version f�r weitere Betriebssysteme (OpenBSD, ...)](http://www.willuhn.de/products/jameica/releases/current/jameica/jameica.zip) zum Download.

2. Entpacke den Inhalt der ZIP-Datei in einen Ordner auf der Festplatte.

3. Starte die Applikation
    * Windows: `jameica-win64.exe`
    * Linux: `./jameica.sh`
    * macOS: Doppelklick auf das Symbol

    > Jameica schreibt _alle_ Dinge (Einstellungen, Logdateien, Daten) in einen einzigen Ordner namens `.jameica` im Benutzerverzeichnis (`%USERPROFILE%` in Win, `~` in Unix/Linux/macOS).

4. Installiere Plugins via _"Datei -> Plugins online suchen..."_.
   In der angezeigte Auswahlliste wird das gew�nschte Plugin mit einem Klick auf den Button "Installieren..." automatisch heruntergeladen und konfiguriert

    > Es gibt eine �bersicht �ber [alle Plugins](https://www.willuhn.de/wiki/doku.php?id=start#alle_bekannten_jameica-plugins) und deren Abh�ngigkeiten zu anderen Plugins.

Dokumentation
-------------
Eine detaillierte Projektdokumentation ist im [Wiki des Autors](https://www.willuhn.de/wiki/) einsehbar.

Dieses Wiki beinhaltet u.a. Informationen wie:
* �bersicht der mind. erforderlichen Java-Version je Releaseversion von Jameica
* [h�ufige Fragen/ FAQ](https://www.willuhn.de/wiki/doku.php?id=support:faq)
* [Anleitung f�r Updates](https://www.willuhn.de/wiki/doku.php?id=support:update)
* [Anleitung f�r Backups](https://www.willuhn.de/wiki/doku.php?id=support:backup)

Mitmachen
---------
### Du hast etwas gefunden, was verbessert werden sollte?
Super! Jedes Feedback hilft!
Bitte pr�fe jedoch zuerst, ob es bereits im [OnlineBanking-Forum](https://homebanking-hilfe.de/forum/index.php?f=33) bekannt ist.
Vielleicht gibt es bereits eine (�bergangs-)L�sung f�r Dein Problem.
Falls nicht, so schaue bitte auf [dieser Website](https://www.willuhn.de/wiki/doku.php?id=support:fehlermelden) nach detaillierten Anweisungen f�r die n�chsten Schritte.

### Du kannst programmieren und m�chtest helfen?
Nutze die M�glichkeiten von Github :-)
Erstelle einen _Fork_ von diesem Repository und sende einen _Pull-Request_ mit den Anpassungen.

### Du m�chtest Dein eigenes Jameica-Plugin programmieren?
Im Github-Projekt [Jameica Example Plugin](https://github.com/willuhn/jameica.example) findest Du alle Informationen.

Lizenz
------
Jameica steht unter GPLv2 (siehe [LICENSE](./LICENSE))

Danksagung
----------
Jameica wird seit 2003 von Olaf Willuhn in seiner Freizeit entwickelt.

Vielen Dank an [alle Mitwirkenden](https://github.com/willuhn/jameica/graphs/contributors), die dabei geholfen haben, diese Software ein bisschen besser zu machen!
