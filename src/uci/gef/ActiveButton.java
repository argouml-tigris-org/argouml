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

// File: ActiveButton.java
// Classes: ActiveButton
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** An ActiveComponent for a Button. It stores an AWT button and a
 *  uci.gef.Action. When the button is pressed, the Action is
 *  performed. Client Frames must still process events and find when
 *  Active component should handle a given event. <p>
 *
 *  Needs-More-Work: This may be deleted in a future release.
 *
 * @see Action
 * @see PaletteFig */

public class ActiveButton extends ActiveComponent  {

  /** The AWT button */
  private Button _button;

  /** The uci.gef.Action to be exectuted */
  private Action _action;

  /** Construct a new ActiveButton.
   * @param label The String to be shown on the button.
   * @param act   The Action to be exectuted when the button is pressed.
   */
  public ActiveButton(String label, Action act) {
    _button = new java.awt.Button(label);
    _action = act;
  }

  public Component component() { return _button; }
  public Action action() { return _action; }

  // public void doIt(java.awt.Event e) { action().doIt(e); }
  public void undoIt() { action().undoIt(); }

} /* end class ActiveButton */

