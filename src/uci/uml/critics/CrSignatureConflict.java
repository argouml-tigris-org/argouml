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



// File: CrSignatureConflict.java
// Classes: CrSignatureConflict
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;

/** Well-formedness rule [1] for Classifier. See page 29 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrSignatureConflict extends CrUML {

  public CrSignatureConflict() {
    setHeadline("Resolve Signature Conflict");
    sd("Two operations of <ocl>self</ocl> have same signature.  A signature "+
       "consists of the operation's name and the number and types of "+
       "its parameters.\n\n"+
       "Operations must have distinct signatures for code generation to "+
       "produce code that will compile.\n\n" +
       "To fix this, use the \"Next>\" button, or manually double click on one "+
       "of the conflicting operations in the navigator pane and use the "+
       "Properties tab to change this name or parameters.");

    addSupportedDecision(CrUML.decMETHODS);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("behavioralFeature");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof uci.uml.Foundation.Core.Classifier)) return NO_PROBLEM;
    Classifier cls = (Classifier) dm;
    Vector str = cls.getBehavioralFeature();
    if (str == null) return NO_PROBLEM;
    int size = str.size();
    for (int i = 0; i < size; i++) {
      BehavioralFeature bf_i = (BehavioralFeature) str.elementAt(i);
      for (int j = i+1; j < size; j++) {
	BehavioralFeature bf_j = (BehavioralFeature) str.elementAt(j);
	if (bf_i.equals(bf_j)) return PROBLEM_FOUND;
      }
    }
    return NO_PROBLEM;
  }

} /* end class CrSignatureConflict.java */

