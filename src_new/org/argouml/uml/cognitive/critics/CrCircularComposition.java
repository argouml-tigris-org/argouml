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



// File: CrCircularComposition.java
// Classes: CrCircularComposition
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;

import org.tigris.gef.util.*;

import org.apache.log4j.Category;
import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;
import org.argouml.uml.*;

/**  */

public class CrCircularComposition extends CrUML {
    protected static Category cat = Category.getInstance(CrCircularComposition.class);

  public CrCircularComposition() {
    setHeadline("Remove Circular Composition");
    addSupportedDecision(CrUML.decCONTAINMENT);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    // no good trigger
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MClassifier)) return NO_PROBLEM;
    MClassifier cls = (MClassifier) dm;
    VectorSet reach = (new VectorSet(cls)).reachable(GenCompositeClasses.SINGLETON);
    if (reach.contains(cls)) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

  public ToDoItem toDoItem(Object dm, Designer dsgr) {
    MClassifier cls = (MClassifier) dm;
    VectorSet offs = computeOffenders(cls);
    return new ToDoItem(this, offs, dsgr);
  }

  protected VectorSet computeOffenders(MClassifier dm) {
    VectorSet offs = new VectorSet(dm);
    VectorSet above = offs.reachable(GenCompositeClasses.SINGLETON);
    java.util.Enumeration enum = above.elements();
    while (enum.hasMoreElements()) {
      MClassifier cls2 = (MClassifier) enum.nextElement();
      VectorSet trans = (new VectorSet(cls2)).reachable(GenCompositeClasses.SINGLETON);
      if (trans.contains(dm)) offs.addElement(cls2);
    }
    return offs;
  }

  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    VectorSet offs = i.getOffenders();
    MClassifier dm = (MClassifier) offs.firstElement();
    if (!predicate(dm, dsgr)) return false;
    VectorSet newOffs = computeOffenders(dm);
    boolean res = offs.equals(newOffs);
      cat.debug("offs="+ offs.toString() +
  		       " newOffs="+ newOffs.toString() +
  		       " res = " + res);
    return res;
  }

  public Class getWizardClass(ToDoItem item) {
    return WizBreakCircularComp.class;
  }

} /* end class CrCircularComposition.java */

