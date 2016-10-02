package at.ticketline.service.interfaces;

import java.util.Date;
import java.util.List;

import at.ticketline.entity.TopTen;

public interface TopTenService {
    
    /**
     * Sucht die Top Ten Veranstaltungen + Anzahl der Verkauften Tickets zur jeweiligen Veranstaltung absteigend Sortiert nach Anzahl verkaufter Tickets
     * @param datum - Datum (=Monat) für das die TopTen Veranstaltungen gesucht wird
     * @return List<TopTen> Liste mit den TopTen verkauften Veranstaltunge + Anzahl der Verkauften Tickets zur Veranstaltung
     */
    public List<TopTen> getTopTenVeranstlatungen(Date datum);
    
    /**
     * Sucht die Top Ten Veranstaltungen + Anzahl der Verkauften Tickets zur jeweiligen Veranstaltung absteigend Sortiert nach Anzahl verkaufter Tickets
     * @param datum - Datum (=Monat) für das die TopTen Veranstaltungen gesucht wird
     * @param kategorie - Es wird nach den Top Ten Veranstaltungen mit der übergebenen Kategorie gesucht
     * @return List<TopTen> Liste mit den TopTen verkauften Veranstaltunge + Anzahl der Verkauften Tickets zur Veranstaltung
     */
    public List<TopTen> getTopTenVeranstaltungenNachKategorie(Date datum, String kategorie);
    

}
