package at.ticketline.kassa.ui.editor;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
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
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.EditorPart;

import at.ticketline.dao.DaoException;
import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.OrtDao;
import at.ticketline.dao.jpa.OrtDaoJpa;
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Bestellung;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kreditkartentyp;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Kundengruppe;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Transaktion;
import at.ticketline.kassa.Activator;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.KundeServiceImpl;
import at.ticketline.service.interfaces.KundeService;

/**
 * Der Editor, der für die Anzeige der bestimmten Kunde , der Bearbeitet wird
 * oder der neue Kunde.
 * 
 * @author MURAT BINGOLTEPE
 * 
 */
public class KundeEditor extends EditorPart implements INeedLogin {
    public static final String ID = "at.ticketline.editor.kunde";
    protected Logger log = LogFactory.getLogger(this.getClass());

    // Service
    private KundeService kundeService = null;

    // Kunde Daten
    private KundeEditorInput kundeInput = null;
    private Kunde kunde = null;
    private Adresse adresse = null;
    private OrtDao ortDao = null;

    private FormToolkit toolkit;
    private ScrolledForm form;
    private boolean dirty = false;

    private Text txtNachname;
    private Text txtVorname;
    private Text txtTitel;
    private Combo cbGeschlecht;
    private DateTime dtGeburtsdatum;
    private Text txtStrasse;
    private Text txtPlz;
    private Text txtOrt;
    private Text txtLand;
    private Text txtTelnr;
    private Text txtEmail;
    private Text txtBlz;
    private Button btnErmaechtigung;
    private Text txtKreditkartennr;
    private Combo cbKreditkartentyp;
    private DateTime dtKreditkartegueltig;
    private Text txtKontolimit;
    private Text txtKontostand;
    private Text txtErmaessigung;
    private Text txtTicketcardnr;
    private DateTime dtTicketcardgueltig;
    private ComboViewer cvOrt;
    private Button btnGesperrt;
    private Combo cbGruppe;
    private Text txtVorliebe;

    private Button btnSave;

    private TableViewer tableViewerBestellung;
    private TableViewer tableViewerTransaktion;
    
    private Composite c;

    // Content provider für den Ort Combobox
    protected OrtContentProvider ortProvider;

    public KundeEditor() {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input)
    throws PartInitException {
	this.setSite(site);
	this.setInput(input);
	this.kundeInput = ((KundeEditorInput) input);
	this.kunde = this.kundeInput.getKunde();
	this.setPartName(this.kundeInput.getName());

	// erzeuge Service
	this.kundeService = new KundeServiceImpl();

	this.ortProvider = new OrtContentProvider();

	OrtDao service = new OrtDaoJpa();
	this.ortDao = service;

	this.ortProvider.setOrtDao(service);
    }

    @Override
    public void createPartControl(Composite parent) {
	parent.setLayout(new GridLayout(1, false));

	this.toolkit = new FormToolkit(parent.getDisplay());
	this.form = this.toolkit.createScrolledForm(parent);
	this.form.setLayoutData(new GridData(GridData.FILL_BOTH));
	this.form.getBody().setLayout(new GridLayout(1, false));
	this.form.setText(this.kundeInput.getName());

	this.createForm(this.form.getBody());
	this.createTable(this.form.getBody());
	this.createSaveButton(this.form.getBody());

	// Wenn die Kunde Bestellungen hat werden die Bestellungen in die
	// Tabelle angezeigt.
	if ((this.kunde.getBestellungen() != null)
		&& (this.kunde.getBestellungen().size() > 0)) {
	    this.tableViewerBestellung.setInput(this.kunde.getBestellungen());
	}

	// Wenn die Kunde Transaktionen hat werden die Transaktionen in die
	// Tabelle angezeigt.
	if ((this.kunde.getTransaktionen() != null)
		&& (this.kunde.getTransaktionen().size() > 0)) {
	    this.tableViewerTransaktion.setInput(this.kunde.getTransaktionen());
	}

	// Warum? Wenn kein Kunde da ist, warum soll dann der Editor Dirty sein??
	// mmn sollte Editor dirty werden, wenn etwas geändert wird...
//	if (this.kunde.getId() == null) {
//	    this.dirty = true;
//	} else
//	    this.dirty = false;
	this.dirty = false;
	
	c = parent;
	PlatformUI.getWorkbench().getHelpSystem().setHelp(c, "TicketlineRCP.kunde"); // contextsensitive hilfe

    }

    protected void createForm(Composite parent) {
	Composite c = this.toolkit.createComposite(parent);
	c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	ColumnLayout columnLayout = new ColumnLayout();
	columnLayout.minNumColumns = 2;
	columnLayout.maxNumColumns = 2;
	c.setLayout(columnLayout);

	EditorModifyListener listener = new EditorModifyListener();

	// Left Side
	Section leftSection = this.toolkit.createSection(c, Section.DESCRIPTION
		| Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
	leftSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		KundeEditor.this.form.reflow(true);
	    }
	});
	leftSection.setText("Daten");
	leftSection.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));
	Composite left = this.toolkit.createComposite(leftSection);
	left.setLayout(new GridLayout(2, false));

	// Nachname
	Label lblNachname = this.toolkit.createLabel(left, "Nachname:",
		SWT.LEFT);
	lblNachname.setSize(230, lblNachname.getSize().y);

	this.txtNachname = this.toolkit.createText(left,
		this.kunde.getNachname(), SWT.LEFT | SWT.BORDER);
	this.txtNachname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtNachname.addModifyListener(listener);

	// Vorname
	Label lblVorname = this.toolkit.createLabel(left, "Vorname:", SWT.LEFT);

	lblVorname.setSize(230, lblVorname.getSize().y);

	this.txtVorname = this.toolkit.createText(left,
		this.kunde.getVorname(), SWT.LEFT | SWT.BORDER);
	this.txtVorname.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
		false));
	this.txtVorname.addModifyListener(listener);

	// Titel
	Label lblTitel = this.toolkit.createLabel(left, "Titel:", SWT.LEFT);

	lblTitel.setSize(230, lblTitel.getSize().y);

	this.txtTitel = this.toolkit.createText(left, this.kunde.getTitel(),
		SWT.LEFT | SWT.BORDER);
	this.txtTitel
	.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
	this.txtTitel.addModifyListener(listener);

	// Geschlecht
	Label lblGeschlecht = this.toolkit.createLabel(left, "Geschlecht:",
		SWT.LEFT);

	lblGeschlecht.setSize(230, lblGeschlecht.getSize().y);

	this.cbGeschlecht = new Combo(left, SWT.FLAT | SWT.READ_ONLY
		| SWT.BORDER);
	this.cbGeschlecht.setItems(Geschlecht.toStringArray());
	if (this.kunde.getGeschlecht() == Geschlecht.WEIBLICH) {
	    this.cbGeschlecht.select(1);
	} else {
	    this.cbGeschlecht.select(0);
	}
	this.cbGeschlecht.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.cbGeschlecht.addModifyListener(listener);
	this.toolkit.adapt(this.cbGeschlecht, true, true);

	// Geburtsdatum
	Label lblGeburtsdatum = this.toolkit.createLabel(left, "Geburtsdatum:",
		SWT.LEFT);

	lblGeburtsdatum.setSize(230, lblGeburtsdatum.getSize().y);

	this.dtGeburtsdatum = new DateTime(left, SWT.DROP_DOWN | SWT.BORDER);
	this.dtGeburtsdatum
	.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.dtGeburtsdatum.addFocusListener(listener);
	this.toolkit.adapt(this.dtGeburtsdatum, true, true);
	if (this.kunde.getGeburtsdatum() != null) {
	    GregorianCalendar gc = this.kunde.getGeburtsdatum();
	    this.dtGeburtsdatum.setYear(gc.get(Calendar.YEAR));
	    this.dtGeburtsdatum.setMonth(gc.get(Calendar.MONTH));
	    this.dtGeburtsdatum.setDay(gc.get(Calendar.DAY_OF_MONTH));
	}

	// Adresse , Strasse
	Label lblStrasse = this.toolkit.createLabel(left, "Strasse:", SWT.LEFT);

	lblStrasse.setSize(230, lblStrasse.getSize().y);

	if (this.kunde.getAdresse() != null) {
	    this.txtStrasse = this.toolkit.createText(left, this.kunde
		    .getAdresse().getStrasse(), SWT.LEFT | SWT.BORDER);
	} else {
	    this.txtStrasse = this.toolkit.createText(left, "", SWT.LEFT
		    | SWT.BORDER);
	}
	this.txtStrasse.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
		false));
	this.txtStrasse.addModifyListener(listener);

	// Adresse , PLZ
	Label lblPlz = this.toolkit.createLabel(left, "PLZ:", SWT.LEFT);

	lblPlz.setSize(230, lblPlz.getSize().y);

	if (this.kunde.getAdresse() != null) {
	    this.txtPlz = this.toolkit.createText(left, this.kunde.getAdresse()
		    .getPlz(), SWT.LEFT | SWT.BORDER);
	} else {
	    this.txtPlz = this.toolkit.createText(left, "", SWT.LEFT
		    | SWT.BORDER);
	}
	this.txtPlz.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
	this.txtPlz.addModifyListener(listener);

	// Adresse , Ort
	Label lblOrt = this.toolkit.createLabel(left, "Ort:", SWT.LEFT);

	lblOrt.setSize(230, lblOrt.getSize().y);

	if (this.kunde.getAdresse() != null) {
	    this.txtOrt = this.toolkit.createText(left, this.kunde.getAdresse()
		    .getOrt(), SWT.LEFT | SWT.BORDER);
	} else {
	    this.txtOrt = this.toolkit.createText(left, "", SWT.LEFT
		    | SWT.BORDER);
	}
	this.txtOrt.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
	this.txtOrt.addModifyListener(listener);

	// Adresse , Land
	Label lblLand = this.toolkit.createLabel(left, "Land:", SWT.LEFT);

	lblLand.setSize(230, lblLand.getSize().y);

	if (this.kunde.getAdresse() != null) {
	    this.txtLand = this.toolkit.createText(left, this.kunde
		    .getAdresse().getLand(), SWT.LEFT | SWT.BORDER);
	} else {
	    this.txtLand = this.toolkit.createText(left, "", SWT.LEFT
		    | SWT.BORDER);
	}
	this.txtLand
	.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
	this.txtLand.addModifyListener(listener);

	// TelNr
	Label lblTelnr = this.toolkit.createLabel(left, "Telefonnummer:",
		SWT.LEFT);

	lblTelnr.setSize(230, lblNachname.getSize().y);

	this.txtTelnr = this.toolkit.createText(left, this.kunde.getTelnr(),
		SWT.LEFT | SWT.BORDER);
	this.txtTelnr
	.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
	this.txtTelnr.addModifyListener(listener);

	// E-mail
	Label lblEmail = this.toolkit.createLabel(left, "E-mail:", SWT.LEFT);

	lblEmail.setSize(230, lblNachname.getSize().y);

	this.txtEmail = this.toolkit.createText(left, this.kunde.getEmail(),
		SWT.LEFT | SWT.BORDER);
	this.txtEmail
	.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
	this.txtEmail.addModifyListener(listener);

	// BLZ
	Label lblBlz = this.toolkit.createLabel(left, "BLZ:", SWT.LEFT);

	lblBlz.setSize(230, lblNachname.getSize().y);

	this.txtBlz = this.toolkit.createText(left, this.kunde.getBlz(),
		SWT.LEFT | SWT.BORDER);
	this.txtBlz.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
	this.txtBlz.addModifyListener(listener);

	leftSection.setClient(left);

	// Right Side
	Section rightSection = this.toolkit.createSection(c,
		Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
		| Section.EXPANDED);
	rightSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		KundeEditor.this.form.reflow(true);
	    }
	});
	rightSection.setText("Daten");
	rightSection.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));
	Composite right = this.toolkit.createComposite(rightSection);
	right.setLayout(new GridLayout(2, false));

	// Ermächtigung
	Label lblErmaechtigung = this.toolkit.createLabel(right,
		"Ermaechtigung:", SWT.LEFT);

	lblBlz.setSize(0, lblErmaechtigung.getSize().y);

	this.btnErmaechtigung = this.toolkit.createButton(right, "", SWT.LEFT
		| SWT.CHECK);

	if (this.kunde.isErmaechtigung())
	    this.btnErmaechtigung.setSelection(true);
	else
	    this.btnErmaechtigung.setSelection(false);

	this.btnErmaechtigung.addSelectionListener(listener);

	// Kredit Karten Nr
	Label lblKreditkartennr = this.toolkit.createLabel(right,
		"Kreditkarten Nr:", SWT.LEFT);

	lblBlz.setSize(230, lblKreditkartennr.getSize().y);

	this.txtKreditkartennr = this.toolkit.createText(right,
		this.kunde.getKreditkartennr(), SWT.LEFT | SWT.BORDER);
	this.txtKreditkartennr.setLayoutData(new GridData(SWT.FILL, SWT.TOP,
		true, false));
	this.txtKreditkartennr.addModifyListener(listener);
	this.txtKreditkartennr.addKeyListener(new KeyListener(){

	    @Override
	    public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	    }

	    @Override
	    public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		// Hier wird KrediKartenTyp mit eingegebene nummer automatisch ausgewählt.
		cbKreditkartentyp.select(
			checkKreditKarte(txtKreditkartennr.getText()));				
	    }

	});


	// Kredit Karten Typ
	Label lblKreditkartentyp = this.toolkit.createLabel(right,
		"Kreditkarten Typ:", SWT.LEFT);

	lblKreditkartentyp.setSize(230, lblKreditkartentyp.getSize().y);

	this.cbKreditkartentyp = new Combo(right, SWT.FLAT | SWT.READ_ONLY
		| SWT.BORDER);
	this.cbKreditkartentyp.setItems(Kreditkartentyp.toStringArray());
	this.cbKreditkartentyp.add("", 0);
	if (this.kunde.getKreditkartentyp() == Kreditkartentyp.MASTERCARD) {
	    this.cbKreditkartentyp.select(4);
	} else if (this.kunde.getKreditkartentyp() == Kreditkartentyp.AMERICAN_EXPRESS) {
	    this.cbKreditkartentyp.select(3);
	} else if (this.kunde.getKreditkartentyp() == Kreditkartentyp.DINERS_CLUB) {
	    this.cbKreditkartentyp.select(2);
	} else if (this.kunde.getKreditkartentyp() == Kreditkartentyp.VISA) {
	    this.cbKreditkartentyp.select(1);
	} else {
	    this.cbKreditkartentyp.select(0);
	}
	this.cbKreditkartentyp.setLayoutData(new GridData(
		GridData.FILL_HORIZONTAL));
	this.cbKreditkartentyp.addModifyListener(listener);
	this.toolkit.adapt(this.cbKreditkartentyp, true, true);
	this.cbKreditkartentyp.setEnabled(false);

	// Kredit Karte Gültig Bis
	Label lblKreditkartegueltig = this.toolkit.createLabel(right,
		"Kredit Karte Gültig Bis:", SWT.LEFT);

	lblGeburtsdatum.setSize(230, lblKreditkartegueltig.getSize().y);

	this.dtKreditkartegueltig = new DateTime(right, SWT.DROP_DOWN
		| SWT.BORDER);
	this.dtKreditkartegueltig.setLayoutData(new GridData(
		GridData.FILL_HORIZONTAL));
	this.dtKreditkartegueltig.addFocusListener(listener);
	this.toolkit.adapt(this.dtKreditkartegueltig, true, true);
	if (this.kunde.getKreditkarteGueltigBis() != null) {
	    GregorianCalendar gc = this.kunde.getKreditkarteGueltigBis();
	    this.dtKreditkartegueltig.setYear(gc.get(Calendar.YEAR));
	    this.dtKreditkartegueltig.setMonth(gc.get(Calendar.MONTH));
	    this.dtKreditkartegueltig.setDay(gc.get(Calendar.DAY_OF_MONTH));
	}

	// Konto Stand
	Label lblKontostand = this.toolkit.createLabel(right, "Konto stand:",
		SWT.LEFT);

	lblBlz.setSize(230, lblKontostand.getSize().y);

	if (this.kunde.getKontostand() != null) {
	    this.txtKontostand = this.toolkit.createText(right, this.kunde
		    .getKontostand().toString(), SWT.LEFT | SWT.BORDER);
	} else {
	    this.txtKontostand = this.toolkit.createText(right, "", SWT.LEFT
		    | SWT.BORDER);
	}
	this.txtKontostand.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
		false));
	this.txtKontostand.addModifyListener(listener);

	this.txtKontostand.addVerifyListener(new VerifyListener() {
	    @Override
	    public void verifyText(VerifyEvent e) {
		if (!Character.isDigit(e.character)
			&& !Character.isISOControl(e.character)) {
		    e.doit = false;
		}
	    }
	});

	// Konto Limit
	Label lblKontolimit = this.toolkit.createLabel(right, "Konto Limit:",
		SWT.LEFT);

	lblBlz.setSize(230, lblKontolimit.getSize().y);

	if (this.kunde.getKontolimit() != null) {
	    this.txtKontolimit = this.toolkit.createText(right, this.kunde
		    .getKontolimit().toString(), SWT.LEFT | SWT.BORDER);
	} else {
	    this.txtKontolimit = this.toolkit.createText(right, "", SWT.LEFT
		    | SWT.BORDER);
	}
	this.txtKontolimit.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
		false));
	this.txtKontolimit.addModifyListener(listener);

	this.txtKontolimit.addVerifyListener(new VerifyListener() {
	    @Override
	    public void verifyText(VerifyEvent e) {
		if (!Character.isDigit(e.character)
			&& !Character.isISOControl(e.character)) {
		    e.doit = false;
		}
	    }
	});

	// Ermaessigung
	Label lblErmaessigung = this.toolkit.createLabel(right, "Ermässigung:",
		SWT.LEFT);

	lblBlz.setSize(230, lblErmaessigung.getSize().y);

	if (this.kunde.getKontolimit() != null) {
	    this.txtErmaessigung = this.toolkit.createText(right, this.kunde
		    .getKontolimit().toString(), SWT.LEFT | SWT.BORDER);
	} else {
	    this.txtErmaessigung = this.toolkit.createText(right, "", SWT.LEFT
		    | SWT.BORDER);
	}
	this.txtErmaessigung.setLayoutData(new GridData(SWT.FILL, SWT.TOP,
		true, false));
	this.txtErmaessigung.addModifyListener(listener);

	this.txtErmaessigung.addVerifyListener(new VerifyListener() {
	    @Override
	    public void verifyText(VerifyEvent e) {
		if (!Character.isDigit(e.character)
			&& !Character.isISOControl(e.character)) {
		    e.doit = false;
		}
	    }
	});

	// Ticket Karten Nr
	Label lblTicketcardnr = this.toolkit.createLabel(right,
		"Ticket Karte Nr:", SWT.LEFT);

	lblBlz.setSize(230, lblTicketcardnr.getSize().y);

	this.txtTicketcardnr = this.toolkit.createText(right,
		this.kunde.getTicketcardnr(), SWT.LEFT | SWT.BORDER);
	this.txtTicketcardnr.setLayoutData(new GridData(SWT.FILL, SWT.TOP,
		true, false));
	this.txtTicketcardnr.addModifyListener(listener);

	// Ticket Karte Gültig Bis
	Label lblTicketcardgueltig = this.toolkit.createLabel(right,
		"Ticket Karte Gültig Bis:", SWT.LEFT);

	lblGeburtsdatum.setSize(230, lblTicketcardgueltig.getSize().y);

	this.dtTicketcardgueltig = new DateTime(right, SWT.DROP_DOWN
		| SWT.BORDER);
	this.dtTicketcardgueltig.setLayoutData(new GridData(
		GridData.FILL_HORIZONTAL));
	this.dtTicketcardgueltig.addFocusListener(listener);
	this.toolkit.adapt(this.dtTicketcardgueltig, true, true);
	if (this.kunde.getTicketcardGueltigBis() != null) {
	    GregorianCalendar gc = this.kunde.getTicketcardGueltigBis();
	    this.dtTicketcardgueltig.setYear(gc.get(Calendar.YEAR));
	    this.dtTicketcardgueltig.setMonth(gc.get(Calendar.MONTH));
	    this.dtTicketcardgueltig.setDay(gc.get(Calendar.DAY_OF_MONTH));
	}

	// Gesperrt
	Label lblGesperrt = this.toolkit.createLabel(right, "Gesperrt:",
		SWT.LEFT);

	lblBlz.setSize(0, lblGesperrt.getSize().y);

	this.btnGesperrt = this.toolkit.createButton(right, "", SWT.LEFT
		| SWT.CHECK);

	if (this.kunde.isGesperrt())
	    this.btnGesperrt.setSelection(true);
	else
	    this.btnGesperrt.setSelection(false);

	this.btnGesperrt.addSelectionListener(listener);

	// Kunde Gruppe
	Label lblGruppe = this.toolkit.createLabel(right, "Kunden Gruppe:",
		SWT.LEFT);

	lblGruppe.setSize(230, lblGruppe.getSize().y);

	this.cbGruppe = new Combo(right, SWT.FLAT | SWT.READ_ONLY | SWT.BORDER);
	this.cbGruppe.setItems(Kundengruppe.toStringArray());
	this.cbGruppe.add("", 0);
	if (this.kunde.getGruppe() == Kundengruppe.PREMIUM) {
	    this.cbGruppe.select(4);
	} else if (this.kunde.getGruppe() == Kundengruppe.GOLD) {
	    this.cbGruppe.select(3);
	} else if (this.kunde.getGruppe() == Kundengruppe.VIP) {
	    this.cbGruppe.select(2);
	} else if (this.kunde.getGruppe() == Kundengruppe.STANDARD) {
	    this.cbGruppe.select(1);
	} else {
	    this.cbGruppe.select(0);
	}
	this.cbGruppe.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.cbGruppe.addModifyListener(listener);
	this.toolkit.adapt(this.cbGruppe, true, true);

	// Vorliebe
	Label lblVorliebe = this.toolkit.createLabel(right, "Vorliebe:",
		SWT.LEFT);

	lblVorliebe.setSize(230, lblVorliebe.getSize().y);

	this.txtVorliebe = this.toolkit.createText(right,
		this.kunde.getVorlieben(), SWT.LEFT | SWT.BORDER);
	this.txtVorliebe.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
		false));
	this.txtVorliebe.addModifyListener(listener);

	// Ort
	Label lblOrts = this.toolkit.createLabel(right, "Ort:", SWT.LEFT);

	lblOrts.setSize(230, lblOrts.getSize().y);

	this.cvOrt = new ComboViewer(right, SWT.FLAT | SWT.READ_ONLY
		| SWT.BORDER);

	this.cvOrt.getCombo().setLayoutData(
		new GridData(GridData.FILL_HORIZONTAL));

	this.cvOrt.setContentProvider(this.ortProvider);

	this.cvOrt.setLabelProvider(new ILabelProvider() {

	    /**
	     * 
	     * @return text - Die Bezeichnung vom Ort wird zurückliefert.
	     */
	    @Override
	    public String getText(Object element) {
		Ort s = (Ort) element;

		String text = s.getBezeichnung();

		return text;
	    }

	    @Override
	    public void addListener(ILabelProviderListener listener) {
	    }

	    @Override
	    public void dispose() {
	    }

	    @Override
	    public boolean isLabelProperty(Object element, String property) {
		return true;
	    }

	    @Override
	    public void removeListener(ILabelProviderListener listener) {
	    }

	    @Override
	    public Image getImage(Object element) {
		return null;
	    }
	});

	this.cvOrt.setInput(new Ort());
	this.cvOrt.getCombo().addModifyListener(listener);

	// Hier Wird index select von Ort ComboViewer erstellt.
	if ((this.kunde.getOrt() != null)) {
	    for (int i = 0; i < this.cvOrt.getCombo().getItemCount(); i++) {
		if (this.kunde.getOrt().getBezeichnung()
			.equals(this.cvOrt.getCombo().getItem(i).toString()))
		    this.cvOrt.getCombo().select(i);
	    }
	} else {
	    this.cvOrt.getCombo().select(0);
	}

	rightSection.setClient(right);
	
	// workaround damit scrollbar angezeigt wird
	leftSection.setExpanded(false);
	leftSection.setExpanded(true);
	rightSection.setExpanded(false);
	rightSection.setExpanded(true);
    }

    protected void createTable(Composite parent) {

	Section bestellungSection = this.toolkit.createSection(parent,
		Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
		| Section.EXPANDED);
	bestellungSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		KundeEditor.this.form.reflow(true);
	    }
	});
	bestellungSection.setText("Bestellungen");
	bestellungSection.setLayoutData(new GridData(GridData.FILL_BOTH));

	this.tableViewerBestellung = new TableViewer(bestellungSection,
		SWT.BORDER | SWT.FULL_SELECTION);
	this.tableViewerBestellung.getTable().setLayoutData(
		new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	TableLayout layout = new TableLayout();
	layout.addColumnData(new ColumnWeightData(28, 100, true));
	layout.addColumnData(new ColumnWeightData(28, 100, true));
	layout.addColumnData(new ColumnWeightData(28, 100, true));
	layout.addColumnData(new ColumnWeightData(28, 100, true));
	layout.addColumnData(new ColumnWeightData(28, 100, true));
	layout.addColumnData(new ColumnWeightData(28, 100, true));

	this.tableViewerBestellung.getTable().setLayout(layout);

	this.tableViewerBestellung.getTable().setLinesVisible(true);
	this.tableViewerBestellung.getTable().setHeaderVisible(true);

	this.tableViewerBestellung
	.setContentProvider(new ArrayContentProvider());
	this.tableViewerBestellung.setLabelProvider(new ITableLabelProvider() {

	    @Override
	    public Image getColumnImage(Object arg0, int arg1) {
		return null;
	    }

	    @Override
	    public String getColumnText(Object element, int index) {
		Bestellung b = (Bestellung) element;
		switch (index) {
		case 0:
		    if (b.getBestellPositionen() != null) {
			return b.getBestellPositionen().toString();
		    } else {
			return "";
		    }
		case 1:
		    if (b.getBestellzeitpunkt() != null) {
			return b.getBestellzeitpunkt().toString();
		    } else {
			return "";
		    }
		case 2:
		    if (b.getZahlungsart() != null) {
			return b.getZahlungsart().toString();
		    } else {
			return "";
		    }
		case 3:
		    if (b.isBezahlt()) {
			return "JA";
		    } else {
			return "NEIN";
		    }
		case 4:
		    if (b.isVersandt()) {
			return "JA";
		    } else {
			return "NEIN";
		    }
		case 5:
		    if (b.getAnmerkungen() != null) {
			return b.getAnmerkungen().toString();
		    } else {
			return "";
		    }
		}
		return null;
	    }

	    @Override
	    public void addListener(ILabelProviderListener listener) {
		// nothing to do
	    }

	    @Override
	    public void dispose() {
		// nothing to do
	    }

	    @Override
	    public boolean isLabelProperty(Object arg0, String arg1) {
		return true;
	    }

	    @Override
	    public void removeListener(ILabelProviderListener arg0) {
		// nothing to do
	    }
	});

	// Die Column der BestellungTabelle werden erstellt.
	TableColumn colBestellposition = new TableColumn(
		this.tableViewerBestellung.getTable(), SWT.LEFT);
	colBestellposition.setText("Bestellposition");
	TableColumn colBestelltzeitpunkt = new TableColumn(
		this.tableViewerBestellung.getTable(), SWT.LEFT);
	colBestelltzeitpunkt.setText("Bestell Zeitpunkt");
	TableColumn colZahlungsart = new TableColumn(
		this.tableViewerBestellung.getTable(), SWT.LEFT);
	colZahlungsart.setText("Zahlungsart");
	TableColumn colBezahlt = new TableColumn(
		this.tableViewerBestellung.getTable(), SWT.LEFT | SWT.CHECK);
	colBezahlt.setText("Bezahlt");
	TableColumn colVerstandt = new TableColumn(
		this.tableViewerBestellung.getTable(), SWT.LEFT | SWT.CHECK);
	colVerstandt.setText("Verstandt");
	TableColumn colAnmerkungen = new TableColumn(
		this.tableViewerBestellung.getTable(), SWT.LEFT);
	colAnmerkungen.setText("Anmerkungen");

	this.toolkit.adapt(this.tableViewerBestellung.getTable(), true, true);
	bestellungSection.setClient(this.tableViewerBestellung.getTable());

	// Table für Transaktionen

	Section transaktionSection = this.toolkit.createSection(parent,
		Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
		| Section.EXPANDED);
	transaktionSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		KundeEditor.this.form.reflow(true);
	    }
	});
	transaktionSection.setText("Transaktionen");
	transaktionSection.setLayoutData(new GridData(GridData.FILL_BOTH));

	this.tableViewerTransaktion = new TableViewer(transaktionSection,
		SWT.BORDER | SWT.FULL_SELECTION);
	this.tableViewerTransaktion.getTable().setLayoutData(
		new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	TableLayout layoutTransaktion = new TableLayout();
	layoutTransaktion.addColumnData(new ColumnWeightData(28, 100, true));
	layoutTransaktion.addColumnData(new ColumnWeightData(28, 100, true));
	layoutTransaktion.addColumnData(new ColumnWeightData(28, 100, true));
	layoutTransaktion.addColumnData(new ColumnWeightData(28, 100, true));
	layoutTransaktion.addColumnData(new ColumnWeightData(28, 100, true));
	layoutTransaktion.addColumnData(new ColumnWeightData(28, 100, true));

	this.tableViewerTransaktion.getTable().setLayout(layoutTransaktion);

	this.tableViewerTransaktion.getTable().setLinesVisible(true);
	this.tableViewerTransaktion.getTable().setHeaderVisible(true);

	this.tableViewerTransaktion
	.setContentProvider(new ArrayContentProvider());
	this.tableViewerTransaktion.setLabelProvider(new ITableLabelProvider() {

	    @Override
	    public Image getColumnImage(Object arg0, int arg1) {
		return null;
	    }

	    @Override
	    public String getColumnText(Object element, int index) {
		Transaktion t = (Transaktion) element;
		switch (index) {
		case 0:
		    if (t.getDatumuhrzeit() != null) {
			return t.getDatumuhrzeit().toString();
		    } else {
			return "";
		    }
		case 1:
		    if (t.getStatus() != null) {
			return t.getStatus().toString();
		    } else {
			return "";
		    }
		case 2:
		    if (t.getReservierungsnr() != null) {
			return t.getReservierungsnr().toString();
		    } else {
			return "";
		    }
		case 3:
		    if (t.getZahlungsart() != null) {
			return t.getZahlungsart().toString();
		    } else {
			return "";
		    }
		case 4:
		    if (t.getMitarbeiter() != null) {
			return t.getMitarbeiter().getNachname() + ", "
			+ t.getMitarbeiter().getVorname();
		    } else {
			return "";
		    }
		case 5:
		    if (t.getPlaetze() != null) {
			return t.getPlaetze().toString();
		    } else {
			return "";
		    }
		}
		return null;
	    }

	    @Override
	    public void addListener(ILabelProviderListener listener) {
		// nothing to do
	    }

	    @Override
	    public void dispose() {
		// nothing to do
	    }

	    @Override
	    public boolean isLabelProperty(Object arg0, String arg1) {
		return true;
	    }

	    @Override
	    public void removeListener(ILabelProviderListener arg0) {
		// nothing to do
	    }
	});

	// Die Column der TransaktionTabelle werden erstellt.
	TableColumn colDatum = new TableColumn(
		this.tableViewerTransaktion.getTable(), SWT.LEFT);
	colDatum.setText("Datumuhrzeit");
	TableColumn colStatus = new TableColumn(
		this.tableViewerTransaktion.getTable(), SWT.LEFT);
	colStatus.setText("Status");
	TableColumn colReservierungsnr = new TableColumn(
		this.tableViewerTransaktion.getTable(), SWT.LEFT);
	colReservierungsnr.setText("Reservierungs Nr");
	TableColumn colZahlungsartt = new TableColumn(
		this.tableViewerTransaktion.getTable(), SWT.LEFT | SWT.CHECK);
	colZahlungsartt.setText("Zahlungsart");
	TableColumn colMitarbeiter = new TableColumn(
		this.tableViewerTransaktion.getTable(), SWT.LEFT);
	colMitarbeiter.setText("Mitarbeiter");
	TableColumn colPlaetze = new TableColumn(
		this.tableViewerTransaktion.getTable(), SWT.LEFT);
	colPlaetze.setText("Plätze");

	this.toolkit.adapt(this.tableViewerTransaktion.getTable(), true, true);
	transaktionSection.setClient(this.tableViewerTransaktion.getTable());
	
	// workaround damit scrollbar angezeigt wird
	bestellungSection.setExpanded(false);
	bestellungSection.setExpanded(true);
	transaktionSection.setExpanded(false);
	transaktionSection.setExpanded(true);
    }

    private void createSaveButton(Composite parent) {
	this.btnSave = new Button(parent, SWT.PUSH);
	this.btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
		false));
	this.btnSave.setText(" Speichern ");
	this.btnSave.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		if (KundeEditor.this.dirty == false) {
		    MessageDialog.openInformation(KundeEditor.this.getSite().getShell(), "Nichts zu tun", "Es wurde nichts geändert!");
		    return;
		}
		IHandlerService handlerService = (IHandlerService) KundeEditor.this
		.getSite().getService(IHandlerService.class);
		try {
		    handlerService.executeCommand("org.eclipse.ui.file.save",
			    null);
		    Activator.getDefault().refreshSucheView();
		} catch (Exception ex) {
		    KundeEditor.this.log.error(ex);

		    MessageDialog.openError(
			    KundeEditor.this.getSite().getWorkbenchWindow()
			    .getShell(),
			    "Error",
			    "Kunde kann nicht gespeichert werden: "
			    + ex.getMessage());
		}
	    }
	});

    }

    @Override
    public void setFocus() {
	txtNachname.setFocus();
	txtNachname.setSelection(0, txtNachname.getText().length());
	 if(c != null) // Context sensitive help
	      c.setFocus(); // context sensitive help
    }

    @Override
    public void doSave(IProgressMonitor monitor) {

	this.log.info("Speichern");
	boolean getSpeicherStatus = true;
	String messagePlz ="";
	String messageEmail ="";
	String messageKredit ="";
	String messageDatumKredit ="";
	String messageDatumTicket ="";
	
	// Pflicht Felder Farben
	Device device = Display.getCurrent();
	Color red = new Color(device, 255, 0, 0);
	Color white = new Color(device, 255, 255, 255);

	// Pflicht Felder Farben auf Weis ersetzen.
	this.txtNachname.setBackground(white);
	this.txtVorname.setBackground(white);
	this.txtStrasse.setBackground(white);
	this.txtPlz.setBackground(white);
	this.txtOrt.setBackground(white);
	this.txtEmail.setBackground(white);
	this.txtKreditkartennr.setBackground(white);

	// Nachname
	if (this.txtNachname.getText().equals("")) {
	    this.txtNachname.setBackground(red);
	    getSpeicherStatus = false;
	} else {
	    this.kunde.setNachname(this.txtNachname.getText());
	}

	// Vorname
	if (this.txtVorname.getText().equals("")) {
	    this.txtVorname.setBackground(red);
	    getSpeicherStatus = false;
	} else {
	    this.kunde.setVorname(this.txtVorname.getText());
	}

	// Titel
	this.kunde.setTitel(this.txtTitel.getText());

	// Geschlecht
	this.kunde.setGeschlecht(Geschlecht.getValueOf(this.cbGeschlecht
		.getText()));

	// Geburtsdatum
	GregorianCalendar gc = new GregorianCalendar();
	if (this.kunde.getGeburtsdatum() != null)
	    gc.set(this.dtGeburtsdatum.getYear(),
		    this.dtGeburtsdatum.getMonth(),
		    this.dtGeburtsdatum.getDay());

	this.kunde.setGeburtsdatum(gc);

	// Adresse
	if (this.txtStrasse.getText().equals("")) {
	    this.txtStrasse.setBackground(red);
	    getSpeicherStatus = false;
	}

	if (this.txtPlz.getText().length() < 4) {
	    this.txtPlz.setBackground(red);
	    getSpeicherStatus = false;
	    messagePlz = "\nFehler : PLZ muss mindestens 4 Stellen haben.";
	}

	if (this.txtOrt.getText().equals("")) {
	    this.txtOrt.setBackground(red);
	    getSpeicherStatus = false;
	}

	if (getSpeicherStatus) {
	    adresse = new Adresse();
	    adresse.setStrasse(this.txtStrasse.getText());
	    adresse.setPlz(this.txtPlz.getText());
	    adresse.setOrt(this.txtOrt.getText());
	    adresse.setLand(this.txtLand.getText());
	    this.kunde.setAdresse(adresse);
	}

	// Tel nr
	this.kunde.setTelnr(this.txtTelnr.getText());

	// E-mail
	if (!(this.txtEmail.getText().equals("")) &&
		(!checkEmail(this.txtEmail.getText()))) {
	    this.txtEmail.setBackground(red);
	    getSpeicherStatus = false;
	    messageEmail = "Ungültige E-Mail Adresse";
	} else {
	    this.kunde.setEmail(this.txtEmail.getText());	    
	}

	// BLZ
	this.kunde.setBlz(this.txtBlz.getText());

	// Ermaechtigung
	this.kunde.setErmaechtigung(this.btnErmaechtigung.getSelection());

	// Kreditkarten nr
	if (!(this.txtKreditkartennr.getText().equals("")) &&
		(checkKreditKarte(this.txtKreditkartennr.getText()) == 0)) {
	    this.txtKreditkartennr.setBackground(red);
	    getSpeicherStatus = false;
	    messageKredit = "\nFehler : Ungültige Kredeitkarten nr."
		+ "\nWir akzeptieren folgende Kreditkarten"
		+ "\nHersteller: VISA, Anfang: 4, Gesamtlänge: 13,16, Beispiel: 4XXX-XXXX-XXXX-XXXX"
		+ "\nHersteller: DINERS CLUB, Anfang: 30,36,38, Gesamtlänge: 14, Beispiel: 30XX-XXXX-XXXX-XX"
		+ "\nHersteller: AMERICAN EXPRESS, Anfang: 34,37, Gesamtlänge: 15, Beispiel: 34XX-XXXX-XXXX-XXX"
		+ "\nHersteller: MASTERCARD, Anfang: 51,52,53,54,55, Gesamtlänge: 16, Beispiel: 51XX-XXXX-XXXX-XXXX";

	} else {
	    this.kunde.setKreditkartennr(this.txtKreditkartennr.getText());
	}


	// Kreditkarten typ
	this.kunde.setKreditkartentyp(Kreditkartentyp
		.getValueOf(this.cbKreditkartentyp.getText()));

	// Kreditkarte Gültig bis
	GregorianCalendar gck = new GregorianCalendar();
	if (this.kunde.getKreditkarteGueltigBis() != null)
	    gck.set(this.dtKreditkartegueltig.getYear(),
		    this.dtKreditkartegueltig.getMonth(),
		    this.dtKreditkartegueltig.getDay());

	if (gck.getTime().before(GregorianCalendar.getInstance().getTime())) {
	    getSpeicherStatus = false;
	    messageDatumKredit = "\nFehler : Kreditkarte bereits abgelaufen.";
	} else {
	    this.kunde.setKreditkarteGueltigBis(gck);		  
	}


	// Konto stand
	if (this.txtKontostand.getText().equals(""))
	    this.txtKontostand.setText("0");

	this.kunde.setKontostand(new BigDecimal(this.txtKontostand.getText()));

	// Konto Limit
	if (this.txtKontolimit.getText().equals(""))
	    this.txtKontolimit.setText("0");

	this.kunde.setKontolimit(new BigDecimal(this.txtKontolimit.getText()));

	// Ermaessigung
	if (this.txtErmaessigung.getText().equals(""))
	    this.txtErmaessigung.setText("0");

	this.kunde.setErmaessigung(new BigDecimal(this.txtErmaessigung
		.getText()));

	// Ticketkarten nr
	this.kunde.setTicketcardnr(this.txtTicketcardnr.getText());

	// Ticketkarte Gültig bis
	GregorianCalendar gct = new GregorianCalendar();
	if (this.kunde.getTicketcardGueltigBis() != null)
	    gct.set(this.dtTicketcardgueltig.getYear(),
		    this.dtTicketcardgueltig.getMonth(),
		    this.dtTicketcardgueltig.getDay());

	
	if (gct.getTime().before(GregorianCalendar.getInstance().getTime())) {
	    getSpeicherStatus = false;
	    messageDatumTicket = "\nFehler : Das Datum für das Ticket ist bereits abgelaufen.";
	} else {
		this.kunde.setTicketcardGueltigBis(gct);
	}


	// Gesperrt
	this.kunde.setGesperrt(this.btnGesperrt.getSelection());

	// Gruppe
	this.kunde.setGruppe(Kundengruppe.getValueOf(this.cbGruppe.getText()));

	// Vorliebe
	this.kunde.setVorlieben(this.txtVorliebe.getText());

	// Ort
	this.ortDao = (OrtDao) DaoFactory.findDaoByEntity(Ort.class);
	Ort query = new Ort();
	query.setBezeichnung(this.cvOrt.getCombo().getItem(
		this.cvOrt.getCombo().getSelectionIndex()));

	List<Ort> ort = ortDao.findByOrt(query);

	if (ort.size() != 0) {
	    this.kunde.setOrtverk(ort.get(0));
	} else {
	    this.kunde.setOrtverk(null);
	}

	this.kunde.setBestellungen(null);
	this.kunde.setTransaktionen(null);

	if (getSpeicherStatus) {
	    try {
		if (this.kunde.getId() == null) {
		    this.kundeService.addKundeToDb(this.kunde);
		} else {
		    this.kunde = this.kundeService
		    .changeKundenDaten(this.kunde);
		}
		this.dirty = false;

		// Benachrichtigen der Workbench, dass sich der Status geaendert
		// hat
		KundeEditor.this.firePropertyChange(IEditorPart.PROP_DIRTY);
		MessageDialog.openInformation(this.getSite()
			.getWorkbenchWindow().getShell(), "Information",
			"Die Kunde wurde erfolgreich gespeichert.");

	    } catch (ConstraintViolationException c) {
		StringBuilder sb = new StringBuilder(
		"Die eingegebene Daten weisen folgende Fehler auf:\n");
		for (ConstraintViolation<?> cv : c.getConstraintViolations()) {
		    sb.append(cv.getPropertyPath().toString().toUpperCase())
		    .append(" ").append(cv.getMessage());
		}
		MessageDialog.openError(this.getSite().getWorkbenchWindow()
			.getShell(), "Error", sb.toString());

	    } catch (DaoException e) {
		this.log.error(e);
		e.printStackTrace();
		MessageDialog.openError(
			this.getSite().getWorkbenchWindow().getShell(),
			"Error",
			"Kunde konnte nicht gespeichert werden: "
			+ e.getMessage());

	    }
	} else {
	    MessageDialog.openError(this.getSite().getWorkbenchWindow()
		    .getShell(), "Error", "Bitte die roten Felder ausfüllen." 
		    + messagePlz
		    + messageEmail
		    + messageKredit
		    + messageDatumKredit
		    + messageDatumTicket);
	    return;
	}

    }
    /**
     * Überprüft ob die Emailadresse korrekt eingegeben ist oder nicht
     * 
     * @param email
     * @return false  wenn email adresse nicht korrekt ist
     * 	       true   wenn email adresse korrekt ist 	
     */
    public boolean checkEmail(String email) {

	//Das ist ein E-Mail Muster
	Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

	//Kontrolliert die email mit dem Pattern
	Matcher m = p.matcher(email);

	boolean matchFound = m.matches();

	if (!matchFound)
	    return false;

	return true;
    }


    /**
     * Überprüft die Kredit Nummer welche Typ gehört
     * 
     * Hersteller	Anfang		Gesamtlänge	Beispiel
     * Visa		4		13,16		4XXX-XXXX-XXXX-XXXX
     * Master		51,52,53,54,55 	16 		55XX-XXXX-XXXX-XXXX
     * Diner’s Club 	30,36,38 	14 		30XX-XXXX-XXXX-XX
     * American Express 34,37 		15 		34XX-XXXX-XXXX-XXX
     *  
     * @param nummer 
     * @return 1 wenn die Nummer Typ von VISA gehört.
     * 	       2 wenn die Nummer Typ von DINERS CLUB gehört.
     * 	       3 wenn die Nummer Typ von AMERICAN EXPRESS gehört.
     *         4 wenn die Nummer Typ von MASTERCARD gehört.
     *         0 wenn die Nummer Ungültig ist.
     * 	
     */
    public int checkKreditKarte(String nummer) {

	char[] nummerChar = nummer.toCharArray(); 
	int nummerLeft = 0;

	if (nummer.length() < 12) {
	    return 0;		
	}
	else if ((nummer.length() > 12) && (nummer.length() < 20)) {
	    nummerLeft = Integer.parseInt(nummer.substring(0, 2));

	    if ((nummerChar[4] != '-') ||
		    (nummerChar[9] != '-') ||
		    (nummerChar[14] != '-')) 
		return 0;

	}

	// Überprüft die Nummer welche Typ gehört.
	if (((nummer.length() == 16) || 
		(nummer.length() == 19)) && 
		(nummerChar[0] == '4')) {
	    // VISA
	    return 1;
	} else if ((nummer.length() == 17) && 
		((nummerLeft == 30) ||
			(nummerLeft == 36) ||
			(nummerLeft == 38))) {
	    // DINERS CLUB"
	    return 2;
	} else if ((nummer.length() == 18) && 
		((nummerLeft == 34) ||
			(nummerLeft == 37))) {
	    // AMERICAN EXPRESS
	    return 3;
	} else if ((nummer.length() == 19) && 
		((nummerLeft == 51) ||
			(nummerLeft == 52) || 
			(nummerLeft == 53) ||
			(nummerLeft == 54) ||
			(nummerLeft == 55))) {
	    // MASTERCARD
	    return 4;

	}else
	    return 0;
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

    public void updateTitle() {
	this.setPartName(this.txtVorname.getText() + " "
		+ this.txtNachname.getText());
	this.form.setText(this.txtVorname.getText() + " "
		+ this.txtNachname.getText());
    }

    class EditorModifyListener implements ModifyListener, FocusListener,
    SelectionListener {
	@Override
	public void modifyText(ModifyEvent e) {
	    if ((e.getSource().equals(KundeEditor.this.txtNachname))
		    || (e.getSource().equals(KundeEditor.this.txtVorname))) {
		KundeEditor.this.updateTitle();
	    }
	    KundeEditor.this.dirty = true;

	    // Benachrichtigen der Workbench, dass sich der Status geaendert hat
	    KundeEditor.this.firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public void focusGained(FocusEvent e) {
	    // nothing to do
	}

	@Override
	public void focusLost(FocusEvent e) {

	    // Geburtsdatum
	    if (e.getSource().equals(KundeEditor.this.dtGeburtsdatum)) {

		if (e.getSource().equals(KundeEditor.this.dtGeburtsdatum) == false) {
		    return;
		}
		try {
		    GregorianCalendar gc = KundeEditor.this.kunde
		    .getGeburtsdatum();
		    if (gc == null) {
			KundeEditor.this.dirty = true;
			KundeEditor.this
			.firePropertyChange(IEditorPart.PROP_DIRTY);
			return;
		    }
		    if ((KundeEditor.this.dtGeburtsdatum.getYear() != gc
			    .get(Calendar.YEAR))
			    || (KundeEditor.this.dtGeburtsdatum.getMonth() != gc
				    .get(Calendar.MONTH))
				    || (KundeEditor.this.dtGeburtsdatum.getDay() != gc
					    .get(Calendar.DAY_OF_MONTH))) {
			KundeEditor.this.dirty = true;
			KundeEditor.this
			.firePropertyChange(IEditorPart.PROP_DIRTY);
		    }
		} catch (Exception ex) {
		    KundeEditor.this.log.error(ex);
		}
	    } else if (e.getSource().equals(
		    KundeEditor.this.dtKreditkartegueltig)) {
		// Kreditkarte gültig bis
		if (e.getSource().equals(KundeEditor.this.dtKreditkartegueltig) == false) {
		    return;
		}
		try {
		    GregorianCalendar gc = KundeEditor.this.kunde
		    .getKreditkarteGueltigBis();
		    if (gc == null) {
			KundeEditor.this.dirty = true;
			KundeEditor.this
			.firePropertyChange(IEditorPart.PROP_DIRTY);
			return;
		    }
		    if ((KundeEditor.this.dtGeburtsdatum.getYear() != gc
			    .get(Calendar.YEAR))
			    || (KundeEditor.this.dtGeburtsdatum.getMonth() != gc
				    .get(Calendar.MONTH))
				    || (KundeEditor.this.dtGeburtsdatum.getDay() != gc
					    .get(Calendar.DAY_OF_MONTH))) {
			KundeEditor.this.dirty = true;
			KundeEditor.this
			.firePropertyChange(IEditorPart.PROP_DIRTY);
		    }
		} catch (Exception ex) {
		    KundeEditor.this.log.error(ex);
		}
	    } else if (e.getSource().equals(
		    KundeEditor.this.dtTicketcardgueltig)) {
		// Ticket Karte Gültig bis
		if (e.getSource().equals(KundeEditor.this.dtTicketcardgueltig) == false) {
		    return;
		}
		try {
		    GregorianCalendar gc = KundeEditor.this.kunde
		    .getTicketcardGueltigBis();
		    if (gc == null) {
			KundeEditor.this.dirty = true;
			KundeEditor.this
			.firePropertyChange(IEditorPart.PROP_DIRTY);
			return;
		    }
		    if ((KundeEditor.this.dtTicketcardgueltig.getYear() != gc
			    .get(Calendar.YEAR))
			    || (KundeEditor.this.dtTicketcardgueltig.getMonth() != gc
				    .get(Calendar.MONTH))
				    || (KundeEditor.this.dtTicketcardgueltig.getDay() != gc
					    .get(Calendar.DAY_OF_MONTH))) {
			KundeEditor.this.dirty = true;
			KundeEditor.this
			.firePropertyChange(IEditorPart.PROP_DIRTY);
		    }
		} catch (Exception ex) {
		    KundeEditor.this.log.error(ex);
		}
	    }

	}

	@Override
	public void widgetSelected(SelectionEvent e) {

	    // Ermaechtigung
	    if (e.getSource().equals(KundeEditor.this.btnErmaechtigung)) {
		KundeEditor.this.dirty = true;
		KundeEditor.this.firePropertyChange(IEditorPart.PROP_DIRTY);
		return;
	    }

	    // Gespettt
	    if (e.getSource().equals(KundeEditor.this.btnGesperrt)) {
		KundeEditor.this.dirty = true;
		KundeEditor.this.firePropertyChange(IEditorPart.PROP_DIRTY);
		return;
	    }

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	    // TODO Auto-generated method stub

	}

    }

    /**
     * Klasse, die für den Inhalt der Ort-Combobox verantwortlich ist
     */
    class OrtContentProvider implements IStructuredContentProvider {

	private OrtDao ortDao = null;

	@Override
	public Object[] getElements(Object query) {

	    this.ortDao = (OrtDao) DaoFactory.findDaoByEntity(Ort.class);

	    if ((query instanceof Ort) == true) {
		// Content für die Ort Combobox
		// egal welcher Ort übergeben wird, es werden alle
		// Orten
		// als Content zurückgeliefert, da die ComboBox nur eine Liste
		// aller Orten ist.
		List<Ort> sList = ortDao.findAll();
		sList.add(0, new Ort()); // Dummy-Element für <keine Auswahl>
		return sList.toArray();

	    } else {
		KundeEditor.this.log.info("Query object not of type Ort: #0",
			query.getClass().getName());
		return new Object[0];
	    }
	}

	@Override
	public void dispose() {
	    // TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	    // TODO Auto-generated method stub

	}

	public void setOrtDao(OrtDao ortdao) {
	    this.ortDao = ortdao;
	}

    }

}