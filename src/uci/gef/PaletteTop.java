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

// File: PaletteTop.java
// Classes: PaletteTop
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** */
public class PaletteTop extends PaletteDecorator  {

  protected Frame _frame = null;

  /** Construct a new Palette */
  public PaletteTop(Palette subpal) { super(subpal); }

  public void frame(Frame f) { _frame = f; }

  /** Handle action events by finding which component was activated
   * and executnig it's associated action.*/
  public boolean action(Event e, Object what) {
    boolean ret = false;

    Enumeration cur = activeComps().elements();
    while (cur.hasMoreElements()) {
      ActiveComponent ac = (ActiveComponent) cur.nextElement();
      if (e.target == ac.component()) {
	ac.doIt(e);
	// needs-more-work: should go through a bottle-neck method
	ret = true;
      }
    }
    return ret || super.action(e, what);
  }

  /** Handle events, I just need to catch window destroy events, the rest
     go through action(). */
  public boolean handleEvent(Event e) {
    switch (e.id) {
    case Event.WINDOW_DESTROY: close(); return true;
    }

    return super.handleEvent(e);
  }

  /** First hide the window, then dispose of its data structures. */
  public void close() {
    if (_frame != null) {
      _frame.hide();
      _frame.dispose();
    }
  }

} /* end class PaletteTop */

