/**
 * [The "BSD license"]
 * Copyright (c) 2016,JGen Notes
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
package eu.jgen.beegen.model.walker.parts;

import java.util.List;
import java.util.Vector;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import eu.jgen.beegen.model.api.JGenContainer;
import eu.jgen.beegen.model.api.JGenModel;
import eu.jgen.beegen.model.api.JGenObject;
import eu.jgen.beegen.model.meta.AscMetaType;
import eu.jgen.beegen.model.meta.Meta;
import eu.jgen.beegen.model.meta.ObjMetaType;
import eu.jgen.beegen.model.walker.parts.SelectAndExpand.StartingPoint;

public class WalkencyContentProvider implements IStructuredContentProvider, ITreeContentProvider {

	private JGenModel genModel;
	private JGenContainer genContainer;
	private Meta meta;

	public WalkencyContentProvider() {
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof JGenModel) {
			return new Object[] { genModel };
		} else if (parentElement instanceof JGenObject) {
			if (genModel  == null) {
				this.genModel = ((JGenObject) parentElement).genModel;
				this.genContainer = genModel.getContainer();
				this.meta = genContainer.meta;
			}
			return findAssociations((JGenObject) parentElement);
		} else if (parentElement instanceof AssociationNodeOne) {
			AssociationNodeOne associationNodeOne = (AssociationNodeOne) parentElement;
			return new JGenObject[] { associationNodeOne.getToGenObject() };
		} else if (parentElement instanceof AssociationNodeMany) {
			JGenObject genObject = ((AssociationNodeMany) parentElement).getFromGenObject();
			AscMetaType ascMetaType = ((AssociationNodeMany) parentElement).getAscMetaType();
			List<JGenObject> list = genObject.findAssociationMany(ascMetaType);
			return list.toArray();
		} 
		return new Object[] {};
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return true;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof JGenModel) {
			this.genModel = (JGenModel) inputElement;
			this.genContainer = genModel.getContainer();
			this.meta = genContainer.meta;
			return  genModel.findTypeObjects(ObjMetaType.PCROOT).toArray();
		} else if (inputElement instanceof ObjMetaType) {
			return genModel.findTypeObjects((ObjMetaType) inputElement).toArray();
		} else if (inputElement instanceof JGenObject) {
			return getChildren(inputElement);
		} else if (inputElement instanceof AssociationNode) {
			AssociationNode associationNode = (AssociationNode) inputElement;
			return new Object[] {associationNode};
		} else if (inputElement instanceof StartingPoint)  {
			return ((StartingPoint) inputElement).getObjects();
		}
		return new Object[] {};
	}

	private Object[] findAssociations(JGenObject fromGenObject) {
		ObjMetaType objTypeCode = fromGenObject.getObjMetaType();
		List<AscMetaType> list = meta.getAssociationCodes(fromGenObject.getObjMetaType());   
		List<AssociationNode> nodes = new Vector<AssociationNode>();
		for (AscMetaType ascTypeCode : list) {
			if (meta.isAssociationOnetoOne(objTypeCode, ascTypeCode)) {    
				JGenObject toGenObject = fromGenObject.findAssociationOne(ascTypeCode);
				if(toGenObject != null) {
					AssociationNodeOne node = new AssociationNodeOne(fromGenObject, toGenObject, ascTypeCode);
					nodes.add(node);
				}
			} else {
				List<JGenObject> array = fromGenObject.findAssociationMany(ascTypeCode);
				if (array.size() != 0) {
					AssociationNodeMany node = new AssociationNodeMany(fromGenObject, null, ascTypeCode);
					nodes.add(node);
				}
			}
		}
		return nodes.toArray();
	}

}
