package at.ticketline.kassa.ui.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.ticketline.entity.Auffuehrung;

public class SitzplanAnzeigeEditorInput implements IEditorInput {
    protected Auffuehrung auffuehrung = null;

    public SitzplanAnzeigeEditorInput(Auffuehrung a) {
	this.auffuehrung = a;
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
	return "Sitzplan";
    }

    @Override
    public IPersistableElement getPersistable() {
	return null;
    }

    @Override
    public String getToolTipText() {
	return "Sitzplan für eine Aufführung";
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public Object getAdapter(Class adapter) {
	return null;
    }

    public void setAuffuehrung(Auffuehrung a) {
	this.auffuehrung = a;
    }

    public Auffuehrung getAuffuehrung() {
	return this.auffuehrung;
    }

    @Override
    public int hashCode() {
	return this.auffuehrung.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if ((obj == null)
		|| ((obj instanceof SitzplanAnzeigeEditorInput) == false)) {
	    return false;
	}
	Auffuehrung a = ((SitzplanAnzeigeEditorInput) obj).getAuffuehrung();

	if ((a.getId() == null) || (this.auffuehrung.getId() == null)) {
	    return false;
	}
	if (a.getId().equals(this.auffuehrung.getId())) {
	    return true;
	} else {
	    return false;
	}
    }
}