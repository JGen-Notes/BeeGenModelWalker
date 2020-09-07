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

import eu.jgen.beegen.model.api.JGenObject;
import eu.jgen.beegen.model.mirror.decaration.Node;
import eu.jgen.beegen.model.mirror.visitor.Visitor;

public class GenericHTMLFormater extends Visitor  {
	
	StringBuffer buffer = new StringBuffer();
	
	public GenericHTMLFormater(String baseref) {
		super();
		buffer.append("<!DOCTYPE html>");
		buffer.append("<html>");
		buffer.append("<head>");
		buffer.append("<base href=\"" + baseref + "\" target=\"_blank\">");
		buffer.append("</head>");
		buffer.append("<body>");
	}
	
	  /** The current indentation */
	  private int curIndent = 0;
	  
	  public void indent(int value) {
		  curIndent = curIndent + value * 3;
	  }

	  public void outdent(int value) {
		  curIndent = curIndent - value * 3;
	  }
	  
	  public void out(Object object) {
		  buffer.append(describe(object));
	  }
	  
	  private String describe(Object object) {
		  if (object instanceof Node) {
			  Node node = (Node) object;
			  JGenObject genObject = node.getGenObject();
			  String name = "";
			  if (genObject.name.length() > 0)  {
				  name = ", <b>name</b>=" + genObject.name;
			  }			  
			  return "<div style=\"margin-left: " + (10 * curIndent)  +"px; color:Gray;\">" + "<img src=\"icons/object.gif\"> <b style=\"color:FireBrick;\">" + node.getType().toString().replace('_', ' ').toLowerCase() + "</b> - [" +"<b>id</b>=" + genObject.objId + ", <b>objType</b>=" + genObject.objType + ", <b>mnemonic</b>=" + genObject.objMnemonic + name + "]" +"</div>";
		  } else if (object instanceof String) {
			  String text = (String) object;
			  return "<div style=\"margin-left: " + (10 * curIndent)  +"px; color:DodgerBlue;\"><b style=\"color:Black;\">" + text + "</b>"  +"</div>";
		  }
		  return "";		  
	  }
	  
	  public String toString() {
		  buffer.append("</body>");
		  buffer.append("</html>");
		  return buffer.toString();
	  }
}
