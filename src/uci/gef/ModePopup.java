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



package uci.gef;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import uci.ui.PopupGenerator;


/** A permanent Mode to catch right-mouse-button events and show a
 *  popup menu.  Needs-more-work: this is not fully implemented
 *  yet. It should ask the Fig under the mouse what menu it should
 *  offer. */

public class ModePopup extends Mode {

  ////////////////////////////////////////////////////////////////
  //  constructor

  public ModePopup(Editor par) { super(par); }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Always false because I never want to get out of popup mode. */
  public boolean canExit() { return false; }


  public String instructions() { return " "; }

  public void showPopup(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    Fig underMouse = _editor.hit(x, y);
    if (!(underMouse instanceof PopupGenerator)) return;
    _editor.getSelectionManager().select(underMouse);
    JPopupMenu popup = new JPopupMenu("test");
    Vector actions = ((PopupGenerator)underMouse).getPopUpActions(me);
    int size = actions.size();
    for (int i = 0; i < size; ++i) {
      Object a = actions.elementAt(i);
      if (a instanceof AbstractAction) popup.add((AbstractAction) a);
      else if (a instanceof JMenu) popup.add((JMenu) a);
    }
    popup.show(_editor.getAwtComponent(), me.getX(), me.getY());
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** Show a popup menu on right-mouse-button down. */
  // works for Solaris
  //  public void mousePressed(MouseEvent me) {
  //     System.out.println("mousePressed");
  //     if (me.isPopupTrigger()) {
  //       showPopup(me);
  //       me.consume();
  //     }
  //     else System.out.println("not isPopupTrigger mousePressed");
  //}

  /** Show a popup menu on right-mouse-button up. */
  // works for Windows
  public void mouseReleased(MouseEvent me) {
    //System.out.println("mouseReleased");
    if (me.isPopupTrigger() || me.getModifiers() == InputEvent.BUTTON3_MASK) {
      showPopup(me);
      me.consume();
    }
    //else System.out.println("not isPopupTrigger mouseReleased");
  }

} /* end class ModePopup */
