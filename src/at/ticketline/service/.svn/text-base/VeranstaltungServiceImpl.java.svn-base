package at.ticketline.service;

import java.util.List;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.VeranstaltungDao;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.interfaces.VeranstaltungService;

/**
 * 
 * @author andrea auer
 *
 */

public class VeranstaltungServiceImpl implements VeranstaltungService {
    
    VeranstaltungDao veranstaltungDao = (VeranstaltungDao) DaoFactory.findDaoByEntity(Veranstaltung.class);
    
    /**
     * finde Veranstaltungen mittels Bezeichnung, Kategorie, Dauer und Inhalt
     * @param v Veranstaltung Entity mit den zu suchenen Parametern
     * @return Liste mit den gefundenen Veranstaltungen
     */
    public List<Veranstaltung> findeVeranstaltungen(Veranstaltung v) {
	
	return veranstaltungDao.findByVeranstaltung(v);
	
    }

}
