package at.ticketline.service;

import java.util.Date;
import java.util.List;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.AuffuehrungDao;
import at.ticketline.dao.interfaces.SaalDao;
import at.ticketline.dao.interfaces.VeranstaltungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.interfaces.AuffuehrungService;

/**
 * 
 * @author Bernhard Sadransky
 * 
 */
public class AuffuehrungServiceImpl implements AuffuehrungService {
    
    protected Logger log = LogFactory.getLogger(AuffuehrungServiceImpl.class);

    private AuffuehrungDao auffuehrungDao;
    private VeranstaltungDao veranstaltungDao;
    private SaalDao saalDao;

    /**
     * Erstellt AuffuehrungService holt DAO's direkt von DaoFactory
     * 
     */
    public AuffuehrungServiceImpl() {
	 this.auffuehrungDao = (AuffuehrungDao) DaoFactory
		.findDaoByEntity(Auffuehrung.class);
	this.veranstaltungDao = (VeranstaltungDao) DaoFactory
		.findDaoByEntity(Veranstaltung.class);
	this.saalDao = (SaalDao) DaoFactory.findDaoByEntity(Saal.class);

    }
    
    public AuffuehrungDao getAuffuehrungDao() {
        return auffuehrungDao;
    }

    public VeranstaltungDao getVeranstaltungDao() {
        return veranstaltungDao;
    }

    public SaalDao getSaalDao() {
        return saalDao;
    }

    /**
     * Erstellt AuffuehrungService
     * 
     * @param auffuehrungDao zu verwendendes AuffuehrungsDao
     * @param veranstaltungDao zu verwendendes VeranstaltungsDao
     * @param saalDao zu verwendendes SaalDao
     */
    public AuffuehrungServiceImpl(AuffuehrungDao auffuehrungDao, VeranstaltungDao veranstaltungDao, 
	    SaalDao saalDao) {
	 this.auffuehrungDao = auffuehrungDao;
	this.veranstaltungDao = veranstaltungDao;
	this.saalDao = saalDao;
   }

    /**
     * Sucht nach Auffuehrungen zu den entsprechenden Parametern
     * 
     * @param a Auffuehrung (für Abfrage brauch wir nur: datumZeit, preis, veranstaltung, saal)
     * @return				Liste mit gefundenen Auffuehrungen zu den Suchkriterien
     * @throws ValidateException	falls datumZeit in der Vergangenheit liegt
    */
    
    @Override
    public List<Auffuehrung> findAuffuehrungen(Auffuehrung a) throws ValidateException 
    {
	
	if (a.getDatumuhrzeit() != null) {

	    if (a.getDatumuhrzeit().before(new Date()))
		throw new ValidateException(ValidateError.INVALID_DATE);    
	}

	return auffuehrungDao.findByAuffuehrung(a);
    }

    /**
     * Liefert alle Veranstaltungen
     * 
     * @return Liste aller Veranstaltungen
     */
    @Override
    public List<Veranstaltung> findAllVeranstaltungen() {
	return this.veranstaltungDao.findAll();
    }

    /**
     * Liefert alle Saele
     * 
     * @return Liste aller Saele
     */
    @Override
    public List<Saal> findAllSaele() {
	return this.saalDao.findAll();
    }
}
