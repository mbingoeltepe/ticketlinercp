package at.ticketline.kassa;

import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	protected Logger log = LogFactory
			.getLogger(ApplicationWorkbenchWindowAdvisor.class);

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = this.getWindowConfigurer();
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(true);
		configurer.setShowProgressIndicator(true);
		configurer.setTitle("Ticketline Kassa");
	}

	@Override
	public void postWindowCreate() {
		IWorkbenchWindowConfigurer configurer = this.getWindowConfigurer();
		configurer.getWindow().getShell().setMaximized(true);
	}

	@Override
	public void postWindowOpen() {
		this.log.info("Ticketline Kassa started");
	}
}
