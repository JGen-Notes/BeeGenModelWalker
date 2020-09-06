package eu.jgen.beegen.model.walker.parts;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;

import eu.jgen.beegen.model.api.JGenObject;
import eu.jgen.beegen.model.mirror.decaration.ActionBlock;
import eu.jgen.beegen.model.mirror.tests.BasicFormater;
import eu.jgen.beegen.model.mirror.tests.BasicFormater2;
import eu.jgen.beegen.model.mirror.tests.MapActionBlock;
import eu.jgen.beegen.model.mirror.tests.TreeDumper2;
import eu.jgen.beegen.model.mirror.worker.BeeGenModelMirrorCreator;
import eu.jgen.beegen.model.mirror.worker.TreeDumper;


public class GenObjectMirroring {
	
	@Inject
	private ESelectionService selectionService;
	Browser browser;
	
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		browser = new Browser(parent, SWT.NONE);
		//browser.setUrl("file:///Users/Marek/beegen01.ief/bee/reports/hello.html");
		browser.setText("Mirror");
	// System.out.println(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
	}
	
	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) JGenObject genObject) {
		
		Bundle plugin = Platform.getBundle ("eu.jgen.beegen.model.walker");
		URL url = plugin.getEntry ("/");
		File file = null;
		try {
			// Resolve the URL
			URL resolvedURL = Platform.resolve (url);
			file = new File (resolvedURL.getFile ());
//			for (File a : file.listFiles()) {
//				System.out.println(a);
//			}
			} catch (Exception e) {
			// Something sensible if an error occurs
			}
		
		
		String path = Platform.getLocation().toString();
		//System.out.println(path);
		
		if (genObject != null) {
			
			if (genObject.objMnemonic.equals("ACBLKBSD")) {
				BeeGenModelMirrorCreator actionBlockMapping = new BeeGenModelMirrorCreator();
				ActionBlock actionBlock = actionBlockMapping
						.transformActionBlock(genObject);
				actionBlock.accept(new BasicFormater());
				if (actionBlock != null) {
					try {
						generateSourceCode(actionBlock);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
 			StructuredSelection selection = new StructuredSelection(genObject);
 			selectionService.setSelection(selection);
		}
	}
	
	private void generateSourceCode(ActionBlock actionBlock)
			throws FileNotFoundException {
		BasicFormater2 treeDumper = new BasicFormater2();
		actionBlock.accept(new BasicFormater());
	 	actionBlock.accept(treeDumper);
	 	System.out.println(treeDumper.getText());
	 	browser.setText(treeDumper.getText());
	}

}
