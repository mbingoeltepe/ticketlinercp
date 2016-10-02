package at.ticketline.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.interfaces.KuenstlerDao;
import at.ticketline.dao.interfaces.KundeDao;
import at.ticketline.dao.interfaces.MitarbeiterDao;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.LoginStatus;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.test.TestInitializer;

public class ValidationTest {

    @BeforeClass
    public static void init() {
	TestInitializer.init();
    }

    @Test
    public void testPersistKuenstler() {
	KuenstlerDao kuenstlerDao = (KuenstlerDao) DaoFactory
		.findDaoByEntity(Kuenstler.class);
	Kuenstler k = new Kuenstler();

	try {
	    kuenstlerDao.persist(k);
	    fail("Kuenstler konnte trotz Fehlern persistiert werden");
	} catch (ConstraintViolationException cve) {
	    List<String> violations = new ArrayList<String>();
	    for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
		violations.add(cv.getPropertyPath().toString());
	    }
	    assertEquals(3, violations.size());
	    assertTrue(violations.contains("nachname"));
	    assertTrue(violations.contains("vorname"));
	    assertTrue(violations.contains("geschlecht"));
	}
    }
}
