package at.ticketline.kassa.ui.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class SitzplanEditorInput implements IEditorInput {

    @SuppressWarnings({ "rawtypes" })
    @Override
    public Object getAdapter(Class adapter) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean exists() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getName() {
	// TODO Auto-generated method stub
	return "Sitzplan";
    }

    @Override
    public IPersistableElement getPersistable() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getToolTipText() {
	// TODO Auto-generated method stub
	return "Sitzplan bearbeiten";
    }

}
