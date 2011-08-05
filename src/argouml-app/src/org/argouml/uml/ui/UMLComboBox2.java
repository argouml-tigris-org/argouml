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

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;

import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargettableModelView;


/**
 * ComboBox for selecting UML Elements.
 * @deprecated in 0.31.5 by Bob Tarling. Property panel controls are now
 * internal to the property panel component
 */
@Deprecated
public class UMLComboBox2
    extends JComboBox
    implements TargettableModelView, TargetListener, 
        JComboBox.KeySelectionManager {

    private static final int KEY_TIME_THRESHOLD_MILLIS = 1500;
    private String searchString = "";
    private long lastKeyEventTime;

    /**
     * Constructor for UMLComboBox2.
     * @deprecated As of ArgoUml version unknown (before 0.13.5),
     * replaced by {@link #UMLComboBox2(UMLComboBoxModel2, Action, boolean)}
     * @param model the ComboBoxModel
     */
    @Deprecated
    UMLComboBox2(UMLComboBoxModel2 model) {
        super(model);
        setFont(LookAndFeelMgr.getInstance().getStandardFont());
        addActionListener(this);
        addPopupMenuListener(model);
    }

    /**
     * Construct a UMLComboBox2.
     * 
     * @param model a UMLComboBoxModel2 which provides UML elements for the user
     *            to choose from
     * @param action action to invoke when an item is selected
     * @param showIcon true if an icon should be shown in front of the items
     */
    public UMLComboBox2(UMLComboBoxModel2 model, Action action,
			boolean showIcon) {
        super(model);
        setFont(LookAndFeelMgr.getInstance().getStandardFont());
        lastKeyEventTime = 0;
        addActionListener(action);
        // setDoubleBuffered(true);
        setKeySelectionManager(this);
        setRenderer(new UMLListCellRenderer2(showIcon));
        addPopupMenuListener(model);
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        int i = getSelectedIndex();
        if (i >= 0) {
            doIt(arg0);
        }
    }
    
    /**
     * Implementation of the JComboBox.KeySelectionManager interface. Helps with
     * navigating through the ComboBox when using the keyboard.
     * 
     * @param key The key which has been pressed
     * @param model Current ComboBoxModel
     * @return int The index of the item that is to be selected
     * @see javax.swing.JComboBox.KeySelectionManager#selectionForKey(char, javax.swing.ComboBoxModel)
     */
    public int selectionForKey(char key, ComboBoxModel model)
    {        
        long now = System.currentTimeMillis();
        int index = -1;
        int startAtIndex = 0;

        // Implements backspace functionality
        if (searchString != null && key == KeyEvent.VK_BACK_SPACE
                && searchString.length() > 0) {
            searchString = searchString.substring(0, searchString.length() - 1);
        }
        else {
            if (lastKeyEventTime + KEY_TIME_THRESHOLD_MILLIS < now) {
                searchString = Character.toString(key);
            }
            else {
                searchString = searchString + key;
                startAtIndex = getSelectedIndex();
                if (startAtIndex < 0) {
                    startAtIndex = 0;
                }
            }
        }
        
        String umlElemName = "";
        String searchStringLowerCase = searchString.toLowerCase();
        ListCellRenderer cellRenderer = getRenderer();
        
        for (int i = startAtIndex, length = model.getSize(); i < length; i++) {
            Object value = model.getElementAt(i);
            if (cellRenderer instanceof UMLListCellRenderer2) {
                umlElemName = 
                    ((UMLListCellRenderer2) cellRenderer).makeText(value);
            } else {
                umlElemName = value.toString();
            }
            if (umlElemName.toLowerCase().startsWith(searchStringLowerCase)) {
                index = i;
                break;
            }
        }
        lastKeyEventTime = now;

        return index;
    }

    /**
     * The 'body' of the actionPerformed method. Is only called if there is
     * actually a selection made.
     *
     * @param event the event
     */
    protected void doIt(@SuppressWarnings("unused") ActionEvent event) { }

    /**
     * Utility method to get the current target.
     *
     * @return Object
     */
    public Object getTarget() {
        return ((UMLComboBoxModel2) getModel()).getTarget();
    }


    /*
     * @see org.argouml.ui.targetmanager.TargettableModelView#getTargettableModel()
     */
    public TargetListener getTargettableModel() {
        return (TargetListener) getModel();
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        if (e.getNewTarget() != getTarget()) {
            removeActionListener(this);
        }
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        removeActionListener(this);
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        addActionListener(this);
    }
}
