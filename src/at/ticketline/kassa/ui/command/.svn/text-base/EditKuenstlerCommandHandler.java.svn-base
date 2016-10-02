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

import at.ticketline.entity.Kuenstler;
import at.ticketline.kassa.ui.editor.KuenstlerEditor;
import at.ticketline.kassa.ui.editor.KuenstlerEditorInput;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.kassa.ui.view.KuenstlerSucheView;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.MitarbeiterServiceImpl;
import at.ticketline.service.interfaces.MitarbeiterService;
import at.ticketline.util.EasyMessage;

public class EditKuenstlerCommandHandler extends AbstractHandler implements
	IHandler {
    public static final String ID = "at.ticketline.kassa.ui.command.editKuenstler";
    protected Logger log = LogFactory.getLogger(this.getClass());
    protected MitarbeiterService mitarbeiterService = new MitarbeiterServiceImpl();

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
	this.log.info(this.getClass().getSimpleName() + " called");
	IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
	IWorkbenchPage page = window.getActivePage();
	KuenstlerSucheView view = (KuenstlerSucheView) page
		.findView(KuenstlerSucheView.ID);

	ISelection selection = view.getSite().getSelectionProvider()
		.getSelection();
	if (((selection != null) || (selection instanceof IStructuredSelection)) == false) {
	    MessageDialog.openError(window.getShell(), "Fehler",
		    "Es wurde kein Kuenstler ausgewaehlt");
	    return null;
	}

	Kuenstler k = (Kuenstler) ((IStructuredSelection) selection)
		.getFirstElement();
	if (k == null) {
	    MessageDialog.openError(window.getShell(), "Fehler",
		    "Es wurde kein Kuenstler ausgewaehlt");
	    return null;
	}
	KuenstlerEditorInput input = new KuenstlerEditorInput(k);
	try {
	    if (!((new KuenstlerEditor()) instanceof INeedLogin)
		    || mitarbeiterService.isLoggedIn()) {
		page.openEditor(input, KuenstlerEditor.ID);
	    } else
		EasyMessage.showLoginRequiredMessage(ID);
	} catch (PartInitException e) {
	    MessageDialog.openError(
		    window.getShell(),
		    "Error",
		    "Editor fuer Kuenstler konnte nicht geoeffnet werden: "
			    + e.getMessage());
	    this.log.error(e);
	}
	return null;
    }

}
