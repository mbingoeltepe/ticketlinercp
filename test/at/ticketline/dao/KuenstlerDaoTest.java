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

import at.ticketline.dao.interfaces.KuenstlerDao;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Kunde;
import at.ticketline.test.AbstractTestCaseBase;

public class KuenstlerDaoTest extends AbstractTestCaseBase {
   
    private static KuenstlerDao kuenstlerDao = null;
    
    /* DataSet mit dem die Operationen durchgefuehrt werden */
    @Override
    public IDataSet getDataSet() {
	return getDataSet("KuenstlerDaoTest.xml");
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
	kuenstlerDao = (KuenstlerDao) DaoFactory.findDaoByEntity(Kuenstler.class);
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
    public void testFindByKuenstler() {
	Kuenstler k_temp = kuenstlerDao.findById(1);
	kuenstlerDao.refresh(k_temp);
	
	List<Kuenstler> kuenstler;
	Kuenstler k = new Kuenstler();
	k.setNachname("Muster");
	k.setVorname("Thomas");
	k.setGeschlecht(Geschlecht.MAENNLICH);
	k.setTitel("Dr");
	kuenstler = kuenstlerDao.findByKuenstler(k);
	
	assertThat(kuenstler.size(), is(1));
	assertThat(kuenstler.get(0).getNachname(), is("Muster"));
	assertThat(kuenstler.get(0).getVorname(), is("Thomas"));
	assertThat(kuenstler.get(0).getGeschlecht(), is(Geschlecht.MAENNLICH));
    }
}
