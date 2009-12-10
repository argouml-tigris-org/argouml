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
        JComponent list = null;
        UMLModelElementListModel model = null;
        
        if ("clientDependency".equals(propName)) {
            model = new UMLModelElementClientDependencyListModel(modelElement);
        } else if ("supplierDependency".equals(propName)) {
            model = new UMLModelElementSupplierDependencyListModel(modelElement);
        } else if ("generalization".equals(propName)) {
            model = new UMLGeneralizableElementGeneralizationListModel(modelElement);
        } else if ("specialization".equals(propName)) {
            model = new UMLGeneralizableElementSpecializationListModel(modelElement);
        } else if ("attribute".equals(propName)) {
            model = new UMLClassAttributeListModel(modelElement);
        } else if ("association".equals(propName)) {
            model = new UMLClassifierAssociationEndListModel(modelElement);
        } else if ("feature".equals(propName)) {
            model = new UMLClassifierFeatureListModel(modelElement);
        } else if ("operation".equals(propName)) {
            model = new UMLClassOperationListModel(modelElement);
        } else if ("ownedElement".equals(propName)) {
            model = new UMLNamespaceOwnedElementListModel(modelElement);
        } else if ("elementImport".equals(propName)) {
            model = new UMLClassifierPackageImportsListModel(modelElement);
        } else if ("parameter".equals(propName)) {
            if (Model.getFacade().isAObjectFlowState(modelElement)) {
                model = new UMLObjectFlowStateParameterListModel(modelElement);
            } else {
                model = new UMLClassifierParameterListModel(modelElement);
            }
        } else if ("raisedSignal".equals(propName)) {
            model = new UMLOperationRaisedSignalsListModel(modelElement);
        } else if ("method".equals(propName)) {
            model = new UMLOperationMethodsListModel(modelElement);
        } else if ("definedTag".equals(propName)) {
            model = new UMLStereotypeTagDefinitionListModel(modelElement);
        } else if ("baseClass".equals(propName)) {
            model = new UMLStereotypeBaseClassListModel(modelElement);
        } else if ("extended_elements".equals(propName)) {
            model = new UMLExtendedElementsListModel(modelElement);
        } else if ("literal".equals(propName)) {
            model = new UMLEnumerationLiteralsListModel(modelElement);
        } else if ("supplier".equals(propName)) {
            model = new UMLDependencySupplierListModel(modelElement);
        } else if ("client".equals(propName)) {
            model = new UMLDependencyClientListModel(modelElement);
        } else if ("connection".equals(propName)) {
            model = new UMLAssociationConnectionListModel(modelElement);
        } else if ("associationRole".equals(propName)) {
            model = new UMLAssociationAssociationRoleListModel(modelElement);
        } else if ("link".equals(propName)) {
            model = new UMLAssociationLinkListModel(modelElement);
        } else if ("specification".equals(propName)) {
            model = new UMLAssociationEndSpecificationListModel(modelElement);
        } else if ("qualifier".equals(propName)) {
            model = new UMLAssociationEndQualifiersListModel(modelElement);
        } else if ("annotatedElement".equals(propName)) {
            model = new UMLCommentAnnotatedElementListModel(modelElement);
        } else if ("context".equals(propName)) {
            model = new UMLSignalContextListModel(modelElement);
        } else if ("reception".equals(propName)) {
            model = new UMLSignalReceptionListModel(modelElement);
        } else if ("extend".equals(propName)) {
            model = new UMLUseCaseExtendListModel(modelElement);
        } else if ("include".equals(propName)) {
            model = new UMLUseCaseIncludeListModel(modelElement);
        } else if ("extensionPoint".equals(propName)) {
            if (Model.getFacade().isAUseCase(modelElement)) {
                model = new UMLUseCaseExtensionPointListModel(modelElement);
            } else {
                model = new UMLExtendExtensionPointListModel(modelElement);
            }
        } else if ("base".equals(propName)) {
            model = new UMLClassifierRoleBaseListModel(modelElement);
        } else if ("availableFeature".equals(propName)) {
            model = new UMLClassifierRoleAvailableFeaturesListModel(modelElement);
        } else if ("availableContents".equals(propName)) {
            model = new UMLClassifierRoleAvailableContentsListModel(modelElement);
        } else if ("predecessor".equals(propName)) {
            model = new UMLMessagePredecessorListModel(modelElement);
        } else if ("actualArgument".equals(propName)) {
            model = new UMLActionArgumentListModel(modelElement);
        } else if ("instantiation".equals(propName)) {
            model = new UMLCreateActionClassifierListModel(modelElement);
        } else if ("constrainingElement".equals(propName)) {
            model = new UMLCollaborationConstrainingElementListModel(modelElement);
        } else if ("top".equals(propName)) {
            model = new UMLStateMachineTopListModel(modelElement);
        } else if ("transitions".equals(propName)) {
            model = new UMLStateMachineTransitionListModel(modelElement);
        } else if ("transition".equals(propName)) {
            model = new UMLEventTransitionListModel(modelElement);
        } else if ("submachineState".equals(propName)) {
            model = new UMLStateMachineSubmachineStateListModel(modelElement);
        } else if ("message".equals(propName)) {
            model = new UMLInteractionMessagesListModel(modelElement);
        } else if ("deployedComponent".equals(propName)) {
            model = new UMLNodeDeployedComponentListModel(modelElement);
        } else if ("residentElement".equals(propName)) {
            model = new UMLComponentResidentListModel(modelElement);
        } else if ("classifier".equals(propName)) {
            model = new UMLInstanceClassifierListModel(modelElement);
        } else if ("resident".equals(propName)) {
            model = new UMLContainerResidentListModel(modelElement);
        } else if ("entry".equals(propName)) {
            model = new UMLStateEntryListModel(modelElement);
            JList l = new UMLStateEntryList(model);
            list = new ScrollList(l);
        } else if ("exit".equals(propName)) {
            model = new UMLStateExitListModel(modelElement);
            JList l = new UMLStateExitList(model);
            list = new ScrollList(l);
        } else if ("deferrableEvent".equals(propName)) {
            model = new UMLStateDeferrableEventListModel(modelElement);
            JList l = new UMLStateDeferrableEventList(model);
            list = new ScrollList(l);                    
        } else if ("doActivity".equals(propName)) {
            model = new UMLStateDoActivityListModel(modelElement);
            JList l = new UMLStateDoActivityList(model);
            list = new ScrollList(l);
        } else if ("outgoing".equals(propName)) {
            model = new UMLStateVertexOutgoingListModel(modelElement);
        } else if ("incoming".equals(propName)) {
            model = new UMLStateVertexIncomingListModel(modelElement);
        } else if ("classifierInState".equals(propName)) {
            model = new UMLOFSStateListModel(modelElement);
        } else if ("internalTransition".equals(propName)) {
            model = new UMLStateInternalTransitionListModel(modelElement);
        } else if ("subvertex".equals(propName)) {
            model = new UMLCompositeStateSubvertexListModel(modelElement);
            JList l = new UMLCompositeStateSubvertexList(model);
            list = new ScrollList(l);
        } else if ("contents".equals(propName)) {
            model = new UMLPartitionContentListModel(modelElement);
        } else if ("partition".equals(propName)) {
            model = new UMLActivityGraphPartitionListModel(modelElement);
        } else if ("signal".equals(propName)) {
            model = new UMLSignalEventSignalListModel(modelElement);
            list = new ScrollList(new UMLSignalEventSignalList(model));                    
        } else if ("action".equals(propName)) {
            model = new UMLActionSequenceActionListModel(modelElement);
            list = new ScrollList(new UMLActionSequenceActionList(model));     
        } else if ("typedValue".equals(propName)) {
            model = new UMLTagDefinitionTypedValuesListModel(modelElement); 
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
