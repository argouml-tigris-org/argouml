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

import org.argouml.kernel.UmlModelMutator;

/**
 * A specialist collection used for filtering the return from getPopUpActions
 * in FigNodeModelElement and FigEdgeModelElement.
 * This class should remain package scope as I imagine its lifetime to be
 * short (we need a better way of registering Actions with Figs)
 *
 * @author Bob Tarling
 */
class ActionList<E> extends Vector<E> {
    
    private final boolean readonly;
    
    /**
     * Construct an ActionList which filters modifying actions when
     * the readonly flag is set using initialList as the initial
     * content.
     * 
     * @param initialList initial contents to use for list.
     * @param readOnly true if mutating actions should be filtered
     */
    ActionList(List<? extends E> initialList, boolean readOnly) {
        super(initialList);
        this.readonly = readOnly;
    }
    
    @Override
    public boolean add(E o) {
        if (readonly) {
            if (isUmlMutator(o) ) {
                return false;
            } else if (o instanceof JMenu) {
                o = (E) trimMenu((JMenu) o);
            }
        }
        if (o != null) {
            return super.add(o);
        } else {
            return false;
        }
    }
    
    @Override
    public void addElement(E o) {
        if (readonly) {
            if (isUmlMutator(o)) {
                return;
            } else if (o instanceof JMenu) {
                o = (E) trimMenu((JMenu) o);
            }
        }
        if (o != null) {
            super.addElement(o);
        }
    }
    
    @Override
    public void add(int index, E o) {
        if (readonly) {
            if (isUmlMutator(o)) {
                return;
            } else if (o instanceof JMenu) {
                o = (E) trimMenu((JMenu) o);
            }
        }
        if (o != null) {
            super.add(index, o);
        }
    }
    
    @Override
    public void insertElementAt(E o, int index) {
        if (readonly) {
            if (isUmlMutator(o)) {
                return;
            } else if (o instanceof JMenu) {
                o = (E) trimMenu((JMenu) o);
            }
        }
        if (o != null) {
            super.insertElementAt(o, index);
        }
    }
    
    /**
     * Trim out any menu items that have a UML model mutating action
     * @param menu the menu to be filtered
     * @return The trimmed menu or null if all contents trimmed out.
     */
    private JMenu trimMenu(JMenu menu) {
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
        return a instanceof UmlModelMutator 
            || a.getClass().isAnnotationPresent(UmlModelMutator.class);
    }
}
