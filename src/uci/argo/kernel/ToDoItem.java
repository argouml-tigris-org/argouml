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

// File: ToDoItem.java
// Classes: ToDoItem
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.kernel;

//import jargo.util.*;
import uci.util.*;
import java.util.*;

/** This class defines the feedback items that can be placed on the
 *  Designer's ToDoList.  The main point of a ToDoItem is to inform
 *  the Designer of some problem or open design issue.  Additional
 *  information in the ToDoItem helps put the designer in a mental
 *  context suitable for resolving the issue: ToDoItem's are well tied
 *  into the design and design process so that the Designer can see
 *  which design material's are the subject of this ToDoItem, and which
 *  Critic raised it.  The expert email address helps connect the
 *  designer with the organizational context.  The more info URL helps
 *  provide background knowledge of the domain. In the future
 *  ToDoItems will include ties back to the design rationale log.
 *  Also the run-time system needs to know who posted each ToDoItem so
 *  that it can automatically remove it if it is no longer valid. */

public class ToDoItem implements java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // constants
  public static final int HIGH_PRIORITY = 1;
  public static final int MED_PRIORITY = 2;
  public static final int LOW_PRIORITY = 3;
  
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Who posted this item (e.g., a Critic, or the designer)? */
  private Poster _poster;

  /** One line description of issue. */
  private String _headline;

  /** How important is this issue? Enough to interrupt the designer? */
  private int _priority;

  /** One paragraph description of the issue. */
  private String _description;

  /** URL for background (textbook?) knowledge about the domain. */
  private String _moreInfoURL;

  /** Which part of the design does this issue affect? */
  private Set _offenders;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ToDoItem(Poster poster, String h, int p, String d, String m,
		  Set offs) {
    _poster = poster;
    _headline = h;
    _offenders = offs;
    _priority = p;
    _description = d;
    _moreInfoURL = m;
  }

  public ToDoItem(Critic c, Object dm, Designer dsgr) {
    _poster = c;
    _headline = c.getHeadline(dm, dsgr);
    _offenders = new Set(dm);
    _priority = c.getPriority(_offenders, dsgr);
    _description = c.getDescription(_offenders, dsgr);
    _moreInfoURL = c.getMoreInfoURL(_offenders, dsgr);
  }

  public ToDoItem(Critic c, Set offs, Designer dsgr) {
    _poster = c;
    _headline = c.getHeadline(offs, dsgr);
    _offenders = offs;
    _priority = c.getPriority(_offenders, dsgr);
    _description = c.getDescription(_offenders, dsgr);
    _moreInfoURL = c.getMoreInfoURL(_offenders, dsgr);
  }

  public ToDoItem(Critic c) {
    _poster = c;
    _headline = c.getHeadline();
    _offenders = new Set();
    _priority = c.getPriority(null, null);
    _description = c.getDescription(null, null);
    _moreInfoURL = c.getMoreInfoURL(null, null);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public String getHeadline() { return _headline; }
  public String getDescription() { return _description; }
  public String getMoreInfoURL() { return _moreInfoURL; }
  public int getPriority() { return _priority; }

   public void setHeadline(String h) { _headline = h; }
   public void setDescription(String d) { _description = d; }
   public void setMoreInfoURL(String m) { _moreInfoURL = m; }
   public void setPriority(int p) { _priority = p; }

  /** Reply a Set of design material's that are the subject of this
   * ToDoItem. */
  public Set getOffenders() { return _offenders; }

  /** Reply the Critic or Designer that posted this ToDoItem. */
  public Poster getPoster() { return _poster; }

  /** Find the email address of the poster. */
  public String getExpertEmail() { return _poster.getExpertEmail(); }

  /** Is this item already on the list? */
  public boolean equals(Object o) {
    if (!(o instanceof ToDoItem)) return false;
    ToDoItem i = (ToDoItem) o;
    boolean b1 = getHeadline().equals(i.getHeadline());
    boolean b2 = getPoster() == (i.getPoster());
    boolean b3 = getOffenders().equals(i.getOffenders());
    return  b1 && b2 && b3;
  }

  ////////////////////////////////////////////////////////////////
  // user interface

  /** When a ToDoItem is selected in the UiToDoList window, highlight
   * the "offending" design material's. */
  public void select() {
    Enumeration offs = getOffenders().elements();
    while (offs.hasMoreElements()) {
      Object dm = offs.nextElement();
       if (dm instanceof DesignMaterial)
	 ((DesignMaterial)dm).highlight(this);
    }
  }

  /** When a ToDoItem is deselected in the UiToDoList window,
   * unhighlight the "offending" design material's. */
  public void deselect() {
    Enumeration offs = getOffenders().elements();
    while (offs.hasMoreElements()) {
      Object dm =  offs.nextElement();
       if (dm instanceof DesignMaterial)
	 ((DesignMaterial)dm).unhighlight(this);
    }
  }

   /** The user has double-clicked or otherwise indicated that they
    * want to do something active with this item. By default, just
    * re-select it, subclasses may choose to do more (e.g., navigate to
    * the offending item if it is not visible). */
   public void action() { deselect(); select(); }


  ////////////////////////////////////////////////////////////////
  // issue resolutions

  /** Some problems can be automatically fixed, ask the Critic to do
   *  it if it can. <p> */
  public void fixIt() { _poster.fixIt(this, null); }

  /** Some problems can be automatically fixed, ask the Critic to do
   *  it if it can. <p> */
  public boolean canFixIt() { return _poster.canFixIt(this); }

  /** Needs-More-Work: this is not done yet. Eventually this will also
   *  feed the rational log. */
  public void resolve(Object reason) {
    ToDoList list = Designer.theDesigner().getToDoList();
    list.resolve(this, reason);
  }

  /** Reply true iff this ToDoItem should be kept on the Designer's
   * ToDoList. This should return false if the poster has been
   * deactivated, or if it can be determined that the problem that
   * raised this issue is no longer present. */
  public boolean stillValid(Designer d) {
    if (_poster == null) return true;
    return _poster.stillValid(this, d);
  }

  /** Reply a string for debugging. */
  public String toString() {
    return this.getClass().getName() + "(" +
      getHeadline() + ") on " +
      getOffenders().toString();
  }

} /* end class ToDoItem */
