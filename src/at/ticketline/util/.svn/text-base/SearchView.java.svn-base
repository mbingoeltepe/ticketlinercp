package at.ticketline.util;

/**
 * Interface um InstantSearch Funktion für Views leichter zu Implementieren
 * 
 * @author Georg Fuderer
 * 
 * @see at.ticketline.util.InstantSearch
 * 
 */
public interface SearchView {

    public static final Boolean enableInstantSearchOnStartup = true;
    
    /**
     * Funktion, die im jeweiligen View die Such-Anfrage ausführt
     */
    public void searchHandler();

    /**
     * Hier werden Vorkehrungen getroffen, um InstantSearch zu aktivieren (z.B.:
     * Entfernen etwaiger zuvor angelegter Suche-Buttons, registrieren der Text
     * und Combo Widgets mittels static Methoden der Klasse "InstantSearch.java"
     * 
     * @see at.ticketline.util.InstantSearch
     */
    public void enableInstantSearch();

}
