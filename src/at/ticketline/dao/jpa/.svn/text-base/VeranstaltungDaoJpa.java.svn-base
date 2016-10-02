package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.interfaces.VeranstaltungDao;
import at.ticketline.entity.Veranstaltung;

public class VeranstaltungDaoJpa extends GenericDaoJpa<Veranstaltung,Integer> implements VeranstaltungDao {
    
    /**
     * Sucht Veranstaltung per Bezeichnung, Kategorie, Dauer und Inhalt aus der DB 
     * @param v
     *            - Veranstaltung Entity mit den zu Suchenden DAten
     * @return List<Veranstaltung> - Liste mit den gefundenen Veranstaltungen
     */
    public List<Veranstaltung> findByVeranstaltung(Veranstaltung v) {
	
	CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
	CriteriaQuery<Veranstaltung> query = builder.createQuery(Veranstaltung.class);
	Root<Veranstaltung> rootVeranstaltung = query.from(Veranstaltung.class);
	
	List<Predicate> wherePredicates = new ArrayList<Predicate>();
	
	if (v.getBezeichnung() != null) {
		String bez = v.getBezeichnung().replace('*', '%').replace('?', '_').toUpperCase();
		wherePredicates.add( builder.like(builder.upper(rootVeranstaltung.<String>get("bezeichnung")), bez) );
	}
	
	if (v.getKategorie() != null) {
		String kat = v.getKategorie().replace('*', '%').replace('?', '_').toUpperCase();
		wherePredicates.add( builder.like(builder.upper(rootVeranstaltung.<String>get("kategorie")), kat) );
	}
	
	if (v.getDauer() != null) {
		wherePredicates.add( builder.equal(rootVeranstaltung.get("dauer"), v.getDauer()) );
	}
	
	if (v.getInhalt() != null) {
		String inh = v.getInhalt().replace('*', '%').replace('?', '_').toUpperCase();
		wherePredicates.add( builder.like(builder.upper(rootVeranstaltung.<String>get("inhalt")), inh) );
	}
	

	
	Predicate whereClause = builder.and(wherePredicates.toArray(new Predicate[0]));
	
	query.where(whereClause);
	
	return this.entityManager.createQuery(query).getResultList();
	
	
    }

}
