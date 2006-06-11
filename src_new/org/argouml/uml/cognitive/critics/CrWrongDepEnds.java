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
import java.util.Iterator;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.ui.FigDependency;

/**
 * A critic to detect when in a deployment-diagram the supplier or the
 * client of a dependency is a mobject and inside a figComponent and
 * the other end is a mobject and inside a figComponentInstance.
 *
 * @author 5eichler
 */
public class CrWrongDepEnds extends CrUML {

    /**
     * The constructor.
     */
    public CrWrongDepEnds() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.PATTERNS);
    }

    /**
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     * java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(dm instanceof UMLDeploymentDiagram)) {
            return NO_PROBLEM;
        }
	UMLDeploymentDiagram dd = (UMLDeploymentDiagram) dm;
	ListSet offs = computeOffenders(dd);
	if (offs == null) {
            return NO_PROBLEM;
        }
	return PROBLEM_FOUND;
    }

    /**
     * @see org.argouml.cognitive.critics.Critic#toDoItem(
     * java.lang.Object, org.argouml.cognitive.Designer)
     */
    public ToDoItem toDoItem(Object dm, Designer dsgr) {
	UMLDeploymentDiagram dd = (UMLDeploymentDiagram) dm;
	ListSet offs = computeOffenders(dd);
	return new UMLToDoItem(this, offs, dsgr);
    }

    /**
     * @see org.argouml.cognitive.Poster#stillValid(
     * org.argouml.cognitive.ToDoItem, org.argouml.cognitive.Designer)
     */
    public boolean stillValid(ToDoItem i, Designer dsgr) {
	if (!isActive()) {
            return false;
        }
	ListSet offs = i.getOffenders();
	UMLDeploymentDiagram dd = (UMLDeploymentDiagram) offs.firstElement();
	//if (!predicate(dm, dsgr)) return false;
	ListSet newOffs = computeOffenders(dd);
	boolean res = offs.equals(newOffs);
	return res;
    }

    /**
     * If there are deps that are going from inside a FigComponent to
     * inside a FigComponentInstance the returned vector-set is not
     * null.  Then in the vector-set are the UMLDeploymentDiagram and
     * all FigDependencies with this characteristic and their
     * FigObjects described over the supplier and client.
     *
     * @param dd the diagram to check
     * @return the set of offenders
     */
    public ListSet computeOffenders(UMLDeploymentDiagram dd) {
	Collection figs = dd.getLayer().getContents();
	ListSet offs = null;
        Iterator figIter = figs.iterator();
	while (figIter.hasNext()) {
	    Object obj = figIter.next();
	    if (!(obj instanceof FigDependency)) {
                continue;
            }
	    FigDependency figDependency = (FigDependency) obj;
	    if (!(Model.getFacade().isADependency(figDependency.getOwner()))) {
                continue;
            }
	    Object dependency = figDependency.getOwner();
	    Collection suppliers = Model.getFacade().getSuppliers(dependency);
	    int count = 0;
	    if (suppliers != null && (suppliers.size() > 0)) {
		Iterator it = suppliers.iterator();
		while (it.hasNext()) {
		    Object moe = /*(MModelElement)*/ it.next();
		    if (Model.getFacade().isAObject(moe)) {
			Object objSup = /*(MObject)*/ moe;
			if (Model.getFacade().getElementResidences(objSup)
			        != null
			    && (Model.getFacade().getElementResidences(objSup)
			            .size() > 0)) {
			    count += 2;
                        }
			if (Model.getFacade().getComponentInstance(objSup)
			        != null) {
			    count++;
                        }
		    }
		}
	    }
	    Collection clients = Model.getFacade().getClients(dependency);
	    if (clients != null && (clients.size() > 0)) {
		Iterator it = clients.iterator();
		while (it.hasNext()) {
		    Object moe = /*(MModelElement)*/ it.next();
		    if (Model.getFacade().isAObject(moe)) {
			Object objCli = /*(MObject)*/ moe;
			if (Model.getFacade().getElementResidences(objCli)
			        != null
			    && (Model.getFacade().getElementResidences(objCli)
			            .size() > 0)) {
			    count += 2;
                        }
			if (Model.getFacade().getComponentInstance(objCli)
			        != null) {
			    count++;
                        }
		    }
		}
	    }
	    if (count == 3) {
		if (offs == null) {
		    offs = new ListSet();
		    offs.addElement(dd);
		}
		offs.addElement(figDependency);
		offs.addElement(figDependency.getSourcePortFig());
		offs.addElement(figDependency.getDestPortFig());
	    }
	}
	return offs;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -6587198606342935144L;
} /* end class CrWrongDepEnds.java */
