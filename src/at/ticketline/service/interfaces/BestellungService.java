package at.ticketline.service.interfaces;

import java.util.List;

import at.ticketline.entity.BestellPosition;
import at.ticketline.entity.Bestellung;

public interface BestellungService {
    
    /**
     * findet eine bestimmte Bestellung in der DB
     * @param b Bestellung, die gesucht wird
     * @return Alle Bestellungen, die die Suchkriterien erfuellen
     */
    public List<Bestellung> findByBestellung(Bestellung b);
    
    /**
     * findet alle bestehenden Bestellungen in der DB
     * @return Liste mit allen Bestellungen
     */
    public List<Bestellung> findAllBestellungen();
    
    /**
     * fuegt eine neue Bestellung zur DB hinzu
     * @param b Bestellung, welche man hinzufuegen will.
     * @return true, falls erfolgreich hinzugefuegt, sonst false
     */
    public Bestellung addBestellung(Bestellung b);
    
    public BestellPosition addBestellposition(BestellPosition bp);
    
    

}
