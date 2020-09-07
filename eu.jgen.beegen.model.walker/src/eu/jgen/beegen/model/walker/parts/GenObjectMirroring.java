package eu.jgen.beegen.model.walker.parts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import eu.jgen.beegen.model.api.JGenContainer;
import eu.jgen.beegen.model.api.JGenModel;
import eu.jgen.beegen.model.api.JGenObject;
import eu.jgen.beegen.model.mirror.decaration.ActionBlock;
import eu.jgen.beegen.model.mirror.worker.BeeGenModelMirrorCreator;
import eu.jgen.beegen.model.mirror.worker.TreeHTMDumper;

public class GenObjectMirroring {

	@Inject
	private ESelectionService selectionService;
	Browser browser;
	private JGenContainer genContainer;
	@SuppressWarnings("unused")
	private JGenModel genModel;
	private String pathReportsDirectory;

	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		browser = new Browser(parent, SWT.NONE);
		browser.setText("");
		
		selectionService.addSelectionListener(new ISelectionListener() {
			@Override
			public void selectionChanged(MPart part, Object object) {
				if (object instanceof JGenContainer) {
					genContainer = (JGenContainer) object;
					genModel = genContainer.getModel();	
				}
			}
		});
	}

	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) JGenObject genObject) {
		String baseref = null;
		try {
			URL resolvedURL = FileLocator.resolve(Platform.getBundle ("eu.jgen.beegen.model.walker").getEntry ("/"));
			baseref = resolvedURL.getPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (genObject != null) {
			if (genObject.objMnemonic.equals("ACBLKBSD")) {
				BeeGenModelMirrorCreator actionBlockMapping = new BeeGenModelMirrorCreator();
				ActionBlock actionBlock = actionBlockMapping.transformActionBlock(genObject);
				if (actionBlock != null) {
					TreeHTMDumper dumper = new TreeHTMDumper(baseref);
					actionBlock.accept(dumper);
					browser.setText(dumper.toString());
				   createReportFile(String.valueOf(genObject.objId), dumper.toString());
				}
			}
		}
	}
	
	private void createReportsFolder() {
		File dir = new File(pathReportsDirectory);
		if (dir.exists() &&dir.isDirectory()) {
			return;
		}
		dir.mkdirs();
	}
	
	private void createReportFile(String name, String contents) {
		String nativeDir = genContainer.getModelLocation().substring(0, genContainer.getModelLocation().lastIndexOf(File.separator));
		pathReportsDirectory = nativeDir + "/reports";
		createReportsFolder();
		try {
			PrintWriter out = new PrintWriter(pathReportsDirectory + "/" + name + ".html");
			out.print(contents); 
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Problem persisting generated HTML in the file.");
			e.printStackTrace();
		}
	}
	
}
