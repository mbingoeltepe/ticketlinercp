package at.ticketline.kassa;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void makeActions(IWorkbenchWindow window) {
		// Registrierung der Standard-Actions
		// Dies ist ein Workaround für Bug 270007
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=270007
		this.register(ActionFactory.SAVE.create(window));
		this.register(ActionFactory.CLOSE.create(window));
		this.register(ActionFactory.IMPORT.create(window));
		this.register(ActionFactory.QUIT.create(window));

		// this.register(ActionFactory.CUT.create(window));
		// this.register(ActionFactory.COPY.create(window));
		// this.register(ActionFactory.PASTE.create(window));
		// this.register(ActionFactory.SELECT_ALL.create(window));

		this.register(ActionFactory.SHOW_VIEW_MENU.create(window));

		this.register(ActionFactory.HELP_CONTENTS.create(window));
		this.register(ActionFactory.HELP_SEARCH.create(window));
		this.register(ActionFactory.ABOUT.create(window));
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {

	}

}
