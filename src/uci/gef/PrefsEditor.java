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

// File: PrefsEditor.java
// Classes: PrefsEditor
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** A dialog box to let the user edit various preferences that effect
 *  the application.  Needs-More-Work: right now the only preferences
 *  that can be edited relate to redrawing. I should add prefs for
 *  handle colors, grid snap(?), and some
 *  behaviors... Needs-More-Work: This code could be deleted if I used
 *  a PropSheet to edit preferences instead.
 *
 * @see RedrawManager */

public class PrefsEditor extends Frame {

  /** A check box for using flicker-free graphics */
  private Checkbox _tryOffScreenCB;

  /** A check box for using straight or rectilinear arcs */
  private Checkbox _printGridCB;
  private Checkbox _printBackgroundCB;

  /** The "More Repaints" button.
   * @see RedrawManager#moreRepairs */
  private Button _moreRepaints;

  /** The "Fewer Repaints" button.
   * @see RedrawManager#fewerRepairs*/
  private Button _fewerRepaints;

  /** The close button */
  private Button _close;

  /** Construct a new PrefsEditor and get ready to open it. This can
   * be invoked via ActionOpenWindow.
   *
   * @see ActionOpenWindow */
  public PrefsEditor() {
    setTitle("Prefs");
    setBackground(Color.lightGray);
    _tryOffScreenCB = new Checkbox("Flicker-free repaint");
    _moreRepaints = new Button("More Repaints");
    _fewerRepaints = new Button("Fewer Repaints");
    _printGridCB = new Checkbox("Print Grid");
    _printBackgroundCB = new Checkbox("Print Background");
    Panel p = new Panel();
    p.setLayout(new GridLayout(6,1));
    _tryOffScreenCB.setState(Globals.getPrefs().getTryOffScreen());
    _printGridCB.setState(Globals.getPrefs().getPrintGrid());
    _printBackgroundCB.setState(Globals.getPrefs().getPrintBackground());
    p.add(_tryOffScreenCB);
    p.add(_moreRepaints);
    p.add(_fewerRepaints);
    p.add(_printGridCB);
    p.add(_printBackgroundCB);
    add("Center", p);
    _close = new Button("Close");
    Panel bottomPanel = new Panel();
    bottomPanel.add("Center",_close);
    add("South", bottomPanel);
    pack();
  }

  /** Close the PrefEditor window */
  public void close() { dispose(); }

  /** If the user wants to close the window, do it. */
  public boolean handleEvent(Event e) {
    switch(e.id) {
    case Event.WINDOW_DESTROY:
      if (e.target == this) {
	close();
	return true;
      }
      break;
    }
    return super.handleEvent(e);
  }

  /** If the use pushes one of the buttons, update
   *  Globals.prefs(). Needs-More-Work: It would be nice to have a more
   *  compact way to code this. I expect that every preference that can
   *  be edited will affect Globals.prefs() in a similiar way. */
  public boolean action(Event e, Object what) {
    Prefs p = Globals.getPrefs();
    if (e.target == _tryOffScreenCB) {
      p.setTryOffScreen(!p.getTryOffScreen());
      _tryOffScreenCB.setState(p.getTryOffScreen());
      return true;
    }
    if (e.target == _moreRepaints) {
      RedrawManager.moreRepairs();
      return true;
    }
    if (e.target == _fewerRepaints) {
      RedrawManager.fewerRepairs();
      return true;
    }
    else if (e.target == _close) {
      close();
      return true;
    }
    else if (e.target == _printGridCB) {
      p.setPrintGrid(!p.getPrintGrid());
      _printGridCB.setState(p.getPrintGrid());
      return true;
    }
    else if (e.target == _printBackgroundCB) {
      p.setPrintBackground(!p.getPrintBackground());
      _printBackgroundCB.setState(p.getPrintBackground());
      return true;
    }
    return false;
  }

} /* end class PrefsEditor */
