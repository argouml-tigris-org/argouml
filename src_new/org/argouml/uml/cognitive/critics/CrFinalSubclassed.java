// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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



// File: CrFinalSubclassed.java
// Classes: CrFinalSubclassed
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;

import org.argouml.cognitive.*;

// Use model through ModelFacade
import org.argouml.api.FacadeManager;
import org.argouml.model.uml.NsumlModelFacade;

/** Well-formedness rule [2] for MGeneralizableElement. See page 31 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. 
 *  In UML 1.3 it is rule [2] in section 2.5.3.18 page 2-54.
 */

import org.argouml.cognitive.critics.*;

public class CrFinalSubclassed extends CrUML {

  public CrFinalSubclassed() {
    setHeadline("Remove final keyword or remove subclasses");

    addSupportedDecision(CrUML.decINHERITANCE);
    setKnowledgeTypes(Critic.KT_SEMANTICS);
    addTrigger("specialization");
    addTrigger("isLeaf");
  }

    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(FacadeManager.getUmlFacade().isAGeneralizableElement(dm))) return NO_PROBLEM;
	if (!(FacadeManager.getUmlFacade().isLeaf(dm))) return NO_PROBLEM;
	Iterator enum = FacadeManager.getUmlFacade().getSpecializations(dm);
	if (enum.hasNext()) return PROBLEM_FOUND;
	return NO_PROBLEM;
    }

} /* end class CrFinalSubclassed.java */

