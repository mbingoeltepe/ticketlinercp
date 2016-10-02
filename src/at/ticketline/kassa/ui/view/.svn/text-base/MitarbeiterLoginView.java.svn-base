package at.ticketline.kassa.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import at.ticketline.entity.LoginStatus;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.MitarbeiterServiceImpl;
import at.ticketline.service.interfaces.MitarbeiterService;

/**
 * Ermöglicht Das Anmelden eines Mitarbeiters mittels Username und Passwort.
 * 
 * 
 * @author Georg Fuderer
 * 
 */
public class MitarbeiterLoginView extends ViewPart {
    public static final String ID = "at.ticketline.view.login";
    protected Logger log = LogFactory.getLogger(this.getClass());

    protected Group formGroup = null;

    protected MitarbeiterService mitarbeiterService = new MitarbeiterServiceImpl();

    private Boolean loginFailed = false;
    private LoginStatus loginStatus;

    // Labels
    protected Label lblUsername;
    protected Label lblPassword;

    // Eingabefelder
    protected Text txtUsername;
    protected Text txtPassword;

    // Buttons
    protected Button btnLogin;
    
    private Composite c;

    @Override
    public void init(IViewSite site) throws PartInitException {
	super.init(site);
    }

    @Override
    public void createPartControl(Composite parent) {
	c = new Composite(parent, SWT.FILL);
	c.setLayout(new GridLayout(5, false));

	this.createForm(c);

	txtUsername.setText("max");
	/**
	 * TODO - for debugging purposes only. line has to be deleted for
	 * release version
	 **/
	txtPassword.setText("max");
	/**
	 * TODO - for debugging purposes only. line has to be deleted for
	 * release version
	 **/
	
	 PlatformUI.getWorkbench().getHelpSystem().setHelp(c, "TicketlineRCP.mitarbeiter_login"); // contextsensitive hilfe
    }

    public void createForm(Composite parent) {
	formGroup = new Group(parent, SWT.BORDER_SOLID);
	formGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1,
		1));
	formGroup.setLayout(new GridLayout(5, false));

	lblUsername = new Label(formGroup, SWT.LEFT);
	txtUsername = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	txtUsername.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		loginButtonClickHandler();
	    }
	});
	txtUsername.addKeyListener(new KeyListener() {

	    @Override
	    public void keyReleased(KeyEvent e) {
	    }

	    @Override
	    public void keyPressed(KeyEvent e) {
		loginFailed = false;
	    }
	});
	txtUsername.addListener(SWT.FocusIn, new Listener() {

	    @Override
	    public void handleEvent(Event event) {
		txtUsername.setSelection(0, txtUsername.getText().length());
	    }
	});
	txtUsername.addListener(SWT.FocusOut, new Listener() {

	    @Override
	    public void handleEvent(Event event) {
		loginFailed = false;
	    }
	});
	txtUsername.addPaintListener(new PaintListener() {

	    @Override
	    public void paintControl(PaintEvent e) {

		if (txtUsername.getText().length() == 0 && loginFailed) {
		    GC gc = e.gc;
		    Color red = new Color(null, 255, 0, 0);
		    gc.setBackground(red);
		    Rectangle rect = new Rectangle(-1, -1, txtUsername
			    .getSize().x + 2, txtUsername.getSize().y + 2);
		    gc.fillRectangle(rect);
		}
		txtUsername.addControlListener(new ControlAdapter() {
		    public void controlResized(ControlEvent e) {
			super.controlResized(e);
		    }
		});
	    }
	});

	lblPassword = new Label(formGroup, SWT.LEFT);
	txtPassword = new Text(formGroup, SWT.PASSWORD | SWT.BORDER);
	txtPassword.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		loginButtonClickHandler();
	    }
	});
	txtPassword.addKeyListener(new KeyListener() {

	    @Override
	    public void keyReleased(KeyEvent e) {
	    }

	    @Override
	    public void keyPressed(KeyEvent e) {
		loginFailed = false;
	    }
	});
	txtPassword.addListener(SWT.FocusIn, new Listener() {

	    @Override
	    public void handleEvent(Event event) {
		txtPassword.setSelection(0, txtPassword.getText().length());
	    }
	});
	txtPassword.addListener(SWT.FocusOut, new Listener() {

	    @Override
	    public void handleEvent(Event event) {
		loginFailed = false;
	    }
	});
	txtPassword.addPaintListener(new PaintListener() {

	    @Override
	    public void paintControl(PaintEvent e) {

		if (txtPassword.getText().length() == 0 && loginFailed) {
		    GC gc = e.gc;
		    Color red = new Color(null, 255, 0, 0);
		    gc.setBackground(red);
		    Rectangle rect = new Rectangle(-1, -1, txtPassword
			    .getSize().x + 2, txtPassword.getSize().y + 2);
		    gc.fillRectangle(rect);
		}
		txtPassword.addControlListener(new ControlAdapter() {
		    public void controlResized(ControlEvent e) {
			super.controlResized(e);
		    }
		});
	    }
	});

	btnLogin = new Button(formGroup, SWT.PUSH);
	btnLogin.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		loginButtonClickHandler();
	    }
	});

	setFormLayoutLoggedIn(false);
    }

    @Override
    public void setFocus() {
	 if(c != null) // Context sensitive help
	      c.setFocus(); // context sensitive help
	txtUsername.setFocus();
    }

    private void setFormLayoutLoggedIn(Boolean loggedIn) {

	txtUsername.setEditable(!loggedIn);
	txtUsername.setEnabled(!loggedIn);
	lblPassword.setEnabled(!loggedIn);
	lblPassword.setVisible(!loggedIn);
	txtPassword.setEnabled(!loggedIn);
	txtPassword.setVisible(!loggedIn);

	lblUsername.setText(loggedIn ? "Eingeloggt als: " : "Username: ");
	lblPassword.setText(loggedIn ? "" : "Passwort: ");
	btnLogin.setText(loggedIn ? "Logout" : "Login");

	if (loggedIn) {
	    txtUsername.setLayoutData(new GridData(150, 15));
	    txtPassword.setLayoutData(new GridData(0, 15));
	} else if (!loggedIn) {
	    txtUsername.setLayoutData(new GridData(150, 15));
	    txtPassword.setLayoutData(new GridData(150, 15));
	    if (loginStatus != LoginStatus.WRONG_PASSWORD)
		txtUsername.setText("");
	    txtPassword.setText("");
	    if (loginStatus == LoginStatus.WRONG_PASSWORD)
		txtPassword.setFocus();
	    else
		txtUsername.setFocus();
	    loginFailed = false;
	}

	formGroup.pack();
    }

    private void loginButtonClickHandler() {

	if (mitarbeiterService.isLoggedIn())
	    logout();
	else {

	    if (txtUsername.getText().length() == 0) {
		log.info("#0: Must enter a username!", ID);
		loginFailed = true;
		txtUsername.setFocus();
	    } else if (txtPassword.getText().length() == 0) {
		log.info("#0: Must enter a password!", ID);
		loginFailed = true;
		txtPassword.setFocus();
	    } else {
		login();
	    }
	}
    }

    private void login() {

	loginStatus = mitarbeiterService.logIn(txtUsername.getText(),
		txtPassword.getText());

	if (loginStatus.equals(LoginStatus.SUCCEEDED)) {
	    loginFailed = false;
	    setFormLayoutLoggedIn(true);
	    try {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("at.ticketline.view.news");
	    } catch (PartInitException e) {
		log.error(e.getMessage());
	    }
	} else if (loginStatus.equals(LoginStatus.WRONG_USERNAME)
		|| loginStatus.equals(LoginStatus.WRONG_PASSWORD)) {
	    loginFailed = true;
	    setFormLayoutLoggedIn(false);
	}
    }

    private void logout() {

	setFormLayoutLoggedIn(false);

	if (mitarbeiterService.logOut()) {
	    loginFailed = false;
	}
    }

}
