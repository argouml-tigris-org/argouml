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



// File: ToDoItem.java
// Classes: ToDoItem
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.cognitive;

import java.util.*;
import javax.swing.*;

import org.tigris.gef.ui.Highlightable;
import org.tigris.gef.util.*;

import org.argouml.kernel.*;
import org.argouml.cognitive.critics.*;

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

  /** One line description of issue.
   */
  private String _headline;

  /** How important is this issue? Enough to interrupt the designer? */
  private int _priority;

  /** One paragraph description of the issue. */
  private String _description;

  /** URL for background (textbook?) knowledge about the domain. */
  private String _moreInfoURL;

  /** Which part of the design this issue affects
   *
   * This is set by the constructor and cannot change.
   */
  private VectorSet _offenders;

  private Icon _clarifier = null;

  private Wizard _wizard = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ToDoItem(Poster poster, String h, int p, String d, String m,
		  VectorSet offs) {
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
    _offenders = new VectorSet(dm);
    _priority = c.getPriority(_offenders, dsgr);
    _description = c.getDescription(_offenders, dsgr);
    _moreInfoURL = c.getMoreInfoURL(_offenders, dsgr);
    _wizard = c.makeWizard(this);
  }

  public ToDoItem(Critic c, VectorSet offs, Designer dsgr) {
    _poster = c;
    _headline = c.getHeadline(offs, dsgr);
    _offenders = offs;
    _priority = c.getPriority(_offenders, dsgr);
    _description = c.getDescription(_offenders, dsgr);
    _moreInfoURL = c.getMoreInfoURL(_offenders, dsgr);
    _wizard = c.makeWizard(this);
  }

  public ToDoItem(Critic c) {
    _poster = c;
    _headline = c.getHeadline();
    _offenders = new VectorSet();
    _priority = c.getPriority(null, null);
    _description = c.getDescription(null, null);
    _moreInfoURL = c.getMoreInfoURL(null, null);
    _wizard = c.makeWizard(this);
  }


    // Cached expansions
    private String _cachedExpandedHeadline = null;
    private String _cachedExpandedDescription = null;

  ////////////////////////////////////////////////////////////////
  // accessors

  public String getHeadline() {
      if (_cachedExpandedHeadline == null) {
	  _cachedExpandedHeadline = _poster.expand(_headline, _offenders);
      }
      return _cachedExpandedHeadline;
  }
  public void setHeadline(String h) { 
      _headline = h; 
      _cachedExpandedHeadline = null;
  }

  public String getDescription() {
      if (_cachedExpandedDescription == null) {
	  _cachedExpandedDescription = _poster.expand(_description, _offenders);
      }
      return _cachedExpandedDescription;
  }
  public void setDescription(String d) {
      _description = d;
      _cachedExpandedDescription = null;
  }

  public String getMoreInfoURL() { return _moreInfoURL; }
  public void setMoreInfoURL(String m) { _moreInfoURL = m; }

  public int getPriority() { return _priority; }
  public void setPriority(int p) { _priority = p; }

  public int getProgress() {
    if (_wizard != null) return _wizard.getProgress();
    return 0;
  }
//   public void setProgress(int p) { 
//     if (_wizard != null) return _wizard.setProgress(p);
//   }
  
  /** Reply a Set of design material's that are the subject of this
   * ToDoItem. */
  public VectorSet getOffenders() { return _offenders; }

  /** Reply the Critic or Designer that posted this ToDoItem. */
  public Poster getPoster() { return _poster; }

  /** Find the email address of the poster. */
  public String getExpertEmail() { return _poster.getExpertEmail(); }

  /** Return a clarifier object that can graphical highlight this
   *  error in a design diagram. By default return a DefaultClarifier
   *
   * @return an Icon or null if none found.
   */
  public Icon getClarifier() {
    if (_clarifier != null) return _clarifier;
    Icon posterClarifier = _poster.getClarifier();
    if (posterClarifier != null) return posterClarifier;
    return null;
  }

  public Wizard getWizard() { return _wizard; }

  public boolean containsKnowledgeType(String type) {
    return getPoster().containsKnowledgeType(type);
  }

  public boolean supports(Decision d) {
    return getPoster().supports(d);
  }

  public boolean supports(Goal g) {
    return getPoster().supports(g);
  }


  /**
   * Is this item a copy?
   */
  public boolean equals(Object o) {
    if (!(o instanceof ToDoItem)) return false;
    ToDoItem i = (ToDoItem) o;
    if (!getHeadline().equals(i.getHeadline())) return false;
    if (!(getPoster() == (i.getPoster()))) return false;

    // For some reason VectorSet.equals() allocates a lot of memory, well
    // some memory at least. Lets try to avoid that when not needed by
    // invoking this test only when the two previous tests are not decisive.
    if (!getOffenders().equals(i.getOffenders())) return false;
    return true;
  }

  ////////////////////////////////////////////////////////////////
  // user interface

  /** When a ToDoItem is selected in the UiToDoList window, highlight
   *  the "offending" design material's. */
  public void select() {
    Enumeration offs = getOffenders().elements();
    while (offs.hasMoreElements()) {
      Object dm = offs.nextElement();
       if (dm instanceof Highlightable)
	 ((Highlightable)dm).setHighlight(true);
    }
  }

  /** When a ToDoItem is deselected in the UiToDoList window,
   *  unhighlight the "offending" design material's. */
  public void deselect() {
    Enumeration offs = getOffenders().elements();
    while (offs.hasMoreElements()) {
      Object dm =  offs.nextElement();
       if (dm instanceof Highlightable)
	 ((Highlightable)dm).setHighlight(false);
    }
  }

  /** The user has double-clicked or otherwise indicated that they
   *  want to do something active with this item. By default, just
   *  re-select it, subclasses may choose to do more (e.g., navigate to
   *  the offending item if it is not visible). */
  public void action() { deselect(); select(); }

  /** Notify the user interface that this ToDoItem has
   *  changed. Currently, this is used to update the progress bar. */
  public void changed() {
    ToDoList list = Designer.theDesigner().getToDoList();
    list.fireToDoItemChanged(this);
  }


  ////////////////////////////////////////////////////////////////
  // issue resolutions

  /** Some problems can be automatically fixed, ask the Critic to do
   *  it if it can. <p> */
  public void fixIt() { _poster.fixIt(this, null); }

  /** Some problems can be automatically fixed, ask the Critic to do
   *  it if it can. <p> */
  public boolean canFixIt() { return _poster.canFixIt(this); }

  /** TODO: this is not done yet. Eventually this will also
   *  feed the rational log. */
//   public void resolve(Object reason) {
//     ToDoList list = Designer.theDesigner().getToDoList();
//     list.resolve(this, reason);
//   }

  /** Reply true iff this ToDoItem should be kept on the Designer's
   *  ToDoList. This should return false if the poster has been
   *  deactivated, or if it can be determined that the problem that
   *  raised this issue is no longer present. */
  public boolean stillValid(Designer d) {
    if (_poster == null) return true;
    if (_wizard != null && _wizard.isStarted() && !_wizard.isFinished())
      return true;
    return _poster.stillValid(this, d);
  }

  /** Reply a string for debugging. */
  public String toString() {
    return this.getClass().getName() + "(" +
      getHeadline() + ") on " +
      getOffenders().toString();
  }

} /* end class ToDoItem */


