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

public class NetPrimitive extends EventHandler
implements Observer { //, GEF { //, IProps
  ////////////////////////////////////////////////////////////////
  // instance variables

  protected Vector _persistObservers = null;

  /** Construct a new net-level object, currently does nothing */
  public NetPrimitive() { }

  /** If a update notification is not understood in a subclass it
   *  should call super.update() and this method will pass that
   *  notification on to my Observer's */
  public void update(Observable o, Object arg) {
    /* If I dont handle it, maybe my observers do */
    setChanged();
    notifyObservers(arg);
  }

  /** Draw the user's attention to any and all visualizations of this
   *  net-level object. */
  public void highlight() {
    notifyObservers(Globals.HIGHLIGHT);
    notifyPersistantObservers(Globals.HIGHLIGHT);
  }

  /** Stop drawing the user's attention. */
  public void unhighlight() {
    notifyObservers(Globals.UNHIGHLIGHT);
    notifyPersistantObservers(Globals.UNHIGHLIGHT);
  }

  ////////////////////////////////////////////////////////////////
  // accessors
  public Object get(String key) {
    return null;
  }

  /** Get a property by name. If it is not defined, return def. */
  public Object get(String key, Object def) {
    Object res = get(key);
    if (null != res) return res;
    return def;
  }

  public boolean put(String key, Object value) {
    return false;
  }

  /** Set multiple graphical attribute by repeatedly calling
   *  put(String key, Object value) */
  public void put(Hashtable newAttrs) {
    Enumeration cur = newAttrs.keys();
    while (cur.hasMoreElements()) {
      String key = (String) cur.nextElement();
      Object val = newAttrs.get(key);
      put(key, val);
    }
  }

  public Enumeration keysIn(String category) {
    return EnumerationEmpty.theInstance();
  }

  public boolean canPut(String key) {
    return false;
  }

  public boolean define(String key, Object value) {
    if (null == get(key)) {
      put(key, value);
      return true;
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////
  // notifications and updates

  public void addPersistantObserver(Observer o) {
    if (_persistObservers == null) _persistObservers = new Vector();
    _persistObservers.removeElement(o);
    _persistObservers.addElement(o);
  }

  public void removePersistObserver(Observer o) {
    _persistObservers.removeElement(o);
  }

  public void notifyPersistantObservers(Object arg) {
    if (_persistObservers == null) return;
    Enumeration eachObs = _persistObservers.elements();
    while (eachObs.hasMoreElements()) {
      Observer obs = (Observer) eachObs.nextElement();
      obs.update(this, arg);
    }
  }

  public void notifyObservers(Object arg) {
    super.notifyObservers(arg);
    notifyPersistantObservers(arg);
  }

  /** Notify observers that one of my properties has changed. */
  public void changedProp(String key) {
    setChanged();
    notifyObservers(key);
  }

} /* end class NetPrimitive */
