// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.



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
