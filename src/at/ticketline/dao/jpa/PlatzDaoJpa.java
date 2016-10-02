package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.interfaces.PlatzDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Platz;
import at.ticketline.entity.PlatzStatus;
import at.ticketline.entity.TopTen;
import at.ticketline.entity.Veranstaltung;

public class PlatzDaoJpa extends GenericDaoJpa<Platz, Integer> implements
	PlatzDao {

    /**
     * Suchte alle Veranstaltung + Anzahl der verkauften Tickets zur jeweiligen Veranstaltung aus der DB.
     * Selektiert wird nach dem Übergebenen Monat und der übergebenen Kategorie.
     * Bei Übergabe eines Leer Strings für die Kategorie werden nur die Top Ten Veranstaltung unabhängig von der Kategorie aus der DB gehohlt
     * @param datum - Datum (mit gesuchten Monat für die TopTen)
     * @param kategorie - Name der Kategorie bzw Leer String wenn nicht nach Kategorie gesucht werden soll
     * 
     * @return Liste mit verkauften Plätzen
     */

    @SuppressWarnings("unchecked")
    @Override
    public List<TopTen> findTopTen(Calendar datum, String kategorie) {



	// Erzeuge Datumsspanne für ein Monat - bezüglich des übergebenen Datums
	int monat = datum.get(Calendar.MONTH);
	int jahr = datum.get(Calendar.YEAR);
	int letzter_des_monats = datum.getActualMaximum(Calendar.DAY_OF_MONTH);

	String begin_datum = jahr + "-" + (monat+1) + "-01 00:00:00.0";
	String end_datum = jahr + "-" + (monat+1) + "-" + letzter_des_monats
		+ " 23:59:59.0";

	Calendar gc_begin = GregorianCalendar.getInstance();
	gc_begin.set(jahr, monat, 1, 0, 0, 0);

	Calendar gc_end = GregorianCalendar.getInstance();
	gc_end.set(jahr, monat, letzter_des_monats, 23, 59, 59);

	// Hohle mir die Anzahl der verkauften Tickets nach Veranstaltung
	String findVerkauft = "select count(v.id) from "
		+ Veranstaltung.class.getSimpleName()
		+ " v join v.auffuehrungen a join a.plaetze p where p.status= ?1 ";

	Calendar temp = GregorianCalendar.getInstance();
	temp.clear();

	if (!kategorie.isEmpty()) {
	    findVerkauft += "and v.kategorie = ?2 ";
	}

	if (datum.compareTo(temp) != 0) {
	    findVerkauft += "and p.auffuehrung.id in (select a2.id from "
		    + Auffuehrung.class.getSimpleName()
		    + " a2 where a2.datumuhrzeit between '" + begin_datum
		    + "' and '" + end_datum + "') ";
	}
	findVerkauft += "group by v.id order by v.id";

	log.debug("FIND VERKAUFT SELECT: " + findVerkauft);

	List<Long> count;

	if (findVerkauft.contains("and v.kategorie = ")) {
	    count = this.entityManager.createQuery(findVerkauft)
		    .setParameter(1, PlatzStatus.GEBUCHT)
		    .setParameter(2, kategorie).getResultList();
	} else {
	    count = (List<Long>) this.entityManager.createQuery(findVerkauft)
		    .setParameter(1, PlatzStatus.GEBUCHT).getResultList();
	}

	// Hohle mir die zugehörige Veranstaltung aus der DB
	String findVeranstaltung = "select distinct v from "
		+ Veranstaltung.class.getSimpleName()
		+ " v join v.auffuehrungen a join a.plaetze p where p.status= ?1 ";

	temp.clear();

	if (!kategorie.isEmpty()) {
	    findVeranstaltung += "and v.kategorie = ?2 ";
	}

	if (datum.compareTo(temp) != 0) {
	    findVeranstaltung += "and p.auffuehrung.id in (select a2.id from "
		    + Auffuehrung.class.getSimpleName()
		    + " a2 where a2.datumuhrzeit between '" + begin_datum
		    + "' and '" + end_datum + "') ";
	}
	findVeranstaltung += "order by v.id";

	List<Veranstaltung> veranstaltungen;

	if (findVeranstaltung.contains("and v.kategorie = ")) {

	    veranstaltungen = (List<Veranstaltung>) this.entityManager
		    .createQuery(findVeranstaltung)
		    .setParameter(1, PlatzStatus.GEBUCHT)
		    .setParameter(2, kategorie).getResultList();
	} else {
	    veranstaltungen = (List<Veranstaltung>) this.entityManager
		    .createQuery(findVeranstaltung)
		    .setParameter(1, PlatzStatus.GEBUCHT).getResultList();
	}
	// List<Veranstaltung> veranstaltungen =
	// (List<Veranstaltung>)this.entityManager.createQuery(findVeranstaltung).setParameter(1,
	// PlatzStatus.GEBUCHT).getResultList();

	// Erzeuge Liste mit den TopTen Veranstaltungen
	@SuppressWarnings("rawtypes")
	List<TopTen> topten = new ArrayList();

	int size = 10;
	if (veranstaltungen.size() <= 10)
	    size = veranstaltungen.size();
	for (int i = 0; i < size; i++) {
	    topten.add(new TopTen(veranstaltungen.get(i), count.get(i)
		    .intValue()));
	}

	Collections.sort(topten);

	return topten;

    }
}
