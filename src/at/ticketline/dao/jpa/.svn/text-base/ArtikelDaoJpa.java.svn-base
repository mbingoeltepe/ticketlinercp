package at.ticketline.dao.jpa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.interfaces.ArtikelDao;
import at.ticketline.entity.Artikel;
import at.ticketline.entity.Veranstaltung;

public class ArtikelDaoJpa extends GenericDaoJpa<Artikel, Integer> implements
	ArtikelDao {

    @Override
    public List<Artikel> findByArtikel(Artikel artikel) {
	// TODO Auto-generated method stub
	CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
	CriteriaQuery<Artikel> query = builder.createQuery(Artikel.class);
	Root<Artikel> rootArtikel = query.from(Artikel.class);

	List<Predicate> wherePredicates = new ArrayList<Predicate>();

	if (artikel.getBeschreibung() != null) {
	    String b = artikel.getBeschreibung().replace('*', '%')
		    .replace('?', '_').toUpperCase();
	    wherePredicates.add(builder.like(
		    builder.upper(rootArtikel.<String> get("beschreibung")), b));
	}

	if (artikel.getKategorie() != null) {
	    wherePredicates.add(builder.equal(rootArtikel.get("kategorie"),
		    artikel.getKategorie()));
	}

	if (artikel.getKurzbezeichnung() != null) {
	    String bezeichnung = artikel.getKurzbezeichnung().replace('*', '%')
		    .replace('?', '_').toUpperCase();
	    wherePredicates.add(builder.like(
		    builder.upper(rootArtikel.<String> get("kurzbezeichnung")),
		    bezeichnung));
	}

	if (artikel.getPreis() != null) {
	    wherePredicates.add(builder.equal(
		    rootArtikel.<BigDecimal> get("preis"), artikel.getPreis()));
	}
	if (artikel.getVeranstaltung() != null) {
	    Veranstaltung v = artikel.getVeranstaltung();
	    wherePredicates.add(builder.equal(
		    (rootArtikel.<Veranstaltung> get("veranstaltung")), v));
	}

	Predicate whereClause = builder.and(wherePredicates
		.toArray(new Predicate[0]));

	query.where(whereClause);

	return this.entityManager.createQuery(query).getResultList();
    }

}
