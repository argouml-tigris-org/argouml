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

// File: HushOrder.java
// Classes: HushOrder
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.kernel;

import java.util.*;

/** A Critic can be disabled for a certain amount of time by giving it
 *  the hush command.  Whereas most ControlMech's activate or deactivate
 *  Critic's based on evidence of the Designer's state of mind, this
 *  command allows the Designer to disable Critic's without stating any
 *  reason.  However, after a period of time, the critic may become
 *  active again.  We think this will often be convienent because
 *  Designer's have a lot of tacit knowledge about their own state of
 *  mind that is not worth making explicit. */

public class HushOrder implements java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // constants
  /** The initial sleeping time. */
  private final long _initialIntervalMS = 1000 * 60 * 10; /* ten minutes */

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Critic should sleep until this time. */
  private Date _hushUntil;
  /** Ifthe designer hushes the critics again before this time, then
   * go to sleep for even longer. */
  private Date _hushAgain;
  /** The sleeping time, including the effects of repeated hushing. */
  private long _interval;

  ////////////////////////////////////////////////////////////////
  // constructor

  public HushOrder() {
    _hushUntil =  new Date(70, 1, 1); /* in the past */
    _hushAgain =  new Date(70, 1, 1); /* in the past */
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public boolean getHushed() {
    return _hushUntil.after(new Date());
  }

  public void setHushed(boolean h) {
    if (h) hush(); else unhush();
  }

  ////////////////////////////////////////////////////////////////
  // criticism control

  public void hush() {
    if (_hushAgain.after(new Date())) _interval = nextInterval(_interval);
    else _interval = _initialIntervalMS;
    long now = (new Date()).getTime();
    _hushUntil.setTime(now + _interval);
    _hushAgain.setTime(now + _interval + _initialIntervalMS);
    System.out.println("Setting hush order to: " +
		       _hushUntil.toString());
  }

  public void unhush() {
    _hushUntil =  new Date(70, 1, 1); /* in the past */
  }

  protected long nextInterval(long last) {
    /* by default, double the hush interval each time */
    return last * 2;
  }

} /* end class HushOrder */
