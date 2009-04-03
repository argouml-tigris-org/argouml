// $Id: ListFactory.java 17009 2009-03-31 22:53:20Z bobtarling $
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

import javax.swing.JComponent;
import javax.swing.JList;
import org.argouml.model.Model;
import org.argouml.uml.ui.behavior.use_cases.ActionAddExtendExtensionPoint;
import org.argouml.uml.ui.behavior.use_cases.ActionNewExtendExtensionPoint;
import org.argouml.uml.ui.behavior.use_cases.ActionNewUseCaseExtensionPoint;

/**
 * Creates the XML Property panels
 * @author Bob Tarling
 */
class ListFactory implements ComponentFactory {
    
    public ListFactory() {
    }
    
    public JComponent createComponent(
            final Object modelElement,
            final String propName) {
        ScrollList list = null;
        UMLModelElementListModel model = null;
        
        Object metaType = modelElement.getClass();
            
        if ("clientDependency".equals(propName)) {
            model = new UMLModelElementClientDependencyListModel(metaType);
        } else if ("supplierDependency".equals(propName)) {
            model = new UMLModelElementSupplierDependencyListModel(metaType);
        } else if ("generalization".equals(propName)) {
            model = new UMLGeneralizableElementGeneralizationListModel();
        } else if ("specialization".equals(propName)) {
            model = new UMLGeneralizableElementSpecializationListModel();
        } else if ("attribute".equals(propName)) {
            model = new UMLClassAttributeListModel();
        } else if ("association".equals(propName)) {
            model = new UMLClassifierAssociationEndListModel();
        } else if ("feature".equals(propName)) {
            model = new UMLClassifierFeatureListModel();
        } else if ("operation".equals(propName)) {
            model = new UMLClassOperationListModel();
        } else if ("ownedElement".equals(propName)) {
            model = new UMLNamespaceOwnedElementListModel();
        } else if ("elementImport".equals(propName)) {
            model = new UMLClassifierPackageImportsListModel();
            model.setTarget(modelElement);
            list = new ScrollList(new UMLMutableLinkedList(model,
                    // TODO: It's OK to change the visibility of this actions?
                    null, // new ActionAddPackageImport(),
                    null,
                    null));
        } else if ("parameter".equals(propName)) {
            if (Model.getFacade().isAObjectFlowState(modelElement)) {
                model = new UMLObjectFlowStateParameterListModel(metaType);
            } else {
                model = new UMLClassifierParameterListModel();
                model.setTarget(modelElement);
                list = new ScrollList(new UMLLinkedList(model, 
                        true, false));
            }
        } else if ("raisedSignal".equals(propName)) {
            model = new UMLOperationRaisedSignalsListModel();
        } else if ("method".equals(propName)) {
            model = new UMLOperationMethodsListModel();
        } else if ("definedTag".equals(propName)) {
            model = new UMLStereotypeTagDefinitionListModel();
        } else if ("baseClass".equals(propName)) {
            model = new UMLStereotypeBaseClassListModel(metaType);
        } else if ("extended_elements".equals(propName)) {
            model = new UMLExtendedElementsListModel();
        } else if ("literal".equals(propName)) {
            model = new UMLEnumerationLiteralsListModel();
        } else if ("supplier".equals(propName)) {
            model = new UMLDependencySupplierListModel();
        } else if ("client".equals(propName)) {
            model = new UMLDependencyClientListModel();
        } else if ("connection".equals(propName)) {
            model = new UMLAssociationConnectionListModel();
        } else if ("associationRole".equals(propName)) {
            model = new UMLAssociationAssociationRoleListModel();
        } else if ("link".equals(propName)) {
            model = new UMLAssociationLinkListModel();
        } else if ("specification".equals(propName)) {
            model = new UMLAssociationEndSpecificationListModel(metaType);
        } else if ("qualifier".equals(propName)) {
            model = new UMLAssociationEndQualifiersListModel();
        } else if ("annotatedElement".equals(propName)) {
            model = new UMLCommentAnnotatedElementListModel();
            model.setTarget(modelElement);
            UMLMutableLinkedList l = new UMLMutableLinkedList(
                    model, null, null, null);
            list = new ScrollList(l);
        } else if ("context".equals(propName)) {
            model = new UMLSignalContextListModel(metaType);
        } else if ("reception".equals(propName)) {
            model = new UMLSignalReceptionListModel(metaType);
        } else if ("extend".equals(propName)) {
            model = new UMLUseCaseExtendListModel();
        } else if ("include".equals(propName)) {
            model = new UMLUseCaseIncludeListModel();
        } else if ("extensionPoint".equals(propName)) {
            if (Model.getFacade().isAUseCase(modelElement)) {
                model = new UMLUseCaseExtensionPointListModel();
                model.setTarget(modelElement);
                JList l = new UMLMutableLinkedList(
                            model, null,
                            ActionNewUseCaseExtensionPoint.SINGLETON);
                list = new ScrollList(l);
            } else {
                model = new UMLExtendExtensionPointListModel();
                model.setTarget(modelElement);
                JList l =
                    new UMLMutableLinkedList(model, 
                        ActionAddExtendExtensionPoint.getInstance(),
                        ActionNewExtendExtensionPoint.SINGLETON);
                list = new ScrollList(l);
            }
        } else if ("base".equals(propName)) {
            model = new UMLClassifierRoleBaseListModel(metaType);
        } else if ("availableFeature".equals(propName)) {
            model = new UMLClassifierRoleAvailableFeaturesListModel();
        } else if ("availableContents".equals(propName)) {
            model = new UMLClassifierRoleAvailableContentsListModel();
        } else if ("predecessor".equals(propName)) {
            model = new UMLMessagePredecessorListModel(metaType);
        } else if ("actualArgument".equals(propName)) {
            model = new UMLActionArgumentListModel();
        } else if ("instantiation".equals(propName)) {
            model = new UMLCreateActionClassifierListModel(metaType);
        } else if ("constrainingElement".equals(propName)) {
            model = new UMLCollaborationConstrainingElementListModel();
        } else if ("top".equals(propName)) {
            model = new UMLStateMachineTopListModel();
        } else if ("transitions".equals(propName)) {
            model = new UMLStateMachineTransitionListModel();
        } else if ("transition".equals(propName)) {
            model = new UMLEventTransitionListModel();
        } else if ("submachineState".equals(propName)) {
            model = new UMLStateMachineSubmachineStateListModel();
        } else if ("message".equals(propName)) {
            model = new UMLInteractionMessagesListModel();
        } else if ("deployedComponent".equals(propName)) {
            model = new UMLNodeDeployedComponentListModel();
        } else if ("residentElement".equals(propName)) {
            model = new UMLComponentResidentListModel();
        } else if ("classifier".equals(propName)) {
            model = new UMLInstanceClassifierListModel(metaType);
        } else if ("resident".equals(propName)) {
            model = new UMLContainerResidentListModel();
        } else if ("entry".equals(propName)) {
            model = new UMLStateEntryListModel();
            model.setTarget(modelElement);
            JList l = new UMLStateEntryList(model);
            list = new ScrollList(l);
        } else if ("exit".equals(propName)) {
            model = new UMLStateExitListModel();
            model.setTarget(modelElement);
            JList l = new UMLStateExitList(model);
            list = new ScrollList(l);
        } else if ("deferrableEvent".equals(propName)) {
            model = new UMLStateDeferrableEventListModel();
            model.setTarget(modelElement);
            JList l = new UMLStateDeferrableEventList(model);
            list = new ScrollList(l);                    
        } else if ("doActivity".equals(propName)) {
            model = new UMLStateDoActivityListModel();
            model.setTarget(modelElement);
            JList l = new UMLStateDoActivityList(model);
            list = new ScrollList(l);
        } else if ("outgoing".equals(propName)) {
            model = new UMLStateVertexOutgoingListModel();
        } else if ("incoming".equals(propName)) {
            model = new UMLStateVertexIncomingListModel();
        } else if ("classifierInState".equals(propName)) {
            model = new UMLOFSStateListModel(metaType);
        } else if ("internalTransition".equals(propName)) {
            model = new UMLStateInternalTransitionListModel(metaType);
        } else if ("subvertex".equals(propName)) {
            model = new UMLCompositeStateSubvertexListModel();
            model.setTarget(modelElement);
            JList l = new UMLCompositeStateSubvertexList(model);
            list = new ScrollList(l);
        } else if ("contents".equals(propName)) {
            model = new UMLPartitionContentListModel(metaType);
        } else if ("partition".equals(propName)) {
            model = new UMLActivityGraphPartitionListModel();
        } else if ("signal".equals(propName)) {
            model = new UMLSignalEventSignalListModel();
            model.setTarget(modelElement);
            list = new ScrollList(new UMLSignalEventSignalList(model));                    
        } else if ("action".equals(propName)) {
            model = new UMLActionSequenceActionListModel();
            model.setTarget(modelElement);
            list = new ScrollList(new UMLActionSequenceActionList(model));
        }
        
        if (model != null && list == null) {
            // If we have a model but no list then build the list with
            // preferred constructor. Eventually all lists should be built
            // this way.
            model.setTarget(modelElement);
            list = new ScrollList(model);
        }
        
        return list;
    }
}
