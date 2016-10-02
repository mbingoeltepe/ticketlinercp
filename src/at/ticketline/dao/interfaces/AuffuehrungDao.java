package at.ticketline.dao.interfaces;

import java.util.List;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.Auffuehrung;

public interface AuffuehrungDao extends GenericDao<Auffuehrung, Integer> {

    /**
     * Sucht Aufführung aus der DB mit den Parametern: Zeit/Datum, Preis,
     * Veranstaltung, Saal
     * 
     * @param Auffuehrung
     *            Entity mit den zu suchendne Parametern
     * @Return List<Auffuehrung> - Liste mit gefundenen Aufführungen
     * 
     */
    public List<Auffuehrung> findByAuffuehrung(Auffuehrung query);

}
