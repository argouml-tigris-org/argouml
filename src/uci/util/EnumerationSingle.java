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
  
  static final long serialVersionUID = -4656163047752777866L;
} /* end class EnumerationSingle */


