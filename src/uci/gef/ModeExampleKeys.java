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

// File: ModeExampleKeys.java
// Classes: ModeExampleKeys
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

/** Mode to detect many common keystroke commands and execute the
 *  appropriate Action. For example, arrow keys cause the selected
 *  Figs to be nudged, Control-arrows scroll the view, 'g'
 *  groups objects, 'u' ungroups objects, etc.
 *
 *  @see Editor
 *  @see ActionNudge
 *  @see AcionAlign
 *  @see ActionScroll
 *  @see ActionReorder
 *  @see ActionGroup
 *  @see ActionUngroup */

public class ModeExampleKeys extends Mode {

  ////////////////////////////////////////////////////////////////
  // constructor

  public ModeExampleKeys(Editor par) { super(par); }

  ////////////////////////////////////////////////////////////////
  // accessors

  public boolean canExit() { return false; }

  ////////////////////////////////////////////////////////////////
  // key bindings

  public void bindKeys() {
     // should this be here or ModeSelect
    bindKey(KeyEvent.VK_TAB, new ActionSelectNext());
    bindKey(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK, new ActionSelectNext());
    bindKey(KeyEvent.VK_G, new ActionGroup());
    bindKey(KeyEvent.VK_U, new ActionUngroup());

    bindKey(KeyEvent.VK_F, new ActionReorder(ActionReorder.BRING_FORWARD));
    bindKey(KeyEvent.VK_B, new ActionReorder(ActionReorder.SEND_BACKWARD));
    bindKey(KeyEvent.VK_F, InputEvent.SHIFT_MASK,
	    new ActionReorder(ActionReorder.BRING_TO_FRONT));
    bindKey(KeyEvent.VK_B, InputEvent.SHIFT_MASK,
	    new ActionReorder(ActionReorder.SEND_TO_BACK));

    bindKey(KeyEvent.VK_BACK_SPACE, new ActionDispose());
    bindKey(KeyEvent.VK_BACK_SPACE, InputEvent.SHIFT_MASK, new ActionDispose());
    bindKey(KeyEvent.VK_DELETE, new ActionDispose());
    bindKey(KeyEvent.VK_DELETE, Event.SHIFT_MASK, new ActionDispose());

    bindKey(KeyEvent.VK_I, new ActionAlign(ActionAlign.ALIGN_TOPS));
    bindKey(KeyEvent.VK_J, new ActionAlign(ActionAlign.ALIGN_LEFTS));
    bindKey(KeyEvent.VK_K, new ActionAlign(ActionAlign.ALIGN_BOTTOMS));
    bindKey(KeyEvent.VK_L, new ActionAlign(ActionAlign.ALIGN_RIGHTS));

    bindKey(KeyEvent.VK_LEFT, new ActionNudge(ActionNudge.LEFT));
    bindKey(KeyEvent.VK_RIGHT, new ActionNudge(ActionNudge.RIGHT));
    bindKey(KeyEvent.VK_UP, new ActionNudge(ActionNudge.UP));
    bindKey(KeyEvent.VK_DOWN, new ActionNudge(ActionNudge.DOWN));

    bindKey(KeyEvent.VK_LEFT, InputEvent.SHIFT_MASK,
	    new ActionNudge(ActionNudge.LEFT));
    bindKey(KeyEvent.VK_RIGHT, InputEvent.SHIFT_MASK,
	    new ActionNudge(ActionNudge.RIGHT));
    bindKey(KeyEvent.VK_UP, InputEvent.SHIFT_MASK,
	    new ActionNudge(ActionNudge.UP));
    bindKey(KeyEvent.VK_DOWN, InputEvent.SHIFT_MASK,
	    new ActionNudge(ActionNudge.DOWN));

    bindCtrlKey(KeyEvent.VK_LEFT, new ActionScroll(ActionScroll.LEFT));
    bindCtrlKey(KeyEvent.VK_RIGHT, new ActionScroll(ActionScroll.RIGHT));
    bindCtrlKey(KeyEvent.VK_UP, new ActionScroll(ActionScroll.UP));
    bindCtrlKey(KeyEvent.VK_DOWN, new ActionScroll(ActionScroll.DOWN));
  }

} /* end class ModeExampleKeys */
