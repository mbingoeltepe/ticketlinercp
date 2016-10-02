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
import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.KuenstlerDao;
import at.ticketline.entity.Kuenstler;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.kassa.ui.view.KuenstlerSucheView;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.MitarbeiterServiceImpl;
import at.ticketline.service.interfaces.MitarbeiterService;
import at.ticketline.util.EasyMessage;

public class DeleteKuenstlerCommandHandler extends AbstractHandler implements
	IHandler, INeedLogin {
    public static final String ID = "at.ticketline.kassa.ui.command.deleteKuenstler";
    protected Logger log = LogFactory.getLogger(this.getClass());
    protected MitarbeiterService mitarbeiterService = new MitarbeiterServiceImpl();

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
	this.log.info(this.getClass().getSimpleName() + " called");

	if (!(this instanceof INeedLogin) || mitarbeiterService.isLoggedIn()) {

	    IWorkbenchWindow window = HandlerUtil
		    .getActiveWorkbenchWindow(event);
	    IWorkbenchPage page = window.getActivePage();
	    KuenstlerSucheView view = (KuenstlerSucheView) page
		    .findView(KuenstlerSucheView.ID);

	    ISelection selection = view.getSite().getSelectionProvider()
		    .getSelection();
	    if (((selection != null) || (selection instanceof IStructuredSelection)) == false) {
		MessageDialog.openError(window.getShell(), "Fehler",
			"Es wurde kein Künstler ausgewählt");
		return null;
	    }

	    Kuenstler k = (Kuenstler) ((IStructuredSelection) selection)
		    .getFirstElement();
	    if (k == null) {
		MessageDialog.openError(window.getShell(), "Fehler",
			"Es wurde kein Künstler ausgewählt");
		return null;
	    }
	    String name = k.getVorname() + " " + k.getNachname();
	    boolean delete = MessageDialog.openQuestion(window.getShell(),
		    "Löschen bestätigen", "Wollen Sie Künstler " + name
			    + " wirklich löschen?");
	    if (delete == false) {
		return null;
	    }
	    if ((k.getEngagements() != null) && (k.getEngagements().size() > 0)) {
		MessageDialog
			.openError(
				window.getShell(),
				"Error",
				"Künstler "
					+ name
					+ " konnte nicht gelöscht werden, da er/sie noch an Veranstaltungen beteiligt ist");
		return null;
	    }
	    KuenstlerDao kuenstlerDao = (KuenstlerDao) DaoFactory
		    .findDaoByEntity(Kuenstler.class);
	    try {
		k = kuenstlerDao.merge(k);
		kuenstlerDao.remove(k);
		MessageDialog.openInformation(window.getShell(), "Information",
			"Künstler " + name + " wurde gelöscht");
	    } catch (DaoException e) {
		this.log.error(e);
		MessageDialog.openError(window.getShell(), "Error",
			"Künstler " + name + " konnte nicht gelöscht werden: "
				+ e.getMessage());
		return null;
	    }
	    view.tableViewer.remove(k);
	} else
	    EasyMessage.showLoginRequiredMessage(ID);

	return null;
    }
}
