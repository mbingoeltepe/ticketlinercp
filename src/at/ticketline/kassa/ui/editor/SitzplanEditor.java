package at.ticketline.kassa.ui.editor;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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
import at.ticketline.entity.Reihe;
import at.ticketline.entity.Saal;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.kassa.ui.sitzplan.SitzplanImpl;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;

public class SitzplanEditor extends EditorPart implements INeedLogin {
    public final static String ID = "at.ticketline.editor.sitzplan";
    protected Logger log = LogFactory.getLogger(KuenstlerEditor.class);

    @SuppressWarnings("unused")
    private SitzplanEditorInput sitzplanInput = null;

    private FormToolkit toolkit;
    private ScrolledForm form;
    private boolean dirty = false;

    private SitzplanImpl plan;

    Button btnPlace;

    @Override
    public void doSave(IProgressMonitor monitor) {
	// TODO Auto-generated method stub

    }

    @Override
    public void doSaveAs() {
	// TODO Auto-generated method stub

    }

    @Override
    public void init(IEditorSite site, IEditorInput input)
	    throws PartInitException {
	this.setSite(site);
	this.setInput(input);
	this.sitzplanInput = ((SitzplanEditorInput) input);
//	this.dirty = true;
    }

    @Override
    public boolean isDirty() {
	return this.dirty;
    }

    @Override
    public boolean isSaveAsAllowed() {
	return false;
    }

    @Override
    public void createPartControl(Composite parent) {
	parent.setLayout(new GridLayout(1, false));

	this.toolkit = new FormToolkit(parent.getDisplay());
	this.form = this.toolkit.createScrolledForm(parent);
	this.form.setLayoutData(new GridData(GridData.FILL_BOTH));
	this.form.getBody().setLayout(new GridLayout(1, false));

	this.createForm(this.form.getBody());

    }

    @Override
    public void setFocus() {
	// TODO Auto-generated method stub

    }

    @SuppressWarnings({ "unused" })
    protected void createForm(Composite parent) {
	Composite c = this.toolkit.createComposite(parent);
	c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	ColumnLayout columnLayout = new ColumnLayout();
	columnLayout.minNumColumns = 1;
	columnLayout.maxNumColumns = 1;

	c.setLayout(columnLayout);
	Section section = this.toolkit.createSection(c, Section.DESCRIPTION
		| Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
	section.addExpansionListener(new ExpansionAdapter() {
	    @Override
	    public void expansionStateChanged(ExpansionEvent e) {
		SitzplanEditor.this.form.reflow(true);
	    }
	});
	section.setText("Sitzplan");
	section.setLayoutData(new ColumnLayoutData(ColumnLayoutData.LEFT));

	Auffuehrung auffuehrung = new Auffuehrung();
	Saal saal = new Saal();
	Reihe reihe = new Reihe();
	reihe.setBezeichnung("1. Reihe");
	reihe.setId(1);
	reihe.setSaal(saal);

	Reihe reihe1 = new Reihe();
	reihe1.setBezeichnung("2. Reihe");
	reihe1.setId(2);
	reihe1.setSaal(saal);

	Set<Reihe> reihen = new LinkedHashSet<Reihe>();

	reihen.add(reihe);
	reihen.add(reihe1);
	saal.setReihen(reihen);

	auffuehrung.setSaal(saal);
	this.plan = new SitzplanImpl(auffuehrung);
	Composite innerComp = this.toolkit.createComposite(section);
	innerComp.setLayout(new GridLayout(plan.getGridSize(innerComp), false));

	Label[][] sitze = plan.getSitzplan(innerComp, true, null);

	section.setClient(innerComp);

	Label[] lblLegende = plan.getLegende(c);
    }
}
