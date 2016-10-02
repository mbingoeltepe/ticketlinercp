/*
CREATE TABLE VERANSTALTUNG 
(ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL, 
BEZEICHNUNG VARCHAR(30) NOT NULL, 
SUBKATEGORIE VARCHAR(30),
DAUER INTEGER NOT NULL, 
SPRACHETON VARCHAR(30), 
BEWERTUNG VARCHAR, 
HINWEIS VARCHAR, 
INHALT LONGVARCHAR, 
KRITIK VARCHAR, 
SPRACHEUT VARCHAR(30), 
VERSION INTEGER, 
JAHRERSTELLUNG INTEGER, 
FREIGABE VARCHAR(30), 
ABBILDUNG VARCHAR(100), 
KATEGORIE VARCHAR(30) NOT NULL, 
PRIMARY KEY (ID))
*/

INSERT INTO VERANSTALTUNG ("ID", "BEZEICHNUNG", "SUBKATEGORIE", "DAUER", "SPRACHETON", "BEWERTUNG", "HINWEIS", "INHALT", "KRITIK", "SPRACHEUT", "VERSION", "JAHRERSTELLUNG", "FREIGABE", "ABBILDUNG", "KATEGORIE") VALUES 
(NULL, 'Medea', NULL, 150, 'Deutsch', NULL, 'inszeniert von Aribert Reimann', NULL, NULL, NULL, 0, NULL, NULL, NULL, 'Oper');
INSERT INTO VERANSTALTUNG ("ID", "BEZEICHNUNG", "SUBKATEGORIE", "DAUER", "SPRACHETON", "BEWERTUNG", "HINWEIS", "INHALT", "KRITIK", "SPRACHEUT", "VERSION", "JAHRERSTELLUNG", "FREIGABE", "ABBILDUNG", "KATEGORIE") VALUES 
(NULL, 'Die heilige Johanna der Schlachthoefe', NULL, 150, 'Deutsch', NULL, 'Bertholt Brecht', NULL, NULL, NULL, 0, NULL, NULL, NULL, 'Theater');
INSERT INTO VERANSTALTUNG ("ID", "BEZEICHNUNG", "SUBKATEGORIE", "DAUER", "SPRACHETON", "BEWERTUNG", "HINWEIS", "INHALT", "KRITIK", "SPRACHEUT", "VERSION", "JAHRERSTELLUNG", "FREIGABE", "ABBILDUNG", "KATEGORIE") VALUES 
(NULL, 'Rasmus, Pontus und der Schwertschlucker', NULL, 120, 'Deutsch', NULL, 'Astrid Lindgren', NULL, NULL, NULL, 0, NULL, NULL, NULL, 'Theater');
INSERT INTO VERANSTALTUNG ("ID", "BEZEICHNUNG", "SUBKATEGORIE", "DAUER", "SPRACHETON", "BEWERTUNG", "HINWEIS", "INHALT", "KRITIK", "SPRACHEUT", "VERSION", "JAHRERSTELLUNG", "FREIGABE", "ABBILDUNG", "KATEGORIE") VALUES 
(NULL, 'Richard II.', NULL, 180, 'Englisch', NULL, 'William Shakespeare', NULL, NULL, NULL, 0, NULL, NULL, NULL, 'Theater');
INSERT INTO VERANSTALTUNG ("ID", "BEZEICHNUNG", "SUBKATEGORIE", "DAUER", "SPRACHETON", "BEWERTUNG", "HINWEIS", "INHALT", "KRITIK", "SPRACHEUT", "VERSION", "JAHRERSTELLUNG", "FREIGABE", "ABBILDUNG", "KATEGORIE") VALUES 
(NULL, 'Zauberklang und Blechsalat', NULL, 60, '', NULL, 'mund.ART Quintett Wien', NULL, NULL, NULL, 0, NULL, NULL, NULL, 'Konzert');
INSERT INTO VERANSTALTUNG ("ID", "BEZEICHNUNG", "SUBKATEGORIE", "DAUER", "SPRACHETON", "BEWERTUNG", "HINWEIS", "INHALT", "KRITIK", "SPRACHEUT", "VERSION", "JAHRERSTELLUNG", "FREIGABE", "ABBILDUNG", "KATEGORIE") VALUES 
(NULL, 'Waldstein Ensemble', NULL, 135, '', NULL, 'Mozart, Faure, Brahms', NULL, NULL, NULL, 0, NULL, NULL, NULL, 'Konzert');

/*
CREATE TABLE ORT 
(ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL, 
BEZEICHNUNG VARCHAR(50) NOT NULL, 
BESITZER VARCHAR(40), 
TELNR VARCHAR(30), 
VERSION INTEGER, 
OEFFNUNGSZEITEN VARCHAR, 
TYP INTEGER NOT NULL, 
VERKAUFSSTELLE BOOLEAN, 
KIOSK BOOLEAN, 
STRASSE VARCHAR(80) NOT NULL, 
PLZ VARCHAR(5) NOT NULL, 
ORT VARCHAR(50) NOT NULL, 
LAND VARCHAR(40), 
PRIMARY KEY (ID))
*/

INSERT INTO ORT ("ID", "BEZEICHNUNG", "BESITZER", "TELNR", "VERSION", "OEFFNUNGSZEITEN", "TYP", "VERKAUFSSTELLE", "KIOSK", "STRASSE", "PLZ", "ORT", "LAND") VALUES
(NULL, 'Staatsoper Wien', 'Wiener Staatsoper GmbH', NULL, 0, NULL, 4, TRUE, FALSE, 'Opernring 2', '1010', 'Wien', 'Österreich');
INSERT INTO ORT ("ID", "BEZEICHNUNG", "BESITZER", "TELNR", "VERSION", "OEFFNUNGSZEITEN", "TYP", "VERKAUFSSTELLE", "KIOSK", "STRASSE", "PLZ", "ORT", "LAND") VALUES
(NULL, 'Burgtheater', 'Besitzer Burgtheater', NULL, 0, NULL, 3, FALSE, FALSE, 'Dr. Karl Lueger-Ring 2', '1010', 'Wien', 'Österreich');
INSERT INTO ORT ("ID", "BEZEICHNUNG", "BESITZER", "TELNR", "VERSION", "OEFFNUNGSZEITEN", "TYP", "VERKAUFSSTELLE", "KIOSK", "STRASSE", "PLZ", "ORT", "LAND") VALUES
(NULL, 'Akademietheater', 'Besitzer Burgtheater', NULL, 0, NULL, 3, FALSE, FALSE, 'Lisztstraße 1', '1030', 'Wien', 'Österreich');
INSERT INTO ORT ("ID", "BEZEICHNUNG", "BESITZER", "TELNR", "VERSION", "OEFFNUNGSZEITEN", "TYP", "VERKAUFSSTELLE", "KIOSK", "STRASSE", "PLZ", "ORT", "LAND") VALUES
(NULL, 'Musikverein', 'Gesellschaft der Musikfreunde in Wien', NULL, 0, NULL, 6, TRUE, FALSE, 'Bösendorferstraße 1', '1010', 'Wien', 'Österreich');

/*
CREATE TABLE SAAL 
(ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL, 
BEZEICHNUNG VARCHAR(80) NOT NULL, 
KOSTENPROTAG NUMERIC(38), 
VERSION INTEGER, 
ORT_ID INTEGER, 
PRIMARY KEY (ID))
*/

INSERT INTO SAAL ("ID", "BEZEICHNUNG", "KOSTENPROTAG", "VERSION", "ORT_ID") VALUES
(NULL, 'Staatsoper', 3440.12, 0, 1);
INSERT INTO SAAL ("ID", "BEZEICHNUNG", "KOSTENPROTAG", "VERSION", "ORT_ID") VALUES
(NULL, 'Burgtheater', 24988.82, 0, 2);
INSERT INTO SAAL ("ID", "BEZEICHNUNG", "KOSTENPROTAG", "VERSION", "ORT_ID") VALUES
(NULL, 'Akademietheather', 124.56, 0, 3);
INSERT INTO SAAL ("ID", "BEZEICHNUNG", "KOSTENPROTAG", "VERSION", "ORT_ID") VALUES
(NULL, 'Musikverein - Brahms-Saal', 2345.00, 0, 4);
INSERT INTO SAAL ("ID", "BEZEICHNUNG", "KOSTENPROTAG", "VERSION", "ORT_ID") VALUES
(NULL, 'Musikverein - Großer Saal', 285443.05, 0, 4);
INSERT INTO SAAL ("ID", "BEZEICHNUNG", "KOSTENPROTAG", "VERSION", "ORT_ID") VALUES
(NULL, 'Musikverein - Gläsener Saal', 1.00, 0, 4);

/*
CREATE TABLE AUFFUEHRUNG 
(ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL, 
DATUMUHRZEIT TIMESTAMP NOT NULL, 
STORNIERT BOOLEAN, 
PREIS INTEGER NOT NULL, 
HINWEIS VARCHAR, 
VERSION INTEGER, 
VERANSTALTUNG_ID INTEGER, 
SAAL_ID INTEGER, 
PRIMARY KEY (ID))
*/

INSERT INTO AUFFUEHRUNG ("ID", "DATUMUHRZEIT", "STORNIERT", "PREIS", "HINWEIS", "VERSION", "VERANSTALTUNG_ID", "SAAL_ID") VALUES 
(NULL, '2011-01-31 20:00:00', FALSE, 2, 'Premiere', 0, 1, 1);
INSERT INTO AUFFUEHRUNG ("ID", "DATUMUHRZEIT", "STORNIERT", "PREIS", "HINWEIS", "VERSION", "VERANSTALTUNG_ID", "SAAL_ID") VALUES 
(NULL, '2011-01-28 19:30:00', FALSE, 1, 'Hinweis', 0, 2, 2);
INSERT INTO AUFFUEHRUNG ("ID", "DATUMUHRZEIT", "STORNIERT", "PREIS", "HINWEIS", "VERSION", "VERANSTALTUNG_ID", "SAAL_ID") VALUES 
(NULL, '2011-03-12 11:30:00', FALSE, 0, 'Hinweis', 0, 3, 3);
INSERT INTO AUFFUEHRUNG ("ID", "DATUMUHRZEIT", "STORNIERT", "PREIS", "HINWEIS", "VERSION", "VERANSTALTUNG_ID", "SAAL_ID") VALUES 
(NULL, '2011-01-31 17:00:00', FALSE, 1, 'Special', 0, 4, 2);
INSERT INTO AUFFUEHRUNG ("ID", "DATUMUHRZEIT", "STORNIERT", "PREIS", "HINWEIS", "VERSION", "VERANSTALTUNG_ID", "SAAL_ID") VALUES 
(NULL, '2011-04-30 16:30:00', FALSE, 2, 'Hinweis', 0, 5, 4);
INSERT INTO AUFFUEHRUNG ("ID", "DATUMUHRZEIT", "STORNIERT", "PREIS", "HINWEIS", "VERSION", "VERANSTALTUNG_ID", "SAAL_ID") VALUES 
(NULL, '2011-01-21 16:30:00', TRUE, 1, 'Hinweis', 0, 5, 6);
INSERT INTO AUFFUEHRUNG ("ID", "DATUMUHRZEIT", "STORNIERT", "PREIS", "HINWEIS", "VERSION", "VERANSTALTUNG_ID", "SAAL_ID") VALUES 
(NULL, '2011-01-01 19:00:00', TRUE, 0, 'Neujahrskonzert', 0, 6, 4);

/* CREATE TABLE KATEGORIE (ID INTEGER GENERATED BY DEFAULT AS IDENTITY
 * (START WITH 1, INCREMENT BY 1) NOT NULL, BEZEICHNUNG VARCHAR(30) NOT NULL, 
 * PREISMIN NUMERIC(38), 
 * PREISSTD NUMERIC(38), 
 * PREISMAX NUMERIC(38), 
 * VERSION INTEGER, 
 * PRIMARY KEY (ID))
 * */

INSERT INTO KATEGORIE ("ID", "BEZEICHNUNG", "PREISMIN", "PREISSTD", "PREISMAX", "VERSION") VALUES
(NULL, 20, 30, 40, 1, 4);
INSERT INTO KATEGORIE ("ID", "BEZEICHNUNG", "PREISMIN", "PREISSTD", "PREISMAX", "VERSION") VALUES
(NULL, 200, 350, 430, 2, 5);
INSERT INTO KATEGORIE ("ID", "BEZEICHNUNG", "PREISMIN", "PREISSTD", "PREISMAX", "VERSION") VALUES
(NULL, 2, 3, 4, 3, 6);
INSERT INTO KATEGORIE ("ID", "BEZEICHNUNG", "PREISMIN", "PREISSTD", "PREISMAX", "VERSION") VALUES
(NULL, 50, 70, 90, 4, 7);

/* CREATE TABLE REIHE 
 * (ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL, 
 * BEZEICHNUNG VARCHAR, 
 * ANZPLAETZE INTEGER NOT NULL, 
 * REIHENFOLGE INTEGER NOT NULL, 
 * SITZPLATZ BOOLEAN NOT NULL, 
 * STARTPLATZ INTEGER NOT NULL, 
 * VERSION INTEGER, 
 * SAAL_ID INTEGER, 
 * KATEGORIE_ID INTEGER, 
 * PRIMARY KEY (ID))
 * */

INSERT INTO REIHE ("ID", "BEZEICHNUNG", "ANZPLAETZE", "REIHENFOLGE", "SITZPLATZ", "STARTPLATZ", "VERSION", "KATEGORIE_ID", "SAAL_ID") VALUES
(NULL, '1. Reihe', 5, 4, true, 1, 1, 2, 2);
INSERT INTO REIHE ("ID", "BEZEICHNUNG", "ANZPLAETZE", "REIHENFOLGE", "SITZPLATZ", "STARTPLATZ", "VERSION", "KATEGORIE_ID", "SAAL_ID") VALUES
(NULL, '2. Reihe', 5, 4, true, 1, 1, 2, 2);
INSERT INTO REIHE ("ID", "BEZEICHNUNG", "ANZPLAETZE", "REIHENFOLGE", "SITZPLATZ", "STARTPLATZ", "VERSION", "KATEGORIE_ID", "SAAL_ID") VALUES
(NULL, '3. Reihe', 5, 4, true, 1, 1, 2, 2);
INSERT INTO REIHE ("ID", "BEZEICHNUNG", "ANZPLAETZE", "REIHENFOLGE", "SITZPLATZ", "STARTPLATZ", "VERSION", "KATEGORIE_ID", "SAAL_ID") VALUES
(NULL, '4. Reihe', 5, 4, true, 1, 1, 2, 2);
INSERT INTO REIHE ("ID", "BEZEICHNUNG", "ANZPLAETZE", "REIHENFOLGE", "SITZPLATZ", "STARTPLATZ", "VERSION", "KATEGORIE_ID", "SAAL_ID") VALUES
(NULL, '5. Reihe', 5, 4, true, 1, 1, 2, 2);

INSERT INTO REIHE ("ID", "BEZEICHNUNG", "ANZPLAETZE", "REIHENFOLGE", "SITZPLATZ", "STARTPLATZ", "VERSION", "KATEGORIE_ID", "SAAL_ID") VALUES
(NULL, '1. Reihe', 5, 4, true, 1, 1, 1, 1);
INSERT INTO REIHE ("ID", "BEZEICHNUNG", "ANZPLAETZE", "REIHENFOLGE", "SITZPLATZ", "STARTPLATZ", "VERSION", "KATEGORIE_ID", "SAAL_ID") VALUES
(NULL, '2. Reihe', 5, 4, true, 1, 1, 1, 1);
INSERT INTO REIHE ("ID", "BEZEICHNUNG", "ANZPLAETZE", "REIHENFOLGE", "SITZPLATZ", "STARTPLATZ", "VERSION", "KATEGORIE_ID", "SAAL_ID") VALUES
(NULL, '3. Reihe', 5, 4, true, 1, 1, 1, 1);
INSERT INTO REIHE ("ID", "BEZEICHNUNG", "ANZPLAETZE", "REIHENFOLGE", "SITZPLATZ", "STARTPLATZ", "VERSION", "KATEGORIE_ID", "SAAL_ID") VALUES
(NULL, '4. Reihe', 5, 4, true, 1, 1, 1, 1);
INSERT INTO REIHE ("ID", "BEZEICHNUNG", "ANZPLAETZE", "REIHENFOLGE", "SITZPLATZ", "STARTPLATZ", "VERSION", "KATEGORIE_ID", "SAAL_ID") VALUES
(NULL, '5. Reihe', 5, 4, true, 1, 1, 1, 1);

/*
CREATE TABLE PERSON 
(ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL, 
TYP VARCHAR(31), 
NACHNAME VARCHAR(50) NOT NULL, 
VORNAME VARCHAR(30) NOT NULL, 
GEBURTSDATUM DATE, 
TELNR VARCHAR, 
GESCHLECHT INTEGER, 
BLZ VARCHAR, 
VERSION INTEGER, 
USERNAME VARCHAR(50), 
EMAIL VARCHAR, 
TITEL VARCHAR(30), 
KONTONR VARCHAR, 
PASSWORT VARCHAR(20), 
ORT_ID INTEGER, 
STRASSE VARCHAR(80) NOT NULL, 
PLZ VARCHAR(5) NOT NULL, 
ORT VARCHAR(50) NOT NULL, 
LAND VARCHAR(40),
KREDITKARTENNR VARCHAR(16), 
KONTOSTAND NUMERIC(38), 
ERMAESSIGUNG NUMERIC(38), 
VORLIEBEN VARCHAR, 
TICKETCARDNR VARCHAR(20), 
KONTOLIMIT NUMERIC(38), 
TICKETCARDGUELTIGBIS DATE, 
GRUPPE INTEGER, 
GESPERRT BOOLEAN, 
KREDITKARTEGUELTIGBIS DATE, 
KREDITKARTENTYP INTEGER, 
ERMAECHTIGUNG BOOLEAN,
BERECHTIGUNG INTEGER, 
SOZIALVERSICHERUNGSNR VARCHAR(12), 
PRIMARY KEY (ID))
*/

INSERT INTO PERSON ("ID", "TYP", "NACHNAME", "VORNAME", "GEBURTSDATUM", "TELNR", "GESCHLECHT", "BLZ", "VERSION", "USERNAME", "EMAIL", "TITEL", "KONTONR", "PASSWORT", "ORT_ID", "STRASSE", "PLZ", "ORT", "LAND", "KREDITKARTENNR", "KONTOSTAND", "ERMAESSIGUNG", "VORLIEBEN", "TICKETCARDNR", "KONTOLIMIT", "TICKETCARDGUELTIGBIS", "GRUPPE", "GESPERRT", "KREDITKARTEGUELTIGBIS", "KREDITKARTENTYP", "ERMAECHTIGUNG", "BERECHTIGUNG", "SOZIALVERSICHERUNGSNR") VALUES
(NULL, 'M', 'Max', 'Mustermann', '1950-09-01', '+436761234567', 0, NULL, 0, 'max', 'max@mustermann.at', NULL, NULL, 'max', 3, 'Musterstraße 1', '1234', 'Musterstadt', 'Musterland', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, FALSE, NULL, NULL, TRUE, 0, '9876543210');
INSERT INTO PERSON ("ID", "TYP", "NACHNAME", "VORNAME", "GEBURTSDATUM", "TELNR", "GESCHLECHT", "BLZ", "VERSION", "USERNAME", "EMAIL", "TITEL", "KONTONR", "PASSWORT", "ORT_ID", "STRASSE", "PLZ", "ORT", "LAND", "KREDITKARTENNR", "KONTOSTAND", "ERMAESSIGUNG", "VORLIEBEN", "TICKETCARDNR", "KONTOLIMIT", "TICKETCARDGUELTIGBIS", "GRUPPE", "GESPERRT", "KREDITKARTEGUELTIGBIS", "KREDITKARTENTYP", "ERMAECHTIGUNG", "BERECHTIGUNG", "SOZIALVERSICHERUNGSNR") VALUES
(NULL, 'M', 'Sadransky', 'Bernhard', '1984-09-07', '+436645305591', 0, NULL, 0, 'sadi', 'e0627796@student.tuwien.ac.at', NULL, NULL, 'sadi', 1, 'Straße 137', '2042', 'Guntersdorf', 'Österreich', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, FALSE, NULL, NULL, TRUE, 3, '1234070984');
INSERT INTO PERSON ("ID", "TYP", "NACHNAME", "VORNAME", "GEBURTSDATUM", "TELNR", "GESCHLECHT", "BLZ", "VERSION", "USERNAME", "EMAIL", "TITEL", "KONTONR", "PASSWORT", "ORT_ID", "STRASSE", "PLZ", "ORT", "LAND", "KREDITKARTENNR", "KONTOSTAND", "ERMAESSIGUNG", "VORLIEBEN", "TICKETCARDNR", "KONTOLIMIT", "TICKETCARDGUELTIGBIS", "GRUPPE", "GESPERRT", "KREDITKARTEGUELTIGBIS", "KREDITKARTENTYP", "ERMAECHTIGUNG", "BERECHTIGUNG", "SOZIALVERSICHERUNGSNR") VALUES
(NULL, 'K', 'Irgendwas', 'Held', '1984-09-07', '+4369912345323', 0, NULL, 0, NULL, 'irgendwas@held.at', NULL, NULL, NULL, 1, 'Straße 137', '2042', 'Guntersdorf', 'Österreich', '43212321', NULL, NULL, NULL, NULL, NULL, NULL, NULL, FALSE, NULL, NULL, FALSE, 3, '1234070984');
INSERT INTO PERSON ("ID", "TYP", "NACHNAME", "VORNAME", "GEBURTSDATUM", "TELNR", "GESCHLECHT", "BLZ", "VERSION", "USERNAME", "EMAIL", "TITEL", "KONTONR", "PASSWORT", "ORT_ID", "STRASSE", "PLZ", "ORT", "LAND", "KREDITKARTENNR", "KONTOSTAND", "ERMAESSIGUNG", "VORLIEBEN", "TICKETCARDNR", "KONTOLIMIT", "TICKETCARDGUELTIGBIS", "GRUPPE", "GESPERRT", "KREDITKARTEGUELTIGBIS", "KREDITKARTENTYP", "ERMAECHTIGUNG", "BERECHTIGUNG", "SOZIALVERSICHERUNGSNR") VALUES
(NULL, 'K', 'Herbert', 'Mayer', '1988-09-06', '+4369912345343', 0, NULL, 0, NULL, 'mayer@aon.at', NULL, NULL, NULL, 1, 'Straße 138', '2042', 'Guntersdorf', 'Österreich', '43212321', NULL, NULL, NULL, NULL, NULL, NULL, NULL, FALSE, NULL, NULL, FALSE, 3, '1234070985');

INSERT INTO KUENSTLER ("ID", "NACHNAME", "VORNAME", "GEBURTSDATUM", "TITEL", "BIOGRAPHIE", "GESCHLECHT", "VERSION") VALUES
(NULL, 'Muster', 'Thomas', '1975-02-12', 'Dr', '', 0, 1);

INSERT INTO ENGAGEMENT("ID", "GAGE", "FUNKTION", "VERSION", "VERANSTALTUNG_ID", "KUENSTLER_ID") VALUES
(NULL, 15000, '', 1, 4, 1);

/*CREATE TABLE ARTIKEL 
 * (ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL, 
 * PREIS NUMERIC(38) NOT NULL, 
 * BESCHREIBUNG LONGVARCHAR, 
 * ABBILDUNG VARCHAR, 
 * KATEGORIE INTEGER NOT NULL, 
 * KURZBEZEICHNUNG VARCHAR(30) NOT NULL, 
 * VERSION INTEGER, 
 * VERANSTALTUNG_ID INTEGER, 
 * PRIMARY KEY (ID))*/

INSERT INTO ARTIKEL("ID", "PREIS", "BESCHREIBUNG", "ABBILDUNG", "KATEGORIE", "KURZBEZEICHNUNG", "VERSION", "VERANSTALTUNG_ID") VALUES
(NULL, 25, 'Lustiges Spielzeug', NULL, 0, 'LS', 1, 1);

INSERT INTO ARTIKEL("ID", "PREIS", "BESCHREIBUNG", "ABBILDUNG", "KATEGORIE", "KURZBEZEICHNUNG", "VERSION", "VERANSTALTUNG_ID") VALUES
(NULL, 299, 'T-Shirt aus Gold', NULL, 0, 'TSG', 1, 1);

INSERT INTO ARTIKEL("ID", "PREIS", "BESCHREIBUNG", "ABBILDUNG", "KATEGORIE", "KURZBEZEICHNUNG", "VERSION", "VERANSTALTUNG_ID") VALUES
(NULL, 29.99, 'Kaffeebecher', NULL, 0, 'Becher', 1, 2);

INSERT INTO ARTIKEL("ID", "PREIS", "BESCHREIBUNG", "ABBILDUNG", "KATEGORIE", "KURZBEZEICHNUNG", "VERSION", "VERANSTALTUNG_ID") VALUES
(NULL, 80, 'CD des Stücks (Einzelstück)', NULL, 0, 'CD', 1, 2);

INSERT INTO ARTIKEL("ID", "PREIS", "BESCHREIBUNG", "ABBILDUNG", "KATEGORIE", "KURZBEZEICHNUNG", "VERSION", "VERANSTALTUNG_ID") VALUES
(NULL, 10, 'Autogramm des Hauptdarstellers', NULL, 0, 'Autogramm', 1, 3);

/* CREATE TABLE TRANSAKTION 
 * (ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL, 
 * DATUMUHRZEIT TIMESTAMP NOT NULL, 
 * STATUS INTEGER NOT NULL, 
 * ZAHLUNGSART INTEGER NOT NULL, 
 * RESERVIERUNGSNR INTEGER, 
 * VERSION INTEGER, 
 * MITARBEITER_ID INTEGER, 
 * KUNDE_ID INTEGER, 
 * PRIMARY KEY (ID))
 * */

/*INSERT INTO TRANSAKTION ("ID", "DATUMUHRZEIT", "STATUS", "ZAHLUNGSART", "RESERVIERUNGSNR", "VERSION", "KUNDE_ID", "MITARBEITER_ID") VALUES
(NULL, '2011-01-31 20:00:00', 1, 1, 23, 1, 1, 1);
INSERT INTO TRANSAKTION ("ID", "DATUMUHRZEIT", "STATUS", "ZAHLUNGSART", "RESERVIERUNGSNR", "VERSION", "KUNDE_ID", "MITARBEITER_ID") VALUES
(NULL, '2011-01-31 20:00:00', 2, 1, 24, 2, 1, 1);*/

/*
 CREATE TABLE PLATZ 
 (ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL, 
 STATUS INTEGER NOT NULL, 
 NUMMER INTEGER NOT NULL, 
 VERSION INTEGER, 
 REIHE_ID INTEGER, 
 TRANSAKTION_ID INTEGER, 
 AUFFUEHRUNG_ID INTEGER,
 KATEGORIE_ID INTEGER,
 PRIMARY KEY (ID))
 */

/*INSERT INTO PLATZ ("ID", "STATUS", "NUMMER", "VERSION", "REIHE_ID", "TRANSAKTION_ID", "AUFFUEHRUNG_ID", "KATEGORIE_ID") VALUES
(NULL, 1, 1, 23, 1, 1, 2, 2);
INSERT INTO PLATZ ("ID", "STATUS", "NUMMER", "VERSION", "REIHE_ID", "TRANSAKTION_ID", "AUFFUEHRUNG_ID", "KATEGORIE_ID") VALUES
(NULL, 1, 2, 1, 1, 1, 2, 2);
INSERT INTO PLATZ ("ID", "STATUS", "NUMMER", "VERSION", "REIHE_ID", "TRANSAKTION_ID", "AUFFUEHRUNG_ID", "KATEGORIE_ID") VALUES
(NULL, 1, 3, 2, 1, 1, 2, 2);
INSERT INTO PLATZ ("ID", "STATUS", "NUMMER", "VERSION", "REIHE_ID", "TRANSAKTION_ID", "AUFFUEHRUNG_ID", "KATEGORIE_ID") VALUES
(NULL, 1, 4, 3, 1, 1, 2, 2);
INSERT INTO PLATZ ("ID", "STATUS", "NUMMER", "VERSION", "REIHE_ID", "TRANSAKTION_ID", "AUFFUEHRUNG_ID", "KATEGORIE_ID") VALUES
(NULL, 1, 5, 4, 1, 1, 2, 2);
INSERT INTO PLATZ ("ID", "STATUS", "NUMMER", "VERSION", "REIHE_ID", "TRANSAKTION_ID", "AUFFUEHRUNG_ID", "KATEGORIE_ID") VALUES
(NULL, 2, 4, 5, 2, 1, 2, 2);
INSERT INTO PLATZ ("ID", "STATUS", "NUMMER", "VERSION", "REIHE_ID", "TRANSAKTION_ID", "AUFFUEHRUNG_ID", "KATEGORIE_ID") VALUES
(NULL, 0, 3, 6, 2, 1, 2, 2);
INSERT INTO PLATZ ("ID", "STATUS", "NUMMER", "VERSION", "REIHE_ID", "TRANSAKTION_ID", "AUFFUEHRUNG_ID", "KATEGORIE_ID") VALUES
(NULL, 2, 1, 7, 2, 1, 2, 2);*/


COMMIT;