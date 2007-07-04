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

import org.apache.log4j.Logger;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.GenCompositeClasses;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;

/**
 * @author jrobbins@ics.uci.edu
 */
public class CrCircularComposition extends CrUML {
    private static final Logger LOG =
	Logger.getLogger(CrCircularComposition.class);

    /**
     * The constructor.
     */
    public CrCircularComposition() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.CONTAINMENT);
	setKnowledgeTypes(Critic.KT_SYNTAX);
	setPriority(ToDoItem.LOW_PRIORITY);
	// no good trigger
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(Model.getFacade().isAClassifier(dm))) {
            return NO_PROBLEM;
        }
	ListSet reach =
	    (new ListSet(dm)).reachable(GenCompositeClasses.getSINGLETON());
	if (reach.contains(dm)) {
            return PROBLEM_FOUND;
        }
	return NO_PROBLEM;
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#toDoItem(java.lang.Object,
     *      org.argouml.cognitive.Designer)
     */
    public ToDoItem toDoItem(Object dm, Designer dsgr) {

        ListSet offs = computeOffenders(dm);
	return new UMLToDoItem(this, offs, dsgr);
    }

    /**
     * @param dm is the UML entity that is being checked
     * @return the list of offenders
     */
    protected ListSet computeOffenders(Object dm) {
	ListSet offs = new ListSet(dm);
	ListSet above = offs.reachable(GenCompositeClasses.getSINGLETON());
        for (Object cls2 : above) {
	    ListSet trans = (new ListSet(cls2))
	        .reachable(GenCompositeClasses.getSINGLETON());
	    if (trans.contains(dm)) {
                offs.add(cls2);
            }
	}
	return offs;
    }

    /*
     * @see org.argouml.cognitive.Poster#stillValid(
     *      org.argouml.cognitive.ToDoItem, org.argouml.cognitive.Designer)
     */
    public boolean stillValid(ToDoItem i, Designer dsgr) {
	if (!isActive()) return false;
	ListSet offs = i.getOffenders();
	Object dm =  offs.get(0);
	if (!predicate(dm, dsgr)) return false;
	ListSet newOffs = computeOffenders(dm);
	boolean res = offs.equals(newOffs);
	LOG.debug("offs=" + offs.toString()
		  + " newOffs=" + newOffs.toString()
		  + " res = " + res);
	return res;
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#getWizardClass(org.argouml.cognitive.ToDoItem)
     */
    public Class getWizardClass(ToDoItem item) {
	return WizBreakCircularComp.class;
    }

}
