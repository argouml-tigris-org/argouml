// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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
import javax.swing.JPopupMenu;

import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsFactory;
import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * @author Jaap
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by ?,
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLActivatorListModel extends UMLModelElementListModel {

    /**
     * Constructor for UMLActivatorListModel.
     * @param container
     * @param property
     * @param showNone
     */
    public UMLActivatorListModel(
				 UMLUserInterfaceContainer container,
				 String property,
				 boolean showNone) {
	super(container, property, showNone);
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel#recalcModelElementSize()
     */
    protected int recalcModelElementSize() {
	if (((MMessage) getTarget()).getActivator() != null) return 1;
	return 0;
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel#getModelElementAt(int)
     */
    protected MModelElement getModelElementAt(int index) {
	return ((MMessage) getTarget()).getActivator();
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel#buildPopup(JPopupMenu, int)
     */
    public boolean buildPopup(JPopupMenu popup, int index) {
	UMLUserInterfaceContainer container = getContainer();
		
        UMLListMenuItem add = new UMLListMenuItem(container.localize("Add"), this, "add", index);
        UMLListMenuItem open = new UMLListMenuItem(container.localize("Open"), this, "open", index);
        UMLListMenuItem delete = new UMLListMenuItem(container.localize("Delete"), this, "delete", index);
        if (getModelElementSize() > 0) {
	    add.setEnabled(false);
        } else {
	    open.setEnabled(false);
	    delete.setEnabled(false);
        }
        popup.add(open);
        popup.add(delete);
        popup.add(add);
        	
	return true;
    }
	
    public void add(int index) {
	Object target = getTarget();
	if (target instanceof MMessage) {
	    MMessage message = (MMessage) target;
	    MMessage activator = CollaborationsFactory.getFactory().buildActivator(message, message.getInteraction());
	    fireIntervalAdded(this, index, index);
	    navigateTo(activator);
	}
    }
	
	

}
