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

// File: CrCompInstanceWithoutNode.java
// Classes: CrComponentInstanceWithoutClassifier
// Original Author: 5eichler@informatik.uni-hamburg.de

package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Iterator;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.uml.cognitive.UMLToDoItem;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.deployment.ui.FigComponentInstance;
import org.argouml.uml.diagram.deployment.ui.FigMNodeInstance;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.tigris.gef.util.VectorSet;
/**
 * A critic to detect when there are component-instances that
 * are not inside a node-instance
 **/

public class CrComponentInstanceWithoutClassifier extends CrUML {

    /**
     * The constructor.
     * 
     */
    public CrComponentInstanceWithoutClassifier() {
	setHeadline("Set ComponentInstance-classifier");
	addSupportedDecision(CrUML.DEC_PATTERNS);
    }

    /**
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     * java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(dm instanceof UMLDeploymentDiagram)) return NO_PROBLEM;
	UMLDeploymentDiagram dd = (UMLDeploymentDiagram) dm;
	VectorSet offs = computeOffenders(dd); 
	if (offs == null) return NO_PROBLEM; 
	return PROBLEM_FOUND; 
    }

    /**
     * @see org.argouml.cognitive.critics.Critic#toDoItem(
     * java.lang.Object, org.argouml.cognitive.Designer)
     */
    public ToDoItem toDoItem(Object dm, Designer dsgr) { 
	UMLDeploymentDiagram dd = (UMLDeploymentDiagram) dm;
	VectorSet offs = computeOffenders(dd); 
	return new UMLToDoItem(this, offs, dsgr); 
    } 
 
    /**
     * @see org.argouml.cognitive.Poster#stillValid(
     * org.argouml.cognitive.ToDoItem, org.argouml.cognitive.Designer)
     */
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
     * If there are component-instances that have no enclosing FigMNodeInstance
     * the returned vector-set is not null. Then in the vector-set
     * are the UMLDeploymentDiagram and all FigComponentInstances with no
     * enclosing FigMNodeInstance
     *
     * @param deploymentDiagram the diagram to check
     * @return the set of offenders
     */
    public VectorSet computeOffenders(UMLDeploymentDiagram deploymentDiagram) { 

	Collection figs = deploymentDiagram.getLayer().getContents(null);
	VectorSet offs = null;
	int size = figs.size();
	boolean isNode = false;
        Iterator figIter = figs.iterator();
	while (figIter.hasNext()) {
	    Object obj = figIter.next();
	    if (obj instanceof FigMNodeInstance) isNode = true;
	}
        figIter = figs.iterator();
	while (figIter.hasNext()) {
	    Object obj = figIter.next();
	    if (!(obj instanceof FigComponentInstance)) continue;
	    FigComponentInstance figComponentInstance = 
	                                    (FigComponentInstance) obj;
	    if (figComponentInstance != null) {
		Object coi = /*(MComponentInstance)*/ 
		                            figComponentInstance.getOwner();
		if (coi != null) {
		    Collection col = ModelFacade.getClassifiers(coi);
		    if (col.size() > 0) continue;     
		}       
		if (offs == null) {
		    offs = new VectorSet();
		    offs.addElement(deploymentDiagram);
		}
		offs.addElement(figComponentInstance);
	    } else if (figComponentInstance.getEnclosingFig() != null 
		     && ((ModelFacade.getNodeInstance(
		                         figComponentInstance.getOwner()))
			 == null)) {
		if (offs == null) {
		    offs = new VectorSet();
		    offs.addElement(deploymentDiagram);
		}
		offs.addElement(figComponentInstance);
	    }
	}

	return offs; 
    } 

} /* end class CrComponentInstanceWithoutClassifier.java */