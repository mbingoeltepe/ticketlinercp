package at.ticketline.kassa.ui.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.PreisKategorie;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.AuffuehrungServiceImpl;
import at.ticketline.service.ValidateException;
import at.ticketline.service.interfaces.AuffuehrungService;

public class AuffuehrungSucheView extends ViewPart {
    public static final String ID = "at.ticketline.view.suche.auffuehrung";
    protected Logger log = LogFactory.getLogger(this.getClass());

    protected VeranstaltungContentProvider veranstaltungProvider;
    protected SaalContentProvider saalProvider;

    // Daten
    private AuffuehrungService auffuehrungService = null;
    // Liste der Aufführungen als Selection, damit man im Commandhandler darauf
    // zugreifen kann
    AuffuehrungResultList resultList;

    // Parent
    Composite parent;

    // Labels
    protected Label lblDatumZeit;
    protected Label lblDatum;
    protected Label lblZeit;

    // Eingabefelder
    protected Button btnDatumZeit;
    protected DateTime dtDatum;
    protected DateTime dtZeit;
    protected Combo cbPreis;
    public ComboViewer cvVeranstaltung;
    protected ComboViewer cvSaal;
    private Composite c;

    @Override
    public void init(IViewSite site) throws PartInitException {
	super.init(site);

	// provider für Comboboxen
	this.veranstaltungProvider = new VeranstaltungContentProvider();
	this.saalProvider = new SaalContentProvider();

	AuffuehrungService service = new AuffuehrungServiceImpl();
	this.auffuehrungService = service;

	this.veranstaltungProvider.setAuffuehrungService(service);
	this.saalProvider.setAuffuehrungService(service);

	this.resultList = new AuffuehrungResultList();
	// setze SelectionProvider für diese View, damit im CommandHandler auf
	// die
	// Ergebnis-Liste zugegriffen werden kann
	this.getSite().setSelectionProvider(
		new AuffuehrungSelectionProvider(this.resultList));

    }

    @Override
    public void createPartControl(Composite parent) {
	this.parent = parent;
	c = new Composite(parent, SWT.NONE);
	c.setLayout(new GridLayout(2, false));

	this.createForm(c);
	
	 PlatformUI.getWorkbench().getHelpSystem().setHelp(c, "TicketlineRCP.auffuehrung_suchen"); // contextsensitive hilfe

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

	this.btnDatumZeit = new Button(formGroup, SWT.CHECK);
	this.btnDatumZeit.setText("Datum/Uhrzeit");
	this.btnDatumZeit
		.setToolTipText("Aktiviere/Deaktiviere Suche nach Datum/Uhrzeit");
	this.btnDatumZeit.setSelection(true);
	this.btnDatumZeit.setSize(20, this.btnDatumZeit.getSize().y);
	this.btnDatumZeit.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchButtonHandler();
	    }
	});
	// füge einen Listener hinzu, der je nach Selektion die Felder Datum und
	// Zeit aktiviert
	// oder inaktiviert
	this.btnDatumZeit.addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {
		if (btnDatumZeit.getSelection() == false) {
		    dtDatum.setEnabled(false);
		    dtZeit.setEnabled(false);
		    lblZeit.setEnabled(false);
		    lblDatum.setEnabled(false);
		} else {
		    dtDatum.setEnabled(true);
		    dtZeit.setEnabled(true);
		    lblDatum.setEnabled(true);
		    lblZeit.setEnabled(true);
		}
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent e) {
	    }

	});
	this.btnDatumZeit.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchButtonHandler();
	    }
	});

	Label spacer = new Label(formGroup, SWT.NULL);
	spacer.setText("");

	lblDatum = new Label(formGroup, SWT.LEFT);
	lblDatum.setText("Datum:");
	// lblDatum.setSize(230, lblDatum.getSize().y);

	this.dtDatum = new DateTime(formGroup, SWT.DATE | SWT.DROP_DOWN
		| SWT.BORDER);
	this.dtDatum.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.dtDatum.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchButtonHandler();
	    }
	});

	lblZeit = new Label(formGroup, SWT.LEFT);
	lblZeit.setText("Zeit:");

	this.dtZeit = new DateTime(formGroup, SWT.TIME | SWT.SHORT
		| SWT.DROP_DOWN | SWT.BORDER);
	this.dtZeit.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.dtZeit.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchButtonHandler();
	    }
	});

	Label lblPreis = new Label(formGroup, SWT.LEFT);
	lblPreis.setText("Preis:");

	this.cbPreis = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
	this.cbPreis.setItems(new String[] { "", "Mindestpreis",
		"Standardpreis", "Maximalpreis" });
	this.cbPreis.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.cbPreis.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchButtonHandler();
	    }
	});

	/*
	 * this.txtPreis = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	 * this.txtPreis.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	 * this.txtPreis.addVerifyListener(new VerifyListener() {
	 * 
	 * @Override public void verifyText(VerifyEvent e) { switch (e.keyCode)
	 * { case SWT.BS: // Backspace case SWT.DEL: // Delete case SWT.HOME: //
	 * Home case SWT.END: // End case SWT.ARROW_LEFT: // Left arrow case
	 * SWT.ARROW_RIGHT: // Right arrow return; }
	 * 
	 * if (e.character == '.') { if (txtPreis.getText().contains(".")) {
	 * e.doit = false; // Aktion nicht erlauben, weil Komma // schon
	 * vorhanden return; } } if (!Character.isDigit(e.character) &&
	 * (e.character != '.')) { e.doit = false; // Aktion nicht erlauben,
	 * weil es keine Zahl // ist } }
	 * 
	 * });
	 */

	Label lblVeranstaltung = new Label(formGroup, SWT.LEFT);
	lblVeranstaltung.setText("Veranstaltung:");

	this.cvVeranstaltung = new ComboViewer(formGroup);
	this.cvVeranstaltung.getCombo().setLayoutData(
		new GridData(GridData.FILL_HORIZONTAL));

	this.cvVeranstaltung.setContentProvider(this.veranstaltungProvider);
	// this.cvVeranstaltung.getCombo().add("", 0);

	this.cvVeranstaltung.setLabelProvider(new ILabelProvider() {

	    @Override
	    public String getText(Object element) {
		Veranstaltung v = (Veranstaltung) element;

		String text = v.getBezeichnung();
		if (v.getJahrerstellung() != null) {
		    text += " " + v.getJahrerstellung();
		}
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
		// nothing to do
		return null;
	    }
	});
	this.cvVeranstaltung.setInput(new Veranstaltung());

	Label lblSaal = new Label(formGroup, SWT.LEFT);
	lblSaal.setText("Saal:");

	this.cvSaal = new ComboViewer(formGroup);
	this.cvSaal.getCombo().setLayoutData(
		new GridData(GridData.FILL_HORIZONTAL));

	this.cvSaal.setContentProvider(this.saalProvider);

	this.cvSaal.setLabelProvider(new ILabelProvider() {

	    @Override
	    public String getText(Object element) {
		Saal s = (Saal) element;

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
	this.cvSaal.setInput(new Saal());

	Label lblEmpty = new Label(formGroup, SWT.LEFT);
	lblEmpty.setText("");

	Button btnSuche = new Button(formGroup, SWT.PUSH);
	btnSuche.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
	btnSuche.setText(" Suchen ");
	btnSuche.setToolTipText("Nach Aufführungen suchen");
	btnSuche.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {

		searchButtonHandler();
	    }
	});

	// default einstellung: nicht nach datum suchen:

	this.btnDatumZeit.setSelection(false);
	this.dtDatum.setEnabled(false);
	this.dtZeit.setEnabled(false);
	this.lblZeit.setEnabled(false);
	this.lblDatum.setEnabled(false);
    }

    /**
     * Liest aus den Eingabefelder die Informationen heraus und erzeugt die
     * Auffuehrung, nach dessen Kriterien die Liste aus der Datenbank abgefragt
     * werden soll.
     * 
     * @return Aufführungs Query Objekt
     */
    protected Auffuehrung createAuffuehrungQuery() {

	Auffuehrung a = new Auffuehrung();

	IStructuredSelection structuredSelection;

	if (this.btnDatumZeit.getSelection() == true) {
	    // suche nach Datum Zeit ebenfalls
	    Calendar c = new GregorianCalendar();
	    c.clear();
	    c.set(this.dtDatum.getYear(), this.dtDatum.getMonth(),
		    this.dtDatum.getDay(), this.dtZeit.getHours(),
		    this.dtZeit.getMinutes());

	    Date d = c.getTime();
	    a.setDatumuhrzeit(d);
	}

	if (this.cbPreis.getSelectionIndex() > 0) {
	    // es wurde eine gültige Preiskategorie ausgewählt
	    String preis = this.cbPreis.getItem(this.cbPreis
		    .getSelectionIndex());

	    if (preis.equals("Mindestpreis")) {
		a.setPreis(PreisKategorie.MINDESTPREIS);
	    } else if (preis.equals("Standardpreis")) {
		a.setPreis(PreisKategorie.STANDARDPREIS);
	    } else if (preis.equals("Maximalpreis")) {
		a.setPreis(PreisKategorie.MAXIMALPREIS);
	    } else {
		log.info("Keine gültige Preiskategorie: #0", preis);
	    }
	}

	if (this.cvVeranstaltung.getCombo().getSelectionIndex() > 0) {
	    // es wurde eine gültige Veranstaltung ausgewählt
	    structuredSelection = (IStructuredSelection) this.cvVeranstaltung
		    .getSelection();
	    Veranstaltung v = (Veranstaltung) structuredSelection
		    .getFirstElement();
	    a.setVeranstaltung(v);
	}

	if (this.cvSaal.getCombo().getSelectionIndex() > 0) {
	    // es wurde ein gültiger Saal ausgewählt
	    structuredSelection = (IStructuredSelection) this.cvSaal
		    .getSelection();
	    Saal s = (Saal) structuredSelection.getFirstElement();
	    a.setSaal(s);
	}

	return a;
    }

    public List<Auffuehrung> searchAuffuehrungen(Auffuehrung query) {
	try {
	    return auffuehrungService.findAuffuehrungen((Auffuehrung) query);

	} catch (ValidateException ve) {
	    MessageDialog.openInformation(AuffuehrungSucheView.this.getSite()
		    .getWorkbenchWindow().getShell(), "Ungültiges Datum",
		    "Der ausgewählte Zeitpunkt ist bereits vorbei");
	    return new ArrayList<Auffuehrung>();
	}
    }

    public void openAuffuehrungListeEditor(List<Auffuehrung> list, Auffuehrung a) {

	// setze Objekte in Selection, damit diese dann im CommandHandler
	// rausgelesen werden können
	this.resultList.setResultList(list);
	this.resultList.setQueryObjekt(a);

	if (list.isEmpty()) {
	    MessageDialog.openInformation(AuffuehrungSucheView.this.getSite()
		    .getWorkbenchWindow().getShell(), "Information",
		    "Es wurden keine Aufführungen gefunden!");
	    return;
	}

	IHandlerService handlerService = (IHandlerService) AuffuehrungSucheView.this
		.getSite().getService(IHandlerService.class);

	try {
	    handlerService.executeCommand(
		    "at.ticketline.command.AuffuehrungListeDirekt", null);
	} catch (Exception ex) {
	    AuffuehrungSucheView.this.log.error(ex);

	    MessageDialog.openError(
		    AuffuehrungSucheView.this.getSite().getWorkbenchWindow()
			    .getShell(),
		    "Error",
		    "Aufführungsliste kann nicht angezeigt werden: "
			    + ex.getMessage());
	}
    }

    @Override
    public void setFocus() {
	 if(c != null) // Context sensitive help
	      c.setFocus(); // context sensitive help
	this.btnDatumZeit.setFocus();
    }

    /**
     * Behandelt die Such Anfrage
     */
    protected void searchButtonHandler() {
	Auffuehrung a = AuffuehrungSucheView.this.createAuffuehrungQuery();
	AuffuehrungSucheView.this.log.info("Querying for #0", a.toString());
	// AuffuehrungSucheView.this.tableViewer.setInput(a);

	List<Auffuehrung> alist = AuffuehrungSucheView.this
		.searchAuffuehrungen(a);

	openAuffuehrungListeEditor(alist, a);
    }

    /**
     * Klasse, die für den Inhalt der Saal-Combobox verantwortlich ist
     */
    class SaalContentProvider implements IStructuredContentProvider {
	private AuffuehrungService auffuehrungService = null;

	@Override
	public Object[] getElements(Object query) {
	    if ((query instanceof Saal) == true) {
		// Content für die Saal Combobox
		// egal welcher Saal übergeben wird, es werden alle
		// Veranstaltungen
		// als Content zurückgeliefert, da die ComboBox nur eine Liste
		// aller Veranst. ist
		List<Saal> sList = auffuehrungService.findAllSaele();
		sList.add(0, new Saal()); // Dummy-Element für <keine
					  // Auswahl>
		return sList.toArray();

	    } else {
		AuffuehrungSucheView.this.log.info(
			"Query object not of type Saal: #0", query.getClass()
				.getName());
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

    /**
     * Klasse, die für den Inhalt der Veranstaltungs-Combobox verantwortlich ist
     */
    class VeranstaltungContentProvider implements IStructuredContentProvider {
	private AuffuehrungService auffuehrungService = null;

	@Override
	public Object[] getElements(Object query) {
	    if ((query instanceof Veranstaltung) == true) {
		// Content für die Veranstaltung Combobox
		// egal welche Veranstaltung übergeben wird, es werden alle
		// Veranstaltungen
		// als Content zurückgeliefert, da die ComboBox nur eine Liste
		// aller Veranst. ist
		List<Veranstaltung> vList = auffuehrungService
			.findAllVeranstaltungen();
		vList.add(0, new Veranstaltung()); // Dummy-Element für <keine
						   // Auswahl>
		return vList.toArray();

	    } else {
		AuffuehrungSucheView.this.log.info(
			"Query object not of type Veranstaltung: #0", query
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

    public class AuffuehrungResultList implements IStructuredSelection {

	List<Auffuehrung> resultList = null;
	Auffuehrung queryObjekt = null;

	public AuffuehrungResultList() {
	    this.resultList = new ArrayList<Auffuehrung>();
	}

	public void setResultList(List<Auffuehrung> list) {
	    this.resultList = list;
	}

	public List<Auffuehrung> getResultList() {
	    return this.resultList;
	}

	public void setQueryObjekt(Auffuehrung a) {
	    this.queryObjekt = a;
	}

	public Auffuehrung getQueryObjekt() {
	    return this.queryObjekt;
	}

	@Override
	public boolean isEmpty() {
	    return resultList.isEmpty();
	}

	@Override
	public Object getFirstElement() {
	    return resultList.get(0);
	}

	@Override
	public Iterator<Auffuehrung> iterator() {
	    return resultList.iterator();
	}

	@Override
	public int size() {
	    return resultList.size();
	}

	@Override
	public Object[] toArray() {
	    return resultList.toArray();
	}

	@Override
	public List<Auffuehrung> toList() {
	    return resultList;
	}
    }

    public class AuffuehrungSelectionProvider implements ISelectionProvider {

	AuffuehrungResultList selection;

	public AuffuehrungSelectionProvider(ISelection selection) {
	    this.selection = (AuffuehrungResultList) selection;
	}

	@Override
	public void setSelection(ISelection selection) {
	    this.selection = (AuffuehrungResultList) selection;
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
