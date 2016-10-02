package at.ticketline.kassa.ui.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import at.ticketline.dao.DaoFactory;

import at.ticketline.dao.interfaces.KundeDao;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.util.InstantSearch;
import at.ticketline.util.SearchView;

/**
 * 
 * @author andrea auer, Georg Fuderer
 * 
 */

public class TicketVerkaufView extends ViewPart implements INeedLogin,
	SearchView {
    public static final String ID = "at.ticketline.view.verkauf.ticket";
    protected Logger log = LogFactory.getLogger(this.getClass());

    public TableViewer tableViewer;
    protected KundeContentProvider kundeProvider;

    // Selection für Datenübergabe, muss immer upgedated werden
    TicketVerkaufData selectionVerkaufData;

    // Textboxen für anzeige
    protected Text txtAuffuehrung;
    protected Text txtDatum;
    // protected DateTime dtUhrzeit;
    // protected Text txtStueck;
    // protected Text txtOrt;
    protected Text txtSaal;

    // Textboxen für suche
    protected Text txtNachname;
    protected Text txtVorname;

    // Button für Kundensuche
    protected Button btnAnonym;
    protected Button btnName;

    // Buttons für Reservieren/Verkaufen
    protected Button btnReservieren;
    protected Button btnVerkaufen;

    // Aufführung aus Aufführung Scuhe übergeben
    protected Auffuehrung auffuehrung;

    protected Label lblSearchSpacer;
    protected Button btnSearch;
    
    private Composite c;

    @Override
    public void init(IViewSite site) throws PartInitException {
	super.init(site);
	this.kundeProvider = new KundeContentProvider();
	this.kundeProvider.setKundeDao((KundeDao) DaoFactory
		.findDaoByEntity(Kunde.class));

	// setze SelectionProvider für diese View, damit im CommandHandler auf
	// die Daten der View zugegriffen werden kann
	this.selectionVerkaufData = new TicketVerkaufData();

	this.getSite().setSelectionProvider(
		new TicketSelectionProvider(this.selectionVerkaufData));
    }

    @Override
    public void createPartControl(Composite parent) {
	Composite c = new Composite(parent, SWT.NONE);
	c.setLayout(new GridLayout(2, false));

	this.createForm(c);
	this.createTable(c);
	this.createButtons(c);

	if (enableInstantSearchOnStartup)
	    enableInstantSearch();
	
	 PlatformUI.getWorkbench().getHelpSystem().setHelp(c, "TicketlineRCP.ticket_suchen"); // contextsensitive hilfe
    }

    public void createForm(Composite parent) {
	Group formGroup = new Group(parent, SWT.BORDER_SOLID);
	formGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2,
		1));
	formGroup.setLayout(new GridLayout(2, false));

	// 1 Zeile mit Karten für Aufführung: und Stück:
	Label lblAuffuehrung = new Label(formGroup, SWT.LEFT);
	lblAuffuehrung.setText("Aufführung:");
	lblAuffuehrung.setSize(230, lblAuffuehrung.getSize().y);

	this.txtAuffuehrung = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtAuffuehrung
		.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtAuffuehrung.setEnabled(false);

	// Label lblStueck = new Label(formGroup, SWT.RIGHT);
	// lblStueck.setText("Stück:");
	// lblStueck.setSize(230, lblStueck.getSize().y);
	//
	// this.txtStueck = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	// this.txtStueck.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	// this.txtStueck.setEnabled(false);

	// 2. Zeile mit Datum und Uhrzeit
	Label lblDatum = new Label(formGroup, SWT.LEFT);
	lblDatum.setText("Datum / Uhrzeit:");
	lblDatum.setSize(230, lblDatum.getSize().y);

	this.txtDatum = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtDatum.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtDatum.setEnabled(false);

	// this.dtDatum = new DateTime(formGroup, SWT.DATE | SWT.DROP_DOWN
	// | SWT.BORDER);
	// this.dtDatum.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	// this.dtDatum.setEnabled(false);
	//
	// Label lblUhrzeit = new Label(formGroup, SWT.RIGHT);
	// lblUhrzeit.setText("Uhrzeit:");
	// lblUhrzeit.setSize(230, lblUhrzeit.getSize().y);
	//
	// this.dtUhrzeit = new DateTime(formGroup, SWT.TIME | SWT.SHORT
	// | SWT.DROP_DOWN | SWT.BORDER);
	// this.dtUhrzeit.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	// this.dtUhrzeit.setEnabled(false);

	// // 3. Zeile mit Veranstaltungsort und Saal
	// Label lblOrt = new Label(formGroup, SWT.LEFT);
	// lblOrt.setText("Ort: ");
	// lblOrt.setSize(230, lblOrt.getSize().y);
	//
	// this.txtOrt = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	// this.txtOrt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	// this.txtOrt.setEnabled(false);

	// 3.Zeile Saal
	Label lblSaal = new Label(formGroup, SWT.RIGHT);
	lblSaal.setText("Saal:");
	lblSaal.setSize(230, lblSaal.getSize().y);

	this.txtSaal = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtSaal.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtSaal.setEnabled(false);

	// 4. Zeile mit Karten für Kunde
	// Label lblKartenFuer = new Label(formGroup, SWT.LEFT);
	// lblKartenFuer.setText("Karten für Kunde: ");
	// lblKartenFuer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	//
	// Label lblsep1= new Label(formGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
	// lblsep1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	// Label lblsep2= new Label(formGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
	// lblsep2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	// Label lblsep3= new Label(formGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
	// lblsep3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	// Eigene Form Group für Kunde Suchen
	Group formGroupKunde = new Group(formGroup, SWT.BORDER_SOLID
		| SWT.TITLE);
	formGroupKunde.setText("Karten für Kunde: ");
	formGroupKunde.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
		false, 4, 1));
	formGroupKunde.setLayout(new GridLayout(2, false));

	// 1. Zeile Radiobutton anonym
	btnAnonym = new Button(formGroupKunde, SWT.RADIO);
	btnAnonym.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
	btnAnonym.setText("anonym");
	btnAnonym.setToolTipText("Karten für anonymen Kunden");
	btnAnonym.setSelection(true);
	btnAnonym.addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {
		if (btnAnonym.getSelection())
		    setAnonym(true);
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent e) {
		if (btnAnonym.getSelection())
		    setAnonym(true);
	    }

	});

	Label lblEmpty2 = new Label(formGroupKunde, SWT.LEFT);
	lblEmpty2.setText("");

	// 2. Zeile Nachname
	btnName = new Button(formGroupKunde, SWT.RADIO);
	btnName.setLayoutData(new GridData(SWT.LEFT, SWT.DOWN, false, false));
	btnName.setText("Nachname:");
	btnName.setToolTipText("Karten für registrierten Kunden");
	btnName.addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {
		if (btnName.getSelection())
		    setAnonym(false);
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent e) {
		if (btnName.getSelection())
		    setAnonym(false);
	    }

	});

	this.txtNachname = new Text(formGroupKunde, SWT.LEFT | SWT.BORDER);
	this.txtNachname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	// 2. Zeile Vorname
	Label lblVorname = new Label(formGroupKunde, SWT.LEFT);
	lblVorname.setText("      Vorname:");
	lblVorname.setSize(230, lblVorname.getSize().y);

	this.txtVorname = new Text(formGroupKunde, SWT.LEFT | SWT.BORDER);
	this.txtVorname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	// 3. Zeile mit Button Kunde Suchen
	lblSearchSpacer = new Label(formGroupKunde, SWT.LEFT);
	lblSearchSpacer.setText("");

	btnSearch = new Button(formGroupKunde, SWT.PUSH);
	btnSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
	btnSearch.setText("Kunde Suchen");
	btnSearch.setToolTipText("Nach Kunde suchen");
	btnSearch.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		searchHandler();
	    }
	});

	// Voreinstellungen bei Fensteraufruf
	btnName.setSelection(false);
	txtNachname.setEnabled(false);
	txtVorname.setEnabled(false);
	btnSearch.setEnabled(false);

    }

    private void setAnonym(Boolean anonym) {

	btnAnonym.setSelection(anonym);
	btnName.setSelection(!anonym);

	txtNachname.setEnabled(!anonym);
	txtVorname.setEnabled(!anonym);

	if (anonym) {
	    tableViewer.setInput(null);
	} else {
	    txtNachname.setFocus();
	    txtNachname.setSelection(0, txtNachname.getText().length());
	    searchHandler();
	}

	if (!btnSearch.isDisposed()) btnSearch.setEnabled(!anonym);
    }

    private void createTable(Composite c) {
	this.tableViewer = new TableViewer(c, SWT.BORDER | SWT.FULL_SELECTION);
	this.tableViewer.getTable().setLayoutData(
		new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
	TableLayout layout = new TableLayout();
	layout.addColumnData(new ColumnWeightData(25, 100, true));
	layout.addColumnData(new ColumnWeightData(20, 100, true));
	layout.addColumnData(new ColumnWeightData(20, 80, true));
	layout.addColumnData(new ColumnWeightData(15, 80, true));
	layout.addColumnData(new ColumnWeightData(20, 80, true));
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
		case 2:
		    if (k.getAdresse() == null)
			return null;
		    return k.getAdresse().format();
		case 3:
		    if (k.getGeburtsdatum() == null)
			return null;
		    return k.getGeburtsdatum().getTime().toString();
		case 4:
		    return k.getTelnr();

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
		TicketVerkaufView.this.fillKunde();
	    }
	});

	TableColumn colNachname = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colNachname.setText("Nachname");
	TableColumn colVorname = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colVorname.setText("Vorname");
	TableColumn colAdresse = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colAdresse.setText("Adresse");
	TableColumn colGebdat = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colGebdat.setText("Geburtsdatum");
	TableColumn colTelnr = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colTelnr.setText("Telefonnummer");

	// this.getSite().setSelectionProvider(this.tableViewer);

    }

    private void createButtons(Composite c) {

	btnReservieren = new Button(c, SWT.PUSH);
	btnReservieren.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
		false));
	btnReservieren.setText("Ticket reservieren");
	btnReservieren.setToolTipText("Ticket für Kunde reservieren");
	btnReservieren.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		TicketVerkaufView.this.selectionVerkaufData.setVerkauf(false);
		TicketVerkaufView.this.openTicketVerkaufEditor();
	    }
	});
	btnVerkaufen = new Button(c, SWT.PUSH);
	btnVerkaufen.setLayoutData(new GridData(SWT.RIGHT_TO_LEFT, SWT.TOP,
		false, false));
	btnVerkaufen.setText("Ticket verkaufen");
	btnVerkaufen.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		// verkauf-flag auf true setzen, da es sich nun um einen Verkauf
		// handelt
		TicketVerkaufView.this.selectionVerkaufData.setVerkauf(true);
		TicketVerkaufView.this.openTicketVerkaufEditor();
	    }
	});

    }

    public void setData(Auffuehrung a) {
	this.auffuehrung = a;
	this.txtAuffuehrung.setText(a.getVeranstaltung().getBezeichnung());
	Calendar cal = new GregorianCalendar();
	cal.setTime(a.getDatumuhrzeit());
	String dat = cal.get(GregorianCalendar.DATE) + "."
		+ (cal.get(GregorianCalendar.MONTH) + 1) + "."
		+ cal.get(GregorianCalendar.YEAR) + " / "
		+ cal.get(GregorianCalendar.HOUR_OF_DAY) + ":"
		+ cal.get(GregorianCalendar.MINUTE) + " Uhr";
	this.txtDatum.setText(dat);
	this.txtSaal.setText(a.getSaal().getBezeichnung());

	this.selectionVerkaufData.setAuffuehrung(a);
    }

    public void fillKunde() {
	try {
	    // TODO what the hell?
	    log.debug("Doppelklick --> DO SOMETHING");

	} catch (Exception ex) {
	    TicketVerkaufView.this.log.error(ex);

	    MessageDialog.openError(TicketVerkaufView.this.getSite()
		    .getWorkbenchWindow().getShell(), "Error",
		    "Der ausgewählte Kunde, konnte nicht übernommen werden"
			    + ex.getMessage());
	}
    }

    public void openTicketVerkaufEditor() {
	IHandlerService handlerService = (IHandlerService) TicketVerkaufView.this
		.getSite().getService(IHandlerService.class);

	// fülle Die Selection Data mit ausgewähltem Kunden
	IStructuredSelection structuredSelection = (IStructuredSelection) this.tableViewer
		.getSelection();

	if (this.btnAnonym.getSelection()) {
	    // es soll an einen anonymen Kunden verkauft/reserviert werden
	    this.selectionVerkaufData.setKunde(new Kunde());
	} else if (!structuredSelection.isEmpty()) {
	    // setze ausgewählten Kunden als Daten
	    this.selectionVerkaufData.setKunde((Kunde) structuredSelection
		    .getFirstElement());
	} else {
	    if (TicketVerkaufView.this.selectionVerkaufData.isVerkauf()) {
		MessageDialog
			.openInformation(
				TicketVerkaufView.this.getSite()
					.getWorkbenchWindow().getShell(),
				"Kein Kunde ausgewählt",
				"Bitte wählen Sie einen Kunden aus, oder selektieren Sie 'anonym', um Tickets zu verkaufen");
		return;
	    } /*
	       * else { MessageDialog
	       * .openInformation(TicketVerkaufView.this.getSite()
	       * .getWorkbenchWindow().getShell(), "Kein Kunde ausgewählt",
	       * "Bitte wählen Sie einen Kunden aus, um Tickets zu reservieren"
	       * ); setAnonym(false); return; }
	       */

	}

	try {
	    handlerService.executeCommand(
		    "at.ticketline.command.EditTicketVerkauf", null);
	} catch (Exception ex) {
	    TicketVerkaufView.this.log.error(ex);

	    MessageDialog.openError(
		    TicketVerkaufView.this.getSite().getWorkbenchWindow()
			    .getShell(),
		    "Error",
		    "Tickets können nicht reserviert/verkauft werden: "
			    + ex.getMessage());
	}
    }

    @Override
    public void searchHandler() {
	
	// nur suchen wenn nicht auf anonym gestellt ist
	if (!btnAnonym.getSelection()) {
        	Kunde k = TicketVerkaufView.this.createKundeQuery();
        	TicketVerkaufView.this.log.info("Querying for #0", k.toString());
        	TicketVerkaufView.this.tableViewer.setInput(k);
	}
    }

    @Override
    public void enableInstantSearch() {

	lblSearchSpacer.dispose();
	btnSearch.dispose();

	InstantSearch.addInstantSearch(this.txtNachname, this);
	InstantSearch.addInstantSearch(this.txtVorname, this);
    }

    @Override
    public void setFocus() {
	 if(c != null) // Context sensitive help
	      c.setFocus(); // context sensitive help
    }

    protected Kunde createKundeQuery() {
	Kunde k = new Kunde();
	String n = this.txtNachname.getText().trim();
	String v = this.txtVorname.getText().trim();
	if (n.length() > 0) {
	    k.setNachname(n);
	}
	if (v.length() > 0) {
	    k.setVorname(v);
	}
	k.setGruppe(null);
	return k;
    }

    class KundeContentProvider implements IStructuredContentProvider {
	private KundeDao kundeDao = null;

	@Override
	public Object[] getElements(Object query) {
	    if ((query instanceof Kunde) == false) {
		TicketVerkaufView.this.log.info(
			"Query object not of type Kunde: #0", query.getClass()
				.getName());
		return new Object[0];
	    }
	    try {
		return this.kundeDao.findByKunde((Kunde) query).toArray();

	    } catch (Exception e) {
		TicketVerkaufView.this.log
			.info("Exception in KundeContentProvider: #0",
				e.getMessage());
		return new Object[0];
	    }
	}

	public void setKundeDao(KundeDao kundeDao) {
	    this.kundeDao = kundeDao;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

    }

    /**
     * Klasse, die die Daten für den aktuellen Verkauf/Reservierung beinhält
     * Sollte stets die aktuellen Inhalte des Verkaufs beinhalten soll a ...
     * Aufführung zu der verkauft/reserviert werden soll k ... der Kunde der die
     * Tickets haben möchte verkauf ... true wenn es sich um einen Verkauf
     * handelt, false wenn Reservierung
     * 
     * @author Andrea
     * 
     */
    public class TicketVerkaufData implements IStructuredSelection {

	Auffuehrung a = null;
	Kunde k = null;
	boolean verkauf = false;

	public TicketVerkaufData() {
	    this.k = new Kunde();
	    this.a = new Auffuehrung();
	}

	public void setAuffuehrung(Auffuehrung a) {
	    this.a = a;
	}

	public void setKunde(Kunde k) {
	    this.k = k;
	}

	public Kunde getKundeData() {
	    return k;
	}

	public Auffuehrung getAuffuehrungData() {
	    return a;
	}

	public void setVerkauf(boolean verkauf) {
	    this.verkauf = verkauf;
	}

	public boolean isVerkauf() {
	    return verkauf;
	}

	@Override
	public boolean isEmpty() {
	    if (k == null)
		return true;
	    if (k.getId() == null) {
		return true;
	    } else {
		return false;
	    }
	}

	@Override
	public Object getFirstElement() {
	    return k;
	}

	@Override
	public Iterator<Kunde> iterator() {
	    return (new ArrayList<Kunde>()).iterator();
	}

	@Override
	public int size() {
	    if (k == null)
		return 0;
	    if (k.getId() == null) {
		return 0;
	    } else {
		return 1;
	    }
	}

	@Override
	public Object[] toArray() {
	    return (new ArrayList<Kunde>()).toArray();
	}

	@Override
	public List<Kunde> toList() {
	    return new ArrayList<Kunde>();
	}
    }

    public class TicketSelectionProvider implements ISelectionProvider {

	TicketVerkaufData selection;

	public TicketSelectionProvider(ISelection selection) {
	    this.selection = (TicketVerkaufData) selection;
	}

	@Override
	public void setSelection(ISelection selection) {
	    this.selection = (TicketVerkaufData) selection;
	}

	@Override
	public ISelection getSelection() {
	    return this.selection;
	}

	@Override
	public void addSelectionChangedListener(
		ISelectionChangedListener listener) {
	    // nothing to do
	}

	@Override
	public void removeSelectionChangedListener(
		ISelectionChangedListener listener) {
	    // nothing to do
	}
    }

}
