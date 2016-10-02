package at.ticketline.kassa.ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.kassa.ui.sitzplan.SitzplanImpl;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;

public class SitzplanAnzeigeEditor extends EditorPart {
    public static final String ID = "at.ticketline.editor.sitzplanAnzeige";
    protected Logger log = LogFactory.getLogger(this.getClass());

    // Daten für Aufführung
    private Auffuehrung auffuehrung = null;

    private FormToolkit toolkit;
    private ScrolledForm form;
    
    // Sitzplan
    SitzplanImpl plan;
    Label[][] sitzplan;

    public SitzplanAnzeigeEditor() {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input)
	    throws PartInitException {
	this.setSite(site);
	log.debug("Sitzplan Anzeige EDITOR");
	this.setInput(input);

	// this.auffuehrung = Activator.getDefault().getAuffuehrung();
	this.auffuehrung = ((SitzplanAnzeigeEditorInput) input)
		.getAuffuehrung();

	if (this.auffuehrung.getVeranstaltung() == null) {
	    this.setPartName("Sitzplan");
	} else {
	    this.setPartName("Sitzplan für "
		    + this.auffuehrung.getVeranstaltung().getBezeichnung());
	}
    }

    @Override
    public void createPartControl(Composite parent) {
	parent.setLayout(new GridLayout(1, false));
	
	this.toolkit = new FormToolkit(parent.getDisplay());
	this.form = this.toolkit.createScrolledForm(parent);
	this.form.setLayoutData(new GridData(GridData.FILL_BOTH));
	this.form.getBody().setLayout(new GridLayout(1, false));

	if (this.auffuehrung.getVeranstaltung() == null) {
	    this.form.setText("Sitzplan");
	} else {
	    this.form.setText("Sitzplan für "
		    + this.auffuehrung.getVeranstaltung().getBezeichnung());
	}

	this.createForm(this.form.getBody());
	// this.createButton(this.form.getBody());

    }

    protected void createForm(Composite parent) {

	plan = new SitzplanImpl(this.auffuehrung);
	
	Composite c = this.toolkit.createComposite(parent);
	c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	ColumnLayout columnLayout = new ColumnLayout();
	columnLayout.minNumColumns = 2;
	columnLayout.maxNumColumns = 2;
	c.setLayout(columnLayout);

	// Left - Daten
	Section leftSection = this.toolkit.createSection(c, Section.DESCRIPTION
		| Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
	leftSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		// TicketVerkaufEditor.this.form.reflow(true);
	    }
	});
	leftSection.setText("Daten");
	leftSection.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));

	Composite left = this.toolkit.createComposite(leftSection);
	left.setLayout(new GridLayout(2, false));

	// Die eingerahmten Informationen zur Aufführung
	Group auffuehrungInfo = new Group(left, SWT.BORDER_SOLID);
	auffuehrungInfo.setText("Aufführungsdaten");
	auffuehrungInfo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true,
		false, 2, 1));
	auffuehrungInfo.setLayout(new GridLayout(2, false));
	Label lblVeranstaltung = new Label(auffuehrungInfo, SWT.LEFT);
	lblVeranstaltung.setText("Veranstaltung: ");
	Label lblVeranstaltungData = new Label(auffuehrungInfo, SWT.LEFT);
	if (this.auffuehrung.getVeranstaltung() != null) {
	    lblVeranstaltungData.setText(this.auffuehrung.getVeranstaltung()
		    .getBezeichnung());
	} else {
	    lblVeranstaltungData.setText("k.A.");
	}

	Label lblDatum = new Label(auffuehrungInfo, SWT.LEFT);
	lblDatum.setText("Datum/Uhrzeit: ");
	Label lblDatumData = new Label(auffuehrungInfo, SWT.LEFT);
	lblDatumData.setText(this.auffuehrung.getDatumuhrzeit().toString());

	Label lblSaal = new Label(auffuehrungInfo, SWT.LEFT);
	lblSaal.setText("Saal: ");
	Label lblSaalData = new Label(auffuehrungInfo, SWT.LEFT);
	if (this.auffuehrung.getSaal() != null) {
	    lblSaalData.setText(this.auffuehrung.getSaal().getBezeichnung());
	} else {
	    lblSaalData.setText("k.A.");
	}
	
	
	Group legendeInfo = new Group(left, SWT.BORDER_SOLID);
	legendeInfo.setText("Legende");
	legendeInfo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true,
		false, 2, 1));
	legendeInfo.setLayout(new GridLayout(2, false));
	plan.getLegende(legendeInfo);

	leftSection.setClient(left);

	// Right --> Sitzplan
	Section rightSection = this.toolkit.createSection(c,
		Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
			| Section.EXPANDED);
	rightSection.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		SitzplanAnzeigeEditor.this.form.reflow(true);
	    }
	});
	rightSection.setText("Sitzplan");
	rightSection
		.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));
	Composite right = this.toolkit.createComposite(rightSection);

	

	right.setLayout(new GridLayout(plan.getGridSize(right), false));

	// Sitzplan wird gezeichnet:
	sitzplan = plan.getSitzplan(right, false, null);
	

	
	rightSection.setClient(right);

	// workaround damit scrollbar angezeigt wird
	leftSection.setExpanded(false);
	leftSection.setExpanded(true);
    }
    
    public void refreshSitzplan() {
	log.info("Refreshing Sitzplan... ");
	plan.refreshSitzplan();
	//sitzplan[0][0].setBackground(new Color(null, 0, 0, 0));
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

    @Override
    public boolean isDirty() {
	return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
	return false;
    }
}
