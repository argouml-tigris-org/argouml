// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

// $Id$
package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.argouml.application.api.Argo;
import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * Abstract action that is the parent to all add actions that add the
 * modelelements via the UMLAddDialog.
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class AbstractActionAddModelElement extends UMLChangeAction {

    private Object/*MModelElement*/ _target;
    private boolean _multiSelect = true;
    private boolean _exclusive = true;
    
    protected AbstractActionAddModelElement() {
        super(Argo.localize("CoreMenu", "Add"), true, NO_ICON);
    }
        

    /**
     * Returns the target.
     * @return MModelElement
     */
    public MModelElement getTarget() {
        return (MModelElement)_target;
    }

    /**
     * Sets the target.
     * @param target The target to set
     */
    public void setTarget(MModelElement target) {
        _target = target;
    }

    /**
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        UMLAddDialog dialog =
	    new UMLAddDialog(getChoices(), getSelected(), getDialogTitle(),
			     isMultiSelect(),
			     isExclusive());
        int result = dialog.showDialog(ProjectBrowser.getInstance());
        if (result == JOptionPane.OK_OPTION) {
            doIt(dialog.getSelected());
        }
    }
    
    /**
     * Returns the choices the user has in the UMLAddDialog. The choices are 
     * depicted on the left side of the UMLAddDialog (sorry Arabic users) and 
     * can be moved via the buttons on the dialog to the right side. On the
     * right side are the selected modelelements.
     * @return Vector
     */
    protected abstract Vector getChoices();
    
    /**
     * The modelelements allready selected BEFORE the dialog is shown.
     * @return Vector
     */
    protected abstract Vector getSelected();
    
    /**
     * Returns the title of the dialog.
     * @return String
     */
    protected abstract String getDialogTitle();
    
    /**
     * The action that has to be done by Argouml after the user clicks ok in the
     * UMLAddDialog. 
     * @param selected The choices the user has selected in the UMLAddDialog
     */
    protected abstract void doIt(Vector selected);

    /**
     * Returns the exclusive.
     * @return boolean
     */
    public boolean isExclusive() {
        return _exclusive;
    }

    /**
     * Returns the multiSelect.
     * @return boolean
     */
    public boolean isMultiSelect() {
        return _multiSelect;
    }

    /**
     * Sets the exclusive.
     * @param exclusive The exclusive to set
     */
    public void setExclusive(boolean exclusive) {
        _exclusive = exclusive;
    }

    /**
     * Sets the multiSelect.
     * @param multiSelect The multiSelect to set
     */
    public void setMultiSelect(boolean multiSelect) {
        _multiSelect = multiSelect;
    }

    /**
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
         return !getChoices().isEmpty();
    }

}