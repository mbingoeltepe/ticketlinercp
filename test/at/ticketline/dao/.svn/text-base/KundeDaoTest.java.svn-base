package at.ticketline.dao;

import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.greaterThan;

import at.ticketline.dao.interfaces.KundeDao;
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Kundengruppe;
import at.ticketline.test.AbstractTestCaseBase;
import at.ticketline.test.TestUtil;

public class KundeDaoTest extends AbstractTestCaseBase {

    private static KundeDao kundeDao = null;
    
    /* DataSet mit dem die Operationen durchgefuehrt werden */
    @Override
    public IDataSet getDataSet() {
	return getDataSet("KundeDaoTest.xml");
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
	kundeDao = (KundeDao) DaoFactory.findDaoByEntity(Kunde.class);
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
    public void testChangeKunde() {
	Kunde k_before = kundeDao.findById(1);
	kundeDao.refresh(k_before);
	
	k_before.setVorname("Harald");
	kundeDao.changeKundenDaten(k_before);
	
	Kunde k_after = kundeDao.findById(1);
	assertThat(k_after.getVorname(), is("Harald"));
    }

    @Test(expected = DaoException.class)
    public void testChangeKunde_DaoException() {
	Kunde k = new Kunde();
	
	k.setNachname("TESTtest");
	kundeDao.changeKundenDaten(k);
    }

//    @Test
//    public void testInsertKunde_neuerKunde() {
////	Ort o= new Ort();
////	Adresse a = new Adresse();
////	a.setOrt("hb");
////	a.setPlz("3454");    private static List<Kunde> kunden = null;
////	a.setStrasse("st");
////	o.setAdresse(a);
////	k2.setId(3333);
//	k2.setNachname("KundeTest");
//	k2.setVorname("Test");
//	kundeDao.persist(k2);
//	//kundeDao.insertKunde(k2);
//	kunden = kundeDao.findByKunde(k2);
//	int kundeAnz = kunden.size();
//	for (int i = 0; i < kunden.size(); i++) {
//	//    assertEquals(k2.getNachname(), kunden.get(i).getNachname());
//	}
//	kunden = kundeDao.findAll();
////	assertEquals(kunden.size(), kundeAnz + 1);
//    }
//
//    @Test(expected = DaoException.class)
//    public void testInsertKunde_Excpetion() {
//	kunden = kundeDao.findAll();
//	k1 = kunden.get(0);
//	kundeDao.insertKunde(k2);
//
//    }

    @Test
    public void testFindByKunde() {
	Kunde k_temp = kundeDao.findById(1);
	kundeDao.refresh(k_temp);
	
	List<Kunde> kunden;
	Kunde k = new Kunde();
	k.setNachname("Sadransky");
	k.setVorname("Bernhard");
	k.setGeschlecht(Geschlecht.MAENNLICH);
	kunden = kundeDao.findByKunde(k);
	
	assertThat(kunden.size(), is(1));
	assertThat(kunden.get(0).getNachname(), is("Sadransky"));
	assertThat(kunden.get(0).getVorname(), is("Bernhard"));
	assertThat(kunden.get(0).getGeschlecht(), is(Geschlecht.MAENNLICH));
    }
    
    @Test
    public void insertKundeShouldPersistKunde() {
	Adresse adresse = new Adresse();
	adresse.setStrasse("Linzer str.");
	adresse.setPlz("3100");
	adresse.setOrt("St.Polten");
	adresse.setLand("Österreich");
	
	Kunde newKunde = new Kunde();

	newKunde.setNachname("X");
	newKunde.setVorname("Y");
	newKunde.setAdresse(adresse);
	newKunde.setGeschlecht(Geschlecht.WEIBLICH);
	newKunde.setGesperrt(false);
	newKunde.setGruppe(Kundengruppe.VIP);
	
	int c = kundeDao.findAll().size();
	kundeDao.insertKunde(newKunde);
	assertThat(kundeDao.findAll().size(), greaterThan(c));
    }
}
