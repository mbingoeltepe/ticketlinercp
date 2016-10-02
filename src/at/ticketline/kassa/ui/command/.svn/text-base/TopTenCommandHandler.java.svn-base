package at.ticketline.kassa.ui.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import at.ticketline.kassa.ui.editor.TopTenEditor;
import at.ticketline.kassa.ui.editor.TopTenEditorInput;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;

public class TopTenCommandHandler extends AbstractHandler implements IHandler {

    public static final String ID = "at.ticketline.kassa.ui.command.topten";
    protected Logger log = LogFactory.getLogger(this.getClass());
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
	
	this.log.info(this.getClass().getSimpleName() + " called");
	IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
	IWorkbenchPage page = window.getActivePage();
	
	// evtl Input hier
	TopTenEditorInput input = new TopTenEditorInput();
	
	try {
	    page.openEditor(input, TopTenEditor.ID);
	} catch (PartInitException e) {
	    MessageDialog.openError(
		    window.getShell(),
		    "Error",
		    "Top Ten koennen nicht geoeffnet werden: "
			    + e.getMessage());
	    this.log.error(e);
	}
	return null;
    }

}
