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

// File: PaletteSticky.java
// Classes: PaletteSticky
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;

/** class to provide "sticky" checkbox on palettes.
 *  <A HREF="../features.html#palettes_sticky_buttons">
 *  <TT>FEATURE: palettes_sticky_buttons</TT></A>
 */

public class PaletteSticky extends PaletteDecorator {

  /** A checkbox that can make some modes sticky */
  private Checkbox _stickyCheckbox;

  public PaletteSticky(Palette subpal) { super(subpal); }

  /** the most recent button pressed */
  private ActiveComponent _lastClicked;

  /** Make the currently stuck button appear to be stuck down. This
   * gives the user the impression that the button is still depressed
   * and that future mouse clicks on the drawing area will make new
   * instances of the same type.
   *  <A HREF="../bugs.html#win_no_button_background">
   *  <FONT COLOR=660000><B>BUG: win_no_button_background</B></FONT></A>
   */
  protected void highlightStuckButton() {
    Enumeration cur = activeComps().elements();
    while (cur.hasMoreElements()) {
      ActiveComponent ac = (ActiveComponent) cur.nextElement();
      ac.unhighlight();
    }
    if (_stickyCheckbox.getState() && _lastClicked != null)
      _lastClicked.highlight();
  }


  /** Handle action events by finding which component was activated
   *  and executnig it's associated action. <p>
   *
   *  Needs-More-Work: When using a sticky tool with the sticky
   *  checkbox set, each Editor that the mouse enters will go into
   *  that Mode. But pushing the 'select' button on the palette will
   *  only make the _current_ Editor go back to select mode, any
   *  other open Editor's that were touched will stay in the stuck
   *  mode.  */
  public boolean action(Event e, Object what) {
    boolean ret = false;
    Editor ce = Globals.curEditor();

    if (e.target == _stickyCheckbox) {
      _lastClicked = null;
      if (!_stickyCheckbox.getState()) Globals.mode(null);
    }

    Enumeration cur = activeComps().elements();
    while (cur.hasMoreElements()) {
      ActiveComponent ac = (ActiveComponent) cur.nextElement();
      if (e.target == ac.component()) {
	if (!ac.canStick()) _stickyCheckbox.setState(false);
	Globals.setSticky(_stickyCheckbox.getState());
	ac.doIt(e);
	if (_stickyCheckbox.getState()) _lastClicked = ac;
	ret = true;
      }
    }
    highlightStuckButton();
    if (!_stickyCheckbox.getState()) ce.finishMode();
    return ret || super.action(e, what);
  }

  public void definePanel() {
    super.definePanel();
    _stickyCheckbox = new Checkbox("Sticky");
    _stickyCheckbox.setState(false);
    add("South", _stickyCheckbox);
    /* maybe add a status pane at the bottom... no, use PaletteMessage...*/
  }

} /* end class PaletteSticky */

