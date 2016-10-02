package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.interfaces.AuffuehrungDao;
import at.ticketline.entity.Auffuehrung;

/**
 * 
 * @author andrea auer
 * 
 */

public class AuffuehrungDaoJpa extends GenericDaoJpa<Auffuehrung, Integer>
	implements AuffuehrungDao {

    /**
     * Sucht Aufführung aus der DB mit den Parametern: Zeit/Datum, Preis,
     * Veranstaltung, Saal
     * 
     * @param Auffuehrung
     *            Entity mit den zu suchendne Parametern
     * @Return List<Auffuehrung> - Liste mit gefundenen Aufführungen
     * 
     */
    @Override
    public List<Auffuehrung> findByAuffuehrung(final Auffuehrung auffuehrung) {

	CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
	CriteriaQuery<Auffuehrung> query = builder
		.createQuery(Auffuehrung.class);
	Root<Auffuehrung> rootAuffuehrung = query.from(Auffuehrung.class);

	List<Predicate> wherePredicates = new ArrayList<Predicate>();

	if (auffuehrung.getDatumuhrzeit() != null) {
	    // Calendar cal = Calendar.getInstance();
	    // Calendar cUG = Calendar.getInstance(); // Untergrenze, mit Zeit
	    // 00:00
	    // Calendar cOG = Calendar.getInstance(); // Obergrenze, mit Zeit
	    // 23:59
	    // cal.setTime(auffuehrung.getDatumuhrzeit());
	    //
	    // cUG.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
	    // cal.get(Calendar.DATE), 0, 0, 0);
	    // cOG.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
	    // cal.get(Calendar.DATE),23, 59, 59);
	    //
	    // // log.debug("AUFFUEHRUNG SUCHEN JPA: " +
	    // cUG.get(Calendar.YEAR)+"."+ cUG.get(Calendar.MONTH)+"."+
	    // cUG.get(Calendar.DATE)+"_"+ cUG.get(Calendar.HOUR_OF_DAY)+":"+
	    // cUG.get(Calendar.MINUTE)
	    // // +"   COG: " + cOG.get(Calendar.YEAR)+"."+
	    // cOG.get(Calendar.MONTH)+"."+ cOG.get(Calendar.DATE)+"_"+
	    // cOG.get(Calendar.HOUR_OF_DAY)+":"+ cOG.get(Calendar.MINUTE));
	    //
	    //
	    // Date cUGDate = cUG.getTime();
	    // Date cOGDate = cOG.getTime();
	    // wherePredicates.add(
	    // builder.between((rootAuffuehrung.<Date>get("datumuhrzeit")),
	    // cUGDate, cOGDate));
	    // // wherePredicates.add(
	    // builder.between((rootAuffuehrung.<Date>get("datumuhrzeit")),
	    // cUG.getTime(), cOG.getTime()));

	    wherePredicates.add(builder.equal(
		    rootAuffuehrung.<Date> get("datumuhrzeit"),
		    auffuehrung.getDatumuhrzeit()));
	}

	if (auffuehrung.getPreis() != null) {
	    wherePredicates.add(builder.equal(
		    rootAuffuehrung.<String> get("preis"),
		    auffuehrung.getPreis()));
	}

	if (auffuehrung.getVeranstaltung() != null) {
	    wherePredicates.add(builder.equal(
		    rootAuffuehrung.get("veranstaltung"),
		    auffuehrung.getVeranstaltung()));
	}

	if (auffuehrung.getSaal() != null) {
	    wherePredicates.add(builder.equal(rootAuffuehrung.get("saal"),
		    auffuehrung.getSaal()));
	}

	Predicate whereClause = builder.and(wherePredicates
		.toArray(new Predicate[0]));
	log.debug("QUERY: " + query);
	query.where(whereClause);
	query.orderBy(builder.asc(rootAuffuehrung.get("datumuhrzeit")));

	return this.entityManager.createQuery(query).getResultList();

    }

}
