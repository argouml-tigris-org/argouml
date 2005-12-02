// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.ui.cmd;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.tigris.gef.base.AlignAction;
import org.tigris.gef.base.CmdDistribute;
import org.tigris.gef.base.CmdNudge;
import org.tigris.gef.base.CmdReorder;

/**
 * Thread to initialize the submenus of the main menu of argouml (only
 * third level, so the ones with the triangle on windows systems).
 *
 *  Menu's and the mnemonics of menu's and the menuitems are separated
 *  in the PropertyResourceBundle <code>menu.properties</code>.
 *
 *  menuitems are separated in the PropertyResourceBundle
 *  <code>action.properties</code>.
 *
 *  The key's in menu.properties have the following structure:
 *
 *  menu:                    [file].[name of menu]
 *   e.g:                    menu.file
 *
 *  mnemonics of menu's:     [file].[name of menu].mnemonic
 *   e.g:                    menu.file.mnemonic
 *
 *  mnemonics of menuitems:  [file].[flag for item].[name of menuitem].mnemonic
 *   e.g:                    menu.item.new.mnemonic
 */
class InitMenusLater implements Runnable {
    private JMenu align, distribute, reorder, nudge;

    /**
     * Constructs this new runnable to initialize the submenus.
     * @param a the alignment submenu
     * @param d the distribution submenu
     * @param r the reorder submenu
     * @param n the nudge submenu
     */
    public InitMenusLater(JMenu a, JMenu d,
			  JMenu r, JMenu n
                          ) {
	this.align = a;
	this.distribute = d;
	this.reorder = r;
	this.nudge = n;
    }

    /**
     * The submenus of argouml are created on startup in a seperate thread.
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {
        int menuShortcut = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        KeyStroke ctrlR = KeyStroke.getKeyStroke(KeyEvent.VK_R, menuShortcut);
        KeyStroke ctrlL = KeyStroke.getKeyStroke(KeyEvent.VK_L, menuShortcut);

//        KeyStroke f1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
//        KeyStroke f2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
//        KeyStroke f3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
//        KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
//        KeyStroke f5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
//        KeyStroke f6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
//        KeyStroke f7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
//        KeyStroke f8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0);
//        KeyStroke f9 = KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0);
//        KeyStroke f10 = KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0);
//
//        KeyStroke alt1 =
//	    KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.ALT_MASK);
//        KeyStroke alt2 =
//	    KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.ALT_MASK);
//        KeyStroke alt3 =
//	    KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.ALT_MASK);
//        KeyStroke alt4 =
//	    KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.ALT_MASK);
//        KeyStroke alt5 =
//	    KeyStroke.getKeyStroke(KeyEvent.VK_5, KeyEvent.ALT_MASK);
//        KeyStroke alt6 =
//	    KeyStroke.getKeyStroke(KeyEvent.VK_6, KeyEvent.ALT_MASK);
//        KeyStroke alt7 =
//	    KeyStroke.getKeyStroke(KeyEvent.VK_7, KeyEvent.ALT_MASK);
//        KeyStroke alt8 =
//	    KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.ALT_MASK);
//        KeyStroke alt9 =
//	    KeyStroke.getKeyStroke(KeyEvent.VK_9, KeyEvent.ALT_MASK);
//        KeyStroke alt0 =
//	    KeyStroke.getKeyStroke(KeyEvent.VK_0, KeyEvent.ALT_MASK);
//
//        KeyStroke altshift1 = KeyStroke.getKeyStroke(KeyEvent.VK_1,
//				   KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK);
//        KeyStroke altshift2 = KeyStroke.getKeyStroke(KeyEvent.VK_2,
//				   KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK);
//        KeyStroke altshift3 = KeyStroke.getKeyStroke(KeyEvent.VK_3,
//				   KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK);

	// ---------------------------------------------- Arrange Menu
        JMenuItem alignTops =
            align.add(new AlignAction(AlignAction.ALIGN_TOPS));
	GenericArgoMenuBar.setMnemonic(alignTops, "align tops");

        JMenuItem alignBottoms =
	    align.add(new AlignAction(AlignAction.ALIGN_BOTTOMS));
	GenericArgoMenuBar.setMnemonic(alignBottoms, "align bottoms");

	JMenuItem alignRights =
            align.add(new AlignAction(AlignAction.ALIGN_RIGHTS));
	GenericArgoMenuBar.setMnemonic(alignRights, "align rights");
	GenericArgoMenuBar.setAccelerator(alignRights, ctrlR);

	JMenuItem alignLefts =
            align.add(new AlignAction(AlignAction.ALIGN_LEFTS));
	GenericArgoMenuBar.setMnemonic(alignLefts, "align lefts");
	GenericArgoMenuBar.setAccelerator(alignLefts, ctrlL);

	JMenuItem alignHCenters =
	    align.add(new AlignAction(AlignAction.ALIGN_H_CENTERS));
	GenericArgoMenuBar.setMnemonic(alignHCenters,
                                       "align horizontal centers");

	JMenuItem alignVCenters =
	    align.add(new AlignAction(AlignAction.ALIGN_V_CENTERS));
	GenericArgoMenuBar.setMnemonic(alignVCenters,
				       "align vertical centers");

	JMenuItem alignToGrid =
	    align.add(new AlignAction(AlignAction.ALIGN_TO_GRID));
	GenericArgoMenuBar.setMnemonic(alignToGrid, "align to grid");

        JMenuItem distributeHSpacing =
	    distribute.add(new CmdDistribute(CmdDistribute.H_SPACING));
	GenericArgoMenuBar.setMnemonic(distributeHSpacing,
				       "distribute horizontal spacing");

        JMenuItem distributeHCenters =
	    distribute.add(new CmdDistribute(CmdDistribute.H_CENTERS));
	GenericArgoMenuBar.setMnemonic(distributeHCenters,
				       "distribute horizontal centers");

        JMenuItem distributeVSpacing =
	    distribute.add(new CmdDistribute(CmdDistribute.V_SPACING));
	GenericArgoMenuBar.setMnemonic(distributeVSpacing,
				       "distribute vertical spacing");

        JMenuItem distributeVCenters =
	    distribute.add(new CmdDistribute(CmdDistribute.V_CENTERS));
	GenericArgoMenuBar.setMnemonic(distributeVCenters,
				       "distribute vertical centers");

        if (reorder != null) {
            JMenuItem reorderBringForward =
                reorder.add(new CmdReorder(CmdReorder.BRING_FORWARD));
                GenericArgoMenuBar.setMnemonic(reorderBringForward,
                               "reorder bring forward");

                JMenuItem reorderSendBackward =
                reorder.add(new CmdReorder(CmdReorder.SEND_BACKWARD));
                GenericArgoMenuBar.setMnemonic(reorderSendBackward,
                               "reorder send backward");

                JMenuItem reorderBringToFront =
                reorder.add(new CmdReorder(CmdReorder.BRING_TO_FRONT));
                GenericArgoMenuBar.setMnemonic(reorderBringToFront,
                               "reorder bring to front");

                JMenuItem reorderSendToBack =
                reorder.add(new CmdReorder(CmdReorder.SEND_TO_BACK));
                GenericArgoMenuBar.setMnemonic(reorderSendToBack,
                               "reorder send to back");

        }
        
        JMenuItem nudgeLeft = nudge.add(new CmdNudge(CmdNudge.LEFT));
	GenericArgoMenuBar.setMnemonic(nudgeLeft, "nudge left");

        JMenuItem nudgeRight = nudge.add(new CmdNudge(CmdNudge.RIGHT));
	GenericArgoMenuBar.setMnemonic(nudgeRight, "nudge right");

        JMenuItem nudgeUp = nudge.add(new CmdNudge(CmdNudge.UP));
	GenericArgoMenuBar.setMnemonic(nudgeUp, "nudge up");

        JMenuItem nudgeDown = nudge.add(new CmdNudge(CmdNudge.DOWN));
	GenericArgoMenuBar.setMnemonic(nudgeDown, "nudge down");
    }
} /* end class InitMenusLater */
