/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006-2008 The Regents of the University of California. All
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

package org.argouml.ui.cmd;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.KeyStroke;

import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerGrid;


/**
 * This action changes the grid shown on the diagram.
 * 
 * @author Michiel
 */
public class ActionAdjustGrid extends AbstractAction {

    private final Map<String, Comparable> myMap;
    private static final String DEFAULT_ID = "03";
    private static ButtonGroup myGroup;

    private static final int DEFAULT_MASK = 
        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    /**
     * @param map this map contains the values for 
     *          the spacing, paintLines and paintDots. 
     * @param name the name for this action
     */
    private ActionAdjustGrid(final Map<String, Comparable> map,
            final String name) {
        super();
        myMap = map;
        putValue(Action.NAME, name);
    }

    public void actionPerformed(final ActionEvent e) {
        final Editor editor = Globals.curEditor();
        if (editor != null) {
            final Layer grid = editor.getLayerManager().findLayerNamed("Grid");
            if (grid instanceof LayerGrid) {
                if (myMap != null) {
                    // Kludge required by GEF's use of HashMap in the API
                    // TODO: This can be removed if they ever fix GEF to use
                    // Maps
                    if (myMap instanceof HashMap) {
                        grid.adjust((HashMap<String, Comparable>) myMap);
                    } else {
                        grid.adjust(new HashMap<String, Comparable>(myMap));
                    }
                    Configuration.setString(Argo.KEY_GRID,
                            (String) getValue("ID"));
                }
            }
        }
    }
    
    static void setGroup(final ButtonGroup group) {
        myGroup = group;
    }
    
    /**
     * This executes one of the actions, 
     * based on the stored ArgoUML configuration. 
     * This function is intended for the initial setting 
     * of the grid when ArgoUML is started. <p>
     * 
     * Additionally, the ButtonGroup is searched for the right Action, 
     * and when found, the button's model initialised.
     */
    static void init() {
        String id = Configuration.getString(Argo.KEY_GRID, DEFAULT_ID);
        List<Action> actions = createAdjustGridActions(false);
        for (Action a : actions) {
            if (a.getValue("ID").equals(id)) {
                a.actionPerformed(null);

                if (myGroup != null) {
                    for (Enumeration e = myGroup.getElements(); 
                        e.hasMoreElements();) {
                        AbstractButton ab = (AbstractButton) e.nextElement();
                        Action action = ab.getAction();
                        if (action instanceof ActionAdjustGrid) {
                            String currentID = (String) action.getValue("ID"); 
                            if (id.equals(currentID)) {
                                myGroup.setSelected(ab.getModel(), true);
                                return;
                            }
                        }
                    }
                }
                return;
            }
        }
    }
    
    /**
     * This function is the one and only location 
     * that defines the number of grid settings, and their content.
     * 
     * @constraint one of the actions should have the ID 'defaultID', 
     *          since this is used in init().
     * @param longStrings
     * @return List of Actions which adjust the grid
     */
    static List<Action> createAdjustGridActions(final boolean longStrings) {
        List<Action> result = new ArrayList<Action>();

        result.add(buildGridAction(longStrings ? "action.adjust-grid.lines-16"
                : "menu.item.lines-16", 16, true, true, "01", KeyEvent.VK_1));
        result.add(buildGridAction(longStrings ? "action.adjust-grid.lines-8"
                : "menu.item.lines-8", 8, true, true, "02", KeyEvent.VK_2));
        result.add(buildGridAction(longStrings ? "action.adjust-grid.dots-16"
                : "menu.item.dots-16", 16, false, true, "03", KeyEvent.VK_3));
        result.add(buildGridAction(longStrings ? "action.adjust-grid.dots-32"
                : "menu.item.dots-32", 32, false, true, "04", KeyEvent.VK_4));
        result.add(buildGridAction(
                longStrings ? "action.adjust-grid.none"
                        : "menu.item.none", 16, false, false, "05",
                KeyEvent.VK_5));
     
        return result;
    }

    /**
     * @param property the name (not yet localised)
     * @param spacing the spacing distance
     * @param paintLines show lines
     * @param paintDots show dots
     * @param id identifier
     * @param key keyCode
     * @return the Action
     */
    public static Action buildGridAction(final String property,
            final int spacing, final boolean paintLines,
            final boolean paintDots, final String id, final int key) {
        String name = Translator.localize(property);
        HashMap<String, Comparable> map1 = new HashMap<String, Comparable>(4);
        map1.put("spacing", Integer.valueOf(spacing));
        map1.put("paintLines", Boolean.valueOf(paintLines));
        map1.put("paintDots", Boolean.valueOf(paintDots));
        Action action = new ActionAdjustGrid(map1, name);
        action.putValue("ID", id);
        action.putValue("shortcut", KeyStroke.getKeyStroke(
                key, DEFAULT_MASK));
        return action;
    }

}
