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

} /* end class Enum */
