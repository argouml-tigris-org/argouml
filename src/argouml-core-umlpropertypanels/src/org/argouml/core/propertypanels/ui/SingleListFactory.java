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
import org.argouml.model.Model;

/**
 * Creates the XML Property panels
 * @author Bob Tarling
 */
class SingleListFactory implements ComponentFactory {
    
    public SingleListFactory() {
    }
    
    public JComponent createComponent(
            final Object modelElement,
            final String propName) {
        
        UMLModelElementListModel model = null;
        UMLSingleRowSelector pane = null;
        
        if ("owner".equals(propName)) {
            model = new UMLFeatureOwnerListModel();
        } else if ("behavioralFeature".equals(propName)) {
            model = new UMLParameterBehavioralFeatListModel();
        } else if ("parent".equals(propName)) {
            model = new UMLGeneralizationParentListModel();
        } else if ("child".equals(propName)) {
            model = new UMLGeneralizationChildListModel();
        } else if ("feature".equals(propName)) {
            model = new UMLParameterBehavioralFeatListModel();
        } else if ("enumeration".equals(propName)) {
            EnumerationListModel m = new EnumerationListModel();
            m.setTarget(modelElement);   
            pane = new UMLSingleRowSelector(m);
        } else if ("association".equals(propName)) {
            model = new UMLAssociationEndAssociationListModel();
        } else if ("base".equals(propName)) {
            model = new UMLExtendBaseListModel();
        } else if ("extension".equals(propName)) {
            model = new UMLExtendExtensionListModel();
        } else if ("addition".equals(propName)) {
            model = new UMLIncludeAdditionListModel();
        } else if ("useCase".equals(propName)) {
            model = new UMLExtensionPointUseCaseListModel();
        } else if ("interaction".equals(propName)) {
            if (Model.getFacade().isAMessage(modelElement)) {
                model = new UMLMessageInteractionListModel();
            } else {
                model = new UMLCollaborationInteractionListModel();                
            }
        } else if ("sender".equals(propName)) {
            model = new UMLMessageSenderListModel();
        } else if ("receiver".equals(propName)) {
            model = new UMLMessageReceiverListModel();
        } else if ("action".equals(propName)) {
            model = new UMLMessageActionListModel();
        } else if ("context".equals(propName)) {
            model = new UMLInteractionContextListModel();
        }
        /*
         * The XML generated is "stimulus", because the A_receiver_stimulus
         * association has "stimulus" and "receiver" as association ends.
         * The A_stimulus_sender has "sender" and "stimulus", so it is generated
         * once. So we have created them by hand with a more explicit name and
         * removed "stimulus".
         */ 
        else if ("sentStimulus".equals(propName)) {
            model = new UMLInstanceSenderStimulusListModel();
        } else if ("receivedStimulus".equals(propName)) {
            model = new UMLInstanceReceiverStimulusListModel();
        } else if ("stateMachine".equals(propName)) {
            model = new UMLTransitionStatemachineListModel();
        } else if ("state".equals(propName)) {
            model = new UMLTransitionStateListModel();
        } else if ("source".equals(propName)) {
            model = new UMLTransitionSourceListModel();
        } else if ("target".equals(propName)) {
            model = new UMLTransitionTargetListModel();
        } else if ("guard".equals(propName)) {
            model = new UMLTransitionGuardListModel();
        } else if ("effect".equals(propName)) {
            model = new UMLTransitionEffectListModel();
        } else if ("trigger".equals(propName)) {
            model = new UMLTransitionTriggerListModel();
        } else if ("transition".equals(propName)) {
            model = new UMLGuardTransitionListModel();
        } else if ("container".equals(propName)) {
            model = new UMLStateVertexContainerListModel();
        } else if ("activityGraph".equals(propName)) {
            model = new UMLPartitionActivityGraphListModel();
        }
        
        if (model != null && pane == null) {
            model.setTarget(modelElement);
            pane = new UMLSingleRowSelector(model);
        }
        
        return pane;
    }
}
