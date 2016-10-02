package at.ticketline.service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import at.ticketline.dao.interfaces.KundeDao;
import at.ticketline.entity.*;
import at.ticketline.service.interfaces.KundeService;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.notNullValue;

public class KundeServiceImplTest {
    
    private static KundeService ks;
    private static Kunde k;

    @Mock private KundeDao kundeDao;
    
    @BeforeClass
    public static void setUp() {
	k = new Kunde();
    }

    @Before
    public void initMocks() {
	MockitoAnnotations.initMocks(this);
	ks = new KundeServiceImpl(kundeDao);
    }
    
    @Test
    public void defaultConstructorShouldInitKundeDao() {
	KundeServiceImpl ks = new KundeServiceImpl();
	
	assertThat(ks.getKundeDao(), notNullValue());
    }
    
    @Test
    public void changeKundenDatenShouldInvokeDaoMethod() {
	ks.changeKundenDaten(k);
	
	verify(kundeDao).changeKundenDaten(k);
    }
    
    @Test
    public void sucheKundeShouldInvokeDaoMethod() {
	ks.sucheKunde(k);
	
	verify(kundeDao).findByKunde(k);
    }
    
    @Test
    public void addKundeToDbShouldInvokeDaoMethod() {
	ks.addKundeToDb(k);
	
	verify(kundeDao).insertKunde(k);
    }
    
    @Test
    public void sucheAlleKundeShouldInvokeDaoMethod() {
	ks.sucheAlleKunden();
	
	verify(kundeDao).findAll();
    }
    
    @Test
    public void removeKundeShouldInvokeDaoMethod() {
	ks.removeKunde(k);
	
	verify(kundeDao).remove(k);
    }
}