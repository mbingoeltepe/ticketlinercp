package at.ticketline.kassa.ui.editor;

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.kassa.ui.view.TicketVerkaufView;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.MitarbeiterServiceImpl;
import at.ticketline.service.interfaces.MitarbeiterService;
import at.ticketline.util.EasyMessage;

/**
 * Der Editor, der für die Anzeige der gefundenen Aufführungen für ein
 * Query-Objekt verantwortlich ist. Beim Query-Objekt handelt es sich um eines
 * von: Auffuehrung, Veranstaltung, Kuenstler, Veranstaltungsort Je nach Art des
 * Query-Objektes werden entsprechende Informationen dazu angezeigt
 * 
 * @author Daniel
 * 
 */
public class AuffuehrungListeEditor extends EditorPart {
    public static final String ID = "at.ticketline.editor.auffuehrungliste";
    protected Logger log = LogFactory.getLogger(this.getClass());
    protected MitarbeiterService mitarbeiterService;

    protected AuffuehrungListeEditorInput auffuehrungListeInput = null;

    private FormToolkit toolkit;
    private ScrolledForm form;
    private Button btnSitzplan;
    private Button btnTickets;

    public TableViewer tableViewer;
    // content provider für den Table
    protected AuffuehrungContentProvider auffuehrungProvider;

    @Override
    public void init(IEditorSite site, IEditorInput input)
	    throws PartInitException {
	this.setSite(site);
	this.setInput(input);
	this.setPartName(input.getName());
	this.auffuehrungListeInput = ((AuffuehrungListeEditorInput) input); // Input
									    // für
									    // Editor
	this.auffuehrungProvider = new AuffuehrungContentProvider(); // input
								     // Provider
								     // für
								     // Tabelle
	this.auffuehrungProvider.setInput(this.auffuehrungListeInput);

	this.mitarbeiterService = new MitarbeiterServiceImpl();
    }

    @Override
    public void createPartControl(Composite parent) {
	parent.setLayout(new GridLayout(1, false));

	this.toolkit = new FormToolkit(parent.getDisplay());
	this.form = this.toolkit.createScrolledForm(parent);
	this.form.setLayoutData(new GridData(GridData.FILL_BOTH));
	this.form.getBody().setLayout(new GridLayout(1, false));
	this.form.setText(this.auffuehrungListeInput.getName());

	this.createForm(this.form.getBody());
	this.createTable(this.form.getBody());
	this.createButtons(this.form.getBody());
	
    }

    @SuppressWarnings("unused")
    private void createForm(Composite parent) {

	Composite c = this.toolkit.createComposite(parent);
	c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	ColumnLayout columnLayout = new ColumnLayout();
	columnLayout.minNumColumns = 1;
	columnLayout.maxNumColumns = 1;
	c.setLayout(columnLayout);

	// Left Side
	Section dataSection = this.toolkit.createSection(c, Section.DESCRIPTION
		| Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);

	dataSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		AuffuehrungListeEditor.this.form.reflow(true);
	    }
	});

	Composite data = this.toolkit.createComposite(dataSection);

	data.setLayout(new GridLayout(2, false));

	// jetzt muss je nach Objekt von Suchkriterien die Anzeige unterschieden
	// werden:
	if (this.auffuehrungListeInput.query instanceof Auffuehrung) {
	    // Titel des Abschnittes:
	    dataSection.setText("Suchkriterium:  Aufführung");
	    Auffuehrung a = (Auffuehrung) this.auffuehrungListeInput.query;

	    Label lblDatumUhrzeit = this.toolkit.createLabel(data,
		    "Datum/Uhrzeit: ");
	    Label lblDatumUhrzeitData;
	    if (a.getDatumuhrzeit() != null) {
		lblDatumUhrzeitData = this.toolkit.createLabel(data, a
			.getDatumuhrzeit().toString());
	    } else {
		lblDatumUhrzeitData = this.toolkit.createLabel(data, "k.A.");
	    }
	    Label lblPreis = this.toolkit.createLabel(data, "Preis: ");
	    Label lblPreisData;
	    if (a.getPreis() != null) {

		switch (a.getPreis()) {
		case MINDESTPREIS:
		    lblPreisData = this.toolkit.createLabel(data,
			    "Mindestpreis");
		    break;
		case MAXIMALPREIS:
		    lblPreisData = this.toolkit.createLabel(data,
			    "Maximalpreis");
		    break;
		case STANDARDPREIS:
		    lblPreisData = this.toolkit.createLabel(data,
			    "Standardpreis");
		    break;
		default:
		    lblPreisData = this.toolkit.createLabel(data, "k.A.");
		    break;
		}
	    } else {
		lblPreisData = this.toolkit.createLabel(data, "k.A.");
	    }
	    Label lblVeranstaltung = this.toolkit.createLabel(data,
		    "Veranstaltung: ");
	    Label lblVeranstaltungData;
	    if (a.getVeranstaltung() != null) {
		lblVeranstaltungData = this.toolkit.createLabel(data, a
			.getVeranstaltung().getBezeichnung());
	    } else {
		lblVeranstaltungData = this.toolkit.createLabel(data, "k.A.");
	    }

	    Label lblSaal = this.toolkit.createLabel(data, "Saal: ");
	    Label lblSaalData;
	    if (a.getSaal() != null) {
		lblSaalData = this.toolkit.createLabel(data, a.getSaal()
			.getBezeichnung());
	    } else {
		lblSaalData = this.toolkit.createLabel(data, "k.A.");
	    }

	}
	// Suchkriterium Künslter
	else if (this.auffuehrungListeInput.query instanceof Kuenstler) {
	    // Titel des Abschnittes:
	    dataSection.setText("Suchkriterium:  Künstler");
	    Kuenstler k = (Kuenstler) this.auffuehrungListeInput.query;

	    Label lblNachname = this.toolkit.createLabel(data, "Nachname: ");
	    Label lblNachnameData;
	    if (k.getNachname() != null) {
		lblNachnameData = this.toolkit.createLabel(data,
			k.getNachname());
	    } else {
		lblNachnameData = this.toolkit.createLabel(data, "k.A.");
	    }
	    Label lblVorname = this.toolkit.createLabel(data, "Vorname: ");
	    Label lblVornameData;
	    if (k.getVorname() != null) {
		lblVornameData = this.toolkit.createLabel(data, k.getVorname());
	    } else {
		lblVornameData = this.toolkit.createLabel(data, "k.A.");
	    }
	    Label lblGeschlecht = this.toolkit
		    .createLabel(data, "Geschlecht: ");
	    Label lblGeschlechtData;
	    if (k.getGeschlecht() != null) {
		switch (k.getGeschlecht()) {
		case MAENNLICH:
		    lblGeschlechtData = this.toolkit.createLabel(data,
			    "männlich");
		    break;
		case WEIBLICH:
		    lblGeschlechtData = this.toolkit.createLabel(data,
			    "weiblich");
		    break;
		default:
		    lblGeschlechtData = this.toolkit.createLabel(data, "k.A.");
		    break;
		}
	    } else {
		lblGeschlechtData = this.toolkit.createLabel(data, "k.A.");
	    }

	}
	// Suchkriterium Veranstaltung
	else if (this.auffuehrungListeInput.query instanceof Veranstaltung) {
	    // Titel des Abschnittes:
	    dataSection.setText("Suchkriterium:  Veranstaltung");
	    Veranstaltung v = (Veranstaltung) this.auffuehrungListeInput.query;

	    Label lblVaBezeichnung = this.toolkit.createLabel(data,
		    "Veranstaltung: ");
	    Label lblVaBezeichnungData;
	    if (v.getBezeichnung() != null) {
		lblVaBezeichnungData = this.toolkit.createLabel(data,
			v.getBezeichnung());
	    } else {
		lblVaBezeichnungData = this.toolkit.createLabel(data, "k.A.");
	    }
	    Label lblKategorie = this.toolkit.createLabel(data, "Kategorie: ");
	    Label lblKategorieData;
	    if (v.getKategorie() != null) {
		lblKategorieData = this.toolkit.createLabel(data,
			v.getKategorie());
	    } else {
		lblKategorieData = this.toolkit.createLabel(data, "k.A.");
	    }
	    Label lblDauer = this.toolkit.createLabel(data, "Dauer: ");
	    Label lblDauerData;
	    if (v.getDauer() != null) {
		lblDauerData = this.toolkit
			.createLabel(data, "" + v.getDauer());
	    } else {
		lblDauerData = this.toolkit.createLabel(data, "k.A.");
	    }
	    Label lblInhalt = this.toolkit.createLabel(data, "Inhalt: ");
	    Label lblInhaltData;
	    if (v.getInhalt() != null) {
		lblInhaltData = this.toolkit.createLabel(data, v.getInhalt());
	    } else {
		lblInhaltData = this.toolkit.createLabel(data, "k.A.");
	    }
	}
	// Suchkriterium Veranstaltungsort
	else if (this.auffuehrungListeInput.query instanceof Ort) {
	    // Titel des Abschnittes:
	    dataSection.setText("Suchkriterium:  Veranstaltungsort");
	    Ort o = (Ort) this.auffuehrungListeInput.query;

	    Label lblOrtsbezeichnung = this.toolkit.createLabel(data,
		    "Bezeichnung: ");
	    Label lblOrtsbezeichnungData;
	    if (o.getBezeichnung() != null) {
		lblOrtsbezeichnungData = this.toolkit.createLabel(data,
			o.getBezeichnung());
	    } else {
		lblOrtsbezeichnungData = this.toolkit.createLabel(data, "k.A.");
	    }
	    Label lblTyp = this.toolkit.createLabel(data, "Ortstyp: ");
	    Label lblTypData;
	    if (o.getOrtstyp() != null) {
		switch (o.getOrtstyp()) {
		case VERKAUFSSTELLE:
		    lblTypData = this.toolkit.createLabel(data,
			    "Verkaufsstelle");
		    break;
		case KIOSK:
		    lblTypData = this.toolkit.createLabel(data, "Kiosk");
		    break;
		case KINO:
		    lblTypData = this.toolkit.createLabel(data, "Kino");
		    break;
		case THEATER:
		    lblTypData = this.toolkit.createLabel(data, "Theater");
		    break;
		case OPER:
		    lblTypData = this.toolkit.createLabel(data, "Oper");
		    break;
		case KABARETT:
		    lblTypData = this.toolkit.createLabel(data, "Kabarett");
		    break;
		case SAAL:
		    lblTypData = this.toolkit.createLabel(data, "Saal");
		    break;
		case LOCATION:
		    lblTypData = this.toolkit.createLabel(data, "Location");
		    break;
		default:
		    lblTypData = this.toolkit.createLabel(data, "k.A.");
		    break;
		}
	    } else {
		lblTypData = this.toolkit.createLabel(data, "k.A.");
	    }
	    Label lblOrtname = this.toolkit.createLabel(data, "Ortsname: ");
	    Label lblOrtnameData;
	    if (o.getAdresse() != null) {
		if (o.getAdresse().getOrt() != null) {
		    lblOrtnameData = this.toolkit.createLabel(data, o
			    .getAdresse().getOrt());
		} else {
		    lblOrtnameData = this.toolkit.createLabel(data, "k.A.");
		}
	    } else {
		lblOrtnameData = this.toolkit.createLabel(data, "k.A.");
	    }
	    Label lblPlz = this.toolkit.createLabel(data, "PLZ: ");
	    Label lblPlzData;
	    if (o.getAdresse() != null) {
		if (o.getAdresse().getPlz() != null) {
		    lblPlzData = this.toolkit.createLabel(data, o.getAdresse()
			    .getPlz());
		} else {
		    lblPlzData = this.toolkit.createLabel(data, "k.A.");
		}
	    } else {
		lblPlzData = this.toolkit.createLabel(data, "k.A.");
	    }
	    Label lblStrasse = this.toolkit.createLabel(data, "Strasse: ");
	    Label lblStrasseData;
	    if (o.getAdresse() != null) {
		if (o.getAdresse().getStrasse() != null) {
		    lblStrasseData = this.toolkit.createLabel(data, o
			    .getAdresse().getStrasse());
		} else {
		    lblStrasseData = this.toolkit.createLabel(data, "k.A.");
		}
	    } else {
		lblStrasseData = this.toolkit.createLabel(data, "k.A.");
	    }

	    Label lblLand = this.toolkit.createLabel(data, "Land: ");
	    Label lblLandData;
	    if (o.getAdresse() != null) {
		if (o.getAdresse().getLand() != null) {
		    lblLandData = this.toolkit.createLabel(data, o.getAdresse()
			    .getLand());
		} else {
		    lblLandData = this.toolkit.createLabel(data, "k.A.");
		}
	    } else {
		lblLandData = this.toolkit.createLabel(data, "k.A.");
	    }
	}

	dataSection.setClient(data);
    }

    /**
     * Erzeugt die Tabelle in der die Daten der gefundenen Aufführungen
     * angezeigt werden
     * 
     * @param c
     *            Parent Composite indem der Table angezeigt wird
     */
    private void createTable(Composite parent) {

	Section auffuehrungSection = this.toolkit.createSection(parent,
		Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
			| Section.EXPANDED);
	auffuehrungSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		AuffuehrungListeEditor.this.form.reflow(true);
	    }
	});
	auffuehrungSection.setText("Aufführungen");
	auffuehrungSection
		.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	this.tableViewer = new TableViewer(auffuehrungSection, SWT.BORDER
		| SWT.FULL_SELECTION);
	this.tableViewer.getTable().setLayoutData(
		new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
	TableLayout layout = new TableLayout();
	layout.addColumnData(new ColumnWeightData(25, 100, true)); // Veranstaltung
	layout.addColumnData(new ColumnWeightData(20, 90, true)); // Datum/Uhrzeit
	layout.addColumnData(new ColumnWeightData(15, 30, true)); // Preis
	layout.addColumnData(new ColumnWeightData(20, 50, true)); // Hinweis
	layout.addColumnData(new ColumnWeightData(20, 50, true)); // Saal
	this.tableViewer.getTable().setLayout(layout);

	this.tableViewer.getTable().setLinesVisible(true);
	this.tableViewer.getTable().setHeaderVisible(true);

	this.tableViewer.setContentProvider(this.auffuehrungProvider);
	this.tableViewer.setLabelProvider(new ITableLabelProvider() {

	    @Override
	    public Image getColumnImage(Object arg0, int arg1) {
		return null;
	    }

	    @Override
	    public String getColumnText(Object element, int index) {

		Auffuehrung a = (Auffuehrung) element;
		switch (index) {
		case 0:
		    if (a.getVeranstaltung() == null)
			return "";
		    if (a.getVeranstaltung().getBezeichnung() == null)
			return "";
		    return a.getVeranstaltung().getBezeichnung();
		case 1:
		    return a.getDatumuhrzeit().toString();
		    // String time;
		    // Calendar c = new GregorianCalendar();
		    // c.clear();
		    // c.setTimeInMillis(a.getDatumuhrzeit().getTime());
		    //
		    // time = c.get(Calendar.DAY_OF_MONTH) + "."
		    // + c.get(Calendar.MONTH) + "."
		    // + c.get(Calendar.YEAR) + " "
		    // + c.get(Calendar.HOUR_OF_DAY) + ":"
		    // + c.get(Calendar.MINUTE);
		    //
		    // return time;
		case 2:
		    switch (a.getPreis()) {
		    case MAXIMALPREIS:
			return "Maximalpreis";
		    case MINDESTPREIS:
			return "Mindestpreis";
		    case STANDARDPREIS:
			return "Standardpreis";
		    default:
			return "";
		    }
		case 3:
		    if (a.getHinweis() == null)
			return "";
		    return a.getHinweis();
		case 4:
		    if (a.getSaal() == null)
			return "";
		    if (a.getSaal().getBezeichnung() == null)
			return "";
		    return a.getSaal().getBezeichnung();
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
		AuffuehrungListeEditor.this.openTicketVerkaufView();
	    }
	});

	TableColumn colVeranstaltung = new TableColumn(
		this.tableViewer.getTable(), SWT.LEFT);
	colVeranstaltung.setText("Veranstaltung");
	TableColumn colDatumZeit = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colDatumZeit.setText("Datum/Uhrzeit");
	TableColumn colPreis = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colPreis.setText("Preis");
	TableColumn colHinweis = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colHinweis.setText("Hinweis");
	TableColumn colSaal = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colSaal.setText("Saal");

	tableViewer.setInput(new Auffuehrung());

	this.toolkit.adapt(this.tableViewer.getTable(), true, true);
	auffuehrungSection.setClient(this.tableViewer.getTable());
	
	// workaround damit scrollbar angezeigt wird
	auffuehrungSection.setExpanded(false);
	auffuehrungSection.setExpanded(true);
    }

    private void createButtons(Composite parent) {
	Composite buttons = this.toolkit.createComposite(parent);
	buttons.setLayout(new GridLayout(2, false));
	buttons.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

	this.btnSitzplan = new Button(buttons, SWT.PUSH);
	// this.btnSitzplan.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP,
	// false,
	// false));
	this.btnSitzplan.setText(" Sitzplan anzeigen ");
	this.btnSitzplan.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		// Sitzplan anzeigen
		AuffuehrungListeEditor.this.openSitzplanAnzeigeEditor();
	    }
	});

	// TODO: Uncomment when rdy
	/*
	 * if(this.mitarbeiterService.getLoggedInMitarbeiter() == null) { //
	 * Kein Mitarbeiter eingeloggt, Button Ticket-Verkauf sollte nicht
	 * sichtbar sein return; }
	 */

	this.btnTickets = new Button(buttons, SWT.PUSH);
	// this.btnTickets.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
	// false));
	this.btnTickets.setText(" Tickets ");
	this.btnTickets.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		// Ticket-View öffnen
		AuffuehrungListeEditor.this.openTicketVerkaufView();
	    }
	});
    }

    private void openTicketVerkaufView() {
	// TODO Uncomment when rdy
	/*
	 * if(this.mitarbeiterService.getLoggedInMitarbeiter() == null) { //
	 * kein Mitarbeiter eingeloggt
	 * MessageDialog.openInformation(AuffuehrungListeEditor.this.getSite()
	 * .getWorkbenchWindow().getShell(), "Einloggen",
	 * "Sie müssen eingeloggt sein um Tickets zu verkaufen!"); return; }
	 */

	IStructuredSelection structuredSelection = (IStructuredSelection) this.tableViewer
		.getSelection();
	if (structuredSelection.isEmpty()) {
	    MessageDialog.openError(AuffuehrungListeEditor.this.getSite()
		    .getWorkbenchWindow().getShell(), "Error",
		    "Es wurde keine Aufführung ausgewählt!");
	    return;
	}

	TicketVerkaufView view;
	try {
	    if (!((new TicketVerkaufView()) instanceof INeedLogin)
		    || mitarbeiterService.isLoggedIn()) {
		view = (TicketVerkaufView) this.getSite().getPage()
		    .showView(TicketVerkaufView.ID);
		Auffuehrung a = (Auffuehrung) structuredSelection.getFirstElement();
		view.setData(a);
	    } else
		EasyMessage.showLoginRequiredMessage(ID);
	} catch (PartInitException e) {
	    log.error("Unable to open Ticket-Verkauf View: " + e.getMessage());
	    return;
	}
    }

    private void openSitzplanAnzeigeEditor() {
	IStructuredSelection structuredSelection = (IStructuredSelection) this.tableViewer
		.getSelection();
	if (structuredSelection.isEmpty()) {
	    MessageDialog.openError(AuffuehrungListeEditor.this.getSite()
		    .getWorkbenchWindow().getShell(), "Error",
		    "Es wurde keine Aufführung ausgewählt!");
	    return;
	}

	Auffuehrung a = (Auffuehrung) structuredSelection.getFirstElement();
	SitzplanAnzeigeEditorInput editorInput = new SitzplanAnzeigeEditorInput(
		a);

	// Editor aufrufen
	try {
	    this.getSite().getPage()
		    .openEditor(editorInput, SitzplanAnzeigeEditor.ID);
	} catch (PartInitException e) {
	    log.error("Unable to open Sitzplan Anzeige: " + e.getMessage());
	    return;
	}
    }

    @Override
    public boolean isDirty() {
	// nothing to do
	return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
	// nothing to do
	return false;
    }

    @Override
    public void setFocus() {
	// nothing to do
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
	// nothing to do
    }

    @Override
    public void doSaveAs() {
	// nothing to do
    }

    /**
     * Klasse, die für den Inhalt des Tables verantwortlich ist
     * 
     * @author Daniel
     * 
     */
    class AuffuehrungContentProvider implements IStructuredContentProvider {
	private AuffuehrungListeEditorInput input = null;

	@Override
	public Object[] getElements(Object query) {
	    return input.listAuffuehrung.toArray();
	}

	public void setInput(AuffuehrungListeEditorInput input) {
	    this.input = input;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}
    }

}
