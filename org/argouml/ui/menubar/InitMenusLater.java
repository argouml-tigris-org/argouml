// $Id$
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

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.argouml.ui.ActionGoToEdit;
import org.argouml.ui.Actions;
import org.argouml.uml.ui.ActionLayout;
import org.tigris.gef.base.CmdAlign;
import org.tigris.gef.base.CmdDistribute;
import org.tigris.gef.base.CmdNudge;
import org.tigris.gef.base.CmdReorder;

/**
 * Thread to initialize the submenus of the main menu of argouml (only
 * third level, so the ones with the triangle on windows systems).
 */
class InitMenusLater implements Runnable {
    JMenu align, distribute, reorder, nudge, setPreferredSize, layout;
    JMenu editTabs, detailsTabs;

    /**
     * Constructs this new runnable to initialize the submenus.
     * @param align the alignment submenu
     * @param distribute the distribution submenu
     * @param reorder the reorder submenu
     * @param nudge the nudge submenu
     * @param layout the layout submenu
     * @param editTabs the edit tabs submenu
     */
    public InitMenusLater(JMenu align, JMenu distribute,
			  JMenu reorder, JMenu nudge,
			  JMenu layout, JMenu editTabs) {
	this.align = align;
	this.distribute = distribute;
	this.reorder = reorder;
	this.nudge = nudge;
	this.layout = layout;
	this.editTabs = editTabs;
    }

    /**
     * The submenus of argouml are created on startup in a seperate thread. 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        int menuShortcut = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        KeyStroke ctrlR = KeyStroke.getKeyStroke(KeyEvent.VK_R, menuShortcut);
        KeyStroke ctrlL = KeyStroke.getKeyStroke(KeyEvent.VK_L, menuShortcut);

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

        KeyStroke alt1 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.ALT_MASK);
        KeyStroke alt2 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.ALT_MASK);
        KeyStroke alt3 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.ALT_MASK);
        KeyStroke alt4 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.ALT_MASK);
        KeyStroke alt5 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_5, KeyEvent.ALT_MASK);
        KeyStroke alt6 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_6, KeyEvent.ALT_MASK);
        KeyStroke alt7 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_7, KeyEvent.ALT_MASK);
        KeyStroke alt8 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.ALT_MASK);
        KeyStroke alt9 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_9, KeyEvent.ALT_MASK);
        KeyStroke alt0 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_0, KeyEvent.ALT_MASK);

        KeyStroke altshift1 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_1,
				   KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK);
        KeyStroke altshift2 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_2,
				   KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK);
        KeyStroke altshift3 =
	    KeyStroke.getKeyStroke(KeyEvent.VK_3,
				   KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK);

	// ---------------------------------------------- Arrange Menu
		
        JMenuItem alignTops = align.add(new CmdAlign(CmdAlign.ALIGN_TOPS));
	GenericArgoMenuBar.setMnemonic(alignTops,"align tops", 'T');
		
        JMenuItem alignBottoms = 
	    align.add(new CmdAlign(CmdAlign.ALIGN_BOTTOMS));
	GenericArgoMenuBar.setMnemonic(alignBottoms,"align bottoms", 'B');
		
	JMenuItem alignRights = align.add(new CmdAlign(CmdAlign.ALIGN_RIGHTS));
	GenericArgoMenuBar.setMnemonic(alignRights,"align rights", 'R');
	GenericArgoMenuBar.setAccelerator(alignRights, ctrlR);
		
	JMenuItem alignLefts = align.add(new CmdAlign(CmdAlign.ALIGN_LEFTS));
	GenericArgoMenuBar.setMnemonic(alignLefts,"align lefts", 'L');
	GenericArgoMenuBar.setAccelerator(alignLefts, ctrlL);

	JMenuItem alignH_Centers = 
	    align.add(new CmdAlign(CmdAlign.ALIGN_H_CENTERS));
	GenericArgoMenuBar.setMnemonic(alignH_Centers,
				       "align horizontal centers", 'H');
			
	JMenuItem alignV_Centers = 
	    align.add(new CmdAlign(CmdAlign.ALIGN_V_CENTERS));
	GenericArgoMenuBar.setMnemonic(alignV_Centers,
				       "align vertical centers", 'V');

	JMenuItem alignToGrid = 
	    align.add(new CmdAlign(CmdAlign.ALIGN_TO_GRID));
	GenericArgoMenuBar.setMnemonic(alignToGrid,
				       "align to grid", 'G');


        JMenuItem distributeH_Spacing = 
	    distribute.add(new CmdDistribute(CmdDistribute.H_SPACING));
	GenericArgoMenuBar.setMnemonic(distributeH_Spacing,
				       "distribute horizontal spacing", 'H');
			
        JMenuItem distributeH_Centers = 
	    distribute.add(new CmdDistribute(CmdDistribute.H_CENTERS));
	GenericArgoMenuBar.setMnemonic(distributeH_Centers,
				       "distribute horizontal centers", 'O');
			
        JMenuItem distributeV_Spacing = 
	    distribute.add(new CmdDistribute(CmdDistribute.V_SPACING));
	GenericArgoMenuBar.setMnemonic(distributeV_Spacing,
				       "distribute vertical spacing", 'V');
			
        JMenuItem distributeV_Centers = 
	    distribute.add(new CmdDistribute(CmdDistribute.V_CENTERS));
	GenericArgoMenuBar.setMnemonic(distributeV_Centers,
				       "distribute vertical centers", 'E');
			

        JMenuItem reorderBringForward = 
	    reorder.add(new CmdReorder(CmdReorder.BRING_FORWARD));
	GenericArgoMenuBar.setMnemonic(reorderBringForward,
				       "reorder bring forward", 'F');
			
        JMenuItem reorderSendBackward = 
	    reorder.add(new CmdReorder(CmdReorder.SEND_BACKWARD));
	GenericArgoMenuBar.setMnemonic(reorderSendBackward,
				       "reorder send backward", 'B');
			
        JMenuItem reorderBringToFront = 
	    reorder.add(new CmdReorder(CmdReorder.BRING_TO_FRONT));
	GenericArgoMenuBar.setMnemonic(reorderBringToFront,
				       "reorder bring to front", 'R');
			
        JMenuItem reorderSendToBack = 
	    reorder.add(new CmdReorder(CmdReorder.SEND_TO_BACK));
	GenericArgoMenuBar.setMnemonic(reorderSendToBack,
				       "reorder send to back", 'S');


        JMenuItem nudgeLeft = nudge.add(new CmdNudge(CmdNudge.LEFT));
	GenericArgoMenuBar.setMnemonic(nudgeLeft,
				       "nudge left", 'L');

        JMenuItem nudgeRight = nudge.add(new CmdNudge(CmdNudge.RIGHT));
	GenericArgoMenuBar.setMnemonic(nudgeRight,
				       "nudge right", 'R');

        JMenuItem nudgeUp = nudge.add(new CmdNudge(CmdNudge.UP));
	GenericArgoMenuBar.setMnemonic(nudgeUp,
				       "nudge up", 'U');

        JMenuItem nudgeDown = nudge.add(new CmdNudge(CmdNudge.DOWN));
	GenericArgoMenuBar.setMnemonic(nudgeDown,
				       "nudge down", 'D');


        JMenuItem autoLayout =
	    layout.add(new ActionLayout("action.layout-automatic"));
	GenericArgoMenuBar.setMnemonic(autoLayout,"layout automatic", 'A');
        JMenuItem incrLayout =
	    layout.add(new ActionLayout("action.layout-incremental"));
	GenericArgoMenuBar.setMnemonic(incrLayout,"layout incremental", 'I');
        /** incremental layout is currently not implemented */
        incrLayout.setEnabled(false);

	// ---------------------------------------------- View Menu
		
        JMenuItem nextEditItem = editTabs.add(Actions.NextEditTab);
	GenericArgoMenuBar.setMnemonic(nextEditItem,"next editing tab", 'N');
        nextEditItem.setAccelerator(F6);
        editTabs.addSeparator();

        JMenuItem tabe1Item =
	    editTabs.add(new ActionGoToEdit("action.as-diagram"));
	GenericArgoMenuBar.setMnemonic(tabe1Item,"as diagram", 'D');
        tabe1Item.setAccelerator(altshift1);
        JMenuItem tabe2Item =
	    editTabs.add(new ActionGoToEdit("action.as-table"));
	GenericArgoMenuBar.setMnemonic(tabe2Item,"as table", 'T');
        tabe2Item.setAccelerator(altshift2);
        JMenuItem tabe3Item =
	    editTabs.add(new ActionGoToEdit("action.as-metrics"));
	GenericArgoMenuBar.setMnemonic(tabe3Item,"as metrics", 'M');
        tabe3Item.setAccelerator(altshift3);
    }
} /* end class InitMenusLater */
