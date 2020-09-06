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
import eu.jgen.beegen.model.mirror.visitor.Visitor;

public class GenericFormater extends Visitor  {
	
	  /** The current indentation */
	  private int curIndent = 0;
	  
	  public void indent(int value) {
		  curIndent = curIndent + value * 3;
	  }

	  public void outdent(int value) {
		  curIndent = curIndent - value * 3;
	  }
	  
	  public String space() {
		  return "                                                                                                                     ".substring(0,curIndent);
	  }
	  
	  public void out(String text) {
		  System.out.println( space() + text);
	  }
	  
	  public void out(Object object) {
		  System.out.println( space() + describe(object));
	  }
	  
	  private String describe(Object object) {
		  if (object instanceof JGenObject) {
			  JGenObject genObject = (JGenObject) object;
			  return "[" + genObject.objId + "," + genObject.objMnemonic + "," + genObject.objType + "," + genObject.name + "]";
		  }
		  return "";
		  
	  }
}
