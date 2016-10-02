package at.ticketline.kassa.ui.editor;

import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.AuffuehrungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.Platz;
import at.ticketline.entity.PlatzStatus;
import at.ticketline.entity.Transaktion;
import at.ticketline.entity.Transaktionsstatus;
import at.ticketline.entity.Zahlungsart;
import at.ticketline.kassa.Activator;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.kassa.ui.sitzplan.SitzplanImpl;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.MitarbeiterServiceImpl;
import at.ticketline.service.TransaktionServiceImpl;
import at.ticketline.service.interfaces.MitarbeiterService;
import at.ticketline.service.interfaces.TransaktionService;

public class TicketVerkaufEditor extends EditorPart implements INeedLogin {
    public static final String ID = "at.ticketline.editor.ticketVerkauf";
    protected Logger log = LogFactory.getLogger(this.getClass());

    private TicketVerkaufEditorInput verkaufInput = null;
    @SuppressWarnings("unused")
    private AuffuehrungDao auffuehrungDao = null;

    // Service
    TransaktionService transaktionService;
    MitarbeiterService mitarbeiterService;

    // Sitzplan
    SitzplanImpl plan;

    // Verkauf/Reservierungsdaten
    private Kunde kunde = null;
    private Auffuehrung auffuehrung = null;
    private boolean verkauf = false;
    private Integer reservierungsNr = null;
    private Transaktion transaktion = null;

    private FormToolkit toolkit;
    private ScrolledForm form;
    private boolean dirty = false;
    private Label lblTicketPreis;
    private Button btnSave;
    
    private Composite c;

    public TicketVerkaufEditor() {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input)
	    throws PartInitException {
	this.setSite(site);
	log.debug("Ticket Verkauf EDITOR");
	this.setInput(input);
	this.verkaufInput = ((TicketVerkaufEditorInput) input);
	this.auffuehrungDao = (AuffuehrungDao) DaoFactory
		.findDaoByEntity(Auffuehrung.class);

	this.auffuehrung = this.verkaufInput.getAuffuehrung();
	this.kunde = this.verkaufInput.getKunde();
	this.verkauf = this.verkaufInput.isVerkauf();
	this.transaktion = this.verkaufInput.getTransaktion();
	if (transaktion == null) {
	    // hierbei muss komplett neu angelegt werden
	    if (this.verkauf == true) {
		this.setPartName("Ticketverkauf");
	    } else {
		this.setPartName("Ticketreservierung");
	    }
	} else {
	    // transaktion existiert bereits, muss geändert werden
	    if (this.verkauf == true) {
		// bestehende Reservierung soll verkauft werden
		this.setPartName("Reservierte Tickets verkaufen");
	    } else {
		// bestehende Reservierung soll geändert werden
		this.setPartName("Reservierung ändern");
	    }
	}

	// erzeuge Service
	this.transaktionService = new TransaktionServiceImpl();
	this.mitarbeiterService = new MitarbeiterServiceImpl();

	// sobald es sich um erstmaliges reservieren bzw. verkaufen handelt
	// sofort auf dirty setzen
	if (this.reservierungsNr == null) {
	    // TODO: dirty erst auf true setzen, wenn auswahl getätigt wurde
	    // (änderungen vorgenommen wurden) lg, georg
	    this.dirty = true;
	}

    }

    @Override
    public void createPartControl(Composite parent) {
	parent.setLayout(new GridLayout(1, false));

	this.toolkit = new FormToolkit(parent.getDisplay());
	this.form = this.toolkit.createScrolledForm(parent);
	this.form.setLayoutData(new GridData(GridData.FILL_BOTH));
	this.form.getBody().setLayout(new GridLayout(1, false));
	if (this.verkauf == true) {
	    this.form.setText("Ticketverkauf für "
		    + this.verkaufInput.getName());
	} else {
	    this.form.setText("Ticketreservierung für "
		    + this.verkaufInput.getName());
	}

	this.createForm(this.form.getBody());
	// this.createButton(this.form.getBody());
	c = parent;
	PlatformUI.getWorkbench().getHelpSystem().setHelp(c, "TicketlineRCP.ticket_verkauf"); // contextsensitive hilfe


    }
    
    /**
     * Zeichnet das Label in dem der Preis der Tickets angezeigt wird neu
     */
    public void drawTicketPrices() {
	StringBuilder sb = new StringBuilder("Preis der Tickets: ");
	sb.append(plan.getPriceForPlaces());
	String pr = "Preis der Tickets: " + plan.getPriceForPlaces() + "         ";  // Man muss anscheinend diese Leerzeichen nachher machen, sonst schneidet er den String ab!! (Who knows why..)
	
	lblTicketPreis.setText(pr);
    }

    protected void createForm(Composite parent) {

	this.plan = new SitzplanImpl(this.auffuehrung);

	Composite c = this.toolkit.createComposite(parent);
	c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	ColumnLayout columnLayout = new ColumnLayout();
	columnLayout.minNumColumns = 2;
	columnLayout.maxNumColumns = 2;
	c.setLayout(columnLayout);

	// Left
	Section leftSection = this.toolkit.createSection(c, Section.DESCRIPTION
		| Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
	leftSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		TicketVerkaufEditor.this.form.reflow(true);
	    }
	});
	leftSection.setText("Daten");
	leftSection.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));

	Composite left = this.toolkit.createComposite(leftSection);
	left.setLayout(new GridLayout(2, false));

	// Die eingerahmten Informationen zur Aufführung
	Group auffuehrungInfo = new Group(left, SWT.BORDER_SOLID);
	auffuehrungInfo.setText("Aufführungsdaten");
	auffuehrungInfo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true,
		false, 2, 1));
	auffuehrungInfo.setLayout(new GridLayout(2, false));
	Label lblVeranstaltung = new Label(auffuehrungInfo, SWT.LEFT);
	lblVeranstaltung.setText("Veranstaltung: ");
	Label lblVeranstaltungData = new Label(auffuehrungInfo, SWT.LEFT);
	if (this.auffuehrung.getVeranstaltung() != null) {
	    lblVeranstaltungData.setText(this.auffuehrung.getVeranstaltung()
		    .getBezeichnung());
	} else {
	    lblVeranstaltungData.setText("k.A.");
	}

	Label lblDatum = new Label(auffuehrungInfo, SWT.LEFT);
	lblDatum.setText("Datum/Uhrzeit: ");
	Label lblDatumData = new Label(auffuehrungInfo, SWT.LEFT);
	lblDatumData.setText(this.auffuehrung.getDatumuhrzeit().toString());

	Label lblSaal = new Label(auffuehrungInfo, SWT.LEFT);
	lblSaal.setText("Saal: ");
	Label lblSaalData = new Label(auffuehrungInfo, SWT.LEFT);
	if (this.auffuehrung.getSaal() != null) {
	    lblSaalData.setText(this.auffuehrung.getSaal().getBezeichnung());
	} else {
	    lblSaalData.setText("k.A.");
	}

	// Die eingerahmten Informationen zum Kunden
	Group kundeInfo = new Group(left, SWT.BORDER_SOLID);
	kundeInfo.setText("Kundendaten");
	kundeInfo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 2,
		1));
	kundeInfo.setLayout(new GridLayout(2, false));
	Label lblNachname = new Label(kundeInfo, SWT.LEFT);
	lblNachname.setText("Nachname: ");
	if (this.kunde.getNachname() != null) {
	    Label lblNachnameData = new Label(kundeInfo, SWT.LEFT);
	    lblNachnameData.setText(this.kunde.getNachname());
	} else {
	    // anonyme Kunden haben keinen Nachnamen
	    Label lblAnonymN = new Label(kundeInfo, SWT.LEFT);
	    lblAnonymN.setText("ANONYM");
	}
	// if (this.kunde.getId() == null) {
	// // anonymer Kunde
	// Label lblAnonymN = new Label(kundeInfo, SWT.LEFT);
	// lblAnonymN.setText("ANONYM");
	// } else {
	// Label lblNachnameData = new Label(kundeInfo, SWT.LEFT);
	// lblNachnameData.setText(this.kunde.getNachname());
	// }
	Label lblVorname = new Label(kundeInfo, SWT.LEFT);
	lblVorname.setText("Vorname: ");
	if (this.kunde.getVorname() != null) {
	    Label lblVornameData = new Label(kundeInfo, SWT.LEFT);
	    lblVornameData.setText(this.kunde.getVorname());
	} else {
	    Label lblAnonymV = new Label(kundeInfo, SWT.LEFT);
	    lblAnonymV.setText("ANONYM");
	}
	// if (this.kunde.getId() == null) {
	// // anonymer Kunde
	// Label lblAnonymV = new Label(kundeInfo, SWT.LEFT);
	// lblAnonymV.setText("ANONYM");
	// } else {
	// Label lblVornameData = new Label(kundeInfo, SWT.LEFT);
	// lblVornameData.setText(this.kunde.getVorname());
	// }

	// Daten zum vorhandenen Ticket (Reservierungsnr. usw.)

	// Sitzplan-Legende
	Group legendeInfo = new Group(left, SWT.BORDER_SOLID);
	legendeInfo.setText("Legende");
	legendeInfo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false,
		2, 1));
	legendeInfo.setLayout(new GridLayout(2, false));
	plan.getLegende(legendeInfo);

	// Preis fuer Tickets
	Group ticketPreis = new Group(left, SWT.BORDER_SOLID);
	ticketPreis.setText("Preis der Tickets");
	ticketPreis.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false,
		2, 1));
	ticketPreis.setLayout(new GridLayout(1, false));
	lblTicketPreis = new Label(ticketPreis, SWT.LEFT);
	
	
	leftSection.setClient(left);
	
	// rechts --> Sitzplan
	Section rightSection = this.toolkit.createSection(c,
		Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
			| Section.EXPANDED);
	rightSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		TicketVerkaufEditor.this.form.reflow(true);
	    }
	});
	rightSection.setText("Sitzplan");
	rightSection.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));
	Composite right = this.toolkit.createComposite(rightSection);
	right.setLayout(new GridLayout(1, false));

	right.setLayout(new GridLayout(plan.getGridSize(right), false));

	// Sitzplan wird gezeichnet:
	this.plan.getSitzplan(right, true, this.transaktion);
	// nachdem sitzplan gezeichnet wurde, wir der preis der selektierten karten bestimmt
	drawTicketPrices();
	rightSection.setClient(right);

	// BUTTON
	this.btnSave = new Button(parent, SWT.PUSH);
	this.btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
		false));

	if (this.transaktion == null) {
	    // neue reservierung/verkauf
	    if (this.verkauf == true) {
		this.btnSave.setText(" Verkaufen ");
	    } else {
		this.btnSave.setText(" Reservieren ");
	    }
	} else {
	    // änderung bestehender
	    if (this.verkauf == true) {
		this.btnSave.setText(" Tickets verkaufen ");
	    } else {
		this.btnSave.setText(" Reservierung ändern ");
	    }
	}

	this.btnSave.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		if (transaktion == null) {
		    if (verkauf == true) {
			verkaufeNeuesTicket();
		    } else {
			reserviereNeuesTicket();
		    }
		} else {
		    if (verkauf == true) {
			verkaufeReservierteTickets();
		    } else {
			aendereReservierteTickets();
		    }
		}

	    }
	});

	// workaround damit scrollbar angezeigt wird
	leftSection.setExpanded(false);
	leftSection.setExpanded(true);
    }

    public void refreshSitzplan() {
	log.info("Refreshing Sitzplan... ");
	plan.refreshSitzplan();
    }
    
  

    // START Verkaufe, Reservierungs bzw. Änderungs-Funktionen

    public void verkaufeNeuesTicket() {
	Kunde k = null;
	Mitarbeiter m;
	if (this.kunde.getId() != null) {
	    k = this.kunde;
	}
	// hole Mitarbeiter
	m = this.mitarbeiterService.getLoggedInMitarbeiter();
	if (m == null) {
	    MessageDialog.openError(TicketVerkaufEditor.this.getSite()
		    .getWorkbenchWindow().getShell(), "Error",
		    "Sie sind nicht eingeloggt! Um Tickets verkaufen zu können müssen "
			    + "sie sich als Mitarbeiter einloggen.");
	    return;
	}

	Set<Platz> plaetze = this.plan.getSelectedPlaetze();
	if (plaetze.isEmpty()) {
	    MessageDialog.openError(TicketVerkaufEditor.this.getSite()
		    .getWorkbenchWindow().getShell(), "Error",
		    "Es wurden keine Plätze ausgewählt! Bitte selektieren sie die"
			    + " zu verkaufenden Plätze direkt im Sitzplan.");
	    return;
	}

	this.transaktionService.createBuchung(Zahlungsart.VORKASSE, k, m,
		plaetze);
	this.dirty = false;
	// Benachrichtigen der Workbench, dass sich der Status geaendert hat
	TicketVerkaufEditor.this.firePropertyChange(IEditorPart.PROP_DIRTY);

	MessageDialog.openInformation(TicketVerkaufEditor.this.getSite()
		.getWorkbenchWindow().getShell(), "Ticketverkauf",
		"Ticketverkauf erfolgreich! Preis der Tickets: " + plan.getPriceForPlaces() + " Euro");
	this.getSite().getPage().closeEditor(this, false);
	Activator.getDefault().refreshEditors();
	Activator.getDefault().refreshSucheView(); // Damit der Status sofort geaendert wird im TicketSucheView!
    }

    public void reserviereNeuesTicket() {
	Kunde k = null;
	Mitarbeiter m;
	if (this.kunde.getId() != null) {
	    k = this.kunde;
	}
	// hole Mitarbeiter
	m = this.mitarbeiterService.getLoggedInMitarbeiter();
	if (m == null) {
	    MessageDialog.openError(TicketVerkaufEditor.this.getSite()
		    .getWorkbenchWindow().getShell(), "Error",
		    "Sie sind nicht eingeloggt! Um Tickets reservieren zu können müssen "
			    + "sie sich als Mitarbeiter einloggen.");
	    return;
	}

	Set<Platz> plaetze = this.plan.getSelectedPlaetze();
	if (plaetze.isEmpty()) {
	    MessageDialog.openError(TicketVerkaufEditor.this.getSite()
		    .getWorkbenchWindow().getShell(), "Error",
		    "Es wurden keine Plätze ausgewählt! Bitte selektieren sie die"
			    + " zu reservierenden Plätze direkt im Sitzplan.");
	    return;
	}

	this.reservierungsNr = this.transaktionService.createReservierung(
		Zahlungsart.VORKASSE, k, m, plaetze);
	this.dirty = false;
	// Benachrichtigen der Workbench, dass sich der Status geaendert hat
	TicketVerkaufEditor.this.firePropertyChange(IEditorPart.PROP_DIRTY);

	MessageDialog.openInformation(TicketVerkaufEditor.this.getSite()
		.getWorkbenchWindow().getShell(), "Reservierung",
		"Ticketreservierung erfolgreich!\n\n Reservierungsnummer:  "
			+ this.reservierungsNr);
	this.getSite().getPage().closeEditor(this, false);
	Activator.getDefault().refreshEditors();
	Activator.getDefault().refreshSucheView(); // Damit der Status sofort geaendert wird im TicketSucheView!
    }

    /**
     * Die aktuelle Transaktion wird geändert durch holen der selektierten
     * Plaetze aus dem Sitzplan und Speicherung der Änderung in der Datenbank,
     * wobei nun alle Plaetze als GEBUCHT eingetragen werden
     */
    public void verkaufeReservierteTickets() {

	if (mitarbeiterService.isLoggedIn() == false) {
	    MessageDialog.openError(TicketVerkaufEditor.this.getSite()
		    .getWorkbenchWindow().getShell(), "Error",
		    "Sie sind nicht eingeloggt! Um reservierte Tickets verkaufen "
			    + "zu können müssen "
			    + "sie sich als Mitarbeiter einloggen.");
	    return;
	}

	Set<Platz> plaetze = this.plan.getSelectedPlaetze();
	if (plaetze.isEmpty()) {
	    MessageDialog.openError(TicketVerkaufEditor.this.getSite()
		    .getWorkbenchWindow().getShell(), "Error",
		    "Es wurden keine Plätze ausgewählt! Bitte selektieren sie die"
			    + " zu verkaufenden Plätze direkt im Sitzplan.");
	    return;
	}
	// ändere die Stati aller Plätze in gebucht
	for (Platz p : plaetze) {
	    p.setStatus(PlatzStatus.GEBUCHT);
	    p.setTransaktion(this.transaktion);
	}
	this.transaktion.setPlaetze(plaetze);
	this.transaktion.setStatus(Transaktionsstatus.BUCHUNG);
	transaktionService.changeReservierung(this.transaktion);
	this.dirty = false;
	// Benachrichtigen der Workbench, dass sich der Status geaendert hat
	TicketVerkaufEditor.this.firePropertyChange(IEditorPart.PROP_DIRTY);

	MessageDialog.openInformation(TicketVerkaufEditor.this.getSite()
		.getWorkbenchWindow().getShell(), "Verkauf",
		"Reservierte Tickets wurden erfolgreich verkauft! Preis der Tickets: " + plan.getPriceForPlaces() + " Euro");
	this.getSite().getPage().closeEditor(this, false);
	Activator.getDefault().refreshEditors();
	Activator.getDefault().refreshSucheView(); // Damit der Status sofort geaendert wird im TicketSucheView!
    }
    
 

    /**
     * Die aktuelle Transaktion wird geändert durch holen der selektierten
     * Plaetze aus dem Sitzplan und Speicherung der Änderung in der Datenbank,
     * wobei nun alle Plaetze als RESERVIERT eingetragen werden
     */
    public void aendereReservierteTickets() {

	if (mitarbeiterService.isLoggedIn() == false) {
	    MessageDialog.openError(TicketVerkaufEditor.this.getSite()
		    .getWorkbenchWindow().getShell(), "Error",
		    "Sie sind nicht eingeloggt! Um Reservierungen ändern "
			    + "zu können müssen "
			    + "sie sich als Mitarbeiter einloggen.");
	    return;
	}

	Set<Platz> plaetze = this.plan.getSelectedPlaetze();
	if (plaetze.isEmpty()) {
	    MessageDialog.openError(TicketVerkaufEditor.this.getSite()
		    .getWorkbenchWindow().getShell(), "Error",
		    "Es wurden keine Plätze ausgewählt! Bitte selektieren sie die"
			    + " zu reservierenden Plätze direkt im Sitzplan.");
	    return;
	}
	// ändere die Stati aller Plätze in reservieret
	for (Platz p : plaetze) {
	    p.setStatus(PlatzStatus.RESERVIERT);
	    p.setTransaktion(this.transaktion);
	}
	this.transaktion.setPlaetze(plaetze);
	transaktionService.changeReservierung(this.transaktion);
	this.dirty = false;
	// Benachrichtigen der Workbench, dass sich der Status geaendert hat
	TicketVerkaufEditor.this.firePropertyChange(IEditorPart.PROP_DIRTY);

	MessageDialog.openInformation(TicketVerkaufEditor.this.getSite()
		.getWorkbenchWindow().getShell(), "Reservierungsänderung",
		"Reservierung wurde erfolgreich geändert!");
	this.getSite().getPage().closeEditor(this, false);
	Activator.getDefault().refreshEditors();
    }

    // ENDE Verkaufe, Reservierungs bzw. Änderungs-Funktionen

    @Override
    public void setFocus() {
	 if(c != null) // Context sensitive help
	      c.setFocus(); // context sensitive help

    }

    @Override
    public void doSave(IProgressMonitor monitor) {
	this.log.info("Speichern");
	if (this.verkauf == true) {
	    this.verkaufeNeuesTicket();
	} else {
	    this.reserviereNeuesTicket();
	}

    }

    @Override
    public void doSaveAs() {
	// nothing to do - use only the Save Command
    }

    @Override
    public boolean isDirty() {
	return this.dirty;
    }

    @Override
    public boolean isSaveAsAllowed() {
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.ticketline.kassa.ui.interfaces.IAuthentication#needsLogin()
     */

    // class EditorModifyListener implements ModifyListener, FocusListener {
    //
    // @Override
    // public void modifyText(ModifyEvent e) {
    // if ((e.getSource()
    // .equals(TicketVerkaufEditor.this.lblAuffuehrungWert))) {
    // TicketVerkaufEditor.this.updateTitle();
    // }
    // TicketVerkaufEditor.this.dirty = true;
    //
    // // Benachrichtigen der Workbench, dass sich der Status geaendert hat
    // TicketVerkaufEditor.this.firePropertyChange(IEditorPart.PROP_DIRTY);
    // }
    //
    // @Override
    // public void focusGained(FocusEvent e) {
    // // nothing to do
    // }
    //
    // @Override
    // public void focusLost(FocusEvent e) {
    // // Nothing to do
    // }
    // }
}
