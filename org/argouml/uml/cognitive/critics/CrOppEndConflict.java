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



// File: CrOppEndConflict.java
// Classes: CrOppEndConflict
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.collaborations.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;

/** Well-formedness rule [2] for MClassifier. See page 29 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

//needs-more-work: split into an inherited attr critic and a local
//attr critic

public class CrOppEndConflict extends CrUML {

  public CrOppEndConflict() {
    setHeadline("Rename MAssociation Roles");
    addSupportedDecision(CrUML.decINHERITANCE);
    addSupportedDecision(CrUML.decRELATIONSHIPS);
    addSupportedDecision(CrUML.decNAMING);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("associationEnd");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MClassifier)) return NO_PROBLEM;
    MClassifier cls = (MClassifier) dm;
    Vector namesSeen = new Vector();
    Collection assocEnds = cls.getAssociationEnds();
    if (assocEnds == null) return NO_PROBLEM;
    Iterator enum = assocEnds.iterator();
    // warn about inheritied name conflicts, different critic?
    while (enum.hasNext()) {
      MAssociationEnd myAe = (MAssociationEnd) enum.next();
      MAssociation asc = (MAssociation) myAe.getAssociation();
      if (asc == null) continue;
      Collection conns = asc.getConnections();
      if (asc instanceof MAssociationRole)
	conns = ((MAssociationRole)asc).getConnections();
      if (conns == null) continue;
      Iterator enum2 = conns.iterator();
      while (enum2.hasNext()) {
	MAssociationEnd ae = (MAssociationEnd) enum2.next();
	if (ae.getType() == cls) continue;
	String aeName = ae.getName();
	if ("".equals(aeName)) continue;
	String aeNameStr = aeName;
	if (aeNameStr.length() == 0) continue;
	if (namesSeen.contains(aeNameStr)) return PROBLEM_FOUND;
	namesSeen.addElement(aeNameStr);
      }
    }
    return NO_PROBLEM;
  }

} /* end class CrOppEndConflict.java */

