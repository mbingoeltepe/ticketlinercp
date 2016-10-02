package at.ticketline.test.integration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.ArtikelDao;
import at.ticketline.entity.Artikel;
import at.ticketline.service.ArtikelServiceImpl;
import at.ticketline.test.AbstractTestCaseBase;

public class ArtikelServiceImplIntTest extends AbstractTestCaseBase {

    private static ArtikelDao artikelDao = null;
    private static ArtikelServiceImpl as;
    
    @Override
    public IDataSet getDataSet() {
	return getDataSet("ArtikelServiceImplIntTest.xml");
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
	as = new ArtikelServiceImpl();
	artikelDao = (ArtikelDao) DaoFactory.findDaoByEntity(Artikel.class);
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
    public void findByArtikel() {
	Artikel a = new Artikel();
	a.setKurzbezeichnung("kurzbezeichnung artikel1");
	List<Artikel> list = as.findyByArtikel(a);
	
	assertThat(list.size(), is(1));
	assertEquals(list.get(0).getKurzbezeichnung(), "kurzbezeichnung artikel1");
	assertEquals(list.get(0).getBeschreibung(), "beschreibung artikel1");
    }
    
    @Test
    public void findByArtikelMitFalscheArtikelBezeichnung() {
	Artikel a = new Artikel();
	a.setKurzbezeichnung("keine");
	List<Artikel> list = as.findyByArtikel(a);
	
	assertThat(list.size(), is(0));
    }

    @Test
    public void findById() {
	
	Artikel a = artikelDao.findById(2);
	
	assertEquals(a.getKurzbezeichnung(), "kurzbezeichnung artikel2");
	assertEquals(a.getBeschreibung(), "beschreibung artikel2");
    }
    
    @Test
    public void findMitFalscheId() {
	
	Artikel a = artikelDao.findById(5);
	
	assertNull(a);
	
    }

    @Test
    public void findAll() {

	List<Artikel> list = as.findAll();	
	
	assertThat(list.size(), is(3));
	assertEquals(list.get(0).getKurzbezeichnung(), "kurzbezeichnung artikel1");
	assertEquals(list.get(0).getBeschreibung(), "beschreibung artikel1");

	assertEquals(list.get(1).getKurzbezeichnung(), "kurzbezeichnung artikel2");
	assertEquals(list.get(1).getBeschreibung(), "beschreibung artikel2");

	assertEquals(list.get(2).getKurzbezeichnung(), "kurzbezeichnung artikel3");
	assertEquals(list.get(2).getBeschreibung(), "beschreibung artikel3");

	
    }

  
}
