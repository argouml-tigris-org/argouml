// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: EnumerationSingle.java
// Classes: EnumerationSingle
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.util;

import java.util.*;

/** A enumeration that has exactly one element. Functially equivelant to:
 * 
 *  <code>
 *  Vector v = new Vector();
 *  v.addElement(obj);
 *  return v.elements();
 *  </code>
 * 
 *  This is useful when you must pass or return an enumeration, but you
 *  do not have many elements.
 *
 * @see uci.uml.critics.ChildGenUML#gen */

public class EnumerationSingle
implements Enumeration, java.io.Serializable {
  Object _element = null;

  public EnumerationSingle(Object ele) { _element = ele; }
  public boolean hasMoreElements() { return _element != null; }
  public Object nextElement() {
    if (_element != null) {
      Object o = _element;
      _element = null;
      return o;
    }
    else throw new NoSuchElementException();
  }

} /* end class EnumerationSingle */


