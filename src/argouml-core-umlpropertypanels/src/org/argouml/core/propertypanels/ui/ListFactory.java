/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.core.propertypanels.ui;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;

import org.argouml.core.propertypanels.model.GetterSetterManager;
import org.argouml.model.Model;

/**
 * Creates the XML Property panels
 * @author Bob Tarling
 */
class ListFactory implements ComponentFactory {
    
    public ListFactory() {
    }
    
    public JComponent createComponent(
            final Object modelElement,
            final String propName,
            final String type) {
        JComponent list = null;
        DefaultListModel model = null;
        
        if ("action".equals(propName)) {
            model = new UMLActionSequenceActionListModel(modelElement);
            list = new OldScrollList(new UMLActionSequenceActionList((UMLModelElementListModel) model));     
        } else if ("actualArgument".equals(propName)) {
            model = new UMLActionArgumentListModel(modelElement, propName);
        } else if ("annotatedElement".equals(propName)) {
            model = new UMLCommentAnnotatedElementListModel(modelElement);
        } else if ("association".equals(propName)) {
            model = new UMLClassifierAssociationEndListModel(modelElement);
        } else if ("associationRole".equals(propName)) {
            model = new UMLAssociationAssociationRoleListModel(modelElement);
//        } else if ("attribute".equals(propName)) {
//            model = new UMLClassAttributeListModel(modelElement);
        } else if ("availableContents".equals(propName)) {
            model = new UMLClassifierRoleAvailableContentsListModel(modelElement);
        } else if ("availableFeature".equals(propName)) {
            model = new UMLClassifierRoleAvailableFeaturesListModel(modelElement);
        } else if ("base".equals(propName)) {
            model = new UMLClassifierRoleBaseListModel(modelElement);
        } else if ("baseClass".equals(propName)) {
            model = new UMLStereotypeBaseClassListModel(modelElement);
        } else if ("classifier".equals(propName)) {
            model = new UMLInstanceClassifierListModel(modelElement);
        } else if ("classifierInState".equals(propName)) {
            model = new UMLOFSStateListModel(modelElement);
        } else if ("client".equals(propName)) {
            model = new UMLDependencyClientListModel(modelElement);
        } else if ("clientDependency".equals(propName)) {
            model = new UMLModelElementClientDependencyListModel(modelElement);
        } else if ("connection".equals(propName)) {
            model = new UMLAssociationConnectionListModel(modelElement);
        } else if ("constrainingElement".equals(propName)) {
            model = new UMLCollaborationConstrainingElementListModel(modelElement);
        } else if ("contents".equals(propName)) {
            model = new UMLPartitionContentListModel(modelElement);
        } else if ("context".equals(propName)) {
            model = new UMLSignalContextListModel(modelElement);
        } else if ("deferrableEvent".equals(propName)) {
            model = new UMLStateDeferrableEventListModel(modelElement);
            JList l = new UMLStateDeferrableEventList((UMLModelElementListModel) model);
            list = new OldScrollList(l);                    
        } else if ("definedTag".equals(propName)) {
            model = new UMLStereotypeTagDefinitionListModel(modelElement);
        } else if ("deployedComponent".equals(propName)) {
            model = new UMLNodeDeployedComponentListModel(modelElement);
        } else if ("doActivity".equals(propName)) {
            model = new UMLStateDoActivityListModel(modelElement);
            JList l = new UMLStateDoActivityList((UMLModelElementListModel) model);
            list = new OldScrollList(l);
        } else if ("elementImport".equals(propName)) {
            model = new UMLClassifierPackageImportsListModel(modelElement);
        } else if ("entry".equals(propName)) {
            model = new UMLStateEntryListModel(modelElement);
            JList l = new UMLStateEntryList((UMLModelElementListModel) model);
            list = new OldScrollList(l);
        } else if ("exit".equals(propName)) {
            model = new UMLStateExitListModel(modelElement);
            JList l = new UMLStateExitList((UMLModelElementListModel) model);
            list = new OldScrollList(l);
        } else if ("extend".equals(propName)) {
            model = new UMLUseCaseExtendListModel(modelElement);
        } else if ("extended_elements".equals(propName)) {
            model = new UMLExtendedElementsListModel(modelElement);
        } else if ("extensionPoint".equals(propName)) {
            if (Model.getFacade().isAUseCase(modelElement)) {
                model = new UMLUseCaseExtensionPointListModel(modelElement, propName);
            } else {
                model = new UMLExtendExtensionPointListModel(modelElement, propName);
            }
//        } else if ("feature".equals(propName)) {
//            model = new UMLClassifierFeatureListModel(modelElement);
        } else if ("generalization".equals(propName)) {
            model = new UMLGeneralizableElementGeneralizationListModel(modelElement);
        } else if ("include".equals(propName)) {
            model = new UMLUseCaseIncludeListModel(modelElement);
        } else if ("incoming".equals(propName)) {
            model = new UMLStateVertexIncomingListModel(modelElement);
        } else if ("instantiation".equals(propName)) {
            model = new UMLCreateActionClassifierListModel(modelElement);
        } else if ("internalTransition".equals(propName)) {
            model = new UMLStateInternalTransitionListModel(modelElement);
        } else if ("link".equals(propName)) {
            model = new UMLAssociationLinkListModel(modelElement);
        } else if ("literal".equals(propName)) {
            model = new UMLEnumerationLiteralsListModel(modelElement);
        } else if ("outgoing".equals(propName)) {
            model = new UMLStateVertexOutgoingListModel(modelElement);
        } else if ("partition".equals(propName)) {
            model = new UMLActivityGraphPartitionListModel(modelElement);
        } else if ("predecessor".equals(propName)) {
            model = new UMLMessagePredecessorListModel(modelElement);
        } else if ("qualifier".equals(propName)) {
            model = new UMLAssociationEndQualifiersListModel(modelElement);
        } else if ("reception".equals(propName)) {
            model = new UMLSignalReceptionListModel(modelElement);
        } else if ("resident".equals(propName)) {
            model = new UMLContainerResidentListModel(modelElement);
        } else if ("residentElement".equals(propName)) {
            model = new UMLComponentResidentListModel(modelElement);
        } else if ("signal".equals(propName)) {
            model = new UMLSignalEventSignalListModel(modelElement);
            list = new OldScrollList(new UMLSignalEventSignalList((UMLModelElementListModel) model));                    
        } else if ("specialization".equals(propName)) {
            model = new UMLGeneralizableElementSpecializationListModel(modelElement);
        } else if ("specification".equals(propName)) {
            model = new UMLAssociationEndSpecificationListModel(modelElement);
        } else if ("submachineState".equals(propName)) {
            model = new UMLStateMachineSubmachineStateListModel(modelElement);
        } else if ("subvertex".equals(propName)) {
            model = new UMLCompositeStateSubvertexListModel(modelElement);
            JList l = new UMLCompositeStateSubvertexList((UMLModelElementListModel) model);
            list = new OldScrollList(l);
        } else if ("supplier".equals(propName)) {
            model = new UMLDependencySupplierListModel(modelElement);
        } else if ("supplierDependency".equals(propName)) {
            model = new UMLModelElementSupplierDependencyListModel(modelElement);
        } else if ("top".equals(propName)) {
            model = new UMLStateMachineTopListModel(modelElement);
        } else if ("transition".equals(propName)) {
            model = new UMLEventTransitionListModel(modelElement);
        } else if ("transitions".equals(propName)) {
            model = new UMLStateMachineTransitionListModel(modelElement);
        } else if ("typedValue".equals(propName)) {
            model = new UMLTagDefinitionTypedValuesListModel(modelElement); 
        } else if ("templateParameter".equals(propName)) {
            model = new UMLModelElementTemplateParameterListModel(modelElement); 
        }
        
        if (model == null) {
            final GetterSetterManager getterSetterManager =
                GetterSetterManager.getGetterSetter(type);
            if (getterSetterManager.contains(propName)) {
                model = new SimpleListModel(propName, type, modelElement, getterSetterManager);
            }
        }
        
        if (list != null) {
            return list;
        }
        
        // If we have a model but no list then build the list with
        // preferred constructor. Eventually all lists should be built
        // this way.
        if (model != null) {
            return new RowSelector(model);
        }
        
        return null;
    }
}
