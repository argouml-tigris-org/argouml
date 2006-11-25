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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.application.api.QuadrantPanel;
import org.argouml.i18n.Translator;
import org.argouml.ui.explorer.ActionPerspectiveConfig;
import org.argouml.ui.explorer.DnDExplorerTree;
import org.argouml.ui.explorer.ExplorerTree;
import org.argouml.ui.explorer.ExplorerTreeModel;
import org.argouml.ui.explorer.NameOrder;
import org.argouml.ui.explorer.PerspectiveComboBox;
import org.argouml.ui.explorer.PerspectiveManager;
import org.argouml.ui.explorer.TypeThenNameOrder;
import org.tigris.toolbar.ToolBar;

/**
 * The upper-left pane of the main ArgoUML window, contains a tree view
 * of the UML model. Currently named "Explorer" instead of "Navigator".<p>
 *
 * The model can be viewed from different tree "Perspectives".<p>
 *
 * Perspectives are now built in the Perspective Manager.<p>
 */
class NavigatorPane
    extends JPanel
    implements QuadrantPanel {

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructs a new navigator panel.<p>
     *
     * This panel consists of a Combobox to select a navigation
     * perspective, a combobox to select ordering,
     * a JTree to display the UML model,
     * and a configuration dialog to tailor the perspectives.
     *
     * @param splash The splash screen where to show progress.
     */
    public NavigatorPane(SplashScreen splash) {

        JComboBox perspectiveCombo = new PerspectiveComboBox();
        JComboBox orderByCombo = new JComboBox();
        ExplorerTree tree = new DnDExplorerTree();
        ToolBar toolbar = new ToolBar();

        toolbar.setFloatable(false);
        toolbar.add(new ActionPerspectiveConfig());
        toolbar.add(perspectiveCombo);

        ToolBar toolbar2 = new ToolBar();

        toolbar2.setFloatable(false);

        orderByCombo.addItem(new TypeThenNameOrder());
        orderByCombo.addItem(new NameOrder());

        toolbar2.add(orderByCombo);

        JPanel toolbarpanel = new JPanel();
        toolbarpanel.setLayout(new BorderLayout());
        toolbarpanel.add(toolbar, BorderLayout.NORTH);
        toolbarpanel.add(toolbar2, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(toolbarpanel, BorderLayout.NORTH);
        add(new JScrollPane(tree), BorderLayout.CENTER);

        if (splash != null) {
            splash.getStatusBar().showStatus(Translator.localize(
		    "statusmsg.bar.making-navigator-pane-perspectives"));
            splash.getStatusBar().showProgress(25);
        }

        perspectiveCombo.addItemListener((ExplorerTreeModel) tree.getModel());
        orderByCombo.addItemListener((ExplorerTreeModel) tree.getModel());
        PerspectiveManager.getInstance().loadUserPerspectives();
    }

    ////////////////////////////////////////////////////////////////
    // methods

    /*
     * @see java.awt.Component#getMinimumSize()
     *
     * sets minimum size to 120,100
     */
    public Dimension getMinimumSize() {
        return new Dimension(120, 100);
    }

    /*
     * @see org.argouml.application.api.QuadrantPanel#getQuadrant()
     */
    public int getQuadrant() {
        return Q_TOP_LEFT;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 8403903607517813289L;
} /* end class NavigatorPane */
