package at.ticketline.kassa.ui.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.kassa.ui.editor.TicketVerkaufEditor;
import at.ticketline.kassa.ui.editor.TicketVerkaufEditorInput;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.kassa.ui.view.TicketVerkaufView;
import at.ticketline.kassa.ui.view.TicketVerkaufView.TicketVerkaufData;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.MitarbeiterServiceImpl;
import at.ticketline.service.interfaces.MitarbeiterService;
import at.ticketline.util.EasyMessage;

public class SellTicketsEditorCommandHandler extends AbstractHandler implements
	IHandler {
    public static final String ID = "at.ticketline.kassa.ui.command.verkaufeTicketsEditor";
    protected Logger log = LogFactory.getLogger(this.getClass());
    protected MitarbeiterService mitarbeiterService = new MitarbeiterServiceImpl();

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
	this.log.info(this.getClass().getSimpleName() + " called");
	IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
	IWorkbenchPage page = window.getActivePage();

	TicketVerkaufView view = (TicketVerkaufView) page
		.findView(TicketVerkaufView.ID);

	// Die Daten aus der View herauslesen, mit denen der Editor gefüllt
	// werden soll
	IStructuredSelection selection = (IStructuredSelection) view.getSite()
		.getSelectionProvider().getSelection();

	// Lies ausgewählten Kunden und Aufführung aus der View aus
	Kunde k = ((TicketVerkaufData) selection).getKundeData();
	Auffuehrung a = ((TicketVerkaufData) selection).getAuffuehrungData();
	boolean verkauf = ((TicketVerkaufData) selection).isVerkauf();
	// erzeuge input editor
	TicketVerkaufEditorInput input = new TicketVerkaufEditorInput(a, k,
		verkauf);

	try {
	    if (!((new TicketVerkaufEditor()) instanceof INeedLogin)
		    || mitarbeiterService.isLoggedIn()) {
		page.openEditor(input, TicketVerkaufEditor.ID);
	    } else
		EasyMessage.showLoginRequiredMessage(ID);
	} catch (PartInitException e) {
	    MessageDialog.openError(window.getShell(), "Error",
		    "Ticket Verkauf/Reservierung konnte nicht geoeffnet werden: "
			    + e.getMessage());
	    this.log.error(e);
	}
	return null;
    }
}
