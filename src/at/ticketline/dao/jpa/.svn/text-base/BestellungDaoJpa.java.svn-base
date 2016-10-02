package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.interfaces.BestellungDao;
import at.ticketline.entity.Bestellung;

public class BestellungDaoJpa extends GenericDaoJpa<Bestellung, Integer>
	implements BestellungDao {

    @Override
    public List<Bestellung> findByBestellung(Bestellung b) {
	// TODO Auto-generated method stub
	CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
	CriteriaQuery<Bestellung> query = builder.createQuery(Bestellung.class);
	Root<Bestellung> rootEngagement = query.from(Bestellung.class);

	List<Predicate> wherePredicates = new ArrayList<Predicate>();

	if (b.getKunde() != null) {
	    int id = b.getKunde().getId();
	    wherePredicates.add(builder.equal((rootEngagement.get("id")), id));
	}

	wherePredicates.add(builder.equal((rootEngagement.get("bezahlt")),
		b.isBezahlt()));

	wherePredicates.add(builder.equal(rootEngagement.get("versandt"),
		b.isVersandt()));

	if (b.getZahlungsart() != null) {
	    wherePredicates.add(builder.equal(
		    builder.upper(rootEngagement.<String> get("zahlungsart")),
		    b.getZahlungsart().toString()));
	}

	if (b.getBestellzeitpunkt() != null) {
	    wherePredicates
		    .add(builder.equal(builder.upper(rootEngagement
			    .<String> get("bestellzeitpunkt")), b
			    .getBestellzeitpunkt()));
	}

	Predicate whereClause = builder.and(wherePredicates
		.toArray(new Predicate[0]));

	query.where(whereClause);

	return this.entityManager.createQuery(query).getResultList();
    }

}
