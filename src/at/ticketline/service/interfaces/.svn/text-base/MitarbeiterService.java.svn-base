/**
 * 
 */
package at.ticketline.service.interfaces;

import at.ticketline.entity.LoginStatus;
import at.ticketline.entity.Mitarbeiter;

/**
 * @author stefanvoeber
 *
 */
public interface MitarbeiterService {
    
    /**
     * loggt den Mitarbeiter ins System ein.
     * Sucht Mitarbeiter anhand von Username und Passwort. 
     * Gibt gefundenen Mitarbeiter zurueck
     * 
     * @param username Der Username des Mitarbeiters String
     * @param password Das Passwort des Mitarbeiters String
     * @return LoginStatus, enum.
     */
    public LoginStatus logIn(String username, String password);
    
    /**
     * loggt den eingeloggten Mitarbeiter wieder aus dem System aus. 
     * 
     */
    public boolean logOut();
    
    
    
    /**
     * Liefert den aktuell eingeloggten Mitarbeiter
     * @return aktuell eingeloggter Mitarbeiter
     */
    public Mitarbeiter getLoggedInMitarbeiter();
    
    
    /**
     * Liefert einen Boolean zurück. Gibt Auskunft, ob aktuell ein Mitarbeiter eingeloggt ist oder nicht.
     * @return true = ein Mitarbeiter ist eingeloggt, false = es ist kein Mitarbeiter eingeloggt.
     */
    public Boolean isLoggedIn();

}
