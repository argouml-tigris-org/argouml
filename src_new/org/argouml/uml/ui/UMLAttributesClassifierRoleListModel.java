package org.argouml.uml.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MFeature;

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
public class UMLAttributesClassifierRoleListModel
	extends UMLAttributesListModel {

	/**
	 * Constructor for UMLAttributesClassifierRoleListModel.
	 * @param container
	 * @param property
	 * @param showNone
	 */
	public UMLAttributesClassifierRoleListModel(
		UMLUserInterfaceContainer container,
		String property,
		boolean showNone) {
		super(container, property, showNone);
	}

	/**
	 * @see org.argouml.uml.ui.UMLAttributesListModel#add(int)
	 */
	public void add(int index) {
		Object target = getTarget();
    	if (target instanceof MClassifierRole) {
    		MClassifierRole role = (MClassifierRole)target;	
	    	Vector choices = new Vector();
	    	Vector selected = new Vector();
	    	choices.addAll(getChoices());
	    	choices.removeAll(role.getAvailableContentses());
	    	selected.addAll(role.getAvailableContentses());
	    	UMLAddDialog dialog = new UMLAddDialog(choices, selected, Argo.localize("UMLMenu", "dialog.title.add-available-contents"), true, true);
	    	int returnValue = dialog.showDialog(ProjectBrowser.getInstance());
	    	if (returnValue == JOptionPane.OK_OPTION) {
	    		Iterator it = dialog.getSelected().iterator();
	    		while (it.hasNext()) {
	    			MFeature othermelement = (MFeature)it.next();
	    			if (!selected.contains(othermelement)) {
	    				role.addFeature(othermelement);
	    			}
	    		}
	    		it = selected.iterator();
	    		while (it.hasNext()) {
	    			MFeature othermelement = (MFeature)it.next();
	    			if (!dialog.getSelected().contains(othermelement)) {
	    				role.removeFeature(othermelement);
	    			}
	    		}
	    	}
    	}
	}

	/**
	 * @see org.argouml.uml.ui.UMLModelElementListModel#buildPopup(JPopupMenu, int)
	 */
	public boolean buildPopup(JPopupMenu popup, int index) {
		return super.buildPopup(popup, index);
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
			list.addAll(CoreHelper.getHelper().getAllAttributes(base));
		}
		return list;
	}

}
