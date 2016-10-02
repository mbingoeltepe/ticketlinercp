package at.ticketline.service;

import java.util.List;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.EngagementDao;
import at.ticketline.entity.Engagement;
import at.ticketline.service.interfaces.EngagementService;

public class EngagementServiceImpl implements EngagementService {

    private EngagementDao engagementDao = (EngagementDao) DaoFactory.findDaoByEntity(Engagement.class);
    @Override
    public List<Engagement> findByEngagement(Engagement e) {
	// TODO Auto-generated method stub
	
	return engagementDao.findByEngagement(e);
    }

}
