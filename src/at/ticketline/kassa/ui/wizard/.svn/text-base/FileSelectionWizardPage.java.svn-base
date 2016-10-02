package at.ticketline.kassa.ui.wizard;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

class FileSelectionWizardPage extends WizardPage implements IWizardPage {
	protected FileDialog fileDialog = null;
	protected Text txtDatei = null;
	protected String[] fileExtensions = null;
	protected String path = null;

	protected FileSelectionWizardPage(String pageName) {
		super(pageName);
		this.setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new GridLayout(3, false));

		this.fileDialog = new FileDialog(c.getShell(), SWT.OPEN);
		this.fileDialog.setText(this.getTitle());
		if (this.fileExtensions != null) {
			this.fileDialog.setFilterExtensions(this.fileExtensions);
		}
		if (this.path != null) {
			this.fileDialog.setFilterPath(this.path);
		}

		Label lblDatei = new Label(c, SWT.LEFT);
		lblDatei.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
		lblDatei.setText("Datei:");

		this.txtDatei = new Text(c, SWT.LEFT | SWT.BORDER);
		this.txtDatei.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				true));
		this.txtDatei.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (FileSelectionWizardPage.this.txtDatei.getText().length() > 0) {
					FileSelectionWizardPage.this.setPageComplete(true);
				} else {
					FileSelectionWizardPage.this.setPageComplete(false);
				}
			}
		});

		Button btnDatei = new Button(c, SWT.PUSH);
		btnDatei.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
		btnDatei.setText("Datei wählen");
		btnDatei.setToolTipText("Datei wählen");
		btnDatei.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = FileSelectionWizardPage.this.fileDialog
						.open();
				if (fileName != null) {
					FileSelectionWizardPage.this.txtDatei.setText(fileName);
				}
			}
		});

		this.setControl(parent);
	}

	public String getFileName() {
		return this.txtDatei.getText();
	}

	public void setFileName(String fileName) {
		this.txtDatei.setText(fileName);
	}

	public String[] getFileExtensions() {
		return this.fileExtensions;
	}

	public void setFileExtensions(String[] fileExtensions) {
		this.fileExtensions = fileExtensions;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}