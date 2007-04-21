// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.util;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;
import org.argouml.application.configuration.Configuration;
import org.argouml.application.configuration.ConfigurationKey;
import org.tigris.toolbar.ToolBarManager;
import org.tigris.toolbar.toolbutton.PopupToolBoxButton;

/**
 * This utility class contains additional functionality related to the 
 * org.tigris.toolbar project for the ArgoUML application.
 *
 * @author Michiel
 */
public class ToolBarUtility {
    
    private static final Logger LOG = Logger.getLogger(ToolBarUtility.class);
    
    /**
     * Manages the selection of the default tool 
     * in a popup tool in the toolbar. <p>
     * 
     * I.e. in a toolbar, you can have tools that can be opened,
     * into a grid of tools. The last used tool is remembered, 
     * and put at the top when the popup is closed, i.e.
     * is the only tool that remains visible. This remembering is
     * persistent, hence stored in the configuration file,
     * under a certain key (i.e. name).
     * 
     * @param actions the array of actions that make up the popup
     * @param key appendix for the key for the configuration file
     */
    public static void manageDefault(Object[] actions, String key) {
        Action defaultAction = null;
        ConfigurationKey k =
            Configuration.makeKey("default", "popupactions", key);
        String defaultName = Configuration.getString(k);
        PopupActionsListener listener = new PopupActionsListener(k);
        for (int i = 0; i < actions.length; ++i) {
            if (actions[i] instanceof Action) {
                Action a = (Action) actions[i];
                if (a.getValue(Action.NAME).equals(defaultName)) {
                    defaultAction = a;
                }
                a.addPropertyChangeListener(listener);
            } else if (actions[i] instanceof Object[]) {
                Object[] actionRow = (Object[]) actions[i];
                for (int j = 0; j < actionRow.length; ++j) {
                    Action a = (Action) actionRow[j];
                    if (a.getValue(Action.NAME).equals(defaultName)) {
                        defaultAction = a;
                    }
                    a.addPropertyChangeListener(listener);
                }
            }
        }

        if (defaultAction != null) {
            defaultAction.putValue("isDefault", Boolean.valueOf(true));
        }
    }

    static class PopupActionsListener implements PropertyChangeListener {
        private boolean blockEvents;
        private ConfigurationKey key;

        /**
         * Constructor.
         *
         * @param k
         */
        public PopupActionsListener(ConfigurationKey k) {
            key = k;
        }

        /*
         * @see java.beans.PropertyChangeListener#propertyChange(
         *         java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getSource() instanceof Action) {
                Action a = (Action) evt.getSource();
                if (!blockEvents && evt.getPropertyName().equals("popped")) {
                    blockEvents = true;
                    /* Switch the value back off, so that we will
                     * get notified again next time.
                     */
                    a.putValue("popped", Boolean.valueOf(false));
                    blockEvents = false;
                    Configuration.setString(key,
                            (String) a.getValue(Action.NAME));
                }
            }
        }
    }

    /** 
     * TODO: Use the following function to have a dropdown set of tools: 
     * ToolBarFactory.addItemsToToolBar(buttonPanel, actions, true); 
     * Instead, this temporary solution: 
     *
     * @param buttonPanel the toolbar
     * @param actions an array of actions representing the tool layout
     */
    public static void addItemsToToolBar(JToolBar buttonPanel, 
            Object[] actions) {
        JButton button = buildPopupToolBoxButton(actions, false);
        if (!ToolBarManager.alwaysUseStandardRollover()) {
            button.setBorderPainted(false);
        }
        buttonPanel.add(button);
    }
    
    /** 
     * TODO: Use the following function to have a dropdown set of tools: 
     * ToolBarFactory.addItemsToToolBar(buttonPanel, actions, true); 
     * Instead, this temporary solution: 
     *
     * @param buttonPanel the toolbar
     * @param actions an array of actions representing the tool layout
     */
    public static void addItemsToToolBar(JToolBar buttonPanel, 
            Collection actions) {
	addItemsToToolBar(buttonPanel, actions.toArray());
    }
    
    /**
     * TODO: Move this into the toolbar project.
     */
    private static PopupToolBoxButton buildPopupToolBoxButton(Object[] actions, 
            boolean rollover) {
        PopupToolBoxButton toolBox = null;
        for (int i = 0; i < actions.length; ++i) {
            if (actions[i] instanceof Action) {
                LOG.info("Adding a " + actions[i] + " to the toolbar");
                Action a = (Action) actions[i];
                if (toolBox == null) {
                    toolBox = new PopupToolBoxButton(a, 0, 1, rollover);
                }
                toolBox.add(a);
            } else if (actions[i] instanceof Component) {
                toolBox.add((Component) actions[i]);
            } else if (actions[i] instanceof Object[]) {
                Object[] actionRow = (Object[]) actions[i];
                for (int j = 0; j < actionRow.length; ++j) {
                    Action a = (Action) actionRow[j];
                    if (toolBox == null) {
                        int cols = actionRow.length;
                        toolBox = new PopupToolBoxButton(a, 0, cols, rollover);
                    }
                    toolBox.add(a);
                }
            } else {
        	LOG.error("Can't add a " + actions[i] + " to the toolbar");
            }
        }
        return toolBox;
    }

}
