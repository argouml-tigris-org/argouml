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

// File: CrUML.java
// Classes: CrUML
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

import org.tigris.gef.util.*;

import org.apache.log4j.Category;
import org.argouml.application.api.*;
import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;
import org.argouml.ocl.OCLEvaluator;
import java.io.*;
import java.util.*;

/** "Abstract" Critic subclass that captures commonalities among all
 *  critics in the UML domain.  This class also defines and registers
 *  the categories of design decisions that the critics can
 *  address.
 *
 * @see org.argouml.cognitive.Designer
 * @see org.argouml.cognitive.DecisionModel
 */

public class CrUML extends Critic {
    protected static Category cat = Category.getInstance(CrUML.class);
    
  public static final Decision decINHERITANCE = new
  Decision("decision.inheritance", 5);

  public static final Decision decCONTAINMENT = new
  Decision("decision.containment", 5);

  public static final Decision decPATTERNS = new
  Decision("decision.design-patterns", 5); //??

  public static final Decision decRELATIONSHIPS = new
  Decision("decision.relationships", 5);

  public static final Decision decSTORAGE = new
  Decision("decision.storage", 5);

  public static final Decision decBEHAVIOR = new
  Decision("decision.behavior", 5);

  public static final Decision decINSTANCIATION = new
  Decision("decision.instantiation", 5);

  public static final Decision decNAMING = new
  Decision("decision.naming", 5);

  public static final Decision decMODULARITY = new
  Decision("decision.modularity", 5);

  public static final Decision decCLASS_SELECTION = new
  Decision("decision.class-selection", 5);

  public static final Decision decEXPECTED_USAGE = new
  Decision("decision.expected-usage", 5);

  public static final Decision decMETHODS = new
  Decision("decision.methods", 5); //??

  public static final Decision decCODE_GEN = new
  Decision("decision.code-generation", 5); //??

  public static final Decision decPLANNED_EXTENSIONS = new
  Decision("decision.planned-extensions", 5);

  public static final Decision decSTEREOTYPES = new
  Decision("decision.stereotypes", 5);

  public static final Decision decSTATE_MACHINES = new
  Decision("decision.mstate-machines", 5);

  public static final String CRITICS_SITE = "http://www.ics.uci.edu/pub/arch/uml/critics/";

  /** Static initializer for this class. Called when the class is
   *  loaded (which is before any subclass instances are instanciated). */
  static {
    Designer d = Designer.theDesigner();
    d.startConsidering(decCLASS_SELECTION);
    d.startConsidering(decNAMING);
    d.startConsidering(decSTORAGE);
    d.startConsidering(decINHERITANCE);
    d.startConsidering(decCONTAINMENT);
    d.startConsidering(decPLANNED_EXTENSIONS);
    d.startConsidering(decSTATE_MACHINES);
    d.startConsidering(decPATTERNS);
    d.startConsidering(decRELATIONSHIPS);
    d.startConsidering(decINSTANCIATION);
    d.startConsidering(decMODULARITY);
    d.startConsidering(decEXPECTED_USAGE);
    d.startConsidering(decMETHODS);
    d.startConsidering(decCODE_GEN);
    d.startConsidering(decSTEREOTYPES);
  }



  ////////////////////////////////////////////////////////////////
  // constructor

  public CrUML() {
  }

  public void setResource(String key) {
        String head = Argo.localize("Cognitive",key + "_head");
        super.setHeadline(head);
        String desc = Argo.localize("Cognitive",key + "_desc");
        super.setDescription(desc);
  }



  /**
   *   Will be deprecated in good time
   */
  public final void setHeadline(String s) {
      //
      //   current implementation ignores the argument
      //     and triggers setResource()
      String className = getClass().getName();
      setResource(className.substring(className.lastIndexOf('.')+1));
  }



  public boolean predicate(Object dm, Designer dsgr) {
    Project p = ProjectBrowser.TheInstance.getProject();
    if (p.isInTrash(dm)) {
      cat.debug("in trash:" + dm);
      return NO_PROBLEM;
    }
    else return predicate2(dm, dsgr);
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    return super.predicate(dm, dsgr);
  }

    private String _cachedMoreInfoURL = null;
    public String getMoreInfoURL(VectorSet offenders, Designer dsgr) {
	if (_cachedMoreInfoURL == null) {
	    String clsName = getClass().getName();
	    clsName = clsName.substring(clsName.lastIndexOf(".") + 1);
	    _cachedMoreInfoURL = CRITICS_SITE + clsName + ".html";
	}
	return _cachedMoreInfoURL;
    }

  ////////////////////////////////////////////////////////////////
  // display related methods
  private static final String OCL_START = "<ocl>";
  private static final String OCL_END = "</ocl>";

    /**
     * Expand text with ocl brackets in it.
     * No recursive expansion.
     */
  public String expand(String res, VectorSet offs) {
      cat.debug("expanding: " + res);
    if (offs.size() == 0) return res;
    Object off1 = offs.firstElement();
    if (!(off1 instanceof MElement)) return res;

    StringBuffer beginning = new StringBuffer("");
    int matchPos = res.indexOf(OCL_START);

    // replace all occurances of OFFENDER with the name of the first offender
    while (matchPos != -1) {
      int endExpr = res.indexOf(OCL_END, matchPos + 1);
      //check if there is no OCL_END; if so, the critic expression s not correct and can not be expanded
      if (endExpr == -1) break; 
      if (matchPos > 0) beginning.append(res.substring(0, matchPos));
      String expr = res.substring(matchPos + OCL_START.length(), endExpr);
      String evalStr = OCLEvaluator.SINGLETON.evalToString(off1, expr);
      cat.debug("expr='" + expr + "' = '" + evalStr + "'");
      if (expr.endsWith("") && evalStr.equals(""))
	evalStr = "(anon)";
      beginning.append(evalStr);
      res = res.substring(endExpr + OCL_END.length());
      matchPos = res.indexOf(OCL_START);
    }
    if (beginning.length() == 0) // This is just to avoid creation of a new
	return res;		// string when not needed.
    else
	return beginning.append(res).toString();
  }

} /* end class CrUML */
