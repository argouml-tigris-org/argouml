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

import java.util.HashMap;
import java.util.Iterator;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Critic;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;

/**
 * Well-formedness rule [1] for MNamespace. See page 33 of UML 1.1
 * Semantics. OMG document ad/97-08-04.
 */
public class CrNameConflict extends CrUML {

    /**
     * The constructor.
     */
    public CrNameConflict() {
        setupHeadAndDesc();
        addSupportedDecision(UMLDecision.NAMING);
        setKnowledgeTypes(Critic.KT_SYNTAX);
        addTrigger("name");
        addTrigger("feature_name");
    }

    /**
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     * java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
        return computeOffenders(dm).size() > 1;
    }

    /**
     * @see org.argouml.cognitive.critics.Critic#toDoItem(
     * java.lang.Object, org.argouml.cognitive.Designer)
     */
    public ToDoItem toDoItem(Object dm, Designer dsgr) {
	ListSet offs = computeOffenders(dm);
	return new UMLToDoItem(this, offs, dsgr);
    }

    /**
     * @param dm the object to check
     * @return the set of offenders
     */
    protected ListSet computeOffenders(Object dm) {
        ListSet offenderResult = new ListSet();
        if (Model.getFacade().isANamespace(dm)) {
            Iterator it = Model.getFacade().getOwnedElements(dm).iterator();
            HashMap names = new HashMap();
            while (it.hasNext()) {
                Object name1Object = it.next();
                String name = Model.getFacade().getName(name1Object);
		if (name == null)
		    continue;
		if ("".equals(name))
		    continue;
                if (names.containsKey(name)) {
                    Object offender = names.get(name);
                    if (!offenderResult.contains(offender)) {
                        offenderResult.addElement(offender);
                    }
                    offenderResult.addElement(name1Object);
                }
                names.put(name, name1Object);
            }
        }
        return offenderResult;
    }


    /**
     * @see org.argouml.cognitive.Poster#stillValid(
     * org.argouml.cognitive.ToDoItem, org.argouml.cognitive.Designer)
     */
    public boolean stillValid(ToDoItem i, Designer dsgr) {
	if (!isActive()) return false;
	ListSet offs = i.getOffenders();

        // first element is e.g. the class, but we need to have its namespace
        // to recompute the offenders.
	Object f = offs.firstElement();
        Object ns = Model.getFacade().getNamespace(f);
	if (!predicate(ns, dsgr)) return false;
	ListSet newOffs = computeOffenders(ns);
	boolean res = offs.equals(newOffs);
	return res;
    }

} /* end class CrNameConflict.java */

