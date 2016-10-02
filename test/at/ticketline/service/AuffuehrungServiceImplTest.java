package at.ticketline.service;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.notNullValue;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import at.ticketline.dao.interfaces.AuffuehrungDao;
import at.ticketline.dao.interfaces.SaalDao;
import at.ticketline.dao.interfaces.VeranstaltungDao;
import at.ticketline.entity.*;
import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AuffuehrungServiceImplTest {

    @Mock private AuffuehrungDao auffuehrungDao;
    @Mock private VeranstaltungDao veranstaltungDao;
    @Mock private SaalDao saalDao;
    
    @Before
    public void initMocks() {
	MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testDefaultConstructorShouldInitDaos() {
	AuffuehrungServiceImpl as = new AuffuehrungServiceImpl();
	
	assertThat(as.getAuffuehrungDao(), notNullValue());
	assertThat(as.getSaalDao(), notNullValue());
	assertThat(as.getVeranstaltungDao(), notNullValue());
    }
    
    @Test
    public void findAuffuehrungenShouldReturnList() throws ValidateException
    {
	AuffuehrungServiceImpl as = new AuffuehrungServiceImpl(auffuehrungDao, veranstaltungDao, saalDao);
	
	Integer[] datumZeit = {2010, 12, 24, 0, 0};
	
	Calendar c = new GregorianCalendar();
	c.clear();
	c.set(datumZeit[0], datumZeit[1], datumZeit[2], datumZeit[3], datumZeit[4]);
	
	Auffuehrung a = new Auffuehrung();
	a.setDatumuhrzeit(c.getTime());
	
	as.findAuffuehrungen(a);
	
	verify(auffuehrungDao).findByAuffuehrung(argThat(new AuffuehrungArgumentMatcher(a)));
    }
    
    @Test(expected=ValidateException.class)
    public void findAuffuehrungenShouldThrowException() throws ValidateException {
	AuffuehrungServiceImpl as = new AuffuehrungServiceImpl(auffuehrungDao, veranstaltungDao, saalDao);
	
	Integer[] datumZeit = {2009, 12, 24, 0, 0};
	
	Calendar c = new GregorianCalendar();
	c.set(Calendar.YEAR, datumZeit[0]);
	c.set(Calendar.MONTH, datumZeit[1]);
	c.set(Calendar.DAY_OF_MONTH, datumZeit[2]);
	c.set(Calendar.HOUR_OF_DAY, datumZeit[3]);
	c.set(Calendar.MINUTE, datumZeit[4]);
	
	Auffuehrung a = new Auffuehrung();
	a.setDatumuhrzeit(c.getTime());
	
	as.findAuffuehrungen(a);
    }
    
    @Test
    public void findAllVeranstaltungenShouldReturnList() {
	AuffuehrungServiceImpl as = new AuffuehrungServiceImpl(auffuehrungDao, veranstaltungDao, saalDao);
	
	as.findAllVeranstaltungen();
	
	verify(veranstaltungDao).findAll();
    }
    
    @Test
    public void findAllSaeleShouldReturnList() {
	AuffuehrungServiceImpl as = new AuffuehrungServiceImpl(auffuehrungDao, veranstaltungDao, saalDao);
	
	as.findAllSaele();
	
	verify(saalDao).findAll();
    }
    
    class AuffuehrungArgumentMatcher extends ArgumentMatcher<Auffuehrung> {
	
	private Auffuehrung auffuehrung;
	public AuffuehrungArgumentMatcher(Auffuehrung a)
	{
	    this.auffuehrung = a;
	}

	@Override
	public boolean matches(Object o) {
	    if (o instanceof Auffuehrung) {
		Auffuehrung a = (Auffuehrung) o;
		
		Date datumUhrzeit = a.getDatumuhrzeit();
		PreisKategorie preis = a.getPreis();
		Veranstaltung veranstaltung = a.getVeranstaltung();
		Saal saal = a.getSaal();
		
		if(datumUhrzeit != null)
		    if(!datumUhrzeit.equals(auffuehrung.getDatumuhrzeit())) 
			return false;
		if(preis != null)
		    if(!a.getPreis().equals(auffuehrung.getPreis()))
			return false;
		if(veranstaltung != null)
			if(a.getVeranstaltung().getId() == auffuehrung.getVeranstaltung().getId()) 
			    return false;
		if(saal != null)
		    if(a.getSaal().getId() != auffuehrung.getSaal().getId()) 
			return false;
	    }
	    else {
		return false;
	    }
	    return true;
	}
    }
}
