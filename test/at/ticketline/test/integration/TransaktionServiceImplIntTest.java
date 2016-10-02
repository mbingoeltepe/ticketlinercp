package at.ticketline.test.integration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.AuffuehrungDao;
import at.ticketline.dao.interfaces.KundeDao;
import at.ticketline.dao.interfaces.TransaktionDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Platz;
import at.ticketline.entity.PlatzStatus;
import at.ticketline.entity.Transaktion;
import at.ticketline.entity.Transaktionsstatus;
import at.ticketline.entity.Zahlungsart;
import at.ticketline.service.TransaktionServiceImpl;
import at.ticketline.service.interfaces.TransaktionService;
import at.ticketline.test.AbstractTestCaseBase;
import at.ticketline.test.TestUtil;

public class TransaktionServiceImplIntTest extends AbstractTestCaseBase {
    
    private static TransaktionService ts;
    
    private static TransaktionDao transaktionDao;
    private static KundeDao kundeDao;
    private static AuffuehrungDao auffuehrungDao;
    
    @Override
    public IDataSet getDataSet() {
	return getDataSet("TransaktionServiceImplIntTest.xml");
    }
    
    @Override
    public DatabaseOperation getSetUpOperation() {
	return DatabaseOperation.CLEAN_INSERT;
    }
    
    @Override
    public DatabaseOperation getTearDownOperation() {
	return DatabaseOperation.NONE;
    }
    
    @Before
    public void before() throws Exception {
	this.onSetup();
    }
    
    @After
    public void after() throws Exception {
	this.onTearDown();
    }

    @BeforeClass
    public static void setUp() {
	ts = new TransaktionServiceImpl();
	transaktionDao = (TransaktionDao) DaoFactory.findDaoByEntity(Transaktion.class);
	kundeDao = (KundeDao) DaoFactory.findDaoByEntity(Kunde.class);   
	auffuehrungDao = (AuffuehrungDao) DaoFactory.findDaoByEntity(Auffuehrung.class);   
    }
    
    @Test
    public void createBuchungShouldPersistTransaktion() {
	assertThat(transaktionDao.findAll().size(), is(2));
	ts.createBuchung(Zahlungsart.BANKEINZUG, null, null, new HashSet<Platz>());
	assertThat(transaktionDao.findAll().size(), is(3));
    }

    @Test
    public void createReservierungShouldPersistTransaktionWithResNr() {
	assertThat(transaktionDao.findAll().size(), is(2));
	int resnr = ts.createReservierung(Zahlungsart.BANKEINZUG, null, null, new HashSet<Platz>());
	List<Transaktion> transaktionen = transaktionDao.findAll();
	assertThat(transaktionen.size(), is(3));
	Transaktion t = transaktionDao.findById(resnr);
	assertThat(t.getReservierungsnr(), is(resnr));
    }
    
    @Test
    public void changeReservierungShouldChangePlaetzeInReservierung() {
	Transaktion t_before = transaktionDao.findById(1);
	transaktionDao.refresh(t_before);
	
	assertThat(t_before.getPlaetze().size(), is(3));
	
	Set<Platz> plaetze = new HashSet<Platz>();
	
	for(Platz p : t_before.getPlaetze())
	    if(p.getId() == 1)
		plaetze.add(p);
	
	t_before.setPlaetze(plaetze);
	
	ts.changeReservierung(t_before);
	
	Transaktion t_after = transaktionDao.findById(1);
	assertThat(t_after.getPlaetze().size(), is(1));
    }
    
    @Test
    public void findTransaktionByAnonymUndAuffuehrungShouldFindTransaktion() {
	Auffuehrung a = auffuehrungDao.findById(1);
	
	assertThat(ts.findTransaktionByAnonymUndAuffuehrung(a).size(), is(1));
    }
    
    @Test
    public void findTransaktionByKundeUndAuffuehrungShouldFindTransaktion() {
	Auffuehrung a = auffuehrungDao.findById(1);
	Kunde k = kundeDao.findById(1);
	
	assertThat(ts.findTransaktionByKundeUndAuffuehrung(k, a).size(), is(1));
    }
    
    @Test
    public void findTransaktionByResNrShouldFindTransaktion() {
	assertThat(ts.findTransaktionByResNr(1).getId(), is(1));
    }
    
    @Test
    public void storniereTransaktionShouldRemoveTransaktion() {
	Transaktion t = transaktionDao.findById(1);
	transaktionDao.refresh(t);
	
	assertThat(transaktionDao.findAll().size(), is(2));
	ts.storniereTransaktion(t);
	assertThat(transaktionDao.findAll().size(), is(1));
    }
    
    @Test
    public void verkaufeReservierteTicketsShouldUpdateStatusInTransaktionAndPlatz() {
	Transaktion t = transaktionDao.findById(1);
	transaktionDao.refresh(t);
	
	ts.verkaufeReservierteTickets(t);
	
	assertThat(t.getStatus(), is(Transaktionsstatus.BUCHUNG));
	
	for(Platz p : t.getPlaetze())
	    assertThat(p.getStatus(), is(PlatzStatus.GEBUCHT));
    }
}
