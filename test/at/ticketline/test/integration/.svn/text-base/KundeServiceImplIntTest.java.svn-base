package at.ticketline.test.integration;


import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.KundeDao;
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Kundengruppe;
import at.ticketline.service.KundeServiceImpl;
import at.ticketline.test.AbstractTestCaseBase;


/**
 * @author bernhard.sadransky
 * @author MURAT BINGOELTEPE
 *
 */
public class KundeServiceImplIntTest extends AbstractTestCaseBase {

    private static KundeServiceImpl ks;
    
    private static KundeDao kundeDao;   

    @Override
    public IDataSet getDataSet() {
	return getDataSet("KundeServiceImplIntTest.xml");
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
	ks = new KundeServiceImpl();
	kundeDao = (KundeDao) DaoFactory.findDaoByEntity(Kunde.class);
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
    public void kundenDatenShouldChanged()
    {	
	Kunde k_before = kundeDao.findById(1);
	kundeDao.refresh(k_before);
	
	k_before.setVorname("Harald");
	ks.changeKundenDaten(k_before);
	
	Kunde k_after = kundeDao.findById(1);
	assertThat(k_after.getVorname(), is("Harald"));
    }
    
    @Test
    public void sucheKundeShouldSearch()
    {
	Kunde k_temp = kundeDao.findById(1);
	kundeDao.refresh(k_temp);
	
	List<Kunde> kunden;
	Kunde k = new Kunde();
	k.setNachname("Sadransky");
	k.setVorname("Bernhard");
	k.setGeschlecht(Geschlecht.MAENNLICH);
	kunden = ks.sucheKunde(k);
	
	assertThat(kunden.size(), is(1));
	assertThat(kunden.get(0).getNachname(), is("Sadransky"));
	assertThat(kunden.get(0).getVorname(), is("Bernhard"));
	assertThat(kunden.get(0).getGeschlecht(), is(Geschlecht.MAENNLICH));
    }
    
    /**
     * Neue Kunde wird in die DB gespeichert.
     */
    @Test
    public void  addKundeToDbShouldPersist()
    {
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
	
	ks.addKundeToDb(newKunde);

	assertThat(kundeDao.findAll().size(), greaterThan(c));
    }
    
    /**
     * Alle Kunden werden gesucht.
     */
    @Test
    public void sucheAlleKundeShouldSearch()
    {	
	assertThat(ks.sucheAlleKunden().size(), is(1));
    }
    
    @Test
    public void removeKundeShouldRemove()
    {	
	Kunde k = kundeDao.findById(1);
	kundeDao.refresh(k);
	
	ks.removeKunde(k);

	assertThat(kundeDao.findAll().size(), is(0));
    }
}