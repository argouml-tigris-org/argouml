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




// File: EnumerationPredicate.java
// Classes: EnumerationPredicate
// Original Author: jrobbins@ics.uci.edu
// $Id$


package uci.util;

import java.util.*;

/** Step through the elements of some other enumeration, but skip over
 *  any elements that do not satisfy the given predicate. */

public class EnumerationPredicate
implements Enumeration, java.io.Serializable  {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The normal enumeration that this EnumerationPredicate is moving
   *  through. */
  protected Enumeration _enum = null;

  /** The predicate that must be satisfied in order for a given
   *  element to be returned by nextElement(). */
  protected Predicate _filter = null;

  /** The element that will be returned on the next call to
   *  nextElement(). This element is "on deck". */
  protected Object _nextElement = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  public EnumerationPredicate(Enumeration e, Predicate p) {
    _enum = e;
    _filter = p;
    findNext();
  }

  ////////////////////////////////////////////////////////////////
  // Enumeration API

  /** Reply true iff there are more elements in the given enumeration
   *  that satisfy the given predicate. */
  public boolean hasMoreElements() { return _nextElement != null; }

  /** Reply the next element in the given enumeration that satisfies
   *  the given predicate. */
  public Object nextElement() {
    if (!hasMoreElements()) throw(new NoSuchElementException());
    Object res = _nextElement;
    findNext();
    return res;
  }

  /** Internal method to find the next element that satisfies the
   *  predicate and store it in _nextElement. */
  protected void findNext() {
    _nextElement = null;
    while (_enum.hasMoreElements() && _nextElement == null) {
      Object o = _enum.nextElement();
      if (_filter.predicate(o))
	_nextElement = o;
    }
  }

  
  static final long serialVersionUID = -33702065612228873L;
} /* end class EnumerationPredicate */
