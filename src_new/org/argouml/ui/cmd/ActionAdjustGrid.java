// $Id: eclipse-argo-codetemplates.xml 10612 2006-05-25 12:58:04Z linus $
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

    private HashMap<String, Comparable> myMap;
    private static final String DEFAULT_ID = "03";
    private static ButtonGroup myGroup;

    private static final int DEFAULT_MASK = 
        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    /**
     * @param map this map contains the values for 
     *          the spacing, paintLines and paintDots. 
     * @param name the name for this action
     */
    private ActionAdjustGrid(HashMap<String, Comparable> map, String name) {
        super();
        myMap = map;
        putValue(Action.NAME, name);
    }

    public void actionPerformed(ActionEvent e) {
        Editor ce = Globals.curEditor();
        Layer grid = ce.getLayerManager().findLayerNamed("Grid");
        if (grid instanceof LayerGrid) {
            if (myMap != null) {
                grid.adjust(myMap);
                Configuration.setString(Argo.KEY_GRID, (String) getValue("ID"));
            }
        }
    }
    
    static void setGroup(ButtonGroup group) {
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
    static List<Action> createAdjustGridActions(boolean longStrings) {
        List<Action> result = new ArrayList<Action>();
        Action a;
        String shortname, longname, name;

        shortname = "menu.item.lines-16";
        longname = "action.adjust-grid.lines-16";
        name = Translator.localize(longStrings ? longname : shortname);
        HashMap<String, Comparable> map1 = new HashMap<String, Comparable>(4);
        map1.put("spacing", new Integer(16));
        map1.put("paintLines", new Boolean(true));
        map1.put("paintDots", new Boolean(true));
        a = new ActionAdjustGrid(map1, name);
        a.putValue("ID", "01");
        a.putValue("shortcut", KeyStroke.getKeyStroke(
                KeyEvent.VK_1, DEFAULT_MASK));
        result.add(a);

        shortname = "menu.item.lines-8";
        longname = "action.adjust-grid.lines-8";
        name = Translator.localize(longStrings ? longname : shortname);
        HashMap<String, Comparable> map2 = new HashMap<String, Comparable>(4);
        map2.put("spacing", new Integer(8));
        map2.put("paintLines", new Boolean(true));
        map2.put("paintDots", new Boolean(true));
        a = new ActionAdjustGrid(map2, name);
        a.putValue("ID", "02");
        a.putValue("shortcut", KeyStroke.getKeyStroke(
                KeyEvent.VK_2, DEFAULT_MASK));
        result.add(a);
        
        shortname = "menu.item.dots-16";
        longname = "action.adjust-grid.dots-16";
        name = Translator.localize(longStrings ? longname : shortname);
        HashMap<String, Comparable> map3 = new HashMap<String, Comparable>(4);
        map3.put("spacing", new Integer(16));
        map3.put("paintLines", new Boolean(false));
        map3.put("paintDots", new Boolean(true));
        a = new ActionAdjustGrid(map3, name);
        a.putValue("ID", "03"); /* This ID is used as DEFAULT_ID ! */
        a.putValue("shortcut", KeyStroke.getKeyStroke(
                KeyEvent.VK_3, DEFAULT_MASK));
        result.add(a);

        shortname = "menu.item.dots-32";
        longname = "action.adjust-grid.dots-32";
        name = Translator.localize(longStrings ? longname : shortname);
        HashMap<String, Comparable> map4 = new HashMap<String, Comparable>(4);
        map4.put("spacing", new Integer(32));
        map4.put("paintLines", new Boolean(false));
        map4.put("paintDots", new Boolean(true));
        a = new ActionAdjustGrid(map4, name);
        a.putValue("ID", "04");
        a.putValue("shortcut", KeyStroke.getKeyStroke(
                KeyEvent.VK_4, DEFAULT_MASK));
        result.add(a);
        
        shortname = "menu.item.none";
        longname = "action.adjust-grid.none";
        name = Translator.localize(longStrings ? longname : shortname);
        HashMap<String, Comparable> map5 = new HashMap<String, Comparable>(4);
        map5.put("spacing", new Integer(16));
        map5.put("paintLines", new Boolean(false));
        map5.put("paintDots", new Boolean(false));
        a = new ActionAdjustGrid(map5, name);
        a.putValue("ID", "05");
        a.putValue("shortcut", KeyStroke.getKeyStroke(
                KeyEvent.VK_5, DEFAULT_MASK));
        result.add(a);
        
        return result;
    }

}