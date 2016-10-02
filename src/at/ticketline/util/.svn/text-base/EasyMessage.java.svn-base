package at.ticketline.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import at.ticketline.kassa.ui.view.MitarbeiterLoginView;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;

/**
 * @author Georg Fuderer
 * 
 * Statische Klasse zum automatischen Anzeigen einer vorgefertigten Nachricht
 * 
 */
public class EasyMessage {

    public static final String ID = "at.ticketline.util.loginRequired";
    protected static Logger log = LogFactory.getLogger(EasyMessage.class);

    /**
     * Informiert den Benutzer darüber, dass für die gewünschte Funktion ein Login erforderlich ist.
     * Gibt zudem eine Meldung im Logger aus
     * Setzt den Focus auf die Mitarbeiter-Login View
     * 
     * @param owner	ID des Aufrufenden Elements (für Logger)
     */
    public static void showLoginRequiredMessage(String owner) {

	log.warn("#0: Must be logged in to use this function", owner);
	MessageDialog
		.openWarning(
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell(),
			"Login erforderlich",
			"Sie müssen sich als Mitarbeiter einloggen, um diese Funktion verwenden zu können.");

	IWorkbenchPage page = PlatformUI.getWorkbench()
		.getActiveWorkbenchWindow().getActivePage();
	    page.activate(page.findView(MitarbeiterLoginView.ID));
    }

    /**
     * Informiert den Benutzer darüber, dass für die gewünschte Funktion ein Login erforderlich ist.
     * Gibt zudem eine Meldung im Logger aus
     * Setzt den Focus auf die Mitarbeiter-Login View
     * 
     */
    public static void showLoginRequiredMessage() {
	showLoginRequiredMessage(ID);
    }

}
