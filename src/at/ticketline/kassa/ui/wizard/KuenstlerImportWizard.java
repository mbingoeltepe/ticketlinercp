package at.ticketline.kassa.ui.wizard;

import java.io.FileReader;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressConstants;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.dao.interfaces.KuenstlerDao;
import at.ticketline.entity.Kuenstler;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class KuenstlerImportWizard extends Wizard implements IImportWizard {
    protected Logger log = LogFactory.getLogger(this.getClass());
    protected FileSelectionWizardPage fileSelection = null;

    public KuenstlerImportWizard() {
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
	this.setWindowTitle("Künstler importieren");
	this.fileSelection = new FileSelectionWizardPage(
		"Import-Datei auswählen");
	this.fileSelection.setTitle("XML-Import");
	this.fileSelection
		.setDescription("Wählen Sie bitte die XML-Datei für den Import");
	this.fileSelection.setFileExtensions(new String[] { "*.xml", "*.*" });
	this.addPage(this.fileSelection);
    }

    @Override
    public boolean performFinish() {
	String fileName = this.fileSelection.getFileName();
	this.log.info("Importing Kuenstler from file #0", fileName);
	KuenstlerImportJob job = new KuenstlerImportJob("Künstler importieren");
	job.setFileName(fileName);
	job.schedule();
	return true;
    }

    @Override
    public boolean isHelpAvailable() {
	return false;
    }

    class KuenstlerImportJob extends Job {
	protected Logger log = LogFactory.getLogger(KuenstlerImportJob.class);
	private String fileName = null;

	public KuenstlerImportJob(String name) {
	    super(name);
	    this.setUser(true);
	    this.setProperty(IProgressConstants.KEEP_PROPERTY, Boolean.TRUE);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IStatus run(IProgressMonitor monitor) {
	    monitor.beginTask("Importiere Datei " + this.fileName, 100);
	    this.log.info("Importing file " + this.fileName);
	    final List<Kuenstler> kuenstler;
	    try {
		XStream xstream = new XStream(new DomDriver());
		kuenstler = (List<Kuenstler>) xstream.fromXML(new FileReader(
			this.fileName));
	    } catch (Exception e) {
		this.log.error(e);
		throw new RuntimeException(e);
	    }
	    this.log.info("Found " + kuenstler.size() + " Kuenstler");
	    monitor.worked(10);
	    KuenstlerDao kuenstlerDao = (KuenstlerDao) DaoFactory
		    .findDaoByEntity(Kuenstler.class);
	    int workSize = Math.round(100 / kuenstler.size());
	    EntityManagerUtil.beginTransaction();
	    try {
		for (Kuenstler k : kuenstler) {
		    kuenstlerDao.persist(k);
		    monitor.worked(workSize);
		    if (monitor.isCanceled()) {
			EntityManagerUtil.rollbackTransaction();
			return Status.CANCEL_STATUS;
		    }
		    // Thread.sleep(1000); // slow down import to show progress
		    // dialog
		}
	    } catch (Exception e) {
		this.log.error(e);
		EntityManagerUtil.rollbackTransaction();
		throw new RuntimeException(e);
	    }
	    EntityManagerUtil.commitTransaction();
	    monitor.done();
	    this.log.info("Kuenstler-Import Job done");
	    PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
		public void run() {
		    MessageDialog.openInformation(PlatformUI.getWorkbench()
			    .getActiveWorkbenchWindow().getShell(),
			    "Information", "Es wurden " + kuenstler.size()
				    + " Künstler importiert");
		}
	    });

	    return Status.OK_STATUS;
	}

	public String getFileName() {
	    return this.fileName;
	}

	public void setFileName(String fileName) {
	    this.fileName = fileName;
	}

    }

}
