package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.interfaces.TransaktionDao;
import at.ticketline.entity.Transaktion;
/**
 * 
 * @author MURAT BINGOLTEPE
 *
 */
public class TransaktionDaoJpa extends GenericDaoJpa<Transaktion,Integer> implements TransaktionDao {


    /**
     * Sucht Transaktion aus der DB mit den Parametern: Zeit/Datum, Reservierungsnummer, Kunde, Mitarbeiter
     * @param Transaktion Entity mit den zu suchendne Parametern
     * @Return List<Transaktion> - Liste mit gefundenen Transaktionen
     * 
     */
    @Override
    public List<Transaktion> findByTransaktion(Transaktion transaktion) {
	CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
	CriteriaQuery<Transaktion> query = builder.createQuery(Transaktion.class);
	Root<Transaktion> rootTransaktion = query.from(Transaktion.class);

	List<Predicate> wherePredicates = new ArrayList<Predicate>();

	if (transaktion.getDatumuhrzeit() != null) {
	    wherePredicates.add(builder.equal(rootTransaktion.<Date>get("datumuhrzeit"), transaktion.getDatumuhrzeit()));
	}
	
	if (transaktion.getReservierungsnr() != null) {
		wherePredicates.add( builder.equal(rootTransaktion.<String>get("reservierungsnr"), transaktion.getReservierungsnr()));
	}
	
	if (transaktion.getKunde() != null) {
		wherePredicates.add( builder.equal(rootTransaktion.get("kunde"), transaktion.getKunde()) );
	}

	if (transaktion.getMitarbeiter() != null) {
		wherePredicates.add( builder.equal(rootTransaktion.get("mitarbeiter"), transaktion.getMitarbeiter()) );
	}

	Predicate whereClause = builder.and(wherePredicates.toArray(new Predicate[0]));
	log.debug("QUERY: " + query);
	query.where(whereClause);
	return this.entityManager.createQuery(query).getResultList();
    }

    @Override
    public Transaktion findByResNr(int resNr) {
	CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
	CriteriaQuery<Transaktion> query = builder.createQuery(Transaktion.class);
	Root<Transaktion> rootTransaktion = query.from(Transaktion.class);

	List<Predicate> wherePredicates = new ArrayList<Predicate>();
	
	wherePredicates.add(builder.equal(rootTransaktion.<String>get("reservierungsnr"), resNr));
	Predicate whereClause = builder.and(wherePredicates.toArray(new Predicate[0]));
	log.debug("QUERY: " + query);
	query.where(whereClause);
	try {
	    Transaktion t = this.entityManager.createQuery(query).getSingleResult();
	    return t;
	} catch (NoResultException e) {
	    return null;
	}
    }

}
