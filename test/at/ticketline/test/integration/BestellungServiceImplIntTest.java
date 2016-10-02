package at.ticketline.test.integration;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
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
import at.ticketline.dao.interfaces.ArtikelDao;
import at.ticketline.dao.interfaces.BestellPositionDao;
import at.ticketline.dao.interfaces.BestellungDao;
import at.ticketline.entity.Artikel;
import at.ticketline.entity.ArtikelKategorie;
import at.ticketline.entity.BestellPosition;
import at.ticketline.entity.Bestellung;
import at.ticketline.entity.Zahlungsart;
import at.ticketline.service.BestellungServiceImpl;
import at.ticketline.test.AbstractTestCaseBase;

public class BestellungServiceImplIntTest extends AbstractTestCaseBase {

    private static BestellungDao bestellungDao = null;
    private static BestellPositionDao bestellpositionDao = null;    
    private static ArtikelDao artikelDao = null;    
    private static BestellungServiceImpl bs;

    @Override
    public IDataSet getDataSet() {
	return getDataSet("BestellungServiceImplIntTest.xml");
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
	bs = new BestellungServiceImpl();
	bestellungDao = (BestellungDao) DaoFactory.findDaoByEntity(Bestellung.class);
	bestellpositionDao = (BestellPositionDao) DaoFactory.findDaoByEntity(BestellPosition.class);
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
    public void findByBestellung() {
	Bestellung b = new Bestellung();
	b.setBezahlt(true);
	b.setVersandt(true);
	List<Bestellung> list = bs.findByBestellung(b);

	assertThat(list.size(), is(2));
	assertEquals(list.get(0).isVersandt(), true);
	assertEquals(list.get(1).getBestellzeitpunkt().toString(), "Wed Oct 20 19:00:00 CEST 2010");

    }

    @Test
    public void findAllBestellungen() {

	List<Bestellung> list = bestellungDao.findAll();

	assertThat(list.size(), is(3));
	assertEquals(list.get(0).isVersandt(), true);
	assertEquals(list.get(1).isBezahlt(), false);
	assertEquals(list.get(2).getBestellzeitpunkt().toString(), "Wed Oct 20 19:00:00 CEST 2010");

    }
    
    @Test
    public void addBestellung() {

	Integer[] datumZeit = {2011, 1, 10, 0, 0};
	
	Calendar c = new GregorianCalendar();
	c.set(Calendar.YEAR, datumZeit[0]);
	c.set(Calendar.MONTH, datumZeit[1]);
	c.set(Calendar.DAY_OF_MONTH, datumZeit[2]);
	c.set(Calendar.HOUR_OF_DAY, datumZeit[3]);
	c.set(Calendar.MINUTE, datumZeit[4]);

	Bestellung b = new Bestellung();
	b.setBezahlt(true);
	b.setVersandt(false);
	b.setBestellzeitpunkt(c.getTime());
	b.setZahlungsart(Zahlungsart.VORKASSE);

	
	int i = bestellungDao.findAll().size();
	
	bs.addBestellung(b);
			
	List<Bestellung> list = bs.findAllBestellungen();

	assertThat(list.size(), greaterThan(i));

    }
    
    @Test
    public void addBestellposition() {

	Integer[] datumZeit = {2011, 1, 10, 0, 0};
	
	Calendar c = new GregorianCalendar();
	c.set(Calendar.YEAR, datumZeit[0]);
	c.set(Calendar.MONTH, datumZeit[1]);
	c.set(Calendar.DAY_OF_MONTH, datumZeit[2]);
	c.set(Calendar.HOUR_OF_DAY, datumZeit[3]);
	c.set(Calendar.MINUTE, datumZeit[4]);

	Bestellung b = new Bestellung();
	b.setBezahlt(true);
	b.setVersandt(false);
	b.setBestellzeitpunkt(c.getTime());
	b.setZahlungsart(Zahlungsart.VORKASSE);
	
	Bestellung dbBest = bs.addBestellung(b);
	
	Artikel a = new Artikel();
	a.setKurzbezeichnung("kurzbezeichnung1");
	a.setBeschreibung("beschreibung1");
	a.setPreis(new BigDecimal(String.valueOf(10)));
	a.setKategorie(ArtikelKategorie.Poster);
	
	artikelDao.persist(a);
	
	BestellPosition bp = new BestellPosition();
	bp.setArtikel(a);
	bp.setBestellung(dbBest);
	bp.setMenge(1);

	
	int i = bestellpositionDao.findAll().size();
	
	bs.addBestellposition(bp);
			
	List<BestellPosition> list = bestellpositionDao.findAll();

	assertThat(list.size(), greaterThan(i));

    }

  
}
