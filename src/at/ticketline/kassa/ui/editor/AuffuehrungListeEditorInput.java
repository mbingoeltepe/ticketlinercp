package at.ticketline.kassa.ui.editor;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.util.Comparable;

/**
 * Klasse die für den Input für den AuffuerungListeEditor verantwortlich ist,
 * welcher die Liste der gefundenen Aufführungen zum Query-Objekt enthält
 * 
 * Muss unbedingt folgende Objekte beinhalten: listAuffuehrung: Eine ArrayListe
 * mit allen gefundenen Aufführungen zum query-Objekt query: Das Query-Objekt,
 * anhand dessen die Aufführungen gesucht wurden
 * 
 * @author Daniel
 * 
 */
public class AuffuehrungListeEditorInput implements IEditorInput {

    protected List<Auffuehrung> listAuffuehrung = null;
    protected Object query = null;

    /**
     * Konstruiert einen neuen AuffuehrungsListeEditorInput
     * 
     * @param listAuffuehrung
     *            Die Liste der anzuzeigenden Auffuehrungen
     * @param query
     *            Das Query-Objekt, zu dem die Aufführungen gefunden wurden, ist
     *            Element von Auffuehrung, Kuenstler, Veranstaltung oder Ort
     */
    public AuffuehrungListeEditorInput(List<Auffuehrung> listAuffuehrung,
	    Object query) {
	this.listAuffuehrung = listAuffuehrung;

	/*
	 * query wird benötigt zum anzeigen spezifischer Information (z.B. zu
	 * welcher Veranstaltung die gefundenen Auffuehrungen gehören
	 */
	this.query = query;
    }
    
    public List<Auffuehrung> getAuffuehrungen() {
	return this.listAuffuehrung;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
	// nothing to do
	return null;
    }

    @Override
    public boolean exists() {
	// nothing to do
	return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
	// nothing to do
	return null;
    }

    @Override
    public String getName() {
	if (this.query == null) {
	    return "Aufführungen";
	}
	if ((this.query instanceof Auffuehrung) == true) {
//	    Auffuehrung a = (Auffuehrung) query;
	    return "Aufführungen";
	} else if ((this.query instanceof Kuenstler) == true) {
	    Kuenstler k = (Kuenstler) query;
	    if (k.getNachname() != null) {
		return "Aufführungen von " + k.getNachname();
	    }
	    return "Aufführungen";
	} else if ((this.query instanceof Veranstaltung) == true) {
	    Veranstaltung v = (Veranstaltung) query;
	    if (v.getBezeichnung() != null) {
		return "Aufführungen zu " + v.getBezeichnung();
	    }
	    return "Aufführungen";
	} else if ((this.query instanceof Ort) == true) {
	    Ort o = (Ort) query;
	    if (o.getBezeichnung() != null) {
		return "Aufführungen in " + o.getBezeichnung();
	    }
	    return "Aufführungen";
	} else {
	    return "";
	}

    }

    @Override
    public IPersistableElement getPersistable() {
	// nothing to do
	return null;
    }

    @Override
    public String getToolTipText() {
	return "Liste der Auffuehrungen";
    }
    
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if ((obj == null) || ((obj instanceof AuffuehrungListeEditorInput) == false)) {
	    return false;
	}
	
	if (getClass().cast(obj).query instanceof Comparable && this.query instanceof Comparable) {
	    if (!((Comparable) ((AuffuehrungListeEditorInput) obj).query).equalsComparable((Comparable) this.query)) return false;
	}
	else {
	    if (((AuffuehrungListeEditorInput) obj).query != this.query) return false;
	}
	
//	List<Auffuehrung> liste = ((AuffuehrungListeEditorInput) obj).getAuffuehrungen();
//	if (liste == this.listAuffuehrung) return true;
//	if (this.listAuffuehrung.containsAll(liste) && liste.containsAll(this.listAuffuehrung)) return true;
	
	return true;
	
    }

}
