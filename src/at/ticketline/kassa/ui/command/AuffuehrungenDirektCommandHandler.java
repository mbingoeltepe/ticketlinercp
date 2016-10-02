package at.ticketline.kassa.ui.command;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.kassa.ui.editor.AuffuehrungListeEditor;
import at.ticketline.kassa.ui.editor.AuffuehrungListeEditorInput;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.kassa.ui.view.AuffuehrungSucheView;
import at.ticketline.kassa.ui.view.AuffuehrungSucheView.AuffuehrungResultList;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.MitarbeiterServiceImpl;
import at.ticketline.service.interfaces.MitarbeiterService;
import at.ticketline.util.EasyMessage;

public class AuffuehrungenDirektCommandHandler extends AbstractHandler
	implements IHandler {
    public static final String ID = "at.ticketline.kassa.ui.command.auffuehrungenDirekt";
    protected Logger log = LogFactory.getLogger(this.getClass());
    protected MitarbeiterService mitarbeiterService = new MitarbeiterServiceImpl();

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
	this.log.info(this.getClass().getSimpleName() + " called");
	IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
	IWorkbenchPage page = window.getActivePage();

	AuffuehrungSucheView view = (AuffuehrungSucheView) page
		.findView(AuffuehrungSucheView.ID);

	// Die Daten aus der View herauslesen, mit denen der Editor gefüllt
	// werden soll
	ISelection selection = view.getSite().getSelectionProvider()
		.getSelection();

	List<Auffuehrung> list = ((AuffuehrungResultList) selection)
		.getResultList();
	if (list == null) {
	    MessageDialog.openInformation(window.getShell(), "Information",
		    "Es wurden keine Aufführungen gefunden!");
	}
	Auffuehrung query = ((AuffuehrungResultList) selection)
		.getQueryObjekt();

	AuffuehrungListeEditorInput input = new AuffuehrungListeEditorInput(
		list, query);

	// AuffuehrungListeEditorInput input = new
	// AuffuehrungListeEditorInput(null, null);

	try {
	    if (!((new AuffuehrungListeEditor()) instanceof INeedLogin)
		    || mitarbeiterService.isLoggedIn()) {
		page.openEditor(input, AuffuehrungListeEditor.ID);
	    } else
		EasyMessage.showLoginRequiredMessage(ID);
	} catch (PartInitException e) {
	    MessageDialog.openError(window.getShell(), "Error",
		    "Editor fuer Liste der Auffuehrungen konnte nicht geooeffnet werden: "
			    + e.getMessage());
	    this.log.error(e);
	}
	return null;
    }

}
