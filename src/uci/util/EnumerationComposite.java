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




// File: EnumerationComposite.java
// Classes: EnumerationComposite
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.util;

import java.util.*;

/** This class concatenates Enumerations.  Successive calls to
  * nextElement return elements from each Enumeration until that
  * enumeration is exhausted. */

public class EnumerationComposite
implements Enumeration, java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The enumerations being concatenated */
  private Vector _subs = new Vector();

  /** The next element to return from nextElement(). */
  private Object _nextElement = null;

  ////////////////////////////////////////////////////////////////
  // constructors
  public EnumerationComposite() { }
  public EnumerationComposite(Enumeration e1) {
    addSub(e1);
  }
  public EnumerationComposite(Enumeration e1, Enumeration e2) {
    addSub(e1);
    addSub(e2);
  }
  public EnumerationComposite(Enumeration e1, Enumeration e2, Enumeration e3) {
    addSub(e1);
    addSub(e2);
    addSub(e3);
  }

  /** Concatenate the given Enumeration to the end of the receiving
   * EnumerationComposite. */
  public void addSub(Enumeration e) {
    if (e != null && e.hasMoreElements()) {
      _subs.addElement(e);
      findNext();
    }
  }

  /** Concatenate the elements() of the given Vector to the end of the
   * receiving EnumerationComposite. */
  public void addSub(Vector v) { if (v != null) addSub(v.elements()); }

  /** Reply true iff this EnumerationComposite has more elements. */
  public boolean hasMoreElements() {
    return _nextElement != null;
  }

  /** Reply the next element, or raise an execption if there is none. */
  public Object nextElement() {
    if (!hasMoreElements()) throw new NoSuchElementException();
    Object res = _nextElement;
    _nextElement = null;
    findNext();
    return res;
  }

  /** Internal function to find the element to return on the next call
   * to nextElement(). */
  protected void findNext() {
    if (_nextElement != null) return;
    while (!_subs.isEmpty() &&
	   !((Enumeration)_subs.firstElement()).hasMoreElements())
      _subs.removeElementAt(0);
    if (!_subs.isEmpty())
      _nextElement = ((Enumeration)_subs.firstElement()).nextElement();
  }

  
  static final long serialVersionUID = -1970828633671289903L;
} /* end class EnumerationComposite */
