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

// File: CrNodesOverlap.java
// Classes: CrNodesOverlap
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.cognitive.critics;

import java.awt.Rectangle;
import java.util.Vector;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.uml.cognitive.critics.CrUML;
import org.argouml.uml.diagram.deployment.ui.FigObject;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.util.VectorSet;



/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */

public class CrNodesOverlap extends CrUML {

    /**
     * The constructor.
     * 
     */
    public CrNodesOverlap() {
	// TODO: {name} is not expanded for diagram objects
	setHeadline("Clean Up Diagram");
	addSupportedDecision(CrUML.decCLASS_SELECTION);
	addSupportedDecision(CrUML.decEXPECTED_USAGE);
	addSupportedDecision(CrUML.decSTATE_MACHINES);
	setKnowledgeTypes(Critic.KT_PRESENTATION);
    }

    ////////////////////////////////////////////////////////////////
    // critiquing API

    /**
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     * java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(dm instanceof Diagram)) return NO_PROBLEM;
	Diagram d = (Diagram) dm;

	// fixes bug #669. Sequencediagrams always overlap, so there is 
	// always a problem
	if (dm 
	    instanceof UMLSequenceDiagram)
	    return NO_PROBLEM;

	VectorSet offs = computeOffenders(d);
	if (offs == null) return NO_PROBLEM;
	return PROBLEM_FOUND;
    }


    /**
     * @see org.argouml.cognitive.critics.Critic#toDoItem(java.lang.Object, 
     * org.argouml.cognitive.Designer)
     */
    public ToDoItem toDoItem(Object dm, Designer dsgr) {
	Diagram d = (Diagram) dm;
	VectorSet offs = computeOffenders(d);
	return new ToDoItem(this, offs, dsgr);
    }

    /**
     * @see org.argouml.cognitive.Poster#stillValid(
     * org.argouml.cognitive.ToDoItem, org.argouml.cognitive.Designer)
     */
    public boolean stillValid(ToDoItem i, Designer dsgr) {
	if (!isActive()) return false;
	VectorSet offs = i.getOffenders();
	Diagram d = (Diagram) offs.firstElement();
	//if (!predicate(dm, dsgr)) return false;
	VectorSet newOffs = computeOffenders(d);
	boolean res = offs.equals(newOffs);
	return res;
    }

    /**
     * @param d the diagram
     * @return the set of offenders
     */
    public VectorSet computeOffenders(Diagram d) {
	//TODO: algorithm is n^2 in number of nodes
	Vector figs = new Vector(d.getLayer().getContents(null));
	int numFigs = figs.size();
	int numRects = 0;
	VectorSet offs = null;
	for (int i = 0; i < numFigs - 1; i++) {
	    Object oi = figs.elementAt(i);
	    if (!(oi instanceof FigNode)) continue;
	    FigNode fni = (FigNode) oi;
	    Rectangle boundsi = fni.getBounds();
	    for (int j = i + 1; j < numFigs; j++) {
		Object oj = figs.elementAt(j);
		if (!(oj instanceof FigNode)) continue;
		FigNode fnj = (FigNode) oj;
		if (fnj.intersects(boundsi)) {
		    if (!(d instanceof UMLDeploymentDiagram)) {   
			if (fni instanceof FigNodeModelElement) {
			    if (((FigNodeModelElement) fni).getEnclosingFig()
				== fnj)
				continue;
			}
			if (fnj instanceof FigNodeModelElement) {
			    if (((FigNodeModelElement) fnj).getEnclosingFig() 
				== fni)
				continue;
			}
		    }
		    // In DeploymentDiagrams the situation is not the
		    // same as in other diagrams only classes,
		    // interfaces and objects can intersect each other
		    // while they are not the EnclosingFig, so you
		    // have to prouve only these elements.
		    else {
			if ((!((fni instanceof  FigClass)
			       || (fni instanceof FigInterface) 
			       || (fni instanceof FigObject))) 
			    || (!((fnj instanceof  FigClass)
				  || (fnj instanceof FigInterface) 
				  || (fnj instanceof FigObject)))) 
			    continue;
		    }            
		    if (offs == null) {
			offs = new VectorSet();
			offs.addElement(d);
		    }
		    offs.addElement(fni);
		    offs.addElement(fnj);
		    break;
		}
	    }
	}
	return offs;
    }

} /* end class CrNodesOverlap */



