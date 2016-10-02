package at.ticketline.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

import at.ticketline.dao.interfaces.KundeDao;
import at.ticketline.dao.interfaces.TransaktionDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.Platz;
import at.ticketline.entity.PlatzStatus;
import at.ticketline.entity.PreisKategorie;
import at.ticketline.entity.Transaktion;
import at.ticketline.entity.Transaktionsstatus;
import at.ticketline.entity.Zahlungsart;
import at.ticketline.service.interfaces.TransaktionService;

public class TransaktionServiceImplTest {
    
    @Mock private TransaktionDao transaktionDao;
    @Mock private KundeDao kundeDao;
    
    private Kunde k;
    private Mitarbeiter m;
    private Zahlungsart z;
    private Platz p1;
    private Platz p2;
    private Platz p3;
    private Set<Platz> plaetze;
    
    @Before
    public void setUp() {
	MockitoAnnotations.initMocks(this);

	k = new Kunde();
	m = new Mitarbeiter();
	z = Zahlungsart.BANKEINZUG;
	plaetze = new HashSet<Platz>();

	k.setId(1);
	m.setId(1);
	
	p1 = new Platz();
	p2 = new Platz();
	p3 = new Platz();
	
	p1.setNummer(1);
	p2.setNummer(2);
	p3.setNummer(3);
	
	plaetze.add(p1);
	plaetze.add(p2);
	plaetze.add(p3);
    }
    
    private TransaktionDao createMockedTransaktionDao(List<Transaktion> transaktionen) {
	TransaktionDao tdao = mock(TransaktionDao.class);
	when(tdao.findAll()).thenReturn(transaktionen);
	return tdao;
    }
    
    @Test
    public void defaultConstructorShouldInitDaos() {
	TransaktionServiceImpl ts = new TransaktionServiceImpl();
	
	assertThat(ts.getTransaktionDao(), notNullValue());
	assertThat(ts.getKundeDao(), notNullValue());
    }
    
    @Test
    public void createBuchungShouldPersistTransaktion() {
	TransaktionService ts = new TransaktionServiceImpl(transaktionDao, kundeDao);

	ts.createBuchung(z, k, m, plaetze);
	
	Transaktion t = new Transaktion();
	t.setZahlungsart(z);
	t.setKunde(k);
	t.setMitarbeiter(m);
	t.setStatus(Transaktionsstatus.BUCHUNG);
	
	for(Platz p : plaetze)
	    p.setStatus(PlatzStatus.GEBUCHT);
	
	t.setPlaetze(plaetze);
	
	verify(transaktionDao).persist(argThat(new TransaktionMatcher(t)));
    }
    
    @Test
    public void createReservierungShouldPersistTransaktionWithResNr() {
	TransaktionService ts = new TransaktionServiceImpl(transaktionDao, kundeDao);
	
	ts.createReservierung(z, k, m, plaetze);

	Transaktion t = new Transaktion();
	t.setZahlungsart(z);
	t.setKunde(k);
	t.setMitarbeiter(m);
	t.setStatus(Transaktionsstatus.RESERVIERUNG);	
	
	for(Platz p : plaetze)
	    p.setStatus(PlatzStatus.RESERVIERT);
	
	t.setPlaetze(plaetze);

	TransaktionMatcher tm = new TransaktionMatcher(t);	
	
	verify(transaktionDao).persist(argThat(tm));
	verify(transaktionDao).refresh(argThat(tm));
	verify(transaktionDao).merge(argThat(tm));
    }
    
    @Test
    public void changeReservierungShouldMergeTransaktion() {
	TransaktionService ts = new TransaktionServiceImpl(transaktionDao, kundeDao);
	
	ts.createReservierung(z, k, m, plaetze);

	Transaktion t = new Transaktion();
	t.setZahlungsart(z);
	t.setKunde(k);
	t.setMitarbeiter(m);
	t.setStatus(Transaktionsstatus.RESERVIERUNG);	
	
	ts.changeReservierung(t);
	
	TransaktionMatcher tm = new TransaktionMatcher(t);
	
	verify(transaktionDao).merge(argThat(tm));
    }
    
    @Test
    public void findTransaktionbyResNrShouldInvokeDaoMethod() {
	TransaktionService ts = new TransaktionServiceImpl(transaktionDao, kundeDao);
	
	ts.findTransaktionByResNr(10);
	
	verify(transaktionDao).findByResNr(10);
    }
    
    @Test
    public void findTransaktionbyKundeundAuffuehrungShouldReturnTransaktion() {
	TransaktionService ts = new TransaktionServiceImpl(transaktionDao, kundeDao);
	
	Auffuehrung a = new Auffuehrung();
	a.setDatumuhrzeit(new Date());
	a.setPreis(PreisKategorie.MAXIMALPREIS);
	a.setPlaetze(plaetze);
	
	p1.setAuffuehrung(a);
	p2.setAuffuehrung(a);
	p3.setAuffuehrung(a);
	
	Transaktion t1 = new Transaktion();
	t1.setZahlungsart(z);
	t1.setKunde(k);
	t1.setMitarbeiter(m);
	t1.setStatus(Transaktionsstatus.RESERVIERUNG);	

	p2.setTransaktion(t1);
	p2.setStatus(PlatzStatus.RESERVIERT);
	
	Transaktion t2 = new Transaktion();
	t2.setZahlungsart(Zahlungsart.KREDITKARTE);
	t2.setMitarbeiter(m);
	t2.setStatus(Transaktionsstatus.STORNO);
	t2.setKunde(k);
	
	HashSet<Platz> plaetze2 = new HashSet<Platz>();
	plaetze2.add(p2);
	
	t1.setPlaetze(plaetze2);
	
	HashSet<Transaktion> transaktionen = new HashSet<Transaktion>();
	transaktionen.add(t1);
	transaktionen.add(t2);
	
	k.setTransaktionen(transaktionen);
	
	List<Transaktion> t_list = ts.findTransaktionByKundeUndAuffuehrung(k, a);
	assertThat(t_list.size(), is(1));
	TransaktionMatcher tm = new TransaktionMatcher(t1);
	Transaktion t = t_list.get(0);
	assertThat(tm.matches(t), is(true));
    }
    
    @Test
    public void findTransaktionByAnonymUndAuffuehrungShouldReturnTransaktion() {	
	Auffuehrung a = new Auffuehrung();
	a.setId(1);
	p1.setAuffuehrung(a);
	p2.setAuffuehrung(a);
	p3.setAuffuehrung(a);
	a.setPlaetze(plaetze);
	
	HashSet<Platz> plaetze2 = new HashSet<Platz>();
	plaetze2.add(p2);
	
	HashSet<Platz> plaetze3 = new HashSet<Platz>();
	plaetze3.add(p1);
	plaetze3.add(p3);
	
	Transaktion t1 = new Transaktion();
	t1.setId(1);
	t1.setKunde(k);
	t1.setPlaetze(plaetze2);
	Transaktion t2 = new Transaktion();
	t2.setId(2);
	t2.setPlaetze(plaetze3);
	
	List<Transaktion> l_trans = new ArrayList<Transaktion>();
	l_trans.add(t1);
	l_trans.add(t2);
	
	TransaktionService ts = new TransaktionServiceImpl(createMockedTransaktionDao(l_trans), kundeDao);
	
	List<Transaktion> t_anonym = ts.findTransaktionByAnonymUndAuffuehrung(a);
	
	assertThat(t_anonym.size(), is(1));
	assertThat(t_anonym.get(0).getId(), is(2));
    }
    
    @Test
    public void findTransaktionbyKundeundAuffuehrungShouldBeEmpty() {
	TransaktionService ts = new TransaktionServiceImpl(transaktionDao, kundeDao);
	
	Auffuehrung a = new Auffuehrung();
	a.setDatumuhrzeit(new Date());
	a.setPreis(PreisKategorie.MAXIMALPREIS);
	a.setPlaetze(plaetze);
	
	Transaktion t = new Transaktion();
	t.setZahlungsart(z);
	t.setKunde(k);
	t.setMitarbeiter(m);
	t.setStatus(Transaktionsstatus.RESERVIERUNG);	

	p2.setTransaktion(t);
	p2.setStatus(PlatzStatus.RESERVIERT);
	
	HashSet<Platz> plaetze2 = new HashSet<Platz>();
	plaetze2.add(p2);
	
	t.setPlaetze(plaetze2);
	
	Kunde k2 = new Kunde();
	k2.setId(2);
	
	List<Transaktion> t2 = ts.findTransaktionByKundeUndAuffuehrung(k2, a);
	
	assertThat(t2.isEmpty(), is(true));
    }
    
    @Test
    public void storniereTransaktionShouldInvokeDaoRemove() {
	TransaktionService ts = new TransaktionServiceImpl(transaktionDao, kundeDao);
	
	Transaktion t = new Transaktion();
	t.setZahlungsart(z);
	t.setKunde(k);
	t.setMitarbeiter(m);
	t.setStatus(Transaktionsstatus.RESERVIERUNG);
	t.setPlaetze(plaetze);
	
	ts.storniereTransaktion(t);
	
	verify(transaktionDao).remove(t);
    }
    
    @Test
    public void verkaufeReservierteTicketsShouldCallMergeWithPlaetzeGebucht() {
	TransaktionService ts = new TransaktionServiceImpl(transaktionDao, kundeDao);
	
	Transaktion t = new Transaktion();
	t.setZahlungsart(z);
	t.setKunde(k);
	t.setMitarbeiter(m);
	t.setStatus(Transaktionsstatus.RESERVIERUNG);
	t.setPlaetze(plaetze);
	
	p1.setStatus(PlatzStatus.RESERVIERT);
	p2.setStatus(PlatzStatus.RESERVIERT);
	p3.setStatus(PlatzStatus.RESERVIERT);
	
	p1.setTransaktion(t);
	p2.setTransaktion(t);
	p3.setTransaktion(t);
	
	Transaktion t2 = new Transaktion();
	t2.setZahlungsart(z);
	t2.setKunde(k);
	t2.setMitarbeiter(m);
	t2.setStatus(Transaktionsstatus.BUCHUNG);
	
	Platz p4 = new Platz();
	Platz p5 = new Platz();
	Platz p6 = new Platz();

	p4.setNummer(1);
	p5.setNummer(2);
	p6.setNummer(3);
	
	p4.setStatus(PlatzStatus.GEBUCHT);
	p5.setStatus(PlatzStatus.GEBUCHT);
	p6.setStatus(PlatzStatus.GEBUCHT);
	
	p4.setTransaktion(t);
	p5.setTransaktion(t);
	p6.setTransaktion(t);
	
	HashSet<Platz> plaetze2 = new HashSet<Platz>();
	plaetze2.add(p4);
	plaetze2.add(p5);
	plaetze2.add(p6);
	
	t2.setPlaetze(plaetze2);

	TransaktionMatcher tm = new TransaktionMatcher(t2);
	
	ts.verkaufeReservierteTickets(t);
	
	verify(transaktionDao).merge(argThat(tm));
    }
    
    class TransaktionMatcher extends ArgumentMatcher<Transaktion> {
	
	private Transaktion transaktion;
	public TransaktionMatcher(Transaktion t) {
	    this.transaktion = t;
	}

	@Override
	public boolean matches(Object o) {
	    if (o instanceof Transaktion) {
		Transaktion t = (Transaktion) o;
		
		Zahlungsart z = t.getZahlungsart();
		Kunde k = t.getKunde();
		Mitarbeiter m = t.getMitarbeiter();
		Transaktionsstatus ts = t.getStatus();
		Set<Platz> plaetze = t.getPlaetze();
			
		if(z != null)
		    if(!z.equals(transaktion.getZahlungsart())) 
			return false;
		
		if(k != null)
		    if(!k.getId().equals(transaktion.getKunde().getId()))
			return false;

		if(m != null)
		    if(!m.getId().equals(transaktion.getMitarbeiter().getId()))
			return false;

		if(ts != null)
		    if(!ts.equals(transaktion.getStatus()))
			return false; 

		if(transaktion.getPlaetze().size() == plaetze.size()) {
		    if(!matchTransaktionInPlatz(t))
			return false;
		    else
			return matchPlaetze(plaetze);
		}
		else
		    return false;
	    }
	    else {
		return false;
	    }	    
	}
	
	private boolean matchTransaktionInPlatz(Transaktion t) {
	    Set<Platz> plaetze = t.getPlaetze();
	    
	    for(Platz p : plaetze)
		if(p.getTransaktion() != t)
		    return false;
	    return true;
	}
	
	private boolean matchPlaetze(Set<Platz> plaetze) {
	    Set<Integer> plaetze_nummer = new HashSet<Integer>();
	    Set<Integer> plaetze_nummer_this = new HashSet<Integer>();
	    Transaktionsstatus ts = this.transaktion.getStatus();
	    PlatzStatus ps = null;
	    
	    if(ts.equals(Transaktionsstatus.BUCHUNG))
		ps = PlatzStatus.GEBUCHT;
	    if(ts.equals(Transaktionsstatus.RESERVIERUNG))
		ps = PlatzStatus.RESERVIERT;
	    
	    for(Platz p : plaetze)
		if(!p.getStatus().equals(ps))
		    return false;
	      
	    for(Platz p : plaetze)
		plaetze_nummer.add(p.getNummer());
	    
	    for(Platz p : this.transaktion.getPlaetze())
		plaetze_nummer_this.add(p.getNummer());
	    
	    return plaetze_nummer_this.equals(plaetze_nummer);
	}
    }
}
