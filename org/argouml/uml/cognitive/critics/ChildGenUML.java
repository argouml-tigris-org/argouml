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

// File: ChildGenUML.java
// Classes: ChildGenUML
// Original Author: jrobbins
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.util.*;

import org.argouml.kernel.*;

/** This class gives critics access to parts of the UML model of the
 *  design.  It defines a gen() function that returns the "children"
 *  of any given part of the UML model.  Basically, it goes from
 *  Project, to Models, to ModelElements.  Argo's critic Agency uses
 *  this to apply critics where appropriate.
 *
 * @see org.argouml.cognitive.critics.Agency */

public class ChildGenUML implements ChildGenerator {
  public static ChildGenUML SINGLETON = new ChildGenUML();

  /** Reply a java.util.Enumeration of the children of the given Object */
  public java.util.Enumeration gen(Object o) {
    if (o instanceof Project) {
      Project p = (Project) o;
      return new EnumerationComposite(p.getUserDefinedModels().elements(),
				      p.getDiagrams().elements());
    }

    if (o instanceof Diagram) {
      Vector figs = ((Diagram)o).getLayer().getContents();
      if (figs != null) return figs.elements();
    }

    if (o instanceof MPackage) {
      Vector ownedElements = new Vector(((MPackage)o).getOwnedElements());
      if (ownedElements != null) return ownedElements.elements();
    }

    if (o instanceof MElementImport) {
      MModelElement me = ((MElementImport)o).getModelElement();
      return new EnumerationSingle(me);  //wasteful!
    }

    if (o instanceof MModelElement) {
      Vector behavior = new Vector(((MModelElement)o).getBehaviors());
      if (behavior != null) behavior.elements();
    }

    // TODO: associationclasses fit both of the next 2 cases

    if (o instanceof MClassifier) {
      MClassifier cls = (MClassifier) o;
      EnumerationComposite res = new EnumerationComposite();
      res.addSub(new Vector(cls.getFeatures()));

      Vector sms = new Vector(cls.getBehaviors());
      MStateMachine sm = null;
      if (sms != null && sms.size() > 0) sm = (MStateMachine) sms.elementAt(0);
      if (sm != null) res.addSub(new EnumerationSingle(sm));
      return res;
    }

    if (o instanceof MAssociation) {
      MAssociation asc = (MAssociation) o;
      Vector assocEnds = new Vector(asc.getConnections());
      if (assocEnds != null) return assocEnds.elements();
      //TODO: MAssociationRole
    }





    // // needed?
    if (o instanceof MStateMachine) {
      MStateMachine sm = (MStateMachine) o;
      EnumerationComposite res = new EnumerationComposite();
      MState top = sm.getTop();
      if (top != null) res.addSub(new EnumerationSingle(top));
      res.addSub(new Vector(sm.getTransitions()));
      return res;
    }

    // needed?
    if (o instanceof MCompositeState) {
      MCompositeState cs = (MCompositeState) o;
      Vector substates = new Vector(cs.getSubvertices());
      if (substates != null) return substates.elements();
    }

    // tons more cases

    return EnumerationEmpty.theInstance();
  }
} /* end class ChildGenUML */

