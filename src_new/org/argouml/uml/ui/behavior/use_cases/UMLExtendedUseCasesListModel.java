// $Id$
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

// $header$
package org.argouml.uml.ui.behavior.use_cases;

import org.argouml.model.ModelFacade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesFactory;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.uml.ui.UMLBinaryRelationListModel;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import org.tigris.gef.graph.MutableGraphModel;

/**
 * @since Sep 30, 2002
 * @author jaap.branderhorst@xs4all.nl
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by ?,
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLExtendedUseCasesListModel extends UMLBinaryRelationListModel {

    /**
     * Constructor for UMLExtendedUseCasesListModel.
     * @param container
     * @param property
     * @param showNone
     */
    public UMLExtendedUseCasesListModel(
        UMLUserInterfaceContainer container,
        String property,
        boolean showNone) {
        super(container, property, showNone);
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getChoices()
     */
    protected Collection getChoices() {
        if (org.argouml.model.ModelFacade.isAExtensionPoint(getTarget())) {
            Collection col = ModelManagementHelper.getHelper().getAllModelElementsOfKind((Class)ModelFacade.USE_CASE);
            col.remove(ModelFacade.getUseCase(getTarget()));
            return col;
        } else
            throw new IllegalStateException("In getChoices of UMLExtendedUseCasesListModel: target is not an instance of MExtensionPoint");
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getSelected()
     */
    protected Collection getSelected() {
        if (ModelFacade.isAExtensionPoint(getTarget())) {
            Object base = ModelFacade.getUseCase(getTarget());
            return UseCasesHelper.getHelper().getExtendingUseCases(base);
        } else
            throw new IllegalStateException("In getSelected of UMLExtendedUseCasesListModel: target is not an instance of MExtensionPoint");
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getAddDialogTitle()
     */
    protected String getAddDialogTitle() {
        return Argo.localize("UMLMenu", "dialog.title.add-extending-usecases");
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#connect(MutableGraphModel, MModelElement, MModelElement)
     */
    protected void connect(
        MutableGraphModel gm,
        Object/*MModelElement*/ from,
        Object/*MModelElement*/ to) {
            
        gm.connect(to, from, (Class)ModelFacade.EXTEND);
        List list = new ArrayList();
        list.add(getTarget());
        Object e = UseCasesHelper.getHelper().getExtends(/*(MUseCase)*/ from, /*(MUseCase)*/ to);
        UmlFactory.getFactory().delete(ModelFacade.getExtensionPoint(e, 0));
        ModelFacade.setExtensionPoints(e, list);
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#build(MModelElement, MModelElement)
     */
    protected void build(Object/*MModelElement*/ from, Object/*MModelElement*/ to) {
        UseCasesFactory.getFactory().buildExtend(/*(MUseCase)*/ to, /*(MUseCase)*/ from, /*(MExtensionPoint)*/ getTarget());    
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getRelation(MModelElement, MModelElement)
     */
    protected Object getRelation(Object from, Object to) {
        return UseCasesHelper.getHelper().getExtends(/*(MUseCase)*/ from, /*(MUseCase)*/ to);
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getSource()
     */
    protected Object getSource() {
        return ModelFacade.getUseCase(getTarget());
    }

}