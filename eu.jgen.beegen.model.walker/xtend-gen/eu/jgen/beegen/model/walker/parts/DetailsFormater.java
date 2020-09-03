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
 */
package eu.jgen.beegen.model.walker.parts;

import eu.jgen.beegen.model.meta.AscMetaType;
import eu.jgen.beegen.model.meta.Meta;
import eu.jgen.beegen.model.meta.ObjMetaType;
import eu.jgen.beegen.model.meta.PrpMetaType;

@SuppressWarnings("all")
public class DetailsFormater {
  public static String formatProperty(final Meta meta, final ObjMetaType objTypeCode, final PrpMetaType prpTypeCode) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("format=");
    buffer.append(meta.getPropertyFormat(objTypeCode, prpTypeCode).toString());
    buffer.append(", default=");
    String _propertyFormat = meta.getPropertyFormat(objTypeCode, prpTypeCode);
    if (_propertyFormat != null) {
      switch (_propertyFormat) {
        case "ART":
          buffer.append(meta.getDefaultTextProperty(objTypeCode, prpTypeCode));
          break;
        case "CHAR":
          buffer.append("\'");
          buffer.append(meta.getDefaultCharProperty(objTypeCode, prpTypeCode));
          buffer.append("\'");
          break;
        case "INT":
          buffer.append(meta.getDefaultNumberProperty(objTypeCode, prpTypeCode));
          break;
        case "LOADNAME":
          buffer.append(meta.getDefaultTextProperty(objTypeCode, prpTypeCode));
          break;
        case "NAME":
          buffer.append(meta.getDefaultTextProperty(objTypeCode, prpTypeCode));
          break;
        case "SINT":
          buffer.append(meta.getDefaultNumberProperty(objTypeCode, prpTypeCode));
          break;
        case "TEXT":
          String _defaultTextProperty = meta.getDefaultTextProperty(objTypeCode, prpTypeCode);
          boolean _tripleEquals = (_defaultTextProperty == null);
          if (_tripleEquals) {
            buffer.append("NULL");
          } else {
            buffer.append("\"");
            buffer.append(meta.getDefaultTextProperty(objTypeCode, prpTypeCode));
            buffer.append("\"");
          }
          break;
        default:
          break;
      }
    } else {
    }
    buffer.append(", length=");
    buffer.append(meta.getPropertyLength(objTypeCode, prpTypeCode));
    return buffer.toString();
  }
  
  public static String formatAssociation(final Meta meta, final ObjMetaType objTypeCode, final AscMetaType ascTypeCode) {
    StringBuffer buffer = new StringBuffer();
    boolean _isAssociationOnetoOne = meta.isAssociationOnetoOne(objTypeCode, ascTypeCode);
    if (_isAssociationOnetoOne) {
      buffer.append("1:1");
    } else {
      buffer.append("1:M");
    }
    buffer.append(", opt=");
    boolean _isAssociationOptional = meta.isAssociationOptional(objTypeCode, ascTypeCode);
    if (_isAssociationOptional) {
      buffer.append("Y");
    } else {
      buffer.append("N");
    }
    buffer.append(", ordered=");
    boolean _isAssociationOrdered = meta.isAssociationOrdered(objTypeCode, ascTypeCode);
    if (_isAssociationOrdered) {
      buffer.append("Y");
    } else {
      buffer.append("N");
    }
    buffer.append(", inverse=");
    buffer.append(meta.getAssociationInverse(objTypeCode, ascTypeCode));
    return buffer.toString();
  }
}
