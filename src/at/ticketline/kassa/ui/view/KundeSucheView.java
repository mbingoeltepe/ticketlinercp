package at.ticketline.kassa.ui.view;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
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
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Kundengruppe;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.KundeServiceImpl;
import at.ticketline.service.interfaces.KundeService;
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
public class KundeSucheView extends ViewPart implements INeedLogin, SearchView {
    public static final String ID = "at.ticketline.view.suche.kunde";

    protected Logger log = LogFactory.getLogger(KundeSucheView.class);

    public TableViewer tableViewer;

    // content provider für den Table
    protected KundeContentProvider kundeProvider;

    protected Text txtNachname;
    protected Text txtVorname;
    protected Combo cbGeschlecht;
    protected Combo cbGruppe;
    protected Combo cbGesperrt;

    protected Label lblSearchSpacer;
    protected Button btnSearch;
    
    private Composite c;

    @Override
    public void init(IViewSite site) throws PartInitException {
	super.init(site);
	this.kundeProvider = new KundeContentProvider();
    }

    @Override
    public void createPartControl(Composite parent) {
	c = new Composite(parent, SWT.NONE);
	c.setLayout(new GridLayout(2, false));

	this.createForm(c);

	this.createTable(c);

	this.createEditButton(c);

	if (enableInstantSearchOnStartup)
	    enableInstantSearch();
	
	 PlatformUI.getWorkbench().getHelpSystem().setHelp(c, "TicketlineRCP.kunde_suchen"); // contextsensitive hilfe

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

	// Nachname
	Label lblNachname = new Label(formGroup, SWT.LEFT);
	lblNachname.setText("Nachname:");
	lblNachname.setSize(230, lblNachname.getSize().y);

	this.txtNachname = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtNachname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtNachname.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	// Vorname
	Label lblVorname = new Label(formGroup, SWT.LEFT);
	lblVorname.setText("Vorname:");

	this.txtVorname = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtVorname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtVorname.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	// Geschlecht
	Label lblGeschlecht = new Label(formGroup, SWT.LEFT);
	lblGeschlecht.setText("Geschlecht:");

	this.cbGeschlecht = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
	this.cbGeschlecht.setItems(new String[] { "", "männlich", "weiblich" });
	this.cbGeschlecht.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.cbGeschlecht.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	// Gesperrt
	Label lblGesperrt = new Label(formGroup, SWT.LEFT);
	lblGesperrt.setText("Gesperrt:");

	this.cbGesperrt = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
	this.cbGesperrt.setItems(new String[] { "", "JA", "NEIN" });
	this.cbGesperrt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.cbGesperrt.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	// Gruppe
	Label lblGruppe = new Label(formGroup, SWT.LEFT);
	lblGruppe.setText("Gruppe:");

	this.cbGruppe = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
	this.cbGruppe.setItems(new String[] { "", "STANDARD", "VIP", "GOLD",
		"PREMIUM" });
	this.cbGruppe.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.cbGruppe.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	Label lblShowAllSpacer = new Label(formGroup, SWT.LEFT);
	lblShowAllSpacer.setText("");

	Button btnShowAll = new Button(formGroup, SWT.PUSH);
	btnShowAll
		.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
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
	btnSearch.setToolTipText("Nach Kunden suchen");
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
	this.tableViewer.getTable().setLayout(layout);

	this.tableViewer.getTable().setLinesVisible(true);
	this.tableViewer.getTable().setHeaderVisible(true);

	this.tableViewer.setContentProvider(this.kundeProvider);
	this.tableViewer.setLabelProvider(new ITableLabelProvider() {

	    @Override
	    public Image getColumnImage(Object arg0, int arg1) {
		return null;
	    }

	    @Override
	    public String getColumnText(Object element, int index) {
		Kunde k = (Kunde) element;
		switch (index) {
		case 0:
		    return k.getNachname();
		case 1:
		    return k.getVorname();
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
		KundeSucheView.this.openKundeEditor();
	    }
	});

	TableColumn colNachname = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colNachname.setText("Nachname");
	TableColumn colVorname = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colVorname.setText("Vorname");

	this.getSite().setSelectionProvider(this.tableViewer);

    }

    private void createEditButton(Composite c) {
	Label lblEmpty = new Label(c, SWT.LEFT);
	lblEmpty.setText("");

	Button btnEdit = new Button(c, SWT.PUSH);
	btnEdit.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
	btnEdit.setText(" Bearbeiten ");
	btnEdit.setToolTipText("Kunde bearbeiten");
	btnEdit.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		KundeSucheView.this.openKundeEditor();
	    }
	});
    }

    @Override
    public void setFocus() {
	 if(c != null) // Context sensitive help
	      c.setFocus(); // context sensitive help

	this.txtNachname.setFocus();
	this.txtNachname.setSelection(0, this.txtNachname.getText().length());
    }

    protected Kunde createKundeQuery() {
	Kunde k = new Kunde();
	String n = this.txtNachname.getText().trim();
	String v = this.txtVorname.getText().trim();
	String g = this.cbGeschlecht.getText().trim();
	if (n.length() > 0) {
	    k.setNachname(n);
	}
	if (v.length() > 0) {
	    k.setVorname(v);
	}
	if (g.length() > 0) {
	    k.setGeschlecht(Geschlecht.getValueOf(g));
	}

	if (cbGesperrt.getText().equals("JA"))
	    k.setGesperrt(true);
	else
	    k.setGesperrt(false);

	k.setGruppe(Kundengruppe.getValueOf(this.cbGruppe.getText()));

	return k;
    }

    public void openKundeEditor() {
	IHandlerService handlerService = (IHandlerService) KundeSucheView.this
		.getSite().getService(IHandlerService.class);
	try {
	    handlerService.executeCommand("at.ticketline.command.EditKunde",
		    null);
	} catch (Exception ex) {
	    KundeSucheView.this.log.error(ex);

	    MessageDialog.openError(KundeSucheView.this.getSite()
		    .getWorkbenchWindow().getShell(), "Error",
		    "Kunde kann nicht bearbeitet werden: " + ex.getMessage());
	}
    }

    /**
     * Behandelt die Such Anfrage
     */
    @Override
    public void searchHandler() {
	Kunde k = KundeSucheView.this.createKundeQuery();
	KundeSucheView.this.log.info("Querying for #0", k.toString());
	KundeSucheView.this.tableViewer.setInput(k);
    }

    @Override
    public void enableInstantSearch() {

	lblSearchSpacer.dispose();
	btnSearch.dispose();

	InstantSearch.addInstantSearch(this.txtNachname, this);
	InstantSearch.addInstantSearch(this.txtVorname, this);
	InstantSearch.addInstantSearch(this.cbGeschlecht, this);
	InstantSearch.addInstantSearch(this.cbGesperrt, this);
	InstantSearch.addInstantSearch(this.cbGruppe, this);

	searchHandler();
    }

    private void showAllHandler() {

	txtNachname.setText("");
	txtVorname.setText("");
	cbGeschlecht.select(0);
	cbGesperrt.select(0);
	cbGruppe.select(0);

	searchHandler();
    }

    class KundeContentProvider implements IStructuredContentProvider {
	private KundeService kundeService = null;

	@Override
	public Object[] getElements(Object query) {
	    kundeService = new KundeServiceImpl();
	    if ((query instanceof Kunde) == false) {
		KundeSucheView.this.log.info(
			"Query object not of type Kunde: #0", query.getClass()
				.getName());
		return new Object[0];
	    }
	    try {
		return kundeService.sucheKunde((Kunde) query).toArray();

	    } catch (Exception e) {
		KundeSucheView.this.log
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
}
