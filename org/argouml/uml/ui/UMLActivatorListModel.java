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
		if (((MMessage)getTarget()).getActivator() != null) return 1;
		return 0;
	}

	/**
	 * @see org.argouml.uml.ui.UMLModelElementListModel#getModelElementAt(int)
	 */
	protected MModelElement getModelElementAt(int index) {
		return ((MMessage)getTarget()).getActivator();
	}

	/**
	 * @see org.argouml.uml.ui.UMLModelElementListModel#buildPopup(JPopupMenu, int)
	 */
	public boolean buildPopup(JPopupMenu popup, int index) {
		UMLUserInterfaceContainer container = getContainer();
		
        UMLListMenuItem add = new UMLListMenuItem(container.localize("Add"),this,"add",index);
        UMLListMenuItem open = new UMLListMenuItem(container.localize("Open"),this,"open",index);
        UMLListMenuItem delete = new UMLListMenuItem(container.localize("Delete"),this,"delete",index);
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
			MMessage message = (MMessage)target;
			MMessage activator = CollaborationsFactory.getFactory().buildActivator(message, message.getInteraction());
			fireIntervalAdded(this,index,index);
			navigateTo(activator);
		}
	}
	
	

}
