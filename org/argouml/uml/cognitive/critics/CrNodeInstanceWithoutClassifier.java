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

// File: CrNodeInstanceInsideElement.java
// Classes: CrNodeInstanceWithoutClassifier
// Original Author: jrobbins@5eichler@informatik.uni-hamburg.de

package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Vector;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.deployment.ui.FigMNodeInstance;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.tigris.gef.util.VectorSet;
/**
 * A critic to detect when there are node-instances that
 * are inside another element
 **/

public class CrNodeInstanceWithoutClassifier extends CrUML {

    public CrNodeInstanceWithoutClassifier() {
	setHeadline("Set NodeInstance-Classifier");
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
     * If there are node-instances that have an enclosing Fig
     * the returned vector-set is not null. Then in the vector-set
     * are the UMLDeploymentDiagram and all FigMNodeInstances with an
     * enclosing Fig
     **/
    public VectorSet computeOffenders(UMLDeploymentDiagram dd) { 
	Vector figs = dd.getLayer().getContents();
	VectorSet offs = null;
	int size = figs.size();
	for (int i = 0; i < size; i++) {
	    Object obj = figs.elementAt(i);
	    if (!(obj instanceof FigMNodeInstance)) continue;
	    FigMNodeInstance fn = (FigMNodeInstance) obj;
	    if (fn != null) {
		Object noi = /*(MNodeInstance)*/ fn.getOwner();
		if (noi != null) {
		    Collection col = ModelFacade.getClassifiers(noi);
		    if (col.size() > 0) continue;     
		}       
		if (offs == null) {
		    offs = new VectorSet();
		    offs.addElement(dd);
		}
		offs.addElement(fn);
	    }
	}
	return offs;
    } 


} /* end class CrNodeInstanceWithoutClassifier.java */
