package at.ticketline.dao;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.interfaces.AuffuehrungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.PreisKategorie;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.ValidateException;
import at.ticketline.test.AbstractTestCaseBase;
import at.ticketline.test.TestUtil;

/**
 * 
 * @author MURAT BINGOELTEPE
 *
 */
public class AuffuehrungDaoTest extends AbstractTestCaseBase {
    
    private static  AuffuehrungDao auffuehrungDao = null;
    
    @Override
    public IDataSet getDataSet() {
	return getDataSet("AuffuehrungDaoTest.xml");
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
	auffuehrungDao = (AuffuehrungDao) DaoFactory.findDaoByEntity(Auffuehrung.class);
    }
    
    @Before
    public void before() throws Exception {
	this.onSetup();
    }
    
    @After
    public void after() throws Exception {
	this.onTearDown();
    }
    
    /**
     * Hier wird eine Auffuehrung von der Datenbank gesucht.
     * Positiver Test / Normal Fall
     */
    @Test
    public void testFindByAuffuehrungMitEingabe() throws ValidateException {
	
	// Saal
	Saal s = new Saal();
	s.setBezeichnung("saal");
	
	// Veranstaltung
	Veranstaltung v = new Veranstaltung();
	v.setBezeichnung("veranstaltung");
	v.setKategorie("v_kat");
	v.setDauer(100);
			
	List<Auffuehrung> list = auffuehrungDao.findAll();

	Auffuehrung a = new Auffuehrung();
	
	// Saal und Veranstaltung werden in die Auffuehrung gespecihert.
	a = list.get(0);
	a.setPreis(PreisKategorie.MINDESTPREIS);
	a.setVeranstaltung(v);
	a.setSaal(s);
	
	// Auffuehrung wird aktualisiert
	auffuehrungDao.merge(a);
	
	// findByAuffuehrung
	List<Auffuehrung> auffuehrung = auffuehrungDao.findByAuffuehrung(a);
	
	assertThat(auffuehrung.size(), is(1));
	assertThat(auffuehrung.get(0).getPreis(), is(PreisKategorie.MINDESTPREIS));
	assertThat(auffuehrung.get(0).isStorniert(), is(false));
	
	assertEquals(auffuehrung.get(0).getVeranstaltung(), a.getVeranstaltung());
	assertEquals(auffuehrung.get(0).getSaal(), a.getSaal());
	
    }
    
    /**
     * Hier wird, wenn keine Eingabe eingegeben wurden. Muss alle Auffuehrungen zurück gelifiert
     * Positive Test / Sondern Fall
     */
    @Test
    public void testFindByAuffuehrungOhneEingabe() throws ValidateException {

	Auffuehrung a = new Auffuehrung();

	a.setDatumuhrzeit(null);
	a.setPreis(null);
	
	List<Auffuehrung> list = auffuehrungDao.findByAuffuehrung(a);

	assertEquals(list.size(),1);


    }
   
    /**
     * Wir haben in der DB nur eine Auffuehrung mit preis=PreisKategorie.MINDESTPREIS
     * gespecihert. Jetzt suchen wir diese Auffuehrung mit PreisKategorie.STANDARDPREIS 
     * Es muss eine Fehlermeldung werfen werden.zB("Keine Auffuehrungen mit dieser Preis gefunden.")
     * Negativer Test / Fehler Fall
     */
    @Test
    public void testFindByAuffuehrungMitFalscheWerte() throws ValidateException {


	Auffuehrung a = new Auffuehrung();
	a.setDatumuhrzeit(null);
	a.setPreis(PreisKategorie.STANDARDPREIS);

	try {	    
	    
	    List<Auffuehrung> list = auffuehrungDao.findByAuffuehrung(a);

	    assertTrue(list.isEmpty());
	    assertEquals(list.size(),0);

	    if (list.size() == 0) {
		throw new IllegalArgumentException("Sollte eine IllegalArgumentException werfen");
	    }

	}catch(IllegalArgumentException expected){

	}

    }
    
  
}
