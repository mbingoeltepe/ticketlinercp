package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.interfaces.KuenstlerDao;
import at.ticketline.entity.Kuenstler;

public class KuenstlerDaoJpa extends GenericDaoJpa<Kuenstler, Integer>
		implements KuenstlerDao {

	@Override
	public List<Kuenstler> findByKuenstler(final Kuenstler kuenstler) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Kuenstler> query = builder.createQuery(Kuenstler.class);
		Root<Kuenstler> rootKuenstler = query.from(Kuenstler.class);
		
		List<Predicate> wherePredicates = new ArrayList<Predicate>();
		
		if (kuenstler.getNachname() != null) {
			String nn = kuenstler.getNachname().replace('*', '%').replace('?', '_').toUpperCase();
			wherePredicates.add( builder.like(builder.upper(rootKuenstler.<String>get("nachname")), nn) );
		}
		
		if (kuenstler.getVorname() != null) {
			String vn = kuenstler.getVorname().replace('*', '%').replace('?', '_').toUpperCase();
			wherePredicates.add( builder.like(builder.upper(rootKuenstler.<String>get("vorname")), vn) );
		}
		
		if (kuenstler.getGeschlecht() != null) {
			wherePredicates.add( builder.equal(rootKuenstler.get("geschlecht"), kuenstler.getGeschlecht()) );
		}
		
		Predicate whereClause = builder.and(wherePredicates.toArray(new Predicate[0]));
		
		query.where(whereClause);
		
		return this.entityManager.createQuery(query).getResultList();
		
	}

}
