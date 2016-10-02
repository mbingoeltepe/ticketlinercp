package at.ticketline.kassa.ui.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
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

import at.ticketline.entity.Artikel;
import at.ticketline.entity.BestellPosition;
import at.ticketline.entity.Bestellung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.entity.Zahlungsart;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.ArtikelServiceImpl;
import at.ticketline.service.BestellungServiceImpl;
import at.ticketline.service.KundeServiceImpl;
import at.ticketline.service.MitarbeiterServiceImpl;
import at.ticketline.service.VeranstaltungServiceImpl;
import at.ticketline.service.interfaces.ArtikelService;
import at.ticketline.service.interfaces.BestellungService;
import at.ticketline.service.interfaces.KundeService;
import at.ticketline.service.interfaces.MitarbeiterService;
import at.ticketline.service.interfaces.VeranstaltungService;

public class ShopEditor extends EditorPart implements INeedLogin {
    public static final String ID = "at.ticketline.editor.shop";
    protected Logger log = LogFactory.getLogger(this.getClass());

    // Service
    MitarbeiterService mitarbeiterService;
    BestellungService bestellungService;
    KundeService kundeService;
    VeranstaltungService veranstaltungService;
    ArtikelService artikelService;

    private FormToolkit toolkit;
    private ScrolledForm form;
    private boolean dirty = false;

    private Combo cbKundeSuchen;
    private Point previousKundeSuchenTextSelection;
    private Combo cbVeranstaltungSuchen;
    private Point previousVeranstaltungSuchenTextSelection;

    private Button btnArtikelZumWarenkorb;
    private Button btnArtikelAnzahlVerringern;
    private Button btnCheckout;

    private Button checkBoxPayed;
    private Label lblSumme;

    private Combo cbPayment;
    private Kunde kunde;
    private ArrayList<Kunde> kunden = null;
    // private boolean articlesPayed = false;

    private ArrayList<WarenkorbArtikel> selectedArtikels = new ArrayList<WarenkorbArtikel>();
    public TableViewer tableViewer;
    public TableViewer tableWarenkorbViewer;

    // content provider für den Table
    protected ArtikelContentProvider artikelProvider = new ArtikelContentProvider();
    protected WarenkorbContentProvider warenkorbProvider = new WarenkorbContentProvider();

    private Composite c;

    public ShopEditor() {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input)
	    throws PartInitException {
	this.setSite(site);
	this.setInput(input);

	// erzeuge Services
	this.mitarbeiterService = new MitarbeiterServiceImpl();
	this.bestellungService = new BestellungServiceImpl();
	this.kundeService = new KundeServiceImpl();
	this.veranstaltungService = new VeranstaltungServiceImpl();
	this.artikelService = new ArtikelServiceImpl();

    }

    @Override
    public void createPartControl(Composite parent) {
	this.c = parent;
	parent.setLayout(new GridLayout(1, false));

	this.toolkit = new FormToolkit(parent.getDisplay());
	this.form = this.toolkit.createScrolledForm(parent);
	this.form.setLayoutData(new GridData(GridData.FILL_BOTH));
	this.form.getBody().setLayout(new GridLayout(1, false));
	this.form.setText("Shop");

	this.createForm(this.form.getBody());

	PlatformUI.getWorkbench().getHelpSystem()
		.setHelp(c, "TicketlineRCP.shop"); // contextsensitive hilfe

	// this.createButton(this.form.getBody());
    }

    protected void createForm(Composite parent) {

	Composite c = this.toolkit.createComposite(parent);
	c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	// gesamtes Layout horizontal geteilt:
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
		ShopEditor.this.form.reflow(true);
	    }
	});
	leftSection.setText("Auswahl");
	leftSection.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));

	Composite left = this.toolkit.createComposite(leftSection);
	left.setLayout(new GridLayout(1, false));

	// Die eingerahmten Auswahl - Widgets für Kunde und Veranstaltung -
	// Suche
	Group auswahlGroup = new Group(left, SWT.BORDER_SOLID);
	auswahlGroup.setText("Daten auswählen");
	auswahlGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false,
		2, 1));
	auswahlGroup.setLayout(new GridLayout(2, false));
	Button btnNeuerKunde = new Button(auswahlGroup, SWT.PUSH);
	btnNeuerKunde.setText("Neuen Kunden anlegen");
	btnNeuerKunde.addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {
		IHandlerService handlerService = (IHandlerService) ShopEditor.this
			.getSite().getService(IHandlerService.class);
		try {
		    handlerService.executeCommand(
			    "at.ticketline.command.CreateKunde", null);
		} catch (Exception ex) {
		    MessageDialog.openError(
			    ShopEditor.this.getSite().getWorkbenchWindow()
				    .getShell(),
			    "Error",
			    "Neuer Kunde kann nicht angelegt werden: "
				    + ex.getMessage());
		}

	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	    }
	});

	@SuppressWarnings("unused")
	Label spacer = new Label(auswahlGroup, SWT.NULL);

	Label lblKundeSuchen = new Label(auswahlGroup, SWT.LEFT);
	lblKundeSuchen.setText("Kunde Suchen: ");

	cbKundeSuchen = new Combo(auswahlGroup, SWT.LEFT | SWT.BORDER
		| SWT.DROP_DOWN);
	cbKundeSuchen.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	cbKundeSuchen.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		kundeSearchHandler();
	    }
	});
	cbKundeSuchen.addListener(SWT.Selection, new Listener() {

	    @Override
	    public void handleEvent(Event event) {
		kundeSearchHandler();

	    }
	});
	cbKundeSuchen.addListener(SWT.CHANGED, new Listener() {

	    @Override
	    public void handleEvent(Event event) {
		// nur wenn etwas eingegeben wurde:
		if (event.character != 0) {
		    // System.out.println(event.character);
		    // wenn Backspace Taste gedrückt wurde (Zeichen löschen)
		    if (event.character == '\b') {
			if (cbKundeSuchen.getText().length() > 0
				&& previousKundeSuchenTextSelection.x != previousKundeSuchenTextSelection.y) {
			    cbKundeSuchen
				    .setText(cbKundeSuchen.getText()
					    .substring(
						    0,
						    cbKundeSuchen.getText()
							    .length() - 1));
			}
		    }
		    kundeSearchHandler();
		    previousKundeSuchenTextSelection = cbKundeSuchen
			    .getSelection();
		}
	    }
	});
	cbKundeSuchen.addListener(SWT.INSERT, new Listener() {

	    @Override
	    public void handleEvent(Event event) {
		kundeSearchHandler();
	    }
	});

	Label lblVeranstaltungSuchen = new Label(auswahlGroup, SWT.LEFT);
	lblVeranstaltungSuchen.setText("Veranstaltung Suchen: ");

	cbVeranstaltungSuchen = new Combo(auswahlGroup, SWT.LEFT | SWT.BORDER);
	cbVeranstaltungSuchen.setLayoutData(new GridData(
		GridData.FILL_HORIZONTAL));
	cbVeranstaltungSuchen.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		veranstaltungSearchHandler();
	    }
	});
	cbVeranstaltungSuchen.addListener(SWT.CHANGED, new Listener() {

	    @Override
	    public void handleEvent(Event event) {
		// nur wenn etwas eingegeben wurde:
		if (event.character != 0) {
		    // System.out.println(event.character);
		    // wenn Backspace Taste gedrückt wurde (Zeichen löschen)
		    if (event.character == '\b') {
			if (cbVeranstaltungSuchen.getText().length() > 0
				&& previousVeranstaltungSuchenTextSelection.x != previousVeranstaltungSuchenTextSelection.y) {
			    cbVeranstaltungSuchen.setText(cbVeranstaltungSuchen
				    .getText().substring(
					    0,
					    cbVeranstaltungSuchen.getText()
						    .length() - 1));
			}
		    }
		    veranstaltungSearchHandler();
		    previousVeranstaltungSuchenTextSelection = cbVeranstaltungSuchen
			    .getSelection();
		}
	    }
	});
	cbVeranstaltungSuchen.addListener(SWT.INSERT, new Listener() {

	    @Override
	    public void handleEvent(Event event) {
		veranstaltungSearchHandler();
	    }
	});
	cbVeranstaltungSuchen.addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {
		updateArtikelTable();
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	    }
	});

	leftSection.setClient(left);

	// rechts --> Warenkorb
	Section rightSection = this.toolkit.createSection(c,
		Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
			| Section.EXPANDED);
	rightSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		ShopEditor.this.form.reflow(true);
	    }
	});
	rightSection.setText("Warenkorb");
	rightSection.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));
	Composite right = this.toolkit.createComposite(rightSection);
	right.setLayout(new GridLayout(1, false));

	right.setLayout(new GridLayout(1, false));

	rightSection.setClient(right);

	// workaround damit scrollbar angezeigt wird
	leftSection.setExpanded(false);
	leftSection.setExpanded(true);
	rightSection.setExpanded(false);
	rightSection.setExpanded(true);

	this.createTable(left);

	btnArtikelZumWarenkorb = new Button(left, SWT.PUSH);
	btnArtikelZumWarenkorb.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL,
		true, false, 1, 1));
	btnArtikelZumWarenkorb.setText("Artikel zum Warenkorb hinzufügen -->");
	btnArtikelZumWarenkorb.addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {
		addItemToCart();
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	    }
	});

	this.createWarentkorbTable(right);

	lblSumme = new Label(right, SWT.RIGHT);
	lblSumme.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false,
		1, 1));
	lblSumme.setText("Summe:                      ");

	btnArtikelAnzahlVerringern = new Button(right, SWT.PUSH);
	btnArtikelAnzahlVerringern.setLayoutData(new GridData(SWT.LEFT,
		SWT.FILL, true, false, 1, 1));
	btnArtikelAnzahlVerringern.setText("<-- Artikel Anzahl verringern");
	btnArtikelAnzahlVerringern
		.addSelectionListener(new SelectionListener() {

		    @Override
		    public void widgetSelected(SelectionEvent e) {
			decrementItemCount();
		    }

		    @Override
		    public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		    }
		});

	this.createComboPayment(right);

	checkBoxPayed = new Button(right, SWT.CHECK);
	checkBoxPayed.setText("Bezahlt");

	btnCheckout = new Button(right, SWT.RIGHT);
	btnCheckout.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true,
		false, 1, 1));
	btnCheckout.setText("CHECKOUT");
	btnCheckout.addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {
		checkout();
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	    }
	});

    }

    private void createComboPayment(Composite c) {
	cbPayment = new Combo(c, SWT.FLAT | SWT.READ_ONLY | SWT.BORDER);

	cbPayment.setItems(Zahlungsart.toStringArray());
	cbPayment.select(Zahlungsart.toStringArray().length - 1);
    }

    private void createWarentkorbTable(Composite c) {
	Group warenkorbGroup = new Group(c, SWT.BORDER_SOLID);
	warenkorbGroup.setText("Warenkorb");
	warenkorbGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		true, 1, 1));
	warenkorbGroup.setLayout(new GridLayout(1, false));

	this.tableWarenkorbViewer = new TableViewer(warenkorbGroup, SWT.BORDER
		| SWT.FULL_SELECTION);
	this.tableWarenkorbViewer.getTable().setLayoutData(
		new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	TableLayout layout = new TableLayout();
	layout.addColumnData(new ColumnWeightData(50, 100, true));
	layout.addColumnData(new ColumnWeightData(50, 100, true));
	layout.addColumnData(new ColumnWeightData(50, 100, true));
	layout.addColumnData(new ColumnWeightData(50, 100, true));
	this.tableWarenkorbViewer.getTable().setLayout(layout);

	this.tableWarenkorbViewer.getTable().setLinesVisible(true);
	this.tableWarenkorbViewer.getTable().setHeaderVisible(true);
	this.tableWarenkorbViewer.setContentProvider(warenkorbProvider);
	this.tableWarenkorbViewer.setLabelProvider(new ITableLabelProvider() {

	    @Override
	    public void removeListener(ILabelProviderListener listener) {

	    }

	    @Override
	    public boolean isLabelProperty(Object element, String property) {
		return true;
	    }

	    @Override
	    public void dispose() {

	    }

	    @Override
	    public void addListener(ILabelProviderListener listener) {

	    }

	    @Override
	    public String getColumnText(Object element, int columnIndex) {
		WarenkorbArtikel artikel = (WarenkorbArtikel) element;
		switch (columnIndex) {
		case 0:
		    return artikel.getBeschreibung();
		case 1:
		    return "" + artikel.getAnzahl();
		case 2:
		    return "€ " + artikel.getPreis();
		case 3:
		    return "€ " + artikel.getSumme();
		}
		return null;
	    }

	    @Override
	    public Image getColumnImage(Object element, int columnIndex) {
		return null;
	    }
	});
	TableColumn colArtikel = new TableColumn(
		this.tableWarenkorbViewer.getTable(), SWT.LEFT);
	colArtikel.setText("Artikel");
	TableColumn colPreis = new TableColumn(
		this.tableWarenkorbViewer.getTable(), SWT.LEFT);
	colPreis.setText("Anzahl");
	TableColumn colKategorie = new TableColumn(
		this.tableWarenkorbViewer.getTable(), SWT.LEFT);
	colKategorie.setText("Stueckpreis");
	TableColumn colVeranstaltung = new TableColumn(
		this.tableWarenkorbViewer.getTable(), SWT.LEFT);
	colVeranstaltung.setText("Summe");

	this.getSite().setSelectionProvider(this.tableWarenkorbViewer);
    }

    private void createTable(Composite c) {

	Group artikelGroup = new Group(c, SWT.BORDER_SOLID);
	artikelGroup.setText("Artikel");
	artikelGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
		1, 1));
	artikelGroup.setLayout(new GridLayout(1, false));

	this.tableViewer = new TableViewer(artikelGroup, SWT.BORDER
		| SWT.FULL_SELECTION);
	this.tableViewer.getTable().setLayoutData(
		new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	TableLayout layout = new TableLayout();
	layout.addColumnData(new ColumnWeightData(50, 100, true));
	layout.addColumnData(new ColumnWeightData(50, 100, true));
	layout.addColumnData(new ColumnWeightData(50, 100, true));
	layout.addColumnData(new ColumnWeightData(50, 100, true));
	this.tableViewer.getTable().setLayout(layout);

	this.tableViewer.getTable().setLinesVisible(true);
	this.tableViewer.getTable().setHeaderVisible(true);

	this.tableViewer.setContentProvider(this.artikelProvider);
	this.tableViewer.setLabelProvider(new ITableLabelProvider() {

	    @Override
	    public Image getColumnImage(Object arg0, int arg1) {
		return null;
	    }

	    @Override
	    public String getColumnText(Object element, int index) {
		Artikel a = (Artikel) element;
		switch (index) {
		case 0:
		    return a.getBeschreibung();
		case 1:
		    return "€ " + a.getPreis();
		case 2:
		    return a.getKategorie().name();
		case 3:
		    return a.getVeranstaltung().getBezeichnung();
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
	this.tableViewer.addDoubleClickListener(new IDoubleClickListener() {
	    @Override
	    public void doubleClick(DoubleClickEvent event) {
		ShopEditor.this.addItemToCart();
	    }
	});

	TableColumn colArtikel = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colArtikel.setText("Artikel");
	TableColumn colPreis = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colPreis.setText("Preis");
	TableColumn colKategorie = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colKategorie.setText("Kategorie");
	TableColumn colVeranstaltung = new TableColumn(
		this.tableViewer.getTable(), SWT.LEFT);
	colVeranstaltung.setText("Veranstaltung");

	this.getSite().setSelectionProvider(this.tableViewer);

    }

    /**
     * Behandelt die Kunden - Suchanfrage
     */
    private void kundeSearchHandler() {

	String searchString = cbKundeSuchen.getText();

	// System.out.println("search called: " + searchString);

	ArrayList<String> itemCollection = new ArrayList<String>();

	for (int i = 0; i < cbKundeSuchen.getItems().length; i++) {
	    itemCollection.add(cbKundeSuchen.getItem(i));
	}

	if (!itemCollection.contains(searchString)) {

	    kunden = new ArrayList<Kunde>();
	    ArrayList<String> kundenStrings = new ArrayList<String>();

	    cbKundeSuchen.removeAll();

	    Kunde suchKunde = new Kunde();
	    if (searchString.split(" ").length == 2) {
		suchKunde.setNachname(searchString.split(" ")[0] + "*");
		suchKunde.setVorname(searchString.split(" ")[1] + "*");
	    } else {
		suchKunde.setNachname(searchString + "*");
		suchKunde.setVorname("*");
	    }

	    kunden.addAll(kundeService.sucheKunde(suchKunde));
	    for (Kunde kunde : kunden) {
		kundenStrings.add(kunde.getNachname() + " "
			+ kunde.getVorname());
	    }

	    if (searchString.split(" ").length == 2) {
		suchKunde.setVorname(searchString.split(" ")[0] + "*");
		suchKunde.setNachname(searchString.split(" ")[1] + "*");
	    } else {
		suchKunde.setVorname(searchString + "*");
		suchKunde.setNachname("*");
	    }
	    for (Kunde kunde : kundeService.sucheKunde(suchKunde)) {
		if (!kunden.contains(kunde)) {
		    kunden.add(kunde);
		    kundenStrings.add(kunde.getVorname() + " "
			    + kunde.getNachname());
		}
	    }

	    Collections.sort(kundenStrings);

	    String[] items = new String[kundenStrings.size()];
	    for (int i = 0; i < kundenStrings.size(); i++) {
		items[i] = kundenStrings.get(i);
	    }

	    if (items.length > 0) {
		cbKundeSuchen.setItems(items);
		if (searchString.length() > 0) {
		    cbKundeSuchen.setText(items[0]);
		    cbKundeSuchen.setSelection(new Point(searchString.length(),
			    items[0].length()));
		}
	    } else {
		cbKundeSuchen.setText(searchString);
		cbKundeSuchen.setSelection(new Point(searchString.length(),
			searchString.length()));
	    }
	}
	try {
	    for (Kunde kunde : kunden) {
		String firstPart = cbKundeSuchen.getText().split(" ")[0];
		String secondPart = cbKundeSuchen.getText().split(" ")[1];
		if (kunde.getVorname().equals(firstPart)
			&& kunde.getNachname().equals(secondPart)) {
		    this.kunde = kunde;
		} else if (kunde.getVorname().equals(secondPart)
			&& kunde.getNachname().equals(firstPart)) {
		    this.kunde = kunde;
		}
	    }
	} catch (Exception ex) {
	}

    }

    /**
     * Behandelt die Veranstaltung - Suchanfrage
     */
    private void veranstaltungSearchHandler() {

	String searchString = cbVeranstaltungSuchen.getText();

	// System.out.println("search called: " + searchString);

	ArrayList<String> itemCollection = new ArrayList<String>();

	for (int i = 0; i < cbVeranstaltungSuchen.getItems().length; i++) {
	    itemCollection.add(cbVeranstaltungSuchen.getItem(i));
	}

	if (!itemCollection.contains(searchString)) {

	    ArrayList<Veranstaltung> veranstaltungen = new ArrayList<Veranstaltung>();
	    ArrayList<String> veranstaltungenStrings = new ArrayList<String>();

	    cbVeranstaltungSuchen.removeAll();

	    Veranstaltung suchVeranstaltung = new Veranstaltung();
	    suchVeranstaltung.setBezeichnung(searchString + "*");

	    veranstaltungen.addAll(veranstaltungService
		    .findeVeranstaltungen(suchVeranstaltung));
	    for (Veranstaltung veranstaltung : veranstaltungen) {
		veranstaltungenStrings.add(veranstaltung.getBezeichnung());
	    }

	    Collections.sort(veranstaltungenStrings);

	    String[] items = new String[veranstaltungenStrings.size()];
	    for (int i = 0; i < veranstaltungenStrings.size(); i++) {
		items[i] = veranstaltungenStrings.get(i);
	    }

	    if (items.length > 0) {
		cbVeranstaltungSuchen.setItems(items);
		if (searchString.length() > 0) {
		    cbVeranstaltungSuchen.setText(items[0]);
		    cbVeranstaltungSuchen.setSelection(new Point(searchString
			    .length(), items[0].length()));
		}
	    } else {
		cbVeranstaltungSuchen.setText(searchString);
		cbVeranstaltungSuchen.setSelection(new Point(searchString
			.length(), searchString.length()));
	    }
	}

	updateArtikelTable();
    }

    protected Artikel createArtikelQuery() {

	VeranstaltungService veranstaltungService = new VeranstaltungServiceImpl();

	Artikel artikel = new Artikel();
	Veranstaltung veranstaltung = new Veranstaltung();

	if (cbVeranstaltungSuchen.getText().length() > 0) {
	    String v = this.cbVeranstaltungSuchen.getText();

	    veranstaltung.setBezeichnung(v);

	    if (veranstaltungService.findeVeranstaltungen(veranstaltung).size() == 1) {
		artikel.setVeranstaltung(veranstaltungService
			.findeVeranstaltungen(veranstaltung).get(0));
	    }
	}

	return artikel;
    }

    private void updateArtikelTable() {
	Artikel a = ShopEditor.this.createArtikelQuery();
	ShopEditor.this.tableViewer.setInput(a);
    }

    private void addItemToCart() {

	Artikel artikel = (Artikel) ((IStructuredSelection) this.tableViewer
		.getSelection()).getFirstElement();
	WarenkorbArtikel wArtikel = new WarenkorbArtikel();
	wArtikel.setAbbildung(artikel.getAbbildung());
	wArtikel.setBeschreibung(artikel.getBeschreibung());
	wArtikel.setBestellPositionen(artikel.getBestellPositionen());
	wArtikel.setId(artikel.getId());
	wArtikel.setKategorie(artikel.getKategorie());
	wArtikel.setKurzbezeichnung(artikel.getKurzbezeichnung());
	wArtikel.setPreis(artikel.getPreis());
	wArtikel.setVeranstaltung(artikel.getVeranstaltung());
	boolean alreadyIn = false;
	for (WarenkorbArtikel sArtikel : selectedArtikels) {
	    if (sArtikel.getId() == wArtikel.getId()) {
		sArtikel.anzahl++;
		alreadyIn = true;
	    }
	}
	if (!alreadyIn)
	    selectedArtikels.add(wArtikel);

	tableWarenkorbViewer.setInput(selectedArtikels);

	updateSumme();

	// System.out.println("artikel hinzu: " + artikel.getBeschreibung());

    }

    private void decrementItemCount() {

	WarenkorbArtikel wArtikel = (WarenkorbArtikel) ((IStructuredSelection) this.tableWarenkorbViewer
		.getSelection()).getFirstElement();

	if (wArtikel.anzahl > 1) {
	    (selectedArtikels.get(selectedArtikels.indexOf(wArtikel))).anzahl--;
	} else
	    selectedArtikels.remove(wArtikel);

	tableWarenkorbViewer.setInput(selectedArtikels);

	updateSumme();

    }

    private void updateSumme() {
	double summe = 0;

	for (WarenkorbArtikel w : selectedArtikels) {
	    summe += w.getSumme();
	}

	// Funktioniert bei mir leider nicht. @Stefan
	// DecimalFormat twoDForm = new DecimalFormat("#.##");

	// lblSumme.setText("Summe: € " +
	// Double.valueOf(twoDForm.format(summe)));
	lblSumme.setText("Summe: € " + summe);

    }

    private void checkout() {

	// TODO real checkout, zahlungsart check
	if (selectedArtikels.size() != 0) {
	    Bestellung bestellung = new Bestellung();

	    if (cbPayment.getItem(cbPayment.getSelectionIndex()).toUpperCase()
		    .equals(Zahlungsart.BANKEINZUG.toString()))
		bestellung.setZahlungsart(Zahlungsart.BANKEINZUG);
	    else if (cbPayment.getItem(cbPayment.getSelectionIndex())
		    .toUpperCase().equals(Zahlungsart.KREDITKARTE.toString()))
		bestellung.setZahlungsart(Zahlungsart.KREDITKARTE);
	    else if (cbPayment.getItem(cbPayment.getSelectionIndex())
		    .toUpperCase().equals(Zahlungsart.NACHNAHME.toString()))
		bestellung.setZahlungsart(Zahlungsart.NACHNAHME);
	    else if (cbPayment.getItem(cbPayment.getSelectionIndex())
		    .toUpperCase().equals(Zahlungsart.TICKETCARD.toString()))
		bestellung.setZahlungsart(Zahlungsart.TICKETCARD);
	    else if (cbPayment.getItem(cbPayment.getSelectionIndex())
		    .toUpperCase().equals(Zahlungsart.VORKASSE.toString()))
		bestellung.setZahlungsart(Zahlungsart.VORKASSE);
	    bestellung.setBestellzeitpunkt(new Date());

	    bestellung.setKunde(this.kunde);

	    if (checkBoxPayed.getSelection())
		bestellung.setBezahlt(true);
	    else
		bestellung.setBezahlt(false);

	    Bestellung dbBest = bestellungService.addBestellung(bestellung);

	    for (WarenkorbArtikel wa : selectedArtikels) {
		BestellPosition bp = new BestellPosition();

		Artikel a = new Artikel();
		a.setBeschreibung(wa.getBeschreibung());
		a.setKategorie(wa.getKategorie());
		a.setKurzbezeichnung(wa.getKurzbezeichnung());
		a.setPreis(wa.getPreis());
		a.setVeranstaltung(wa.getVeranstaltung());

		List<Artikel> dbArticles = artikelService.findyByArtikel(a);
		if (dbArticles.size() == 1) {
		    bp.setArtikel(dbArticles.get(0));
		}
		bp.setBestellung(dbBest);
		bp.setMenge(wa.getAnzahl());
		bestellungService.addBestellposition(bp);
	    }

	    MessageDialog.openInformation(
		    this.getSite().getShell(),
		    "Artikel verkauft",
		    "Artikel wurden erfolgreich verkauft \nPreis: €"
			    + lblSumme.getText());

	    selectedArtikels.clear();
	    tableWarenkorbViewer.setInput(selectedArtikels);

	    updateSumme();
	}

	else {
	    MessageDialog.openInformation(this.getSite().getShell(),
		    "Keine Artikel", "Es sind keine Artikel im Einkaufswagen");
	}
    }

    @Override
    public void setFocus() {
	if (c != null) // Context sensitive help
	    c.setFocus(); // context sensitive help
	cbKundeSuchen.setFocus();
	cbKundeSuchen.setSelection(new Point(0, cbKundeSuchen.getText()
		.length()));
	kundeSearchHandler();
	veranstaltungSearchHandler();
	
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
	this.log.info("Speichern");
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

    class WarenkorbContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
	    // TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	    // TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
	    return selectedArtikels.toArray();
	}

    }

    class ArtikelContentProvider implements IStructuredContentProvider {
	private ArtikelService artikelService = null;

	@Override
	public Object[] getElements(Object query) {
	    artikelService = new ArtikelServiceImpl();
	    if ((query instanceof Artikel) == false) {
		ShopEditor.this.log.info("Query object not of type Kunde: #0",
			query.getClass().getName());
		return new Object[0];
	    }
	    try {
		// ArrayList<Artikel> tmp = new ArrayList<Artikel>();
		// tmp.addAll(artikelService.findyByArtikel((Artikel) query));

		return artikelService.findyByArtikel((Artikel) query).toArray();

	    } catch (Exception e) {
		ShopEditor.this.log
			.info("Exception in KundeContentProvider: #0",
				e.getMessage());
		return new Object[0];
	    }
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

    }

    /**
     * Artikel inklusive anzahl und gesamtpreis
     * 
     * @author stefanvoeber
     * 
     */
    class WarenkorbArtikel extends Artikel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int anzahl = 1;

	public double getSumme() {

	    // Funktioniert bei mir leider nicht! @stefan
	    // DecimalFormat twoDForm = new DecimalFormat("#.##");
	    // String val = twoDForm.format(this.getAnzahl()
	    // * this.getPreis().doubleValue());
	    return Double.valueOf(this.getAnzahl()
		    * this.getPreis().doubleValue());
	}

	// public void setSumme(int summe) {
	// this.summe = summe;
	// }

	public int getAnzahl() {
	    return anzahl;
	}

	public void setAnzahl(int anzahl) {
	    this.anzahl = anzahl;
	}

    }

}
