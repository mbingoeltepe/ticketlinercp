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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import at.ticketline.entity.Kunde;
import at.ticketline.kassa.ui.editor.KundeEditor;
import at.ticketline.kassa.ui.editor.KundeEditorInput;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.kassa.ui.view.KundeSucheView;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.MitarbeiterServiceImpl;
import at.ticketline.service.interfaces.MitarbeiterService;
import at.ticketline.util.EasyMessage;

/**
 * @author MURAT BINGOLTEPE
 * 
 */
public class EditKundeCommandHandler extends AbstractHandler implements
	IHandler, INeedLogin {
    public static final String ID = "at.ticketline.kassa.ui.command.editKunde";
    protected Logger log = LogFactory.getLogger(this.getClass());
    protected MitarbeiterService mitarbeiterService = new MitarbeiterServiceImpl();

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
	this.log.info(this.getClass().getSimpleName() + " called");
	IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
	IWorkbenchPage page = window.getActivePage();
	KundeSucheView view = (KundeSucheView) page
		.findView(KundeSucheView.ID);

	ISelection selection = view.getSite().getSelectionProvider()
		.getSelection();
	if (((selection != null) || (selection instanceof IStructuredSelection)) == false) {
	    MessageDialog.openError(window.getShell(), "Fehler",
		    "Es wurde keine Kunde ausgewaehlt");
	    return null;
	}

	Kunde k = (Kunde) ((IStructuredSelection) selection)
		.getFirstElement();
	if (k == null) {
	    MessageDialog.openError(window.getShell(), "Fehler",
		    "Es wurde keine Kunde ausgewaehlt");
	    return null;
	}
	KundeEditorInput input = new KundeEditorInput(k);
	try {
	    if (!((new KundeEditor()) instanceof INeedLogin)
		    || mitarbeiterService.isLoggedIn()) {
		page.openEditor(input, KundeEditor.ID);
	    } else
		EasyMessage.showLoginRequiredMessage(ID);
	} catch (PartInitException e) {
	    MessageDialog.openError(
		    window.getShell(),
		    "Error",
		    "Editor fuer Kunde konnte nicht geoeffnet werden: "
			    + e.getMessage());
	    this.log.error(e);
	}
	return null;
    }

}
