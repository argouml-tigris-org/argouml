// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.argouml.i18n.Translator;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.undo.UndoableAction;

/**
 * Abstract action that is the parent to all add actions that add the
 * modelelements via the UMLAddDialog.
 * 
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class AbstractActionAddModelElement2 extends UndoableAction {

    private Object target;
    private boolean multiSelect = true;
    private boolean exclusive = true;

    /**
     * Construct an action to add a model element to some list.
     */
    protected AbstractActionAddModelElement2() {
        super(Translator.localize("menu.popup.add-modelelement"), null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("menu.popup.add-modelelement"));
    }

    public AbstractActionAddModelElement2(String name) {
        super(name);
    }

    public AbstractActionAddModelElement2(String name, Icon icon) {
        super(name, icon);
    }
    

    /*
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        UMLAddDialog dialog =
            new UMLAddDialog(getChoices(), getSelected(), getDialogTitle(),
                             isMultiSelect(),
                             isExclusive());
        int result = dialog.showDialog(ArgoFrame.getInstance());
        if (result == JOptionPane.OK_OPTION) {
            doIt(dialog.getSelectedList());
        }
    }
    
    /**
     * Returns the choices the user has in the UMLAddDialog. The choices are
     * depicted on the left side of the UMLAddDialog (sorry Arabic users) and
     * can be moved via the buttons on the dialog to the right side. On the
     * right side are the selected modelelements.
     * @return List of choices
     */
    protected abstract List getChoices();

    
    /**
     * The modelelements already selected BEFORE the dialog is shown.
     * @return List of model elements
     */
    protected abstract List getSelected();

    /**
     * The action that has to be done by ArgoUml after the user clicks ok in the
     * UMLAddDialog.
     * @param selected The choices the user has selected in the UMLAddDialog
     */
    protected void doIt(Collection selected) {
        // TODO: This is for backward compatibility.  It can be removed after
        // the deprecation period for AbstractActionAddModelElement has lapsed
        if (this instanceof AbstractActionAddModelElement) {
            ((AbstractActionAddModelElement) this).doIt((Vector) selected);
        }
    }

    /*
     * @see javax.swing.Action#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return !getChoices().isEmpty();
    }
    
    
    /**
     * Returns the UML model target.
     * @return UML ModelElement
     */
    protected Object getTarget() {
        return target;
    }

    /**
     * Sets the UML model target.
     * @param theTarget The target to set
     */
    public void setTarget(Object theTarget) {
        target = theTarget;
    }

    /**
     * Returns the title of the dialog.
     * @return String
     */
    protected abstract String getDialogTitle();

    /**
     * Returns the exclusive.
     * @return boolean
     */
    public boolean isExclusive() {
        return exclusive;
    }

    /**
     * Returns the multiSelect.
     * @return boolean
     */
    public boolean isMultiSelect() {
        return multiSelect;
    }

    /**
     * Sets the exclusive.
     * @param theExclusive The exclusive to set
     */
    public void setExclusive(boolean theExclusive) {
        exclusive = theExclusive;
    }

    /**
     * Sets the multiSelect.
     * @param theMultiSelect The multiSelect to set
     */
    public void setMultiSelect(boolean theMultiSelect) {
        multiSelect = theMultiSelect;
    }

}