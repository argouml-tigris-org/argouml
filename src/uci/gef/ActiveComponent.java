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

// File: ActiveComponent.java
// Classes: ActiveComponent
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** Abstract class for objects that combine AWT widgets with editor
 *  Actions. Needs-More-Work: This may be deleted in a future release
 *  because people can do just as well using a GUI builder. */

public abstract class ActiveComponent {

  /** True for palette buttons that can be made to stick down to
   *  allow rapid construction of multiple figures.  <p>
   *  needs-more-work: should probably be in ActionButton. */
  protected boolean _canStick = false;

  public ActiveComponent() { }

  /** Return the AWT widget that the user can manipulate */
  public abstract Component component();
  public abstract Action action();

  /** Carry out an editor Action appropriate for some user interface
   * action.
   * @param e The Event that manipulated the AWT widget.
   */
  public void doIt(Event e) {
    Editor ce = Globals.curEditor();
    if (ce != null) ce.executeAction(action(), e);
  }

  /** Reverse the effect of an earlier action */
  public void undoIt() {
    System.out.println("Not implemented yet");
  }

  /** Set whether a widget can be stuck down to repeatedly perform its
    action */
  public void canStick(boolean b) { _canStick = b; }

  /** Return whether a widget can be stuck down */
  public boolean canStick() { return _canStick; }

  /** Make the widget appear normal, not stuck down. */
  public void unhighlight() {
    component().setBackground(component().getParent().getBackground());
    component().setForeground(component().getParent().getForeground());
  }

  /** Make the widget appear stuck down by inverting foreground and
   * background colors. <p>
   * needs-more-work: This does not work on Windows95. */
  public void highlight() {
    if (_canStick) {
      component().setBackground(component().getParent().getForeground());
      component().setForeground(component().getParent().getBackground());
    }
  }

} /* end class ActiveComponent */
