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
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.EngagementDao;
import at.ticketline.dao.interfaces.KuenstlerDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Engagement;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kuenstler;
import at.ticketline.kassa.ui.editor.AuffuehrungListeEditor;
import at.ticketline.kassa.ui.editor.AuffuehrungListeEditorInput;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.KuenstlerServiceImpl;
import at.ticketline.service.interfaces.KuenstlerService;
import at.ticketline.util.InstantSearch;
import at.ticketline.util.SearchView;

/**
 * @author ???, Georg Fuderer
 * 
 * @see at.ticketline.util.SearchView
 * @see at.ticketline.util.InstantSearch
 * 
 */
public class KuenstlerSucheView extends ViewPart implements SearchView {
    public static final String ID = "at.ticketline.view.suche.kuenstler";

    protected Logger log = LogFactory.getLogger(KuenstlerSucheView.class);

    public TableViewer tableViewer;
    protected KuenstlerContentProvider kuenstlerProvider;

    protected Text txtNachname;
    protected Text txtVorname;
    protected Combo cbGeschlecht;

    protected Label lblSearchSpacer;
    protected Button btnSearch;
    
    private Composite c;

    KuenstlerService kuenstlerService = new KuenstlerServiceImpl();

    @Override
    public void init(IViewSite site) throws PartInitException {
	super.init(site);
	this.kuenstlerProvider = new KuenstlerContentProvider();
	this.kuenstlerProvider.setKuenstlerDao((KuenstlerDao) DaoFactory
		.findDaoByEntity(Kuenstler.class));
    }

    @Override
    public void createPartControl(Composite parent) {
c = new Composite(parent, SWT.NONE);
	c.setLayout(new GridLayout(2, false));

	this.createForm(c);

	this.createTable(c);

	this.createEditButton(c);

	this.createShowAuffuehrungenButton(c);

	if (enableInstantSearchOnStartup) enableInstantSearch();
	
	 PlatformUI.getWorkbench().getHelpSystem().setHelp(c, "TicketlineRCP.kuenstler_suchen"); // contextsensitive hilfe

    }

    public void createForm(Composite parent) {
	Group formGroup = new Group(parent, SWT.BORDER_SOLID);
	formGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2,
		1));
	formGroup.setLayout(new GridLayout(2, false));

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
	this.txtNachname.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	Label lblVorname = new Label(formGroup, SWT.LEFT);
	lblVorname.setText("Vorname:");

	this.txtVorname = new Text(formGroup, SWT.LEFT | SWT.BORDER);
	this.txtVorname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtVorname.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

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
	btnSearch.setToolTipText("Nach Künstlern suchen");
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

	this.tableViewer.setContentProvider(this.kuenstlerProvider);
	this.tableViewer.setLabelProvider(new ITableLabelProvider() {

	    @Override
	    public Image getColumnImage(Object arg0, int arg1) {
		return null;
	    }

	    @Override
	    public String getColumnText(Object element, int index) {
		Kuenstler k = (Kuenstler) element;
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
		// KuenstlerSucheView.this.openKuenstlerEditor();
		openAuffuehrungVonVeranstaltungEditor();
	    }
	});
	this.tableViewer
		.addSelectionChangedListener(new ISelectionChangedListener() {

		    @Override
		    public void selectionChanged(SelectionChangedEvent event) {
			// Nur Suche starten, wenn auch etwas ausgewählt ist:
			if (!event.getSelection().isEmpty())
			    openAuffuehrungVonVeranstaltungEditor();
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
	btnEdit.setToolTipText("Künstler bearbeiten");
	btnEdit.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		KuenstlerSucheView.this.openKuenstlerEditor();
	    }
	});
    }

    private void createShowAuffuehrungenButton(Composite c) {
	Label lblEmpty = new Label(c, SWT.LEFT);
	lblEmpty.setText("");

	Button btnEdit = new Button(c, SWT.PUSH);
	btnEdit.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
	btnEdit.setText(" Aufführungen anzeigen ");
	btnEdit.setToolTipText("Auffuehrungen dieses Kuenstlers anzeigen");
	btnEdit.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {

		openAuffuehrungVonVeranstaltungEditor();
	    }
	});
    }

    public void enableInstantSearch() {

	lblSearchSpacer.dispose();
	btnSearch.dispose();

	InstantSearch.addInstantSearch(this.txtNachname, this);
	InstantSearch.addInstantSearch(this.txtVorname, this);
	InstantSearch.addInstantSearch(this.cbGeschlecht, this);

	searchHandler();
    }

    private void showAllHandler() {
	
	txtNachname.setText("");
	txtVorname.setText("");
	cbGeschlecht.select(0);
	
	searchHandler();
    }
    
    /**
     * Sucht alle Auffuehrungen des Kuenstlers und oeffnet einen Editor, welche
     * diese anzeigt.
     */
    public void openAuffuehrungVonVeranstaltungEditor() {
	Engagement engangement = new Engagement();
	EngagementDao engagementDao = (EngagementDao) DaoFactory
		.findDaoByEntity(Engagement.class);
	IStructuredSelection selection;

	Kuenstler k = null;
	selection = (IStructuredSelection) tableViewer.getSelection();
	if (selection.isEmpty()) {
	    MessageDialog.openInformation(this.getSite().getWorkbenchWindow()
		    .getShell(), "Künstler auswählen",
		    "Es wurde kein Künstler ausgewaehlt");
	    return;
	}
	k = (Kuenstler) selection.getFirstElement();

	List<Kuenstler> kuenstlerList = kuenstlerService.findByKuenstler(k);

	for (Kuenstler kuenstler : kuenstlerList) {
	    engangement.setKuenstler(kuenstler);
	    List<Engagement> engagementList = engagementDao
		    .findByEngagement(engangement);
	    for (Engagement en : engagementList) {
		List<Auffuehrung> auffuehrungList = new ArrayList<Auffuehrung>();
		for (Auffuehrung a : en.getVeranstaltung().getAuffuehrungen())
		    auffuehrungList.add(a);
		if (auffuehrungList.size() == 0) {
		    MessageDialog
			    .openInformation(this.getSite()
				    .getWorkbenchWindow().getShell(),
				    "keine Aufführungen",
				    "Es konnten keine Aufführungen zum ausgewählten Künstler gefunden werden");
		    return;
		}
		AuffuehrungListeEditorInput editorInput = new AuffuehrungListeEditorInput(
			auffuehrungList, selection.getFirstElement());
		try {
		    this.getSite().getPage()
			    .openEditor(editorInput, AuffuehrungListeEditor.ID);
		} catch (Exception e) {
		    log.error("Unable to open AuffuehrungListeEditor");
		}
	    }
	    if (engagementList.isEmpty()) {
		AuffuehrungListeEditorInput editorInput = new AuffuehrungListeEditorInput(
			new ArrayList<Auffuehrung>(), selection.getFirstElement());
		try {
		    this.getSite().getPage()
			    .openEditor(editorInput, AuffuehrungListeEditor.ID);
		} catch (Exception e) {
		    log.error("Unable to open AuffuehrungListeEditor");
		}
	    }
	}
    }

    @Override
    public void setFocus() {
	 if(c != null) // Context sensitive help
	      c.setFocus(); // context sensitive help
	this.txtNachname.setFocus();
	this.txtNachname.setSelection(0, this.txtNachname.getText().length());
    }

    protected Kuenstler createKuenstlerQuery() {
	Kuenstler k = new Kuenstler();
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
	return k;
    }

    public void openKuenstlerEditor() {
	IHandlerService handlerService = (IHandlerService) KuenstlerSucheView.this
		.getSite().getService(IHandlerService.class);
	try {
	    handlerService.executeCommand(
		    "at.ticketline.command.EditKuenstler", null);
	} catch (Exception ex) {
	    KuenstlerSucheView.this.log.error(ex);

	    MessageDialog
		    .openError(
			    KuenstlerSucheView.this.getSite()
				    .getWorkbenchWindow().getShell(),
			    "Error",
			    "Künstler kann nicht bearbeitet werden: "
				    + ex.getMessage());
	}
    }

    /**
     * Behandelt die Such Anfrage
     */
    @Override
    public void searchHandler() {
	Kuenstler k = KuenstlerSucheView.this.createKuenstlerQuery();
	KuenstlerSucheView.this.log.info("Querying for #0", k.toString());
	KuenstlerSucheView.this.tableViewer.setInput(k);
    }

    class KuenstlerContentProvider implements IStructuredContentProvider {
	private KuenstlerDao kuenstlerDao = null;

	@Override
	public Object[] getElements(Object query) {
	    if ((query instanceof Kuenstler) == false) {
		KuenstlerSucheView.this.log.info(
			"Query object not of type Kuenstler: #0", query
				.getClass().getName());
		return new Object[0];
	    }
	    try {
		return this.kuenstlerDao.findByKuenstler((Kuenstler) query)
			.toArray();

	    } catch (Exception e) {
		KuenstlerSucheView.this.log.info(
			"Exception in KuenstlerContentProvider: #0",
			e.getMessage());
		return new Object[0];
	    }
	}

	public void setKuenstlerDao(KuenstlerDao kuenstlerDao) {
	    this.kuenstlerDao = kuenstlerDao;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

    }
}
