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

package org.argouml.uml.ui.behavior.use_cases;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesHelper;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.uml.ui.UMLBinaryRelationListModel;
import org.argouml.uml.ui.UMLConnectionListModel;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import org.tigris.gef.graph.MutableGraphModel;

import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * Binary relation list model for associations with usecases
 * 
 * @author jaap.branderhorst@xs4all.nl
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.foundation.core.PropPanelClassifier#getAssociationEndScroll()},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLUseCaseAssociationListModel
	extends UMLConnectionListModel {

	/**
	 * Constructor for UMLUseCaseAssociationListModel.
	 * @param container
	 * @param property
	 * @param showNone
	 */
	public UMLUseCaseAssociationListModel(
		UMLUserInterfaceContainer container,
		String property,
		boolean showNone) {
		super(container, property, showNone);
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getChoices()
	 */
	protected Collection getChoices() {
		Vector choices = new Vector();	
		choices.addAll(super.getChoices());
		Vector choices2 = new Vector();
		Collection specpath = UseCasesHelper.getHelper().getSpecificationPath((MUseCase)getTarget());
		if (!specpath.isEmpty()) {
			Iterator it = choices.iterator();
			while (it.hasNext()) {
				MClassifier choice = (MClassifier)it.next();
				if (choice instanceof MUseCase) {
					Collection specpath2 = UseCasesHelper.getHelper().getSpecificationPath((MUseCase)choice);
					if (!specpath.equals(specpath2)) choices2.add(choice);
				} else
					choices2.add(choice);
			}
		} else {
			choices2 = choices;
		}
		return choices2;
	}	

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getAddDialogTitle()
	 */
	protected String getAddDialogTitle() {
		return Argo.localize("UMLMenu", "dialog.title.add-associated-usecases");
	}

}
