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

// File: SnoozeOrder.java
// Classes: SnoozeOrder
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.cognitive.critics;

import java.util.*;

/** A Critic can be disabled for a certain amount of time by giving it
 *  the snooze command.  Whereas most ControlMech's activate or deactivate
 *  Critic's based on evidence of the Designer's state of mind, this
 *  command allows the Designer to disable Critic's without stating any
 *  reason.  However, after a period of time, the critic may become
 *  active again.  We think this will often be convienent because
 *  Designer's have a lot of tacit knowledge about their own state of
 *  mind that is not worth making explicit. */

public class SnoozeOrder implements java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // constants
  /** The initial sleeping time. */
  private final long _initialIntervalMS = 1000 * 60 * 10; /* ten minutes */

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Critic should sleep until this time. */
  private Date _snoozeUntil;
  /** Ifthe designer snoozees the critics again before this time, then
   * go to sleep for even longer. */
  private Date _snoozeAgain;
  /** The sleeping time, including the effects of repeated snoozeing. */
  private long _interval;

    private Date _now = new Date();
    private Date getNow() {
	_now.setTime(System.currentTimeMillis());
	return _now;
    }
	
  ////////////////////////////////////////////////////////////////
  // constructor

  public SnoozeOrder() {
    /* in the past, 0 milliseconds after January 1, 1970, 00:00:00 GMT. */
    _snoozeUntil =  new Date(0); 
    _snoozeAgain =  new Date(0); 
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public boolean getSnoozed() {
    return _snoozeUntil.after(getNow());
  }

  public void setSnoozed(boolean h) {
    if (h) snooze(); else unsnooze();
  }

  ////////////////////////////////////////////////////////////////
  // criticism control

  public void snooze() {
    if (_snoozeAgain.after(getNow())) _interval = nextInterval(_interval);
    else _interval = _initialIntervalMS;
    long now = (getNow()).getTime();
    _snoozeUntil.setTime(now + _interval);
    _snoozeAgain.setTime(now + _interval + _initialIntervalMS);
    Critic.cat.info("Setting snooze order to: " +
		       _snoozeUntil.toString());
  }

  public void unsnooze() {
    /* in the past, 0 milliseconds after January 1, 1970, 00:00:00 GMT. */
    _snoozeUntil =  new Date(0); 
  }

  protected long nextInterval(long last) {
    /* by default, double the snooze interval each time */
    return last * 2;
  }

} /* end class SnoozeOrder */
