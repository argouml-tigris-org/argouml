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

// File: CheckItem.java
// Classes: CheckItem
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.checklist;

import java.util.*;

import uci.util.*;


/** This class defines an item that can be placed on a Checklist.  */

public class CheckItem implements java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // instance variables

  protected String _category;

  /** One sentence description of the issue. usually in the form of a
   *  question. */
  protected String _description;

  /** URL for background (textbook?) knowledge about the domain. */
  protected String _moreInfoURL = "http://www.ics.uci.edu/pub/arch/argo/";

  protected Predicate _pred = PredicateTrue.theInstance();

  ////////////////////////////////////////////////////////////////
  // constructors

  public CheckItem(String c, String d) {
    setCategory(c);
    setDescription(d);
  }

  public CheckItem(String c, String d, String m, Predicate p) {
    this(c, d);
    setMoreInfoURL(m);
    _pred = p;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public String getCategory() { return _category; }
  public void setCategory(String c) { _category = c; }

  public String getDescription() { return _description; }
  public String getDescription(Object dm) {
    return expand(_description, dm);
  }
  public void setDescription(String d) { _description = d; }

  public String getMoreInfoURL() { return _moreInfoURL; }
  public void setMoreInfoURL(String m) { _moreInfoURL = m; }

  public Predicate getPredicate() { return _pred; }
  public void setPredicate(Predicate p) { _pred = p; }

  /** Is this item already on the list? */
  public boolean equals(Object o) {
    if (!(o instanceof CheckItem)) return false;
    CheckItem i = (CheckItem) o;
    return getDescription().equals(i.getDescription());
  }

  public String toString() { return getDescription(); }

  public String expand(String desc, Object dm) { return desc; }
  
} /* end class CheckItem */
