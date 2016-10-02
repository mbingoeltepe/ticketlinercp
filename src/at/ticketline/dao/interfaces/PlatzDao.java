package at.ticketline.dao.interfaces;

import java.util.Calendar;
import java.util.List;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.Platz;
import at.ticketline.entity.TopTen;



public interface PlatzDao extends GenericDao<Platz,Integer> {
    
    /**
     * Suchte alle Veranstaltung + Anzahl der verkauften Tickets zur jeweiligen Veranstaltung aus der DB.
     * Selektiert wird nach dem Übergebenen Monat und der übergebenen Kategorie.
     * Bei Übergabe eines Leer Strings für die Kategorie werden nur die Top Ten Veranstaltung unabhängig von der Kategorie aus der DB gehohlt
     * @param datum - Datum (mit gesuchten Monat für die TopTen)
     * @param kategorie - Name der Kategorie bzw Leer String wenn nicht nach Kategorie gesucht werden soll
     * 
     * @return Liste mit verkauften Plätzen
     */

	public List<TopTen> findTopTen(Calendar datum, String kategorie);
}