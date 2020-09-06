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
package eu.jgen.beegen.model.mirror.decaration;

import eu.jgen.beegen.model.mirror.visitor.Visitor;

public class ViewsDeclaration extends Node  {

	private static final long serialVersionUID = 1L;
	
	private ImportViews importViews;

	private LocalViews localViews;

	private ExportViews exportViews;

	private EntityActions entityActions;

	public ViewsDeclaration(ImportViews importViews, LocalViews localViews,
			ExportViews exportViews, EntityActions entityActions) {
		super();
		this.importViews = importViews;
		this.localViews = localViews;
		this.exportViews = exportViews;
		this.entityActions = entityActions;
	}
	
	public ViewsDeclaration() {
	}

	public EntityActions getEntityTypes() {
		return entityActions;
	}
	
	public ExportViews getExportViews() {
		return exportViews;
	}
	
	public ImportViews getImportViews() {
		return importViews;
	}

	public LocalViews getLocalViews() {
		return localViews;
	}

	public void setEntityTypes(EntityActions entityActions) {
		this.entityActions = entityActions;
	}

	public void setExportViews(ExportViews exportViews) {
		this.exportViews = exportViews;
	}

	public void setImportViews(ImportViews importViews) {
		this.importViews = importViews;
	}

	public void setLocalViews(LocalViews localViews) {
		this.localViews = localViews;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
}
