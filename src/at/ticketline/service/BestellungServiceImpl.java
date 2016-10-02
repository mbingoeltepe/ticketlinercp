package at.ticketline.service;

import java.util.List;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.BestellPositionDao;
import at.ticketline.dao.interfaces.BestellungDao;
import at.ticketline.entity.BestellPosition;
import at.ticketline.entity.Bestellung;
import at.ticketline.service.interfaces.BestellungService;

public class BestellungServiceImpl implements BestellungService {

    BestellungDao bestellungDao = (BestellungDao) DaoFactory
	    .findDaoByEntity(Bestellung.class);
    BestellPositionDao bestellPosDao = (BestellPositionDao) DaoFactory
	    .findDaoByEntity(BestellPosition.class);

    @Override
    public List<Bestellung> findByBestellung(Bestellung b) {
	return bestellungDao.findByBestellung(b);
    }

    @Override
    public List<Bestellung> findAllBestellungen() {
	return bestellungDao.findAll();
    }

    @Override
    public Bestellung addBestellung(Bestellung b) {
	return bestellungDao.persist(b);
    }

    @Override
    public BestellPosition addBestellposition(BestellPosition bp) {
	return bestellPosDao.persist(bp);
    }

}
