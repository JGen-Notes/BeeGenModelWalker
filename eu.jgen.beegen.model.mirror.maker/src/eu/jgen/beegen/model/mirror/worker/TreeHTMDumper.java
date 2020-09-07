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
package eu.jgen.beegen.model.mirror.worker;

import eu.jgen.beegen.model.mirror.decaration.AttributeView;
import eu.jgen.beegen.model.mirror.decaration.Clause;
import eu.jgen.beegen.model.mirror.decaration.EntityActions;
import eu.jgen.beegen.model.mirror.decaration.EventDeclaration;
import eu.jgen.beegen.model.mirror.decaration.ExportViews;
import eu.jgen.beegen.model.mirror.decaration.GroupView;
import eu.jgen.beegen.model.mirror.decaration.ImportViews;
import eu.jgen.beegen.model.mirror.decaration.LocalViews;
import eu.jgen.beegen.model.mirror.decaration.MainDeclaration;
import eu.jgen.beegen.model.mirror.decaration.SimpleView;
import eu.jgen.beegen.model.mirror.decaration.Statement;
import eu.jgen.beegen.model.mirror.decaration.StatementsGroup;
import eu.jgen.beegen.model.mirror.decaration.ViewsDeclaration;
import eu.jgen.beegen.model.mirror.declaration.annotated.IActionBlock;
import eu.jgen.beegen.model.mirror.declaration.annotated.IActionBlockBody;

public class TreeHTMDumper extends GenericHTMLFormater {

	@Override
	public void visit(ViewsDeclaration viewsDeclaration) {
		indent(1);
		out(viewsDeclaration);
		super.visit(viewsDeclaration);
		outdent(1);
	}

	@Override
	public void visit(MainDeclaration mainDeclaration) {
		indent(1);
		out(mainDeclaration);
		super.visit(mainDeclaration);
		outdent(1);
	}

	@Override
	public void visit(EventDeclaration eventDeclaration) {
		out(eventDeclaration);
		super.visit(eventDeclaration);
	}

	@Override
	public void visit(AttributeView attribute) {
		indent(1);
		out(attribute);
		super.visit(attribute);
		outdent(1);
	}

	@Override
	public void visit(SimpleView simpleView) {
		indent(1);
		out(simpleView);
		super.visit(simpleView);
		outdent(1);
	}

	@Override
	public void visit(GroupView groupView) {
		indent(1);
		out(groupView);
		//System.out.println(space() + groupView);
		super.visit(groupView);
		outdent(1);
	}

	@Override
	public void visit(ImportViews importViews) {
		indent(1);
		out(importViews);
		super.visit(importViews);
		outdent(1);
	}

	@Override
	public void visit(ExportViews exportViews) {
		indent(1);
		out(exportViews);
		super.visit(exportViews);
		outdent(1);
	}

	@Override
	public void visit(LocalViews localViews) {
		indent(1);
		out(localViews);
		super.visit(localViews);
		outdent(1);
	}

	@Override
	public void visit(EntityActions entityActions) {
		indent(1);
		out(entityActions);
		super.visit(entityActions);
		outdent(1);
	}

	@Override
	public void visit(IActionBlock actionBlock) {
		out(actionBlock);
		super.visit(actionBlock);
	}

	@Override
	public void visit(IActionBlockBody actionBlockBody) {
		indent(1);
		out("{");
		super.visit(actionBlockBody);
		out("}");
		outdent(1);
	}

	@Override
	public void visit(StatementsGroup statementsGroup) {
		indent(1);
		out("{");
		super.visit(statementsGroup);
		out("}");
		outdent(1);
	}

	@Override
	public void visit(Statement statement) {
		indent(1);
 		out(statement);
		super.visit(statement);
		outdent(1);
	}

	public TreeHTMDumper(String baseref) {
		super(baseref);
	}

	@Override
	public void visit(Clause clause) {
		indent(1);
		out(clause);
		super.visit(clause);
		outdent(1);
	}

}
