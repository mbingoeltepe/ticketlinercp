package at.ticketline.dao.interfaces;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.LoginStatus;
import at.ticketline.entity.Mitarbeiter;

public interface MitarbeiterDao extends GenericDao<Mitarbeiter,Integer> {

	/**
	 * login Methode. Fragt die Datenbank nach username und passwort in Mitarbeiter ab.
	 * liefert den gefundenen Mitarbeiter zurueck.
	 * @param username String, username des Mitarbeiters
	 * @param password String, passwort des Mitarbeiters
	 * @return LoginStatus, enum.
	 */
	public LoginStatus logIn(String username, String password);
	
	/**
	 * loggt den Mitarbeiter wieder aus dem System aus. 
	 * @param mitarbeiter Gibt mitarbeiter an, welcher ausgeloggt werden soll
	 * @return true, falls erfolgreich ausgeloggt wurde, sonst false
	 */
	public boolean logOut(Mitarbeiter mitarbeiter);
	
	
	/**
	 * liefert den aktuell eingeloggten Mitarbeiter
	 * @return aktuell eingeloggter Mitarbeiter
	 */
	public Mitarbeiter getLoggedInMitarbeiter();
}
