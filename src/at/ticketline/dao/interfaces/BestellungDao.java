package at.ticketline.dao.interfaces;

import java.util.List;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.Bestellung;


public interface BestellungDao extends GenericDao<Bestellung,Integer> {

    public List<Bestellung> findByBestellung(Bestellung b);
}
