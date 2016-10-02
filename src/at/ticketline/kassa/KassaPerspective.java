package at.ticketline.kassa;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.kassa.ui.view.AuffuehrungSucheView;
import at.ticketline.kassa.ui.view.KuenstlerSucheView;
import at.ticketline.kassa.ui.view.KundeSucheView;
import at.ticketline.kassa.ui.view.MitarbeiterLoginView;
import at.ticketline.kassa.ui.view.NewsView;
import at.ticketline.kassa.ui.view.OrtSucheView;
import at.ticketline.kassa.ui.view.TicketSucheView;
import at.ticketline.kassa.ui.view.TicketVerkaufView;
import at.ticketline.kassa.ui.view.VeranstaltungSucheView;

public class KassaPerspective implements IPerspectiveFactory {
	public static String ID = "at.ticketline.kassa.perspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);

		// Mitarbeiter Login View:
		layout.addView(MitarbeiterLoginView.ID, IPageLayout.TOP, 0.15f, editorArea);
		layout.getViewLayout(MitarbeiterLoginView.ID).setCloseable(false);
		layout.getViewLayout(MitarbeiterLoginView.ID).setMoveable(false);
		
		IFolderLayout menu = layout.createFolder(
				"at.ticketline.kassa.left_folder", IPageLayout.LEFT, 0.5f,
				editorArea);
		if (!(new AuffuehrungSucheView() instanceof INeedLogin)) {
		    menu.addView(AuffuehrungSucheView.ID);
		}
		menu.addView(KuenstlerSucheView.ID);
		// nächsten Views nicht sofort anzeigen, sondern erst wenn sie ausgewählt werden
//		menu.addPlaceholder(AuffuehrungSucheView.ID);
		menu.addPlaceholder(TicketVerkaufView.ID);
		menu.addPlaceholder(TicketSucheView.ID);
		menu.addPlaceholder(KundeSucheView.ID);
		menu.addPlaceholder(VeranstaltungSucheView.ID);
		menu.addPlaceholder(OrtSucheView.ID);
		menu.addPlaceholder(NewsView.ID);
//		layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.BOTTOM,
//				0.70f, editorArea);
	}
}
