package org.argouml.uml.ui.behavior.collaborations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.UMLAddDialog;
import org.argouml.uml.ui.UMLListMenuItem;
import org.argouml.uml.ui.UMLModelElementListModel;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * Binary relation list model for available con between classifierroles
 * 
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLClassifierRoleAvailableContentsListModel
	extends UMLModelElementListModel {

	

	/**
	 * Constructor for UMLClassifierRoleAvailableContentsListModel.
	 * @param container
	 * @param property
	 * @param showNone
	 */
	public UMLClassifierRoleAvailableContentsListModel(
		UMLUserInterfaceContainer container,
		String property,
		boolean showNone) {
		super(container, property, showNone);
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
        UMLListMenuItem add =new UMLListMenuItem(container.localize("Add"),this,"add",index);
        if(_upper >= 0 && getModelElementSize() >= _upper) {
            add.setEnabled(false);
        }
        popup.add(add);
        popup.add(delete);
        return true;
	}

	/**
	 * @see org.argouml.uml.ui.UMLModelElementListModel#getModelElementAt(int)
	 */
	protected MModelElement getModelElementAt(int index) {
		return elementAtUtil(((MClassifierRole)getTarget()).getAvailableContentses(), index, MClassifier.class);
	}

	/**
	 * @see org.argouml.uml.ui.UMLModelElementListModel#recalcModelElementSize()
	 */
	protected int recalcModelElementSize() {
		Object target = getTarget();
		if (target instanceof MClassifierRole) {
			MClassifierRole role = (MClassifierRole)target;
			return role.getAvailableContentses().size();
		}
		return 0;
	}
	
	protected List getChoices() {
		if (getTarget() == null || !(getTarget() instanceof MClassifierRole)) 
			throw new IllegalStateException("In getElements: target not legal");
		MClassifierRole role = (MClassifierRole)getTarget();
		if (role.getBases().isEmpty()) return new ArrayList();
		List list = new ArrayList();
		Iterator it = role.getBases().iterator();
		while (it.hasNext()) {
			MClassifier base = (MClassifier)it.next();
			list.addAll(CoreHelper.getHelper().getAllContents(base));
		}
		return list;
	}
	
	/**
	 * @see org.argouml.uml.ui.UMLConnectionListModel#add(int)
	 */
	public void add(int index) {
		Object target = getTarget();
    	if (target instanceof MModelElement) {
    		MClassifierRole role = (MClassifierRole)target;	
	    	Vector choices = new Vector();
	    	Vector selected = new Vector();
	    	choices.addAll(getChoices());
	    	choices.removeAll(role.getAvailableContentses());
	    	selected.addAll(role.getAvailableContentses());
	    	UMLAddDialog dialog = new UMLAddDialog(choices, selected, Argo.localize("UMLMenu", "dialog.title.add-available-contents"), true, true);
	    	int returnValue = dialog.showDialog(ProjectBrowser.TheInstance);
	    	if (returnValue == JOptionPane.OK_OPTION) {
	    		Iterator it = dialog.getSelected().iterator();
	    		while (it.hasNext()) {
	    			MModelElement othermelement = (MModelElement)it.next();
	    			if (!selected.contains(othermelement)) {
	    				role.addAvailableContents(othermelement);
	    			}
	    		}
	    		it = selected.iterator();
	    		while (it.hasNext()) {
	    			MModelElement othermelement = (MModelElement)it.next();
	    			if (!dialog.getSelected().contains(othermelement)) {
	    				role.removeAvailableContents(othermelement);
	    			}
	    		}
	    	}
    	}
	}
	
	
		
	

	/**
	 * @see org.argouml.uml.ui.UMLModelElementListModel#delete(int)
	 */
	public void delete(int index) {
		MModelElement element = elementAtUtil(((MClassifierRole)getTarget()).getAvailableContentses(), index, MClassifier.class);
		((MClassifierRole)getTarget()).removeAvailableContents(element);
	}

}
