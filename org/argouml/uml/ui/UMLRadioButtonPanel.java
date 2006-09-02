// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import org.argouml.model.Model;
import org.argouml.swingext.JXButtonGroupPanel;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.tigris.gef.presentation.Fig;

/**
 * A panel that shows a group of radiobuttons. An action can be added
 * to the panel which will be executed when one of the radiobuttons is
 * pressed. Via the name of the button (settext), the action can find
 * out which button is pressed.
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Jan 4, 2003
 */
public abstract class UMLRadioButtonPanel
    extends JXButtonGroupPanel
    implements TargetListener, PropertyChangeListener {

    private static Font smallFont = LookAndFeelMgr.getInstance().getSmallFont();

    /**
     * The target object of which some attribute is shown via this panel.
     */
    private Object panelTarget;

    /**
     * The name of the MEvent that is fired when the target object has changed
     * the attribute that is shown here.
     */
    private String propertySetName;

    /**
     * Constructs a new UMLRadioButtonPanel.
     * @param title The title of the titledborder around the buttons. If the
     * title is null, there is no border shown.
     * @param labeltextsActioncommands A map of keys containing the texts for
     * the buttons and values containing the actioncommand that permits the
     * setAction to logically recognize the button.
     * @param thePropertySetName the name of the MEvent that is fired when the
     * property that it shows changes value.
     * @param setAction the action that should be registred with the buttons and
     * that's executed when one of the buttons is pressed.
     * @param horizontal when true the buttons should be layed out horizontaly.
     */
    public UMLRadioButtonPanel(
			       String title,
			       Map labeltextsActioncommands,
			       String thePropertySetName,
			       Action setAction,
			       boolean horizontal) {
        super();
        setLayout(horizontal ? new GridLayout() : new GridLayout(0, 1));
        setDoubleBuffered(true);
        if (title != null) {
            TitledBorder border = new TitledBorder(title);
            border.setTitleFont(smallFont);
            setBorder(border);
        }
        setButtons(labeltextsActioncommands, setAction);
        setPropertySetName(thePropertySetName);
    }

    /**
     * Initially constructs the buttons.
     *
     * @param labeltextsActioncommands A map of keys containing the
     * texts for the buttons and values containing the actioncommand
     * that permits the setAction to logically recognize the button.
     * @param setAction the action that should be registred with the
     * buttons and that's executed when one of the buttons is pressed
     * 
     * TODO: This forces the buttons to be ordered in key map order,
     * an order which is arbitrary for a HashMap.  This should be
     * changed to allow the UI designer to specify the order. - tfm
     */
    private void setButtons(Map labeltextsActioncommands, Action setAction) {
        super.removeAll();
        Iterator it = labeltextsActioncommands.keySet().iterator();
        while (it.hasNext()) {
            String keyAndLabel = (String) it.next();
            JRadioButton button = new JRadioButton(keyAndLabel);
            button.addActionListener(setAction);
	    String actionCommand =
		(String) labeltextsActioncommands.get(keyAndLabel);
            button.setActionCommand(actionCommand);
            button.setFont(LookAndFeelMgr.getInstance().getSmallFont());
            super.add(button);
        }
    }


    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals(propertySetName)) {
            buildModel();
        }
    }

    /**
     * Returns the target.
     * @return Object
     */
    public Object getTarget() {
        return panelTarget;
    }

    /**
     * Sets the target and removes/adds this as a listener to the target.
     * @param target The target to set
     */
    public void setTarget(Object target) {
        target = target instanceof Fig ? ((Fig) target).getOwner() : target;
        if (Model.getFacade().isAModelElement(panelTarget)) {
            Model.getPump().removeModelEventListener(this, panelTarget,
                    propertySetName);
        }
        panelTarget = target;
        if (Model.getFacade().isAModelElement(panelTarget)) {
            Model.getPump().addModelEventListener(this, panelTarget,
                    propertySetName);
        }
        if (panelTarget != null) {
            buildModel();
        }
    }

    /**
     * Returns the propertySetName.
     * @return String
     */
    public String getPropertySetName() {
        return propertySetName;
    }

    /**
     * Sets the propertySetName.
     * @param name The propertySetName to set
     */
    public void setPropertySetName(String name) {
        propertySetName = name;
    }

    /**
     * Builds the model. That is: it selects the radiobutton showing the value
     * of the attribute shown. The name of this method is choosen to be
     * compliant with for example UMLModelElementListModel2
     */
    public abstract void buildModel();

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

}
