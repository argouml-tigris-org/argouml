// Copyright (c) 1996-2002 The Regents of the University of California. All
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


package org.argouml.ui.menubar;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.tigris.gef.base.*;
import org.tigris.gef.ui.*;
import org.tigris.gef.util.*;

import org.argouml.application.api.*;
import org.argouml.application.events.*;
import org.argouml.ui.*;
import org.argouml.ui.cmd.CmdSetPreferredSize;
import org.argouml.uml.ui.*;


class InitMenusLater implements Runnable {
  JMenu align, distribute, reorder, nudge, setPreferredSize, layout;
  JMenu editTabs, detailsTabs;

  public InitMenusLater(JMenu a, JMenu d, JMenu r, JMenu n, JMenu sPS, 
                        JMenu l, JMenu et, JMenu dt) {
    align = a;
    distribute = d;
    reorder = r;
    nudge = n;
    setPreferredSize = sPS;
    layout = l;
    editTabs = et;
    detailsTabs = dt;
  }

  public void run() {
    KeyStroke ctrlR = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK);
    KeyStroke ctrlL = KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_MASK);

    KeyStroke F1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
    KeyStroke F2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
    KeyStroke F3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
    KeyStroke F4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
    KeyStroke F5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
    KeyStroke F6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
    KeyStroke F7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
    KeyStroke F8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0);
    KeyStroke F9 = KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0);
    KeyStroke F10 = KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0);

    KeyStroke alt1 = KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.ALT_MASK);
    KeyStroke alt2 = KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.ALT_MASK);
    KeyStroke alt3 = KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.ALT_MASK);
    KeyStroke alt4 = KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.ALT_MASK);
    KeyStroke alt5 = KeyStroke.getKeyStroke(KeyEvent.VK_5, KeyEvent.ALT_MASK);
    KeyStroke alt6 = KeyStroke.getKeyStroke(KeyEvent.VK_6, KeyEvent.ALT_MASK);
    KeyStroke alt7 = KeyStroke.getKeyStroke(KeyEvent.VK_7, KeyEvent.ALT_MASK);
    KeyStroke alt8 = KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.ALT_MASK);
    KeyStroke alt9 = KeyStroke.getKeyStroke(KeyEvent.VK_9, KeyEvent.ALT_MASK);
    KeyStroke alt0 = KeyStroke.getKeyStroke(KeyEvent.VK_0, KeyEvent.ALT_MASK);

    KeyStroke altshift1 =
      KeyStroke.getKeyStroke(KeyEvent.VK_1,
			     KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK);
    KeyStroke altshift2 =
      KeyStroke.getKeyStroke(KeyEvent.VK_2,
			     KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK);
    KeyStroke altshift3 =
      KeyStroke.getKeyStroke(KeyEvent.VK_3,
			     KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK);

    align.add(new CmdAlign(CmdAlign.ALIGN_TOPS));
    align.add(new CmdAlign(CmdAlign.ALIGN_BOTTOMS));
    GenericArgoMenuBar.setAccelerator(align.add(new CmdAlign(CmdAlign.ALIGN_RIGHTS)),ctrlR);
    GenericArgoMenuBar.setAccelerator(align.add(new CmdAlign(CmdAlign.ALIGN_LEFTS)),ctrlL);

    align.add(new CmdAlign(CmdAlign.ALIGN_H_CENTERS));
    align.add(new CmdAlign(CmdAlign.ALIGN_V_CENTERS));
    align.add(new CmdAlign(CmdAlign.ALIGN_TO_GRID));

    distribute.add(new CmdDistribute(CmdDistribute.H_SPACING));
    distribute.add(new CmdDistribute(CmdDistribute.H_CENTERS));
    distribute.add(new CmdDistribute(CmdDistribute.V_SPACING));
    distribute.add(new CmdDistribute(CmdDistribute.V_CENTERS));

    reorder.add(new CmdReorder(CmdReorder.BRING_FORWARD));
    reorder.add(new CmdReorder(CmdReorder.SEND_BACKWARD));
    reorder.add(new CmdReorder(CmdReorder.BRING_TO_FRONT));
    reorder.add(new CmdReorder(CmdReorder.SEND_TO_BACK));
    
   

    nudge.add(new CmdNudge(CmdNudge.LEFT));
    nudge.add(new CmdNudge(CmdNudge.RIGHT));
    nudge.add(new CmdNudge(CmdNudge.UP));
    nudge.add(new CmdNudge(CmdNudge.DOWN));


    setPreferredSize.
        add(new CmdSetPreferredSize(CmdSetPreferredSize.MINIMUM_SIZE));
    setPreferredSize.
        add(new CmdSetPreferredSize(CmdSetPreferredSize.PREFERRED_SIZE));

    JMenuItem autoLayout = layout.add(new ActionLayout("Automatic"));
    JMenuItem incrLayout = layout.add(new ActionLayout("Incremental"));
    /** incremental layout is currently not implemented */
    incrLayout.setEnabled(false);

    JMenuItem nextEditItem = editTabs.add(Actions.NextEditTab);
    nextEditItem.setAccelerator(F6);
    editTabs.addSeparator();

    JMenuItem tabe1Item = editTabs.add(new ActionGoToEdit("As Diagram"));
    tabe1Item.setAccelerator(altshift1);
    JMenuItem tabe2Item = editTabs.add(new ActionGoToEdit("As Table"));
    tabe2Item.setAccelerator(altshift2);
    JMenuItem tabe3Item = editTabs.add(new ActionGoToEdit("As Metrics"));
    tabe3Item.setAccelerator(altshift3);

//    JMenuItem nextDetailsItem = detailsTabs.add(Actions.NextDetailsTab);
//    nextDetailsItem.setAccelerator(F5);
//    detailsTabs.addSeparator();

    JMenuItem tab1Item = detailsTabs.add(new ActionGoToDetails("ToDo Item"));
    tab1Item.setAccelerator(alt1);
    JMenuItem tab2Item = detailsTabs.add(new ActionGoToDetails("Properties"));
    tab2Item.setAccelerator(alt2);
    JMenuItem tab3Item = detailsTabs.add(new ActionGoToDetails("Javadocs"));
    tab3Item.setAccelerator(alt3);
    JMenuItem tab4Item = detailsTabs.add(new ActionGoToDetails("Source"));
    tab4Item.setAccelerator(alt4);
    JMenuItem tab5Item = detailsTabs.add(new ActionGoToDetails("Constraints"));
    tab5Item.setAccelerator(alt5);
    JMenuItem tab6Item = detailsTabs.add(new ActionGoToDetails("Tagged Values"));
    tab6Item.setAccelerator(alt6);
    JMenuItem tab7Item = detailsTabs.add(new ActionGoToDetails("Checklist"));
    tab7Item.setAccelerator(alt7);
    JMenuItem tab8Item = detailsTabs.add(new ActionGoToDetails("History"));
    tab8Item.setAccelerator(alt8);
    //JMenuItem tab9Item = detailsTabs.add(new ActionGoToDetails(""));
    //tab9Item.setAccelerator(alt9);
    //JMenuItem tab0Item = detailsTabs.add(new ActionGoToDetails(""));
    //tab0Item.setAccelerator(alt0);
  }
} /* end class InitMenusLater */
