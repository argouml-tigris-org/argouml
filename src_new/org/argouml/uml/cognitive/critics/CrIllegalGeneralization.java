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



// File: CrIllegalGeneralization.java
// Classes: CrIllegalGeneralization
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import org.argouml.cognitive.Designer;
import org.argouml.model.ModelFacade;
/** Well-formedness rule [1] for MGeneralization. See page 32 of UML 1.1
 *  Semantics. OMG document ad/97-08-04.
 *  This critic checks that the parent and child in a generalization are
 *  of the same metatype.
 */

public class CrIllegalGeneralization extends CrUML {

    public CrIllegalGeneralization() {
	setHeadline("Illegal MGeneralization ");

	addSupportedDecision(CrUML.decINHERITANCE);
	addTrigger("supertype");
	addTrigger("subtype");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(ModelFacade.isAGeneralization(dm))) return NO_PROBLEM;
	Object gen = /*(MGeneralization)*/ dm;
	Object cls1 = ModelFacade.getParent(gen);
	Object cls2 = ModelFacade.getChild(gen);
	if (cls1 == null || cls2 == null) return NO_PROBLEM;
	java.lang.Class javaClass1 = cls1.getClass();
	java.lang.Class javaClass2 = cls2.getClass();
	if (javaClass1 != javaClass2) return PROBLEM_FOUND;
	return NO_PROBLEM;
    }

} /* end class CrIllegalGeneralization.java */