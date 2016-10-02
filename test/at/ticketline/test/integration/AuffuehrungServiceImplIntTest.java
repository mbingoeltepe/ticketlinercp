package at.ticketline.test.integration;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.PreisKategorie;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.AuffuehrungServiceImpl;
import at.ticketline.service.ValidateException;
import at.ticketline.test.AbstractTestCaseBase;
import at.ticketline.test.TestUtil;

public class AuffuehrungServiceImplIntTest extends AbstractTestCaseBase {
    
    private static AuffuehrungServiceImpl as;

    @Override
    public IDataSet getDataSet() {
	return getDataSet("AuffuehrungServiceImplIntTest.xml");
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
	as = new AuffuehrungServiceImpl();
    }
    
    @Test
    public void findAuffuehrungenShouldReturnList() throws ValidateException
    {	
	Integer[] datumZeit = {2011, 0, 31, 20, 0};
	
	Calendar c = new GregorianCalendar();
	c.clear();
	c.set(datumZeit[0], datumZeit[1], datumZeit[2], datumZeit[3], datumZeit[4]);
	
	Auffuehrung a = new Auffuehrung();
	a.setDatumuhrzeit(c.getTime());
	a.setPreis(PreisKategorie.MAXIMALPREIS);
	
	Veranstaltung v = new Veranstaltung();
	v.setId(1);
	
	Saal s = new Saal();
	s.setId(1);
	
	a.setVeranstaltung(v);
	a.setSaal(s);
		
	assertThat(as.findAuffuehrungen(a).size(), is(1));
    }

    @Test(expected=ValidateException.class)
    public void findAuffuehrungenShouldThrowException() throws ValidateException {	
	Integer[] datumZeit = {2009, 12, 24, 0, 0};
	
	Calendar c = new GregorianCalendar();
	c.set(Calendar.YEAR, datumZeit[0]);
	c.set(Calendar.MONTH, datumZeit[1]);
	c.set(Calendar.DAY_OF_MONTH, datumZeit[2]);
	c.set(Calendar.HOUR_OF_DAY, datumZeit[3]);
	c.set(Calendar.MINUTE, datumZeit[4]);
	
	Auffuehrung a = new Auffuehrung();
	a.setDatumuhrzeit(c.getTime());
	
	as.findAuffuehrungen(a);
    }

    @Test
    public void findAllVeranstaltungenShouldReturnList() {
	assertThat(as.findAllVeranstaltungen().size(), is(1));
    }

    @Test
    public void findAllSaeleShouldReturnList() {
	assertThat(as.findAllSaele().size(), is(1));
    }
}