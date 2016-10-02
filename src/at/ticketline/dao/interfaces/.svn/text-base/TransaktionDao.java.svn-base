package at.ticketline.dao.interfaces;

import java.util.List;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.Transaktion;

public interface TransaktionDao extends GenericDao<Transaktion,Integer> {
    
    public List<Transaktion> findByTransaktion(Transaktion query);
    
    /**
     * findet eine Transaktion anhand der Reservierungsnummer. 
     * @param resNr Reservierungsnummer, die die Transaktion eindeutig zuweist.
     * @return gefundene Transaktion
     */
    public Transaktion findByResNr(int resNr);
    
}
