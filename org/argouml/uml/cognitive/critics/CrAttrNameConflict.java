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



// File: CrAttrNameConflict.java
// Classes: CrAttrNameConflict
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;

/** Well-formedness rule [2] for MClassifier. See page 29 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrAttrNameConflict extends CrUML {

  public CrAttrNameConflict() {
    setHeadline("Revise MAttribute Names to Avoid Conflict");
    addSupportedDecision(CrUML.decINHERITANCE);
    addSupportedDecision(CrUML.decSTORAGE);
    addSupportedDecision(CrUML.decNAMING);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("structuralFeature");
    addTrigger("feature_name");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MClassifier)) return NO_PROBLEM;
    MClassifier cls = (MClassifier) dm;
    Collection str = cls.getFeatures();
    if (str == null) return NO_PROBLEM;
    Iterator enum = str.iterator();
    Vector namesSeen = new Vector();
    // warn about inheritied name conflicts, different critic?
    while (enum.hasNext()) {
      MFeature f = (MFeature) enum.next();
      if (!(f instanceof MStructuralFeature))
        continue;
      MStructuralFeature sf = (MStructuralFeature) f;
      String sfName = sf.getName();
      if (sfName == null || sfName.length() == 0) continue;
      String nameStr = sfName;
      if (nameStr.length() == 0) continue;
      if (namesSeen.contains(nameStr)) return PROBLEM_FOUND;
      namesSeen.addElement(nameStr);
    }
    return NO_PROBLEM;
  }

  public Icon getClarifier() {
    return ClAttributeCompartment.TheInstance;
  }

} /* end class CrAttrNameConflict.java */

