package at.ticketline.service;

import java.util.List;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.KuenstlerDao;
import at.ticketline.entity.Kuenstler;
import at.ticketline.service.interfaces.KuenstlerService;

public class KuenstlerServiceImpl implements KuenstlerService {

    KuenstlerDao kuenstlerDao = (KuenstlerDao) DaoFactory.findDaoByEntity(Kuenstler.class);
    
    @Override
    public List<Kuenstler> findByKuenstler(Kuenstler k) {
	// TODO Auto-generated method stub
	return kuenstlerDao.findByKuenstler(k);
    }

}
