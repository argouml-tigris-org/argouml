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
    bindKey(9, new ActionSelectNext()); // should this be here or ModeSelect
    bindKey(9, Event.SHIFT_MASK, new ActionSelectNext());
    bindKey('g', new ActionGroup());
    bindKey('u', new ActionUngroup());

    bindKey('f', new ActionReorder(ActionReorder.BRING_FORWARD));
    bindKey('b', new ActionReorder(ActionReorder.SEND_BACKWARD));
    bindKey('F', new ActionReorder(ActionReorder.BRING_TO_FRONT));
    bindKey('B', new ActionReorder(ActionReorder.SEND_TO_BACK));

    bindKey(8, new ActionDispose());
    bindKey(8, Event.SHIFT_MASK, new ActionDispose());
    bindKey(127, new ActionDispose());
    bindKey(127, Event.SHIFT_MASK, new ActionDispose());

    bindKey('i', new ActionAlign(ActionAlign.ALIGN_TOPS));
    bindKey('j', new ActionAlign(ActionAlign.ALIGN_LEFTS));
    bindKey('k', new ActionAlign(ActionAlign.ALIGN_BOTTOMS));
    bindKey('l', new ActionAlign(ActionAlign.ALIGN_RIGHTS));

    bindKey(Event.LEFT, new ActionNudge(ActionNudge.LEFT));
    bindKey(Event.RIGHT, new ActionNudge(ActionNudge.RIGHT));
    bindKey(Event.UP, new ActionNudge(ActionNudge.UP));
    bindKey(Event.DOWN, new ActionNudge(ActionNudge.DOWN));

    bindKey(Event.LEFT, Event.SHIFT_MASK, new ActionNudge(ActionNudge.LEFT));
    bindKey(Event.RIGHT, Event.SHIFT_MASK, new ActionNudge(ActionNudge.RIGHT));
    bindKey(Event.UP, Event.SHIFT_MASK, new ActionNudge(ActionNudge.UP));
    bindKey(Event.DOWN, Event.SHIFT_MASK, new ActionNudge(ActionNudge.DOWN));

    bindCtrlKey(Event.LEFT, new ActionScroll(ActionScroll.LEFT));
    bindCtrlKey(Event.RIGHT, new ActionScroll(ActionScroll.RIGHT));
    bindCtrlKey(Event.UP, new ActionScroll(ActionScroll.UP));
    bindCtrlKey(Event.DOWN, new ActionScroll(ActionScroll.DOWN));
  }

} /* end class ModeExampleKeys */
