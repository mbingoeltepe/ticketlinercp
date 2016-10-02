package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.interfaces.OrtDao;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Ortstyp;

/**
 * @author MURAT BINGOLTEPE, Andrea Auer
 */

public class OrtDaoJpa extends GenericDaoJpa<Ort,Integer> implements OrtDao {
    
    /**
     * Sucht Orten per Bezeichnung aus der DB
     * 
     * @param query
     *            - Ort Entity mit den zu Suchenden Daten
     * @return List<Ort> - Liste mit den gefundenen Orten
     */
    @Override
    public List<Ort> findByOrt(final Ort ort) {

	CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
	CriteriaQuery<Ort> query = builder.createQuery(Ort.class);
	Root<Ort> rootOrt = query.from(Ort.class);

	List<Predicate> wherePredicates = new ArrayList<Predicate>();

	if (ort.getBezeichnung() != null) {
	    String b = ort.getBezeichnung().replace('*', '%').replace('?', '_').toUpperCase();
	    wherePredicates.add( builder.like(builder.upper(rootOrt.<String>get("bezeichnung")), b) );
	}


	Predicate whereClause = builder.and(wherePredicates.toArray(new Predicate[0]));

	query.where(whereClause);

	return this.entityManager.createQuery(query).getResultList();

    }
    
    @Override
    public List<Ort> findByVeranstaltungsort(Ort ort) {
	CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
	CriteriaQuery<Ort> query = builder.createQuery(Ort.class);
	Root<Ort> rootOrt = query.from(Ort.class);

	List<Predicate> wherePredicates = new ArrayList<Predicate>();

	if (ort.getBezeichnung() != null) {
	    String bez = ort.getBezeichnung().replace('*', '%')
		    .replace('?', '_').toUpperCase();
	    log.info("------------------------------------------------Bezeichnung: "+ bez);
	    wherePredicates.add(builder.like(builder.upper(rootOrt.<String> get("bezeichnung")), bez));
	}
	
	if (ort.getOrtstyp() != null) {
	    wherePredicates.add(builder.equal(rootOrt.get("typ"), ort.getOrtstyp()));
	    log.info("------------------------------------------------orttyp: "+ ort.getOrtstyp());
	}

	if (ort.getAdresse().getStrasse() != null) {
	    String str = ort.getAdresse().getStrasse().replace('*', '%')
		    .replace('?', '_').toUpperCase();
	    log.info("------------------------------------------------Strasse: "+ str);
	    wherePredicates.add(builder.like(builder.upper(rootOrt.get("adresse").<String> get("strasse")), str));
	}
	
	if (ort.getAdresse().getPlz() != null) {
	    String plz = ort.getAdresse().getPlz().replace('*', '%')
		    .replace('?', '_').toUpperCase();
	    log.info("------------------------------------------------Plz: "+ plz);
	    wherePredicates.add(builder.like(
		    builder.upper(rootOrt.get("adresse").<String> get("plz")), plz));
	    
	}

	if (ort.getAdresse().getOrt() != null) {
	    String ortname = ort.getAdresse().getOrt().replace('*', '%')
		    .replace('?', '_').toUpperCase();
	    log.info("------------------------------------------------Ort: "+ ortname);
	    wherePredicates.add(builder.like(
		    builder.upper(rootOrt.get("adresse").<String> get("ort")), ortname));
	    
	}

	if (ort.getAdresse().getLand() != null) {
	    String land = ort.getAdresse().getLand().replace('*', '%')
		    .replace('?', '_').toUpperCase();
	    log.info("------------------------------------------------Land: "+ land);
	    wherePredicates.add(builder.like(
		    builder.upper(rootOrt.get("adresse").<String> get("land")), land));
	 
	}

	wherePredicates.add(builder.notEqual(rootOrt.get("typ"), Ortstyp.ADRESSE));

	Predicate whereClause = builder.and(wherePredicates
		.toArray(new Predicate[0]));

	query.where(whereClause);

	return this.entityManager.createQuery(query).getResultList();
    }

}
