// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

// File: CrDupParamName.java
// Classes: CrDupParamName
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.Iterator;
import java.util.Vector;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.critics.Critic;
import org.argouml.model.ModelFacade;

/** Well-formedness rule [1] for MBehavioralFeature. See page 28 of UML 1.1
 *  Semantics. OMG document ad/97-08-04.
 */
public class CrDupParamName extends CrUML {

    /**
     * The constructor.
     * 
     */
    public CrDupParamName() {
	setHeadline("Duplicate Parameter Name");

	addSupportedDecision(CrUML.decCONTAINMENT);
	setKnowledgeTypes(Critic.KT_SYNTAX);
    }

    /**
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     * java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!ModelFacade.isABehavioralFeature(dm)) {
	    return NO_PROBLEM;
	}

	Object bf = /*(MBehavioralFeature)*/ dm;
	Vector namesSeen = new Vector();
	Iterator params = ModelFacade.getParameters(bf).iterator();
	while (params.hasNext()) {
	    Object p = /*(MParameter)*/ params.next();
	    if (ModelFacade.isReturn(p)) {
		continue;
	    }

	    String pName = ModelFacade.getName(p);
	    if (pName == null || "".equals(pName)) {
		continue;
	    }

	    if (namesSeen.contains(pName)) {
		return PROBLEM_FOUND;
	    }

	    namesSeen.addElement(pName);
	}

	return NO_PROBLEM;
    }

} /* end class CrDupParamName.java */
