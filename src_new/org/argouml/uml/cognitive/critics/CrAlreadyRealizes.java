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

package org.argouml.uml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;

/** Critic to detect whether a class implements unneedded realizations through
 *  inheritance.
 */
public class CrAlreadyRealizes extends CrUML {

  /** Constructor
   */
  public CrAlreadyRealizes() {
    setHeadline("Remove Unneeded Realizes from <ocl>self</ocl>");
    addSupportedDecision(CrUML.decINHERITANCE);
    setKnowledgeTypes(Critic.KT_SEMANTICS, Critic.KT_PRESENTATION);
    addTrigger("genealization");
    addTrigger("realization");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MClass)) return NO_PROBLEM;
    MClass cls = (MClass) dm;
    Collection interfaces = getSpecifications(cls);
    Vector indirect = findIndirectRealizations(cls);
    Iterator enum = interfaces.iterator();
    while (enum.hasNext()) {
      Object o = enum.next();
      if (!(o instanceof MClassifier))
        continue;
      MClassifier intf = (MClassifier)o;
      if (indirect.contains(intf)) return PROBLEM_FOUND;
    }
    return NO_PROBLEM;
  }

  /** find all indirect realizations of the given classifier.
   */
  public Vector findIndirectRealizations(MClassifier cls) {
    Vector res = new Vector();
    Collection interfaces = getSpecifications(cls);
    for (Iterator iter = interfaces.iterator(); iter.hasNext();) {
      Object o = iter.next();
      if (!(o instanceof MClassifier))
        continue;
      MClassifier intf = (MClassifier)o;
      accumIndirect(intf, res);
    }
    return res;
  }

  public void accumIndirect(MGeneralizableElement intf, Vector res) {
    if (intf == null) return;
      Collection gens = intf.getGeneralizations();
      for (Iterator iter = gens.iterator(); iter.hasNext(); ) {
  
        MGeneralization g = (MGeneralization) iter.next();
        MGeneralizableElement sup = g.getParent();
  
        if (!res.contains(sup)) {
            res.addElement(sup);
            accumIndirect(sup, res);
        }
      }
  }

  /** get all the direct dependencies of the Classifier.
   *  @param cls the Classifier to inspect.
   */
  private Collection getSpecifications(MClassifier cls)
  {
     Vector res = new Vector();
     Collection deps = cls.getClientDependencies();
     if (deps==null) return res;
     for (Iterator iter = deps.iterator(); iter.hasNext();) {
       MDependency dependency = (MDependency)iter.next();
       MStereotype stereotype = dependency.getStereotype();
       if ((stereotype==null) || ("realize".equals(stereotype.getName())))
         res.addAll(dependency.getSuppliers());
     };
     return res;
  };
} /* end class CrAlreadyRealizes */

