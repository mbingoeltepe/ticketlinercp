package at.ticketline.service;

import java.util.List;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.OrtDao;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Ortstyp;
import at.ticketline.service.interfaces.OrtService;

public class OrtServiceImpl implements OrtService {

    OrtDao ortDao = (OrtDao) DaoFactory.findDaoByEntity(Ort.class);
        
    /**
     * sucht einene/mehrere Veranstaltungsort(e) aus der DB
     * @param ort Ort nachdem gesucht wird
     * @return Eine Liste mit gefunden Veranstaltungsort
     */
    public List<Ort> sucheVeranstaltungsort(Ort ort) {
	return ortDao.findByVeranstaltungsort(ort);
    }
    
    /**
     * sucht alle veranstaltungsorte aus der DB
     * @return Liste mit allen Veranstaltungsorte
     */
    @Override
    public List<Ort> sucheAlleVeranstaltungsorte() {
	List<Ort> orte = (List<Ort>) ortDao.findAll();
	
	// Entfernt alle Orte mit Typ Adresse --> das sind keine Veranstaltungsorte sondern Adressen für Typ Person
	for(int i = 0; i<orte.size(); i++) {
	    if(orte.get(i).getOrtstyp() == Ortstyp.ADRESSE) {
		orte.remove(i);
	    }
	}
	return orte;
    }

}