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

import at.ticketline.dao.interfaces.TransaktionDao;
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Kundengruppe;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.Transaktion;
import at.ticketline.service.ValidateException;
import at.ticketline.test.AbstractTestCaseBase;
import at.ticketline.test.TestUtil;

/**
 * In diesem JUnitTest werden alle Referensen(Kunde, Mitarbeiter) von
 * Transaktion im Laufen bringen. Als beispiel werden 1 Transaktion in der
 * Datenbank gespeichert. Werden die gespeicherte Transaktion gesucht.
 * 
 * @author MURAT BINGOELTEPE
 * @author Bernhard Sadransky
 */
public class TransaktionDaoTest extends AbstractTestCaseBase {

    private static  TransaktionDao transaktionDao = null;

    @Override
    public IDataSet getDataSet() {
	return getDataSet("TransaktionDaoTest.xml");
    }

    @Override
    public DatabaseOperation getSetUpOperation() {
	return DatabaseOperation.CLEAN_INSERT;
    }

    @Override
    public DatabaseOperation getTearDownOperation() {
	return DatabaseOperation.DELETE_ALL;
    }

    @BeforeClass
    public static void setUp() throws Exception {	
	transaktionDao = (TransaktionDao) DaoFactory.findDaoByEntity(Transaktion.class);
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
    public void refreshShouldUpdateEntityId() {
	List<Transaktion> transaktion = transaktionDao.findAll();
	Transaktion t = new Transaktion();	
	t = transaktion.get(0); 

	int oldID = t.getId();

	transaktionDao.persist(t);
	transaktionDao.refresh(t);

	assertThat(t.getId(), is(oldID));
    }

    /**
     * Hier wird eine Transaktion von der Datenbank gesucht.
     * Positiver Test / Normal Fall
     */
    @Test
    public void testFindByTransaktionMitEingabe()
    throws ValidateException {

	// Adresse für Mitarbeiter und Kunde
	Adresse adresse = new Adresse();
	adresse.setStrasse("Strasse1");
	adresse.setOrt("Ort1");
	adresse.setPlz("1001");

	// Mitarbeiter
	Mitarbeiter mitarbeiter = new Mitarbeiter();

	mitarbeiter.setUsername("User Mitarbeiter");
	mitarbeiter.setPasswort("Passwort Mitarbeiter");
	mitarbeiter.setVorname("Vorname Mitarbeiter");
	mitarbeiter.setNachname("Nachname Mitarbeiter");
	mitarbeiter.setGeschlecht(Geschlecht.MAENNLICH);
	mitarbeiter.setAdresse(adresse);

	// Kunde
	Kunde kunde = new Kunde();
	kunde.setVorname("Vorname Kunde");
	kunde.setNachname("Nachname Kunde");
	kunde.setGeschlecht(Geschlecht.WEIBLICH);
	kunde.setGruppe(Kundengruppe.STANDARD);
	kunde.setAdresse(adresse);

	List<Transaktion> transaktion = transaktionDao.findAll();

	// Kunde und Mitarbeiter werden in die Transaktion gespecihert.
	Transaktion t = new Transaktion();
	t = transaktion.get(0);
	t.setKunde(kunde);
	t.setMitarbeiter(mitarbeiter);

	// Transaktion wird aktualisiert
	transaktionDao.merge(t);

	// findByTransaktion
	List<Transaktion> list = transaktionDao
	.findByTransaktion(t);

	assertThat(list.size(),is(1));
	assertEquals(list.get(0).getDatumuhrzeit(),
		t.getDatumuhrzeit());
	assertEquals(list.get(0).getReservierungsnr(),
		t.getReservierungsnr());
	assertEquals(list.get(0).getZahlungsart(),
		t.getZahlungsart());
	assertEquals(list.get(0).getStatus(),
		t.getStatus());
	assertEquals(list.get(0).getMitarbeiter(),
		t.getMitarbeiter());
	assertEquals(list.get(0).getKunde(), t.getKunde());

    }

    /**
     * Hier wird, wenn keine Eingabe eingegeben wurden. Muss alle Transaktionen
     * zurück gelifiert Sondern Fall
     */
    @Test
    public void testFindByTransaktionOhneEingabe() throws ValidateException {

	Transaktion transaktionOhneEingabe = new Transaktion();

	List<Transaktion> transaktionList = transaktionDao
	.findByTransaktion(transaktionOhneEingabe);

	assertEquals(transaktionList.size(), 1);

    }

    /**
     * Wir haben in der DB nur eine Transaktion mit Reservierungsnr=1234
     * gespecihert. Jetzt suchen wir diese Transaktion mit 1111 
     * Es muss eine Fehlermeldung werfen werden.zB("Keine Transaktionen mit dieser Reservierungsnr gefunden.")
     * Negativer Test / Fehler Fall
     */
    @Test
    public void testFindByTransaktionMitFalscheWerte() throws ValidateException {

	Transaktion t = new Transaktion();

	t.setReservierungsnr(1111);
	t.setDatumuhrzeit(null);
	t.setKunde(null);
	t.setMitarbeiter(null);


	try {

	    // Transaktion t wird von der Datenbank gesucht.
	    List<Transaktion> transaktionList = transaktionDao
	    .findByTransaktion(t);

	    assertTrue(transaktionList.isEmpty());
	    assertEquals(transaktionList.size(), 0);

	    if (transaktionList.size() == 0) {
		throw new IllegalArgumentException(
		"Sollte eine IllegalArgumentException werfen");
	    }

	} catch (IllegalArgumentException expected) {

	}

    }

    /**
     * Wir haben in der DB nur eine Transaktion mit Reservierungsnr=1234
     * gespecihert. Jetzt suchen wir diese Transaktion
     */
    @Test
    public void findByResNr() throws ValidateException {


	Transaktion t = transaktionDao.findByResNr(1234);

	assertThat(t.getReservierungsnr(), is(1234));

    }

}
