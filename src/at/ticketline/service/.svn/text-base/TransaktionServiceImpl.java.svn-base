package at.ticketline.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.KundeDao;
import at.ticketline.dao.interfaces.TransaktionDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.Platz;
import at.ticketline.entity.PlatzStatus;
import at.ticketline.entity.Transaktion;
import at.ticketline.entity.Transaktionsstatus;
import at.ticketline.entity.Zahlungsart;
import at.ticketline.service.interfaces.TransaktionService;

/**
 * 
 * @author bernhard.sadransky
 * 
 */
public class TransaktionServiceImpl implements TransaktionService {

    private TransaktionDao transaktionDao;
    private KundeDao kundeDao;

    /**
     * erstellt TransaktionServiceImpl und generiert eigenes Dao
     */
    public TransaktionServiceImpl() {
	this.transaktionDao = (TransaktionDao) DaoFactory
		.findDaoByEntity(Transaktion.class);
	this.kundeDao = (KundeDao) DaoFactory.findDaoByEntity(Kunde.class);
    }

    /**
     * erstellt TransaktionServiceImpl
     * 
     * @param transaktionDao
     *            zu verwendendes TransaktionDao
     */
    public TransaktionServiceImpl(TransaktionDao transaktionDao,
	    KundeDao kundeDao) {
	this.transaktionDao = transaktionDao;
	this.kundeDao = kundeDao;
    }

    public TransaktionDao getTransaktionDao() {
	return transaktionDao;
    }

    public KundeDao getKundeDao() {
	return kundeDao;
    }

    /**
     * erstellt eine Transaktion mit Status Buchung
     * 
     * @param z
     *            gewünschte Zahlungsart
     * @param k
     *            Kunde der die Buchung in Auftrag gegeben hat
     * @param m
     *            Mitarbeiter der die Buchung durchführt
     * @param plaetze
     *            Plätze die zu buchen sind
     */
    @Override
    public void createBuchung(Zahlungsart z, Kunde k, Mitarbeiter m,
	    Set<Platz> plaetze) {
	Transaktion t = createDefaultVerkaufTransaktion(z, k, m, plaetze);
	t.setStatus(Transaktionsstatus.BUCHUNG);

	for (Platz p : plaetze)
	    p.setStatus(PlatzStatus.GEBUCHT);

	transaktionDao.persist(t);
	transaktionDao.refresh(t);

	if (k != null)
	    kundeDao.refresh(k);
    }

    /**
     * erstellt eine Transaktion mit Status Reservierung
     * 
     * @param z
     *            gewünschte Zahlungsart
     * @param k
     *            Kunde der die Reservierung in Auftrag gegeben hat
     * @param m
     *            Mitarbeiter der die Reservierung durchführt
     * @param plaetze
     *            Plätze die zu reservieren sind
     * 
     * @return reservierungsnummer
     */
    @Override
    public Integer createReservierung(Zahlungsart z, Kunde k, Mitarbeiter m,
	    Set<Platz> plaetze) {
	Transaktion t = createDefaultReservierungTransaktion(z, k, m, plaetze);
	t.setStatus(Transaktionsstatus.RESERVIERUNG);
	for (Platz p : plaetze)
	    p.setStatus(PlatzStatus.RESERVIERT);

	transaktionDao.persist(t);
	transaktionDao.refresh(t);
	t.setReservierungsnr(t.getId());
	transaktionDao.merge(t);

	if (k != null)
	    kundeDao.refresh(k);

	return t.getReservierungsnr();
    }

    /*
     * erzeugt eine Transaktion mit den übergebenen Parametern und setzt bei den
     * Plätzen eine Referenz auf die erzeugte Transaktion
     */
    private Transaktion createDefaultReservierungTransaktion(Zahlungsart z,
	    Kunde k, Mitarbeiter m, Set<Platz> plaetze) {
	Transaktion t = new Transaktion();
	t.setDatumuhrzeit(new Date());
	t.setZahlungsart(z);
	t.setKunde(k);
	t.setMitarbeiter(m);

	for (Platz p : plaetze) {
	    p.setTransaktion(t);
	    p.setStatus(PlatzStatus.RESERVIERT);
	}

	t.setPlaetze(plaetze);

	return t;
    }

    /*
     * erzeugt eine Transaktion mit den übergebenen Parametern und setzt bei den
     * Plätzen eine Referenz auf die erzeugte Transaktion
     */
    private Transaktion createDefaultVerkaufTransaktion(Zahlungsart z, Kunde k,
	    Mitarbeiter m, Set<Platz> plaetze) {
	Transaktion t = new Transaktion();
	t.setDatumuhrzeit(new Date());
	t.setZahlungsart(z);
	t.setKunde(k);
	t.setMitarbeiter(m);

	for (Platz p : plaetze) {
	    p.setTransaktion(t);
	    p.setStatus(PlatzStatus.GEBUCHT);
	}

	t.setPlaetze(plaetze);

	return t;
    }

    @Override
    public Transaktion changeReservierung(Transaktion transaktion) {
	Transaktion t = transaktionDao.findById(transaktion.getId());
	// t.setPlaetze(transaktion.getPlaetze());
	return transaktionDao.merge(transaktion);
	// wichtig! nicht refreshen! Holt anscheinend dann den alten zustand aus
	// der db und der change funktioniert nicht.

    }

    @Override
    public List<Transaktion> findTransaktionByAnonymUndAuffuehrung(
	    Auffuehrung auffuehrung) {
	List<Transaktion> tlist = new ArrayList<Transaktion>();

	if (auffuehrung == null || auffuehrung.getId() == null) {
	    return transaktionDao.findAll();
	}

	for (Transaktion t : transaktionDao.findAll()) {
	    // if (t.getKunde() == null) { (auskommentiert --> alle Kunden
	    // werden gefunden (anonyme + registrierte)
	    // kein Kunde --> Anonym
	    for (Platz p : t.getPlaetze()) {
		if (p.getAuffuehrung() == auffuehrung) {
		    // auffuehrunge stimmt überein
		    tlist.add(t);
		}
		break;
	    }
	    // }
	}

	return tlist;
    }

    @Override
    public List<Transaktion> findAll() {
	return transaktionDao.findAll();
    }

    @Override
    public Transaktion findTransaktionByResNr(int reservierungsNummer) {
	return transaktionDao.findByResNr(reservierungsNummer);
    }

    @Override
    public List<Transaktion> findTransaktionByKundeUndAuffuehrung(Kunde kunde,
	    Auffuehrung auffuehrung) {
	List<Transaktion> transaktionen = new ArrayList<Transaktion>();

	if (kunde.getId() == null
		&& (auffuehrung == null || auffuehrung.getId() == null)) {
	    return transaktionDao.findAll();
	}

	for (Transaktion t : kunde.getTransaktionen()) {

	    for (Platz p : t.getPlaetze()) {
		if (auffuehrung == null) {
		    transaktionen.add(t);
		} else if (p.getAuffuehrung() == auffuehrung
			|| auffuehrung.getId() == null) {
		    transaktionen.add(t);
		}
		break;
	    }
	}

	return transaktionen;
    }

    // @Override
    // public Transaktion findTransaktionByKundeUndAuffuehrung(Kunde kunde,
    // Auffuehrung auffuehrung) {
    //
    // for (Platz p : auffuehrung.getPlaetze()) {
    // Transaktion t = p.getTransaktion();
    // if (t != null) {
    // // manche Plätze haben keine Transaktion
    // // ( wenn sie wieder freigegeben wurden
    // if (t.getKunde() == kunde) {
    // return t;
    // }
    //
    // }
    //
    // }
    // return null;
    // }

    @Override
    public void storniereTransaktion(Transaktion transaktion) {
	for (Platz p : transaktion.getPlaetze()) {
	    p.setStatus(PlatzStatus.FREI);
	}

	transaktionDao.remove(transaktion);
	if (transaktion.getKunde() != null)
	    kundeDao.refresh(transaktion.getKunde());
    }

    public void verkaufeReservierteTickets(Transaktion transaktion) {
	for (Platz p : transaktion.getPlaetze())
	    p.setStatus(PlatzStatus.GEBUCHT);
	transaktion.setStatus(Transaktionsstatus.BUCHUNG);

	transaktionDao.merge(transaktion);
    }

}
