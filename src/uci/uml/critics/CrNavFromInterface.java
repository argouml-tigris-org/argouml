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



// File: CrNavFromInterface.java
// Classes: CrNavFromInterface.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import com.sun.java.util.collections.*;
import uci.argo.kernel.*;
import uci.util.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.collaborations.*;

public class CrNavFromInterface extends CrUML {

  public CrNavFromInterface() {
    setHeadline("Remove Navigation from MInterface <ocl>self</ocl>");
    sd("Associations involving an MInterface can be not be naviagable in "+
       "the direction from the MInterface.  This is because interfaces do "+
       "contain only operation declarations and cannot hold pointers to "+
       "other objects.\n\n" +
       "This part of the design should be changed before you can generate "+
       "code from this design.  If you do generate code before fixing this "+
       "problem, the code will not match the design.\n\n"+
       "To fix this, select the MAssociation and use the \"Properties\" "+
       "tab to uncheck Navigable for the end touching the MInterface.  "+
       "The MAssociation should then appear with an stick arrowhead pointed "+
       "away from the MInterface.");
    addSupportedDecision(CrUML.decRELATIONSHIPS);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("end_navigable");
  }

  /** Applies to Associations only, not AssociationClasses. */
  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MAssociation)) return NO_PROBLEM;
    MAssociation asc = (MAssociation) dm;
    Collection conns = asc.getConnections();
    if (asc instanceof MAssociationRole)
      conns = ((MAssociationRole)asc).getConnections();
    int aggCount = 0;
    Iterator enum = conns.iterator();
    while (enum.hasNext()) {
      MAssociationEnd ae = (MAssociationEnd) enum.next();
      if (!ae.isNavigable()) continue;
      if (ae.getType() instanceof MInterface) return PROBLEM_FOUND;
      if (ae.getType() instanceof MClassifierRole) {
        Collection bases = ((MClassifierRole)ae.getType()).getBases();
        for (Iterator iter = bases.iterator(); iter.hasNext();) {
          if (iter.next() instanceof MInterface)
            return PROBLEM_FOUND;
        };
      };
    }
    return NO_PROBLEM;
  }

} /* end class CrNavFromInterface */

