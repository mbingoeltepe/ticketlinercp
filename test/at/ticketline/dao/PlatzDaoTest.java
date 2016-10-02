package at.ticketline.dao;

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

import at.ticketline.dao.interfaces.PlatzDao;
import at.ticketline.entity.Platz;
import at.ticketline.entity.TopTen;
import at.ticketline.test.AbstractTestCaseBase;

public class PlatzDaoTest extends AbstractTestCaseBase {

    private static PlatzDao platzDao = null;
    
    /* DataSet mit dem die Operationen durchgefuehrt werden */
    @Override
    public IDataSet getDataSet() {
	return getDataSet("PlatzDaoTest.xml");
    }
    
    /* Wird ausgefuehrt wenn onSetup() aufgerufen wird.
     * CLEAN_INSERT loescht die im DataSet angegebene(n) Tabelle(n) und fuegt dann die Daten ein.
     * Default Operation ist CLEAN_INSERT (man muesste es hier also nicht angeben)*/
    @Override
    public DatabaseOperation getSetUpOperation() {
	return DatabaseOperation.CLEAN_INSERT;
    }
    
    /* Wird ausgefuehrt wenn ontTearDown() aufgerufen wird.
     * NONE tut gar nichts.
     * Default Operation ist NONE (man muesste es hier also nicht angeben)*/    
    @Override
    public DatabaseOperation getTearDownOperation() {
	return DatabaseOperation.NONE;
    }
    
    @BeforeClass
    public static void setUp() {	
	platzDao = (PlatzDao) DaoFactory.findDaoByEntity(Platz.class);
    }
    
    @Before
    public void before() throws Exception {
	this.onSetup(); // vorher angebene SetUp-Operation wird ausgefuehrt
    }
    
    @After
    public void after() throws Exception {
	this.onTearDown(); //vorher angegebene TearDown-Operation wird ausgefuehrt
    }
    
    @Test
    public void findVerkaufteTickets_ohneKategorie() {
	Calendar date = GregorianCalendar.getInstance();
	date.set(2010,9,10); // 9 ist Oktober beim Gregorian Calender
	//date.clear();
	List<TopTen> platze = platzDao.findTopTen(date, "");
	assertThat(platze.size(), is(2));
	for(int i=0; i< platze.size(); i++) {
	    System.out.println("Veranstaltung: "+platze.get(i).toString());
	}
	
    }
    
    @Test
    public void findTopTen_mitKategorie() {
	Calendar date = GregorianCalendar.getInstance();
	date.set(2010,9,10); // 9 ist Oktober beim Gregorian Calender
	//date.clear();
	List<TopTen> platze = platzDao.findTopTen(date, "kat");
	assertThat(platze.size(), is(1));
	for(int i=0; i< platze.size(); i++) {
	    System.out.println("Veranstaltung: "+platze.get(i).toString());
	}
	
    }
    
    @Test
    public void findTopTen_mitFalscherKategorie() {
	Calendar date = GregorianCalendar.getInstance();
	date.set(2010,9,10); // 9 ist Oktober beim Gregorian Calender
	//date.clear();
	List<TopTen> platze = platzDao.findTopTen(date, "bla bla bla");
	assertThat(platze.size(), is(0));
	
    }
    

  
}
