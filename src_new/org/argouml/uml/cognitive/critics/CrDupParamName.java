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



// File: CrDupParamName.java
// Classes: CrDupParamName
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;
import org.argouml.model.uml.UmlHelper;
import org.argouml.uml.*;

/** Well-formedness rule [1] for MBehavioralFeature. See page 28 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrDupParamName extends CrUML {

    public CrDupParamName() {
	setHeadline("Duplicate Parameter Name");

	addSupportedDecision(CrUML.decCONTAINMENT);
	setKnowledgeTypes(Critic.KT_SYNTAX);
    }

    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(dm instanceof MBehavioralFeature)) return NO_PROBLEM;
	MBehavioralFeature bf = (MBehavioralFeature) dm;
	Vector params = new Vector(bf.getParameters());
	params.remove(UmlHelper.getHelper().getCore().getReturnParameter((MOperation) bf));
	Vector namesSeen = new Vector();
	Iterator enum = params.iterator();
	while (enum.hasNext()) {
	    MParameter p = (MParameter) enum.next();
	    String pName = p.getName();
	    if (pName == null || "".equals(pName)) continue;
	    String nameStr = pName;
	    if (nameStr == null || nameStr.length() == 0) continue;
	    if (namesSeen.contains(nameStr)) return PROBLEM_FOUND;
	    namesSeen.addElement(nameStr);
	}
	return NO_PROBLEM;
    }

} /* end class CrDupParamName.java */

