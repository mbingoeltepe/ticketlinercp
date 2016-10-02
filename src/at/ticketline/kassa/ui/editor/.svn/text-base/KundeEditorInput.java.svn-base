package at.ticketline.kassa.ui.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.ticketline.entity.Kunde;

/**
 * @author MURAT BINGOLTEPE
 * 
 */
public class KundeEditorInput implements IEditorInput {
    protected Kunde kunde = null;

    public KundeEditorInput(Kunde k) {
	this.kunde = k;
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
	if (this.kunde.getId() == null) {
	    return "Neue Kunde";
	} else {
	    return this.kunde.getVorname() + " "
		    + this.kunde.getNachname();
	}
    }

    @Override
    public IPersistableElement getPersistable() {
	return null;
    }

    @Override
    public String getToolTipText() {
	return "Kunde bearbeiten";
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public Object getAdapter(Class adapter) {
	return null;
    }

    public void setKunde(Kunde k) {
	this.kunde = k;
    }

    public Kunde getKunde() {
	return this.kunde;
    }

    @Override
    public int hashCode() {
	return this.kunde.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if ((obj == null) || ((obj instanceof KundeEditorInput) == false)) {
	    return false;
	}
	Kunde k = ((KundeEditorInput) obj).getKunde();

	if ((k.getId() == null) || (this.kunde.getId() == null)) {
	    return false;
	}
	if (k.getId().equals(this.kunde.getId())) {
	    return true;
	} else {
	    return false;
	}
    }
}
