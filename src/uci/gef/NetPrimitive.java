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

// File: NetPrimitive.java
// Classes: NetPrimitive
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.beans.*;

import uci.util.*;
import uci.ui.*;

/** Abstract superclass for all Net-level objects. I currently
 *  anticipate exactly 4 subclasses: NetNode, NetPort, NetEdge, and
 *  NetList. The only behavior that is common to all of those is that
 *  they pass update notifications on to their Observer's if they don't
 *  handle a notification themselves, and they can highlight by passing
 *  a notification to their FigNode, or FigEdge if any.
 *
 * @see NetNode
 * @see NetPort
 * @see NetEdge
 * @see NetList
 * @see FigNode
 * @see FigEdge
 */

public class NetPrimitive {
  ////////////////////////////////////////////////////////////////
  // instance variables

  protected PropertyChangeSupport _changeSup = new PropertyChangeSupport(this);
  protected boolean _highlight = false;
  
  /** Construct a new net-level object, currently does nothing */
  public NetPrimitive() { }

  /** Draw the user's attention to any and all visualizations of this
   *  net-level object. */
  public boolean setHighlight() { return _highlight; }
  
  public void setHighlight(boolean b) {
    boolean old = _highlight;
    _highlight = b;
    firePropertyChange("Highlight", old, _highlight);
  }


  ////////////////////////////////////////////////////////////////
  // notifications and updates

  public void addPropertyChangeListener(PropertyChangeListener l) {
    _changeSup.addPropertyChangeListener(l);
  }

  public void removePropertyChangeListener(PropertyChangeListener l) {
    _changeSup.removePropertyChangeListener(l);
  }

  public void firePropertyChange(String pName, Object oldV, Object newV) {
    _changeSup.firePropertyChange(pName, oldV, newV);
  }

  public void firePropertyChange(String pName, boolean oldV, boolean newV) {
    _changeSup.firePropertyChange(pName,
				  oldV ? Boolean.TRUE : Boolean.FALSE,
				  newV ? Boolean.TRUE : Boolean.FALSE);
  }

  public void firePropertyChange(String pName, int oldV, int newV) {
    _changeSup.firePropertyChange(pName, new Integer(oldV), new Integer(newV));
  }
  
} /* end class NetPrimitive */
