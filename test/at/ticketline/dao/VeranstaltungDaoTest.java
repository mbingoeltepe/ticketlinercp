package at.ticketline.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


import java.util.List;


import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import at.ticketline.dao.interfaces.VeranstaltungDao;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.test.AbstractTestCaseBase;

public class VeranstaltungDaoTest extends AbstractTestCaseBase {

    private static VeranstaltungDao veranstaltungDao = null;
    
    /* DataSet mit dem die Operationen durchgefuehrt werden */
    @Override
    public IDataSet getDataSet() {
	return getDataSet("VeranstaltungDaoTest.xml");
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
    public static void setUp() throws Exception {
	veranstaltungDao = (VeranstaltungDao) DaoFactory.findDaoByEntity(Veranstaltung.class);
    }
    
    @Before
    public void before() throws Exception {
	this.onSetup(); // vorher angebene SetUp-Operation wird ausgefuehrt
    }
    
    @After
    public void after() throws Exception {
	this.onTearDown(); //vorher angegebene TearDown-Operation wird ausgefuehrt
    }
    

    /**
     * Such Veranstaltung mit Bezeichnung aus der DB
     * Positiv Test / Normalfall
     */

    @Test
    public void testFindByVeranstaltung_mitBezeichnungDauer() {
	List<Veranstaltung> veranstaltungen;
	Veranstaltung v = new Veranstaltung();
	v.setBezeichnung("testveranstaltung");
	v.setDauer(new Integer(40));
	veranstaltungen = veranstaltungDao.findByVeranstaltung(v);
	
	assertThat(veranstaltungen.size(), is(1));
	assertThat(veranstaltungen.get(0).getBezeichnung(), is("testveranstaltung"));
    }
    
    /**
     * Such Veranstaltung mit Kategorie aus der DB
     * Positiv Test / Normalfall
     */

    @Test
    public void testFindByVeranstaltung_mitKategorie() {
	List<Veranstaltung> veranstaltungen;
	Veranstaltung v = new Veranstaltung();
	v.setKategorie("bla");
	veranstaltungen = veranstaltungDao.findByVeranstaltung(v);
	
	assertThat(veranstaltungen.size(), is(1));
	assertThat(veranstaltungen.get(0).getKategorie(), is("bla"));
    }
    
    /**
     * Such Veranstaltung mit Inhalt aus der DB
     * Positiv Test / Normalfall
     */

    @Test
    public void testFindByVeranstaltung_mitInhalt() {
	List<Veranstaltung> veranstaltungen;
	Veranstaltung v = new Veranstaltung();
	v.setInhalt("test*");
	veranstaltungen = veranstaltungDao.findByVeranstaltung(v);
	
	assertThat(veranstaltungen.size(), is(1));
	assertThat(veranstaltungen.get(0).getInhalt(), is("test test test"));
    }
    
    /**
     * Such Veranstaltung mit Inhalt aus der DB
     * Positiv Test / Normalfall
     */

    @Test
    public void testFindByVeranstaltung_shouldfindAll() {
	List<Veranstaltung> veranstaltungen;
	Veranstaltung v = new Veranstaltung();
	veranstaltungen = veranstaltungDao.findByVeranstaltung(v);
	
	assertThat(veranstaltungen.size(), is(2));
    }
}
