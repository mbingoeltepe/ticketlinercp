package at.ticketline.test.integration;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.MitarbeiterDao;
import at.ticketline.entity.LoginStatus;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.service.MitarbeiterServiceImpl;
import at.ticketline.service.interfaces.MitarbeiterService;
import at.ticketline.test.AbstractTestCaseBase;
import at.ticketline.test.TestUtil;


/**
 * @author Georg Fuderer
 * 
 */
public class MitarbeiterServiceImplIntTest extends AbstractTestCaseBase {

    private static MitarbeiterDao mitarbeiterDao;
    private static MitarbeiterService mitarbeiterService;
    
    @Override
    public IDataSet getDataSet() {
	return getDataSet("MitarbeiterServiceImplIntTest.xml");
    }
    
    @Override
    public DatabaseOperation getSetUpOperation() {
	return DatabaseOperation.CLEAN_INSERT;
    }
    
    @Override
    public DatabaseOperation getTearDownOperation() {
	return DatabaseOperation.NONE;
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
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() {
	mitarbeiterDao = (MitarbeiterDao) DaoFactory.findDaoByEntity(Mitarbeiter.class);
	mitarbeiterService = new MitarbeiterServiceImpl();
    }

    /**
     * Test method for
     * {@link at.ticketline.service.MitarbeiterServiceImpl#logIn(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testLogIn() {
	Mitarbeiter m = mitarbeiterDao.findById(1);

	mitarbeiterService.logOut();

	Assert.assertEquals(
		LoginStatus.SUCCEEDED,
		mitarbeiterService.logIn(m.getUsername(),
			m.getPasswort()));

	mitarbeiterService.logOut();
    }

    /**
     * Test method for
     * {@link at.ticketline.service.MitarbeiterServiceImpl#logOut()}.
     */
    @Test
    public void testLogOut() {
	Mitarbeiter m = mitarbeiterDao.findById(1);

	mitarbeiterService
		.logIn(m.getUsername(), m.getPasswort());

	Assert.assertEquals(true, mitarbeiterService.logOut());
    }

    /**
     * Test method for
     * {@link at.ticketline.service.MitarbeiterServiceImpl#getLoggedInMitarbeiter()}
     * .
     */
    @Test
    public void testGetLoggedInMitarbeiter() {
	Mitarbeiter m = mitarbeiterDao.findById(1);

	mitarbeiterService
		.logIn(m.getUsername(), m.getPasswort());

	Assert.assertEquals(m,
		mitarbeiterService.getLoggedInMitarbeiter());
    }

    /**
     * Test method for
     * {@link at.ticketline.service.MitarbeiterServiceImpl#isLoggedIn()}.
     */
    @Test
    public void testIsLoggedIn() {
	Mitarbeiter m = mitarbeiterDao.findById(1);

	mitarbeiterService
		.logIn(m.getUsername(), m.getPasswort());

	Assert.assertEquals(true, mitarbeiterService.isLoggedIn());
    }

}
