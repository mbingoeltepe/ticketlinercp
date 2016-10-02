package at.ticketline.service;

import java.util.List;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.KundeDao;
import at.ticketline.entity.Kunde;
import at.ticketline.service.interfaces.KundeService;

public class KundeServiceImpl implements KundeService {
    
    
    private KundeDao kundeDao;
    
    public KundeServiceImpl() {
	kundeDao = (KundeDao) DaoFactory.findDaoByEntity(Kunde.class);
    }
    
    public KundeServiceImpl(KundeDao kundeDao) {
	this.kundeDao = kundeDao;
    }
    
    public KundeDao getKundeDao() {
	return kundeDao;
    }
    
    @Override
    public Kunde changeKundenDaten(Kunde kunde) {
	return kundeDao.changeKundenDaten(kunde);
    }
    @Override
    public List<Kunde> sucheKunde(Kunde kunde) {	
	return kundeDao.findByKunde(kunde);
    }
    @Override
    public void addKundeToDb(Kunde kunde) {
	kundeDao.insertKunde(kunde);
    }
    @Override
    public List<Kunde> sucheAlleKunden() {
	return kundeDao.findAll();
    }
    @Override
    public void removeKunde(Kunde kunde) {
	kundeDao.remove(kunde);
	
    }
}
