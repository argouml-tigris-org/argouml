// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.



// File: CrIllegalName.java.java
// Classes: CrIllegalName.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;


public class CrIllegalName extends CrUML {

  public CrIllegalName() {
    setHeadline("Choose a Legal Name");
    sd("The names of model elements must be sequences of letters, "+
       "numbers, and underscores.  They cannot contain punctuation.\n\n"+
       "useing legal names is needed for code generation. \n\n"+
       "To fix this, use the FixIt button, or manually select the "+
       "highlighted element and use the Properties tab to give it "+
       "a different name.");
    addSupportedDecision(CrUML.decNAMING);
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof ModelElement)) return NO_PROBLEM;
    ModelElement me = (ModelElement) dm;
    Name meName = me.getName();
    if (meName == null || meName.equals(Name.UNSPEC)) return NO_PROBLEM;
    String nameStr = meName.getBody();
    int len = nameStr.length();
    for (int i = 0; i < len; i++) {
      char c = nameStr.charAt(i);
      if (!Character.isLetterOrDigit(c) && c != '_')
	return PROBLEM_FOUND;
    }
    return NO_PROBLEM;
  }

} /* end class CrIllegalName */

