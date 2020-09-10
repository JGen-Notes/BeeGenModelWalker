package eu.jgen.beegen.model.walker.handlers;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import eu.jgen.beegen.model.api.JGenContainer;
import eu.jgen.beegen.model.api.JGenModel;

public class OpenHandler {
	
	private JGenContainer genContainer;
	private JGenModel genModel;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Inject
	ESelectionService selectionService;
	
	 @Execute
	   public void execute(Shell shell){
	       FileDialog dialog = new FileDialog(shell);
	       dialog.setFilterExtensions(new String[] {".db"});
	       String path = dialog.open();
	       if (path != null) {
	    	   findAndOpenModel(shell, path);
	    	   selectionService. setSelection(genContainer);	    	   
	       }	      
	   }
	 
	 private void findAndOpenModel(Composite parent, String path) {		
			genContainer = new JGenContainer();
			genModel = genContainer.connect(path);
			if (genModel == null) {
				logger.severe("Connection to the Bee Gen Model cannot be completed.");
				MessageDialog.openConfirm(parent.getShell(), "Connect Error",
						"Check logs for the possible reasons.");
				return;
			}
			logger.info("Connecting from handler to the Bee Gen Model: " + genModel.getName());		
		}
  
}
