package at.ticketline.test.integration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.VeranstaltungServiceImpl;
import at.ticketline.test.AbstractTestCaseBase;

public class VeranstaltungServiceImplIntTest extends AbstractTestCaseBase {

    private static VeranstaltungServiceImpl vs=null;
    
    /* DataSet mit dem die Operationen durchgefuehrt werden */
    @Override
    public IDataSet getDataSet() {
	return getDataSet("VeranstaltungServiceImplIntTest.xml");
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
	vs = new VeranstaltungServiceImpl();
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
     * Sucht Veranstaltung mit Bezeichnung aus der DB
     * Positiv Test / Normalfall
     */

    @Test
    public void testFindByVeranstaltung_mitBezeichnungDauer() {
	List<Veranstaltung> veranstaltungen;
	Veranstaltung v = new Veranstaltung();
	v.setBezeichnung("test_veranstaltung_1");
	v.setDauer(new Integer(10));
	veranstaltungen = vs.findeVeranstaltungen(v);
	
	assertThat(veranstaltungen.size(), is(1));
	assertThat(veranstaltungen.get(0).getBezeichnung(), is("test_veranstaltung_1"));
    }
    
    /**
     * Sucht Veranstaltung mit Kategorie aus der DB
     * Positiv Test / Normalfall
     */

    @Test
    public void testFindByVeranstaltung_mitKategorie() {
	List<Veranstaltung> veranstaltungen;
	Veranstaltung v = new Veranstaltung();
	v.setKategorie("kategorie_1");
	veranstaltungen = vs.findeVeranstaltungen(v);
	
	assertThat(veranstaltungen.size(), is(1));
	assertThat(veranstaltungen.get(0).getKategorie(), is("kategorie_1"));
    }
    
    /**
     * Sucht Veranstaltung mit Inhalt aus der DB
     * Positiv Test / Normalfall
     */

    @Test
    public void testFindByVeranstaltung_mitInhalt() {
	List<Veranstaltung> veranstaltungen;
	Veranstaltung v = new Veranstaltung();
	v.setInhalt("test*");
	veranstaltungen = vs.findeVeranstaltungen(v);
	
	assertThat(veranstaltungen.size(), is(1));
	assertThat(veranstaltungen.get(0).getInhalt(), is("test_inhalt"));
	//assertThat(veranstaltungen.get(1).getInhalt(), is("hallo test hallo test"));
    }
    
    /**
     * Sucht Veranstaltung mit Inhalt aus der DB
     * Positiv Test / Normalfall
     */

    @Test
    public void testFindByVeranstaltung_shouldfindAll() {
	List<Veranstaltung> veranstaltungen;
	Veranstaltung v = new Veranstaltung();
	veranstaltungen = vs.findeVeranstaltungen(v);
	
	assertThat(veranstaltungen.size(), is(2));
    }
    
    /**
     * Sucht Veranstaltung mit falschen Werten
     * Negativ Test 
     */

    @Test
    public void testFindByVeranstaltung_shouldnotFindVeranstaltungen() {
	List<Veranstaltung> veranstaltungen=null;
	Veranstaltung v = new Veranstaltung();
	v.setBezeichnung("asdf_test_asdf");
	v.setKategorie("fdssdffds");
	veranstaltungen = vs.findeVeranstaltungen(v);
	
	assertThat(veranstaltungen.size(), is(0));
    }
}
