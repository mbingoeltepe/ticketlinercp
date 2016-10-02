package at.ticketline.service.interfaces;

import java.util.List;
import java.util.Set;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.Platz;
import at.ticketline.entity.Transaktion;
import at.ticketline.entity.Zahlungsart;

/**
 * 
 * @author bernhard.sadransky
 * 
 */
public interface TransaktionService {
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
    public void createBuchung(Zahlungsart z, Kunde k, Mitarbeiter m,
	    Set<Platz> plaetze);

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
    public Integer createReservierung(Zahlungsart z, Kunde k, Mitarbeiter m,
	    Set<Platz> plaetze);

    
    /**
     * findet alle Transaktionen
     * 
     * @return gefundene Transaktionen
     */
    public List<Transaktion> findAll();
    
    /**
     * findet alle Transaktionen zu einer Aufführung, die für anonyme Kunden
     * erstellt wurden
     * 
     * @param a Auffuehrung, nach welcher die Transaktionen gesucht werden
     * @return gefundene Transaktionen
     */
    public List<Transaktion> findTransaktionByAnonymUndAuffuehrung(Auffuehrung a);
    
    /**
     * findet eine Transaktion anhand der Reservierungsnummer.
     * 
     * @param reservierungsNummer
     *            Reservierungsnummer, die die Transaktion eindeutig zuweist.
     * @return gefundene Transaktion
     */
    public Transaktion findTransaktionByResNr(int reservierungsNummer);

    /**
     * findet Transaktionen anhand der Auffuehrung und des Kunden
     * 
     * @param kunde
     *            Kunde, welcher die Transaktion taetigt.
     * @param auffuehrung
     *            Auffuehrung, fuer welche eine Transaktion erstellt wurde
     * @return gefundene Transaktionen
     */
    public List<Transaktion> findTransaktionByKundeUndAuffuehrung(Kunde kunde,
	    Auffuehrung auffuehrung);

    /**
     * uebernimmt eine aktuelle transaktion und schreibt aenderungen in die DB
     * 
     * @param transaktion
     * @return 
     */
    public Transaktion changeReservierung(Transaktion transaktion);

    /**
     * setzt alle plaetze dieser transaktion auf frei und loescht die
     * transaktion aus der db
     * 
     * @param transaktion
     *            Transaktion die storniert werden soll
     */
    public void storniereTransaktion(Transaktion transaktion);

    /**
     * setzt plaetze der transaktion auf gebucht
     * 
     * @param transaktion
     *            Transaktion von welcher plaetze von reserviert auf gebucht
     *            gaendert werden sollen
     */
    public void verkaufeReservierteTickets(Transaktion transaktion);

}
