package at.ticketline.kassa.ui.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import at.ticketline.dao.DaoException;
import at.ticketline.entity.Kunde;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.kassa.ui.view.KundeSucheView;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.KundeServiceImpl;
import at.ticketline.service.MitarbeiterServiceImpl;
import at.ticketline.service.interfaces.KundeService;
import at.ticketline.service.interfaces.MitarbeiterService;
import at.ticketline.util.EasyMessage;

/**
 * @author MURAT BINGOLTEPE
 * 
 */
public class DeleteKundeCommandHandler extends AbstractHandler implements
	IHandler, INeedLogin{
    public static final String ID = "at.ticketline.kassa.ui.command.deleteKunde";
    protected Logger log = LogFactory.getLogger(this.getClass());
    protected MitarbeiterService mitarbeiterService = new MitarbeiterServiceImpl();


    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
	this.log.info(this.getClass().getSimpleName() + " called");
	if (!(this instanceof INeedLogin) || mitarbeiterService.isLoggedIn()) {
	    	
	IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
	IWorkbenchPage page = window.getActivePage();
	KundeSucheView view = (KundeSucheView) page
		.findView(KundeSucheView.ID);

	ISelection selection = view.getSite().getSelectionProvider()
		.getSelection();
	if (((selection != null) || (selection instanceof IStructuredSelection)) == false) {
	    MessageDialog.openError(window.getShell(), "Fehler",
		    "Es wurde kein Kunde ausgewählt");
	    return null;
	}

	Kunde k = (Kunde) ((IStructuredSelection) selection)
		.getFirstElement();
	if (k == null) {
	    MessageDialog.openError(window.getShell(), "Fehler",
		    "Es wurde keine Kunde ausgewählt");
	    return null;
	}
	String name = k.getVorname() + " " + k.getNachname();
	boolean delete = MessageDialog.openQuestion(window.getShell(),
		"Löschen bestätigen", "Wollen Sie Kunde " + name
			+ " wirklich löschen?");
	if (delete == false) {
	    return null;
	}

	if ((k.getBestellungen() != null) && (k.getBestellungen().size() > 0)) {
	    MessageDialog
		    .openError(
			    window.getShell(),
			    "Error",
			    "Kunde "
				    + name
				    + " konnte nicht gelöscht werden, da er/sie noch an Bestellungen beteiligt ist");
	    return null;
	}
	
	if ((k.getTransaktionen() != null) && (k.getTransaktionen().size() > 0)) {
	    MessageDialog
		    .openError(
			    window.getShell(),
			    "Error",
			    "Kunde "
				    + name
				    + " konnte nicht gelöscht werden, da er/sie noch an Transaktionen beteiligt ist");
	    return null;
	}
	
	KundeService kundeService = new KundeServiceImpl();

	try {
	    k = kundeService.changeKundenDaten(k);
	    kundeService.removeKunde(k);
	    MessageDialog.openInformation(window.getShell(), "Information",
		    "Kunde " + name + " wurde gelöscht");
	} catch (DaoException e) {
	    this.log.error(e);
	    MessageDialog
		    .openError(
			    window.getShell(),
			    "Error",
			    "Kunde " + name
				    + " konnte nicht gelöscht werden: "
				    + e.getMessage());
	    return null;
	}
	view.tableViewer.remove(k);
	} else 
	    EasyMessage.showLoginRequiredMessage(ID);
	return null;
    }
}
