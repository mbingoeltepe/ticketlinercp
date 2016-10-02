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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.kassa.ui.editor.AuffuehrungListeEditor;
import at.ticketline.kassa.ui.editor.AuffuehrungListeEditorInput;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.VeranstaltungServiceImpl;
import at.ticketline.service.interfaces.VeranstaltungService;
import at.ticketline.util.InstantSearch;
import at.ticketline.util.SearchView;

/**
 * 
 * @author MURAT BINGOLTEPE, Georg Fuderer
 * 
 * @see at.ticketline.kassa.ui.interfaces.INeedLogin
 * @see at.ticketline.util.SearchView
 * @see at.ticketline.util.InstantSearch
 * 
 */
public class VeranstaltungSucheView extends ViewPart implements SearchView {
    public static final String ID = "at.ticketline.view.suche.veranstaltung";

    protected Logger log = LogFactory.getLogger(VeranstaltungSucheView.class);

    public TableViewer tableViewer;

    // content provider für den Table
    protected VeranstaltungContentProvider veranstaltungProvider;

    protected Text txtBezeichnung;
    protected Text txtKategorie;
    protected Scale sclDauer;
    protected Text txtInhalt;
    protected Label lblValue;

    protected Label lblSearchSpacer;
    protected Button btnSearch;
    
    private Composite c;

    @Override
    public void init(IViewSite site) throws PartInitException {
	super.init(site);
	this.veranstaltungProvider = new VeranstaltungContentProvider();
    }

    @Override
    public void createPartControl(Composite parent) {
	Composite c = new Composite(parent, SWT.NONE);
	c.setLayout(new GridLayout(2, false));

	this.createForm(c);

	this.createTable(c);

	this.createAuffuehrungButton(c);

	if (enableInstantSearchOnStartup) enableInstantSearch();

	 PlatformUI.getWorkbench().getHelpSystem().setHelp(c, "TicketlineRCP.veranstaltung_suchen"); // contextsensitive hilfe
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

	// Kategorie
	Label lblKategorie = new Label(formGroup, SWT.LEFT);
	lblKategorie.setText("Kategorie:");

	this.txtKategorie = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtKategorie.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtKategorie.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	// Dauer
	Label lblDauer = new Label(formGroup, SWT.LEFT);
	lblDauer.setText("Dauer:");

	this.sclDauer = new Scale(formGroup, SWT.TIME);
	this.sclDauer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.sclDauer.setBounds(0, 0, 40, 200);
	this.sclDauer.setMaximum(600);
	this.sclDauer.setMinimum(0);
	this.sclDauer.setIncrement(1);
	this.sclDauer.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});
	this.sclDauer.addListener(SWT.Selection, new Listener() {
	    public void handleEvent(Event e) {
		updateDurationLabel();
	    }
	});

	Label lblEmpty1 = new Label(formGroup, SWT.LEFT);
	lblEmpty1.setText("");

	lblValue = new Label(formGroup, SWT.LEFT);
	lblValue.setText("                                                         ");

	// Inhalt
	Label lblInhalt = new Label(formGroup, SWT.LEFT);
	lblInhalt.setText("Inhalt:");

	this.txtInhalt = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtInhalt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtInhalt.addListener(SWT.DefaultSelection, new Listener() {
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

	lblSearchSpacer = new Label(formGroup, SWT.LEFT);
	lblSearchSpacer.setText("");

	btnSearch = new Button(formGroup, SWT.PUSH);
	btnSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
	btnSearch.setText(" Suchen ");
	btnSearch.setToolTipText("Nach Veranstaltungen suchen");
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
	layout.addColumnData(new ColumnWeightData(50, 100, true));
	layout.addColumnData(new ColumnWeightData(50, 100, true));

	this.tableViewer.getTable().setLayout(layout);

	this.tableViewer.getTable().setLinesVisible(true);
	this.tableViewer.getTable().setHeaderVisible(true);

	this.tableViewer.setContentProvider(this.veranstaltungProvider);
	this.tableViewer.setLabelProvider(new ITableLabelProvider() {

	    @Override
	    public Image getColumnImage(Object arg0, int arg1) {
		return null;
	    }

	    @Override
	    public String getColumnText(Object element, int index) {
		Veranstaltung v = (Veranstaltung) element;
		switch (index) {
		case 0:
		    return v.getBezeichnung();
		case 1:
		    return v.getKategorie();
		case 2:
		    return v.getDauer().toString();
		case 3:
		    return v.getInhalt();
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
		VeranstaltungSucheView.this
			.openAuffuehrungVonVeranstaltungEditor();
	    }
	});
	this.tableViewer
		.addSelectionChangedListener(new ISelectionChangedListener() {

		    @Override
		    public void selectionChanged(SelectionChangedEvent event) {
			// Nur Suche starten, wenn auch etwas ausgewählt ist:
			if (!event.getSelection().isEmpty()) {
			    VeranstaltungSucheView.this
				    .openAuffuehrungVonVeranstaltungEditor();
			}
		    }
		});

	TableColumn colBezeichnung = new TableColumn(
		this.tableViewer.getTable(), SWT.LEFT);
	colBezeichnung.setText("Bezeichnung");
	TableColumn colKategorie = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colKategorie.setText("Kategorie");
	TableColumn colDauer = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colDauer.setText("Dauer");
	TableColumn colInhalt = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colInhalt.setText("Inhalt");

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
		VeranstaltungSucheView.this
			.openAuffuehrungVonVeranstaltungEditor();
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

    protected Veranstaltung createVeranstaltungQuery() {
	Veranstaltung v = new Veranstaltung();
	String b = this.txtBezeichnung.getText().trim();
	String k = this.txtKategorie.getText().trim();
	int d = this.sclDauer.getSelection();
	String i = this.txtInhalt.getText().trim();
	if (b.length() > 0) {
	    v.setBezeichnung(b);
	}
	if (k.length() > 0) {
	    v.setKategorie(k);
	}
	if (i.length() > 0) {
	    v.setInhalt(i);
	}

	if (d != 0) {
	    v.setDauer(new Integer(d));

	}

	return v;
    }

    public void openAuffuehrungVonVeranstaltungEditor() {
	IStructuredSelection selection = (IStructuredSelection) tableViewer
		.getSelection();
	Veranstaltung v = (Veranstaltung) selection.getFirstElement();
	if (selection.isEmpty()) {
	    MessageDialog.openInformation(this.getSite().getWorkbenchWindow()
		    .getShell(), "Veranstaltung auswählen",
		    "Es wurde keine Veranstaltung ausgewaehlt");
	    return;
	}
	if (v.getAuffuehrungen() == null) {
	    MessageDialog
		    .openInformation(this.getSite().getWorkbenchWindow()
			    .getShell(), "keine Aufführungen",
			    "Es konnten keine Aufführungen zur ausgewählten Veranstaltung gefunden werden");
	    return;
	}
	List<Auffuehrung> liste = new ArrayList<Auffuehrung>(
		v.getAuffuehrungen());
	AuffuehrungListeEditorInput editorInput = new AuffuehrungListeEditorInput(
		liste, selection.getFirstElement());
	try {
	    this.getSite().getPage()
		    .openEditor(editorInput, AuffuehrungListeEditor.ID);
	} catch (Exception e) {
	    log.error("Unable to open AuffuehrungListeEditor");
	}
    }
    
    private void updateDurationLabel() {
	int zeit = sclDauer.getSelection();
	int stunde = 0;
	int minuten = 0;
	if (zeit < 60 && zeit > 0) {
	    lblValue.setText(zeit + " Minuten");
	} else if (zeit >= 60) {
	    stunde = zeit / 60;
	    minuten = zeit - stunde * 60;
	    lblValue.setText(stunde + " Stunde " + minuten + " Minuten");
	} else {
	    lblValue.setText("");
	}
    }

    /**
     * Behandelt die Such Anfrage
     */
    @Override
    public void searchHandler() {
	Veranstaltung v = VeranstaltungSucheView.this
		.createVeranstaltungQuery();
	VeranstaltungSucheView.this.log.info("Querying for #0", v.toString());
	VeranstaltungSucheView.this.tableViewer.setInput(v);
    }

    @Override
    public void enableInstantSearch() {

	lblSearchSpacer.dispose();
	btnSearch.dispose();

	InstantSearch.addInstantSearch(this.txtBezeichnung, this);
	InstantSearch.addInstantSearch(this.txtKategorie, this);
	InstantSearch.addInstantSearch(this.sclDauer, this);
	InstantSearch.addInstantSearch(this.txtInhalt, this);

	searchHandler();
    }
    
    private void showAllHandler() {
	
	txtBezeichnung.setText("");
	txtInhalt.setText("");
	txtKategorie.setText("");
	sclDauer.setSelection(0);
	updateDurationLabel();
	
	searchHandler();
    }

    class VeranstaltungContentProvider implements IStructuredContentProvider {
	private VeranstaltungService veranstaltungService = null;

	@Override
	public Object[] getElements(Object query) {
	    veranstaltungService = new VeranstaltungServiceImpl();
	    if ((query instanceof Veranstaltung) == false) {
		VeranstaltungSucheView.this.log.info(
			"Query object not of type Veranstaltung: #0", query
				.getClass().getName());
		return new Object[0];
	    }
	    try {
		return veranstaltungService.findeVeranstaltungen(
			(Veranstaltung) query).toArray();

	    } catch (Exception e) {
		VeranstaltungSucheView.this.log.info(
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
