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



// File: CrMultiComposite.java
// Classes: CrMultiComposite
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** Well-formedness rule [2] for AssociationEnd. See page 28 of UML
 *  1.1 Semantics. OMG document ad/97-08-04. This Critic is currently
 *  not used.  It is not registered in Init.java. */

public class CrMultiComposite extends CrUML {

  public CrMultiComposite() {
    setHeadline("Composite Role with Multiplicity > 1");
    sd("A composite (black diamond) role of an association indicates that \n"+
       "instances of that class contain instances of the associated classes. \n"+
       "Since each instance can only be contained in one other object, the \n"+
       "multiplicity of a composite role must be 0..1 or 1..1.\n\n"+
       "Good OO design depends on building good is-part-of relationships.\n\n"+
       "To fix this, use the \"Next>\" button, or manually set the multiplicity \n"+
       "to 0..1 or 1..1, or change the composite aggregation into another kind \n"+
       "of aggregation (e.g., a white diamond is less strict).");

    addSupportedDecision(CrUML.decCONTAINMENT);
    setKnowledgeTypes(Critic.KT_SEMANTICS);
    addTrigger("aggregation");
    addTrigger("multiplicity");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof AssociationEnd)) return NO_PROBLEM;
    AssociationEnd ae = (AssociationEnd) dm;
    AggregationKind ak = ae.getAggregation();
    Multiplicity m = ae.getMultiplicity();
    if (ak != AggregationKind.COMPOSITE) return NO_PROBLEM;
    if (m.max() <= 1) return NO_PROBLEM;
    return PROBLEM_FOUND;
  }

  public Class getWizardClass(ToDoItem item) {
    return WizAssocComposite.class;
  }


} /* end class CrMultiComposite */

