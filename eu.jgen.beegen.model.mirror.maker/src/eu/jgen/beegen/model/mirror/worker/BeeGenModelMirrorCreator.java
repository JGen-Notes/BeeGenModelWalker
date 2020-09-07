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

import java.util.List;

import eu.jgen.beegen.model.api.JGenObject;
import eu.jgen.beegen.model.meta.AscMetaType;
import eu.jgen.beegen.model.meta.ObjMetaType;
import eu.jgen.beegen.model.meta.PrpMetaType;
import eu.jgen.beegen.model.mirror.decaration.ActionBlock;
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
import eu.jgen.beegen.model.mirror.decaration.AggregateObjectType;
import eu.jgen.beegen.model.mirror.decaration.Views;
import eu.jgen.beegen.model.mirror.decaration.ViewsDeclaration;

public class BeeGenModelMirrorCreator {

	public static final int VT_VIEW = 0;

	public static final int VT_GROUP = 1;
	
	public static final String NT_FILTER = "FILTER";
	
	public static final String NT_HIGHLIGHT = "HIGHLIGHT";
	
	public static final String NT_UNMARK = "UNMARK";
	
	public static final String NT_UNFILTER = "UNFILTER";

	public static final String NT_UNHIGHLIGHT = "UNHIGHLIGHT";

	public BeeGenModelMirrorCreator() {
	}

	/**
	 * Method checks if any entity action views.
	 */
	@SuppressWarnings("unused")
	private boolean areAnyActionEntities(JGenObject dvset) {
		JGenObject dvgent = null;
		dvgent = dvset.findAssociationOne(AscMetaType.CNTENTS);
		if (dvgent == null) {
			return false;
		} else if (dvgent.findAssociationMany(AscMetaType.CONTAINS).size() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Method checks if any export views.
	 */
	@SuppressWarnings("unused")
	private boolean areAnyExports(JGenObject dvset) {
		JGenObject dvgout = null;
		dvgout = dvset.findAssociationOne(AscMetaType.CNTOUTS);
		if (dvgout == null) {
			return false;
		} else if (dvgout.findAssociationMany(AscMetaType.CONTAINS).size() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Method checks if any import views.
	 */
	@SuppressWarnings("unused")
	private boolean areAnyImports(JGenObject dvset) {
		JGenObject dvgin = null;
		dvgin = dvset.findAssociationOne(AscMetaType.CNTINPS);
		if (dvgin == null) {
			return false;
		} else if (dvgin.findAssociationMany(AscMetaType.CONTAINS).size() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Method checks if there are any local views.
	 */
	@SuppressWarnings("unused")
	private boolean areAnyLocals(JGenObject dvset) {
		JGenObject dvglcl = null;
		dvglcl = dvset.findAssociationOne(AscMetaType.CNTLCLS);
		if (dvglcl == null) {
			return false;
		} else if (dvglcl.findAssociationMany(AscMetaType.CONTAINS).size() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Method checks if view is simple or group view.
	 */
	private int checkViewType(JGenObject vw) {
		if (vw.getObjMetaType() == ObjMetaType.ENTVW) {
			return VT_VIEW;
		} else {
			return VT_GROUP;
		}
	}

	/**
	 * Create action block body.
	 */
	private ActionBlockBody createActionBlockBody(JGenObject acblkdef) {
		ActionBlockBody actionBlockBody = new ActionBlockBody();
		actionBlockBody.setGenObject(acblkdef);
		actionBlockBody.setType(AggregateObjectType.ACTION_BLOCK_BODY); 
		ViewsDeclaration viewsDeclaration = new ViewsDeclaration();
		viewsDeclaration.setGenObject(acblkdef.findAssociationOne(AscMetaType.GRPBY));
		viewsDeclaration.setType(AggregateObjectType.VIEWS);
		actionBlockBody.setViewsDeclaration(viewsDeclaration);

		// create views
		transformImportDeclaration(viewsDeclaration, (JGenObject) acblkdef);
		transformLocalsDeclaration(viewsDeclaration, (JGenObject) acblkdef);
		transformExportsDeclaration(viewsDeclaration, acblkdef);
		transformEntityActionsDeclaration(viewsDeclaration, acblkdef);

		// create main logic
		MainDeclaration mainDeclaration = transformMainDeclaration(acblkdef);
		mainDeclaration.setType(AggregateObjectType.MAIN);
		actionBlockBody.setMainDeclaration(mainDeclaration);

		// create all events blocks
		List<JGenObject> evnthdlr = acblkdef
				.findAssociationMany(AscMetaType.EVENTFRM);
		for (int i = 0; i < evnthdlr.size(); i++) {
			EventDeclaration eventDeclaration = transformEventDeclaration(evnthdlr
					.get(i));
			actionBlockBody.getEventDeclarations().add(eventDeclaration);
		}
		return actionBlockBody;
	}

	/**
	 * Method creates a new node representing attribute declaration.
	 */
	private void createAttributeView(SimpleView simpleView, JGenObject prdvw) {  
		AttributeView attribute = new AttributeView();
		attribute.setGenObject(prdvw);
		attribute.setType(AggregateObjectType.ATTRIBUTE_VIEW);
		attribute.setParent(simpleView);
		simpleView.getAttribute().add(attribute);
		return;
	}

	/**
	 * Method creates contained views.
	 */
	private void createContainedViewDeclarations(Views views, JGenObject dvg) {
		List<JGenObject> vw = dvg.findAssociationMany(AscMetaType.CONTAINS);
		for (int i = 0; i < vw.size(); i++) {
			createViewDeclaration(views, vw.get(i));
		}
	}

	/**
	 * Method creates node representing group of statements.
	 */
	private void createStatementsGroup(StatementsGroup statementsGroup,
			JGenObject group) {
		statementsGroup.setType(AggregateObjectType.STATEMENTS_GROUP);
		List<JGenObject> objects = group.findAssociationMany(AscMetaType.DEFNDBY);
		for (int i = 0; i < objects.size(); i++) {
			selectStatementProcessingMethod(objects.get(i), statementsGroup);
		}
	}

	/**
	 * Method creates a new simple view declaration node.
	 */
	private SimpleView createsSimpleView(JGenObject vw) {
		SimpleView simpleView = new SimpleView();
		simpleView.setGenObject(vw);
		simpleView.setType(AggregateObjectType.SIMPLE_VIEW);
		List<JGenObject> prdvw = vw.findAssociationMany(AscMetaType.DTLBYP);
		int j = 0;
		for (int i = 0; i < prdvw.size(); i++) {
			if (prdvw.get(i).getObjMetaType() == ObjMetaType.PRDVW) {
				createAttributeView(simpleView, (JGenObject) prdvw.get(i));
				j = i + 1;
				break;
			}
		}
		for (int i = j; i < prdvw.size(); i++) {
			if (prdvw.get(i).getObjMetaType() == ObjMetaType.PRDVW) {
				createAttributeView(simpleView, (JGenObject) prdvw.get(i));
			}
		}
		return simpleView;
	}

	/**
	 * Method adds view declaration node (simple or group view).
	 */
	private void createViewDeclaration(Views views, JGenObject vw) {
		switch (checkViewType(vw)) {
		case VT_VIEW:
			SimpleView simpleView = createsSimpleView(vw);
			simpleView.setGenObject(vw);
			views.getSimpleViews().add(simpleView);
			break;
		case VT_GROUP:
			GroupView groupView = expandGroupView(vw);
			groupView.setGenObject(vw);
			views.getGroupViews().add(groupView);
			break;
		default:
			return;
		}
	}

	/**
	 * Method expands group view looking for nested simple views or group views.
	 */
	private GroupView expandGroupView(JGenObject vw) {
		GroupView groupView = new GroupView();
		groupView.setType(AggregateObjectType.GROUP_VIEW);
		groupView.setGenObject(vw);
		List<JGenObject> vwch = vw.findAssociationMany(AscMetaType.GRPFOR);
		for (int i = 0; i < vwch.size(); i++) {
			if (vwch.get(i).getObjMetaType() == ObjMetaType.GRPVW) {
				groupView.getGroupViews().add(expandGroupView(vwch.get(i)));
			} else {
				groupView.getSimpleViews().add(createsSimpleView(vwch.get(i)));
			}
		}
		return groupView;
	}

	/**
	 * Method selects suitable method to process specific statement.
	 */
	private void selectStatementProcessingMethod(JGenObject stmtObject,
			StatementsGroup statementsGroup) {
// TODO		if (statementObject.findCharacterProperty(PrpMetaType.COMNTD) == 'Y') {
//			return;
//		}
		ObjMetaType code = stmtObject.getObjMetaType();
		if (code == ObjMetaType.ACTNOTE) {
			transformNoteStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.LCLES) {
			transformExitStateIsStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.ACTMOVE) {
			transformMoveStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.LCLCM) {
			transformCommandIsStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.LCLST) {
			transformSetStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.ACTIF) {
			transformIfStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.ACTCO) {
			transformCaseOfStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.ACTFL) {
			transformForStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.ACTFE) {
			transformForEachStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.CNTUSE) {
			transformUseStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.IGNORER) {
			transformIgnoreStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.CHECKR) {
			transformCheckStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.GETAR) {
			transformGetStatement1((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.ACTWH) {
			transformWhileStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.ACTRE) {
			transformRepeatUntilStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.ENTC) {
			transformCreateStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.ENTU) {
			transformUpdateStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.ENTD) {
			transformDeleteStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.ENTS) {
			if (stmtObject.findAssociationMany(AscMetaType.READS).size() > 0) {
				transformSummarizeStatement((JGenObject) stmtObject,
						statementsGroup);
			} else {
				transformReadStatement((JGenObject) stmtObject, statementsGroup);
			}
			return;
		}
		if (code == ObjMetaType.ACTSE) {
			JGenObject ents = stmtObject
					.findAssociationOne(AscMetaType.DETLBY);
			if (ents.findAssociationMany(AscMetaType.READS).size() > 0) {
				transformSummarizeEachStatement((JGenObject) stmtObject,
						statementsGroup);
			} else {
				transformReadEachStatement((JGenObject) stmtObject,
						statementsGroup);
			}
			return;
		}
		if (code == ObjMetaType.PRDRA) {
			transformAssociateStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.PRDAR) {
			transformRemoveStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.PRDRT) {
			transformTransferStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.PRDRD) {
			transformDisassociateStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.LCLOW) {
			transformOpenWindowStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.LCLCW) {
			transformCloseWindowStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.LCLEC) {
			transformEnableCommandStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.LCLDC) {
			transformDisableCommandStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.LCLMC) {
			transformMarkCommandStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.RFRSHACT) {
			transformRefreshStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.LCLUC) {
			transformUnmarkCommandStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.LCLVA) {
			transformMakeStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.FLWESC) {
			transformEscapeStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.FLWNXT) {
			transformNextStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.LCLPRNT) {
			transformPrinterTerminalStatement((JGenObject) stmtObject,
					statementsGroup);
			return;
		}
		if (code == ObjMetaType.ACTNOTE) {
			transformNoteStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.FLWRETRY) {
			transformRetryStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.FLWABORT) {
			transformAbortStatement((JGenObject) stmtObject, statementsGroup);
			return;
		}
		if (code == ObjMetaType.ACTGMOD) {
			if (stmtObject.findTextProperty(PrpMetaType.ACTLABL).equals(
					"ADD_EMPTY_ROW_TO")) {
				transformAddStatement((JGenObject) stmtObject,
						statementsGroup);
			} else if (stmtObject.findTextProperty(PrpMetaType.ACTLABL)
					.equals("GET_ROW")) {
				transformGetStatement1((JGenObject) stmtObject,
						statementsGroup);
			} else if (stmtObject.findTextProperty(PrpMetaType.ACTLABL)
					.equals(NT_FILTER)) {
				transformFilterStatement((JGenObject) stmtObject,
						statementsGroup);
			} else if (stmtObject.findTextProperty(PrpMetaType.ACTLABL)
					.equals(NT_HIGHLIGHT)) {
				transformHighlightStatement((JGenObject) stmtObject,
						statementsGroup);
			} else if (stmtObject.findTextProperty(PrpMetaType.ACTLABL)
					.equals("REMOVE_ROW_FROM")) {
				transformRemoveRowStatement((JGenObject) stmtObject,
						statementsGroup);
			} else if (stmtObject.findTextProperty(PrpMetaType.ACTLABL)
					.equals("SORT")) {
				transformSortStatement((JGenObject) stmtObject,
						statementsGroup);
			} else if (stmtObject.findTextProperty(PrpMetaType.ACTLABL)
					.equals(NT_UNFILTER)) {
				transformUnfilterStatement((JGenObject) stmtObject,
						statementsGroup);
			} else if (stmtObject.findTextProperty(PrpMetaType.ACTLABL)
					.equals(NT_UNHIGHLIGHT)) {
				transformUnhighlightStatement((JGenObject) stmtObject,
						statementsGroup);
			} else if (stmtObject.findTextProperty(PrpMetaType.ACTLABL)
					.equals("SET")) {
				transformSetDotStatement((JGenObject) stmtObject,
						statementsGroup);
			} else if (stmtObject.findTextProperty(PrpMetaType.ACTLABL)
					.equals("INVOKE")) {
				transformInvokeStatement((JGenObject) stmtObject,
						statementsGroup);
			} else if (stmtObject.findTextProperty(PrpMetaType.ACTLABL)
					.equals("FUNCTION")) {
				transformFunctionStatement((JGenObject) stmtObject,
						statementsGroup);
			} else if (stmtObject.findTextProperty(PrpMetaType.ACTLABL)
					.equals("DISPLAY")) {
				transformDisplayStatement((JGenObject) stmtObject,
						statementsGroup);
			} else {
				System.out.println("Unknown generic modyfying action");
			}
			return;
		}
	}

	
	/**
	 * Method creates GET ASYNC RESPONSE statement
	 */
	public void transformGetStatement1(JGenObject getar, StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(getar);
		statement.setType(AggregateObjectType.GET_ASYNC_RESPONSE);
		statementsGroup.getStatements().add(statement);
		// process WHEN (Successful) phrases
		JGenObject acblkgrp = getar.findAssociationOne(AscMetaType.HASSUCCS);
		if (acblkgrp != null) {
			Clause clause = new Clause();
			clause.setGenObject(acblkgrp);
			clause.setType(AggregateObjectType.WHEN_SUCCESFUL);
			statement.getClauses().add(clause);
			StatementsGroup whenStatementsGroup = new StatementsGroup();
			whenStatementsGroup.setGenObject(acblkgrp);
			clause.setStatementsGroup(whenStatementsGroup);
			createStatementsGroup(whenStatementsGroup, acblkgrp);	
		}
		//  process WHEN (Unsuccessful) phrases
		List<JGenObject> acblkgrps = getar.findAssociationMany(AscMetaType.HASEXCP);
		for (JGenObject acblkgrp2 : acblkgrps) {
			Clause clause = new Clause();
			clause.setGenObject(acblkgrp2);
			clause.setType(AggregateObjectType.WHEN);
			statement.getClauses().add(clause);
			StatementsGroup whenStatementsGroup = new StatementsGroup();
			whenStatementsGroup.setGenObject(acblkgrp2);
			clause.setStatementsGroup(whenStatementsGroup);
			createStatementsGroup(whenStatementsGroup, acblkgrp2);	
		}
	}


	/**
	 * Method creates CHECK ASYNC RESPONSE
	 */
	public void transformCheckStatement(JGenObject checkr,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(checkr);
		statement.setType(AggregateObjectType.CHECK_ASYNC_RESPONSE);
		statementsGroup.getStatements().add(statement);
		// process WHEN (Successful) phrases
		JGenObject acblkgrp = checkr.findAssociationOne(AscMetaType.HASSUCCS);
		if (acblkgrp != null) {
			Clause clause = new Clause();
			clause.setGenObject(acblkgrp);
			clause.setType(AggregateObjectType.WHEN_AVIALABLE);
			statement.getClauses().add(clause);
			StatementsGroup whenStatementsGroup = new StatementsGroup();
			whenStatementsGroup.setGenObject(acblkgrp);
			clause.setStatementsGroup(whenStatementsGroup);
			createStatementsGroup(whenStatementsGroup, acblkgrp);	
		}
		//  process WHEN (Unsuccessful) phrases
		List<JGenObject> acblkgrps = checkr.findAssociationMany(AscMetaType.HASEXCP);
		for (JGenObject acblkgrp2 : acblkgrps) {
			Clause clause = new Clause();
			clause.setGenObject(acblkgrp2);
			clause.setType(AggregateObjectType.WHEN);
			statement.getClauses().add(clause);
			StatementsGroup whenStatementsGroup = new StatementsGroup();
			whenStatementsGroup.setGenObject(acblkgrp2);
			clause.setStatementsGroup(whenStatementsGroup);
			createStatementsGroup(whenStatementsGroup, acblkgrp2);	
		}
		
	}

	/**
	 * Method creates IGNORE ASYN RESONSE statement
	 */
	public void transformIgnoreStatement(JGenObject ignorer,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(ignorer);
		statement.setType(AggregateObjectType.IGNORE_ASYNC_RESPONSE);
		statementsGroup.getStatements().add(statement);
		// process WHEN (Successful) phrases
		List<JGenObject> acblkgrps = ignorer.findAssociationMany(AscMetaType.HASEXCP);
		for (JGenObject acblkgrp2 : acblkgrps) {
			Clause clause = new Clause();
			clause.setGenObject(acblkgrp2);
			clause.setType(AggregateObjectType.WHEN);
			statement.getClauses().add(clause);
			StatementsGroup whenStatementsGroup = new StatementsGroup();
			whenStatementsGroup.setGenObject(acblkgrp2);
			clause.setStatementsGroup(whenStatementsGroup);
			createStatementsGroup(whenStatementsGroup, acblkgrp2);	
		}
	 
	}

	/**
	 * Method creates node representing FUNCTION statement.
	 */
	public void transformFunctionStatement(JGenObject actgmod,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actgmod);
		statement.setType(AggregateObjectType.FUNCTION);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates  node for the ABORT statement.
	 */
	public void transformAbortStatement(JGenObject flwabort,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(flwabort);
		statement.setType(AggregateObjectType.ABORT);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates  node the action block.
	 */
	public ActionBlock transformActionBlock(JGenObject acblkdef) {
		ActionBlock actionBlock = new ActionBlock(
				createActionBlockBody(acblkdef));
		actionBlock.setGenObject(acblkdef);
		if (acblkdef.findCharacterProperty(PrpMetaType.INTEXT) == 'E') {
			actionBlock.setType(AggregateObjectType.ACTION_BLOCK_EXTERNAL);
		} else {
			actionBlock.setType(AggregateObjectType.ACTION_BLOCK);
		}

		return actionBlock;
	}

	/**
	 * Method creates node representing ADD statement.
	 */
	public void transformAddStatement(JGenObject actgmod,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actgmod);
		statement.setType(AggregateObjectType.ADD);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates  node representing ASSOCIATE statement.
	 */
	public void transformAssociateStatement(JGenObject prdra,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(prdra);
		statement.setType(AggregateObjectType.ASSOCIATE);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node for CASE OF statement.
	 */
	public void transformCaseOfStatement(JGenObject actco,
			StatementsGroup statementsGroup) {
		Statement statement = new Statement();
		statement.setGenObject(actco);
		statement.setType(AggregateObjectType.CASE_OF);
		statementsGroup.getStatements().add(statement);
		
		// inner constructs
		List<JGenObject> acblkgrps = actco
		.findAssociationMany(AscMetaType.DEFNDBY);
		for (JGenObject acblkgrp : acblkgrps) {
			List<JGenObject> exp = acblkgrp.findAssociationMany(
					AscMetaType.SUBJTOE);
			if (exp.size() != 0) {
				Clause clause = new Clause();
				clause.setGenObject(acblkgrp);
				clause.setType(AggregateObjectType.CASE);
				statement.getClauses().add(clause);
				StatementsGroup innerStatementsGroup = new StatementsGroup();
				innerStatementsGroup.setGenObject(acblkgrp);
				clause.setStatementsGroup(innerStatementsGroup);
				createStatementsGroup(innerStatementsGroup, acblkgrp);	
			} else {
				Clause clause = new Clause();
				clause.setGenObject(acblkgrp);
				clause.setType(AggregateObjectType.OTHERWISE);
				statement.getClauses().add(clause);
				StatementsGroup innerStatementsGroup = new StatementsGroup();
				innerStatementsGroup.setGenObject(acblkgrp);
				clause.setStatementsGroup(innerStatementsGroup);
				createStatementsGroup(innerStatementsGroup, acblkgrp);			
			}
		}
	}

	/**
	 * Method creates node representing CLOSE WINDOW or DIALOGBOX
	 * statement.
	 */
	public void transformCloseWindowStatement(JGenObject lclcw,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(lclcw);
		statement.setType(AggregateObjectType.CLOSE_WINDOW);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing COMMAND IS statement.
	 */
	public void transformCommandIsStatement(JGenObject lclcm,
			StatementsGroup statementsGroup) {
		Statement statement = new Statement();
		statement.setGenObject(lclcm);
		statement.setType(AggregateObjectType.COMMAND_IS);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing CREATE statement.
	 */
	public void transformCreateStatement(JGenObject entc,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(entc);
		statement.setType(AggregateObjectType.CREATE);
		statementsGroup.getStatements().add(statement);
		// process inner statement for CREATE
		StatementsGroup innerStatementsGroup = new StatementsGroup();
		innerStatementsGroup.setGenObject(entc);
		statement.setStatementsGroup(innerStatementsGroup);
		List<JGenObject> prd = entc
				.findAssociationMany(AscMetaType.DTLBY);
		for (JGenObject mmObj : prd) {
			ObjMetaType code = mmObj.getObjMetaType();
			if (code == ObjMetaType.PRDAS) {
				transformSetStatement((JGenObject) mmObj, innerStatementsGroup);
			}
			if (code == ObjMetaType.PRDRA) {
				transformAssociateStatement((JGenObject) mmObj,
						innerStatementsGroup);
			}
			if (code == ObjMetaType.PRDRD) {
				transformDisassociateStatement((JGenObject) mmObj,
						innerStatementsGroup);
			}
			if (code == ObjMetaType.PRDRT) {
				transformTransferStatement((JGenObject) mmObj, innerStatementsGroup);
			}	
		}
		// process WHEN (Successful) phrases
		JGenObject acblkgrp = entc.findAssociationOne(AscMetaType.HASSUCCS);
		if (acblkgrp != null) {
			Clause clause = new Clause();
			clause.setGenObject(acblkgrp);
			clause.setType(AggregateObjectType.WHEN_SUCCESFUL);
			statement.getClauses().add(clause);
			StatementsGroup whenStatementsGroup = new StatementsGroup();
			whenStatementsGroup.setGenObject(acblkgrp);
			clause.setStatementsGroup(whenStatementsGroup);
			createStatementsGroup(whenStatementsGroup, acblkgrp);	
		}
		//  process WHEN (Unsuccessful) phrases
		List<JGenObject> acblkgrps = entc.findAssociationMany(AscMetaType.HASEXCP);
		for (JGenObject acblkgrp2 : acblkgrps) {
			Clause clause = new Clause();
			clause.setGenObject(acblkgrp2);
			clause.setType(AggregateObjectType.WHEN);
			statement.getClauses().add(clause);
			StatementsGroup whenStatementsGroup = new StatementsGroup();
			whenStatementsGroup.setGenObject(acblkgrp2);
			clause.setStatementsGroup(whenStatementsGroup);
			createStatementsGroup(whenStatementsGroup, acblkgrp2);	
		}
	}

	/**
	 * Method creates node representing DELETE statement.
	 */
	public void transformDeleteStatement(JGenObject entd,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(entd);
		statement.setType(AggregateObjectType.DELETE);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing DISABLE COMMAND statement.
	 */
	public void transformDisableCommandStatement(JGenObject lcldc,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(lcldc);
		statement.setType(AggregateObjectType.DISABLE_COMMAND);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing DISASSOCIATE statement.
	 */
	public void transformDisassociateStatement(JGenObject prdrd,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(prdrd);
		statement.setType(AggregateObjectType.DISASSOCIATE);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing DISPLAY statement.
	 */
	public void transformDisplayStatement(JGenObject actgmod,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actgmod);
		statement.setType(AggregateObjectType.DISPLAY);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing ENABLE COMMAND statement.
	 */
	public void transformEnableCommandStatement(JGenObject lclec,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(lclec);
		statement.setType(AggregateObjectType.ENABLE_COMMAND);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing entity actions.
	 */
	public void transformEntityActionsDeclaration(
			ViewsDeclaration viewsDeclaration, JGenObject acblkdef) {
		JGenObject dvset = acblkdef.findAssociationOne(AscMetaType.GRPBY);
//		if (areAnyActionEntities(dvset)) {
			EntityActions entityActions = new EntityActions();
			entityActions.setType(AggregateObjectType.ENTITY_ACTIONS);
			entityActions.setGenObject(dvset
					.findAssociationOne(AscMetaType.CNTENTS));
			viewsDeclaration.setEntityTypes(entityActions);
			createViewDeclaration(entityActions,
					dvset.findAssociationOne(AscMetaType.CNTENTS));
//		}
	}

	/**
	 * Method creates node representing ESCAPE statement.
	 */
	public void transformEscapeStatement(JGenObject flwesc,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(flwesc);
		statement.setType(AggregateObjectType.ESCAPE);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing event declaration.
	 */
	public EventDeclaration transformEventDeclaration(JGenObject evnthdlr) {
		EventDeclaration eventDeclaration = new EventDeclaration();
		eventDeclaration.setGenObject(evnthdlr);
		eventDeclaration.setType(AggregateObjectType.EVENT);
		StatementsGroup statementsGroup = new StatementsGroup();
		createStatementsGroup(statementsGroup, evnthdlr);
		eventDeclaration.setStatementsGroup(statementsGroup);
		return eventDeclaration;
	}

	/**
	 * Method creates node representing EXIT STATE IS statement.
	 */
	public void transformExitStateIsStatement(JGenObject lcles,
			StatementsGroup statementsGroup) {
		Statement statement = new Statement();
		statement.setGenObject(lcles);
		statement.setType(AggregateObjectType.EXIT_STATE_IS);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing exports.
	 */
	public void transformExportsDeclaration(ViewsDeclaration viewsDeclaration,
			JGenObject acblkdef) {
		JGenObject dvset = acblkdef.findAssociationOne(AscMetaType.GRPBY);
//		if (areAnyExports(dvset)) {
			ExportViews exportViews = new ExportViews();
			exportViews.setType(AggregateObjectType.EXPORTS);
			exportViews.setGenObject(dvset
					.findAssociationOne(AscMetaType.CNTOUTS));
			viewsDeclaration.setExportViews(exportViews);
			createContainedViewDeclarations(exportViews,
					dvset.findAssociationOne(AscMetaType.CNTOUTS));
	//	}
	}

	/**
	 * Method creates node representing FILTER statement.
	 */
	public void transformFilterStatement(JGenObject actgmod,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actgmod);
		statement.setType(AggregateObjectType.FILTER);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing FOR EACH statement.
	 */
	public void transformForEachStatement(JGenObject actfe,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actfe);
		statement.setType(AggregateObjectType.FOR_EACH);
		statementsGroup.getStatements().add(statement);
		
		// inner constructs
		List<JGenObject> acblkgrps = actfe
		.findAssociationMany(AscMetaType.DEFNDBY);
		for (JGenObject acblkgrp : acblkgrps) {
			StatementsGroup innerStatementsGroup = new StatementsGroup();
			innerStatementsGroup.setGenObject(acblkgrp);
			statement.setStatementsGroup(innerStatementsGroup);
			createStatementsGroup(innerStatementsGroup, acblkgrp);
		}
	}

	/**
	 * Method creates node representing  FOR statement.
	 */
	public void transformForStatement(JGenObject actfl,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actfl);
		statement.setType(AggregateObjectType.FOR);
		statementsGroup.getStatements().add(statement);
	
		// inner constructs
		List<JGenObject> acblkgrps = actfl
		.findAssociationMany(AscMetaType.DEFNDBY);
		for (JGenObject acblkgrp : acblkgrps) {
			StatementsGroup innerStatementsGroup = new StatementsGroup();
			innerStatementsGroup.setGenObject(acblkgrp);
			statement.setStatementsGroup(innerStatementsGroup);
			createStatementsGroup(innerStatementsGroup, acblkgrp);
		}
	}

	/**
	 * Method creates node representing GET statement.
	 */
	public void transformGetStatement(JGenObject actgmod,
			StatementsGroup statementsGroup) {
		Statement statement = new Statement();
		statement.setGenObject(actgmod);
		statement.setType(AggregateObjectType.GET);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing HIGHLIGHT statement.
	 */
	public void transformHighlightStatement(JGenObject actgmod,
			StatementsGroup statementsGroup) {
		Statement statement = new Statement();
		statement.setGenObject(actgmod);
		statement.setType(AggregateObjectType.HIGHLIGHT);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing IF statement.
	 */
	public void transformIfStatement(JGenObject actif,
			StatementsGroup statementsGroup) {
		// main IF statement
		Statement statement = new Statement();
		statement.setGenObject(actif);
		statement.setType(AggregateObjectType.IF);
		statementsGroup.getStatements().add(statement);
		// inner constructs
		List<JGenObject> acblkgrp = actif
		.findAssociationMany(AscMetaType.DEFNDBY);
	
		for (int i = 0; i < acblkgrp.size(); i++) {
			if (i == 0) {
				// IF
				StatementsGroup innerStatementsGroup = new StatementsGroup();
				innerStatementsGroup.setGenObject(acblkgrp.get(i));
				statement.setStatementsGroup(innerStatementsGroup);
				createStatementsGroup(innerStatementsGroup, acblkgrp.get(i));
			} else {
				JGenObject cndus = acblkgrp.get(i).findAssociationOne(AscMetaType.SUBJTO);
				if (cndus != null) {
					Clause clause = new Clause();
					clause.setGenObject(acblkgrp.get(i));
					clause.setType(AggregateObjectType.ELSE_IF);
					statement.getClauses().add(clause);
					StatementsGroup innerStatementsGroup = new StatementsGroup();
					innerStatementsGroup.setGenObject(acblkgrp.get(i));
					clause.setStatementsGroup(innerStatementsGroup);
					createStatementsGroup(innerStatementsGroup, acblkgrp.get(i));
				} else {
					Clause clause = new Clause();
					clause.setGenObject(acblkgrp.get(i));
					clause.setType(AggregateObjectType.ELSE);
					statement.getClauses().add(clause);
					StatementsGroup innerStatementsGroup = new StatementsGroup();
					innerStatementsGroup.setGenObject(acblkgrp.get(i));
					clause.setStatementsGroup(innerStatementsGroup);
					createStatementsGroup(innerStatementsGroup, acblkgrp.get(i));
				}
			}
		}
	}

	/**
	 * Method creates node representing imports.
	 */
	public void transformImportDeclaration(ViewsDeclaration viewsDeclaration,
			JGenObject acblkdef) {
		JGenObject dvset = acblkdef.findAssociationOne(AscMetaType.GRPBY);
//		if (areAnyImports(dvset)) {
			ImportViews importViews = new ImportViews();
			importViews.setType(AggregateObjectType.IMPORTS);
			importViews.setGenObject(dvset
					.findAssociationOne(AscMetaType.CNTINPS));
			viewsDeclaration.setImportViews(importViews);
			createContainedViewDeclarations(importViews,
					dvset.findAssociationOne(AscMetaType.CNTINPS));
//		}
	}

	/**
	 * Method creates node representing INVOKE statement.
	 */
	public void transformInvokeStatement(JGenObject actgmod,
			StatementsGroup statementsGroup) {
		Statement statement = new Statement();
		statement.setGenObject(actgmod);
		statement.setType(AggregateObjectType.INVOKE);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node for the local views.
	 */
	public void transformLocalsDeclaration(ViewsDeclaration viewsDeclaration,
			JGenObject acblkdef) {
		JGenObject dvset = acblkdef.findAssociationOne(AscMetaType.GRPBY);
//		if (areAnyLocals(dvset)) {
			LocalViews localViews = new LocalViews();
			localViews.setType(AggregateObjectType.LOCALS);
			localViews.setGenObject(dvset
					.findAssociationOne(AscMetaType.CNTLCLS));
			viewsDeclaration.setLocalViews(localViews);
			createContainedViewDeclarations(localViews,
					dvset.findAssociationOne(AscMetaType.CNTLCLS));
//		}

	}

	/**
	 * Method creates node representing main part of the action
	 * block logic.
	 */
	public MainDeclaration transformMainDeclaration(JGenObject acblkdef) {
		MainDeclaration mainDeclaration = new MainDeclaration();
		mainDeclaration.setGenObject(acblkdef);
		mainDeclaration.setType(AggregateObjectType.MAIN);
		StatementsGroup statementsGroup = new StatementsGroup();
		statementsGroup.setType(AggregateObjectType.STATEMENTS_GROUP);
		statementsGroup.setGenObject(acblkdef);
		createStatementsGroup(statementsGroup, acblkdef);
		mainDeclaration.setStatementsGroup(statementsGroup);
		return mainDeclaration;
	}

	/**
	 * Method creates node representing MAKE statement.
	 */
	public void transformMakeStatement(JGenObject lclva,
			StatementsGroup statementsGroup) {
		Statement statement = new Statement();
		statement.setGenObject(lclva);
		statement.setType(AggregateObjectType.MAKE);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing MARK COMMAND statement.
	 */
	public void transformMarkCommandStatement(JGenObject lclmc,
			StatementsGroup statementsGroup) {
		Statement statement = new Statement();
		statement.setGenObject(lclmc);
		statement.setType(AggregateObjectType.MARK_COMMAND);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing MOVE statement.
	 */
	public void transformMoveStatement(JGenObject actmove,
			StatementsGroup statementsGroup) {
		Statement statement = new Statement();
		statement.setGenObject(actmove);
		statement.setType(AggregateObjectType.MOVE);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates declaration node representing NEXT statement.
	 */
	public void transformNextStatement(JGenObject flwnxt,
			StatementsGroup statementsGroup) {
		Statement statement = new Statement();
		statement.setGenObject(flwnxt);
		statement.setType(AggregateObjectType.NEXT);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing NOTE statement.
	 */
	public void transformNoteStatement(JGenObject actnote,
			StatementsGroup statementsGroup) {
		Statement statement = new Statement();
		statement.setGenObject(actnote);
		statement.setType(AggregateObjectType.NOTE);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing OPEN WINDOW or DIALOG
	 * statement.
	 */
	public void transformOpenWindowStatement(JGenObject lclow,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(lclow);
		statement.setType(AggregateObjectType.OPEN_WINDOW_DIALOG);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing PRINTER TERMINAL statement.
	 */
	public void transformPrinterTerminalStatement(JGenObject lclprnt,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(lclprnt);
		statement.setType(AggregateObjectType.PRINTER_TERMINAL);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing READ EACH statement.
	 */
	public void transformReadEachStatement(JGenObject actse,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actse);
		statement.setType(AggregateObjectType.READ_EACH);
		statementsGroup.getStatements().add(statement);
		
		// process inner statement for READ EACH
		List<JGenObject> acblkgrps = actse.findAssociationMany(AscMetaType.DEFNDBY); 
		for (JGenObject acblkgrp : acblkgrps) {
			StatementsGroup innerStatementsGroup = new StatementsGroup();
			innerStatementsGroup.setGenObject(acblkgrp);
			statement.setStatementsGroup(innerStatementsGroup);
			createStatementsGroup(innerStatementsGroup, acblkgrp);
		}
	}

	/**
	 * Method creates node representing READ statement.
	 */
	public void transformReadStatement(JGenObject ents,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(ents);
		statement.setType(AggregateObjectType.READ);
		statementsGroup.getStatements().add(statement);
		// process WHEN (Successful) phrases
		JGenObject acblkgrp = ents.findAssociationOne(AscMetaType.HASSUCCS);
		if (acblkgrp != null) {
			Clause clause = new Clause();
			clause.setGenObject(acblkgrp);
			clause.setType(AggregateObjectType.WHEN_SUCCESFUL);
			statement.getClauses().add(clause);
			StatementsGroup whenStatementsGroup = new StatementsGroup();
			whenStatementsGroup.setGenObject(acblkgrp);
			clause.setStatementsGroup(whenStatementsGroup);
			createStatementsGroup(whenStatementsGroup, acblkgrp);	
		}
		//  process WHEN (Unsuccessful) phrases
		List<JGenObject> acblkgrps = ents.findAssociationMany(AscMetaType.HASEXCP);
		for (JGenObject acblkgrp2 : acblkgrps) {
			Clause clause = new Clause();
			clause.setGenObject(acblkgrp2);
			clause.setType(AggregateObjectType.WHEN);
			statement.getClauses().add(clause);
			StatementsGroup whenStatementsGroup = new StatementsGroup();
			whenStatementsGroup.setGenObject(acblkgrp2);
			clause.setStatementsGroup(whenStatementsGroup);
			createStatementsGroup(whenStatementsGroup, acblkgrp2);	
		}
	}

	/**
	 * Method creates node representing REFRESH statements.
	 */
	public void transformRefreshStatement(JGenObject rfrshact,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(rfrshact);
		statement.setType(AggregateObjectType.REFRESH);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing REMOVE ROW statement.
	 */
	public void transformRemoveRowStatement(JGenObject actgmod,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actgmod);
		statement.setType(AggregateObjectType.REMOVE_ROW);
		statementsGroup.getStatements().add(statement);
	}
	
	/**
	 * Method creates node representing REMOVE statement.
	 */
	public void transformRemoveStatement(JGenObject prdra,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(prdra);
		statement.setType(AggregateObjectType.REMOVE);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing REPEAT UNTIL statement.
	 */
	public void transformRepeatUntilStatement(JGenObject actre,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actre);
		statement.setType(AggregateObjectType.REPEAT_UNTIL);
		statementsGroup.getStatements().add(statement);
		
		// inner constructs
		List<JGenObject> acblkgrps = actre
		.findAssociationMany(AscMetaType.DEFNDBY);
		for (JGenObject acblkgrp : acblkgrps) {
			StatementsGroup innerStatementsGroup = new StatementsGroup();
			innerStatementsGroup.setGenObject(acblkgrp);
			statement.setStatementsGroup(innerStatementsGroup);
			createStatementsGroup(innerStatementsGroup, acblkgrp);
		}
	}

	/**
	 * Method creates node representing RETRY statement.
	 */
	public void transformRetryStatement(JGenObject flwretry,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(flwretry);
		statement.setType(AggregateObjectType.RETRY);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing SET statement.
	 */
	public void transformSetDotStatement(JGenObject actgmod,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actgmod);
		statement.setType(AggregateObjectType.SET);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing SET statement.
	 */
	public void transformSetStatement(JGenObject lclst,
			StatementsGroup statementsGroup) {
		Statement statement = new Statement();
		statement.setGenObject(lclst);
		statement.setType(AggregateObjectType.SET);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing SORT statement.
	 */
	public void transformSortStatement(JGenObject actgmod,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actgmod);
		statement.setType(AggregateObjectType.SORT);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing SUMMARIZE EACH statement.
	 */
	public void transformSummarizeEachStatement(JGenObject actse,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actse);
		statement.setType(AggregateObjectType.SUMMARIZE_EACH);
		statementsGroup.getStatements().add(statement);
		// process inner statement for SUMMARIZE_EACH
		List<JGenObject> acblkgrps = actse.findAssociationMany(AscMetaType.DEFNDBY);
		for (JGenObject acblkgrp : acblkgrps) {
			StatementsGroup innerStatementsGroup = new StatementsGroup();
			innerStatementsGroup.setGenObject(acblkgrp);
			statement.setStatementsGroup(innerStatementsGroup);
			createStatementsGroup(innerStatementsGroup, acblkgrp);
		}
	}

	/**
	 * Method creates node representing SUMMARIZE statement.
	 */
	public void transformSummarizeStatement(JGenObject ents,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(ents);
		statement.setType(AggregateObjectType.SUMMARIZE);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing TRANSFER statement.
	 */
	public void transformTransferStatement(JGenObject prdrt,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(prdrt);
		statement.setType(AggregateObjectType.TRANSFER);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing UNFILTER statement.
	 */
	public void transformUnfilterStatement(JGenObject actgmod,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actgmod);
		statement.setType(AggregateObjectType.UNFILTER);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing UNHIGHLIGHT statement.
	 */
	public void transformUnhighlightStatement(JGenObject actgmod,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actgmod);
		statement.setType(AggregateObjectType.UNHIGHLIGHT);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing UNMARK COMMAND statement.
	 */
	public void transformUnmarkCommandStatement(JGenObject lcluc,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(lcluc);
		statement.setType(AggregateObjectType.UNMARK_COMMAND);
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing UPDATE statement.
	 */
	public void transformUpdateStatement(JGenObject entu,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(entu);
		statement.setType(AggregateObjectType.UPDATE);
		statementsGroup.getStatements().add(statement);
		// process inner statement for CREATE
		StatementsGroup innerStatementsGroup = new StatementsGroup();
		innerStatementsGroup.setGenObject(entu);
		statement.setStatementsGroup(innerStatementsGroup);
		List<JGenObject> prd = entu
				.findAssociationMany(AscMetaType.DTLBY);
		for (JGenObject mmObj : prd) {
			ObjMetaType code = mmObj.getObjMetaType();
			if (code == ObjMetaType.PRDAS) {
				transformSetStatement((JGenObject) mmObj, innerStatementsGroup);
			}
			if (code == ObjMetaType.PRDAR) {
				transformRemoveStatement((JGenObject) mmObj, innerStatementsGroup);
			}
			if (code == ObjMetaType.PRDRA) {
				transformAssociateStatement((JGenObject) mmObj,
						innerStatementsGroup);
			}
			if (code == ObjMetaType.PRDRD) {
				transformDisassociateStatement((JGenObject) mmObj,
						innerStatementsGroup);
			}
			if (code == ObjMetaType.PRDRT) {
				transformTransferStatement((JGenObject) mmObj, innerStatementsGroup);
			}	
		}
		// process WHEN (Successful) phrases
		JGenObject acblkgrp = entu.findAssociationOne(AscMetaType.HASSUCCS);
		if (acblkgrp != null) {
			Clause clause = new Clause();
			clause.setGenObject(acblkgrp);
			clause.setType(AggregateObjectType.WHEN_SUCCESFUL);
			statement.getClauses().add(clause);
			StatementsGroup whenStatementsGroup = new StatementsGroup();
			whenStatementsGroup.setGenObject(acblkgrp);
			clause.setStatementsGroup(whenStatementsGroup);
			createStatementsGroup(whenStatementsGroup, acblkgrp);	
		}
		//  process WHEN (Unsuccessful) phrases
		List<JGenObject> acblkgrps = entu.findAssociationMany(AscMetaType.HASEXCP);
		for (JGenObject acblkgrp2 : acblkgrps) {
			Clause clause = new Clause();
			clause.setGenObject(acblkgrp2);
			clause.setType(AggregateObjectType.WHEN);
			statement.getClauses().add(clause);
			StatementsGroup whenStatementsGroup = new StatementsGroup();
			whenStatementsGroup.setGenObject(acblkgrp2);
			clause.setStatementsGroup(whenStatementsGroup);
			createStatementsGroup(whenStatementsGroup, acblkgrp2);	
		}
	}

	/**
	 * Method creates node representing USE statement.
	 */
	public void transformUseStatement(JGenObject cntuse,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(cntuse);
		statement.setType(AggregateObjectType.USE);
		JGenObject acblk = cntuse.findAssociationOne(AscMetaType.OF)
				.findAssociationOne(AscMetaType.REFS);
		if (acblk.getObjMetaType() == ObjMetaType.ACBLKBSD) {
			JGenObject busprst = acblk.findAssociationOne(AscMetaType.DEFINES);
			if (busprst != null) {
				statement.setType(AggregateObjectType.USE_PROCEDURE_STEP);
				if (cntuse.findCharacterProperty(PrpMetaType.SYNCUSE) == 'N') {
					statement.setType(AggregateObjectType.ASYNC_USE);
					// process WHEN (Successful) phrases
					JGenObject acblkgrp = cntuse.findAssociationOne(AscMetaType.HASSUCCS);
					if (acblkgrp != null) {
						Clause clause = new Clause();
						clause.setGenObject(acblkgrp);
						clause.setType(AggregateObjectType.WHEN_SUCCESFUL);
						statement.getClauses().add(clause);
						StatementsGroup whenStatementsGroup = new StatementsGroup();
						whenStatementsGroup.setGenObject(acblkgrp);
						clause.setStatementsGroup(whenStatementsGroup);
						createStatementsGroup(whenStatementsGroup, acblkgrp);	
					}
					//  process WHEN (Unsuccessful) phrases
					List<JGenObject> acblkgrps = cntuse.findAssociationMany(AscMetaType.HASEXCP);
					for (JGenObject acblkgrp2 : acblkgrps) {
						Clause clause = new Clause();
						clause.setGenObject(acblkgrp2);
						clause.setType(AggregateObjectType.WHEN);
						statement.getClauses().add(clause);
						StatementsGroup whenStatementsGroup = new StatementsGroup();
						whenStatementsGroup.setGenObject(acblkgrp2);
						clause.setStatementsGroup(whenStatementsGroup);
						createStatementsGroup(whenStatementsGroup, acblkgrp2);	
					}
				}
			}
		}
		statementsGroup.getStatements().add(statement);
	}

	/**
	 * Method creates node representing WHILE statement.
	 */
	public void transformWhileStatement(JGenObject actwh,
			StatementsGroup statementsGroup) {
		// create statement declaration
		Statement statement = new Statement();
		statement.setGenObject(actwh);
		statement.setType(AggregateObjectType.WHILE);
		statementsGroup.getStatements().add(statement);
		// inner constructs
		List<JGenObject> acblkgrps = actwh
		.findAssociationMany(AscMetaType.DEFNDBY);
		for (JGenObject acblkgrp : acblkgrps) {
			StatementsGroup innerStatementsGroup = new StatementsGroup();
			innerStatementsGroup.setGenObject(acblkgrp);
			statement.setStatementsGroup(innerStatementsGroup);
			createStatementsGroup(innerStatementsGroup, acblkgrp);
		}
	}

}
