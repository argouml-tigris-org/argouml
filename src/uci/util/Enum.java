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




// File: Enum.java
// Classes: Enum
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.util;

import java.util.*;

public class Enum implements Enumeration, java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Enumeration _enum = null;
  protected Object _nextElement  = null;
  protected Functor _transform = null;
  protected Predicate _preFilter = null;
  protected Predicate _postFilter = null;
  //needs-more-work: protected boolean _nestedEnums = false;

  ////////////////////////////////////////////////////////////////
  // constructors
  public Enum(Enumeration e) { enum(e); findNextElement(); }
  public Enum(Enumeration e, Functor f, Predicate p1, Predicate p2) {
    map(f);
    preFilter(p1);
    postFilter(p2);
    enum(e);
    findNextElement();
  }
  public Enum(Enumeration e, Functor f) {
    map(f);
    enum(e);
    findNextElement();
  }

  ////////////////////////////////////////////////////////////////
  // accessors and modifiers

  public void enum(Enumeration e) { _enum = e; }
  public void map(Functor f) { _transform = f; }
  //needs-more-work: public void map_star(Functor f) {
  //needs-more-work: _f = f; _nestedEnums = true; }
  public void preFilter (Predicate p) { _preFilter = p; }
  public void postFilter (Predicate p) { _postFilter = p; }
  //needs-more-work: public void select(Predicate p) { _p = p; }
  //needs-more-work: public void reject(Predicate p) {
  //needs-more-work: _p = new PredicateNot(p); }

  ////////////////////////////////////////////////////////////////
  // Enumeration API

  public boolean hasMoreElements() { return _nextElement != null; }

  public Object nextElement() {
    Object res = _nextElement;
    findNextElement();
    return res;
  }

  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void findNextElement() {
    Object xformed;
    _nextElement = null;
    while (_enum.hasMoreElements() && _nextElement == null) {
      Object enumNE = _enum.nextElement();
      if (_preFilter == null || _preFilter.predicate(enumNE)) {
	if (_transform == null) xformed = enumNE;
	else xformed = _transform.apply(enumNE);
	if (_postFilter == null || _postFilter.predicate(xformed))
	  _nextElement = xformed;
      }
    }
  }

  
  static final long serialVersionUID = 8795272597408860203L;
} /* end class Enum */
