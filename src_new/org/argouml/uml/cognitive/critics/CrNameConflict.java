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



// File: CrNameConflict.java
// Classes: CrNameConflict
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.HashMap;
import java.util.Iterator;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.uml.cognitive.UMLToDoItem;
import org.argouml.cognitive.critics.Critic;
import org.argouml.cognitive.ui.Wizard;
import org.argouml.model.ModelFacade;
import org.tigris.gef.util.VectorSet;


/** Well-formedness rule [1] for MNamespace. See page 33 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrNameConflict extends CrUML {

    /**
     * The constructor.
     * 
     */
    public CrNameConflict() {
        setHeadline("Revise Name to Avoid Conflict");
        addSupportedDecision(CrUML.DEC_NAMING);
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
	VectorSet offs = computeOffenders(dm);
	return new UMLToDoItem(this, offs, dsgr);
    }

    /**
     * @param dm the object to check
     * @return the set of offenders
     */
    protected VectorSet computeOffenders(Object dm) {
        VectorSet offs = new VectorSet();
        if (ModelFacade.isANamespace(dm)) {
            Iterator it = ModelFacade.getOwnedElements(dm).iterator();
            HashMap names = new HashMap(); 
            while (it.hasNext()) { 
                Object name1Object = it.next();
                String name = ModelFacade.getName(name1Object);
		if (name == null)
		    continue;
		if ("".equals(name))
		    continue;
                if (names.containsKey(name)) {
                    Object off = names.get(name);
                    offs.addElement(off);
                    offs.addElement(name1Object);
                    break;
                }
                names.put(name, name1Object); 
            } 
        } 
        return offs;
    }

    
    /**
     * @see org.argouml.cognitive.Poster#stillValid(
     * org.argouml.cognitive.ToDoItem, org.argouml.cognitive.Designer)
     */
    public boolean stillValid(ToDoItem i, Designer dsgr) {
	if (!isActive()) return false;
	VectorSet offs = i.getOffenders();
        
        // first element is e.g. the class, but we need to have its namespace
        // to recompute the offenders.
	Object f = offs.firstElement();
        Object ns = ModelFacade.getNamespace(f);
	if (!predicate(ns, dsgr)) return false;
	VectorSet newOffs = computeOffenders(ns);
	boolean res = offs.equals(newOffs);
	return res;
    }
 
    /**
     * @see org.argouml.cognitive.critics.Critic#initWizard(org.argouml.kernel.Wizard)
     */
    public void initWizard(Wizard w) {
        if (w instanceof WizMEName) {
            ToDoItem item = (ToDoItem) w.getToDoItem();
            Object me = item.getOffenders().firstElement();
            String sug = ModelFacade.getName(me);
            String ins = "Change the name to something different.";
            ((WizMEName) w).setInstructions(ins);
            ((WizMEName) w).setSuggestion(sug);
            ((WizMEName) w).setMustEdit(true);
        }
    }

    /**
     * @see org.argouml.cognitive.critics.Critic#getWizardClass(org.argouml.cognitive.ToDoItem)
     */
    public Class getWizardClass(ToDoItem item) { return WizMEName.class; }


} /* end class CrNameConflict.java */

