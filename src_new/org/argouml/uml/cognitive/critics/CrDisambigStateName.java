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

import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.state_machines.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;

public class CrDisambigStateName extends CrUML {

  public CrDisambigStateName() {
    setHeadline("Choose a Unique Name for <ocl>self</ocl>");
    addSupportedDecision(CrUML.decNAMING);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("name");
    addTrigger("parent");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MState)) return NO_PROBLEM;
    MState s = (MState) dm;
    String myName = s.getName();
    // needs-more-work: should define a CompoundCritic
    if (myName == null || myName.equals("")) return NO_PROBLEM;
    String myNameString = myName;
    if (myNameString.length() == 0) return NO_PROBLEM;
    Collection pkgs = s.getElementImports2();
    if (pkgs == null) return NO_PROBLEM;
    for (Iterator iter = pkgs.iterator(); iter.hasNext();) {
      MElementImport imp = (MElementImport)iter.next();
      MNamespace ns = imp.getPackage();
      if (ns == null) return NO_PROBLEM;
      Collection oes = ns.getOwnedElements();
      if (oes == null) return NO_PROBLEM;
      Iterator enum = oes.iterator();
      while (enum.hasNext()) {
        MElementImport eo = (MElementImport) enum.next();
        MModelElement me = (MModelElement) eo.getModelElement();
        if (!(me instanceof MClassifier)) continue;
        if (me == s) continue;
        String meName = me.getName();
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

