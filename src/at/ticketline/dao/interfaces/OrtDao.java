package at.ticketline.dao.interfaces;

import java.util.List;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.Ort;

/**
 * @author MURAT BINGOLTEPE, Andrea Auer
 */

public interface OrtDao extends GenericDao<Ort,Integer> {
    
    /**
     * Sucht Orte per Bezeichnung aus der DB
     * 
     * @param query
     *            - Ort Entity mit den zu Suchenden Daten
     * @return List<Ort> - Liste mit den gefundenen Orten
     */

    public List<Ort> findByOrt(Ort query);
    
    /**
     * Sucht Orte mit Bezeichnung, Adresse und Typ aus der DB
     * @param ort - Entitiy mt den zu suchendne Daten
     * @return List<Ort> - Liste mit den gefundenen Veranstaltungsorten
     */
    public List<Ort> findByVeranstaltungsort(Ort ort);
    
}
