package at.ticketline.kassa;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.persistence.jpa.osgi.PersistenceProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.kassa.ui.editor.SitzplanAnzeigeEditor;
import at.ticketline.kassa.ui.editor.TicketVerkaufEditor;
import at.ticketline.log.EclipseRcpLogAppender;
import at.ticketline.util.SearchView;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "at.ticketline.kassa";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		EclipseRcpLogAppender.logger = Activator.getDefault().getLog();
		EclipseRcpLogAppender.pluginId = Activator.PLUGIN_ID;
		
		EntityManagerUtil.init("ticketline",new PersistenceProvider());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		DaoFactory.destroy();
		plugin = null;
		super.stop(context);

	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * Methode um den TicketView zu refreshen
	 */
	public void refreshSucheView() {
	    IViewReference[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
	    .getActivePage().getViewReferences();
	    IViewPart viewPart;
	    
	    for(int i = 0; i < views.length; i++) {
		viewPart = views[i].getView(true);
		if(viewPart instanceof SearchView) {
		    ((SearchView) viewPart).searchHandler();
		}
	    }
		
	}
	
	public void refreshEditors() {
	    IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
	    .getActivePage().getEditorReferences();
	    IEditorPart editorPart;
	    
	    for(int i = 0; i < editors.length; i++) {
		editorPart = editors[i].getEditor(true);
		if(editorPart instanceof SitzplanAnzeigeEditor) {
		    ((SitzplanAnzeigeEditor) editorPart).refreshSitzplan();
		} 
	    }
	}
	
	public void refreshPrice() {
	    IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
	    .getActivePage().getEditorReferences();
	    IEditorPart editorPart;
	    for(int i = 0; i < editors.length; i++) {
		editorPart = editors[i].getEditor(true);
		if (editorPart instanceof TicketVerkaufEditor) {
		    ((TicketVerkaufEditor) editorPart).drawTicketPrices();
		}
	    }
	}
}
