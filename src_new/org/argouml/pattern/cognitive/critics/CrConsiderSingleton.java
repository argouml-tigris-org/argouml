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



// File: CrConsiderSingleton.java
// Classes: CrConsiderSingleton
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.pattern.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import org.argouml.cognitive.*;
import org.argouml.uml.*;
import org.argouml.uml.cognitive.critics.*;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */

public class CrConsiderSingleton extends CrUML {

  public CrConsiderSingleton() {
    addSupportedDecision(CrUML.decPATTERNS);
    setPriority(ToDoItem.LOW_PRIORITY);
    addTrigger("stereotype");
    addTrigger("structuralFeature");
    addTrigger("associationEnd");
    setResource("CrConsiderSingleton");
  }

  protected void sd(String s) { setDescription(s); }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MClass)) return NO_PROBLEM;
    MClass cls = (MClass) dm;
    Collection str = MMUtil.SINGLETON.getAttributes(cls);
    Collection ends = cls.getAssociationEnds();

    //if it is already a Singleton, nevermind
    MStereotype st = cls.getStereotype();
    if (st != null) {
 	if (st.getName().equals("Singleton")) return NO_PROBLEM;

    }

    // if it has instance vars, no specific reason for Singleton
    if (str != null) {
        Iterator strEnum = str.iterator();
        while (strEnum.hasNext()) {
	    MStructuralFeature sf = (MStructuralFeature) strEnum.next();
	    if (MScopeKind.INSTANCE.equals(sf.getTargetScope())) return NO_PROBLEM;
        }
    }

    // if it has outgoing assocs, no specific reason for Singleton
    if (ends != null) {
        Iterator endEnum = ends.iterator();
        while (endEnum.hasNext()) {
	    MAssociationEnd ae = (MAssociationEnd) endEnum.next();
	    MAssociation a = ae.getAssociation();
            if(a != null) {
              Collection connections = a.getConnections();
              if(connections != null) {
                  Iterator iter = connections.iterator();
                  Object end = null;
                  while(iter.hasNext()) {
                      end = iter.next();
                      if(end == ae) continue;
                      if(((MAssociationEnd) end).isNavigable()) return NO_PROBLEM;
                  }
              }
            }
        }
    }
    // if it has no ivars, suggest the designer consider the Singleton Pattern
    return PROBLEM_FOUND;
  }

} /* end class CrConsiderSingleton */

