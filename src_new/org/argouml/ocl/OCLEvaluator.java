// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.ocl;

import java.util.*;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.data_types.MExpression;

// stereotype <<utility>>
public class OCLEvaluator extends org.tigris.gef.ocl.OCLEvaluator {

  protected OCLEvaluator() {
  }

  public static OCLEvaluator SINGLETON = new OCLEvaluator();

  public synchronized String evalToString(Object self, String expr) {
    String res = null;
    if (GET_NAME_EXPR_1.equals(expr) && self instanceof MModelElement) {
      res = ((MModelElement)self).getName();
      if (res == null || "".equals(res)) res = "(anon)";
    }
    if (GET_NAME_EXPR_2.equals(expr) && self instanceof MModelElement) {
      res = ((MModelElement)self).getName();
      if (res == null || "".equals(res)) res = "(anon)";
    }
    if (GET_OWNER_EXPR.equals(expr) && self instanceof MFeature) {
      res = ((MFeature)self).getOwner().getName();
      if (res == null || "".equals(res)) res = "(anon)";
    }
    if (res == null) res = evalToString(self, expr, ", ");
    return res;
  }

  public synchronized String evalToString(Object self,
						 String expr, String sep) {
    _scratchBindings.put("self", self);
    java.util.List values = eval(_scratchBindings, expr);
    _strBuf.setLength(0);
    Iterator iter = values.iterator();
    while(iter.hasNext()) {
      Object v = iter.next();
      if (v instanceof MModelElement) {
	v = ((MModelElement)v).getName();
	if ("".equals(v)) v = "(anon)";
      }
      if (v instanceof MExpression) {
	v = ((MExpression)v).getBody();
	if ("".equals(v)) v = "(unspecified)";
      }
      if (! "".equals(v)) {
	_strBuf.append(v);
	if (iter.hasNext()) _strBuf.append(sep);
      }
    }
    return _strBuf.toString();
  }

}  // end of OCLEvaluator
