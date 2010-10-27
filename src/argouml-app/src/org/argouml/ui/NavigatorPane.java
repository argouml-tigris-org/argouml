/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.argouml.i18n.Translator;
import org.argouml.taskmgmt.ProgressMonitor;
import org.argouml.ui.explorer.ActionPerspectiveConfig;
import org.argouml.ui.explorer.DnDExplorerTree;
import org.argouml.ui.explorer.ExplorerTree;
import org.argouml.ui.explorer.ExplorerTreeModel;
import org.argouml.ui.explorer.NameOrder;
import org.argouml.ui.explorer.PerspectiveComboBox;
import org.argouml.ui.explorer.PerspectiveManager;
import org.argouml.ui.explorer.TypeThenNameOrder;
import org.tigris.toolbar.ToolBarFactory;

/**
 * The upper-left pane of the main ArgoUML window, contains a tree view
 * of the UML model. Currently named "Explorer" instead of "Navigator".<p>
 *
 * The model can be viewed from different tree "Perspectives".<p>
 *
 * Perspectives are now built in the Perspective Manager.<p>
 */
class NavigatorPane
    extends JPanel {

    /**
     * Constructs a new navigator panel.<p>
     *
     * This panel consists of a Combobox to select a navigation
     * perspective, a combobox to select ordering,
     * a JTree to display the UML model,
     * and a configuration dialog to tailor the perspectives.
     *
     * @param splash The splash screen where to show progress.
     * @deprecated for 0.31.7 by tfmorris.  
     * Use {@link NavigatorPane#NavigatorPane(ProgressMonitor)}.
     */
    @Deprecated
    public NavigatorPane(SplashScreen splash) {
        this(splash, null);
    }

    /**
     * Construct a new navigator panel.<p>
     *
     * This panel consists of a Combobox to select a navigation
     * perspective, a combobox to select ordering,
     * a JTree to display the UML model,
     * and a configuration dialog to tailor the perspectives.
     *
     * @param pm ProgressMonitor to receive progress updates.  May be null.
     */
    @Deprecated
    public NavigatorPane(ProgressMonitor pm) {
        this(null, pm);
    }
    
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
    private NavigatorPane(SplashScreen splash, ProgressMonitor pm) {

        JComboBox perspectiveCombo = new PerspectiveComboBox();
        JComboBox orderByCombo = new JComboBox();
        ExplorerTree tree = new DnDExplorerTree();

        Collection<Object> toolbarTools = new ArrayList<Object>();
        toolbarTools.add(new ActionPerspectiveConfig());
        toolbarTools.add(perspectiveCombo);
        JToolBar toolbar = (new ToolBarFactory(toolbarTools)).createToolBar();
        toolbar.setFloatable(false);

        orderByCombo.addItem(new TypeThenNameOrder());
        orderByCombo.addItem(new NameOrder());

        Collection<Object> toolbarTools2 = new ArrayList<Object>();
        toolbarTools2.add(orderByCombo);
        JToolBar toolbar2 = (new ToolBarFactory(toolbarTools2)).createToolBar();
        toolbar2.setFloatable(false);

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
        } else if (pm != null) {
            pm.updateSubTask(Translator.localize(
                    "statusmsg.bar.making-navigator-pane-perspectives"));
            pm.updateProgress(25);
        }

        perspectiveCombo.addItemListener((ExplorerTreeModel) tree.getModel());
        orderByCombo.addItemListener((ExplorerTreeModel) tree.getModel());
        PerspectiveManager.getInstance().loadUserPerspectives();
    }

    /*
     * @see java.awt.Component#getMinimumSize()
     *
     * sets minimum size to 120,100
     */
    public Dimension getMinimumSize() {
        return new Dimension(120, 100);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 8403903607517813289L;
} /* end class NavigatorPane */
