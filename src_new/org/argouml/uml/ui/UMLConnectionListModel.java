// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// 3 May 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to mark the
// project as needing saving if an association is added, deleted, changed or
// moved.

package org.argouml.uml.ui;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.ui.*;
import org.tigris.gef.graph.MutableGraphModel;
import org.argouml.application.api.Argo;
import org.argouml.kernel.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.foundation.core.PropPanelClassifier#getAssociationEndScroll()},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLConnectionListModel extends UMLBinaryRelationListModel  {

    

	/**
	 * Constructor for UMLConnectionListModel.
	 * @param container
	 * @param property
	 * @param showNone
	 */
	public UMLConnectionListModel(
		UMLUserInterfaceContainer container,
		String property,
		boolean showNone) {
		super(container, property, showNone);
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#build(MModelElement, MModelElement)
	 */
	protected void build(MModelElement from, MModelElement to) {
		CoreFactory.getFactory().buildAssociation((MClassifier)from, (MClassifier)to);
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#connect(MutableGraphModel, MModelElement, MModelElement)
	 */
	protected void connect(
		MutableGraphModel gm,
		MModelElement from,
		MModelElement to) {
			gm.connect(from, to, MAssociation.class);
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getAddDialogTitle()
	 */
	protected String getAddDialogTitle() {
		return Argo.localize("UMLMenu", "dialog.add-associations");
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getChoices()
	 */
	protected Collection getChoices() {
		return ModelManagementHelper.getHelper().getAllModelElementsOfKind(MClassifier.class);
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getRelation(MModelElement, MModelElement)
	 */
	protected MModelElement getRelation(MModelElement from, MModelElement to) {
            // this could get awkward but we assume that there is only one association
	   return (MModelElement)((CoreHelper.getHelper().getAssociations((MClassifier)from, (MClassifier)to)).toArray())[0];
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getSelected()
	 */
	protected Collection getSelected() {
		return CoreHelper.getHelper().getAssociatedClassifiers((MClassifier)getTarget());
	}

}




