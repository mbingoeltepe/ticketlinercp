package at.ticketline.service.interfaces;

import java.util.List;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Veranstaltung;

import at.ticketline.service.ValidateException;
/**
 * @author Bernhard Sadransky
 */
public interface AuffuehrungService {
    /**
     * Sucht nach Auffuehrungen zu den entsprechenden Parametern
     * 
     * @param a Auffuehrung (für Abfrage brauch wir nur: datumZeit, preis, veranstaltung, saal)
     * @return				Liste mit gefundenen Auffuehrungen zu den Suchkriterien
     * @throws ValidateException	falls datumZeit in der Vergangenheit liegt
     */
    public List<Auffuehrung> findAuffuehrungen(Auffuehrung a) throws ValidateException;
    /**
     * Liefert alle Veranstaltungen
     * 
     * @return	Liste aller Veranstaltungen
     */
    public List<Veranstaltung> findAllVeranstaltungen();
    /**
     * Liefer alle Saele
     * 
     * @return	Liste aller Saele
     */
    public List<Saal> findAllSaele();
}
