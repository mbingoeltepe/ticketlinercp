package at.ticketline.kassa.ui.editor;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
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

import at.ticketline.dao.DaoException;
import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.KuenstlerDao;
import at.ticketline.entity.Engagement;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kuenstler;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;

public class KuenstlerEditor extends EditorPart implements INeedLogin {
    public static final String ID = "at.ticketline.editor.kuenstler";
    protected Logger log = LogFactory.getLogger(this.getClass());

    private KuenstlerDao kuenstlerDao = null;
    private KuenstlerEditorInput kuenstlerInput = null;
    private Kuenstler kuenstler = null;

    private FormToolkit toolkit;
    private ScrolledForm form;
    private boolean dirty = false;

    private Text txtNachname;
    private Text txtVorname;
    private Text txtTitel;
    private Combo cbGeschlecht;
    private Text txtBiographie;
    private DateTime dtGeburtsdatum;

    private Button btnSave;

    private TableViewer tableViewer;
    
    private Composite c;
   

    public KuenstlerEditor() {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input)
	    throws PartInitException {
	this.setSite(site);
	this.setInput(input);
	this.kuenstlerInput = ((KuenstlerEditorInput) input);
	this.kuenstlerDao = (KuenstlerDao) DaoFactory
		.findDaoByEntity(Kuenstler.class);
	this.kuenstler = this.kuenstlerInput.getKuenstler();
	this.setPartName(this.kuenstlerInput.getName());
//	if (this.kuenstler.getId() == null) {
//	    this.dirty = true;
//	}
	this.dirty = false;

    }

    @Override
    public void createPartControl(Composite parent) {
	parent.setLayout(new GridLayout(1, false));

	this.toolkit = new FormToolkit(parent.getDisplay());
	this.form = this.toolkit.createScrolledForm(parent);
	this.form.setLayoutData(new GridData(GridData.FILL_BOTH));
	this.form.getBody().setLayout(new GridLayout(1, false));
	this.form.setText(this.kuenstlerInput.getName());

	this.createForm(this.form.getBody());
	this.createTable(this.form.getBody());
	this.createSaveButton(this.form.getBody());

	if ((this.kuenstler.getEngagements() != null)
		&& (this.kuenstler.getEngagements().size() > 0)) {
	    this.tableViewer.setInput(this.kuenstler.getEngagements());
	}
	
	c = parent;
	PlatformUI.getWorkbench().getHelpSystem().setHelp(c, "TicketlineRCP.kuenstler"); // contextsensitive hilfe

    }

    @SuppressWarnings("unused")
    protected void createForm(Composite parent) {
	Composite c = this.toolkit.createComposite(parent);
	c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	ColumnLayout columnLayout = new ColumnLayout();
	columnLayout.minNumColumns = 2;
	columnLayout.maxNumColumns = 2;
	c.setLayout(columnLayout);

	EditorModifyListener listener = new EditorModifyListener();

	// Left Side
	Section leftSection = this.toolkit.createSection(c, Section.DESCRIPTION
		| Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
	leftSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		KuenstlerEditor.this.form.reflow(true);
	    }
	});
	leftSection.setText("Daten");
	leftSection.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));
	Composite left = this.toolkit.createComposite(leftSection);
	left.setLayout(new GridLayout(2, false));

	Label lblNachname = this.toolkit.createLabel(left, "Nachname:",
		SWT.LEFT);
	lblNachname.setSize(230, lblNachname.getSize().y);

	this.txtNachname = this.toolkit.createText(left,
		this.kuenstler.getNachname(), SWT.LEFT | SWT.BORDER);
	this.txtNachname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.txtNachname.addModifyListener(listener);

	Label lblVorname = this.toolkit.createLabel(left, "Vorname:", SWT.LEFT);

	this.txtVorname = this.toolkit.createText(left,
		this.kuenstler.getVorname(), SWT.LEFT | SWT.BORDER);
	this.txtVorname.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
		false));
	this.txtVorname.addModifyListener(listener);

	Label lblTitel = this.toolkit.createLabel(left, "Titel:", SWT.LEFT);

	this.txtTitel = this.toolkit.createText(left,
		this.kuenstler.getTitel(), SWT.LEFT | SWT.BORDER);
	this.txtTitel
		.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
	this.txtTitel.addModifyListener(listener);

	Label lblGeschlecht = this.toolkit.createLabel(left, "Geschlecht:",
		SWT.LEFT);

	this.cbGeschlecht = new Combo(left, SWT.FLAT | SWT.READ_ONLY
		| SWT.BORDER);
	this.cbGeschlecht.setItems(Geschlecht.toStringArray());
	if (this.kuenstler.getGeschlecht() == Geschlecht.WEIBLICH) {
	    this.cbGeschlecht.select(1);
	} else {
	    this.cbGeschlecht.select(0);
	}
	this.cbGeschlecht.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.cbGeschlecht.addModifyListener(listener);
	this.toolkit.adapt(this.cbGeschlecht, true, true);

	Label lblGeburtsdatum = this.toolkit.createLabel(left, "Geburtsdatum:",
		SWT.LEFT);

	this.dtGeburtsdatum = new DateTime(left, SWT.DROP_DOWN | SWT.BORDER);
	this.dtGeburtsdatum
		.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	this.dtGeburtsdatum.addFocusListener(listener);
	this.toolkit.adapt(this.dtGeburtsdatum, true, true);
	if (this.kuenstler.getGeburtsdatum() != null) {
	    GregorianCalendar gc = this.kuenstler.getGeburtsdatum();
	    this.dtGeburtsdatum.setYear(gc.get(Calendar.YEAR));
	    this.dtGeburtsdatum.setMonth(gc.get(Calendar.MONTH));
	    this.dtGeburtsdatum.setDay(gc.get(Calendar.DAY_OF_MONTH));
	}

	leftSection.setClient(left);

	// Right Side
	Section rightSection = this.toolkit.createSection(c,
		Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
			| Section.EXPANDED);
	rightSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		KuenstlerEditor.this.form.reflow(true);
	    }
	});
	rightSection.setText("Biographie");
	rightSection.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));
	Composite right = this.toolkit.createComposite(rightSection);
	right.setLayout(new GridLayout(1, false));

	this.txtBiographie = this.toolkit.createText(right,
		this.kuenstler.getBiographie(), SWT.MULTI | SWT.BORDER
			| SWT.WRAP);
	this.txtBiographie.setLayoutData(new GridData(GridData.FILL_BOTH));
	this.txtBiographie.addModifyListener(listener);
	rightSection.setClient(right);
    }

    protected void createTable(Composite parent) {

	Section engagementSection = this.toolkit.createSection(parent,
		Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
			| Section.EXPANDED);
	engagementSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		KuenstlerEditor.this.form.reflow(true);
	    }
	});
	engagementSection.setText("Engagements");
	engagementSection.setLayoutData(new GridData(GridData.FILL_BOTH));

	this.tableViewer = new TableViewer(engagementSection, SWT.BORDER
		| SWT.FULL_SELECTION);
	this.tableViewer.getTable().setLayoutData(
		new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	TableLayout layout = new TableLayout();
	layout.addColumnData(new ColumnWeightData(28, 100, true));
	layout.addColumnData(new ColumnWeightData(28, 100, true));
	layout.addColumnData(new ColumnWeightData(28, 100, true));
	layout.addColumnData(new ColumnWeightData(15, 100, true));
	this.tableViewer.getTable().setLayout(layout);

	this.tableViewer.getTable().setLinesVisible(true);
	this.tableViewer.getTable().setHeaderVisible(true);

	this.tableViewer.setContentProvider(new ArrayContentProvider());
	this.tableViewer.setLabelProvider(new ITableLabelProvider() {

	    @Override
	    public Image getColumnImage(Object arg0, int arg1) {
		return null;
	    }

	    @Override
	    public String getColumnText(Object element, int index) {
		Engagement e = (Engagement) element;
		switch (index) {
		case 0:
		    if (e.getVeranstaltung() != null) {
			return e.getVeranstaltung().getBezeichnung();
		    } else {
			return "";
		    }
		case 1:
		    if (e.getVeranstaltung() != null) {
			return e.getVeranstaltung().getKategorie();
		    } else {
			return "";
		    }
		case 2:
		    if (e.getFunktion() == null) {
			return "";
		    } else {
			return e.getFunktion();
		    }
		case 3:
		    if (e.getGage() == null) {
			return "";
		    } else {
			return e.getGage().toString();
		    }
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

	TableColumn colVeranstaltung = new TableColumn(
		this.tableViewer.getTable(), SWT.LEFT);
	colVeranstaltung.setText("Veranstaltung");
	TableColumn colKategorie = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colKategorie.setText("Kategorie");
	TableColumn colFunktion = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colFunktion.setText("Funktion");
	TableColumn colGage = new TableColumn(this.tableViewer.getTable(),
		SWT.LEFT);
	colGage.setText("Gage");

	this.toolkit.adapt(this.tableViewer.getTable(), true, true);
	engagementSection.setClient(this.tableViewer.getTable());
    }

    private void createSaveButton(Composite parent) {
	this.btnSave = new Button(parent, SWT.PUSH);
	this.btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
		false));
	this.btnSave.setText(" Speichern ");
	this.btnSave.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		if (KuenstlerEditor.this.dirty == false) {
		    return;
		}
		IHandlerService handlerService = (IHandlerService) KuenstlerEditor.this
			.getSite().getService(IHandlerService.class);
		try {
		    handlerService.executeCommand("org.eclipse.ui.file.save",
			    null);
		} catch (Exception ex) {
		    KuenstlerEditor.this.log.error(ex);

		    MessageDialog.openError(
			    KuenstlerEditor.this.getSite().getWorkbenchWindow()
				    .getShell(),
			    "Error",
			    "Künstler kann nicht gespeichert werden: "
				    + ex.getMessage());
		}
	    }
	});

    }

    @Override
    public void setFocus() {
	txtNachname.setFocus();
	txtNachname.setSelection(0, txtNachname.getText().length());
	
	 if(c != null) // Context sensitive help
	      c.setFocus(); // context sensitive help
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
	this.log.info("Speichern");
	this.kuenstler.setNachname(this.txtNachname.getText());
	this.kuenstler.setVorname(this.txtVorname.getText());
	this.kuenstler.setTitel(this.txtTitel.getText());
	this.kuenstler.setGeschlecht(Geschlecht.getValueOf(this.cbGeschlecht
		.getText()));
	if (this.kuenstler.getGeburtsdatum() == null) {
	    this.kuenstler.setGeburtsdatum(new GregorianCalendar());
	}
	this.kuenstler.getGeburtsdatum().set(Calendar.YEAR,
		this.dtGeburtsdatum.getYear());
	this.kuenstler.getGeburtsdatum().set(Calendar.MONTH,
		this.dtGeburtsdatum.getMonth());
	this.kuenstler.getGeburtsdatum().set(Calendar.DAY_OF_MONTH,
		this.dtGeburtsdatum.getDay());
	this.kuenstler.setBiographie(this.txtBiographie.getText());

	try {
	    if (this.kuenstler.getId() == null) {
		this.kuenstlerDao.persist(this.kuenstler);
	    } else {
		this.kuenstler = this.kuenstlerDao.merge(this.kuenstler);
	    }
	    this.dirty = false;

	    // Benachrichtigen der Workbench, dass sich der Status geaendert hat
	    KuenstlerEditor.this.firePropertyChange(IEditorPart.PROP_DIRTY);
	} catch (ConstraintViolationException c) {
	    StringBuilder sb = new StringBuilder(
		    "Die eingegebene Daten weisen folgende Fehler auf:\n");
	    for (ConstraintViolation<?> cv : c.getConstraintViolations()) {
		sb.append(cv.getPropertyPath().toString().toUpperCase())
			.append(" ").append(cv.getMessage());
	    }
	    MessageDialog.openError(this.getSite().getWorkbenchWindow()
		    .getShell(), "Error", sb.toString());

	} catch (DaoException e) {
	    this.log.error(e);
	    e.printStackTrace();
	    MessageDialog.openError(
		    this.getSite().getWorkbenchWindow().getShell(),
		    "Error",
		    "Künstler konnte nicht gespeichert werden: "
			    + e.getMessage());

	}
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

    public void updateTitle() {
	this.setPartName(this.txtVorname.getText() + " "
		+ this.txtNachname.getText());
	this.form.setText(this.txtVorname.getText() + " "
		+ this.txtNachname.getText());
    }

    class EditorModifyListener implements ModifyListener, FocusListener {

	@Override
	public void modifyText(ModifyEvent e) {
	    if ((e.getSource().equals(KuenstlerEditor.this.txtNachname))
		    || (e.getSource().equals(KuenstlerEditor.this.txtVorname))) {
		KuenstlerEditor.this.updateTitle();
	    }
	    KuenstlerEditor.this.dirty = true;

	    // Benachrichtigen der Workbench, dass sich der Status geaendert hat
	    KuenstlerEditor.this.firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public void focusGained(FocusEvent e) {
	    // nothing to do
	}

	@Override
	public void focusLost(FocusEvent e) {
	    if (e.getSource().equals(KuenstlerEditor.this.dtGeburtsdatum) == false) {
		return;
	    }
	    try {
		GregorianCalendar gc = KuenstlerEditor.this.kuenstler
			.getGeburtsdatum();
		if (gc == null) {
		    KuenstlerEditor.this.dirty = true;
		    KuenstlerEditor.this
			    .firePropertyChange(IEditorPart.PROP_DIRTY);
		    return;
		}
		if ((KuenstlerEditor.this.dtGeburtsdatum.getYear() != gc
			.get(Calendar.YEAR))
			|| (KuenstlerEditor.this.dtGeburtsdatum.getMonth() != gc
				.get(Calendar.MONTH))
			|| (KuenstlerEditor.this.dtGeburtsdatum.getDay() != gc
				.get(Calendar.DAY_OF_MONTH))) {
		    KuenstlerEditor.this.dirty = true;
		    KuenstlerEditor.this
			    .firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	    } catch (Exception ex) {
		KuenstlerEditor.this.log.error(ex);
	    }

	}
    }
}
