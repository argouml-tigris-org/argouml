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



// File: CrDisambigStateName.java
// Classes: CrDisambigStateName
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Iterator;
import javax.swing.Icon;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.critics.Critic;
import org.argouml.model.ModelFacade;
public class CrDisambigStateName extends CrUML {

    public CrDisambigStateName() {
	setHeadline("Choose a Unique Name for <ocl>self</ocl>");
	addSupportedDecision(CrUML.decNAMING);
	setKnowledgeTypes(Critic.KT_SYNTAX);
	addTrigger("name");
	addTrigger("parent");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(ModelFacade.isAState(dm))) return NO_PROBLEM;
	Object state = /*(MState)*/ dm;
	String myName = ModelFacade.getName(state);
	// TODO: should define a CompoundCritic
	if (myName == null || myName.equals("")) return NO_PROBLEM;
	String myNameString = myName;
	if (myNameString.length() == 0) return NO_PROBLEM;
	Collection pkgs = ModelFacade.getElementImports2(state);
	if (pkgs == null) return NO_PROBLEM;
	for (Iterator iter = pkgs.iterator(); iter.hasNext();) {
	    Object imp = /*(MElementImport)*/ iter.next();
	    Object ns = ModelFacade.getPackage(imp);
	    if (ns == null) return NO_PROBLEM;
	    Collection oes = ModelFacade.getOwnedElements(ns);
	    if (oes == null) return NO_PROBLEM;
	    Iterator enum = oes.iterator();
	    while (enum.hasNext()) {
		Object eo = /*(MElementImport)*/ enum.next();
		Object me = /*(MModelElement)*/ ModelFacade.getModelElement(eo);
		if (!(ModelFacade.isAClassifier(me))) continue;
		if (me == state) continue;
		String meName = ModelFacade.getName(me);
		if (meName == null || meName.equals("")) continue;
		if (meName.equals(myNameString)) return PROBLEM_FOUND;
	    }
	};
	return NO_PROBLEM;
    }

    public Icon getClarifier() {
	return ClClassName.TheInstance;
    }

} /* end class CrDisambigStateName */