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



// File: CrCircularInheritance.java.java
// Classes: CrCircularInheritance.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;

import uci.util.*;
import uci.argo.kernel.*;
import uci.uml.Foundation.Core.*;
import uci.uml.util.*;

/** Well-formedness rule [2] for GeneralizableElement. See page 31 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrCircularInheritance extends CrUML {

  public CrCircularInheritance() {
    setHeadline("Remove Circular Inheritance");
    sd("Inheritances relationships cannot have cycles. \n\n"+
       "A legal class inheritance hierarchy is needed for code generation \n"+
       "and the correctness of the design. \n\n"+
       "To fix this, use the FixIt button, or manually select one of the  \n"+
       "generalization arrows in the cycle and remove it.");

    addSupportedDecision(CrUML.decINHERITANCE);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof GeneralizableElement)) return NO_PROBLEM;
    GeneralizableElement ge = (GeneralizableElement) dm;
    Set reach = (new Set(ge)).reachable(new SuperclassGen());
    if (reach.contains(ge)) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

  public ToDoItem toDoItem(Object dm, Designer dsgr) {
    GeneralizableElement ge = (GeneralizableElement) dm;
    Set offs = computeOffenders(ge);
    return new ToDoItem(this, offs, dsgr);
  }

  protected Set computeOffenders(GeneralizableElement dm) {
    Set offs = new Set(dm);
    Set above = offs.reachable(new SuperclassGen());
    Enumeration enum = above.elements();
    while (enum.hasMoreElements()) {
      GeneralizableElement ge2 = (GeneralizableElement) enum.nextElement();
      Set trans = (new Set(ge2)).reachable(new SuperclassGen());
      if (trans.contains(dm)) offs.addElement(ge2);
    }
    return offs;
  }

  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    Set offs = i.getOffenders();
    GeneralizableElement dm = (GeneralizableElement) offs.firstElement();
    if (!predicate(dm, dsgr)) return false;
    Set newOffs = computeOffenders(dm);
    boolean res = offs.equals(newOffs);
//      System.out.println("offs="+ offs.toString() +
//  		       " newOffs="+ newOffs.toString() +
//  		       " res = " + res);
    return res;
  }
  
} /* end class CrCircularInheritance.java */

