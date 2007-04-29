// $Id: eclipse-argo-codetemplates.xml 10612 2006-05-25 12:58:04Z linus $
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

package org.argouml.ui.cmd;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
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
import org.tigris.gef.base.Guide;
import org.tigris.gef.base.GuideGrid;

/**
 * This action changes the snap (called guide or grid-guide in GEF).
 *
 * @author Michiel
 */
public class ActionAdjustSnap extends AbstractAction {

    private int guideSize;
    private static final String DEFAULT_ID = "8";
    private static ButtonGroup myGroup;
    
    /**
     * @param size the size of the snap in pixels
     * @param name the name of the action
     */
    private ActionAdjustSnap(int size, String name) {
        super();
        guideSize = size;
        putValue(Action.NAME, name);
    }
    
    public void actionPerformed(ActionEvent e) {
        Editor ce = Globals.curEditor();
        Guide guide = ce.getGuide();
        if (guide instanceof GuideGrid) {
            ((GuideGrid) guide).gridSize(guideSize);
            Configuration.setString(Argo.KEY_SNAP, (String) getValue("ID"));
        }
    }

    static void setGroup(ButtonGroup group) {
        myGroup = group;
    }

    /**
     * This executes one of the actions, 
     * based on the stored ArgoUML configuration. 
     * This function is intended for the initial setting 
     * of the snap when ArgoUML is started.
     */
    public static void init() {
        String id = Configuration.getString(Argo.KEY_SNAP, DEFAULT_ID);
        List actions = createAdjustSnapActions();
        Iterator i = actions.iterator();
        Action a;
        while (i.hasNext()) {
            a = (Action) i.next();
            if (a.getValue("ID").equals(id)) {
                a.actionPerformed(null);

                if (myGroup != null) {
                    for (Enumeration e = myGroup.getElements(); 
                        e.hasMoreElements();) {
                        AbstractButton ab = (AbstractButton) e.nextElement();
                        Action action = ab.getAction();
                        if (action instanceof ActionAdjustSnap) {
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
     * 4, 8, 16, 32
     * 
     * @return
     */
    static List createAdjustSnapActions() {
        List result = new ArrayList();
        Action a;
        String name;
        
        name = Translator.localize("menu.item.snap-4");
        a = new ActionAdjustSnap(4, name);
        a.putValue("ID", "4");
        a.putValue("shortcut", KeyStroke.getKeyStroke(
                KeyEvent.VK_1, Event.ALT_MASK + Event.CTRL_MASK));
        result.add(a);
        
        name = Translator.localize("menu.item.snap-8");
        a = new ActionAdjustSnap(8, name);
        a.putValue("ID", "8"); /* This ID is used as DEFAULT_ID ! */
        a.putValue("shortcut", KeyStroke.getKeyStroke(
                KeyEvent.VK_2, Event.ALT_MASK + Event.CTRL_MASK));
        result.add(a);
        
        name = Translator.localize("menu.item.snap-16");
        a = new ActionAdjustSnap(16, name);
        a.putValue("ID", "16");
        a.putValue("shortcut", KeyStroke.getKeyStroke(
                KeyEvent.VK_3, Event.ALT_MASK + Event.CTRL_MASK));
        result.add(a);
        
        name = Translator.localize("menu.item.snap-32");
        a = new ActionAdjustSnap(32, name);
        a.putValue("ID", "32");
        a.putValue("shortcut", KeyStroke.getKeyStroke(
                KeyEvent.VK_4, Event.ALT_MASK + Event.CTRL_MASK));
        result.add(a);
        
        return result;
    }

}
