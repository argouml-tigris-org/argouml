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

// $header$
package org.argouml.uml.ui;

import java.awt.event.ActionEvent;

import org.argouml.application.api.Argo;
import org.argouml.kernel.Project;
import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * Action to delete modelelements from the model without navigating to/from them.
 * Used in UMLMutableList for deletion of modelelements from the list.
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 * @stereotype singleton
 */
public class ActionRemoveModelElement extends UMLChangeAction {

    private MModelElement _elementToDelete = null;
    
    public final static ActionRemoveModelElement SINGLETON = new ActionRemoveModelElement();
    /**
     * Constructor for ActionRemoveModelElement.
     */
    protected ActionRemoveModelElement() {
        super(Argo.localize("CoreMenu", "Delete From Model"), true, NO_ICON);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Project p = ProjectBrowser.TheInstance.getProject();
        if (_elementToDelete != null && ActionRemoveFromModel.sureRemove(_elementToDelete))
            p.moveToTrash(_elementToDelete);
        _elementToDelete = null;
    }

    /**
     * Returns the modelelement to be deleted.
     * @return MModelElement
     */
    public MModelElement getElementToDelete() {
        return _elementToDelete;
    }

    /**
     * Sets the elementToDelete.
     * @param elementToDelete The elementToDelete to set
     */
    public void setElementToDelete(MModelElement elementToDelete) {
        _elementToDelete = elementToDelete;
    }

    /**
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
        return _elementToDelete != null;
    }

}
