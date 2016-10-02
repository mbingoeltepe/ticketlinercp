package at.ticketline.entity;

import at.ticketline.entity.Veranstaltung;

/**
 * Business Object für die TopTen Auswhal
 * Enthält eine Veranstaltung und die Anzahl der gebuchten Plätze
 * @author Andrea Auer
 */
@SuppressWarnings("rawtypes")
public class TopTen implements Comparable {
	private Veranstaltung veranstaltung;
	private int gebuchtePlaetze;
	
	public TopTen(Veranstaltung v, int gebuchtePlaetze)
	{
		this.veranstaltung=v;
		this.gebuchtePlaetze=gebuchtePlaetze;
	}
	public Veranstaltung getVeranstaltung() {
		return veranstaltung;
	}
	public void setVeranstaltung(Veranstaltung veranstaltung) {
		this.veranstaltung = veranstaltung;
	}
	public int getGebuchtePlaetze() {
		return gebuchtePlaetze;
	}
	public void setGebuchtePlaetze(int gebuchtePlaetze) {
		this.gebuchtePlaetze = gebuchtePlaetze;
	}
	@Override
	public int compareTo(Object arg0) {
		int ret = ((TopTen)arg0).gebuchtePlaetze-this.gebuchtePlaetze;
		if(ret!=0)
			return ret;
		else return this.getVeranstaltung().getBezeichnung().compareTo(((TopTen)arg0).getVeranstaltung().getBezeichnung());
	}
	
	public String toString() {
	    return this.veranstaltung.getId()+" "+this.veranstaltung.getBezeichnung()+" Anzahl verkaufter Tickets: "+this.gebuchtePlaetze;
	    
	}
	
}
