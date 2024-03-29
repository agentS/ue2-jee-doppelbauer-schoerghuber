<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Inhaltsverzeichnis**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Architektur](#architektur)
  - [Ablauf der Kommunikation](#ablauf-der-kommunikation)
  - [Timetable-Service](#timetable-service)
    - [Domänenmodell](#dom%C3%A4nenmodell)
    - [Suchen von Verbindungen](#suchen-von-verbindungen)
  - [Booking-Service](#booking-service)
    - [Domänenmodell](#dom%C3%A4nenmodell-1)
    - [Ticket](#ticket)
    - [Reservierung](#reservierung)
    - [Endpunkte](#endpunkte)
    - [Verwendung](#verwendung)
      - [Create Booking Request](#create-booking-request)
      - [Create Booking Response](#create-booking-response)
      - [Find Booking By ID Request](#find-booking-by-id-request)
      - [Find Booking By ID Response](#find-booking-by-id-response)
  - [DTOs](#dtos)
- [Quarkus](#quarkus)
  - [Unsere Erfahrungen](#unsere-erfahrungen)
  - [Automatisierte Tests](#automatisierte-tests)
    - [Ergebnisse](#ergebnisse)
- [Wildfly mit Microprofile](#wildfly-mit-microprofile)
  - [Unsere Erfahrungen](#unsere-erfahrungen-1)
- [OpenLiberty](#openliberty)
- [MicroProfile Config](#microprofile-config)
  - [Beschreibung](#beschreibung)
  - [Verwendung in Services](#verwendung-in-services)
  - [Implementierung](#implementierung)
    - [Zugriff auf Konfigurationsparameter](#zugriff-auf-konfigurationsparameter)
    - [Benutzerdefinierte Konfigurationsquelle](#benutzerdefinierte-konfigurationsquelle)
  - [Ergebnisse](#ergebnisse-1)
- [MicroProfile RestClient](#microprofile-restclient)
  - [Beschreibung](#beschreibung-1)
  - [Verwendung in Services](#verwendung-in-services-1)
  - [Implementierung](#implementierung-1)
    - [Interface Definition](#interface-definition)
    - [Verwendung mittels CDI](#verwendung-mittels-cdi)
  - [Ergebnisse](#ergebnisse-2)
- [MicroProfile OpenAPI](#microprofile-openapi)
  - [Beschreibung](#beschreibung-2)
  - [Verwendung in Services](#verwendung-in-services-2)
  - [Implementierung](#implementierung-2)
    - [Serverseitige OpenAPI-Dokument-Generierung](#serverseitige-openapi-dokument-generierung)
    - [Generierung des Clients](#generierung-des-clients)
  - [Ergebnisse](#ergebnisse-3)
- [MicroProfile OpenTracing](#microprofile-opentracing)
  - [Beschreibung](#beschreibung-3)
  - [Verwendung in Services](#verwendung-in-services-3)
  - [Implementierung](#implementierung-3)
  - [Ergebnisse](#ergebnisse-4)
- [Frontend](#frontend)
- [Setup](#setup)
  - [Docker-Container](#docker-container)
    - [PostgreSQL](#postgresql)
    - [Jaeger](#jaeger)
    - [Redis](#redis)
  - [Timetable-Service](#timetable-service-1)
    - [Initialisierung der Konfigurations-Datenbank (REDIS)](#initialisierung-der-konfigurations-datenbank-redis)
    - [Initialisierung der Konfigurationsdatei zum Zugriff auf die Konfigurations-Datenbank](#initialisierung-der-konfigurationsdatei-zum-zugriff-auf-die-konfigurations-datenbank)
  - [Booking-Service](#booking-service-1)
    - [Starten von Wildfly](#starten-von-wildfly)
    - [Initialisierung der Konfigurations-Datenbank (REDIS)](#initialisierung-der-konfigurations-datenbank-redis-1)
    - [Initialisierung der Konfigurationsdatei zum Zugriff auf die Konfigurations-Datenbank](#initialisierung-der-konfigurationsdatei-zum-zugriff-auf-die-konfigurations-datenbank-1)
    - [Wildfly Jaeger Opentracing](#wildfly-jaeger-opentracing)
    - [Wildfly Benutzer](#wildfly-benutzer)
    - [Wildfly Datenbank-Treiber und DataSource](#wildfly-datenbank-treiber-und-datasource)
  - [Frontend](#frontend-1)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Architektur

Die Architektur setzt sich im wesentlichen aus vier Komponenten zusammen.
In Docker werden ein Jaeger-Container, ein PostgreSQL-Container und ein Redis-Container gehostet. Der PostgreSQL-Container beinhaltet ein DBMS mit zwei Datenbanken (`timetable` und `booking`, welche von dem entsprechenden Service genutzt werden).
Innerhalb des Quarkus Applikationsservers läuft der Timetable-Service. Dieser kommuniziert mit den Docker Containern und stellt eine REST-Schnittstelle nach Außen zur Verfügung.
Innerhalb des Wildfly Applikationsservers läuft der Booking-Service. Dieser kommuniziert ebenfalls mit den Docker Containern und stellt eine REST-Schnittstelle nach Außen zur Verfügung. Das React Frontend kommuniziert über die REST-Schnittstellen mit dem Timetable- und dem Booking-Service.

![Komponenten Diagramm](doc/img/component-diagram.png)

## Ablauf der Kommunikation
Das nachfolgende Sequenzdiagramm stellt den Ablauf des Buchens einer Zugverbindung von A nach B in einer etwas vereinfachten Form dar. Das Frontend interagiert sowohl mit dem Timetable-Service, als auch mit dem Booking-Service. Weiters kommuniziert das Booking-Service mit dem Timetable-Service um Informationen zu den Verbindungen sowie zu den einzelnen Stationen und Zügen zu bekommen, da diese Informationen vom Timetable-Service verwaltet werden.

![Vereinfachte Darstellung des Buchens von Verbindungen](doc/img/sequence-diagram.png)

## Timetable-Service

Der Timetable-Service ist für das Auslesen des Fahrplans und der Assets (Bahnhöfe, Zuggarnituren) verantwortlich.
Die folgende Aufzählung zeigt die wichtigsten Funktionalitäten des Services auf:

- Finden einer Verbindung zwischen zwei Bahnhöfen
- Anzeigen aller von einem Bahnhof erreichbaren Ziele
- Anzeigen aller Bahnhöfen und Suchen nach Bahnhöfen per Namen inkl. Funktionalität für Autocomplete im Frontend
- Anzeigen aller Zuggarnituren und Suche nach Zuggarnituren

### Domänenmodell

Die folgende Abbildung zeigt ein UML-Klassendiagramm des Domänenmodells.
Die Klassen des Domänenmodells sind jeweils mit JPA-Annotationen angereichert und werden daraus folgend mittels JPA persistiert.

Die Klasse `RailwayStation` dient zur Abbildung von Bahnhöfen, welche über eine ID und einen Namen verfügen.
Die Klasse `TrainConnection` stellt eine Zugverbindung dar, welche über eine ID, einen Code zur Identifizierung durch Passagiere und Personal (z.B. NJ466) und eine Menge von Waggons verfügt.

Ein Waggon (Klasse `TrainCar`) hat ebenfalls eine ID, eine Nummer, die innerhalb des Zuges eindeutig ist (z.B. 21) und Passagieren hilft, den Waggon, in welchem sich ihr Platz befindet, zu finden, einen Typ, also ob es sich um einen Schlaf-, Liege- oder Sitzwagen handelt und die Kapazität des Waggons.
Eine Navigation von einem Waggon zu der Zugverbindung, auf welcher dieser eingesetzt wird, ist ebenfalls möglich.

Den Zusammenhang welche Bahnhöfe mittels welcher Zugverbindung erreicht werden können stellt die Klasse `RailwayStationConnection` dar.
Eine Ausprägung dieser Domänenklasse definiert einen Hop einer Verbindung.
Ein Hop verbindet jeweils zwei aufeinanderfolgende Bahnhöfe, z.B. Wien Hauptbahnhof nach Wien Meidling auf der Verbindung von Wien Hauptbahnhof nach Zürich Hauptbahnhof.
Die gesamte Verbindung von z.B. Wien Hauptbahnhof nach Zürich Hauptbahnhof wird als eine Sequenz von Objekten der Klasse `RailwayStationConnection` modelliert: Ausgehend von Wien Hauptbahnhof nach Wien Meidling gibt es pro Zwischenhalt ein Objekt der Klasse `RailwayStationConnection` bis der Ankunftsbahnhof Zürich Hauptbahnhof entspricht.
Der Zielbahnhof eines Hops ist daher der Abfahrtsbahnhof des darauf folgenden Hops.
So ist z.B. Wien Meidlung der Ankunftsbahnhof des Hops von Wien Hauptbahnhof nach Wien Meidling und gleichzeitig der Ausgangsbahnhof des Hops von Wien Meidling nach St. Pölten.
Alle Hops einer direkten Zugverbindung (z.B. von Wien Hauptbahnhof nach Zürich Hauptbahnhof) haben daher das gleiche Objekt vom Typ `TrainConnection` zugeordnet.
Ein Umsteigen (z.B. in Wien Hauptbahnhof auf der Verbindung von Rom nach Berlin Hauptbahnhof) wird durch einen Wechsel des Objekts vom Typ `TrainConnection` signalisiert, wie er eben im Hop mit Ankunftsbahnhof Wien Hauptbahnhof zum nächsten Hop mit Abfahrtsbahnhof Wien Hauptbahnhof stattfindet.

![Klassendiagramm des Domänenmodells des Timetable-Services](doc/img/timetable/domainModellClassDiagram.svg)

### Suchen von Verbindungen

Die Suche einer Verbindung zwischen zwei Bahnhöfen ist einer der komplexeren Prozesse unserer Geschäftslogik, welchen wir daher hier kurz beschreiben möchten.

Der erste Schritt ist eine rekursive SQL-Abfrage, welche alle Hop-by-Hop-Verbindungen, die vom Abfahrtsbahnhof erreicht werden können, ermittelt.
Diese Abfrage, welche unten dargestellt ist, verwendet die Common-Table-Expressions des ANSI-SQL-1999-Standards, was eine Übertragbarkeit zwischen verschiedenen DBMS gewährleistet.
Da auch Zugverbindungen in die Gegenrichtung definiert sind, kommt es in der Abfrage zu Zyklen.
Um diese zu erkennen und die Suche beim Auftreten von Zyklen zu beenden, wird ein Array der Bahnhof-IDs des Suchpfades mitgeführt. Sobald eine ID doppelt vorkommt, wird der Suchzweig beendet.
Dieser Suchpfad ist ebenfalls für den nächsten Schritt, das Umwandeln in eine Suchbaumstruktor, von entscheidender Bedeutung.

```java
@RequestScoped
@Transactional
public class RailwayStationConnectionDaoJpa implements RailwayStationConnectionDao {
    // ...

    private static final String QUERY_ALL_CONNECTIONS_FROM =
            "WITH RECURSIVE connections_to(departurestation_id, arrivalstation_id, trainconnection_id, departuretime, arrivaltime, depth, searchpath, cycle) AS "
            + "("
            + "SELECT departurestation_id, arrivalstation_id, trainconnection_id, departuretime, arrivaltime, 1, ARRAY[departurestation_id], false "
            + "FROM railwaystationconnection "
            + "WHERE departurestation_id = :departureStationId "
            + "UNION ALL "
            + "SELECT R.departurestation_id, R.arrivalstation_id, R.trainconnection_id, R.departuretime, R.arrivaltime, C.depth + 1, searchpath || R.departurestation_id, R.arrivalstation_id = ANY(searchpath) "
            + "FROM connections_to AS C INNER JOIN railwaystationconnection AS R ON C.arrivalstation_id = R.departurestation_id "
            + "WHERE NOT cycle"
            + ") "
            + "SELECT C.departurestation_id, D.name AS departurestation_name, C.arrivalstation_id, A.name AS arrivalstation_name, C.trainconnection_id, T.code AS trainconnection_code, C.departuretime AS departuretime, C.arrivaltime AS arrivaltime, C.searchpath AS searchpath FROM connections_to AS C "
            + "INNER JOIN railwaystation AS D ON D.id = C.departurestation_id "
            + "INNER JOIN railwaystation AS A ON A.id = C.arrivalstation_id "
            + "INNER JOIN trainconnection AS T ON T.id = C.trainconnection_id "
            + "WHERE NOT cycle "
            + "ORDER BY C.searchpath ASC;";

    // ...
}
```

Nach dem Ermitteln aller Hop-by-Hop-Verbindungen wird die schnellste Verbindung mittels Iterative-Deepening-Search ermittelt.
Die Logik wurde in generischer Form von uns implementiert und befindet sich in der Methode `SearchGraphAlgorithms::findPathIterativeDeepeningSearch`.
Allerdings liegen die Hop-by-Hop-Verbindungen noch nicht in Form eines Suchbaums vor und müssen daher entsprechend konvertiert werden.
Die Logik hierzu findet sich in der Klasse `RailwayStationConnectionSearchGraph`, genauer gesagt in dessen Methode `createFromConnections`.
Jene Methode baut den Suchbaum von der Wurzel, also dem Abfahrtsbahnhof, entlang des in der SQL-Abfrage ermittelten Suchpfades auf.

Nach Ermittlung des Pfades konvertiert die Geschäftslogik das Ergebnis noch in entsprechende DTOs.

## Booking-Service

Der Booking-Service ist für das Reservieren von Plätzen in den Waggons eines Zugs, sowie für das Anlegen der Fahrscheine/Tickets, welche benötigt werden um von A nach B zu kommen, zuständig. Eine Buchung umfasst bei der vorliegenden Implementierung ein oder mehrere Tickets. Ein Ticket ist einem Zug zugeordnet und enthält alle Reservierungen für die jeweiligen Hops der Verbindung, welche mit diesem Zug gefahren werden. Zum Erstellen einer Buchung bekommt der Booking-Service das Startdatum, eine OriginId, eine DestinationId sowie den Waggontyp, welcher reserviert werden soll, übergeben. Die OriginId und die DestinationId verweisen auf Stationen, welche vom Timetable-Service verwaltet werden.

### Domänenmodell

Die folgende Abbildung zeigt ein UML-Klassendiagramm des Domänenmodells. Die Klassen des Domänenmodells sind jeweils mit JPA-Annotationen angereichert und werden daraus folgend mittels JPA persistiert. 

Die Klasse `Booking` dient zur Abbildung von Buchungen, welche über eine ID, OriginId (Id des Startbahnhofs), DestinationId (Id des Zielbahnhofs), eine Menge an Tickets und ein DepartureDate verfügen. Dabei bezieht sich der Startbahnhof auf den Start und der Zielbahnhof auf das Ziel der Zugverbindung (eine Zugverbindung besteht aus mehreren Hops).

Die Klasse `Ticket` dient zur Abbildung von Tickets, welche über eine ID, OriginId (Id des Startbahnhofs), DestinationId (Id des Zielbahnhofs) und eine Menge an Reservierungen verfügen. Hier bezieht sich die OriginId und die DestiationId auf einen Teilabschnitt der Zugverbindung (= Teilmenge an Hops), für welche dieses Ticket gilt. Also jene Hops, welche mit demselben Zug gefahren werden.

Die Klasse `Reservation` dient zur Abbildung von Reservierungen, welche über eine ID, OriginId, DestinationId, einen TrainCode, eine TrainCarId, ein Reservierungsdatum sowie die DepartureTime und die ArrivalTime verfügen. Die OriginId und die DestinationId beziehen sich dabei auf einen konkreten Hop von A nach B.

![Klassendiagramm des Domänenmodells des Booking-Services](doc/img/booking/booking-models.png)

### Ticket
Um eine Buchung anlegen zu können werden Informationen zu den einzelnen Verbindungen zwischen dem Startbahnhof und dem Zielbahnhof benötigt. Diese stellt der Timetable-Service zur Verfügung. Dabei kann es sein, dass man ein, oder mehrmals umsteigen muss (= Zug wechseln). Dies wurde so implementiert, dass für jeden Zug ein eigenes Ticket ausgestellt wird. Da der Timetable-Service alle Verbindungen auf einmal sendet, unabhängig davon, ob der Fahrgast umsteigen muss oder nicht, um die Netzwerklast zu reduzieren, ist es Aufgabe des Booking-Service die Verbinungen den jeweiligen Zügen zuzuordnen um auch entsprechend die Reservierungen durchführen zu können. 

### Reservierung
Die Züge haben Waggons, welche geordnet sind, eine gewisse Kapazität aufweisen und einer Kategorie (`SEATER`, `SLEEPER`, `COUCHETTE`) zugewiesen sind. Die gewünschte Kategorie muss bei der Buchung angegeben werden. Weiters gehen wir in unserem Anwendungsfall davon aus, dass alle Züge jeden Tag zur selben Uhrzeit fahren. Demnach muss bei der Reservierung auch das Datum berücksichtigt werden. So ermittelt der Booking-Service zuerst für eine Verbindung und deren Hops das dazugehörige Datum ausgehend vom Startdatum der Buchung. Da es Verbindungen gibt, welche sich über mehrere Tage erstrecken, muss dies ebenfalls bei der Reservierung berücksichtigt werden.

Bei der Reservierung wird so vorgegangen, dass ein Platz in einem Waggon der gewünschten Kategorie reserviert wird. Dabei werden die Waggons von vorne (vorderes Ende des Zugs) nach hinten aufegfüllt. Der Booking-Service ermittelt anhand der bereits bestehenden Buchungen, ob und in welchem Waggon der gewünschten Kategorie für eine Verbindung und deren Hops zum benötigten Datum noch ein Platz frei ist und reserviert diesen.


### Endpunkte
Um die oben beschriebene Funktionalität nach außen zugänglich zu machen stellt das Booking-Service einen REST-Endpunkt zur Verfügung welcher mit den Methoden POST und GET angesprochen werden kann.
```java
@RequestScoped
@Path("/bookings")
public class BookingResource {
        // ...
        @POST
        @Path("/")
        @Produces(MediaType.APPLICATION_JSON)
        // ...
        public Response postBooking(@Valid BookingRequestDto2 bookingRequest) {
                // ...
        }

        @GET
        @Path("/{id}")
        @Produces(MediaType.APPLICATION_JSON)
        // ...
        public Response findById(@PathParam("id") Long id) {
                // ...
        }
        // ...
}
```

### Verwendung
#### Create Booking Request
![POST](doc/img/booking/post.PNG)
```json
{
    "originId": 0,
    "destinationId": 4,
    "journeyStartDate": "2020-04-26",
    "trainCarType": "SEAT"
}
```
#### Create Booking Response
![Response Header](doc/img/booking/created-response.PNG)
```json
{
    "bookingId": 27
}
```
#### Find Booking By ID Request
![GET](doc/img/booking/get.PNG)
#### Find Booking By ID Response
```json
{
    "id": 27,
    "originId": 0,
    "originStationName": "Wien Hauptbahnhof",
    "destinationId": 4,
    "destinationStationName": "Linz/Donau",
    "departureDate": "2020-04-26",
    "tickets": [
        {
            "id": 26,
            "originId": 0,
            "destinationId": 4,
            "trainCode": "NJ 466",
            "stops": [
                {
                    "connection": {
                        "date": "2020-04-26",
                        "departureStation": {
                            "id": 0,
                            "name": "Wien Hauptbahnhof"
                        },
                        "arrivalStation": {
                            "id": 1,
                            "name": "Wien Meidling"
                        },
                        "departureTime": "21:27:00",
                        "arrivalTime": "21:32:00"
                    },
                    "reservation": {
                        "id": 22,
                        "trainCar": {
                            "id": 5,
                            "number": 25,
                            "type": "SEAT"
                        }
                    }
                },
                {
                    "connection": {
                        "date": "2020-04-26",
                        "departureStation": {
                            "id": 1,
                            "name": "Wien Meidling"
                        },
                        "arrivalStation": {
                            "id": 2,
                            "name": "St. PÃ¶lten"
                        },
                        "departureTime": "21:35:00",
                        "arrivalTime": "22:00:00"
                    },
                    "reservation": {
                        "id": 23,
                        "trainCar": {
                            "id": 5,
                            "number": 25,
                            "type": "SEAT"
                        }
                    }
                },
                {
                    "connection": {
                        "date": "2020-04-26",
                        "departureStation": {
                            "id": 2,
                            "name": "St. PÃ¶lten"
                        },
                        "arrivalStation": {
                            "id": 3,
                            "name": "Amstetten"
                        },
                        "departureTime": "22:02:00",
                        "arrivalTime": "22:27:00"
                    },
                    "reservation": {
                        "id": 24,
                        "trainCar": {
                            "id": 5,
                            "number": 25,
                            "type": "SEAT"
                        }
                    }
                },
                {
                    "connection": {
                        "date": "2020-04-26",
                        "departureStation": {
                            "id": 3,
                            "name": "Amstetten"
                        },
                        "arrivalStation": {
                            "id": 4,
                            "name": "Linz/Donau"
                        },
                        "departureTime": "22:29:00",
                        "arrivalTime": "22:55:00"
                    },
                    "reservation": {
                        "id": 25,
                        "trainCar": {
                            "id": 5,
                            "number": 25,
                            "type": "SEAT"
                        }
                    }
                }
            ]
        }
    ]
}
```

## DTOs

Wir haben unsere Anwendungen nach dem Dreischichtenmuster entworfen.
Eine Frage, die sich bei der Verwendung von JPA stellt, sind, [wann und wo die Transaktionsgrenzen gezogen werden](https://stackoverflow.com/questions/23118789/why-we-shouldnt-make-a-spring-mvc-controller-transactional).
Wir haben uns dafür entschieden, die REST-Controller nicht in die Transaktion eingreifend zu halten.
Jedoch kann es in diesem Fall zu Problemen mit dem Lazy-Loading-Feature der JPA kommen.
Um dies zu vermeiden, haben wir uns entschieden, DTOs (Data-Transfer-Objects) einzuführen, welche von den Methoden der Geschäftslogik mit den benötigten Daten aus den Domänenobjekten befüllt werden.
Ein weiterer Vorteil dieses Ansatzes ist, dass wir somit eine Trennung von Domänenmodell zu den von den REST-Endpunkten verwendeten DTO-Klassen erhalten und beide somit unabhängig entwickeln können.

Die DTO-Klassen spiegeln in den meisten Fällen die Domänenklassen wieder, es gibt aber auch DTO-Klassen, für die es keine Domänenklassen gibt.
Ein Beispiel ist die DTO-Klasse `RailwayStationDestinationsDto`, welche für einen Abfahrtsbahnhof alle erreichbaren Bahnhöfe angibt.

Darüber hinaus ermöglicht es diese Trennung REST-spezifische Annotationen, wie z.B. jene von MicroProfile OpenAPI von datenbankspezifischen, wie jenen der JPA, zu trennen.

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

## Automatisierte Tests

Quarkus hat eine herausragende Unterstützung für automatisierte Tests auf mehreren Stufen:

- Mittels Rest-Easy können einfach Integrationstests der REST-Endpunkte durchgeführt werden, wobei das zurückgelieferte JSON-Dokument von der Test-Suite inspiziert werden muss.
- Da auch im Testmodus Dependency-Injection via CDI vollständig unterstützt wird, können Integrationstests über mehrere Schichten des Systems durchgeführt werden. Die Unterstützung geht sogar so weit, dass z.B. Annotationen der JTA wie `@Transactional` unterstützt werden.
- Natürlich können über die mitgelieferte Bibliothekt JUnit 5 auch Unit-Tests durchgeführt werden.

Wir haben uns für den Timetable-Service auf Integrationstests der Geschäftslogik festgelegt, da wir somit die volle Funktionalität der Bibliothek JUnit auf Java-Objekten ausnutzen können, was insbesondere gegenüber Tests der REST-Controller erhebliche Vorteile bietet.
So ist es wesentlich einfacher die Inhalte einer Java-Collection zu prüfen als ein JSON-Dokument.
Außerdem sind unsere REST-Controller lediglich sehr dünne Wrapper um die Geschäftslogik und durch Tests der Geschäftslogik werden bereits weite Teile des Systems automatisch getestet.

### Ergebnisse

Die Tests werden in dem Standard-Ordner von Maven (`src/test/java`) abgelegt.
Als Beispiel folgt unten ein Auszug aus unserer Testsuite für das Suchen von Verbindungen zwischen zwei Bahnhöfen.
Die Testmethode `testFindRouteWithConnection` sucht eine Verbindung zwischen Berlin und Zürich und überprüft, ob eine Verbindung zurückgegeben wird und ob diese über die gewünschte Anzahl von Zwischenhalten verfügt.
Die Testmethode `testFindRouteForNonExistingStop` stellt sicher, dass eine entsprechende Exception ausgelöst wird, wenn es keine Verbindung zwischen zwei Bahnhöfen gibt.

```java
@QuarkusTest
public class RouteManagerTest {
    @Inject
    private RouteManager routeManager;

    @Test
    public void testFindRouteWithConnection() throws NoRouteException {
        var connections = this.routeManager.findAllStopsBetween(43L, 14L);
        assertNotNull(connections);
        assertTrue(connections.size() > 0);
        assertEquals(29, connections.size());
    }

    @Test
    public void testFindRouteForNonExistingStop() {
        assertThrows(
                NoRouteException.class,
                () -> this.routeManager.findAllStopsBetween(0L, -1L)
        );
    }
}
```

Das Ausführen der Tests funktioniert, wie gewohnt, über das Maven-Goal `test`.
Unten folgt noch ein Auszug aus dem Testprotokoll, welches die Ausführung des zuvor genannten Maven-Goals ergibt.

```
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.104 s - in eu.nighttrains.timetable.test.RailwayStationManagerTest
[INFO] Running eu.nighttrains.timetable.test.RouteManagerTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.117 s - in eu.nighttrains.timetable.test.RouteManagerTest
[INFO] Running eu.nighttrains.timetable.test.TrainConnectionManagerTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.079 s - in eu.nighttrains.timetable.test.TrainConnectionManagerTest
17:42:19 INFO  traceId=, spanId=, sampled= [io.quarkus] (main) Quarkus stopped in 0.039s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  12.016 s
[INFO] Finished at: 2020-04-10T17:42:19+02:00
[INFO] ------------------------------------------------------------------------
```

# Wildfly mit Microprofile

[Wildfly](https://wildfly.org/) ist ein Application Server nach dem Java-EE-Standard und Teil des JBoss Middleware-Frameworks. Wildfly ist in der Programmiersprache Java implementiert und ist plattformunabhängig.  Weiters bildet Wildfly die Grundlage der kommerziellen Version der IBM Red Hat JBoss Enterprise Application Plattform (EAP). In der Version **17.0.1** wurde Wildfly als Jakarta EE 8 kompatible Implementierung zertifiziert. Seit der Version **18.0.1** unterstützt WildFly MicroProfile 3.0 ([Wildfly 18](https://wildfly.org/news/2019/10/03/WildFly18-Final-Released/)). Dabei wird Eclipse-Microprofile out of the box vom WildFly Applikationsserver unterstützt.


## Unsere Erfahrungen
Um die Microprofile Implementierungen von Wildfly einzubinden war es notwendig die folgende "Bill Of Material" (BOM - org.wildfly.bom [Example Bom Wildfly](https://github.com/wildfly/boms/blob/master/microprofile/pom.xml)) in die Datei pom.xml einzubinden ([Tutorial](http://www.mastertheboss.com/javaee/eclipse-microservices/how-to-build-microprofile-application-on-wildfly)). Damit IntelliJ die Bibliotheken erkennt war es weiters notwendig die BOM Abhängigkeit nicht nur direkt als dependency unter dependencies anzugeben, sondern diese in das dependencyManagement Element zu geben. Weiters hat dieses Vorgehen zum Einbinden der Microprofile Abhängigkeiten für die Wildfly Version 18.0.1 nicht funktioniert. Deshalb haben wir uns zu einem Upgrade entschieden und in unserem Projekt die Wildfly Version 19.0.1 verwendet, mit welcher es sehr gut funktioniert hat.

```xml
<dependencyManagement>
        <dependencies>
                <dependency>
                <groupId>org.wildfly.bom</groupId>
                <artifactId>wildfly-microprofile</artifactId>
                <version>19.0.0.Final</version>
                <scope>import</scope>
                <type>pom</type>
                </dependency>
        </dependencies>
</dependencyManagement>

<dependencies>
        <dependency>
                <groupId>org.eclipse.microprofile.openapi</groupId>
                <artifactId>microprofile-openapi-api</artifactId>
        </dependency>
        <dependency>
                <groupId>org.eclipse.microprofile.rest.client</groupId>
                <artifactId>microprofile-rest-client-api</artifactId>
        </dependency>
        <dependency>
                <groupId>org.eclipse.microprofile.opentracing</groupId>
                <artifactId>microprofile-opentracing-api</artifactId>
        </dependency>
        <dependency>
                <groupId>org.eclipse.microprofile.config</groupId>
                <artifactId>microprofile-config-api</artifactId>
        </dependency>
<!-- ... -->
</dependencies>
```
Ansonsten kann man sagen, dass es keine wirklich gute Dokumentation von seiten Wildflys gibt, welche die Implementierung von Anwendungen mit Eclipse-Microprofile unterstützen. Abgesehen von den Schwierigkeiten mit den Abhängigkeiten zu Beginn gab es jedoch keine weiteren Probleme in Zusammenhang mit Wildfly zu bewältigen.

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
Der folgende Auszug zeigt die Konfigurationsdatei für den Timetable-Service:

```json
{
  "hostname": "127.0.0.1",
  "port": 6379,
  "timeout": 3000,
  "maximumPoolSize": 5,

  "prefix": "timetable"
}
```

Um die benutzerdefinierte Konfigurationsquelle als Service zu registrieren, ist der vollständig qualifizierte Name der Klasse in der Datei `org.eclipse.microprofile.config.spi.ConfigSource` im Verzeichnis `src/main/resources/META-INF/services` anzugeben.
Für die von uns definierte Datei hat die Konfiguration den folgenden Inhalt:

```
eu.nighttrains.configuration.redis.RedisConfigurationSource
```

## Ergebnisse

Wie bereits erwähnt, wird sowohl die URL des Timetable-REST-Endpunkts im Booking-Service als auch die gesamte Konfiguration des Quarkus-basierten Timetable-Services aus einer Redis-KV-Datenbank geladen.
Leider bietet MicroProfile Config nicht sehr viele visuelle Möglichkeiten, es ist aber eine wichtige Basistechnologie für andere MicroProfile-APIs.

Interessant ist noch die Ausführung des Kommandos `CLIENT LIST` auf der Redis CLI, nachdem die Server gestartet wurden.
Wie in der nachfolgenden Ausgabe ersichtlich, 

```
127.0.0.1:6379> CLIENT LIST
id=3 addr=127.0.0.1:35536 fd=8 name= age=37 idle=7 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=0 obl=0 oll=0 omem=0 events=r cmd=ping
id=4 addr=127.0.0.1:35542 fd=9 name= age=36 idle=6 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=0 obl=0 oll=0 omem=0 events=r cmd=ping
id=5 addr=127.0.0.1:35546 fd=10 name= age=34 idle=4 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=0 obl=0 oll=0 omem=0 events=r cmd=ping
id=7 addr=127.0.0.1:35586 fd=12 name= age=22 idle=19 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=0 obl=0 oll=0 omem=0 events=r cmd=hget
id=8 addr=127.0.0.1:35598 fd=11 name= age=2 idle=0 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=26 qbuf-free=32742 obl=0 oll=0 omem=0 events=r cmd=client
```

Die Clients mit den IDs 3 bis 5 sind jene Clients des Timetable-Services, während der Client mit der ID 7 jener Client des Booking-Services ist.
Die höhere Anzahl an Redis-Clients des Timetable-Services erklärt sich dadurch, dass wesentlich mehr Teile der Konfiguration des Timetable-Services aus dem Redis-Server geladen werden, während für den Booking-Service nur die Ordinalnummer der Priorität der Konfigurationsquelle sowie die URL des Timetable-Services aus Redis geladen werden.
Der Client mit der ID 8 ist lediglich der CLI-basierte Redis-Client.

# MicroProfile RestClient

## Beschreibung

Der MicroProfile Rest Client vereinfacht das Erstellen von REST-Clients, indem RESTful-Services typsicher über HTTP aufgerufen werden können ([MicroProfile-WhitePaper](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&ved=2ahUKEwi0-dqIn97oAhXtwMQBHXaRDiEQFjAAegQIBxAB&url=https%3A%2F%2Fmicroprofile.io%2Fdownload%2F6339%2F&usg=AOvVaw1ltq2WmqqPUhOWaji-TxT7)). Der Rest Client setzt dabei auf die JAX-RS 2.1 APIs auf. MicroProfile Rest Clients ermöglichen es dem Entwickler normale Java Interfaces zu verwenden, um einen RESTful Service aufzurufen. Über das Interface wird mittels Annotationen das Mapping zwischen der Methode und dem dazugehörigen REST Aufruf festgelegt. Das Erzeugen eines Proxys, welcher das Interface implementiert, kann implizit mittels CDI, oder explizit durch den `RestClientBuilder` erfolgen ([microprofile-rest-client](https://github.com/eclipse/microprofile-rest-client)).


## Verwendung in Services
Das Booking-Service verwendet MicroProfile Rest Client für die Kommunikation mit dem Timetable-Service. Es müssen Verbindungen zwischen dem Start- und dem Zielbahnhof geladen werden sowie Informationen zu den Bahnhöfen, den Zügen und deren Waggons.

## Implementierung

### Interface Definition
Mittels der Annotation `@RegisterRestClient` kann ein Proxy, welcher dieses Interface implementiert, über CDI verwendet werden. Weiters kann mittels dieser Annotation die Basis-URL für den REST Aufruf angegeben werden, wie nachfolgend dargestellt.
```java
@RegisterRestClient(configKey = "timetableService",
        baseUri = "http://localhost:8082/")
```
In unserer Implementierung wird jedoch nur der `configKey` gesetzt. Dabei wird die zuvor beschriebene MicroProfile Config verwendet. Anhand dieses Keys laden wir die benötigte Basis-URL aus unseren Konfigurationsquellen und können demnach dieselben Konfigurationen bei all unseren Rest Clients verwenden. Dadurch können Code Duplizierung sowie ein vermehrter Änderungsaufwand, wenn sich beispielsweise die `baseUri` ändern sollte, vermieden werden. Konkret wird der nachfolgende Wert aus der Config geladen.
```
timetableService/mp-rest/uri "http://localhost:8082"
```

Der nachfolgende Code stellt die Implementierung des Microprofile Rest Client für den Endpunkt `/trainConnection` des Timetable-Service dar. 
```java
@RegisterRestClient(configKey = "timetableService")
@Path("/trainConnection")
public interface TrainConnectionClient extends AutoCloseable {
    @GET
    @Path("/")
    List<TrainConnection> findAllTrainConnections()
            throws UnknownUriException, ProcessingException;

    @GET
    @Path("/{id}")
    TrainConnection findTrainConnectionById(@PathParam("id") long id)
            throws UnknownUriException, ProcessingException;

    @GET
    @Path("/code/{code}")
    TrainConnection findTrainConnectionByCode(@PathParam("code") String code)
            throws UnknownUriException, ProcessingException;

    @GET
    @Path("/{id}/cars")
    List<TrainCar> findAllTrainCarsById(@PathParam("id") long id)
            throws UnknownUriException, ProcessingException;

    @GET
    @Path("/code/{code}/cars")
    List<TrainCar> findAllTrainCarsByCode(@PathParam("code") String code)
            throws UnknownUriException, ProcessingException;
}
```

### Verwendung mittels CDI
Bei der Verwendung des Rest Client haben wir uns dazu entschieden, den Rest Client aus Architektursicht wie ein Dao zu verwenden. Demnach wird der Rest Client mittels CDI einer Manager Klasse injeziert, welche als Wrapper dient und eine weitere Abstraktionsstufe bildet. Injeziert wird der Rest Client in den `TrainConnectionManager` durch die Angabe der CDI Annotationen `@Inject` und `@RestClient`.

```java
@RequestScoped
public class TrainConnectionManagerCdi implements TrainConnectionManager {
    @Inject
    @RestClient
    private TrainConnectionClient trainConnectionClient;
    @Inject @LoggerQualifier(type = LoggerType.CONSOLE)
    private Logger logger;

    @Override
    public List<TrainConnection> findAllTrainConnections(){
        try {
            return trainConnectionClient.findAllTrainConnections();
        } catch(Exception ex) {
            logger.info(ex.getMessage());
            return new ArrayList<>();
        }
    }

    // ...
}
```

## Ergebnisse

Ergebnis der Verwendung von MicroProfile Rest Client ist die erfolgreiche Kommunikation des Booking-Service mit dem Timetable-Service sowie das erfolgreiche auslagern und laden der `baseUri` in eine Konfigurations-Quelle.

# MicroProfile OpenAPI

## Beschreibung

OpenAPI ist der Nachfolgerstandard zu Swagger und ermöglicht es via standardisierten Beschreibungsdateien REST-APIs zu dokumentieren und automatisch Clients oder Server-Stubs zu generieren.
Die MicroProfile OpenAPI-Spezifikation stellt eine Menge von Interfaces und Annotationen bereit, die es auf einfache Art und Weise ermöglichen, OpenAPI-v3-Dokumente basierend auf JAX-RS-Endpunkten zu erstellen.
Zusätzlich kann über Filter die OpenAPI-Ausgabe noch verändert werden und es können auch statische Dokumente im OpenAPI-Format eingebunden werden.

## Verwendung in Services

Sowohl der Timetable- als auch der Booking-Service verwenden die in MicroProfile OpenAPI definierten Annotationen, um die JAX-RS-Endpunkte zu dokumentieren und somit die automatische Generierung von Clients zu ermöglichen.
Zusätzlich wird die JAX-RS-Applikation noch mit Dokumentation über den Service (Verantwortlicher, Lizenz) erweitert.

## Implementierung

### Serverseitige OpenAPI-Dokument-Generierung

Nach Einbindung der MicroProfile OpenAPI-Depdendency steht automatisch der Endpunkt `/openapi` zur Verfügung, welcher ein OpenAPI-v3-Dokument im YAML-Format generiert.
Quarkus stellt zusätzlich unter dem Pfad `/swagger-ui` eine grafische Oberfläche zum Inspizieren und Ausprobieren der REST-Endpunkte zur Verfügung.

Allerdings fehlen diesen Endpunkten standardmäßig noch wichtige Eigenschaften, wie zusätzliche HTTP-Status-Codes im Fehlerfall, Tags, Beschreibungen der Parameter und Beschreibungen der Responses.
Diese müssen nun mittels der MicroProfile OpenAPI-Annotationen zu den JAX-RS-Endpunkten hinzugefügt werden.

Als Beispiel sei der Endpunkt zum Ermitteln der Verbindung zwischen zwei Bahnhöfen gegeben.
Für diese werden im unten folgenden Beispiel für Parameter eine Beschreibung sowie eine Angabe, ob diese erforderlich sind, festgelegt.
Zusätzlich werden die möglichen HTTP-Response-Status-Codes des Endpunktes festgelegt.
Falls eine Route gefunden wird, wird der HTTP-Status-Code 200 zurückgeliefert und falls keine Route gefunden wird, der HTTP-Status-Code 404.
Als Ergebnis des API-Aufrufs wird für den Erfolgsfall (HTTP-Status-Code 200) der Rückgabewert der Handler-Methode angenommen und daher muss dieser nicht mehr eingestellt werden.
Aufgrund der Annotation `@Produces(MediaType.APPLICATION_JSON)` wird von MicroProfile OpenAPI festgelegt, dass dieser Endpunkt den Response-Body als JSON serialisiert.
Nur für den Fall, dass keine Route gefunden wird, muss festgelegt werden, dass der Response als Plain-Text geliefert wird.
Der große Vorteil der Verwendung einer statisch typisierten Programmiersprache im Gegensatz zu einer dynamisch typisierten Programmiersprache ist, dass die Datenstrukturen der Parameter und Rückgabewerte automatisch von MicroProfile OpenAPI ermittelt werden und diese nicht manuell mittels eines Dokumentes eingetragen werden müssen, wie es z.B. in der [OpenAPI-Implementierung für Pythons Flask-Framework](https://github.com/flasgger/flasgger) der Fall ist.

```java
	@Path("/from/{originId}/to/{destinationId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Parameters({
            @Parameter(name = "originId", description = "Origin railway station ID", required = true, example = "0"),
            @Parameter(name = "destinationId", description = "Destination railway station ID", required = true, example = "14")
    })
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    name = "Stops including train connection information between the stations",
                    description = "A list of all stops including their train connection information between the origin station and the destination station."
            ),
            @APIResponse(
                    responseCode = "404",
                    name = "No route between the railway stations",
                    description = "There is no route connecting the two railway stations. This might be because there is no connection between the stations or at least one of the stations does not exist.",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            )
    })
    @Tag(ref = TimetableApiApplication.OPEN_API_TAG_NAME_TIMETABLE)
    public List<RailwayStationConnectionDto> findAllStopsBetween(
            @PathParam("originId") Long originId,
            @PathParam("destinationId") Long destinationId
    ) {
        try {
            return this.routeManager.findAllStopsBetween(originId, destinationId);
        } catch (NoRouteException exception) {
            throw new NotFoundException(exception);
        }
    }
```

Wird für einen JAX-RS-Endpunkt-Handler anstelle des konkreten Datentyps ein Objekt der Klasse `Response` zurückgeliefert, kann der Datentyp von MicroProfile OpenAPI nicht automatisch inferiert und in das OpenAPI-Dokument eingetragen werden.
Dies kann aber auf simple Art und Weise gelöst werden, indem für den Annotationsparameter `content` der Annotation `@APIResponse` das entsprechende Schema festgelegt wird, wie das unten zu sehende Beispiel zeigt.
Selbiges gilt ebenfalls für den Annotationsparameter `content` der Annotation `@RequestBody`.

```java
	@POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestBody(
            name = "Booking request object",
            description = "Contains all details about the booking request",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = BookingRequestDto2.class)
            )
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    name = "Booking created",
                    description = "Books all stops between origin and destination station",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = BookingResponseDto.class)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    name = "The booking could not be completed",
                    description = "The booking could not be completed",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            )
    })
    @Tag(ref = BookingApplication.OPEN_API_TAG_NAME_BOOKING)
    public Response postBooking(@Valid BookingRequestDto2 bookingRequest) {
    	// ...
    }
```

Interessant ist es noch allgemeine Informationen zum REST-Service (verantwortliche Person, Lizenz, Version) hinzuzufügen.
Hierzu ist über Klasse, welche von `javax.ws.rs.core.Application` ableitet, die Annotation `@OpenAPIDefinition` mit den entsprechenden Parametern anzugeben, wie unten zu sehen ist.

Besonders interessant sind dabei die Definition des Servers und der Tags.
Tags dienen dazu, die Funktionen des APIs zu gruppieren und führen dazu, dass der Tag-Name anstatt des Standardnamens bei der Generierung des Clients verwendet wird.
Im unteren Beispiel wird ein Tag für den Timetable-Service festgelegt, welcher in den vorherigen Beispielen von den Handler-Methoden mittels der Annotation `@Tag(ref = TimetableApiApplication.OPEN_API_TAG_NAME_TIMETABLE)` referenziert wird.

Die Festlegung des Servers war nur für den Quarkus-Servers erforderlich, da ansonsten der Standardwert in das OpenAPI-Dokument eingetragen wird.
Für den Wildfly-Server wird dieser aus der Listen-Address des Servers ermittelt und muss daher nicht extra angegeben werden.
Wir haben versucht, die Informationen für das Server-Tag zur Laufzeit mittels einer Implementierung des Interfaces `OASFilter` und des MicroProfile Config-APIs zu ermitteln, was aber nicht funktioniert hat, da das Dokument noch vor Auslesen der Werte aus der Konfiguration erstellt wird.
Daher sahen wir uns leider gezwungen, den Wert hartkodiert zu hinterlegen.

```java
@OpenAPIDefinition(
        info = @Info(
                title = "Timetable API",
                version = "0.0.0",
                contact = @Contact(
                        name = "Lukas Schoerghuber",
                        email = "lukas.schoerghuber@posteo.at"
                ),
                license = @License(
                        name = "GPLv3",
                        url = "https://www.gnu.org/licenses/gpl-3.0.en.html"
                )
        ),
        servers = @Server(url = "http://127.0.0.1:8082", description = "Timetable API development server"),
        tags = @Tag(name = TimetableApiApplication.OPEN_API_TAG_NAME_TIMETABLE, description = "Timetable-related endpoints")
)
@ApplicationPath("/")
public class TimetableApiApplication extends Application {
    public static final String OPEN_API_TAG_NAME_TIMETABLE = "timetable";
}
```

### Generierung des Clients

Noch interessanter als eine gute Dokumentation einer REST-Schnittstelle ist die Möglichkeit, Clients für das API automatisch zu generieren.
Nach einigen Experimenten mit [Swagger-Codegen](https://github.com/swagger-api/swagger-codegen) und [OpenAPI-Generator](https://github.com/OpenAPITools/openapi-generator) haben wir uns für OpenAPI-Generator entschieden, da dieser auch TypeScript-Fetch-Clients für OpenAPI-Dokumente in der Version 3 generieren kann und damit besser mit unserem React-Frontend kombinierbar ist.

Da der OpenAPI-Generator in Java implementiert ist und als Maven-Paket bereitstellt, bietet sich die Möglichkeit im Zuge des Maven-Builds die Clients für das React-Frontend zu generieren.
Um dies zu bewerkstelligen, ist zuerst ein Dependency-Eintrag zum OpenAPI-Generator in die Datei `pom.xml` hinzuzufügen, welcher unten zu sehen ist.

```xml
<dependencies>
	<!-- ... -->
	<dependency>
        <groupId>org.openapitools</groupId>
        <artifactId>openapi-generator-maven-plugin</artifactId>
        <version>4.2.0</version>
    </dependency>
</dependencies>
```

Anschließend ist das OpenAPI-Generator-Plug-In in den Buildvorgang einzuhängen, wodurch bei jeder Ausführung des Maven-Targets `compile` die OpenAPI-Spezifikation vom Server geladen und der Client generiert wird.
Hierzu empfiehlt es sich die Clients so zu generieren, dass der Quarkus-Server im Entwicklungsmodus gestartet wird und anschließend das Kommando `mvn clean compile` ausgeführt wird.

```xml
<build>
    <plugins>
    	<plugin>
            <groupId>org.openapitools</groupId>
            <artifactId>openapi-generator-maven-plugin</artifactId>
            <version>4.2.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                    <configuration>
                        <verbose>true</verbose>
                        <inputSpec>http://127.0.0.1:8082/openapi</inputSpec>
                        <generatorName>typescript-fetch</generatorName>
                        <output>${project.build.directory}/generated-sources/openapi/typescriptFetch</output>
                        <configOptions>
                            <supportsES6>true</supportsES6>
                            <typescriptThreePlus>true</typescriptThreePlus>
                        </configOptions>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## Ergebnisse

Die unten zu sehende Abbildung zeigt eine Zusammenfassung des Swagger-UI-Frontends, welches auf dem Quarkus-basierten Timetable-Service läuft.
Dabei haben wir die Auswirkungen einiger Annotationen im Bild festgehalten.

![Übersicht über Swagger UI](doc/img/swaggerUIOverview.png)

Als Nächstes möchten wir auf die Auswirkungen der Annotationen auf die JAX-RS-Handler eingehen.
Zur Illustration dient dazu die unten zu sehende Abbildung.
Wie zu sehen ist, ist für Parameter neben Beschreibung und Beispielwert angegeben, ob diese unbedingt erforderlich sind.
Ebenso sind die möglichen Response-Codes des APIs mitsamt Beschreibung, Beispielwert und Media-Type aufgeführt.

![Detaillierte Ansicht der Informationen zu einem REST-Endpunkt](doc/img/openAPIEndpointDefinition.png)

Natürlich kann das API auch direkt von Swagger UI ausprobiert werden, wie die folgende Abbildung zeigt, welche das Ergebnis der Abfrage einer Zugverbindung von Rom (Bahnhofs-ID 28) nach Zürich (Bahnhofs-ID 14) zeigt.

![Ergebnis des Ausprobierens eines REST-Endpunktes von Swagger UI](doc/img/openAPIEndpointTryOut.png)

Ein weiteres Artefakt der Verwendung von MicroProfile OpenAPI ist der Client-Code für das React-basierte Frontend.
Das folgende Snippet zeigt den Aufruf der automatisch generierten Client-Methode zum Buchen eines Tickets.
Da es sich um TypeScript-Code handelt ist zur Compilezeit eine Typüberprüfung für sowohl den Body-Parameter als auch den Rückgabewert möglich, wodurch Fehler im Frontend-Code vermieden werden können.
Einzig der automatisch generierte Name (`apiBookingsPost`) der Methode ist unglücklich vom Client-Generator gewählt.
Hierfür bietet sich die Möglichkeit mittels der Annotation `@OperationId` oder über die Generatorkonfiguration einen besseren Namen auszuwählen.

```typescript
const bookingResponse = await this.props.bookingApi.apiBookingsPost({
	bookingRequestDto2: {
		originId: this.props.departureStationId,
		destinationId: this.props.arrivalStationId,
		trainCarType: this.state.trainCarType,
		journeyStartDate: this.state.departureDay
	}
});
```

# MicroProfile OpenTracing

## Beschreibung

Beim Einsatz von Microservice-Architekturen stellt sich das Problem des Debuggins und Troubleshootings über mehrerer Services verteilter Anwendungen.
OpenTracing stellt dabei ein standardisiertes, plattformunabhängiges API zur Instrumentarisierung von Microservices zur Verfügung.
Dabei können zusammenhängende Requests zwischen den Services über eine eindeutige Request-ID mittels grafischer Tools wie [Zipkin](https://zipkin.io/) oder [Jaeger](https://www.jaegertracing.io/) nachverfolgt werden.

MicroProfile OpenTracing ermöglicht es, Anwendungen ohne explizites Hinzufügen von Code um verteiltes Tracing zu erweitern.
Dabei werden Backends, welche das OpenTracing-Protokoll unterstützen, wie z.B. Jaeger unterstützt.

## Verwendung in Services

Bei Verwendung von MicroProfile OpenTracing werden laut Standard standardmäßig alle JAX-RS-Handler und MicroProfile Rest Client-Aufrufe um Tracing erweitert.
Wir haben darüber hinaus in beiden Services die Geschäftslogik und im Timetable-Service mittels einer Extension von Quarkus die JDBC-Aufrufe um Tracing erweitert.

## Implementierung

Der schwierigste Teil ist die Konfiguration des OpenTracing-Client.
Für Quarkus ist die [Dokumentation](https://quarkus.io/guides/opentracing#create-the-configuration) sehr gut und da man diese praktisch unverändert übernehmen kann, verweisen wir auf diese.
Für Wildfly beschreibt der [folgende Annoncement-Post](https://wildfly.org/news/2020/03/19/Micro_Profile_OpenTracing_Comes_To_WildFly/) die Verbindung zum Jaeger-Backend sehr gut.
Die Konfiguration für unsere Services ist ebenfalls im später folgenden Konfigurations-Abschnitt wiedergegeben.
Mit diesen Einstellungen werden bereits die JAX-RS-Endpoint-Aufrufe sowie die MicroProfile Rest Client-Aufrufe über Servicegrenzen getraced.

Ebenso kann mittels Annotationen festgelegt werden, dass Aufrufe einzelner Methoden oder aller Methoden einer Klasse getraced werden sollen.
Hierzu ist über die Klasse oder Metohde einfach die Annotation `@Traced` einzufügen.
Unsere Implementierung verwendet dies, um alle Aufrufe der Methoden der Geschäftslogikschicht zu verfolgen.
Unten folgt noch ein Beispiel eines solchen Tracing-Aufrufes.

```java
@RequestScoped
@Transactional
@Traced
public class RouteManagerCdi implements RouteManager {
	// ...
}
```

Um JDBC-Aufrufe nachverfolgen zu können, bietet Quarkus noch eine Erweiterung an.
Der erste Schritt zur Aktivierung dieser ist es, die Dependency zum entsprechenden JAR-File in die Datei `pom.xml` des Projektes einzutragen, wie das folgende Beispiel zeigt.

```xml
<dependencies>
	<dependency>
        <groupId>io.opentracing.contrib</groupId>
        <artifactId>opentracing-jdbc</artifactId>
    </dependency>
</dependencies>
```

Der JDBC-Tracer wrappt den JDBC-Treiber und augmentiert die Aufrufe um Tracing.
Daher müssen noch die Verbindungseinstellungen zur Datenbank überarbeitet werden:

1. Die URL des JDBC-Treibers muss um `:tracing` erweitert werden, sodass der Tracing-JDBC-Treiber aktiviert wird. Das folgende Beispiel zeigt die URL für die Timetable-Datenbank: `jdbc:tracing:postgresql://127.0.0.1:5432/timetable`
2. Der Tracing-Treiber muss explizit als JDBC-Treiber verwendet werden. Hierzu muss der Wert `io.opentracing.contrib.jdbc.TracingDriver` für den Schlüssel `quarkus.datasource.jdbc.driver` gesetzt werden.
3. Da nun der JDBC-Tracing-Treiber verwendet wird, muss der Dialekt für JPA noch gesetzt werden. Um diesen für PostgreSQL zu konfigurieren, ist für der Wert `org.hibernate.dialect.PostgreSQLDialect` für den Schlüssel `quarkus.hibernate-orm.dialect` zu setzen.

Das Jaeger-Tracing-Backend kann zu Entwicklungszwecken einfach mittels eines Docker-Containers betrieben werden, wozu das unten zu sehende Kommando auszuführen ist.

```bash
docker run -d --name jaeger -e COLLECTOR_ZIPKIN_HTTP_PORT=9411 -p 5775:5775/udp -p 6831:6831/udp -p 6832:6832/udp -p 5778:5778 -p 16686:16686 -p 14268:14268 -p 14250:14250 -p 9411:9411 --rm jaegertracing/all-in-one:1.17
```

## Ergebnisse

Von nun an sollten die Aufrufe der REST-Endpunkte, der Geschäftslogik und die JDBC-Aufrufe des Timetable-Service über die Jaeger-Web-UI nachverfolgbar sein.

In der Jaeger-UI sollten nun Traces für den Service angezeigt werden, wie die folgende Abbildung zeigt.

![Überblick aller Traces für den Booking-Service](doc/img/jaegerUiOverview.png)

Nun kann man beispielsweise den oben zu sehenden Trace für die Methode `postBooking` auswählen und nun ergibt sich, wie in der folgenden Abbildung zu sehen ist, ein Bild über alle Methodenaufrufe, die bei der Abarbeitung des Requests ausgeführt werden.
Dies funktioniert auch über Servicegrenzen hinweg: So ist der Booking-Service blau eingefärbt, während der Timetable-Service gelb eingefärbt ist.
Ebenfalls zu sehen ist, dass die JAX-RS-Endpunkt-Aufrufe, die Geschäftslogikaufrufe und die MicroProfile Rest Client-Aufrufe des Booking-Service verfolgt werden.
Für den Timetable-Service werden ebenfalls die JAX-RS-Endpunkt-Aufrufe und die Geschäftslogikaufrufe sowie zusätzlich die JDBC-Aufrufe nachverfolgt.

![Traces, die sich bei der Buchung einer Fahrkarte ergeben](doc/img/jaegerUiTraceBooking.png)

# Frontend

Da das Frontend nicht Teil der Übungsaufgabe war, haben wir dies in dieser Dokumentation bisher kaum erwähnt.
Jedoch bietet es eine sehr gute Möglichkeit die funktionalen Ergebnisse unserer Ausarbeitung darzustellen.
Daher werden wir hier anhand des Frontend einen Überblick über die Funktionalität unserer Anwendung geben.

Unsere Anwendung verwendet das Package `react-router-dom` zur Strukturierung der einzelnen Komponenten sowie um die Browser-History komplett unterstützen zu können.
Die Startseite, welche unten zu sehen ist, bietet eine einfache Möglichkeit Verbindungen zwischen zwei Bahnhöfen zu suchen.
Wie ebenfalls in der Abbildung zu sehen ist, unterstützt die Anwendung ein einfaches Autocomplete für die Auswahl der Bahnhöfe.

![Suchen einer Verbindung zwischen zwei Bahnhöfen](doc/img/frontend/connectionSearch.png)

Nach dem Klick auf den Suchen-Button wird nach einer Verbindung gesucht und sofern eine gefunden wird, wird diese dem Kunden angezeigt, wie die unten zu sehende Abbildung verdeutlicht.

![Anzeige einer Verbidndung zwischen zwei Bahnhöfen](doc/img/frontend/connectionDisplayCollapsed.png)

Durch Klick auf den Link-Button zum Anzeigen der Zwischenhalte, wird die Tabelle mit den Zwischenhalten expandiert, wie unten zu sehen ist.

![Anzeige einer Verbidndung zwischen zwei Bahnhöfen](doc/img/frontend/connectionDisplayExpanded.png)

Alle Abfragen bisher sind vom Timetable-Service beantwortet worden.
Nach Selektion von Abfahrtsdatum und Wagenkategorie kann auf den Buchen-Button geklickt werden, was zur Folge hat, dass eine Anfrage an den Booking-Service gesendet.
Dieser versucht nun die Wagenkategorie für die gesamte Strecke zu buchen, wobei auch Umstiege berücksichtig werden.
Ein Umstieg wurde so modelliert, dass pro Umstieg ein zusätzliches Ticket erforderlich ist.
Auf der Strecke von Rom über Wien nach Berlin ist also ein Ticket von Rom nach Wien und ein Ticket von Wien nach Berlin erforderlich.

Nachdem die Buchung erfolgreich durchgeführt wurde, erfolgt eine Weiterleitung auf die Komponente zum Anzeigen der Buchung, wobei ein Beispiel unten zu sehen ist.
Für jede Buchung werden Abfahrts- und Zielbahnhof, Abfahrtsdatum und die einzelnen Tickets angezeigt.
Beim Klick auf ein Ticket werden Wagenkategorie, Wagennummer und alle Halte und Abfahrts- sowie Ankunftszeiten dargestellt.

![Anzeige der Tickets für eine Buchung von Rom nach Berlin](doc/img/frontend/bookingDisplay.png)

Abschließend zeigt die unten zu sehende Abbildung die Suche für den Entdeckungsmodus.
Hierfür muss lediglich ein Abfahrtsbahnhof eingegeben werden und nach Klick auf den Suchen-Button werden, wie in der zweiten folgenden Abbildung alle von diesem Bahnhof erreichbaren Ziele angezeigt.
Durch Klick auf den Link zum Finden einer Verbindung, wird auf die bereits bekannte Verbindungsanzeige weitergeleitet.

![Suchen nach allen vom Bahnhof Amstetten erreichbaren Destinationen](doc/img/frontend/discoverySearch.png)
![Anzeige aller vom Bahnhof Amstetten erreichbaren Ziele](doc/img/frontend/discoveryDisplay.png)

# Setup

## Docker-Container

### PostgreSQL

```
docker run --name postgres-docker -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
```

### Jaeger

```
docker run -d --name jaeger -p 6831:6831/udp -p 5778:5778 -p 14268:14268 -p 16686:16686 --rm jaegertracing/all-in-one:1.16
```


### Redis

```
docker run --name redis-sve2 -p 6379:6379 -d redis
docker exec -it redis-sve2 redis-cli
```

## Timetable-Service

### Initialisierung der Konfigurations-Datenbank (REDIS)

Um die Konfigurationsdatenbank mit den für den Timetable-Service benötigten Konfigurationen zu initialisieren müssen folgende Kommandos auf der Redis CLI ausgeführt werden: 

```
SET timetable:ordinal 600
HMSET timetable quarkus.http.port 8082 quarkus.http.cors "true" quarkus.http.cors.origins "http://localhost:3000,http://127.0.0.1:3000"
HMSET timetable openapi.server.url "http://127.0.0.1:8082"
HMSET timetable quarkus.datasource.db-kind "postgresql" quarkus.datasource.username "lukas" quarkus.datasource.password "Cisco0" quarkus.datasource.jdbc.url "jdbc:tracing:postgresql://127.0.0.1:5432/timetable" quarkus.datasource.jdbc.driver "io.opentracing.contrib.jdbc.TracingDriver" quarkus.hibernate-orm.dialect "org.hibernate.dialect.PostgreSQLDialect" quarkus.hibernate-orm.database.generation "drop-and-create"
HMSET timetable quarkus.jaeger.service-name "timetable" quarkus.jaeger.sampler-type "const" quarkus.jaeger.sampler-param 1 quarkus.log.console.format "%d{HH:mm:ss} %-5p traceId=%X{traceId}, spanId=%X{spanId}, sampled=%X{sampled} [%c{2.}] (%t) %s%e%n"
```

### Initialisierung der Konfigurationsdatei zum Zugriff auf die Konfigurations-Datenbank

Der Inhalt der Datei zur Konfiguration der Verbindung zu Redis, welche sich im Ordner `src/main/resources` befindet und den Namen `redisConfiguration.json` haben muss, ist unten zu sehen:

```json
{
  "hostname": "127.0.0.1",
  "port": 6379,
  "timeout": 3000,
  "maximumPoolSize": 5,

  "prefix": "timetable"
}
```

## Booking-Service

### Starten von Wildfly

Wildfly muss mit der Konfigurationsdatei `standalone-microprofile.xml` gestartet werden, um die Features des Eclipse MicroProfiles zu aktivieren.
Wir haben dies über die JVM-Option `-Djboss.server.default.config=standalone-microprofile.xml` realisiert.

### Initialisierung der Konfigurations-Datenbank (REDIS)

Um die Konfigurationsdatenbank mit den für den Booking-Service benötigten Konfigurationen zu initialisieren müssen folgende Kommandos auf der Redis CLI ausgeführt werden: 

```
SET booking:ordinal 600
HMSET booking timetableService/mp-rest/uri "http://localhost:8082"
```

### Initialisierung der Konfigurationsdatei zum Zugriff auf die Konfigurations-Datenbank

Der Inhalt der Datei zur Konfiguration der Verbindung zu Redis, welche sich im Ordner `src/main/resources` befindet und den Namen `redisConfiguration.json` haben muss, ist im folgenden Snippet zu sehen:

```json
{
  "hostname": "127.0.0.1",
  "port": 6379,
  "timeout": 3000,
  "maximumPoolSize": 5,

  "prefix": "booking"
}
```

### Wildfly Jaeger Opentracing
```
# wildfly jaeger configuration
$bin = Join-Path $([Environment]::GetEnvironmentVariable("JBOSS_HOME", "User")) "bin"

# using udp: define an outbound socket binding towards the Jaeger tracer.
$cmd1='/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=jaeger:add(host=localhost, port=6831)'
& $bin\jboss-cli.ps1 --connect --command="$cmd1"

# define MPOT tracer configuration
$cmd2='/subsystem=microprofile-opentracing-smallrye/jaeger-tracer=jaeger-demo:add(sampler-type=const, sampler-param=1, reporter-log-spans=true, sender-binding=jaeger)'
& $bin\jboss-cli.ps1 --connect --command="$cmd2"

#setting the default tracer
$cmd3='/subsystem=microprofile-opentracing-smallrye:write-attribute(name=default-tracer, value=jaeger-demo)'
& $bin\jboss-cli.ps1 --connect --command="$cmd3"
```

### Wildfly Benutzer
Einen Benutzer zu Wildfly hinzufügen.
```
$bin = Join-Path $([Environment]::GetEnvironmentVariable("JBOSS_HOME", "User")) "bin"

& $bin\add-user.ps1 -u admin -p admin123!
```

### Wildfly Datenbank-Treiber und DataSource
Datenbank-Treiber für PostgreSql und DataSource zu Wildfly hinzufügen. 
```
$bin = Join-Path $([Environment]::GetEnvironmentVariable("JBOSS_HOME", "User")) "bin"

$cmd1='module add --name=org.postgres --resources=postgresql-42.2.11.jar --dependencies=javax.api,javax.transaction.api'
& $bin\jboss-cli.ps1 --connect --command="$cmd1"

$cmd2='/subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver)'
& $bin\jboss-cli.ps1 --connect --command="$cmd2"

$cmd3='data-source add --jndi-name=java:/PostGreDS --name=PostgrePool --connection-url=jdbc:postgresql://localhost:5432/booking --driver-name=postgres --user-name=postgres --password=postgres'
& $bin\jboss-cli.ps1 --connect --command="$cmd3"
```

## Frontend
```
npm install
```
```
npm start
```
