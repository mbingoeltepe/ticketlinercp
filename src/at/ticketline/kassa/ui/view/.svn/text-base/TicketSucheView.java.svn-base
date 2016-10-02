package at.ticketline.kassa.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Transaktion;
import at.ticketline.entity.Transaktionsstatus;
import at.ticketline.kassa.ui.editor.TicketVerkaufEditor;
import at.ticketline.kassa.ui.editor.TicketVerkaufEditorInput;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.AuffuehrungServiceImpl;
import at.ticketline.service.KundeServiceImpl;
import at.ticketline.service.TransaktionServiceImpl;
import at.ticketline.service.ValidateException;
import at.ticketline.service.interfaces.AuffuehrungService;
import at.ticketline.service.interfaces.KundeService;
import at.ticketline.service.interfaces.TransaktionService;
import at.ticketline.util.InstantSearch;
import at.ticketline.util.SearchView;

/**
 * @author ???, Georg Fuderer
 * 
 * @see at.ticketline.kassa.ui.interfaces.INeedLogin
 * @see at.ticketline.util.SearchView
 * @see at.ticketline.util.InstantSearch
 * 
 */
public class TicketSucheView extends ViewPart implements INeedLogin, SearchView {
    public static final String ID = "at.ticketline.view.suche.ticket";

    protected Logger log = LogFactory.getLogger(TicketSucheView.class);

    public TableViewer tableViewer;

    // Service
    protected TransaktionService transaktionService;
    protected KundeService kundeService;

    // Provider für ComboBoxen
    protected AuffuehrungContentProvider auffuehrungProvider;
    protected TicketContentProvider ticketProvider;

    // die Liste der aktuell gefundenen Tickets (Transaktionen)
    // muss immer up-to-date sein
    protected List<Transaktion> foundTickets;

    // Eingabefelder und Buttons
    protected Button btnReservierungsNr;
    protected Button btnNameAuffuehrung;
    protected Text txtReservierungsNr;
    protected Text txtVorname;
    protected Text txtNachname;
    protected ComboViewer cvAuffuehrung;
    protected Button btnSucheAuffuehrung;
    protected Label lblNachname;
    protected Label lblVorname;
    protected Label lblAuffuehrung;
    protected Button btnVerkaufen;
    protected Button btnBearbeiten;
    protected Button btnStornieren;

    protected Label lblSearchSpacer;
    protected Button btnSearch;
    
    private Composite c;

    @Override
    public void init(IViewSite site) throws PartInitException {
	super.init(site);

	// provider für ComboViewer
	this.auffuehrungProvider = new AuffuehrungContentProvider();
	this.auffuehrungProvider
		.setAuffuehrungService(new AuffuehrungServiceImpl());

	// provider für Tabelle
	this.ticketProvider = new TicketContentProvider();
	this.transaktionService = new TransaktionServiceImpl();
	this.kundeService = new KundeServiceImpl();

	this.foundTickets = new ArrayList<Transaktion>();
    }

    @Override
    public void createPartControl(Composite parent) {
	c = new Composite(parent, SWT.NONE);
	c.setLayout(new GridLayout(2, false));

	this.createForm(c);

	this.createTable(c);

	this.createButtons(c);

	if (enableInstantSearchOnStartup)
	    enableInstantSearch();
	
	 PlatformUI.getWorkbench().getHelpSystem().setHelp(c, "TicketlineRCP.ticket_suchen"); // contextsensitive hilfe
    }

    /**
     * Erzeugt die Form welche alle Suchfelder und den Suchen-Button beinhaltet
     * 
     * @param parent
     *            Der Parent Composite indem die Form eingefügt wird
     */
    public void createForm(Composite parent) {
	Group formGroup = new Group(parent, SWT.BORDER_SOLID);
	formGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2,
		1));
	formGroup.setLayout(new GridLayout(2, false));

	// 1. Zeile
	this.btnReservierungsNr = new Button(formGroup, SWT.RADIO | SWT.LEFT);
	this.btnReservierungsNr.setText("Reservierungs-Nr.: ");

	this.txtReservierungsNr = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtReservierungsNr.setLayoutData(new GridData(
		GridData.FILL_HORIZONTAL));
	// nur Zahlen erlauben für Textfeld Reservierungsnummer
	this.txtReservierungsNr.addVerifyListener(new VerifyListener() {
	    @Override
	    public void verifyText(VerifyEvent e) {
		switch (e.keyCode) {
		case SWT.BS:
		case SWT.END: // End
		case SWT.ARROW_LEFT: // Left arrow
		case SWT.ARROW_RIGHT: // Right arrow
		    return;
		}

		if (!Character.isDigit(e.character)) {
		    e.doit = false;
		}
	    }
	});
	this.txtReservierungsNr.addListener(SWT.DefaultSelection,
		new Listener() {
		    public void handleEvent(Event e) {
			TicketSucheView.this.searchHandler();
		    }
		});
	// 2. Zeile
	this.btnNameAuffuehrung = new Button(formGroup, SWT.RADIO | SWT.LEFT);
	this.btnNameAuffuehrung.setText("Name und Aufführung");
	Label spacer = new Label(formGroup, SWT.NULL);
	spacer.setText("");
	// 3. Zeile
	this.lblNachname = new Label(formGroup, SWT.LEFT);
	lblNachname.setText("   Nachname: ");
	this.txtNachname = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtNachname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	// 4. Zeile
	this.lblVorname = new Label(formGroup, SWT.LEFT);
	lblVorname.setText("   Vorname: ");
	this.txtVorname = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtVorname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	// 5. Zeile
	this.lblAuffuehrung = new Label(formGroup, SWT.LEFT);
	lblAuffuehrung.setText("   Aufführung: ");

	this.cvAuffuehrung = new ComboViewer(formGroup);
	this.cvAuffuehrung.getCombo().setLayoutData(
		new GridData(GridData.FILL_HORIZONTAL));

	this.cvAuffuehrung.setContentProvider(this.auffuehrungProvider);

	this.cvAuffuehrung.setLabelProvider(new ILabelProvider() {

	    @Override
	    public String getText(Object element) {
		Auffuehrung a = (Auffuehrung) element;
		String text = a.format();
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
	// erstmals mit allen Auffuehrungen befüllen
	this.cvAuffuehrung.setInput(new Auffuehrung());

	// 6. Zeile
	/*
	 * Label spacer2 = new Label(formGroup, SWT.NULL); spacer2.setText("");
	 * this.btnSucheAuffuehrung = new Button(formGroup, SWT.PUSH);
	 * this.btnSucheAuffuehrung.setText(" Suche Aufführung ... ");
	 */

	// 7. Zeile
	lblSearchSpacer = new Label(formGroup, SWT.NULL);
	lblSearchSpacer.setText("");
	btnSearch = new Button(formGroup, SWT.PUSH);
	btnSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
	btnSearch.setText(" Ticket Suchen ");
	btnSearch
		.setToolTipText("Nach reservierten und verkauften Tickets suchen");
	btnSearch.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {

		TicketSucheView.this.searchHandler();
	    }
	});

	this.btnReservierungsNr.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		setSearchByName(false);
	    }
	});

	this.btnNameAuffuehrung.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		setSearchByName(true);
	    }
	});

	this.btnReservierungsNr.setSelection(true);
	this.txtNachname.setEnabled(false);
	this.txtVorname.setEnabled(false);
	// this.btnSucheAuffuehrung.setEnabled(false);
	this.cvAuffuehrung.getCombo().setEnabled(false);
	this.lblNachname.setEnabled(false);
	this.lblVorname.setEnabled(false);
	this.lblAuffuehrung.setEnabled(false);
    }

    private void setSearchByName(Boolean searchByName) {
	TicketSucheView.this.txtReservierungsNr.setEnabled(!searchByName);
	TicketSucheView.this.txtNachname.setEnabled(searchByName);
	TicketSucheView.this.txtVorname.setEnabled(searchByName);
	// TicketSucheView.this.btnSucheAuffuehrung.setEnabled(searchByName);
	TicketSucheView.this.cvAuffuehrung.getCombo().setEnabled(searchByName);
	TicketSucheView.this.lblNachname.setEnabled(searchByName);
	TicketSucheView.this.lblVorname.setEnabled(searchByName);
	TicketSucheView.this.lblAuffuehrung.setEnabled(searchByName);

	if (searchByName) {
	    TicketSucheView.this.txtReservierungsNr.setText("");
	    TicketSucheView.this.txtNachname.setFocus();
	    TicketSucheView.this.txtNachname.setSelection(0,
		    TicketSucheView.this.txtNachname.getText().length());
	} else {
	    TicketSucheView.this.txtNachname.setText("");
	    TicketSucheView.this.txtVorname.setText("");
	    // TicketSucheView.this.cvAuffuehrung.setSelection(null);
	    TicketSucheView.this.txtReservierungsNr.setFocus();
	    TicketSucheView.this.txtReservierungsNr.setSelection(0,
		    TicketSucheView.this.txtReservierungsNr.getText().length());
	}

	TicketSucheView.this.searchHandler();
    }

    private void createTable(Composite c) {
	this.tableViewer = new TableViewer(c, SWT.BORDER | SWT.FULL_SELECTION);
	this.tableViewer.getTable().setLayoutData(
		new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	TableLayout layout = new TableLayout();
	layout.addColumnData(new ColumnWeightData(12, 20, true));
	layout.addColumnData(new ColumnWeightData(22, 100, true));
	layout.addColumnData(new ColumnWeightData(22, 100, true));
	layout.addColumnData(new ColumnWeightData(22, 100, true));
	layout.addColumnData(new ColumnWeightData(22, 100, true));
	this.tableViewer.getTable().setLayout(layout);

	this.tableViewer.getTable().setLinesVisible(true);
	this.tableViewer.getTable().setHeaderVisible(true);

	this.tableViewer.setContentProvider(this.ticketProvider);
	this.tableViewer.setLabelProvider(new ITableLabelProvider() {

	    @Override
	    public Image getColumnImage(Object arg0, int arg1) {
		return null;
	    }

	    @Override
	    public String getColumnText(Object element, int index) {
		Transaktion t = (Transaktion) element;
		String text = "";
		switch (index) {
		case 0: // Res-Nr.
		    if (t.getReservierungsnr() == null)
			return null;
		    return t.getReservierungsnr().toString();
		case 1: // Status
		    if (t.getStatus() == null)
			return null;
		    switch (t.getStatus()) {
		    case BUCHUNG:
			return "Verkauf";
		    case RESERVIERUNG:
			return "Reservierung";
		    case STORNO:
			return "Storno";
		    default:
			return null;
		    }
		case 2: // Kunde
		    if (t.getKunde() != null) {
			if (t.getKunde().getNachname() != null) {
			    text += t.getKunde().getNachname() + " ";
			}
			if (t.getKunde().getVorname() != null) {
			    text += t.getKunde().getVorname();
			}
		    }
		    return text;
		case 3: // Datum/Uhrzeit
		    if (t.getDatumuhrzeit() != null)
			return t.getDatumuhrzeit().toString();
		case 4: // Mitarbeiter
		    if (t.getMitarbeiter() != null) {
			if (t.getMitarbeiter().getNachname() != null) {
			    text += t.getMitarbeiter().getNachname() + " ";
			}
			if (t.getMitarbeiter().getVorname() != null) {
			    text += t.getMitarbeiter().getVorname();
			}
		    }
		    return text;
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

	TableColumn colResNr = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colResNr.setText("Reservierungs-Nr.");
	TableColumn colStatus = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colStatus.setText("Status");
	TableColumn colKunde = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colKunde.setText("Kunde");
	TableColumn colDatumZeit = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colDatumZeit.setText("Datum/Uhrzeit");
	TableColumn colMitarbeiter = new TableColumn(
		this.tableViewer.getTable(), SWT.LEFT);
	colMitarbeiter.setText("Mitarbeiter");

	this.tableViewer
		.addSelectionChangedListener(new ISelectionChangedListener() {

		    public void selectionChanged(SelectionChangedEvent event) {
			// Handle selection changed event here
			IStructuredSelection selection = (IStructuredSelection) event
				.getSelection();
			if (selection.isEmpty())
			    return;
			Transaktion t = (Transaktion) selection
				.getFirstElement();
			if (t.getStatus().equals(Transaktionsstatus.BUCHUNG)) {
			    TicketSucheView.this.btnStornieren.setEnabled(true);
			    TicketSucheView.this.btnBearbeiten
				    .setEnabled(false);
			    TicketSucheView.this.btnVerkaufen.setEnabled(false);
			} else if (t.getStatus().equals(
				Transaktionsstatus.RESERVIERUNG)) {
			    TicketSucheView.this.btnStornieren.setEnabled(true);
			    TicketSucheView.this.btnBearbeiten.setEnabled(true);
			    TicketSucheView.this.btnVerkaufen.setEnabled(true);
			} else {
			    TicketSucheView.this.btnStornieren
				    .setEnabled(false);
			    TicketSucheView.this.btnBearbeiten
				    .setEnabled(false);
			    TicketSucheView.this.btnVerkaufen.setEnabled(false);
			}
		    }

		});

	this.tableViewer.addDoubleClickListener(new IDoubleClickListener() {

	    @Override
	    public void doubleClick(DoubleClickEvent event) {

		//bearbeitenHandler();
	    }
	});

	this.getSite().setSelectionProvider(this.tableViewer);
    }

    private void createButtons(Composite parent) {

	Composite c = new Composite(parent, SWT.RIGHT);
	c.setLayout(new GridLayout(3, false));
	c.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

	this.btnVerkaufen = new Button(c, SWT.PUSH);
	btnVerkaufen.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
		false));
	btnVerkaufen.setText(" Verkaufen ");
	btnVerkaufen.setToolTipText("Reservierte Tickets verkaufen");
	this.btnBearbeiten = new Button(c, SWT.PUSH);
	btnBearbeiten.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
		false));
	btnBearbeiten.setText(" Bearbeiten ");
	btnBearbeiten.setToolTipText("Reservierte Tickets bearbeiten");
	this.btnStornieren = new Button(c, SWT.PUSH);
	btnStornieren.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
		false));
	btnStornieren.setText(" Stornieren ");
	btnStornieren.setToolTipText("Verkauf oder Reservierung stornieren");

	this.btnBearbeiten.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {

		bearbeitenHandler();
	    }
	});
	this.btnVerkaufen.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {

		if (!TicketSucheView.this.verifyTicketSelected())
		    return;

		IStructuredSelection structuredSelection = (IStructuredSelection) tableViewer
			.getSelection();
		Transaktion t = (Transaktion) structuredSelection
			.getFirstElement();
		openTicketEditor(t, true);

	    }
	});
	this.btnStornieren.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {

		if (!TicketSucheView.this.verifyTicketSelected())
		    return;

		IStructuredSelection structuredSelection = (IStructuredSelection) tableViewer
			.getSelection();
		Transaktion t = (Transaktion) structuredSelection
			.getFirstElement();
		MessageDialog dialog = new MessageDialog(null, "Title", null,
			"Wollen Sie die Tickets wirklich Stornieren?",
			MessageDialog.QUESTION, new String[] { "Yes", "No",
				"Cancel" }, 0); // yes is
						// the
						// default
		int result = dialog.open();
		if (result == 0) {
		    transaktionService.storniereTransaktion(t);
		    MessageDialog.openInformation(TicketSucheView.this
			    .getSite().getWorkbenchWindow().getShell(),
			    "Ticket storniert",
			    "Ticket wurde erfolgreich storniert");
		    TicketSucheView.this.foundTickets.remove(t);
		    tableViewer.setInput(new Object());
		}

	    }
	});
    }

    private void bearbeitenHandler() {
	if (!TicketSucheView.this.verifyTicketSelected())
	    return;

	IStructuredSelection structuredSelection = (IStructuredSelection) tableViewer
		.getSelection();
	Transaktion t = (Transaktion) structuredSelection.getFirstElement();
	openTicketEditor(t, false);
    }

    private void openTicketEditor(Transaktion t, boolean verkauf) {

	TicketVerkaufEditorInput editorInput = new TicketVerkaufEditorInput(t,
		verkauf);

	// Editor aufrufen
	try {
	    this.getSite().getPage()
		    .openEditor(editorInput, TicketVerkaufEditor.ID);
	} catch (PartInitException e) {
	    log.error("Unable to open Ticket-Editor: " + e.getMessage());
	    return;
	}
    }

    public boolean verifyTicketSelected() {
	IStructuredSelection structuredSelection = (IStructuredSelection) tableViewer
		.getSelection();
	if (structuredSelection.isEmpty()) {
	    MessageDialog.openError(TicketSucheView.this.getSite()
		    .getWorkbenchWindow().getShell(), "Ticket auswählen",
		    "Kein Ticket ausgewählt. Bitte wählen sie ein gültiges"
			    + " Ticket aus.");
	}
	return true;
    }

    @Override
    public void setFocus() {
	 if(c != null) // Context sensitive help
	      c.setFocus(); // context sensitive help
	if (btnReservierungsNr.getSelection())
	    this.txtReservierungsNr.setFocus();
	else
	    this.txtNachname.setFocus();
	
	searchHandler();
    }

    /**
     * Liest die aktuellen Informationen aus den Eingabefeldern, sucht aufgrund
     * der erhaltenen Informationen die Tickets und updated die aktuelle
     * Ergebnis-Liste und Tabelle
     */
    @Override
    public void searchHandler() {
	Transaktion t = null;
	this.foundTickets.clear();

	if (this.btnReservierungsNr.getSelection() == true) {
	    // suche nach Reservierungsnummer
	    int resNr = 0;
	    if (this.txtReservierungsNr.getText().length() > 0) {
		try {
		    resNr = Integer.parseInt(this.txtReservierungsNr.getText()
			    .trim());
		} catch (Exception e) {
		    MessageDialog
			    .openError(TicketSucheView.this.getSite()
				    .getWorkbenchWindow().getShell(),
				    "Ungültige Reservierungsnummer",
				    "Bitte geben Sie eine gültige Reservierungs-Nr. ein.");
		    return;
		}
	    }
	    if (resNr > 0) {
		t = transaktionService.findTransaktionByResNr(resNr);
		if (t != null)
		    this.foundTickets.add(t);
	    } else
		this.foundTickets.addAll(transaktionService.findAll());

	} else {
	    // suche nach Name und Aufführung
	    // Aufführung:
	    /*
	     * if (this.cvAuffuehrung.getCombo().getSelectionIndex() < 0) {
	     * MessageDialog.openError(TicketSucheView.this.getSite()
	     * .getWorkbenchWindow().getShell(), "Ungültige Aufführung",
	     * "Bitte wählen Sie eine Aufführung aus."); return; }
	     */
	    IStructuredSelection structuredSelection = (IStructuredSelection) this.cvAuffuehrung
		    .getSelection();
	    Auffuehrung a = (Auffuehrung) structuredSelection.getFirstElement();

	    // Kunde
	    Kunde kunde = new Kunde();
	    kunde.setGruppe(null);
	    String n = this.txtNachname.getText().trim();
	    String v = this.txtVorname.getText().trim();
	    if (n.length() > 0) {
		kunde.setNachname(n);
	    }
	    if (v.length() > 0) {
		kunde.setVorname(v);
	    }

	    List<Kunde> klist = kundeService.sucheKunde(kunde);
	    // es könnte sein, dass mehrere Kunden mit demselben Namen gefunden
	    // wurden

	    if (kunde.getVorname() == null && kunde.getNachname() == null) {
		// nach keinem speziellen kunden gesucht --> Suche auch nach
		// anonymen Kunden
		for (Transaktion temp : transaktionService
			.findTransaktionByAnonymUndAuffuehrung(a)) {
		    this.foundTickets.add(temp);
		}
	    } else {
		for (Kunde k : klist) {
		    // für alle Kunden werden die aktuellen Transaktionen
		    // gesucht
		    for (Transaktion temp : transaktionService
			    .findTransaktionByKundeUndAuffuehrung(k, a)) {
			this.foundTickets.add(temp);
		    }
		}
	    }

	}
	// Table updaten
	TicketSucheView.this.tableViewer.setInput(new Object());
    }

    @Override
    public void enableInstantSearch() {

	lblSearchSpacer.dispose();
	btnSearch.dispose();

	InstantSearch.addInstantSearch(this.txtReservierungsNr, this);
	InstantSearch.addInstantSearch(this.txtNachname, this);
	InstantSearch.addInstantSearch(this.txtVorname, this);
	InstantSearch.addInstantSearch(this.cvAuffuehrung, this);

	searchHandler();
    }

    /**
     * öffnet den Ticket-Editor mit den aktuellen Informationen über die
     * Transaktion
     */
    public void openTicketEditor() {
	// TODO
    }

    /*
     * class KuenstlerContentProvider implements IStructuredContentProvider {
     * private KuenstlerDao kuenstlerDao = null;
     * 
     * @Override public Object[] getElements(Object query) { if ((query
     * instanceof Kuenstler) == false) { KuenstlerSucheView.this.log.info(
     * "Query object not of type Kuenstler: #0", query .getClass().getName());
     * return new Object[0]; } try { return
     * this.kuenstlerDao.findByKuenstler((Kuenstler) query) .toArray();
     * 
     * } catch (Exception e) { KuenstlerSucheView.this.log.info(
     * "Exception in KuenstlerContentProvider: #0", e .getMessage()); return new
     * Object[0]; } }
     * 
     * public void setKuenstlerDao(KuenstlerDao kuenstlerDao) {
     * this.kuenstlerDao = kuenstlerDao; }
     * 
     * @Override public void dispose() { }
     * 
     * @Override public void inputChanged(Viewer arg0, Object arg1, Object arg2)
     * { }
     * 
     * }
     */

    /**
     * Klasse die für den Input der Tabelle verantwortlich ist
     */
    class TicketContentProvider implements IStructuredContentProvider {
	@Override
	public Object[] getElements(Object query) {
	    return TicketSucheView.this.foundTickets.toArray();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}
    }

    /**
     * Klasse, die für den Inhalt der Aufführung-Combobox verantwortlich ist
     */
    class AuffuehrungContentProvider implements IStructuredContentProvider {
	private AuffuehrungService auffuehrungService = null;

	@Override
	public Object[] getElements(Object query) {
	    if ((query instanceof Auffuehrung) == true) {
		// Content für die Saal Combobox
		// egal welcher Saal übergeben wird, es werden alle
		// Veranstaltungen
		// als Content zurückgeliefert, da die ComboBox nur eine Liste
		// aller Veranst. ist
		List<Auffuehrung> aList;
		try {
		    aList = auffuehrungService
			    .findAuffuehrungen((Auffuehrung) query);
		    aList.add(0, new Auffuehrung());

		} catch (ValidateException e) {
		    MessageDialog.openInformation(TicketSucheView.this
			    .getSite().getWorkbenchWindow().getShell(),
			    "Ungültiges Datum",
			    "Der ausgewählte Zeitpunkt ist bereits vorbei");
		    return (new ArrayList<Auffuehrung>()).toArray();
		}
		return aList.toArray();

	    } else {
		TicketSucheView.this.log.info(
			"Query object not of type Aufführung: #0", query
				.getClass().getName());
		return new Object[0];
	    }
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void setAuffuehrungService(AuffuehrungService auffuehrungService) {
	    this.auffuehrungService = auffuehrungService;
	}
    }

}
