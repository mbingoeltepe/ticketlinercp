DELETE FROM PLATZ;
DELETE FROM AUFFUEHRUNG;
DELETE FROM ENGAGEMENT;
DELETE FROM REIHE;
DELETE FROM SAAL;
DELETE FROM NEWS;
DELETE FROM TRANSAKTION;
DELETE FROM PERSON;
DELETE FROM ARTIKEL;
DELETE FROM BESTELLUNG;
DELETE FROM BESTELLPOSITION;
DELETE FROM ORT;
DELETE FROM VERANSTALTUNG;
DELETE FROM KATEGORIE;
DELETE FROM KUENSTLER;

ALTER TABLE PLATZ ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE AUFFUEHRUNG ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE ENGAGEMENT ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE SAAL ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE NEWS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE TRANSAKTION ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE PERSON ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE REIHE ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE ARTIKEL ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE BESTELLUNG ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE BESTELLPOSITION ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE ORT ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE VERANSTALTUNG ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE KATEGORIE ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE KUENSTLER ALTER COLUMN ID RESTART WITH 1;
COMMIT;
