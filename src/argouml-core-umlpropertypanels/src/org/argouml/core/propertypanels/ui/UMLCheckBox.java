// $Id: UMLCheckBox.java 13325 2007-08-14 02:47:31Z tfmorris $
// Copyright (c) 2002-2007 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JCheckBox;

import org.argouml.model.Model;
import org.argouml.ui.LookAndFeelMgr;

/**
 * The checkbox to be used to show boolean UML attributes in the GUI's. Mostly
 * used on proppanels. Other GUI elements (like UMLLinkedList) divide the
 * responsibility of showing an attribute and maintaining the state of the
 * attribute between a GUI element and a model. This is not the case for the
 * UMLCheckBox. Reason for this is that the model is just too simple to need
 * extra classes for the model.
 * 
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
abstract class UMLCheckBox extends JCheckBox
    implements PropertyChangeListener {

    private Object checkBoxTarget;
    private String propertySetName;
    
    /**
     * The action that will be called when the checkbox changes
     */
    private Action action;

    /**
     * Constructor for UMLCheckBox.
     * @param text the text of the check box
     * @param a the action we're going to listen to
     * @param name the property set name
     */
    public UMLCheckBox(String text, Action a, String name, Object target) {
        super(text);
        action = a;
        setFont(LookAndFeelMgr.getInstance().getStandardFont());
        propertySetName = name;
        addActionListener(a);

        setActionCommand((String) a.getValue(Action.ACTION_COMMAND_KEY));
        setTarget(target);
    }

    /*
     * The property value has changed so rebuild our view.
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        buildModel();
    }

    /**
     * Return the target. 
     *
     * @return the target
     */
    public Object getTarget() {
        return checkBoxTarget;
    }

    /**
     * Sets the target. This method will not be used until the target does
     * not come via the container.
     * @param target The target to set
     */
    protected void setTarget(Object target) {
        checkBoxTarget = target;
        Model.getPump().addModelEventListener(
                this, checkBoxTarget, propertySetName);
        buildModel();
    }

    /**
     * Builds the model. That is: it sets the checkbox to true or
     * false. The name of this method is chosen to be compliant with
     * for example UMLModelElementListModel.
     */
    public abstract void buildModel();
    
    /**
     * Remove all listeners when the component is removed from its parent
     */
    public void removeNotify() {
        removeActionListener(action);
        Model.getPump().removeModelEventListener(
                this, checkBoxTarget, propertySetName);
    }
}
