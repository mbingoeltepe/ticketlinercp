package at.ticketline.dao;

import java.util.List;

import junit.framework.Assert;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

import at.ticketline.dao.interfaces.MitarbeiterDao;
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Berechtigung;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.LoginStatus;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.test.AbstractTestCaseBase;
import at.ticketline.test.TestUtil;

/**
 * @author Georg Fuderer
 * 
 */
public class MitarbeiterDaoTest extends AbstractTestCaseBase {

    private static MitarbeiterDao mitarbeiterDao = null;
    
    @Override
    public IDataSet getDataSet() {
	return getDataSet("MitarbeiterDaoTest.xml");
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
    
    @Before
    public void before() throws Exception {
	this.onSetup(); // vorher angebene SetUp-Operation wird ausgefuehrt
    }
    
    @After
    public void after() throws Exception {
	this.onTearDown(); //vorher angegebene TearDown-Operation wird ausgefuehrt
    }
    
    @BeforeClass
    public static void setUpBeforeClass() {
	mitarbeiterDao = (MitarbeiterDao) DaoFactory
		.findDaoByEntity(Mitarbeiter.class);
    }
    
    @Test
    public void testCreateMitarbeiter() {
	Adresse adresse = new Adresse();
	adresse.setLand("Österreich");
	adresse.setOrt("Graz");
	adresse.setPlz("8010");
	adresse.setStrasse("Uhrturmplatzl 123");

	Mitarbeiter testUser = new Mitarbeiter();
	testUser.setId(2);
	testUser.setUsername("bepi");
	testUser.setPasswort("bepi");
	testUser.setVorname("Josef");
	testUser.setNachname("Sturm");
	testUser.setAdresse(adresse);
	testUser.setBerechtigung(Berechtigung.VERKAUF);
	testUser.setGeschlecht(Geschlecht.MAENNLICH);

	Assert.assertNotNull(mitarbeiterDao.persist(testUser));

	List<Mitarbeiter> mitarbeiterList = mitarbeiterDao.findAll();
	Mitarbeiter mitarbeiterFromDB = null;

	for (Mitarbeiter mitarbeiter : mitarbeiterList) {
	    if (mitarbeiter.getVorname().equals(testUser.getVorname())
		    && mitarbeiter.getNachname().equals(testUser.getNachname())) {
		mitarbeiterFromDB = mitarbeiter;
	    }
	}

	Assert.assertEquals(testUser.getVorname(),
		mitarbeiterFromDB.getVorname());
	Assert.assertEquals(testUser.getNachname(),
		mitarbeiterFromDB.getNachname());
    }

    /**
     * Test method for
     * {@link at.ticketline.dao.jpa.MitarbeiterDaoJpa#logIn(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testLogInShouldSucceed() {
	Assert.assertNull(mitarbeiterDao.getLoggedInMitarbeiter());
	assertThat(mitarbeiterDao.logIn("anitarummel123", "pwd"), is(LoginStatus.SUCCEEDED));
	Assert.assertNotNull(mitarbeiterDao.getLoggedInMitarbeiter());
    }
    
    @Test
    public void testLoginShouldReturnWrongUsername() {
	assertThat(mitarbeiterDao.logIn("bepi", "bepi"), is(LoginStatus.WRONG_USERNAME));
    }
    
    @Test
    public void testLoginShouldReturnWrongPassword() {
	assertThat(mitarbeiterDao.logIn("anitarummel123", "wrong"), is(LoginStatus.WRONG_PASSWORD));
    }

    /**
     * Test method for
     * {@link at.ticketline.dao.jpa.MitarbeiterDaoJpa#logOut(at.ticketline.entity.Mitarbeiter)}
     * .
     */
    @Test
    public void testLogOutShouldReturnTrue() {
	Mitarbeiter testUser = new Mitarbeiter();
	testUser.setId(1);

	assertThat(mitarbeiterDao.logIn("anitarummel123", "pwd"), is(LoginStatus.SUCCEEDED));
	Assert.assertEquals(true, mitarbeiterDao.logOut(testUser));
    }
    
    @Test
    public void testLogOutShouldReturnFalse() {
	Mitarbeiter testUser = new Mitarbeiter();
	testUser.setId(2);

	assertThat(mitarbeiterDao.logIn("anitarummel123", "pwd"), is(LoginStatus.SUCCEEDED));
	Assert.assertEquals(false, mitarbeiterDao.logOut(testUser));
    }

    /**
     * Test method for
     * {@link at.ticketline.dao.jpa.MitarbeiterDaoJpa#getLoggedInMitarbeiter()}.
     */
    @Test
    public void testGetLoggedInMitarbeiter() {
	assertThat(mitarbeiterDao.logIn("anitarummel123", "pwd"), is(LoginStatus.SUCCEEDED));	
	assertThat(mitarbeiterDao.getLoggedInMitarbeiter().getUsername(), is("anitarummel123"));
    }
}
