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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Critic;
import org.argouml.model.Model;
import org.argouml.uml.GenDescendantClasses;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;

/**
 * A critic to detect when a class can never have instances (of
 * itself of any subclasses).
 *
 * @author jrobbins
 */
public class CrSubclassReference extends CrUML {

    /**
     * The constructor.
     */
    public CrSubclassReference() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.RELATIONSHIPS);
	addSupportedDecision(UMLDecision.PLANNED_EXTENSIONS);
	setKnowledgeTypes(Critic.KT_SEMANTICS);
	addTrigger("specialization");
	addTrigger("associationEnd");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(Model.getFacade().isAClass(dm))) return NO_PROBLEM;
	Object cls = /*(MClass)*/ dm;
	ListSet offs = computeOffenders(cls);
	if (offs != null) return PROBLEM_FOUND;
	return NO_PROBLEM;
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#toDoItem(java.lang.Object,
     *      org.argouml.cognitive.Designer)
     */
    public ToDoItem toDoItem(Object dm, Designer dsgr) {
	Object cls = /*(MClassifier)*/ dm;
	ListSet offs = computeOffenders(cls);
	return new UMLToDoItem(this, offs, dsgr);
    }

    /*
     * @see org.argouml.cognitive.Poster#stillValid(
     *      org.argouml.cognitive.ToDoItem, org.argouml.cognitive.Designer)
     */
    public boolean stillValid(ToDoItem i, Designer dsgr) {
	if (!isActive()) return false;
	ListSet offs = i.getOffenders();
	Object dm = /*(MClassifier)*/ offs.firstElement();
	//if (!predicate(dm, dsgr)) return false;
	ListSet newOffs = computeOffenders(dm);
	boolean res = offs.equals(newOffs);
	return res;
    }

    /**
     * @param cls is the UML entity that is being checked.
     * @return the list of offenders
     */
    public ListSet computeOffenders(Object cls) {
	Collection asc = Model.getFacade().getAssociationEnds(cls);
	if (asc == null || asc.size() == 0) return null;

	Enumeration descendEnum =
	    GenDescendantClasses.getSINGLETON().gen(cls);
	if (!descendEnum.hasMoreElements()) return null;
	ListSet descendants = new ListSet();
	while (descendEnum.hasMoreElements())
	    descendants.addElement(descendEnum.nextElement());

	//TODO: GenNavigableClasses?
	ListSet offs = null;
	for (Iterator iter = asc.iterator(); iter.hasNext();) {
	    Object ae = /*(MAssociationEnd)*/ iter.next();
	    Object a = Model.getFacade().getAssociation(ae);
	    List conn = new ArrayList(Model.getFacade().getConnections(a));
	    if (conn.size() != 2) continue;
	    Object otherEnd = /*(MAssociationEnd)*/ conn.get(0);
	    if (ae == conn.get(0))
		otherEnd = /*(MAssociationEnd)*/ conn.get(1);
	    if (!Model.getFacade().isNavigable(otherEnd)) continue;
	    Object otherCls = Model.getFacade().getType(otherEnd);
	    if (descendants.contains(otherCls)) {
		if (offs == null) {
		    offs = new ListSet();
		    offs.addElement(cls);
		}
		offs.addElement(a);
		offs.addElement(otherCls);
	    }
	}
	return offs;
    }

} /* end class CrSubclassReference */
