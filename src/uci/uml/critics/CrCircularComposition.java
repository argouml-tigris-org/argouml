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



// File: CrCircularComposition.java.java
// Classes: CrCircularComposition.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;

import uci.util.*;
import uci.argo.kernel.*;
import uci.uml.Foundation.Core.*;
import uci.uml.util.*;

/**  */

public class CrCircularComposition extends CrUML {

  public CrCircularComposition() {
    setHeadline("Remove Circular Composition");
    sd("Composition relationships (black diamonds) cannot have cycles. \n\n"+
       "A legal aggregation inheritance hierarchy is needed for code \n"+
       "generation and the correctness of the design. \n\n"+
       "To fix this, use the FixIt button, or manually select one of the  \n"+
       "associations in the cycle and remove it or change its aggregation \n"+
       "to something other than composite.");

    addSupportedDecision(CrUML.decCONTAINMENT);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof Classifier)) return NO_PROBLEM;
    Classifier cls = (Classifier) dm;
    Set reach = (new Set(cls)).reachable(new GenCompositeClasses());
    if (reach.contains(cls)) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

  public ToDoItem toDoItem(Object dm, Designer dsgr) {
    Classifier cls = (Classifier) dm;
    Set offs = computeOffenders(cls);
    return new ToDoItem(this, offs, dsgr);
  }

  protected Set computeOffenders(Classifier dm) {
    Set offs = new Set(dm);
    Set above = offs.reachable(new GenCompositeClasses());
    Enumeration enum = above.elements();
    while (enum.hasMoreElements()) {
      Classifier cls2 = (Classifier) enum.nextElement();
      Set trans = (new Set(cls2)).reachable(new GenCompositeClasses());
      if (trans.contains(dm)) offs.addElement(cls2);
    }
    return offs;
  }

  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    Set offs = i.getOffenders();
    Classifier dm = (Classifier) offs.firstElement();
    if (!predicate(dm, dsgr)) return false;
    Set newOffs = computeOffenders(dm);
    boolean res = offs.equals(newOffs);
//      System.out.println("offs="+ offs.toString() +
//  		       " newOffs="+ newOffs.toString() +
//  		       " res = " + res);
    return res;
  }
  
} /* end class CrCircularComposition.java */

