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

// File: EnumerationEmpty.java
// Classes: EnumerationEmpty
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.util;

import java.util.*;

/** A enumeration that is always empty. Functially equivelant to:
 * 
 *  <code>(new Vector()).elements();</code>
 * 
 *  This is useful when you must pass or return an enumeration, but you
 *  do not have any elements.
 *
 * @see uci.gef.DiagramElement#keysIn */

public class EnumerationEmpty
implements Enumeration, java.io.Serializable {

  public boolean hasMoreElements() { return false; }
  public Object nextElement() {
    throw new NoSuchElementException();
  }
  protected static EnumerationEmpty _theInstance = new EnumerationEmpty();
  public static EnumerationEmpty theInstance() { return _theInstance; }

} /* end class EnumerationEmpty */
