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

import org.apache.commons.logging.Log;
import org.argouml.ui.*;

import org.argouml.uml.*;

import java.util.*;

import java.awt.*;

import java.awt.event.*;

import javax.swing.*;

import java.lang.reflect.*;

import ru.novosoft.uml.*;



/**

 *   This abstract class provides the basic layout and event dispatching

 *   support for all Property Panels.  The property panel is layed out

 *   as a number (specified in the constructor) of equally sized panels

 *   that split the available space.  Each panel has a column of

 *   "captions" and matching column of "fields" which are laid out

 *   indepently from the other panels.

 */

public class PropPanelButton extends JButton implements ActionListener, UMLUserInterfaceComponent {
    protected static Log logger = 
        org.apache.commons.logging.LogFactory.getLog(PropPanelButton.class);

    private PropPanel _propPanel;

    private Method _actionMethod;

    private Method _enabledMethod;

    private static Object[] _noArgs = {};



    public PropPanelButton(PropPanel propPanel,JPanel buttonPanel,

        Icon icon,String toolTipText,

        String actionMethod,String enabledMethod) {

        super(icon);

        _propPanel = propPanel;

        setPreferredSize(new Dimension(icon.getIconWidth()+6,icon.getIconHeight()+6));

	setMaximumSize(new Dimension(icon.getIconWidth()+6,icon.getIconHeight()+6));

	setMinimumSize(new Dimension(icon.getIconWidth()+6,icon.getIconHeight()+6));

	setSize(new Dimension(icon.getIconWidth()+6,icon.getIconHeight()+6));

         setToolTipText(toolTipText);



        Class propPanelClass = propPanel.getClass();

        Class[] noClass = {};

        try {

            _actionMethod = propPanelClass.getMethod(actionMethod,noClass);

            if(enabledMethod != null) {

                _enabledMethod = propPanelClass.getMethod(enabledMethod,noClass);

            }

        }

        catch(Exception e) {
            logger.error(e.toString() + " in PropPanelButton("+  toolTipText + ")", e);
        }

        setEnabled(false);

        buttonPanel.add(this);

        addActionListener(this);

    }





    public void targetReasserted() {

        boolean enabled = false;

        Object target = _propPanel.getTarget();

        if(target != null && _actionMethod != null && _propPanel != null) {

            enabled = true;

            if(_enabledMethod != null) {

                try {

                    enabled = ((Boolean) _enabledMethod.invoke(_propPanel,_noArgs)).booleanValue();

                }

		catch(InvocationTargetException ex) {
                    logger.error(ex.getTargetException().toString() + " is InvocationTargetException in PropPanelButton", ex.getTargetException());
                    logger.error("Container: " + _propPanel.getClass().getName());
                    logger.error("ActionMethod: " + _actionMethod.toString());
                }              

		catch(Exception e) {
                    logger.error(e.toString() + " in PropPanelButton", e);
                }

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



    public void actionPerformed(final java.awt.event.ActionEvent event) {

        if(_actionMethod != null && _propPanel != null) {

          try {

              _actionMethod.invoke(_propPanel,_noArgs);

          }

	  catch(InvocationTargetException ex) {
            logger.error(ex.getTargetException().toString() + " is InvocationTargetException in PropPanelButton", ex.getTargetException());
            logger.error("Container: " + _propPanel.getClass().getName());
            logger.error("ActionMethod: " + _actionMethod.toString());
	  }              

          catch(Exception e) {
            logger.error(e.toString() + " in PropPanelButton.actionPerformed", e);
            logger.error("Container: " + _propPanel.getClass().getName());
            logger.error("ActionMethod: " + _actionMethod.toString());
          }

        }

    }

}

