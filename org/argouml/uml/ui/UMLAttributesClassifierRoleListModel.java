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
	    MClassifierRole role = (MClassifierRole) target;	
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
		    MFeature othermelement = (MFeature) it.next();
		    if (!selected.contains(othermelement)) {
			role.addFeature(othermelement);
		    }
		}
		it = selected.iterator();
		while (it.hasNext()) {
		    MFeature othermelement = (MFeature) it.next();
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
	MClassifierRole role = (MClassifierRole) getTarget();
	if (role.getBases().isEmpty()) return new ArrayList();
	List list = new ArrayList();
	Iterator it = role.getBases().iterator();
	while (it.hasNext()) {
	    MClassifier base = (MClassifier) it.next();
	    list.addAll(CoreHelper.getHelper().getAllAttributes(base));
	}
	return list;
    }

}
