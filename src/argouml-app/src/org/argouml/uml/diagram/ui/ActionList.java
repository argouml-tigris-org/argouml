// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.util.List;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.argouml.model.UmlModelMutator;

/**
 * A specialist collection used for filtering the return from getPopUpActions
 * in FigNodeModelElement and FigEdgeModelElement.
 * This class should remain package scope as I imagine its lifetime to be
 * short (we need a better way of registering Actions with Figs)
 *
 * @author Bob Tarling
 */
class ActionList extends Vector {
    
    private boolean readonly;
    
    ActionList(List list, boolean readonly) {
        super(list);
        this.readonly = readonly;
    }
    
    public boolean add(Object o) {
        if (readonly) {
            if (o instanceof UmlModelMutator) {
                return false;
            } else if (o instanceof JMenu) {
                o = trimMenu(o);
            }
        }
        if (o != null) {
            return super.add(o);
        } else {
            return false;
        }
    }
    
    public void addElement(Object o) {
        if (readonly) {
            if (o instanceof UmlModelMutator) {
                return;
            } else if (o instanceof JMenu) {
                o = trimMenu(o);
            }
        }
        if (o != null) {
            super.addElement(o);
        }
    }
    
    public void add(int index, Object o) {
        if (readonly) {
            if (o instanceof UmlModelMutator) {
                return;
            } else if (o instanceof JMenu) {
                o = trimMenu(o);
            }
        }
        if (o != null) {
            super.add(index, o);
        }
    }
    
    public void insertElementAt(Object o, int index) {
        if (readonly) {
            if (o instanceof UmlModelMutator) {
                return;
            } else if (o instanceof JMenu) {
                o = trimMenu(o);
            }
        }
        if (o != null) {
            super.insertElementAt(o, index);
        }
    }
    
    /**
     * Trim out any menu items that have a UML model mutating action
     * @param o
     * @return The trimmed menu or null if all contents trimmed out.
     */
    private JMenu trimMenu(Object o) {
        JMenu menu = (JMenu) o;
        for (int i = menu.getItemCount() - 1; i >= 0; --i) {
            JMenuItem menuItem = menu.getItem(i);
            Action action = menuItem.getAction();
            if (action == null
                    && menuItem.getActionListeners().length > 0
                    && menuItem.getActionListeners()[0] instanceof Action) {
                action = (Action) menuItem.getActionListeners()[0];
            }
            if (isUmlMutator(action)) {
                menu.remove(i);
            }
        }
        if (menu.getItemCount() == 0) {
            return null;
        }
        return menu;
    }
    
    /**
     * @param a An object (typically an Action) to test if its a UML model
     * mutator
     * @return true if the given action mutates the UML model.
     */
    private boolean isUmlMutator(Object a) {
        return a instanceof UmlModelMutator;
        // TODO: Why doesn't this work for annotation?
        //return a.getClass().isAnnotationPresent(UmlModelMutator.class);
    }
}
