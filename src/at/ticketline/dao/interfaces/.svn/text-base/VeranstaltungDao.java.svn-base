package at.ticketline.dao.interfaces;

import java.util.List;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.Veranstaltung;

/**
 * 
 * @author andrea auer
 *
 */

public interface VeranstaltungDao extends GenericDao<Veranstaltung,Integer> {
    
    /**
     * Sucht Veranstaltung per Bezeichnung, Kategorie, Dauer und Inhalt aus der DB 
     * @param v
     *            - Veranstaltung Entity mit den zu Suchenden DAten
     * @return List<Veranstaltung> - Liste mit den gefundenen Veranstaltungen
     */
    public List<Veranstaltung> findByVeranstaltung(Veranstaltung v);
    
    

}
