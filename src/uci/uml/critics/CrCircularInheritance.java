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



// File: CrCircularInheritance.java
// Classes: CrCircularInheritance
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
    setHeadline("Remove <ocl>self</ocl>'s Circular Inheritance");
    sd("Inheritances relationships cannot have cycles. \n\n"+
       "A legal class inheritance hierarchy is needed for code generation "+
       "and the correctness of the design. \n\n"+
       "To fix this, use the \"Next>\" button, or manually select one of the  "+
       "generalization arrows in the cycle and remove it.");
    setPriority(ToDoItem.HIGH_PRIORITY);
    addSupportedDecision(CrUML.decINHERITANCE);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("generalization");
    // no need for trigger on "specialization"
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof GeneralizableElement)) return NO_PROBLEM;
    GeneralizableElement ge = (GeneralizableElement) dm;
    VectorSet reach = (new VectorSet(ge)).reachable(new SuperclassGen());
    if (reach.contains(ge)) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

  public ToDoItem toDoItem(Object dm, Designer dsgr) {
    GeneralizableElement ge = (GeneralizableElement) dm;
    VectorSet offs = computeOffenders(ge);
    return new ToDoItem(this, offs, dsgr);
  }

  protected VectorSet computeOffenders(GeneralizableElement dm) {
    VectorSet offs = new VectorSet(dm);
    VectorSet above = offs.reachable(new SuperclassGen());
    Enumeration enum = above.elements();
    while (enum.hasMoreElements()) {
      GeneralizableElement ge2 = (GeneralizableElement) enum.nextElement();
      VectorSet trans = (new VectorSet(ge2)).reachable(new SuperclassGen());
      if (trans.contains(dm)) offs.addElement(ge2);
    }
    return offs;
  }

  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    VectorSet offs = i.getOffenders();
    GeneralizableElement dm = (GeneralizableElement) offs.firstElement();
    if (!predicate(dm, dsgr)) return false;
    VectorSet newOffs = computeOffenders(dm);
    boolean res = offs.equals(newOffs);
//      System.out.println("offs="+ offs.toString() +
//  		       " newOffs="+ newOffs.toString() +
//  		       " res = " + res);
    return res;
  }

} /* end class CrCircularInheritance.java */

