package at.ticketline.kassa.ui.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.kassa.ui.view.OrtSucheView;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.MitarbeiterServiceImpl;
import at.ticketline.service.interfaces.MitarbeiterService;
import at.ticketline.util.EasyMessage;

/**
 * @author Georg Fuderer
 *
 */
public class SearchOrtCommandHandler extends AbstractHandler implements
	IHandler, INeedLogin {
    public static final String ID = "at.ticketline.kassa.ui.command.sucheOrt";
    protected Logger log = LogFactory.getLogger(this.getClass());
    protected MitarbeiterService mitarbeiterService = new MitarbeiterServiceImpl();

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
	this.log.info(this.getClass().getSimpleName() + " called");
	IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
	IWorkbenchPage page = window.getActivePage();
	try {
	    if (!((new OrtSucheView()) instanceof INeedLogin)
		    || mitarbeiterService.isLoggedIn()) {
		page.showView(OrtSucheView.ID);
	    } else
		EasyMessage.showLoginRequiredMessage(ID);
	} catch (PartInitException e) {
	    MessageDialog.openError(
		    window.getShell(),
		    "Error",
		    "Suche Ort View konnte nicht geoeffnet werden: "
			    + e.getMessage());
	    this.log.error(e);
	}
	return null;
    }

}
