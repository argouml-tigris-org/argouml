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



// File: CrAlreadyRealizes.java
// Classes: CrAlreadyRealizes.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

public class CrAlreadyRealizes extends CrUML {

  public CrAlreadyRealizes() {
    setHeadline("Remove Unneeded Realizes from <ocl>self</ocl>");
    sd("The selected class already indirectly realizes Interface " +
       "{item.extra}.  There is no need to directly realize it again.\n\n"+
       "Simplifying the design is always a good idea.  You might dismiss "+
       "this \"to do\" item if you want to make it very explicit that the "+
       "selected Class realizes this Interface.\n\n"+
       "To fix this, select the Realization (dashed line with white "+
       "triangular arrowhead) and press the \"Delete\" key.");
    addSupportedDecision(CrUML.decINHERITANCE);
    setKnowledgeTypes(Critic.KT_SEMANTICS, Critic.KT_PRESENTATION);
    addTrigger("genealization");
    addTrigger("realization");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MMClass)) return NO_PROBLEM;
    MMClass cls = (MMClass) dm;
    Vector interfaces = cls.getSpecification();
    Vector indirect = findIndirectRealizations(cls);
    java.util.Enumeration enum = interfaces.elements();
    while (enum.hasMoreElements()) {
      Realization r = (Realization) enum.nextElement();
      Classifier intf = r.getSupertype();
      if (indirect.contains(intf)) return PROBLEM_FOUND;
    }
    return NO_PROBLEM;
  }


  public Vector findIndirectRealizations(Classifier cls) {
    Vector res = new Vector();
    Vector interfaces = cls.getSpecification();
    int size = interfaces.size();
    //System.out.println("class " + cls + " has " + size + " interfaces");
    for (int i = 0; i < size; i++) {
      Realization r = (Realization) interfaces.elementAt(i);
      Classifier intf = r.getSupertype();
      accumIndirect(intf, res);
    }
    return res;
  }

  public void accumIndirect(GeneralizableElement intf, Vector res) {
    Vector gens = intf.getGeneralization();
    int size = gens.size();
    for (int i = 0; i < size; i++) {
      Generalization g = (Generalization) gens.elementAt(i);
      GeneralizableElement sup = g.getSupertype();
      //System.out.println("sup = " + sup);
      if (!res.contains(sup)) {
	res.addElement(sup);
	accumIndirect(sup, res);
      }
    }
  }

} /* end class CrAlreadyRealizes */

