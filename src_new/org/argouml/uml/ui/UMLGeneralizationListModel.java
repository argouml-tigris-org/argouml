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

// 25 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Tidied up layout, to
// facilitate comparison with UMLSpecializationListModel.java. Corrected
// default null label to "none" rather than "null" for consistency with rest of
// ArgoUML. Made getGeneralizations private, since it is only a local
// utility. It would be nice to make add() use the MMUtil routines, to fully
// create the NSUML object, but they aren't up to it yet, so use the NSUML
// factory. Remove the open() method, since it merely duplicates the parent
// routine.

// 3 May 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to mark the
// project as needing saving if a generalization is added, deleted, changed or
// moved.


package org.argouml.uml.ui;

import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;

import org.argouml.ui.*;
import org.tigris.gef.graph.MutableGraphModel;
import org.argouml.application.api.Argo;
import org.argouml.kernel.*;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;


/**
 * <p>A concrete class to provide the list of model elements that are
 *   generalizations of some other element.</p>
 *
 * <p>This list should support the full set of "Open", "Add", "Delete"</p>
 *
 * <p>Where there is no entry, the default text is "none".</p>
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.foundation.core.UMLGeneralizableElementGeneralizationListModel},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */

public class UMLGeneralizationListModel extends UMLBinaryRelationListModel  {


	/**
	 * Constructor for UMLGeneralizationListModel.
	 * @param container
	 * @param property
	 * @param showNone
	 */
	public UMLGeneralizationListModel(
		UMLUserInterfaceContainer container,
		String property,
		boolean showNone) {
		super(container, property, showNone);
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#build(MModelElement, MModelElement)
	 */
	protected void build(MModelElement from, MModelElement to) {
		if (from instanceof MGeneralizableElement && to instanceof MGeneralizableElement) {
		CoreFactory.getFactory().buildGeneralization((MGeneralizableElement)from, (MGeneralizableElement)to);
		} else
			throw new IllegalArgumentException("In build of UMLGeneralizationListModel: either the arguments are null or not instanceof MGeneralizableElement");
		
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#connect(MutableGraphModel, MModelElement, MModelElement)
	 */
	protected void connect(
		MutableGraphModel gm,
		MModelElement from,
		MModelElement to) {
			gm.connect(from, to, MGeneralization.class);
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getAddDialogTitle()
	 */
	protected String getAddDialogTitle() {
		return Argo.localize("UMLMenu", "dialog.title.add-generalizations");
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getChoices()
	 */
	protected Collection getChoices() {
		return ModelManagementHelper.getHelper().getAllModelElementsOfKind(getTarget().getClass());
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getRelation(MModelElement, MModelElement)
	 */
	protected MModelElement getRelation(MModelElement from, MModelElement to) {
		return CoreHelper.getHelper().getGeneralization((MGeneralizableElement)from, (MGeneralizableElement)to);
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getSelected()
	 */
	protected Collection getSelected() {
                if (getTarget() == null) return new ArrayList();
		if (getTarget() instanceof MGeneralizableElement) {
			return CoreHelper.getHelper().getExtendedClassifiers((MGeneralizableElement)getTarget());
		} else
			throw new IllegalStateException("In getSelected of UMLGeneralizaitonListModel: target is not an instanceof GeneralizbleElement");
	}

} /* End of class UMLGeneralizationListModel */
