# Architektur

Alexander --> Architekturdiagramm

## Timetable-Service

Lukas

## Booking-Service

Alexander

# Quarkus

[Quarkus](https://quarkus.io/) ist ein Framework zur Erstellung von Anwendungen auf der JVM, die in Container-Umgebungen eingesetzt werden.
Mit Quarkus erstellte Anwendungen zeichnen sich durch einen schnellen Applikationsstart und geringen Arbeitsspeicherverbrauch aus.
Zusätzlich können bei der Verwendung von GraalVM native Binaries der Anwendungen erzeugt werden, welche sich durch noch geringere Startzeiten und Ressourcenverbrauch auszeichnen.
Darüber hinaus ist Quarkus für eine gute Zusammenarbeit mit der Containerplattform Kubernetes optimiert.

Da die von GraalVM für native Binaries verwendete SubstrateVM noch nicht alle Features der JVM unterstützt und insbesondere mit Reflection Probleme bereitet, vermeidet Quarkus den Einsatz von Reflection und bietet eine Möglichkeit, bei Applikationsstart nicht unterstütze Elemente als Warnungen anzuzeigen.

Quarkus ist mit einer Vielzahl von Java-Libraries und Frameworks, darunter Eclipse MicorProfile in der aktuellsten Version 3.3, kompatibel.
Ebenfalls bietet Quarkus Unterstütztung für Basistechnologien, die für Webanwendungen benötigt werden, wie z.B. JDBC, JPA und JTA.
Zusätzlich werden noch reaktive Programmierkonzepte, wie z.B. die APIs von Eclipse Vert.x, unterstützt.

Quarkus bietet ebenfalls einen sehr guten [Projektgenerator](https://code.quarkus.io/), mit welchem die Dependencies auf einfache Art und Weise ausgewählt werden können.
Diesen haben wir auch für unsere Implementierung genutzt.

## Unsere Erfahrungen

Durch die Verwendung des Projektgenerators, die gute Dokumentation bereitete Quarkus während der Implementierung kaum Probleme.
Positiv anzumerken ist, dass wir einen GitHub-Issue erstellt haben, welcher sehr schnell vom Entwicklerteam beantwortet wurde.

Ursprünglich wollten wir die Implementierung des Timetable-Services mit [Thorntail](https://thorntail.io/) durchführen.
Allerdings wird Thorntail laut [einem Blogpost](https://thorntail.io/posts/thorntail-community-announcement-on-quarkus/) nicht mehr mit voller Teamstärke weiterentwickelt und die Zukunft liegt in Quarkus.
Dies ist allerdings auf der Thorntail-Projektseite mit Stand April 2020 nur schwer ersichtlich.

# Wildfly mit Microprofile

Alexander

# OpenLiberty

Der Booking-Service sollte ursprünglich mit [IBM OpenLiberty](https://www.openliberty.io/) entwickelt werden.
Wir haben uns aufgrund der [sehr guten und einsteigerfreundlichen Dokumentation](https://www.openliberty.io/guides/) von OpenLiberty hierzu entschieden.
Die Dokumentationsbeispiele liesen sich auch sehr gut auf unser Projekt übertragen, allerdings standen wir schnell vor dem Problem, dass wir beide es trotz mehrstündiger Bemühungen nicht schafften, den JDBC-Treiber für PostgreSQL und die JTA-Data-Source im System zu registrieren.
Wir haben dabei [eine](https://blog.sebastian-daschner.com/entries/openliberty-with-postgres) [Vielzahl](https://openliberty.io/blog/2019/07/18/microprofile-30-developer-experience.html#postgresql) [von](https://blog.sebastian-daschner.com/entries/openliberty-override-configuration) [Diskussionsbeiträgen](https://stackoverflow.com/questions/59368683/how-to-add-postgresql-driver-jar-to-open-liberty-microprofile-fat-jar-created) probiert und keine Lösung hat funktioniert.

Nichtsdestotrotz konnten wir aus den Tutorials viel wertvolles Wissen über die MicroProfile-APIs gewinnen und aufgrund der Standardisierung sehr gut auf Quarkus und Wildfly übertragen.

# MicroProfile Config

## Beschreibung

MicroProfile Config ist ein API, welches den Zugriff auf die Konfiguration einer Anwendung mittels Dependency-Injection ermöglicht.
Dabei baut MicroProfile Config auf CDI auf.
Darüber hinaus ist MicroProfile Config in der Lage, Konfigurationsparameter aus mehreren Quellen zu vereinigen und der Anwendung über ein einheitliches API zur Verfügung zu stellen.
Jeder Konfigurationsquelle wird dabei eine Priorität zugewiesen und im Falle, dass zwei Konfigurationsparameter über den selben Schlüssel angesprochen werden, wird dem Wert aus der Quelle mit der höchten Priorität Vorrang gegeben.
Neben den vom Applikationsserver standardmäßig bereitgestellten Konfigurationsquellen können natürlich selbstimplementierte Quellen hinzugefügt werden.
Unser Projekt verwendet beispielsweise Werte aus einer Redis-Key-Value-Datenbank.

## Verwendung in Services

Das Booking-Service verwendet MicroProfile, um die Basis-URL des Timetable-Services aus der Konfiguration zu laden.

Quarkus hat eine ausgezeichnete Unterstützung für MicroProfile Config und wir haben eines unserer Experimente so ausgebaut, dass die gesamte Konfiguration für Quarkus über das Config-API geladen wurde.

Wie bereits zuvor erwähnt, haben wir auch eine selbstimplementierte Konfigurationsquelle implementiert, welche auf die in einem Redis-Key-Value-Store gespeicherte Konfiguration zugreift.
Dies bietet den Vorteil, dass mittels Value-Providern die Konfigurationswerte sich zur Laufzeit des Programms ändern können.

## Implementierung

### Zugriff auf Konfigurationsparameter

Der Zugriff auf Konfigurationsparameter ist über den im MicroProfile enthaltenen CDI-Mechanismus am einfachsten und elegantesten möglich, wie der folgende Quellcodeauszug zeigt:

```java
@Inject @ConfigProperty(name = "timetableService", defaultValue="http://127.0.0.1:8080") 
private String timetableServiceUrl;
```

`@Inject` gibt an, dass der Wert über CDI injiziert wird und `@ConfigProperty`, dass dieser über MicroProfile Config ermittelt wird.
Der Annotationsparameter `name` gibt den Schlüssel des Konfigurationsparameter an und der optionale Annotationsparameter `defaultValue` gibt einen Standardwert an, falls der Schlüssel in keiner Konfiguration gefunden wird.

MicroProfile Config ist ein Basis-API, auf das andere MicroProfile-APIs aufbauen.
So kann z.B. bei der Verwendung von MicroProfile Rest Client die URL des aufgerufenen Service aus der Konfiguration geladen werden.
Hierzu ist einfach der Schlüssel des Konfigurationsparameters mit der URL des REST-Endpunkts bei der als Parameter `configKey` der Annotation zur Registrierung des REST-Clients anzugeben.
Dies ist unter anderem im unten zu sehenden Beispiel gegeben:

```java
@RegisterRestClient(configKey = "timetableService")
```

### Benutzerdefinierte Konfigurationsquelle

Um eine benutzerdefinierte Konfigurationsquelle zu erstellen, sind zwei Schritte notwendig:

1. Implementierung der Konfigurationsquelle
2. Registrierung der Konfigurationsquelle als Service-Provider

Die Programmierung einer Konfigurationsquelle erfolgt durch Erstellung einer Klasse, welche das Interface `org.eclipse.microprofile.config.spi.ConfigSource` implementiert.

Das Interface `ConfigSource` definiert die Methoden `getProperties`, `getPropertyNames`, `getValue`, `getName` und `getOrdinal`.

Die Methode `getProperties` liefert eine Map, welche die Schlüssel auf die Werte abbildet zurück.
Die Properties unserer Services haben wir als Member eines Hashes in Redis modelliert.
Der Name des Hashes entspricht dabei dem Namen des Services, also entweder `booking ` oder `timetable`.
Die Implementierung der Methode `getProperties` ermittelt zuerst mittels des Kommandos `HKEYS` die Schlüssel aller Werte des Hashes, erstellt eine Map, welche das Ergebnis repräsentiert und fragt anschließend mittels `HGET` den Wert für jeden Schlüssel ab.

Die Implementierung der Methode `getPropertyNames` liefert einfach das Ergebnis der Abfrage `HKEYS` zurück.

Zur Abfrage einzelner Properties dient die Implementierung der Methode `getValue`.
Diese gibt das Ergebnis der Abfrage `HGET` zurück.

Für `HKEYS` und `HGET` wird dabei jeweils der Name des Hashes benötigt.
Diesen lädt die Konfigurationsdatei über eine weitere Konfigurationsdatei im JSON-Format zum Start der Applikation.
Die JSON-Datei muss den Namen `redisConfiguration.json` haben und sich in der Wurzel des Ressourcenverzeichnisses befinden.
Neben dem Präfix (entweder `booking` oder `timetable`) enthält diese Datei noch die Konfiguration zum Zugriff auf Redis.
Zum Parsen der Datei wird JSON-P verwendet.

Um die benutzerdefinierte Konfigurationsquelle als Service zu registrieren, ist der vollständig qualifizierte Name der Klasse in der Datei `org.eclipse.microprofile.config.spi.ConfigSource` im Verzeichnis `src/main/resources/META-INF/services` anzugeben.
Für die von uns definierte Datei hat die Konfiguration den folgenden Inhalt:

```
eu.nighttrains.configuration.redis.RedisConfigurationSource
```

## Ergebnisse

Wie bereits erwähnt, wird sowohl die URL des Timetable-REST-Endpunkts im Booking-Service als auch die gesamte Konfiguration des Quarkus-basierten Timetable-Services aus einer Redis-KV-Datenbank geladen.
Leider bietet MicroProfile Config nicht sehr viele visuelle Möglichkeiten, es ist aber eine wichtige Basistechnologie für andere MicroProfile-APIs.

# MicroProfile RestClient

Alexander

## Beschreibung

## Verwendung in Services

## Implementierung

## Ergebnisse

# MicroProfile OpenAPI

Lukas

## Beschreibung

## Verwendung in Services

## Implementierung

## Ergebnisse

# MicroProfile OpenTracing

Lukas

## Beschreibung

## Verwendung in Services

## Implementierung

## Ergebnisse

# Ergebnisse

Lukas & Alexander

# Setup

Alexander

## Docker-Container

### PostgreSQL

### Jaeger

### Redis

## Timetable-Service

## Booking-Service

## Frontend
