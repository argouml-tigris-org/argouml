/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.kernel;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

/**
 * This class is both a List and an Action although it purpose is not to serve
 * as a true executable Action.
 * Its purpose is to allow menu items, toolbars and toolbuttons etc to be built
 * from a model mode up of Actions where each Action is either an executable
 * action to be shown as a menu item or toolbutton. However should an Action
 * be of this specific class it provides the name and icon only for building
 * a submenu node or dropdown toolbutton and contains a list of further
 * Actions to be contained in that subnode.
 *
 * @author Bob Tarling
 */
public class ActionList extends ArrayList<Action> implements Action {

    private Action dummyAction;
    
    public ActionList(String name) {
        dummyAction = new DummyAction(name);
    }
    
    public ActionList(String name, Icon icon) {
        dummyAction = new DummyAction(name, icon);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener arg0) {
        dummyAction.addPropertyChangeListener(arg0);
    }

    public Object getValue(String arg0) {
        return dummyAction.getValue(arg0);
    }

    public boolean isEnabled() {
        return dummyAction.isEnabled();
    }

    public void putValue(String key, Object value) {
        dummyAction.putValue(key, value);
    }

    public void removePropertyChangeListener(PropertyChangeListener arg0) {
        dummyAction.removePropertyChangeListener(arg0);
    }

    public void setEnabled(boolean b) {
        dummyAction.setEnabled(b);
    }

    public void actionPerformed(ActionEvent e) {
        dummyAction.actionPerformed(e);
    }

    private static class DummyAction extends AbstractAction {
        DummyAction(String name) {
            super(name);
        }
        DummyAction(String name, Icon icon) {
            super(name, icon);
        }
        public void actionPerformed(ActionEvent e) {
            // Do nothing
        }
    }
}
