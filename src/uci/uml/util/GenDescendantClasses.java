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

/** Utility class to generate the subclasses of a class.  It
 *  recursively moves down the class hierarchy.  But it does that in a
 *  safe way that will nothang in case of cyclic inheritance. */

public class GenDescendantClasses implements ChildGenerator {
  public static GenDescendantClasses SINGLETON = new GenDescendantClasses();

  public java.util.Enumeration gen(Object o) {
    if (!(o instanceof GeneralizableElement))
      return EnumerationEmpty.theInstance();

    Classifier cls = (Classifier) o;
    Vector gens = cls.getSpecialization();
    if (gens == null) return EnumerationEmpty.theInstance();
    Vector res = new Vector();
    accumulateDescendants(cls, res);
    return res.elements();
  }


  public void accumulateDescendants(GeneralizableElement cls, Vector accum) {
    Vector gens = cls.getSpecialization();
    if (gens == null) return;
    int size = gens.size();
    for (int i = 0; i < size; i++) {
      Generalization g = (Generalization) gens.elementAt(i);
      GeneralizableElement ge = g.getSubtype();
      if (!accum.contains(ge)) {
	accum.addElement(ge);
	accumulateDescendants(cls, accum);
      }
    }
  }
} /* end class GenDescendantClasses */

