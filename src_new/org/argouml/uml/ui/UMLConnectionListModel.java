// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

import java.util.*;

import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.tigris.gef.graph.MutableGraphModel;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003), replaced by 
 *             {@link org.argouml.uml.ui.foundation.core.PropPanelClassifier#getAssociationEndScroll()},
 *             this class is part of the 'old'(pre 0.13.*) 
 *             implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLConnectionListModel extends UMLBinaryRelationListModel  {

    /**
     * Constructor for UMLConnectionListModel.
     * @param container the container for UI elements
     * @param property the property name
     * @param showNone true if we have to show a "none" 
     *                 for elements without name
     */
    public UMLConnectionListModel(UMLUserInterfaceContainer container,
				  String property,
				  boolean showNone) {
	super(container, property, showNone);
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#build(Object,Object)
     */
    protected void build(Object/*MModelElement*/ from, 
            Object/*MModelElement*/ to) {
	CoreFactory.getFactory().buildAssociation(from, to);
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#connect(
     * MutableGraphModel, Object, Object)
     */
    protected void connect(
			   MutableGraphModel gm,
			   Object/*MModelElement*/ from,
			   Object/*MModelElement*/ to) {
	gm.connect(from, to, (Class) ModelFacade.ASSOCIATION);
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getAddDialogTitle()
     */
    protected String getAddDialogTitle() {
	return Translator.localize("UMLMenu", "dialog.add-associations");
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getChoices()
     */
    protected Collection getChoices() {
	return ModelManagementHelper.getHelper()
	    .getAllModelElementsOfKind((Class) ModelFacade.CLASSIFIER);
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getRelation(Object,Object)
     */
    protected Object getRelation(Object from, Object to) {
	// this could get awkward but we assume 
        // that there is only one association
	return ((CoreHelper.getHelper().getAssociations(from, to))
            .toArray())[0];
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getSelected()
     */
    protected Collection getSelected() {
	return CoreHelper.getHelper().getAssociatedClassifiers(getTarget());
    }

}



