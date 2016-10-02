package at.ticketline.service;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.MitarbeiterDao;
import at.ticketline.entity.LoginStatus;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.interfaces.MitarbeiterService;

/**
 * @author stefanvoeber
 * 
 */
public class MitarbeiterServiceImpl implements MitarbeiterService {

    public static String ID = "at.ticketline.service.mitarbeiterService";
    protected Logger log = LogFactory.getLogger(this.getClass());
    protected MitarbeiterDao mitarbeiterDao = (MitarbeiterDao) DaoFactory
	    .findDaoByEntity(Mitarbeiter.class);

    public LoginStatus logIn(String username, String password) {

	LoginStatus status = mitarbeiterDao.logIn(username, password);

	if (status.equals(LoginStatus.SUCCEEDED)) {
	    log.info("#0: Logging in Mitarbeiter '#1' with username: '#1' ...",
		    ID, getLoggedInMitarbeiter().getVorname() + " "
			    + getLoggedInMitarbeiter().getNachname(),
		    getLoggedInMitarbeiter().getUsername());
	} else if (status.equals(LoginStatus.WRONG_USERNAME)) {
	    log.warn("#0: No Mitarbeiter with username: '#0' found", ID,
		    username);

	    MessageDialog.openError(PlatformUI.getWorkbench()
		    .getActiveWorkbenchWindow().getShell(), "Login Error",
		    "Username '" + username + "' existiert nicht.");
	} else if (status.equals(LoginStatus.WRONG_PASSWORD)) {
	    log.warn("#0: Wrong password entered for username: '#1'", ID,
		    username);

	    MessageDialog.openError(PlatformUI.getWorkbench()
		    .getActiveWorkbenchWindow().getShell(), "Login Error",
		    "Falsches Passwort für User '" + username + "'");
	}

	return status;
    }

    public boolean logOut() {

	Boolean success = true;

	if (mitarbeiterDao.getLoggedInMitarbeiter() != null) {

	    log.info(
		    "#0: Logging out Mitarbeiter '#1' with username: '#2' ...",
		    ID, getLoggedInMitarbeiter().getVorname() + " "
			    + getLoggedInMitarbeiter().getNachname(),
		    getLoggedInMitarbeiter().getUsername());

	    success = mitarbeiterDao.logOut(mitarbeiterDao
		    .getLoggedInMitarbeiter());

	    try {
		IWorkbenchPage page = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage();

		log.info(
			"#0: Closing Views and Editors that require Login ...",
			ID);

		for (int i = 0; i < page.getViewReferences().length; i++) {
		    IViewPart view = page.getViewReferences()[i].getView(false);

		    if (view instanceof INeedLogin) {
			page.hideView(view);
		    }
		}

		for (int j = 0; j < page.getEditorReferences().length; j++) {
		    if (page.getEditorReferences()[j].getEditor(false) instanceof INeedLogin) {
			page.closeEditor(
				page.getEditorReferences()[j].getEditor(false),
				true);
			page.closeEditor(
				page.getEditorReferences()[j].getEditor(false),
				false);
		    }
		}
	    } catch (Exception e) {
		log.info("#0: #1", ID, e);
	    }
	}

	return success;
    }

    public Mitarbeiter getLoggedInMitarbeiter() {
	return mitarbeiterDao.getLoggedInMitarbeiter();
    }

    public Boolean isLoggedIn() {
	return (mitarbeiterDao.getLoggedInMitarbeiter() != null);
    }

}
