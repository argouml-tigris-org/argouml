

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



// File: CrIllegalName.java
// Classes: CrIllegalName
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import javax.swing.Icon;
import org.argouml.cognitive.Designer;
import org.argouml.model.ModelFacade;
import ru.novosoft.uml.foundation.core.MModelElement;




/** A critic to detect whether a model element name is legally formed.
 */
public class CrIllegalName extends CrUML {

    public CrIllegalName() {
	setHeadline("Choose a Legal Name");
	addSupportedDecision(CrUML.decNAMING);
	addTrigger("name");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(ModelFacade.isAModelElement(dm))) return NO_PROBLEM;
	MModelElement me = (MModelElement) dm;
	String meName = me.getName();
	if (meName == null || meName.equals("")) return NO_PROBLEM;
	String nameStr = meName;
	int len = nameStr.length();

	// normal model elements are not allowed to have spaces,
	// but for States we make an exception
	for (int i = 0; i < len; i++) {
	    char c = nameStr.charAt(i);
	    if (!(Character.isLetterOrDigit(c) || c == '_' ||
		  (c == ' ' && ModelFacade.isAStateVertex(me))))
		return PROBLEM_FOUND;
	}
	return NO_PROBLEM;
    }

    public Icon getClarifier() {
	return ClClassName.TheInstance;
    }

} /* end class CrIllegalName */