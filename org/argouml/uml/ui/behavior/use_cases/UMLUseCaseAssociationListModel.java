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
import java.util.Vector;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.uml.ui.UMLBinaryRelationListModel;
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
 */
public class UMLUseCaseAssociationListModel
	extends UMLBinaryRelationListModel {

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
		choices.addAll(CoreHelper.getHelper().getAllClassifiers());
		return choices;
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getSelected()
	 */
	protected Collection getSelected() {
		if (getTarget() instanceof MClassifier) {
			return CoreHelper.getHelper().getAssociatedClassifiers((MClassifier)getTarget());
		} else
			throw new IllegalStateException("Target is no instanceof MClassifier");
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getAddDialogTitle()
	 */
	protected String getAddDialogTitle() {
		return Argo.localize("UMLMenu", "dialog.title.add-realized-interfaces");
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
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#build(MModelElement, MModelElement)
	 */
	protected void build(MModelElement from, MModelElement to) {
		if (from != null && to != null && from instanceof MUseCase && to instanceof MClassifier) {
			CoreFactory.getFactory().buildAssociation((MClassifier)from, (MClassifier)to);
		}
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getRelation(MModelElement, MModelElement)
	 */
	protected MModelElement getRelation(MModelElement from, MModelElement to) {
		if (from instanceof MClassifier && to instanceof MClassifier) {
			return CoreHelper.getHelper().getAssociation((MClassifier)from, (MClassifier)to);
		} else 
			throw new IllegalArgumentException("Tried to get relation between some objects of which one was not a classifier");
	}

}
