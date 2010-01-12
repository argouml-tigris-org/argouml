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

// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.i18n.Translator;

/**
 * Class that handles toolbars show/hide functionality.
 * 
 * @author Aleksandar
 */
public class ArgoToolbarManager {
    /**
     * Menu item to be shown in popup menu and submenu.
     * 
     * @author Aleksandar
     */
    private class ToolbarManagerMenuItemAction extends AbstractAction {
        /**
         * Key to which this action is associated
         */
        private Object key;

        /**
         * Toolbars that will be shown or hidden according to this action
         */
        private ArrayList<JToolBar> toolbars = new ArrayList<JToolBar>();

        public ToolbarManagerMenuItemAction(String name, Object newKey) {
            super(name);
            this.key = newKey;
            toolbars = new ArrayList<JToolBar>();
        }

        public Object getKey() {
            return key;
        }

        public ArrayList<JToolBar> getToolbars() {
            return toolbars;
        }

        public void actionPerformed(final ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    for (JToolBar toolbar : getToolbars()) {
                        toolbar.setVisible(((JCheckBoxMenuItem) e.getSource())
                                .isSelected());

                        // Make this change persistant
                        ConfigurationKey configurationKey = Configuration
                                .makeKey(ArgoToolbarManager.KEY_NAME, toolbar
                                        .getName());
                        Configuration.setString(configurationKey,
                                ((Boolean) toolbar.isVisible()).toString());
                    }
                }
            });

            for (JMenuItem menuItem : allMenuItems) {
                if (menuItem.getAction().equals(this)) {
                    menuItem.setSelected(((JCheckBoxMenuItem) e.getSource())
                            .isSelected());
                }
            }
        }
    }

    /**
     * Key name for all toolabar settings. Every toolbar settings is subkey of
     * this key.
     */
    private static final String KEY_NAME = "toolbars";

    /**
     * Single instance.
     */
    private static ArgoToolbarManager instance;

    /**
     * Popup menu.
     */
    private JPopupMenu popup;

    /**
     * Menu
     */
    private JMenu menu;

    /**
     * All menu items
     */
    private ArrayList<JMenuItem> allMenuItems = new ArrayList<JMenuItem>();

    /**
     * Private constructor.
     */
    private ArgoToolbarManager() {

    }

    /**
     * Get single instance.
     * 
     * @return single instance.
     */
    public static ArgoToolbarManager getInstance() {
        if (instance == null) {
            instance = new ArgoToolbarManager();
        }
        return instance;
    }

    /**
     * Registers new toolbar.
     * 
     * @param key Class that is toolbar connected to, or toolbar object itself
     *            (if there is just one toolbar of that kind in the application.
     * @param toolbar new toolbar to register
     * @param prefferedMenuPosition preffered menu postition, -1 for the last
     *            postition
     */
    private void registerNew(Object key, JToolBar newToolbar,
            int prefferedMenuPosition) {
        // If menus don't containt menu item necessary for this class, create it
        JCheckBoxMenuItem wantedMenuItem = null;
        for (int i = 0; i < getMenu().getItemCount(); i++) {
            ToolbarManagerMenuItemAction menuItemAction = 
                (ToolbarManagerMenuItemAction) getMenu()
                    .getItem(i).getAction();
            if (menuItemAction.getKey().equals(key)) {
                wantedMenuItem = (JCheckBoxMenuItem) getMenu().getItem(i);
            }
        }

        // If there is persistant state for this toolbar, respect it,
        // or add it to persistance data
        boolean visibility = getConfiguredToolbarAppearance(newToolbar
                .getName());
        newToolbar.setVisible(visibility);

        // Create new menu item if it doesn't exist for this class.
        if (wantedMenuItem == null) {
            ToolbarManagerMenuItemAction action = 
                new ToolbarManagerMenuItemAction(
                    Translator.localize(newToolbar.getName()), key);
            wantedMenuItem = new JCheckBoxMenuItem(Translator
                    .localize(newToolbar.getName()), newToolbar.isVisible());
            wantedMenuItem.setAction(action);

            JCheckBoxMenuItem menuItem2 = new JCheckBoxMenuItem(Translator
                    .localize(newToolbar.getName()), newToolbar.isVisible());
            menuItem2.setAction(action);

            getMenu().insert(wantedMenuItem, prefferedMenuPosition);
            getPopupMenu().insert(menuItem2, prefferedMenuPosition);
            allMenuItems.add(wantedMenuItem);
            allMenuItems.add(menuItem2);
        }

        ArrayList<JToolBar> toolBarsForClass = 
            ((ToolbarManagerMenuItemAction) wantedMenuItem
                .getAction()).getToolbars();

        // If visibility is already changed for this class, respect it.
        boolean visible = true;
        if (toolBarsForClass.size() > 0) {
            visible = toolBarsForClass.get(0).isVisible();
            newToolbar.setVisible(visible);
        }

        // Add toolbar.
        toolBarsForClass.add(newToolbar);

        // Register popup menu with toolbar.
        newToolbar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (e.isPopupTrigger()) {
                    getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.isPopupTrigger()) {
                    getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    /**
     * Registers container with all containing toolbars. If all toolbars are
     * hidden, container will be hidden, too. This means that when toolbars are
     * placed on JPanel, when all toolbars are hidden, this panel needs to be
     * hidden, too.
     * 
     * @param container container
     * @param toolbars toolbars in the container
     */
    public void registerContainer(final JComponent container,
            final JToolBar[] toolbars) {
        for (JToolBar toolbar : toolbars) {
            registerNew(toolbar, toolbar, -1);
        }

        for (JToolBar toolbar : toolbars) {
            toolbar.addComponentListener(new ComponentAdapter() {
                public void componentHidden(ComponentEvent e) {
                    boolean allHidden = true;
                    for (JToolBar bar : toolbars) {
                        if (bar.isVisible()) {
                            allHidden = false;
                            break;
                        }
                    }

                    if (allHidden) {
                        container.setVisible(false);
                    }
                }

                public void componentShown(ComponentEvent e) {
                    JToolBar oneVisible = null;
                    for (JToolBar bar : toolbars) {
                        if (bar.isVisible()) {
                            oneVisible = bar;
                            break;
                        }
                    }

                    if (oneVisible != null) {
                        container.setVisible(true);
                    }
                }
            });
        }
    }

    /**
     * Registers new toolbar for specific group.
     * 
     * @param key group that toolbar belongs to.
     * @param newToolbar new toolbar to register.
     * @param prefferedMenuPosition preffered menu postition, -1 for the last
     *            postition
     */
    public void registerToolbar(Object key, JToolBar newToolbar,
            int prefferedMenuPosition) {
        registerNew(key, newToolbar, prefferedMenuPosition);
    }

    /**
     * Gets popup menu
     * 
     * @return Popup menu
     */
    private JPopupMenu getPopupMenu() {
        if (popup == null) {
            popup = new JPopupMenu();
        }

        return popup;
    }

    /**
     * Gets menu for all toolbars.
     * 
     * @return menu to be shown in view menu
     */
    public JMenu getMenu() {
        if (menu == null) {
            menu = new JMenu();
        }

        return menu;
    }

    /**
     * Gets toolbar visibility status from configuration. If it doesn't exist in
     * configuration it does NOT create new entries in configuration for that
     * toolbar.
     * 
     * @param toolbarName Name of the toolbar to get visibility status
     * @return visibility status
     */
    public boolean getConfiguredToolbarAppearance(String toolbarName) {
        ConfigurationKey key = Configuration.makeKey("toolbars", toolbarName);
        String visibilityAsString = Configuration.getString(key);

        return (visibilityAsString.equals("false")) ? false : true;
    }
}
