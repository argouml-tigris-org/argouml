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


package uci.uml.util;

import java.util.*;

import java.util.*;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** Utility class to generate the children of a class.  In this case
 *  the "children" of a class are the other classes that are
 *  assocaiated with the parent class, and that Association has a
 *  COMPOSITE end at the parent.  This is used in one of the
 *  NavPerspectives. */

public class GenCompositeClasses implements ChildGenerator {
  public static GenCompositeClasses SINGLETON = new GenCompositeClasses();

  public java.util.Enumeration gen(Object o) {
    if (!(o instanceof Classifier)) return EnumerationEmpty.theInstance();
    Classifier cls = (Classifier) o;
    Vector ends = cls.getInheritedAssociationEnds();
    if (ends == null) return EnumerationEmpty.theInstance();
    Vector res = new Vector();
    java.util.Enumeration enum = ends.elements();
    while (enum.hasMoreElements()) {
      AssociationEnd ae = (AssociationEnd) enum.nextElement();
      if (AggregationKind.COMPOSITE.equals(ae.getAggregation())) {
	IAssociation asc = ae.getAssociation();
	Vector conn = asc.getConnection();
	if (conn == null || conn.size() != 2) continue;
	Object otherEnd = (ae == conn.elementAt(0)) ?
	  conn.elementAt(1) : conn.elementAt(0);
	Classifier componentClass = ((AssociationEnd)otherEnd).getType();
	res.addElement(componentClass);
      }
    }
    return res.elements();
  }
} /* end class GenCompositeClasses */
  
