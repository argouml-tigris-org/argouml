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






package uci.uml.critics;

import com.sun.java.util.collections.*;

import uci.util.*;
import uci.argo.kernel.*;
import uci.uml.util.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

public class CrMergeClasses extends CrUML {

  public CrMergeClasses() {
    setHeadline("Consider Combining Classes");
    String s = "";
    s += "The highlighted class, <ocl>self</ocl>, only participates in one association and that ";
    s += "association is one-to-one with another class.  Since instances of these ";
    s += "two classes must always be created together and destroyed together, ";
    s += "combining these classes might simplify your design without loss of any ";
    s += "representation power.  However, you may find the combined class too large ";
    s += "and complex, in which case separating them is usually better.\n\n";
    s += "Organizing classes to manage complexity of the design is always important, ";
    s += "especially when the design is already complex. \n\n";
    s += "To fix this, click on the \"Next>\" button, or manually add the attribues and ";
    s += "operations of the highlighted class to the other class, then remove the ";
    s += "highlighted class from the project. ";

    setDescription(s);
    addSupportedDecision(CrUML.decCLASS_SELECTION); //?
    //no good trigger, should be applied to association instead
  }


  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MClass)) return NO_PROBLEM;
    MClass cls = (MClass) dm;
    Collection ends = cls.getAssociationEnds();
    if (ends == null || ends.size() != 1) return NO_PROBLEM;
    MAssociationEnd myEnd = (MAssociationEnd) ends.iterator().next();
    MAssociation asc = myEnd.getAssociation();
    List conns = asc.getConnections();
    MAssociationEnd ae0 = (MAssociationEnd) conns.get(0);
    MAssociationEnd ae1 = (MAssociationEnd) conns.get(1);
    if (ae0.getMultiplicity().equals(MMultiplicity.M1_1) &&
        ae1.getMultiplicity().equals(MMultiplicity.M1_1))
      return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

} /* end class CrMergeClasses */
