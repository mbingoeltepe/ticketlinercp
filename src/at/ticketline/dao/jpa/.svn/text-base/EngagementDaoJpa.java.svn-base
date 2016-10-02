package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.interfaces.EngagementDao;
import at.ticketline.entity.Engagement;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Veranstaltung;

public class EngagementDaoJpa extends GenericDaoJpa<Engagement, Integer>
	implements EngagementDao {

    @Override
    public List<Engagement> findByEngagement(Engagement e) {
	CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
	CriteriaQuery<Engagement> query = builder.createQuery(Engagement.class);
	Root<Engagement> rootEngagement = query.from(Engagement.class);

	List<Predicate> wherePredicates = new ArrayList<Predicate>();

	if (e.getKuenstler() != null) {
	    Kuenstler k = e.getKuenstler();
	    wherePredicates.add(builder.equal(
		    rootEngagement.<Kuenstler> get("kuenstler"), k));
	}

	if (e.getFunktion() != null) {
	    String funktion = e.getFunktion().replace('*', '%')
		    .replace('?', '_').toUpperCase();
	    wherePredicates.add(builder.like(
		    builder.upper(rootEngagement.<String> get("funktion")),
		    funktion));
	}

	if (e.getGage() != null) {
	    wherePredicates.add(builder.equal(rootEngagement.get("gage"),
		    e.getGage()));
	}
	if (e.getVeranstaltung() != null) {
	    Veranstaltung v = e.getVeranstaltung();
	    int id = v.getId();
	    wherePredicates.add(builder.equal(rootEngagement.get("id"), id));
	}
	

	Predicate whereClause = builder.and(wherePredicates
		.toArray(new Predicate[0]));

	query.where(whereClause);

	return this.entityManager.createQuery(query).getResultList();
    }

}
