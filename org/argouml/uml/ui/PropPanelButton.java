// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

import org.apache.log4j.Logger;
import java.awt.event.*;
import javax.swing.*;
import java.lang.reflect.*;
import ru.novosoft.uml.MElementEvent;

/**
 *   This abstract class provides the basic layout and event dispatching
 *   support for all Property Panels.  The property panel is layed out
 *   as a number (specified in the constructor) of equally sized panels
 *   that split the available space.  Each panel has a column of
 *   "captions" and matching column of "fields" which are laid out
 *   indepently from the other panels.
 */

public class PropPanelButton extends JButton implements ActionListener, UMLUserInterfaceComponent {
    protected static Logger cat = 
        Logger.getLogger(PropPanelButton.class);

    private PropPanel _propPanel;
    /**
     * The action method to be executed.
     */
    private Method _actionMethod;
    
    /**
     * The action to be exectuted.  Either action or action method must be set but not both.
     */
    private Action _action;
    
    private Method _enabledMethod;
    private static Object[] _noArgs = {};

    /** Constructor for a new button in the propertypanel.
     * @param propPanel the property panel to use (usually this)
     * @param buttonPanel the button panel to use (usually buttonPanel)
     * @param icon the icon of this button (choose one from PropPanelModelElement)
     * @param toolTipText
     * @param actionMethod string name of the actionMethod, found by reflection
     * @param enabledMethod string name of the enableMethod, found by reflection
     * @param action the action to be executed. Must be null if actionMethod is filled.
     */
    public PropPanelButton(
            PropPanel propPanel,
            JComponent buttonPanel,
            Icon icon,
            String toolTipText,
            String actionMethod,
            String enabledMethod,
            Action action) {

        super(icon);

        _propPanel = propPanel;

        //setPreferredSize(new Dimension(icon.getIconWidth()+6,icon.getIconHeight()+6));
	//setMaximumSize(new Dimension(icon.getIconWidth()+6,icon.getIconHeight()+6));
	//setMinimumSize(new Dimension(icon.getIconWidth()+6,icon.getIconHeight()+6));
	//setSize(new Dimension(icon.getIconWidth()+6,icon.getIconHeight()+6));
        setToolTipText(toolTipText);

        Class propPanelClass = propPanel.getClass();
        Class[] noClass = {};
        if (actionMethod != null) {
        try {
            _actionMethod = propPanelClass.getMethod(actionMethod, noClass);
            if (enabledMethod != null) {
                _enabledMethod = propPanelClass.getMethod(enabledMethod, noClass);
            }
        } catch (Exception e) {
            cat.error(e.toString() + " in PropPanelButton(" +  toolTipText + ")", e);
        }
        } else
        if (action != null) {
            _action = action;
        } else
            throw new IllegalArgumentException("Either action method or action must be indicated. They are both null now.");

        setEnabled(false);

        buttonPanel.add(this);
        addActionListener(this);
    }
    
    /**
     * @deprecated as of 0.17.1 This constructor constructs a proppanel button using the old reflection method. 
     * @param propPanel
     * @param buttonPanel
     * @param icon
     * @param toolTipText
     * @param actionMethod
     * @param enabledMethod
     */
    public PropPanelButton( PropPanel propPanel,
            JComponent buttonPanel,
            Icon icon,
            String toolTipText,
            String actionMethod,
            String enabledMethod) { 
        this (propPanel, buttonPanel, icon, toolTipText, actionMethod, enabledMethod, null);
        
    }
    
    public PropPanelButton( PropPanel propPanel,
            JComponent buttonPanel,
            Icon icon,
            String toolTipText,                        
            Action action) { 
        this (propPanel, buttonPanel, icon, toolTipText, null, null, action);
        
    }

    public void targetReasserted() {
        boolean enabled = false;
        Object target = _propPanel.getTarget();
        if (target != null && (_actionMethod != null || _action != null) && _propPanel != null) {
            enabled = true;
            if (_enabledMethod != null) {
                try {
                    enabled = ((Boolean) _enabledMethod.invoke(_propPanel, _noArgs)).booleanValue();
                } catch (InvocationTargetException ex) {
                    cat.error(ex.getTargetException().toString() + " is InvocationTargetException in PropPanelButton", ex.getTargetException());
                    cat.error("Container: " + _propPanel.getClass().getName());
                    cat.error("ActionMethod: " + _actionMethod.toString());
                } catch (Exception e) {
                    cat.error(e.toString() + " in PropPanelButton", e);
                }
            } else 
            if (_action != null) {
                enabled = _action.isEnabled();
            } 
            
        }

        setEnabled(enabled);
    }

    public void targetChanged() {
        targetReasserted();
    }

    public void roleAdded(final MElementEvent p1) {
    }

    public void recovered(final MElementEvent p1) {
    }

    public void roleRemoved(final MElementEvent p1) {
    }

    public void listRoleItemSet(final MElementEvent p1) {

    }

    public void removed(final MElementEvent p1) {
    }

    public void propertySet(final MElementEvent event) {
    }

    /** actionPerfomed invokes the defined action via reflection for this
     *  button.
     */
    public void actionPerformed(final java.awt.event.ActionEvent event) {

        if (_actionMethod != null && _propPanel != null) {
            try {
                _actionMethod.invoke(_propPanel, _noArgs);
            } catch (InvocationTargetException ex) {
                cat.error(ex.getTargetException().toString() + " is InvocationTargetException in PropPanelButton", ex.getTargetException());
                cat.error("Container: " + _propPanel.getClass().getName());
                cat.error("ActionMethod: " + _actionMethod.toString());
	    } catch (Exception e) {
                cat.error(e.toString() + " in PropPanelButton.actionPerformed", e);
                cat.error("Container: " + _propPanel.getClass().getName());
                cat.error("ActionMethod: " + _actionMethod.toString());
            }
        } else
        if (_action != null) {
            _action.actionPerformed(event);
        } else
            throw new IllegalStateException("No action or action method indicated");
    }
}
