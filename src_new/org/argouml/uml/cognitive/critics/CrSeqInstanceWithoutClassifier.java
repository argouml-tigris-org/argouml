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

import java.util.Collection;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.ui.FigNodeModelElement;

/**
 * A critic to detect when an object in a deployment-diagram
 * is not inside a component or a component-instance.
 *
 * @author 5eichler
 */
public class CrSeqInstanceWithoutClassifier extends CrUML {

    /**
     * The constructor.
     */
    public CrSeqInstanceWithoutClassifier() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.PATTERNS);
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    @Override
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(dm instanceof UMLSequenceDiagram)) {
            return NO_PROBLEM;
        }
	UMLSequenceDiagram sd = (UMLSequenceDiagram) dm;
	ListSet offs = computeOffenders(sd);
	if (offs == null) return NO_PROBLEM;
	return PROBLEM_FOUND;
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#toDoItem( java.lang.Object,
     *      org.argouml.cognitive.Designer)
     */
    @Override
    public ToDoItem toDoItem(Object dm, Designer dsgr) {
	UMLSequenceDiagram sd = (UMLSequenceDiagram) dm;
	ListSet offs = computeOffenders(sd);
	return new UMLToDoItem(this, offs, dsgr);
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
	UMLSequenceDiagram sd = (UMLSequenceDiagram) offs.get(0);
	//if (!predicate(dm, dsgr)) return false;
	ListSet newOffs = computeOffenders(sd);
	boolean res = offs.equals(newOffs);
	return res;
    }

    /**
     * If there are instances that have no classifiers they belong to
     * the returned vector-set is not null. Then in the vector-set
     * are the UMLSequenceDiagram and all FigObjects, FigComponentInstances
     * and FigMNodeInstances with no classifier.
     *
     * @param sd the diagram to check
     * @return the set of offenders
     */
    public ListSet computeOffenders(UMLSequenceDiagram sd) {
	Collection figs = sd.getLayer().getContents();
	ListSet offs = null;
        for (Object obj : figs) {
	    if (!(obj instanceof FigNodeModelElement)) {
                continue;
            }
	    FigNodeModelElement fn = (FigNodeModelElement) obj;
	    if (fn != null && (Model.getFacade().isAInstance(fn.getOwner()))) {
		Object minst = fn.getOwner();
		if (minst != null) {
		    Collection col = Model.getFacade().getClassifiers(minst);
		    if (col.size() > 0) {
                        continue;
                    }
		}
		if (offs == null) {
		    offs = new ListSet();
		    offs.add(sd);
		}
		offs.add(fn);
	    }
	}
	return offs;
    }

}
