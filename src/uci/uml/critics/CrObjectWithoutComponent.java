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



// File: CrClassWithoutComponent.java
// Classes: CrClassWithoutComponent
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package uci.uml.critics;

import java.util.*;
import java.util.Enumeration; 
import uci.argo.kernel.*;
import uci.util.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import uci.gef.*;
import uci.util.*;
import uci.graph.*;
import uci.ui.*;
import uci.uml.visual.*; 


/**
 * A critic to detect when an object in a deployment-diagram
 * is not inside a component or a component-instance
 **/

public class CrObjectWithoutComponent extends CrUML {

  public CrObjectWithoutComponent() {
    setHeadline("Objects normally are inside components");
    sd(" In Deployment-diagrams objects are normally inside components or component-instances");
    addSupportedDecision(CrUML.decPATTERNS);
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof UMLDeploymentDiagram)) return NO_PROBLEM;
    UMLDeploymentDiagram dd = (UMLDeploymentDiagram) dm;
    VectorSet offs = computeOffenders(dd);
    if (offs == null) return NO_PROBLEM;
    return PROBLEM_FOUND;
  }

  public ToDoItem toDoItem(Object dm, Designer dsgr) {
    UMLDeploymentDiagram dd = (UMLDeploymentDiagram) dm;
    VectorSet offs = computeOffenders(dd);
    return new ToDoItem(this, offs, dsgr);
  }

  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    VectorSet offs = i.getOffenders();
    UMLDeploymentDiagram dd = (UMLDeploymentDiagram) offs.firstElement();
    //if (!predicate(dm, dsgr)) return false;
    VectorSet newOffs = computeOffenders(dd);
    boolean res = offs.equals(newOffs);
    return res;
  }

  /**
   * If there are objects that are not inside a component or a component-instance
   * the returned vector-set is not null. Then in the vector-set
   * are the UMLDeploymentDiagram and all FigObjects with no
   * enclosing FigComponent or FigComponentInstance
   **/
  public VectorSet computeOffenders(UMLDeploymentDiagram dd) { 
    Vector figs = dd.getLayer().getContents();
    VectorSet offs = null;
    int size = figs.size();
    for (int i=0; i<size; i++) {
      Object obj = figs.elementAt(i);
      if (!(obj instanceof FigObject)) continue;
      FigObject fo = (FigObject) obj;
      if (fo.getEnclosingFig() == null ||
         (!((fo.getEnclosingFig().getOwner() instanceof MComponentImpl) || 
            (fo.getEnclosingFig().getOwner() instanceof MComponentInstanceImpl)))) {
        if (offs == null) {
          offs = new VectorSet();
          offs.addElement(dd);
        }
        offs.addElement(fo);
      }
    }
    return offs;
  } 
 
} /* end class CrObjectWithoutComponent.java */

