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



// File: CrOperNameConflict.java
// Classes: CrOperNameConflict
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import javax.swing.*;

import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

public class CrOperNameConflict extends CrUML {

  public CrOperNameConflict() {
    setHeadline("Change Names or Signatures in <ocl>self</ocl>");
    sd("Two operations have the exact same signature.  "+
       "Operations must have distinct signatures.  A signature is the combination "+
       "of the operation's name, and parameter types. \n\n"+
       "Avoiding conflicting siginatures is key to code generation and producing an "+
       "understandable and maintainable design.\n\n"+
       "To fix this, use the \"Next>\" button, or manually select the one of the "+
       "conflicting operations of this class and change its name or parameters.");

    addSupportedDecision(CrUML.decMETHODS);
    addSupportedDecision(CrUML.decNAMING);
    addTrigger("behavioralFeature");
    addTrigger("feature_name");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof Classifier)) return NO_PROBLEM;
    Classifier cls = (Classifier) dm;
    Vector str = cls.getBehavioralFeature();
    if (str == null) return NO_PROBLEM;
    java.util.Enumeration enum = str.elements();
    Vector operSeen = new Vector();
    // warn about inheritied name conflicts, different critic?
    while (enum.hasMoreElements()) {
      BehavioralFeature bf = (BehavioralFeature) enum.nextElement();
      int size = operSeen.size();
      for (int i = 0; i < size; i++) {
	BehavioralFeature otherBF = (BehavioralFeature) operSeen.elementAt(i);
	if (signaturesMatch(bf, otherBF)) return PROBLEM_FOUND;
      }
      operSeen.addElement(bf);
    }
    return NO_PROBLEM;
  }


  public boolean signaturesMatch(BehavioralFeature bf1, BehavioralFeature bf2) {
    Name name1 = bf1.getName();
    Name name2 = bf2.getName();
    if (name1 == null || name2 == null) return false;
    if (!name1.equals(name2)) return false;
    Vector params1 = bf1.getParameter();
    Vector params2 = bf2.getParameter();
    int size1 = params1.size();
    int size2 = params2.size();
    if (size1 != size2) return false;
    for (int i = 0; i < size1; i++) {
      Parameter p1 = (Parameter) params1.elementAt(i);
      Parameter p2 = (Parameter) params2.elementAt(i);
      Name p1Name = p1.getName();
      Name p2Name = p2.getName();
      if (p1Name == null || p2Name == null) return false;
      if (!p1Name.equals(p2Name)) return false;
      Classifier p1Type = p1.getType();
      Classifier p2Type = p2.getType();
      if (p1Type == null || p2Type == null) return false;
      if (!p1Type.equals(p2Type)) return false;
    }
    return true;
  }

  public Icon getClarifier() {
    return ClOperationCompartment.TheInstance;
  }

} /* end class CrOperNameConflict.java */

