package at.ticketline.dao.interfaces;

import java.util.List;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.Kunde;

public interface KundeDao extends GenericDao<Kunde,Integer> {
    
    /**
     * Sucht Kunden per Nachname und/oder Vorname aus der DB
     * 
     * @param k
     *            - Kunde Entity mit den zu Suchenden DAten
     * @return List<Kunde> - Liste mit den gefundenen Kunden
     */
    public List<Kunde> findByKunde(Kunde query);
    
    /**
     * Aendert Daten des Kunden in der DB
     * @param query Kunde, von welchem Daten geaendert werden soll
     * @return Kunde, von welchem Daten geaendert wurden
     */
    public Kunde changeKundenDaten(Kunde k);
    
    /**
     * Speichert einen neuen Kunden in die DB
     * 
     * @param k
     *            - Kunde Entity das in die DB gespeichert wird
     */

    public void insertKunde(Kunde k);

}
