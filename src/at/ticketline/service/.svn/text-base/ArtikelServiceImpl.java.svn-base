package at.ticketline.service;

import java.util.List;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.ArtikelDao;
import at.ticketline.entity.Artikel;
import at.ticketline.service.interfaces.ArtikelService;

public class ArtikelServiceImpl implements ArtikelService {

    private ArtikelDao artikelDao = (ArtikelDao) DaoFactory.findDaoByEntity(Artikel.class);
    @Override
    public List<Artikel> findyByArtikel(Artikel a) {
	// TODO Auto-generated method stub
	return artikelDao.findByArtikel(a);
    }

    @Override
    public Artikel findyById(int id) {
	// TODO Auto-generated method stub
	return artikelDao.findById(id);
    }

    @Override
    public List<Artikel> findAll() {
	// TODO Auto-generated method stub
	return artikelDao.findAll();
    }

}
