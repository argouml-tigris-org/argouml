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





// File: CrNoInstanceVariables.java
// Classes: CrNoInstanceVariables
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;
import org.argouml.uml.*;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */

public class CrNoInstanceVariables extends CrUML {

  public CrNoInstanceVariables() {
    setHeadline("Add Instance Variables to <ocl>self</ocl>");
    sd("You have not yet specified instance variables for <ocl>self</ocl>. "+
       "Normally classes have instance variables that store state "+
       "information for each instance. Classes that provide only "+
       "static attributes and methods should be stereotyped <<utility>>.\n\n"+
       "Defining instance variables needed to complete the information "+
       "representation part of your design. \n\n"+
       "To fix this, press the \"Next>\" button, or add instance "+
       "variables by dobule clicking on <ocl>self</ocl> in the navigator pane and "+
       "using the Create menu to make a new attribute. ");

    addSupportedDecision(CrUML.decSTORAGE);
    setKnowledgeTypes(Critic.KT_COMPLETENESS);
    addTrigger("structuralFeature");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MClass)) return NO_PROBLEM;
    MClass cls = (MClass) dm;
    if ((cls.getStereotype()!=null) && "utility".equals(cls.getStereotype().getName()) )
      return NO_PROBLEM;
    Collection str = getInheritedStructuralFeatures(cls);
    if (str == null) return PROBLEM_FOUND;
    Iterator enum = str.iterator();
    while (enum.hasNext()) {
      MStructuralFeature sf = (MStructuralFeature) enum.next();
      MChangeableKind ck = sf.getChangeability();
      MScopeKind sk = sf.getOwnerScope();
      if (MScopeKind.INSTANCE.equals(sk))
	return NO_PROBLEM;
    }
    //needs-more-work?: don't count static or constants?
    return PROBLEM_FOUND;
  }

  public Icon getClarifier() {
    return ClAttributeCompartment.TheInstance;
  }

  private Collection getInheritedStructuralFeatures(MClassifier cls)
  {
     Collection res = new Vector();
	 res.addAll(MMUtil.SINGLETON.getAttributes(cls));

     Collection inh = cls.getGeneralizations();
     for (Iterator iter = inh.iterator(); iter.hasNext();) {
       MGeneralization gen = (MGeneralization)iter.next();
       if (gen.getParent() instanceof MClassifier) {
         Collection superstructs = getInheritedStructuralFeatures((MClassifier)gen.getParent());
         res.addAll(superstructs);
       };
     };
     return res;
  };
} /* end class CrNoInstanceVariables */

