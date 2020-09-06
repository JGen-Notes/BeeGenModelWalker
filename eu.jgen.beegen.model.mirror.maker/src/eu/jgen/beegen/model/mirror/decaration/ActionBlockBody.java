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

import java.util.ArrayList;

import eu.jgen.beegen.model.mirror.declaration.annotated.IActionBlockBody;
import eu.jgen.beegen.model.mirror.visitor.Visitor;

public class ActionBlockBody extends Node implements IActionBlockBody  {

	private static final long serialVersionUID = 1L;

	private ViewsDeclaration viewsDeclaration;

	private MainDeclaration mainDeclaration;

	private ArrayList<EventDeclaration> eventDeclarations;

	public ActionBlockBody() {
	}

	public ActionBlockBody(ViewsDeclaration viewsDeclaration,
			MainDeclaration mainDeclaration,
			ArrayList<EventDeclaration> eventDeclarations) {
		super();
		this.viewsDeclaration = viewsDeclaration;
		if (viewsDeclaration != null)
			viewsDeclaration.setParent(this);
		this.mainDeclaration = mainDeclaration;
		if (mainDeclaration != null)
			mainDeclaration.setParent(this);
		this.eventDeclarations = eventDeclarations;
		if (eventDeclarations != null) {
			for (EventDeclaration eventDeclaration : eventDeclarations) {
				eventDeclaration.setParent(this);
			}
		}
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public ArrayList<EventDeclaration> getEventDeclarations() {
		if (eventDeclarations == null) {
			eventDeclarations = new ArrayList<EventDeclaration>();
		}
		return eventDeclarations;
	}

	@Override
	public MainDeclaration getMainDeclaration() {
		return mainDeclaration;
	}

	@Override
	public ViewsDeclaration getViewsDeclaration() {
		return viewsDeclaration;
	}

	@Override
	public void setEventDeclarations(
			ArrayList<EventDeclaration> eventDeclarations) {
		this.eventDeclarations = eventDeclarations;
	}

	@Override
	public void setMainDeclaration(MainDeclaration mainDeclaration) {
		this.mainDeclaration = mainDeclaration;
	}

	@Override
	public void setViewsDeclaration(ViewsDeclaration viewsDeclaration) {
		this.viewsDeclaration = viewsDeclaration;
	}

}
