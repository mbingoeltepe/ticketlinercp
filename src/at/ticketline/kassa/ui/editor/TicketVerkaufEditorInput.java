package at.ticketline.kassa.ui.editor;

import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Platz;
import at.ticketline.entity.Transaktion;

public class TicketVerkaufEditorInput implements IEditorInput {
    protected Kunde kunde = null;
    protected Auffuehrung auffuehrung = null;
    protected boolean verkauf = false;
    protected Transaktion transaktion = null;
   
    /**
     * Wird gerufen für neue Reservierungen und neue Verkäufe
     * @param a die Aufführung zu der die Reservierung/der Verkauf durchgeführt werden soll
     * @param k der Kunde, der reserviert/kauft
     * @param verkauf true: Es handelt sich um einen Verkauf
     *                false: ES handelt sich um eine Reservierung
     */
    public TicketVerkaufEditorInput(Auffuehrung a, Kunde k, boolean verkauf) {
	this.kunde = k;
	this.auffuehrung = a;
	this.verkauf = verkauf;
	this.transaktion = null;
    }
    
    /**
     * Wird nur für Transaktionen gerufen, die Reservierungen sind
     * @param t die Transaktion, die der Reservierung entspricht
     * @param verkauf true: Reservierung soll verkauft werden
     * 	              false: Reservierung soll nur geändert werden
     */
    public TicketVerkaufEditorInput(Transaktion t, boolean verkauf) {
	this.transaktion = t;
	this.kunde = transaktion.getKunde();
	if(this.kunde == null) this.kunde = new Kunde();
	Set<Platz> pset = transaktion.getPlaetze();
	for (Platz p : pset) {
	    this.auffuehrung = p.getAuffuehrung();
	    break;
	}
	this.verkauf = verkauf;
    }

    @Override
    public boolean exists() {
	return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
	return null;
    }

    @Override
    public String getName() {
	if (this.auffuehrung.getId() == null) {
	    return "Aufführung";
	} else {
	    return this.auffuehrung.getVeranstaltung().getBezeichnung();
	}
    }

    @Override
    public IPersistableElement getPersistable() {
	return null;
    }

    @Override
    public String getToolTipText() {
	return "Ticket reservieren/verkaufen";
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public Object getAdapter(Class adapter) {
	return null;
    }

    // public void setKunde(Kunde k) {
    // this.kunde = k;
    // }

    public Kunde getKunde() {
	return this.kunde;
    }

    public boolean isVerkauf() {
	return this.verkauf;
    }

    // public void setAuffuehrung(Auffuehrung a) {
    // this.auffuehrung = a;
    // }

    public Auffuehrung getAuffuehrung() {
	return this.auffuehrung;
    }
    
    public Transaktion getTransaktion() {
	return this.transaktion;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if ((obj == null)
		|| ((obj instanceof TicketVerkaufEditorInput) == false)) {
	    return false;
	}

	Kunde k = ((TicketVerkaufEditorInput) obj).getKunde();
	Auffuehrung a = ((TicketVerkaufEditorInput) obj).getAuffuehrung();
	boolean verkauf = ((TicketVerkaufEditorInput) obj).isVerkauf();

	if (a.getId().equals(this.auffuehrung.getId())
		&& (verkauf == this.verkauf)) {
	    if ((k.getId() == null) && (this.kunde.getId() == null)) {
		// selbe Aufführung und beides an anonyme Kunden
		return true;
	    } else {
		if ((k.getId() != null) && (this.kunde.getId() != null)) {
		    // beide kunden sind nicht null
		    if (k.getId().equals(this.kunde.getId())) {
			// selbe Aufführung und selber Kunde
			return true;
		    }
		}
	    }
	}
	return false;
    }

}
