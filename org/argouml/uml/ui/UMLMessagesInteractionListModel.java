// Copyright (c) 1996-01 The Regents of the University of California. All
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
import java.util.Collection;

import javax.swing.JPopupMenu;

import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * List model for messages on the interaction proppanel. 
 * 
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLMessagesInteractionListModel extends UMLModelElementListModel {

	/**
	 * Constructor for UMLMessagesModel.
	 * @param container
	 * @param property
	 * @param showNone
	 */
	public UMLMessagesInteractionListModel(
		UMLUserInterfaceContainer container,
		String property,
		boolean showNone) {
		super(container, property, showNone);
	}

	/**
	 * Gets the message at index
	 * @see org.argouml.uml.ui.UMLModelElementListModel#getModelElementAt(int)
	 */
	protected MModelElement getModelElementAt(int index) {
		 return elementAtUtil(getMessages(), index,
                             MMessage.class);
	}
	
	/**
	 * Returns the messages belonging to the interaction that's the target of
	 * this model.
	 * @return Collection
	 */
	private Collection getMessages() {
        Collection messages = null;
        Object target = getTarget();

        if (target instanceof MInteraction) {
            MInteraction interaction = (MInteraction) target;
            messages = interaction.getMessages();
        }

        return messages;

    }
    
	/**
	 * @see org.argouml.uml.ui.UMLModelElementListModel#recalcModelElementSize()
	 */
	protected int recalcModelElementSize() {
		int size = 0;
		Collection messages = getMessages();
		if (messages != null) size = messages.size();
		return size;
	}
	
	
	

	/**
	 * @see org.argouml.uml.ui.UMLModelElementListModel#buildPopup(JPopupMenu, int)
	 */
	public boolean buildPopup(JPopupMenu popup, int index) {
		UMLUserInterfaceContainer container = getContainer();
        UMLListMenuItem open = new UMLListMenuItem(container.localize("Open"),this,"open",index);
        UMLListMenuItem delete = new UMLListMenuItem(container.localize("Delete"),this,"delete",index);
        if(getModelElementSize() <= 0) {
            open.setEnabled(false);
            delete.setEnabled(false);
        }

        popup.add(open);
        popup.add(delete);
        
        // no moveup, movedown
		return true;
	}


}
