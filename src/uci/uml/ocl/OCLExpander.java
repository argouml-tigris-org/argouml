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

package uci.uml.ocl;

import java.util.*;
import java.awt.*;
import java.io.*;
import java.lang.*;

public class OCLExpander {

  ////////////////////////////////////////////////////////////////
  // constants

  public static String OCL_START = "<ocl>";
  public static String OCL_END = "</ocl>";


  ////////////////////////////////////////////////////////////////
  // instance variables

  public Hashtable _templates = new Hashtable();
  public Hashtable _bindings = new Hashtable();
  public boolean _useXMLEscapes = true;

  ////////////////////////////////////////////////////////////////
  // constructor

  public OCLExpander(Hashtable templates) {
    _templates = templates;
  }

  ////////////////////////////////////////////////////////////////
  // template expansion

  public void expand(Writer w, Object target, String prefix, String suffix) {
    PrintWriter pw = new PrintWriter(w);
    if (target == null) return;

    Vector exprs = findTemplatesFor(target);
    String expr = null;
    int numExpr = (exprs == null) ? 0 : exprs.size();
    for (int i = 0; i < numExpr && expr == null; i++) {
      TemplateRecord tr = (TemplateRecord) exprs.elementAt(i);
      if (tr.guard == null || tr.guard.equals("")) { expr = tr.body; break; }
      _bindings.put("self", target);
      Vector results = OCLEvaluator.eval(_bindings, tr.guard);
      if (results.size() > 0 && !Boolean.FALSE.equals(results.elementAt(0))) {
	expr = tr.body;
	break;
      }
    }

    if (expr == null) {
      String s = target.toString();
      if (_useXMLEscapes) s = replaceWithXMLEscapes(s);
      pw.println(prefix + s + suffix);
      return;
    }

    StringTokenizer st = new StringTokenizer(expr, "\n\r");
    while (st.hasMoreTokens()) {
      String line = st.nextToken();
      expandLine(pw, line, target, prefix, suffix);
    }
    // System.out.println();
  }  // end of expand

  protected void expandLine(PrintWriter pw, String line, Object target,
			    String prefix, String suffix) {
    // if no embedded expression then output line else
    // then loop over all values of expr and call recursively for each resul

    int startPos = line.indexOf(OCL_START, 0);
    int endPos = line.indexOf(OCL_END, 0);

    if (startPos == -1 || endPos == -1) { // no embedded expr's
      pw.println(prefix + line + suffix);
      return;
    }

    // assume one embedded expression on line
    prefix = prefix + line.substring(0, startPos);
    String expr = line.substring(startPos + OCL_START.length(), endPos);
    suffix = line.substring(endPos + OCL_END.length()) + suffix;
    _bindings.put("self", target);
    Vector results = OCLEvaluator.eval(_bindings, expr);
    int size = results.size();
    for (int i = 0; i < size; i++) {
      expand(pw, results.elementAt(i), prefix, suffix);
    }
  }


  /** Find the Vector of templates that could apply to this target
   *  object.  That includes the templates for its class and all
   *  superclasses.  Needs-More-Work: should cache. */
  public Vector findTemplatesFor(Object target) {
    Vector res = null;
    boolean shared = true;
    for (Class c = target.getClass(); c != null; c = c.getSuperclass()) {
      Vector temps = (Vector) _templates.get(c);
      if (temps == null) continue;
      if (res == null) {
	// if only one template applies, return it
	res = temps;
      }
      else {
	// if another template also applies, merge the two vectors,
	// but leave the original unchanged
	if (shared) {
	  shared = false;
	  Vector newRes = new Vector();
	  for (int i = 0; i < res.size(); i++)
	    newRes.addElement(res.elementAt(i));
	  res = newRes;
	}
	for (int j = 0; j < temps.size(); j++)
	  res.addElement(temps.elementAt(j));
      }
    }
    return res;
  }

  protected String replaceWithXMLEscapes(String s) {
    s = replaceAll(s, "&", "&amp;");
    s = replaceAll(s, "<", "&lt;");
    s = replaceAll(s, ">", "&gt;");
    return s;
  }

  protected String replaceAll(String s, String pat, String rep) {
    int index = s.indexOf(pat);
    int patLen = pat.length();
    int repLen = rep.length();
    while (index != -1) {
      s = s.substring(0, index) + rep + s.substring(index+patLen);
      index = s.indexOf(pat, index + repLen);
    }
    return s;
  }
} /* end class OCLExpander */
