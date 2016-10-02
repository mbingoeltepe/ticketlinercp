package at.ticketline.service.interfaces;

import java.util.List;

import at.ticketline.entity.Kunde;

public interface KundeService {

    /**
     * aendert den Kunden in der Datenbank mit den neuen Feldern
     * @param kunde Kunde, welcher geaendert werden soll
     * @return Kunde, der in der DB upgedatet wurde
     */
    public Kunde changeKundenDaten(Kunde kunde);
    
    /**
     * sucht einen Kunden in der DB
     * @param kunde Kunde, welcher gesucht wird
     * @return Eine Liste an Kunden, die bei dem Query in Frage kommt
     */
    public List<Kunde> sucheKunde(Kunde kunde);
    
    /**
     * fuegt einen Kunden zur Datenbank hinzu
     * @param kunde Kunde, welcher hinzugefuegt werden soll
     * @return true, falls er erfolgreich hinzufgefuegt wurde, sonst false
     */
    public void addKundeToDb(Kunde kunde);
    
    /**
     * suche Alle Kunden
     * 
     * @return Alle Kunden
     */
    public List<Kunde> sucheAlleKunden();
    
    /**
     * Kunde Löschen
     */
    public void removeKunde(Kunde kunde);

    
    
}
