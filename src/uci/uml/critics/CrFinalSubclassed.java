// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.



// File: CrFinalSubclassed.java.java
// Classes: CrFinalSubclassed.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;

/** Well-formedness rule [2] for GeneralizableElement. See page 31 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrFinalSubclassed extends CrUML {

  public CrFinalSubclassed() {
    setHeadline("This class is subclassed from a class that is marked final");
    sd("In Java, the keyword 'final' indicates that a class is not intended \n"+
       "to have subclasses. \n\n"+
       "A well thought-out class inheritance hierarchy that conveys and \n"+
       "supports intended extensions is an important part of achieving \n"+
       "an understandable and maintainable design.\n\n"+
       "To fix this, use the FixIt button, or manually select the class and \n"+
       "change its base class, or select the base class and use the properties \n"+
       "tab to remove the 'final' keyword.");

    addSupportedDecision(CrUML.decINHERITANCE);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof GeneralizableElement)) return NO_PROBLEM;
    GeneralizableElement ge = (GeneralizableElement) dm;
    Vector bases = ge.getGeneralization();
    if (bases == null) return NO_PROBLEM;
    java.util.Enumeration enum = bases.elements();
    while (enum.hasMoreElements()) {
      Generalization g = (Generalization) enum.nextElement();
      GeneralizableElement base = g.getSupertype();
      if (base.getIsLeaf()) return PROBLEM_FOUND;
    }
    return NO_PROBLEM;
  }

} /* end class CrFinalSubclassed.java */

