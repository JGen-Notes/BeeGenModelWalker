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


import eu.jgen.beegen.model.api.JGenObject;
import eu.jgen.beegen.model.mirror.visitor.Visitor;


public class Node implements INode {
	
	private static final long serialVersionUID = 1L;
	
	private INode parent;
	
	private JGenObject genObject;
	
	private AggregateObjectType type;
	
	public void setType(AggregateObjectType type) {
		this.type = type;
	}

	public AggregateObjectType getType() {
		return type;
	}
	
	public JGenObject getGenObject() {
		return genObject;
	}

	public void setGenObject(JGenObject mMObj) {
		this.genObject = mMObj;
	}

	@Override
	public INode getParent() {

		return parent;
	}

	@Override
	public void setParent(INode parent) {
		this.parent = parent;

	}

	@Override
	public void accept(Visitor visitor) {	
	}
	
	public String toString() {
		return getType() + " : " + genObject.toString();
	}
	
//	public String toString() {
//		return "<p style=\"margin-left: 40px\">This text is indented.</p>";
//	}
	
	//String a =  "/Users/Marek/git/BeeGenModelWalker/eu.jgen.beegen.model.walker/icons/object.gif";
	//return "[" + this.objId + ", objType=" + this.objType + ", mnemonic=" + this.objMnemonic + name + "]" ;
	//return "<p style=\"margin-left: 80px; color:DodgerBlue;\">" + "<img src=\"/Users/Marek/git/BeeGenModelWalker/eu.jgen.beegen.model.walker/icons/object.gif\">" + "[" +"<b>id</b>=" + this.objId + ", <b>objType</b>=" + this.objType + ", <b>mnemonic</b>=" + this.objMnemonic + name + "]" +"</p>";

}
