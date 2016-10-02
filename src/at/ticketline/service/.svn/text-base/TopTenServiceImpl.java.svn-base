package at.ticketline.service;

import java.util.Calendar;
import java.util.List;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.PlatzDao;
import at.ticketline.entity.Platz;
import at.ticketline.entity.TopTen;

public class TopTenServiceImpl {
    PlatzDao platzDao = (PlatzDao) DaoFactory.findDaoByEntity(Platz.class);
    
    /**
     * Sucht die Top Ten Veranstaltungen + Anzahl der Verkauften Tickets zur jeweiligen Veranstaltung absteigend Sortiert nach Anzahl verkaufter Tickets
     * @param datum - Datum (=Monat) für das die TopTen Veranstaltungen gesucht wird
     * @return List<TopTen> Liste mit den TopTen verkauften Veranstaltunge + Anzahl der Verkauften Tickets zur Veranstaltung
     */
    public List<TopTen> getTopTenVeranstlatungen(Calendar datum) {
	
	return platzDao.findTopTen(datum, "");
    }
    
    /**
     * Sucht die Top Ten Veranstaltungen + Anzahl der Verkauften Tickets zur jeweiligen Veranstaltung absteigend Sortiert nach Anzahl verkaufter Tickets
     * @param datum - Datum (=Monat) für das die TopTen Veranstaltungen gesucht wird
     * @param kategorie - Es wird nach den Top Ten Veranstaltungen mit der übergebenen Kategorie gesucht
     * @return List<TopTen> Liste mit den TopTen verkauften Veranstaltunge + Anzahl der Verkauften Tickets zur Veranstaltung
     */
    public List<TopTen> getTopTenVeranstaltungenNachKategorie(Calendar datum, String kategorie) {
	return platzDao.findTopTen(datum, kategorie);
    }

}
