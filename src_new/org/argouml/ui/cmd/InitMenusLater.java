// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.tigris.gef.base.AlignAction;
import org.tigris.gef.base.CmdReorder;
import org.tigris.gef.base.DistributeAction;

/**
 * Thread to initialize the submenus of the main menu of argouml (only third
 * level, so the ones with the triangle on windows systems).
 * 
 * Menu's and the mnemonics of menu's and the menuitems are separated in the
 * PropertyResourceBundle <code>menu.properties</code>.
 * 
 * menuitems are separated in the PropertyResourceBundle
 * <code>action.properties</code>.
 * 
 * The key's in menu.properties have the following structure:
 * 
 * menu: [file].[name of menu] e.g: menu.file
 * 
 * mnemonics of menu's: [file].[name of menu].mnemonic e.g: menu.file.mnemonic
 * 
 * mnemonics of menuitems: [file].[flag for item].[name of menuitem].mnemonic
 * e.g: menu.item.new.mnemonic
 */
class InitMenusLater implements Runnable {
    private JMenu align, distribute, reorder;

    /**
     * Constructs this new runnable to initialize the submenus.
     * 
     * @param a
     *            the alignment submenu
     * @param d
     *            the distribution submenu
     * @param r
     *            the reorder submenu
     */
    public InitMenusLater(JMenu a, JMenu d, JMenu r) {
        this.align = a;
        this.distribute = d;
        this.reorder = r;
    }

    /**
     * The submenus of argouml are created on startup in a seperate thread.
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        initMenus(this.align, this.distribute, this.reorder);
    }

    /**
     * Initialize submenus.
     * 
     * @param align
     * @param distribute
     * @param reorder
     */
    public static void initMenus(JMenu align, JMenu distribute, JMenu reorder) {
        initAlignMenu(align);
        initDistributeMenu(distribute);
        initReorderMenu(reorder);
    }

    /**
     * Initialize submenus of the Align menu.
     * 
     * @param align
     *            the Align menu
     */
    private static void initAlignMenu(JMenu align) {
        JMenuItem alignTops = align
                .add(new AlignAction(AlignAction.ALIGN_TOPS));
        GenericArgoMenuBar.setMnemonic(alignTops, "align tops");
        ShortcutMgr.assignAccelerator(alignTops, ShortcutMgr.ACTION_ALIGN_TOPS);

        JMenuItem alignBottoms = align.add(new AlignAction(
                AlignAction.ALIGN_BOTTOMS));
        GenericArgoMenuBar.setMnemonic(alignBottoms, "align bottoms");
        ShortcutMgr.assignAccelerator(alignBottoms,
                ShortcutMgr.ACTION_ALIGN_BOTTOMS);

        JMenuItem alignRights = align.add(new AlignAction(
                AlignAction.ALIGN_RIGHTS));
        GenericArgoMenuBar.setMnemonic(alignRights, "align rights");
        ShortcutMgr.assignAccelerator(alignRights,
                ShortcutMgr.ACTION_ALIGN_RIGHTS);

        JMenuItem alignLefts = align.add(new AlignAction(
                AlignAction.ALIGN_LEFTS));
        GenericArgoMenuBar.setMnemonic(alignLefts, "align lefts");
        ShortcutMgr.assignAccelerator(alignLefts,
                ShortcutMgr.ACTION_ALIGN_LEFTS);

        JMenuItem alignHCenters = align.add(new AlignAction(
                AlignAction.ALIGN_H_CENTERS));
        GenericArgoMenuBar.setMnemonic(alignHCenters,
                "align horizontal centers");
        ShortcutMgr.assignAccelerator(alignHCenters,
                ShortcutMgr.ACTION_ALIGN_H_CENTERS);

        JMenuItem alignVCenters = align.add(new AlignAction(
                AlignAction.ALIGN_V_CENTERS));
        GenericArgoMenuBar.setMnemonic(alignVCenters, "align vertical centers");
        ShortcutMgr.assignAccelerator(alignVCenters,
                ShortcutMgr.ACTION_ALIGN_V_CENTERS);

        JMenuItem alignToGrid = align.add(new AlignAction(
                AlignAction.ALIGN_TO_GRID));
        GenericArgoMenuBar.setMnemonic(alignToGrid, "align to grid");
        ShortcutMgr.assignAccelerator(alignToGrid,
                ShortcutMgr.ACTION_ALIGN_TO_GRID);
    }

    /**
     * Initialize submenus of the Distribute menu.
     * 
     * @param distribute
     *            the Distribute menu
     */
    private static void initDistributeMenu(JMenu distribute) {
        JMenuItem distributeHSpacing = distribute.add(new DistributeAction(
                DistributeAction.H_SPACING));
        GenericArgoMenuBar.setMnemonic(distributeHSpacing,
                "distribute horizontal spacing");
        ShortcutMgr.assignAccelerator(distributeHSpacing,
                ShortcutMgr.ACTION_DISTRIBUTE_H_SPACING);

        JMenuItem distributeHCenters = distribute.add(new DistributeAction(
                DistributeAction.H_CENTERS));
        GenericArgoMenuBar.setMnemonic(distributeHCenters,
                "distribute horizontal centers");
        ShortcutMgr.assignAccelerator(distributeHCenters,
                ShortcutMgr.ACTION_DISTRIBUTE_H_CENTERS);

        JMenuItem distributeVSpacing = distribute.add(new DistributeAction(
                DistributeAction.V_SPACING));
        GenericArgoMenuBar.setMnemonic(distributeVSpacing,
                "distribute vertical spacing");
        ShortcutMgr.assignAccelerator(distributeVSpacing,
                ShortcutMgr.ACTION_DISTRIBUTE_V_SPACING);

        JMenuItem distributeVCenters = distribute.add(new DistributeAction(
                DistributeAction.V_CENTERS));
        GenericArgoMenuBar.setMnemonic(distributeVCenters,
                "distribute vertical centers");
        ShortcutMgr.assignAccelerator(distributeVCenters,
                ShortcutMgr.ACTION_DISTRIBUTE_V_CENTERS);
    }

    /**
     * Initialize the submenus for the Reorder menu.
     * 
     * @param reorder
     *            the main Reorder menu
     */
    private static void initReorderMenu(JMenu reorder) {
        JMenuItem reorderBringForward = reorder.add(new CmdReorder(
                CmdReorder.BRING_FORWARD));
        GenericArgoMenuBar.setMnemonic(reorderBringForward,
                "reorder bring forward");
        ShortcutMgr.assignAccelerator(reorderBringForward,
                ShortcutMgr.ACTION_REORDER_FORWARD);

        JMenuItem reorderSendBackward = reorder.add(new CmdReorder(
                CmdReorder.SEND_BACKWARD));
        GenericArgoMenuBar.setMnemonic(reorderSendBackward,
                "reorder send backward");
        ShortcutMgr.assignAccelerator(reorderSendBackward,
                ShortcutMgr.ACTION_REORDER_BACKWARD);

        JMenuItem reorderBringToFront = reorder.add(new CmdReorder(
                CmdReorder.BRING_TO_FRONT));
        GenericArgoMenuBar.setMnemonic(reorderBringToFront,
                "reorder bring to front");
        ShortcutMgr.assignAccelerator(reorderBringToFront,
                ShortcutMgr.ACTION_REORDER_TO_FRONT);

        JMenuItem reorderSendToBack = reorder.add(new CmdReorder(
                CmdReorder.SEND_TO_BACK));
        GenericArgoMenuBar.setMnemonic(reorderSendToBack,
                "reorder send to back");
        ShortcutMgr.assignAccelerator(reorderSendToBack,
                ShortcutMgr.ACTION_REORDER_TO_BACK);
    }
} /* end class InitMenusLater */
