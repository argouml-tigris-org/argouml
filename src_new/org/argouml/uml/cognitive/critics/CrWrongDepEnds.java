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

// File: CrWrongDepEnds.java
// Classes: CrWrongDepEnds
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.tigris.gef.util.*;

import org.argouml.cognitive.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.deployment.ui.*;

/**
 * A critic to detect when in a deployment-diagram the supplier or the
 * client of a dependency is a mobject and inside a figComponent and
 * the other end is a mobject and inside a figComponentInstance
 **/

public class CrWrongDepEnds extends CrUML {

    public CrWrongDepEnds() {
	setHeadline("Wrong location-types");
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
     * If there are deps that are going from inside a FigComponent to
     * inside a FigComponentInstance the returned vector-set is not
     * null.  Then in the vector-set are the UMLDeploymentDiagram and
     * all FigDependencies with this characteristic and their
     * FigObjects described over the supplier and client.
     **/
    public VectorSet computeOffenders(UMLDeploymentDiagram dd) { 
	Vector figs = dd.getLayer().getContents();
	VectorSet offs = null;
	int size = figs.size();
	for (int i = 0; i < size; i++) {
	    Object obj = figs.elementAt(i);
	    if (!(obj instanceof FigDependency)) continue;
	    FigDependency fd = (FigDependency) obj;
	    if (!(fd.getOwner() instanceof MDependency)) continue;
	    MDependency dep = (MDependency) fd.getOwner();
	    Collection suppliers = dep.getSuppliers();
	    int count = 0;
	    if (suppliers != null && (suppliers.size() > 0)) {
		Iterator it = suppliers.iterator();
		while (it.hasNext()) {
		    MModelElement moe = (MModelElement) it.next();
		    if (moe instanceof MObject) {
			MObject obj_sup = (MObject) moe;
			if (obj_sup.getElementResidences() != null
			    && (obj_sup.getElementResidences().size() > 0))
			    count = count + 2;
			if (obj_sup.getComponentInstance() != null)
			    count = count + 1;
		    }
		}
	    }
	    Collection clients = dep.getClients();
	    if (clients != null && (clients.size() > 0)) {
		Iterator it = clients.iterator();
		while (it.hasNext()) {
		    MModelElement moe = (MModelElement) it.next();
		    if (moe instanceof MObject) {
			MObject obj_cli = (MObject) moe;
			if (obj_cli.getElementResidences() != null
			    && (obj_cli.getElementResidences().size() > 0))
			    count = count + 2;
			if (obj_cli.getComponentInstance() != null)
			    count = count + 1;
		    }
		}
	    }
	    if (count == 3) {
		if (offs == null) {
		    offs = new VectorSet();
		    offs.addElement(dd);
		}
		offs.addElement(fd);
		offs.addElement(fd.getSourcePortFig()); 
		offs.addElement(fd.getDestPortFig()); 
	    }
	}
	return offs;
    } 

} /* end class CrWrongDepEnds.java */

