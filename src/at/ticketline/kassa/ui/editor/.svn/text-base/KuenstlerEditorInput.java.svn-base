package at.ticketline.kassa.ui.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.ticketline.entity.Kuenstler;

public class KuenstlerEditorInput implements IEditorInput {
    protected Kuenstler kuenstler = null;

    public KuenstlerEditorInput(Kuenstler k) {
	this.kuenstler = k;
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
	if (this.kuenstler.getId() == null) {
	    return "Neuer Künstler";
	} else {
	    return this.kuenstler.getVorname() + " "
		    + this.kuenstler.getNachname();
	}
    }

    @Override
    public IPersistableElement getPersistable() {
	return null;
    }

    @Override
    public String getToolTipText() {
	return "Künstler bearbeiten";
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public Object getAdapter(Class adapter) {
	return null;
    }

    public void setKuenstler(Kuenstler k) {
	this.kuenstler = k;
    }

    public Kuenstler getKuenstler() {
	return this.kuenstler;
    }

    @Override
    public int hashCode() {
	return this.kuenstler.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if ((obj == null) || ((obj instanceof KuenstlerEditorInput) == false)) {
	    return false;
	}
	Kuenstler k = ((KuenstlerEditorInput) obj).getKuenstler();

	if ((k.getId() == null) || (this.kuenstler.getId() == null)) {
	    return false;
	}
	if (k.getId().equals(this.kuenstler.getId())) {
	    return true;
	} else {
	    return false;
	}
    }
}
