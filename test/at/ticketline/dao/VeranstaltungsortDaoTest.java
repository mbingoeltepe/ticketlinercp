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

import at.ticketline.dao.interfaces.OrtDao;
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Ortstyp;
import at.ticketline.service.ValidateException;
import at.ticketline.test.AbstractTestCaseBase;

/*
 * @author auer andrea
 */

public class VeranstaltungsortDaoTest extends AbstractTestCaseBase {
    
    private static OrtDao ortDao = null;
    
    @Override
    public IDataSet getDataSet() {
	return getDataSet("VeranstaltungsortDaoTest.xml");
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
    public static void setUp() throws Exception {
	ortDao = (OrtDao) DaoFactory.findDaoByEntity(Ort.class);
	//veranstaltungsortDao = (VeranstaltungsortDao) DaoFactory.findDaoByEntity(Ort.class);
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
     * Hier wird ein Veranstaltungsort mit Bezeichnung gesucht
     * Positiver Test / Normal Fall
     */
    @Test
    public void testFindByVeranstaltungsortMitBezeichnung() throws ValidateException {
	
	Ort o = new Ort();
	o.setBezeichnung("halle1");
	o.setOrtstyp(null);
	
	Adresse a = new Adresse();
	a.setStrasse(null);
	a.setPlz(null);
	a.setOrt(null);
	a.setLand(null);
	o.setAdresse(a);
	
	List<Ort> veranstaltungsort = ortDao.findByVeranstaltungsort(o);

	
	assertThat(veranstaltungsort.size(), is(1));
	assertEquals(veranstaltungsort.get(0).getBezeichnung(), "halle1");
	assertEquals(veranstaltungsort.get(0).getAdresse().getStrasse(), "hauptstr1234");


	
    }
    
    /**
     * Hier wird ein Veranstaltungsort ohne Parameter gesucht, es sollen alle Orte (die nicht Typ 8 hat) ausgegeben werden
     * Positiver Test / Normal Fall
     */
    @Test
    public void testFindByVeranstaltungsortOhneParameter() throws ValidateException {
	
	Ort o = new Ort();
	Adresse a = new Adresse();

	o.setAdresse(a);
	
	List<Ort> veranstaltungsort = ortDao.findByVeranstaltungsort(o);

	assertThat(veranstaltungsort.size(), is(2));


	
    }
    
    /**
     * Hier wird ein Veranstaltungsort mit Bezeichnung und Adresse gesucht
     * Positiver Test / Normal Fall
     */
    @Test
    public void testFindByVeranstaltungsortMitAdresse() throws ValidateException {
	
	Ort o = new Ort();
	o.setBezeichnung("halle1");
	o.setOrtstyp(Ortstyp.THEATER);
	
	Adresse a = new Adresse();
	a.setStrasse("hauptstr1234");
	a.setPlz("1111");
	a.setOrt("fakecity");
	a.setLand("fakeland");
	o.setAdresse(a);
	
	List<Ort> veranstaltungsort = ortDao.findByVeranstaltungsort(o);

	assertThat(veranstaltungsort.size(), is(1));
	assertEquals(veranstaltungsort.get(0).getBezeichnung(), "halle1");
	assertEquals(veranstaltungsort.get(0).getAdresse().getStrasse(), "hauptstr1234");

    }
    

   
    /**
     * Wir haben in der DB keinen Ort vom Typ 5
     * gespeichhert. Jetzt suchen wir diese Auffuehrung mit Typ 5 - es soll kein Ort gefunden werden
     * Es muss eine Fehlermeldung werfen werden.zB("Keine Veranstaltungsort mit diesem Typ gefunden")
     * Negativer Test / Fehler Fall
     */
    @Test
    public void testFindByVeranstaltungsortMitFalscheWerte() throws ValidateException {


	Ort o = new Ort();
	Adresse a = new Adresse();
	o.setAdresse(a);
	o.setOrtstyp(Ortstyp.KABARETT);


	try {	    
	    
	    List<Ort> list = ortDao.findByVeranstaltungsort(o);

	    assertTrue(list.isEmpty());
	    assertEquals(list.size(),0);

	    if (list.size() == 0) {
		throw new IllegalArgumentException("Sollte eine IllegalArgumentException werfen");
	    }

	}catch(IllegalArgumentException expected){

	}

    }
    
  
}
