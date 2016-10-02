package at.ticketline.kassa.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
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
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import at.ticketline.entity.Adresse;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Ortstyp;
import at.ticketline.entity.Saal;
import at.ticketline.kassa.ui.editor.AuffuehrungListeEditor;
import at.ticketline.kassa.ui.editor.AuffuehrungListeEditorInput;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.OrtServiceImpl;
import at.ticketline.service.interfaces.OrtService;
import at.ticketline.util.InstantSearch;
import at.ticketline.util.SearchView;

/**
 * @author Georg Fuderer
 * 
 * @see at.ticketline.kassa.ui.interfaces.INeedLogin
 * @see at.ticketline.util.SearchView
 * @see at.ticketline.util.InstantSearch
 * 
 */
public class OrtSucheView extends ViewPart implements SearchView {
    public static final String ID = "at.ticketline.view.suche.ort";

    protected Logger log = LogFactory.getLogger(this.getClass());

    public TableViewer tableViewer;

    // content provider für den Table
    protected OrtContentProvider ortProvider;

    protected Text txtBezeichnung;
    protected Text txtStrasse;
    protected Text txtOrt;
    protected Text txtLand;
    protected Text txtPlz;
    protected Combo cbTyp;

    protected Label lblSearchSpacer;
    protected Button btnSearch;
    
    private Composite c;

    @Override
    public void init(IViewSite site) throws PartInitException {
	super.init(site);
	this.ortProvider = new OrtContentProvider();
    }

    @Override
    public void createPartControl(Composite parent) {
	c = new Composite(parent, SWT.NONE);
	c.setLayout(new GridLayout(2, false));

	this.createForm(c);

	this.createTable(c);

	this.createAuffuehrungButton(c);

	if (enableInstantSearchOnStartup) enableInstantSearch();

	 PlatformUI.getWorkbench().getHelpSystem().setHelp(c, "TicketlineRCP.ort_suchen"); // contextsensitive hilfe
    }

    /**
     * Erzeugt die Form welche alle Suchfelder und den Suchen-Button beinhaltet
     * 
     * @param parent
     *            Der Parent Composite indem die Form eingefügt wird
     * 
     */
    public void createForm(Composite parent) {
	Group formGroup = new Group(parent, SWT.BORDER_SOLID);
	formGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2,
		1));
	formGroup.setLayout(new GridLayout(2, false));

	// Bezeichnung
	Label lblBezeichnung = new Label(formGroup, SWT.LEFT);
	lblBezeichnung.setText("Bezeichnung:");
	lblBezeichnung.setSize(230, lblBezeichnung.getSize().y);

	this.txtBezeichnung = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtBezeichnung
		.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtBezeichnung.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	// Strasse
	Label lblStrasse = new Label(formGroup, SWT.LEFT);
	lblStrasse.setText("Strasse:");

	this.txtStrasse = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtStrasse.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtStrasse.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	// Ort
	Label lblOrt = new Label(formGroup, SWT.LEFT);
	lblOrt.setText("Ort:");

	this.txtOrt = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtOrt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtOrt.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	// Land
	Label lblLand = new Label(formGroup, SWT.LEFT);
	lblLand.setText("Land:");

	this.txtLand = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtLand.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtLand.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	// Postleitzahl
	Label lblPlz = new Label(formGroup, SWT.LEFT);
	lblPlz.setText("Plz:");

	this.txtPlz = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtPlz.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtPlz.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	// Typ
	Label lblTyp = new Label(formGroup, SWT.LEFT);
	lblTyp.setText("Typ:");

	this.cbTyp = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY
		| SWT.BORDER);
	this.cbTyp.add("");
	for (Ortstyp typ : Ortstyp.values())
	    this.cbTyp.add(typ.name());

	this.cbTyp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.cbTyp.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	Label lblShowAllSpacer = new Label(formGroup, SWT.LEFT);
	lblShowAllSpacer.setText("");

	Button btnShowAll = new Button(formGroup, SWT.PUSH);
	btnShowAll.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
	btnShowAll.setText(" Alle anzeigen ");
	btnShowAll.setToolTipText("Setzt die Suchkriterien zurück");
	btnShowAll.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {

		showAllHandler();
	    }
	});

	lblSearchSpacer = new Label(formGroup, SWT.NULL);
	lblSearchSpacer.setText("");

	btnSearch = new Button(formGroup, SWT.PUSH);
	btnSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
	btnSearch.setText(" Suchen ");
	btnSearch.setToolTipText("Nach Ort suchen");
	btnSearch.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {

		searchHandler();
	    }
	});
    }

    private void createTable(Composite c) {
	this.tableViewer = new TableViewer(c, SWT.BORDER | SWT.FULL_SELECTION);
	this.tableViewer.getTable().setLayoutData(
		new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	TableLayout layout = new TableLayout();
	layout.addColumnData(new ColumnWeightData(50, 100, true));
	layout.addColumnData(new ColumnWeightData(50, 100, true));
	layout.addColumnData(new ColumnWeightData(40, 70, true));
	layout.addColumnData(new ColumnWeightData(50, 100, true));
	layout.addColumnData(new ColumnWeightData(30, 50, true));
	layout.addColumnData(new ColumnWeightData(50, 100, true));

	this.tableViewer.getTable().setLayout(layout);

	this.tableViewer.getTable().setLinesVisible(true);
	this.tableViewer.getTable().setHeaderVisible(true);

	this.tableViewer.setContentProvider(this.ortProvider);
	this.tableViewer.setLabelProvider(new ITableLabelProvider() {

	    @Override
	    public Image getColumnImage(Object arg0, int arg1) {
		return null;
	    }

	    @Override
	    public String getColumnText(Object element, int index) {
		Ort o = (Ort) element;
		switch (index) {
		case 0:
		    return o.getBezeichnung();
		case 1:
		    return o.getAdresse().getStrasse();
		case 2:
		    return o.getAdresse().getOrt();
		case 3:
		    return o.getAdresse().getLand();
		case 4:
		    return o.getAdresse().getPlz();
		case 5:
		    return o.getOrtstyp().name();
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
		OrtSucheView.this.openAuffuehrungVonOrtEditor();
	    }
	});
	this.tableViewer
		.addSelectionChangedListener(new ISelectionChangedListener() {

		    @Override
		    public void selectionChanged(SelectionChangedEvent event) {
			// Nur Suche starten, wenn auch etwas ausgewählt ist:
			if (!event.getSelection().isEmpty()) {
			    OrtSucheView.this.openAuffuehrungVonOrtEditor();
			}
		    }
		});

	TableColumn colBezeichnung = new TableColumn(
		this.tableViewer.getTable(), SWT.LEFT);
	colBezeichnung.setText("Bezeichnung");
	TableColumn colStrasse = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colStrasse.setText("Strasse");
	TableColumn colOrt = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colOrt.setText("Ort");
	TableColumn colLand = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colLand.setText("Land");
	TableColumn colPlz = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colPlz.setText("Plz");
	TableColumn colTyp = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colTyp.setText("Typ");

	this.getSite().setSelectionProvider(this.tableViewer);

    }

    private void createAuffuehrungButton(Composite c) {
	Label lblEmpty = new Label(c, SWT.LEFT);
	lblEmpty.setText("");

	Button btnAuffuehrung = new Button(c, SWT.PUSH);
	btnAuffuehrung.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
		false));
	btnAuffuehrung.setText(" Aufführungen anzeigen ");
	btnAuffuehrung.setToolTipText("Auffuehrungen zeigen");
	btnAuffuehrung.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		OrtSucheView.this.openAuffuehrungVonOrtEditor();
	    }
	});
    }

    @Override
    public void setFocus() {
	 if(c != null) // Context sensitive help
	      c.setFocus(); // context sensitive help
	this.txtBezeichnung.setFocus();
	this.txtBezeichnung.setSelection(0, this.txtBezeichnung.getText()
		.length());
    }

    protected Ort createOrtQuery() {
	Ort veranstaltungsort = new Ort();

	String bezeichnung = this.txtBezeichnung.getText().trim();
	String strasse = this.txtStrasse.getText().trim();
	String ort = this.txtOrt.getText().trim();
	String land = this.txtLand.getText().trim();
	String plz = this.txtPlz.getText().trim();
	String typ = this.cbTyp.getText().trim();

	if (bezeichnung.length() > 0) {
	    veranstaltungsort.setBezeichnung(bezeichnung);
	}

	Adresse adr = new Adresse();

	if (strasse.length() > 0) {
	    adr.setStrasse(strasse);
	}
	if (ort.length() > 0) {
	    adr.setOrt(ort);
	}
	if (land.length() > 0) {
	    adr.setLand(land);
	}
	if (plz.length() > 0) {
	    adr.setPlz(plz);
	}

	veranstaltungsort.setAdresse(adr);

	if (typ.length() > 0) {
	    veranstaltungsort.setOrtstyp(Ortstyp.valueOf(typ));
	}

	return veranstaltungsort;
    }

    public void openAuffuehrungVonOrtEditor() {
	IStructuredSelection selection = (IStructuredSelection) tableViewer
		.getSelection();
	Ort o = (Ort) selection.getFirstElement();

	List<Auffuehrung> liste = new ArrayList<Auffuehrung>();

	if (selection.isEmpty()) {
	    MessageDialog.openInformation(this.getSite().getWorkbenchWindow()
		    .getShell(), "Veranstaltungsort auswählen",
		    "Es wurde kein Veranstaltungsort ausgewaehlt");
	    return;
	}

	if (o.getSaele() != null) {

	    for (Saal saal : o.getSaele()) {
		if (saal.getAuffuehrungen() != null) {
		    for (Auffuehrung auffuehrung : saal.getAuffuehrungen()) {
			liste.add(auffuehrung);
		    }
		}
	    }
	}

	if (liste.size() == 0) {
	    MessageDialog
		    .openInformation(
			    this.getSite().getWorkbenchWindow().getShell(),
			    "keine Aufführungen",
			    "Es konnten keine Aufführungen zum ausgewählten Veranstaltungsort gefunden werden");
	    return;
	}

	AuffuehrungListeEditorInput editorInput = new AuffuehrungListeEditorInput(
		liste, selection.getFirstElement());
	try {
	    this.getSite().getPage()
		    .openEditor(editorInput, AuffuehrungListeEditor.ID);
	} catch (Exception e) {
	    log.error("Unable to open AuffuehrungListeEditor");
	}
    }

    /**
     * Behandelt die Such Anfrage
     */
    @Override
    public void searchHandler() {
	Ort o = OrtSucheView.this.createOrtQuery();
	OrtSucheView.this.log.info("Querying for #0", o.toString());
	OrtSucheView.this.tableViewer.setInput(o);
    }

    @Override
    public void enableInstantSearch() {

	lblSearchSpacer.dispose();
	btnSearch.dispose();

	InstantSearch.addInstantSearch(this.txtBezeichnung, this);
	InstantSearch.addInstantSearch(this.txtStrasse, this);
	InstantSearch.addInstantSearch(this.txtOrt, this);
	InstantSearch.addInstantSearch(this.txtLand, this);
	InstantSearch.addInstantSearch(this.txtPlz, this);
	InstantSearch.addInstantSearch(this.cbTyp, this);

	searchHandler();
    }

    private void showAllHandler() {

	txtBezeichnung.setText("");
	txtLand.setText("");
	txtOrt.setText("");
	txtPlz.setText("");
	txtStrasse.setText("");

	searchHandler();
    }

    class OrtContentProvider implements IStructuredContentProvider {
	private OrtService ortService = null;

	@Override
	public Object[] getElements(Object query) {
	    ortService = new OrtServiceImpl();
	    if ((query instanceof Ort) == false) {
		OrtSucheView.this.log.info(
			"Query object not of type Veranstaltung: #0", query
				.getClass().getName());
		return new Object[0];
	    }
	    try {
		return ortService.sucheVeranstaltungsort((Ort) query).toArray();

	    } catch (Exception e) {
		OrtSucheView.this.log.info(
			"Exception in VeranstaltungContentProvider: #0",
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
}
