package at.ticketline.test.integration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.PlatzDao;
import at.ticketline.entity.Platz;
import at.ticketline.entity.TopTen;
import at.ticketline.service.TopTenServiceImpl;
import at.ticketline.test.AbstractTestCaseBase;

public class TopTenServiceImplIntTest extends AbstractTestCaseBase {

    private static PlatzDao platzDao = null;
    private static TopTenServiceImpl tts;
    
    @Override
    public IDataSet getDataSet() {
	return getDataSet("TopTenServiceImplIntTest.xml");
    }
    
    @Override
    public DatabaseOperation getSetUpOperation() {
	return DatabaseOperation.CLEAN_INSERT;
    }
    
    @Override
    public DatabaseOperation getTearDownOperation() {
	return DatabaseOperation.NONE;
    }
    
    @BeforeClass
    public static void setUp() {	
	tts = new TopTenServiceImpl();
	platzDao = (PlatzDao) DaoFactory.findDaoByEntity(Platz.class);
    }
    
    @Before
    public void before() throws Exception {
	this.onSetup();
    }
    
    @After
    public void after() throws Exception {
	this.onTearDown();
    }
    
    @Test
    public void findVerkaufteTickets_ohneKategorie() {
	Calendar date = GregorianCalendar.getInstance();
	date.set(2010,9,10);
	List<TopTen> platze = tts.getTopTenVeranstlatungen(date);
	assertThat(platze.size(), is(2));
		
    }
    
    @Test
    public void findTopTen_mitKategorie() {
	Calendar date = GregorianCalendar.getInstance();
	date.set(2010,9,10);
	List<TopTen> platze = platzDao.findTopTen(date, "kat");
	assertThat(platze.size(), is(1));
	
    }
    
    @Test
    public void findTopTen_mitFalscherKategorie() {
	Calendar date = GregorianCalendar.getInstance();
	date.set(2010,10,10);
	List<TopTen> platze = tts.getTopTenVeranstaltungenNachKategorie(date, "bla bla bla");
	assertThat(platze.size(), is(0));
	
    }
    

  
}
