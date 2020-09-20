/**
 * [The "BSD license"]
 * Copyright (c) 2016, JGen Notes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions 
 *    and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS 
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package eu.jgen.beegen.model.mirror.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.eclipse.swt.internal.cocoa.OS;

import eu.jgen.beegen.model.api.JGenContainer;
import eu.jgen.beegen.model.api.JGenModel;
import eu.jgen.beegen.model.api.JGenObject;
import eu.jgen.beegen.model.meta.ObjMetaType;
import eu.jgen.beegen.model.meta.PrpMetaType;
import eu.jgen.beegen.model.mirror.decaration.ActionBlock;
import eu.jgen.beegen.model.mirror.worker.BeeGenModelMirrorCreator;
import eu.jgen.beegen.model.mirror.worker.TreeHTMDumper;

public class MapActionBlockHTMLUtility {

	private JGenContainer genContainer;

	private JGenModel genModel;
	
	private String pathReportsDirectory;
	
	private String baseref = "file:///Users/Marek/git/BeeGenModelWalker/eu.jgen.beegen.model.walker/";

	public static void main(String[] args) {

		MapActionBlockHTMLUtility map = new MapActionBlockHTMLUtility();
		try {
			System.out.println("Mapping Action Block as HTML, Version 0.2");
			System.out.println("Starting...");
			map.start(args[0], args[1]);
			System.out.println("Finished.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}

	private void start(String modelPath, String actionBlockName)
			throws FileNotFoundException {
		
		genContainer = new JGenContainer();
		genModel = genContainer.connect(modelPath);
		String nativeDir = genContainer.getContainerLocation().substring(0, genContainer.getContainerLocation().lastIndexOf(File.separator));
		pathReportsDirectory = nativeDir + "/reports";
		System.out.println(nativeDir);
		createReportsFolder();		
		List<JGenObject> list  = genModel.findNamedObjects(ObjMetaType.ACBLKBSD, PrpMetaType.NAME,
				actionBlockName);
		JGenObject acblkbsd = list.get(0);
		transform(acblkbsd);
	}

	private void transform(JGenObject acblk) throws FileNotFoundException {
		BeeGenModelMirrorCreator actionBlockMapping = new BeeGenModelMirrorCreator();
		ActionBlock actionBlock = actionBlockMapping
				.transformActionBlock(acblk);
		if (actionBlock != null) {
			TreeHTMDumper dumper = new TreeHTMDumper(baseref); 
			actionBlock.accept(dumper);
			System.out.println(dumper);
			createReportFile(String.valueOf(acblk.objId), dumper.toString());
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
