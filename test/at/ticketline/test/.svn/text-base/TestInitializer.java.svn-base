package at.ticketline.test;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.persistence.jpa.PersistenceProvider;

import at.ticketline.dao.EntityManagerUtil;

/**
 * 
 * Initialisiert die Testumgebung.
 * Die init-Methode muss am Anfang jedes Testlaufs aufgerufen werden,
 * um die Testumgebung zu initialisieren.
 * 
 */
public class TestInitializer {
	
	private static boolean initialized = false;
	
	public static void init() {
		if (TestInitializer.initialized)
			return;
		EntityManagerUtil.init("ticketline", new PersistenceProvider());
		TestInitializer.initialized = true;
		
		BasicConfigurator.configure();
	}

}
