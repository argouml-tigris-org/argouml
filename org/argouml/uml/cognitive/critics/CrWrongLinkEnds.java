
// $Id$
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

package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.static_structure.ui.FigLink;
import org.tigris.gef.util.VectorSet;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MLinkEnd;

/**
 * A critic to detect when in a deployment-diagram
 * the FigObject of the first MLinkEnd is inside a FigComponent
 * and the FigObject of the other MLinkEnd is inside a FigComponentInstance
 **/

public class CrWrongLinkEnds extends CrUML {

    public CrWrongLinkEnds() {
	setHeadline("LinkEnds have not the same locations");
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
     * If there are links that are going from inside a FigComponent to
     * inside a FigComponentInstance the returned vector-set is not
     * null.  Then in the vector-set are the UMLDeploymentDiagram and
     * all FigLinks with this characteristic and their FigObjects
     * described over the links MLinkEnds
     **/
    public VectorSet computeOffenders(UMLDeploymentDiagram dd) { 
	Vector figs = dd.getLayer().getContents();
	VectorSet offs = null;
	int size = figs.size();
	for (int i = 0; i < size; i++) {
	    Object obj = figs.elementAt(i);
	    if (!(obj instanceof FigLink)) continue;
	    FigLink fl = (FigLink) obj;
	    if (!(ModelFacade.isALink(fl.getOwner()))) continue;
	    MLink link = (MLink) fl.getOwner();
	    Collection ends = link.getConnections();
	    if (ends != null && (ends.size() > 0)) {
		int count = 0;
		Iterator it = ends.iterator();
		while (it.hasNext()) {
		    MLinkEnd le = (MLinkEnd) it.next();
		    if (le.getInstance().getElementResidences() != null
			&& (le.getInstance().getElementResidences().size() > 0))
			count = count + 2;
		    if (le.getInstance().getComponentInstance() != null)
			count = count + 1;
		}
		if (count == 3) {
		    if (offs == null) {
			offs = new VectorSet();
			offs.addElement(dd);
		    }
		    offs.addElement(fl);
		    offs.addElement(fl.getSourcePortFig()); 
		    offs.addElement(fl.getDestPortFig()); 
		}
	    }
	}
	return offs;
    } 

} /* end class CrWrongLinkEnds.java */
