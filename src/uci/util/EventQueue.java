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

// File: EventQueue.java
// Class: EventQueue
// Original Author: jrobbins@ics.uci.edu
// $id$

package uci.util;

import java.util.*;
import java.awt.Event;

/** A <I>very</I> simple Queue implementation. The point of using this
 *  is to allow event skipping in uci.gef.Editor.
 *  Needs-More-Work: this might be eliminated when I switch to 100%
 *  Java 1.1. */

public class EventQueue {
  private Vector _elements = new Vector();

  /** Construct an empty event queue. */
  public EventQueue() { }

  /** Add an event to the queue. */
  public void enqueue(Event obj) {
    _elements.addElement(obj);
  }

  /* Take the first event off the queue. */
  public  Event dequeue() {
    Event e = (Event)_elements.elementAt(0);
    _elements.removeElementAt(0);
    return e;
  }

  /** See what is at the head of the queue. */
  public  Event peek() { return (Event)_elements.elementAt(0); }

  /** Return true if leading two events have the same event id. */
  public synchronized boolean nextIsSameAs(Event curEvent) {
    if (isEmpty()) return false;
    return curEvent.id == peek().id;
  }

  /** Reply the number of events in the queue now. */
  public  int size() { return _elements.size(); }

  /** Reply true iff there are no events in the queue. */
  public  boolean isEmpty() { return size() == 0; }

} /* end class EventQueue */
