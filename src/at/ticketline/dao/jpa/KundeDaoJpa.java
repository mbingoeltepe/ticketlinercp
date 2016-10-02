package at.ticketline.dao.jpa;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.interfaces.KundeDao;
import at.ticketline.entity.Kunde;

/**
 * @author andrea.auer
 */

public class KundeDaoJpa extends GenericDaoJpa<Kunde, Integer> implements KundeDao {   
    
    private KundeDao kundeDao;
    /**
     * Sucht Kunden per Nachname und/oder Vorname aus der DB
     * 
     * @param k
     *            - Kunde Entity mit den zu Suchenden DAten
     * @return List<Kunde> - Liste mit den gefundenen Kunden
     */
    public List<Kunde> findByKunde(Kunde k) {
	CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
	CriteriaQuery<Kunde> query = builder.createQuery(Kunde.class);
	Root<Kunde> rootKunde = query.from(Kunde.class);

	List<Predicate> wherePredicates = new ArrayList<Predicate>();

	if (k.getNachname() != null) {
	    String nn = k.getNachname().replace('*', '%').replace('?', '_')
		    .toUpperCase();
	    wherePredicates.add(builder.like(
		    builder.upper(rootKunde.<String> get("nachname")), nn));
	}

	if (k.getVorname() != null) {
	    String vn = k.getVorname().replace('*', '%').replace('?', '_')
		    .toUpperCase();
	    wherePredicates.add(builder.like(
		    builder.upper(rootKunde.<String> get("vorname")), vn));
	}

	if (k.getGeschlecht() != null) {
		wherePredicates.add( builder.equal(rootKunde.get("geschlecht"), k.getGeschlecht()) );
	}
	
	//Gruppe
	if (k.getGruppe() != null) {
	wherePredicates.add( builder.equal(rootKunde.get("gruppe"), k.getGruppe()) );
	}
	// Gesperrt
	wherePredicates.add( builder.equal(rootKunde.get("gesperrt"), k.isGesperrt()) );

	Predicate whereClause = builder.and(wherePredicates
		.toArray(new Predicate[0]));
	query.where(whereClause);
	return this.entityManager.createQuery(query).getResultList();
    }

    /**
     * Speichert die Änderungen am Entity in die Datenbank
     * 
     * @param Kunde
     *            - Kunde Entity mit den Daten die in die DB geschreiben werden
     * @return Kunde - Kunde Entitiy mit den gänderten DAten aus der DB
     */
    @Override
    public Kunde changeKundenDaten(Kunde query) {
	kundeDao = (KundeDao) DaoFactory.findDaoByEntity(Kunde.class);
	Kunde kunde = findById(query.getId());
	kunde = kundeDao.merge(query);
	return kunde;
    }

    /**
     * Speichert einen neuen Kunden in die DB
     * 
     * @param k
     *            - Kunde Entity das in die DB gespeichert wird
     */

    public void insertKunde(Kunde k) {
	kundeDao = (KundeDao) DaoFactory.findDaoByEntity(Kunde.class);
	kundeDao.persist(k);
    }
}
