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
package eu.jgen.beegen.model.mirror.visitor;

import java.util.ArrayList;

import eu.jgen.beegen.model.mirror.decaration.ActionBlockBody;
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

public class Visitor {

	public void visit(IActionBlock actionBlock) {
		ActionBlockBody actionBlockBody = actionBlock.getActionBlockBody();
		actionBlockBody.accept(this);
	}

	public void visit(IActionBlockBody actionBlockBody) {
		ViewsDeclaration viewsDeclaration = actionBlockBody
				.getViewsDeclaration();
		viewsDeclaration.accept(this);
		MainDeclaration mainDeclaration = actionBlockBody.getMainDeclaration();
		mainDeclaration.accept(this);
		ArrayList<EventDeclaration> eventDeclarations = actionBlockBody
				.getEventDeclarations();
		for (EventDeclaration eventDeclaration : eventDeclarations) {
			eventDeclaration.accept(this);
		}
	}

	public void visit(ViewsDeclaration viewsDeclaration) {
		ImportViews importViews = viewsDeclaration.getImportViews();
		if (importViews != null) {
			importViews.accept(this);
		}
		LocalViews localViews = viewsDeclaration.getLocalViews();
		if (localViews != null) {
			localViews.accept(this);
		}
		ExportViews exportViews = viewsDeclaration.getExportViews();
		if (exportViews != null) {
			exportViews.accept(this);
		}
		
		EntityActions entityActions = viewsDeclaration.getEntityTypes();
		if (entityActions != null) {
			entityActions.accept(this);
		}

	}

	public void visit(MainDeclaration mainDeclaration) {
		StatementsGroup statementsGroup = mainDeclaration.getStatementsGroup();
		statementsGroup.accept(this);
	}

	public void visit(EventDeclaration eventDeclaration) {
		StatementsGroup statementsGroup = eventDeclaration.getStatementsGroup();
		if (statementsGroup != null) {
			statementsGroup.accept(this);
		}
	}

	public void visit(Statement statement) {
//		StatementsGroup statementsGroup = statement.getStatementsGroup();
//		if (statementsGroup != null) {
//			statementsGroup.accept(this);
//		}
		ArrayList<Clause> clauses = statement.getClauses();
		for (Clause clause : clauses) {
			clause.accept(this);
		}
	}

	public void visit(AttributeView attribute) {
	}

	public void visit(SimpleView simpleView) {
		ArrayList<AttributeView> attributes = simpleView.getAttribute();
		for (AttributeView attribute : attributes) {
			attribute.accept(this);
		}
	}

	public void visit(GroupView groupView) {
		ArrayList<SimpleView> simpleViews = groupView.getSimpleViews();
		for (SimpleView simpleView : simpleViews) {
			simpleView.accept(this);
		}
		ArrayList<GroupView> groupViews = groupView.getGroupViews();
		for (GroupView groupView2 : groupViews) {
			groupView2.accept(this);
		}

	}

	public void visit(ImportViews importViews) {
		ArrayList<SimpleView> simpleViews = importViews.getSimpleViews();
		for (SimpleView simpleView : simpleViews) {
			simpleView.accept(this);
		}
		ArrayList<GroupView> groupViews = importViews.getGroupViews();
		for (GroupView groupView2 : groupViews) {
			groupView2.accept(this);
		}
	}

	public void visit(ExportViews exportViews) {
		ArrayList<SimpleView> simpleViews = exportViews.getSimpleViews();
		for (SimpleView simpleView : simpleViews) {
			simpleView.accept(this);
		}
		ArrayList<GroupView> groupViews = exportViews.getGroupViews();
		for (GroupView groupView2 : groupViews) {
			groupView2.accept(this);
		}
	}

	public void visit(LocalViews localViews) {
		ArrayList<SimpleView> simpleViews = localViews.getSimpleViews();
		for (SimpleView simpleView : simpleViews) {
			simpleView.accept(this);
		}
		ArrayList<GroupView> groupViews = localViews.getGroupViews();
		for (GroupView groupView2 : groupViews) {
			groupView2.accept(this);
		}
	}

	public void visit(EntityActions entityActions) {
		ArrayList<SimpleView> simpleViews = entityActions.getSimpleViews();
		for (SimpleView simpleView : simpleViews) {
			simpleView.accept(this);
		}
	}
	
	public void visit(StatementsGroup statementsGroup) {
		ArrayList<Statement> statements = statementsGroup.getStatements();
		for (Statement statement : statements) {
			statement.accept(this);
		}
	}

	public void visit(Visitor visitor) {
		visitor.visit(this);	
	}
	
	public void visit(Clause clause) {
		StatementsGroup statementsGroup = clause.getStatementsGroup();
		if (statementsGroup != null) {
			statementsGroup.accept(this);
		}
	}

}
