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




// File: CmdReorder.java
// Classes: CmdReorder
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** Cmd to change the back-to-front ordering of Fig's.
 *
 * @see LayerDiagram#reorder */

public class CmdReorder extends Cmd {
  ////////////////////////////////////////////////////////////////
  // constants
  public static final int SEND_TO_BACK = 1;
  public static final int BRING_TO_FRONT = 2;
  public static final int SEND_BACKWARD = 3;
  public static final int BRING_FORWARD = 4;

  public static CmdReorder SendToBack = new CmdReorder(SEND_TO_BACK);
  public static CmdReorder BringToFront = new CmdReorder(BRING_TO_FRONT);
  public static CmdReorder SendBackward = new CmdReorder(SEND_BACKWARD);
  public static CmdReorder BringForward = new CmdReorder(BRING_FORWARD);

  ////////////////////////////////////////////////////////////////
  // instance variables
  private int function;

  ////////////////////////////////////////////////////////////////
  // constructor

  /** Construct a new CmdReorder with the given reordering
    constrant (see above) */
  public CmdReorder(int f) {
    super(wordFor(f));
    function = f;
  }


  protected static String wordFor(int f) {
    switch (f) {
    case SEND_BACKWARD: return "Backward";
    case SEND_TO_BACK: return "To Back";
    case BRING_FORWARD: return "Forward";
    case BRING_TO_FRONT: return "To Front";
    }
    return "";
  }
  
  ////////////////////////////////////////////////////////////////
  // Cmd API

  public void doIt() {
    Editor ce = Globals.curEditor();
    LayerManager lm = ce.getLayerManager();
    SelectionManager sm = ce.getSelectionManager();
    sm.startTrans();
    sm.reorder(function, lm.getActiveLayer());
    sm.endTrans();
    ce.repairDamage();
  }

  public void undoIt() {
    System.out.println("Connot undo CmdReorder, yet");
  }

  static final long serialVersionUID = -3129263720315040861L;

} /* end class CmdReorder */

