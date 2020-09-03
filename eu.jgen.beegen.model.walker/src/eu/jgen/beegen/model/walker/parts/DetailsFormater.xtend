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
package eu.jgen.beegen.model.walker.parts

import eu.jgen.beegen.model.meta.ObjMetaType
import eu.jgen.beegen.model.meta.PrpMetaType
import eu.jgen.beegen.model.meta.Meta
import eu.jgen.beegen.model.meta.AscMetaType

class DetailsFormater {

	def static String formatProperty(Meta meta, ObjMetaType objTypeCode, PrpMetaType prpTypeCode) {
		var buffer = new StringBuffer()
		buffer.append("format=")
		buffer.append(meta.getPropertyFormat(objTypeCode, prpTypeCode).toString)
		buffer.append(", default=")
		switch (meta.getPropertyFormat(objTypeCode, prpTypeCode)) {
			case "ART": {
				buffer.append(meta.getDefaultTextProperty(objTypeCode, prpTypeCode))
			}
			case "CHAR": {    
				buffer.append("'")
				buffer.append(meta.getDefaultCharProperty(objTypeCode, prpTypeCode))
				buffer.append("'")
			}
			case "INT": {
				buffer.append(meta.getDefaultNumberProperty(objTypeCode, prpTypeCode))
			}
			case "LOADNAME": {
				buffer.append(meta.getDefaultTextProperty(objTypeCode, prpTypeCode))
			}
			case "NAME": {
				buffer.append(meta.getDefaultTextProperty(objTypeCode, prpTypeCode))
			}   
			case "SINT": {
				buffer.append(meta.getDefaultNumberProperty(objTypeCode, prpTypeCode))
			}
			case "TEXT": {
				if (meta.getDefaultTextProperty(objTypeCode, prpTypeCode) === null) {
					buffer.append("NULL")
				} else {
					buffer.append("\"")
					buffer.append(meta.getDefaultTextProperty(objTypeCode, prpTypeCode))
					buffer.append("\"")
				}
			}  
			default: {
			}
		}
		buffer.append(", length=")
		buffer.append((meta.getPropertyLength(objTypeCode, prpTypeCode)))
		return buffer.toString
	}

	def static String formatAssociation(Meta meta, ObjMetaType objTypeCode, AscMetaType ascTypeCode) {
		var buffer = new StringBuffer()
		if (meta.isAssociationOnetoOne(objTypeCode, ascTypeCode)) {
			buffer.append("1:1")
		} else {
			buffer.append("1:M")
		}		
		buffer.append(", opt=")
		if (meta.isAssociationOptional(objTypeCode, ascTypeCode)) {
			buffer.append("Y")
		} else {
			buffer.append("N")
		}   
		buffer.append(", ordered=")
		if (meta.isAssociationOrdered(objTypeCode, ascTypeCode)) {
			buffer.append("Y")
		} else {
			buffer.append("N")
		}	
		buffer.append(", inverse=")
		buffer.append(meta.getAssociationInverse(objTypeCode, ascTypeCode))				
		return buffer.toString
	}
}
