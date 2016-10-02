package at.ticketline.service.interfaces;

import java.util.List;

import at.ticketline.entity.Artikel;

/**
 * Interface fuer Service Methoden von Artikel 
 * @author stefanvoeber
 *
 */
public interface ArtikelService {

    /** findByArtikel
     * sucht den gewuenschten Artikel in der DB und liefert Ergebnisse
     * @param a: Gesuchter Artikel
     * @return Eine Liste der Artikel, die auf die Suchkriterien passen
     */
    public List<Artikel> findyByArtikel(Artikel a);
    
    /**
     * sucht den Artikel anhand der ID
     * @param id
     * @return Einen Artikel mit entsprechender ID, sonst null
     */
    public Artikel findyById(int id);
    
    /**
     * sucht alle Artikel
     * @return alle Artikel, die in der DB gefunden wurden
     */
    public List<Artikel> findAll();
}
