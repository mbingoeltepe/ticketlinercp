package at.ticketline.kassa.ui.editor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.PlatzDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Platz;
import at.ticketline.entity.TopTen;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;

public class TopTenEditor extends EditorPart {
    public static final String ID = "at.ticketline.editor.topten";
    protected Logger log = LogFactory.getLogger(this.getClass());

    // Labels
    protected Label lblMonat;

    // Eingabefelder
    protected DateTime dtZeitraum;
    protected Text txtKategorie;
    protected Button btnChart;

    private FormToolkit toolkit;
    private ScrolledForm form;

    // Table
    public TableViewer tableViewer;
    // content provider für den Table
    protected VeranstaltungContentProvider veranstaltungProvider;
    // DAO
    private PlatzDao platzDao;
    // Liste der aktuell gefundenen TopTen
    private List<TopTen> topten;

    // Chart
    JFreeChart chart;
    DefaultCategoryDataset dataSet;
    ChartComposite chartComposite;
    
    private Composite c;

    @Override
    public void init(IEditorSite site, IEditorInput input)
	    throws PartInitException {
	this.setSite(site);
	this.setInput(input);

	this.setPartName("Top Ten");
	this.veranstaltungProvider = new VeranstaltungContentProvider();

	// Service für den contentProvider
	platzDao = (PlatzDao) DaoFactory.findDaoByEntity(Platz.class);
	topten = new ArrayList<TopTen>();
	// TODO Dummy DATEN
	Veranstaltung v1 = new Veranstaltung();
	v1.setBezeichnung("Veranstaltung 1");
	v1.setDauer(90);
	Veranstaltung v2 = new Veranstaltung();
	v2.setBezeichnung("Veranstaltung 2");
	v2.setDauer(90);
	Veranstaltung v3 = new Veranstaltung();
	v3.setBezeichnung("Veranstaltung 3");
	v3.setDauer(90);
	Veranstaltung v4 = new Veranstaltung();
	v4.setBezeichnung("Veranstaltung 4");
	v4.setDauer(90);
	Veranstaltung v5 = new Veranstaltung();
	v5.setBezeichnung("Veranstaltung 5");
	v5.setDauer(90);
	Veranstaltung v6 = new Veranstaltung();
	v6.setBezeichnung("Veranstaltung 6");
	v6.setDauer(90);
	Veranstaltung v7 = new Veranstaltung();
	v7.setBezeichnung("Veranstaltung 7");
	v7.setDauer(90);
	TopTen t1 = new TopTen(v1, 10);
	TopTen t2 = new TopTen(v2, 10);
	TopTen t3 = new TopTen(v3, 9);
	TopTen t4 = new TopTen(v4, 8);
	TopTen t5 = new TopTen(v5, 7);
	TopTen t6 = new TopTen(v6, 3);
	TopTen t7 = new TopTen(v7, 2);
	topten.add(t1);
	topten.add(t2);
	topten.add(t3);
	topten.add(t4);
	topten.add(t5);
	topten.add(t6);
	topten.add(t7);
    }

    @Override
    public void createPartControl(Composite parent) {
	this.c = parent;
	parent.setLayout(new GridLayout(1, false));

	this.toolkit = new FormToolkit(parent.getDisplay());
	this.form = this.toolkit.createScrolledForm(parent);
	this.form.setLayoutData(new GridData(GridData.FILL_BOTH));
	this.form.getBody().setLayout(new GridLayout(1, false));
	this.form.setText("Top Ten");

	this.createForm(this.form.getBody());
	
	 PlatformUI.getWorkbench().getHelpSystem().setHelp(c, "TicketlineRCP.topten"); // contextsensitive hilfe

    }

    public void createForm(Composite parent) {
	Composite c = this.toolkit.createComposite(parent);
	c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	ColumnLayout columnLayout = new ColumnLayout();
	columnLayout.minNumColumns = 1;
	columnLayout.maxNumColumns = 1;
	c.setLayout(columnLayout);

	// Top - Kriterien
	Section topSection = this.toolkit.createSection(c, Section.DESCRIPTION
		| Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
	topSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		// TicketVerkaufEditor.this.form.reflow(true);
	    }
	});
	topSection.setText("Zeitraum und Kategorie");
	// topSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	Composite top = this.toolkit.createComposite(topSection);
	top.setLayout(new GridLayout(2, false));

	// Felder
	Label lblZeitraum = new Label(top, SWT.LEFT);
	lblZeitraum.setText("Zeitraum: ");
	this.dtZeitraum = new DateTime(top, SWT.DATE | SWT.SHORT
		| SWT.DROP_DOWN | SWT.BORDER);
	dtZeitraum.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	Label lblKategorie = new Label(top, SWT.LEFT);
	lblKategorie.setText("Kategorie: ");
	this.txtKategorie = new Text(top, SWT.LEFT | SWT.BORDER);
	txtKategorie.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event e) {
		searchHandler();
	    }
	});

	this.btnChart = new Button(top, SWT.PUSH);
	btnChart.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
	btnChart.setText("  Chart  ");
	btnChart.setToolTipText("Chart anzeigen");
	btnChart.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		updateChart();
	    }
	});

	topSection.setClient(top);

	// Middle - Diagramm
	Section middleSection = this.toolkit.createSection(c,
		Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
			| Section.EXPANDED);
	middleSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		// TicketVerkaufEditor.this.form.reflow(true);
	    }
	});
	middleSection.setText("Chart");
	// middleSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	Composite middle = this.toolkit.createComposite(middleSection);
	// middle.setLayout(new GridLayout(1, false));

	middle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	c.setLayout(columnLayout);

	displayChart(middle);

	middleSection.setClient(middle);

	// BOTTOM - Liste der Veranstaltungen
	Section bottomSection = this.toolkit.createSection(c,
		Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
			| Section.EXPANDED);
	bottomSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		// TicketVerkaufEditor.this.form.reflow(true);
	    }
	});
	bottomSection.setText("Liste der Veranstaltungen");
	// middleSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	Composite bottom = this.toolkit.createComposite(bottomSection);
	// middle.setLayout(new GridLayout(1, false));

	bottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	// c.setLayout(columnLayout);
	bottom.setLayout(new GridLayout(1, false));

	// Button btn = new Button(bottom, SWT.PUSH);
	// btn.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
	// btn.setText("  Chart  ");
	// btn.setToolTipText("Chart anzeigen");

	createTable(bottom);

	bottomSection.setClient(bottom);

	bottomSection.setExpanded(false);
	bottomSection.setExpanded(true);

	updateChart();
    }

    private void displayChart(Composite parent) {
	dataSet = new DefaultCategoryDataset();
	chart = createChart(dataSet);
	// updateChart();

	chartComposite = new ChartComposite(parent, SWT.CENTER, chart, true);
	chartComposite.setSize(850, 350);
    }

    private void updateChart() {
	// read from Input-Fields
	Calendar c = new GregorianCalendar();
	c.clear();
	c.set(this.dtZeitraum.getYear(), this.dtZeitraum.getMonth(),
		this.dtZeitraum.getDay());

	String kategory = txtKategorie.getText().trim();

	// Call Method to get Top Ten
	//TODO
	topten = platzDao.findTopTen(c, kategory);
	

	chart.clearSubtitles();

	String subtitle = "";
	switch (dtZeitraum.getMonth()) {
	case 0:
	    subtitle = "Jänner ";
	    break;
	case 1:
	    subtitle = "Februar ";
	    break;
	case 2:
	    subtitle = "März ";
	    break;
	case 3:
	    subtitle = "April ";
	    break;
	case 4:
	    subtitle = "Mai ";
	    break;
	case 5:
	    subtitle = "Juni ";
	    break;
	case 6:
	    subtitle = "Juli ";
	    break;
	case 7:
	    subtitle = "August ";
	    break;
	case 8:
	    subtitle = "September ";
	    break;
	case 9:
	    subtitle = "Oktober ";
	    break;
	case 10:
	    subtitle = "November ";
	    break;
	case 11:
	    subtitle = "Dezember ";
	    break;
	}
	subtitle += dtZeitraum.getYear();
	chart.addSubtitle(new TextTitle(subtitle));

	if (!txtKategorie.getText().trim().isEmpty()) {
	    subtitle = "Kategorie: ";
	    subtitle += txtKategorie.getText().trim();
	    chart.addSubtitle(new TextTitle(subtitle));
	}

	dataSet.clear();
	updateDataset(dataSet);

	tableViewer.setInput(new Object());
    }

    private void updateDataset(DefaultCategoryDataset dataset) {
	// DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	// row keys...
	String series1 = "Veranstaltung";

	for (TopTen top : topten) {
	    System.out.println(top.toString());
	    if (top.getVeranstaltung().getBezeichnung() == null) {
		dataset.addValue(top.getGebuchtePlaetze(), series1, "Unbekannt");
	    } else {
		dataset.addValue(top.getGebuchtePlaetze(), series1, top
			.getVeranstaltung().getBezeichnung());
	    }

	}

	// column keys...
	// String category1 = "Label 1";
	// String category2 = "Label 2";
	// String category3 = "Label 3";
	// String category4 = "Label 4";
	// String category5 = "Label 5";
	// String category6 = "Label 6";
	// String category7 = "Label 7";
	// String category8 = "Label 8";
	// String category9 = "Label 9";
	// String category10 = "Label 10";
	//
	// dataset.addValue(10, series1, category1);
	// dataset.addValue(4, series1, category2);
	// dataset.addValue(3, series1, category3);
	// dataset.addValue(2, series1, category4);
	// dataset.addValue(1, series1, category5);
	// dataset.addValue(1, series1, category6);
	// dataset.addValue(1, series1, category7);
	// dataset.addValue(0, series1, category8);
	// dataset.addValue(0, series1, category9);
	// dataset.addValue(0, series1, category10);
    }

    private JFreeChart createChart(CategoryDataset dataset) {
	JFreeChart chart = ChartFactory.createBarChart("Top Ten Verkäufe", // chart
		// title
		"Veranstaltungen", // domain axis label
		"Anzahl verkaufter Tickets", // range axis label
		dataset, // data
		PlotOrientation.VERTICAL, // orientation
		false, // include legend
		true, // tooltips?
		false // URLs?
		);

	CategoryPlot plot = (CategoryPlot) chart.getPlot();

	// CategoryAxis axis = plot.getDomainAxis();

	// plot.setBackgroundPaint();
	// plot.setDomainGridlinePaint(new Color(null, 255, 0, 0));
	// plot.setDomainGridlinesVisible(true);
	// plot.setRangeGridlinePaint(Color.white);

	return chart;

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
		TopTenEditor.this.openAuffuehrungVonVeranstaltungEditor();
	    }
	});
	this.tableViewer
		.addSelectionChangedListener(new ISelectionChangedListener() {

		    @Override
		    public void selectionChanged(SelectionChangedEvent event) {
			// Nur Suche starten, wenn auch etwas ausgewählt ist:
			if (!event.getSelection().isEmpty()) {
			    TopTenEditor.this
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

	tableViewer.setInput(new Object());

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

    public void searchHandler() {
	// TODO
	log.debug("searchHandler called for Top Ten");
	return;
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
	return;
    }

    @Override
    public void doSaveAs() {
	return;
    }

    @Override
    public void setFocus() {
	 if(c != null) // Context sensitive help
	      c.setFocus(); // context sensitive help
    }

    @Override
    public boolean isDirty() {
	return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
	return false;
    }

    class VeranstaltungContentProvider implements IStructuredContentProvider {
	// private VeranstaltungService veranstaltungService = null;

	@Override
	public Object[] getElements(Object query) {
	    // Aus der Klassenvariable die liste retournieren
	    List<Veranstaltung> list = new ArrayList<Veranstaltung>();
	    for(TopTen top : topten) {
		list.add(top.getVeranstaltung());
	    }

	    return list.toArray();
	    // return new Object[0];
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

    }

}
