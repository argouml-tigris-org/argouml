
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

// File: CrSubclassReference.javoa
// Classes: CrSubclassReference
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;

import org.tigris.gef.util.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;
import org.argouml.uml.GenDescendantClasses;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */

public class CrSubclassReference extends CrUML {

    public CrSubclassReference() {
	setHeadline("Remove Reference to Specific Subclass");
	addSupportedDecision(CrUML.decRELATIONSHIPS);
	addSupportedDecision(CrUML.decPLANNED_EXTENSIONS);
	setKnowledgeTypes(Critic.KT_SEMANTICS);
	addTrigger("specialization");
	addTrigger("associationEnd");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(org.argouml.model.ModelFacade.isAClass(dm))) return NO_PROBLEM;
	MClass cls = (MClass) dm;
	VectorSet offs = computeOffenders(cls);
	if (offs != null) return PROBLEM_FOUND;
	return NO_PROBLEM;
    }

    public ToDoItem toDoItem(Object dm, Designer dsgr) {
	MClassifier cls = (MClassifier) dm;
	VectorSet offs = computeOffenders(cls);
	return new ToDoItem(this, offs, dsgr);
    }

    public boolean stillValid(ToDoItem i, Designer dsgr) {
	if (!isActive()) return false;
	VectorSet offs = i.getOffenders();
	MClassifier dm = (MClassifier) offs.firstElement();
	//if (!predicate(dm, dsgr)) return false;
	VectorSet newOffs = computeOffenders(dm);
	boolean res = offs.equals(newOffs);
	return res;
    }

    public VectorSet computeOffenders(MClassifier cls) {
	Collection asc = cls.getAssociationEnds();
	if (asc == null || asc.size() == 0) return null;

	java.util.Enumeration descendEnum =
	    GenDescendantClasses.SINGLETON.gen(cls);
	if (!descendEnum.hasMoreElements()) return null;
	VectorSet descendants = new VectorSet();
	while (descendEnum.hasMoreElements())
	    descendants.addElement(descendEnum.nextElement());

	//TODO: GenNavigableClasses?
	int nAsc = asc.size();
	VectorSet offs = null;
	for (Iterator iter = asc.iterator(); iter.hasNext();) {
	    MAssociationEnd ae = (MAssociationEnd) iter.next();
	    MAssociation a = ae.getAssociation();
	    List conn = a.getConnections();
	    if (conn.size() != 2) continue;
	    MAssociationEnd otherEnd = (MAssociationEnd) conn.get(0);
	    if (ae == conn.get(0))
		otherEnd = (MAssociationEnd) conn.get(1);
	    if (!otherEnd.isNavigable()) continue;
	    MClassifier otherCls = otherEnd.getType();
	    if (descendants.contains(otherCls)) {
		if (offs == null) {
		    offs = new VectorSet();
		    offs.addElement(cls);
		}
		offs.addElement(a);
		offs.addElement(otherCls);
	    }
	}
	return offs;
    }

} /* end class CrSubclassReference */
