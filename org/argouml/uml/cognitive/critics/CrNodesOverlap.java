// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.cognitive.critics;

import java.awt.Rectangle;
import java.util.List;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.diagram.deployment.ui.FigObject;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.FigNode;

/**
 * A critic to detect when a modelelements overlap.
 *
 * @author jrobbins
 */
public class CrNodesOverlap extends CrUML {

    /**
     * The constructor.
     */
    public CrNodesOverlap() {
	// TODO: {name} is not expanded for diagram objects
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.CLASS_SELECTION);
	addSupportedDecision(UMLDecision.EXPECTED_USAGE);
	addSupportedDecision(UMLDecision.STATE_MACHINES);
	setKnowledgeTypes(Critic.KT_PRESENTATION);
    }

    ////////////////////////////////////////////////////////////////
    // critiquing API

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    @Override
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(dm instanceof Diagram)) {
            return NO_PROBLEM;
        }
	Diagram d = (Diagram) dm;

	// fixes bug #669. Sequencediagrams always overlap, so they shall
	// never report a problem
	if (dm instanceof UMLSequenceDiagram) {
            return NO_PROBLEM;
        }

	ListSet offs = computeOffenders(d);
	if (offs == null) return NO_PROBLEM;
	return PROBLEM_FOUND;
    }


    /*
     * @see org.argouml.cognitive.critics.Critic#toDoItem(java.lang.Object,
     *      org.argouml.cognitive.Designer)
     */
    @Override
    public ToDoItem toDoItem(Object dm, Designer dsgr) {
	Diagram d = (Diagram) dm;
	ListSet offs = computeOffenders(d);
	return new ToDoItem(this, offs, dsgr);
    }

    /*
     * @see org.argouml.cognitive.Poster#stillValid(
     *      org.argouml.cognitive.ToDoItem, org.argouml.cognitive.Designer)
     */
    @Override
    public boolean stillValid(ToDoItem i, Designer dsgr) {
	if (!isActive()) {
            return false;
        }
	ListSet offs = i.getOffenders();
	Diagram d = (Diagram) offs.get(0);
	//if (!predicate(dm, dsgr)) return false;
	ListSet newOffs = computeOffenders(d);
	boolean res = offs.equals(newOffs);
	return res;
    }

    /**
     * @param d the diagram
     * @return the set of offenders
     */
    public ListSet computeOffenders(Diagram d) {
	//TODO: algorithm is n^2 in number of nodes
	List figs = d.getLayer().getContents();
	int numFigs = figs.size();
	ListSet offs = null;
	for (int i = 0; i < numFigs - 1; i++) {
	    Object oi = figs.get(i);
	    if (!(oi instanceof FigNode)) {
                continue;
            }
	    FigNode fni = (FigNode) oi;
	    Rectangle boundsi = fni.getBounds();
	    for (int j = i + 1; j < numFigs; j++) {
		Object oj = figs.get(j);
		if (!(oj instanceof FigNode)) {
                    continue;
                }
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
			offs = new ListSet();
			offs.add(d);
		    }
		    offs.add(fni);
		    offs.add(fnj);
		    break;
		}
	    }
	}
	return offs;
    }

} 



