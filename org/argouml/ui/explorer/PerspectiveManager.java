// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.ui.explorer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.argouml.uml.diagram.collaboration.ui.GoClassifierToCollaboration;
import org.argouml.uml.diagram.collaboration.ui.GoCollaborationInteraction;
import org.argouml.uml.diagram.collaboration.ui.GoInteractionMessage;
import org.argouml.uml.diagram.collaboration.ui.GoMessageAction;
import org.argouml.uml.diagram.collaboration.ui.GoModelToCollaboration;
import org.argouml.uml.diagram.collaboration.ui.GoOperationToCollaboration;
import org.argouml.uml.diagram.deployment.ui.GoDiagramToNode;
import org.argouml.uml.diagram.sequence.ui.GoLinkStimuli;
import org.argouml.uml.diagram.sequence.ui.GoStimulusToAction;
import org.argouml.uml.diagram.state.ui.GoCompositeStateToSubvertex;
import org.argouml.uml.diagram.state.ui.GoMachineDiagram;
import org.argouml.uml.diagram.state.ui.GoMachineToState;
import org.argouml.uml.diagram.state.ui.GoMachineToTrans;
import org.argouml.uml.diagram.state.ui.GoProjectToStateMachine;
import org.argouml.uml.diagram.state.ui.GoStateMachineToTransition;
import org.argouml.uml.diagram.state.ui.GoStateToDoActivity;
import org.argouml.uml.diagram.state.ui.GoStateToEntry;
import org.argouml.uml.diagram.state.ui.GoStateToExit;
import org.argouml.uml.diagram.state.ui.GoStateToIncomingTrans;
import org.argouml.uml.diagram.state.ui.GoStateToInternalTrans;
import org.argouml.uml.diagram.state.ui.GoStateToOutgoingTrans;
import org.argouml.uml.diagram.state.ui.GoTransitionToSource;
import org.argouml.uml.diagram.state.ui.GoTransitionToTarget;
import org.argouml.uml.diagram.static_structure.ui.GoClassToAssociatedClass;
import org.argouml.uml.diagram.static_structure.ui.GoClassToSummary;

import org.argouml.uml.diagram.static_structure.ui.GoModelToClass;
import org.argouml.uml.diagram.static_structure.ui.GoSummaryToAssociation;
import org.argouml.uml.diagram.static_structure.ui.GoSummaryToAttribute;
import org.argouml.uml.diagram.static_structure.ui.GoSummaryToIncomingDependency;
import org.argouml.uml.diagram.static_structure.ui.GoSummaryToInheritance;
import org.argouml.uml.diagram.static_structure.ui.GoSummaryToOperation;
import org.argouml.uml.diagram.static_structure.ui.GoSummaryToOutgoingDependency;
import org.argouml.uml.diagram.ui.GoBehavioralFeatureToStateDiagram;
import org.argouml.uml.diagram.ui.GoBehavioralFeatureToStateMachine;
import org.argouml.uml.diagram.ui.GoClassifierToBeh;
import org.argouml.uml.diagram.ui.GoClassifierToStateMachine;
import org.argouml.uml.diagram.ui.GoClassifierToStructuralFeature;

import org.argouml.uml.diagram.ui.GoDiagramToEdge;
import org.argouml.uml.diagram.ui.GoGenElementToDerived;
import org.argouml.uml.diagram.ui.GoModelToBaseElements;
import org.argouml.uml.diagram.ui.GoNamespaceToDiagram;
import org.argouml.uml.diagram.ui.GoOperationToCollaborationDiagram;
import org.argouml.uml.diagram.ui.GoProjectToDiagram;
import org.argouml.uml.diagram.ui.GoProjectToModel;



import org.argouml.uml.diagram.use_case.ui.GoUseCaseToExtensionPoint;
import org.argouml.uml.ui.behavior.common_behavior.GoSignalToReception;

/**
 * Provides a model and event management for perspectives(views) of the
 * Explorer.
 *
 * @author  alexb
 * @since 0.15.2
 */
public class PerspectiveManager {
    
    private static PerspectiveManager instance;
    
    private List perspectiveListeners;
    
    private List perspectives;
    
    public static PerspectiveManager getInstance() {
        if (instance == null) {
            instance = new PerspectiveManager();
        }
        return instance;
    }

    /** Creates a new instance of PerspectiveManager */
    private PerspectiveManager() {
        
        perspectiveListeners = new ArrayList();
        perspectives = new ArrayList();
    }
    
    public void addListener(PerspectiveManagerListener listener){
        
        perspectiveListeners.add(listener);
    }
    
    public void removeListener(PerspectiveManagerListener listener){
        
        perspectiveListeners.remove(listener);
    }
    
    public void addPerspective(Object perspective){
        
        perspectives.add(perspective);
        Iterator listenerIt = perspectiveListeners.iterator();
        while(listenerIt.hasNext()){
            
            PerspectiveManagerListener listener = 
                (PerspectiveManagerListener)listenerIt.next();
            
            listener.addPerspective(perspective);
        }
    }
    
    public void removePerspective(Object perspective){
        
        perspectives.remove(perspective);
        Iterator listenerIt = perspectiveListeners.iterator();
        while(listenerIt.hasNext()){
            
            PerspectiveManagerListener listener = 
                (PerspectiveManagerListener)listenerIt.next();
            
            listener.removePerspective(perspective);
        }
    }
    
    public List getPerspectives(){
        
        return perspectives;
    }
    
    public void loadDefaultPerspectives(){
        
        ExplorerPerspective classPerspective = 
            new ExplorerPerspective("Class centric");
        classPerspective.addRule(new GoNamespaceToClassifierAndPackage());
        classPerspective.addRule(new GoNamespaceToDiagram());
        classPerspective.addRule(new GoClassToSummary());
        classPerspective.addRule(new GoSummaryToAssociation());
        classPerspective.addRule(new GoSummaryToAttribute());
        classPerspective.addRule(new GoSummaryToOperation());
        classPerspective.addRule(new GoSummaryToInheritance());
        classPerspective.addRule(new GoSummaryToIncomingDependency());
        classPerspective.addRule(new GoSummaryToOutgoingDependency());
        
        ExplorerPerspective packagePerspective = 
            new ExplorerPerspective("combobox.item.package-centric");
        packagePerspective.addRule(new GoProjectToModel());
        packagePerspective.addRule(new GoNamespaceToOwnedElements());
        packagePerspective.addRule(new GoNamespaceToDiagram());
        packagePerspective.addRule(new GoModelToCollaboration());
        packagePerspective.addRule(new GoUseCaseToExtensionPoint());
        packagePerspective.addRule(new GoClassifierToStructuralFeature());
        packagePerspective.addRule(new GoClassifierToBeh());
        packagePerspective.addRule(new GoCollaborationInteraction());
        packagePerspective.addRule(new GoInteractionMessage());
        packagePerspective.addRule(new GoMessageAction());
        packagePerspective.addRule(new GoSignalToReception());
        packagePerspective.addRule(new GoLinkStimuli());
        packagePerspective.addRule(new GoStimulusToAction());
        packagePerspective.addRule(new GoClassifierToCollaboration());
        packagePerspective.addRule(new GoOperationToCollaboration());
        packagePerspective.addRule(new GoOperationToCollaborationDiagram());
        packagePerspective.addRule(new GoBehavioralFeatureToStateMachine());
        packagePerspective.addRule(new GoBehavioralFeatureToStateDiagram());
        packagePerspective.addRule(new GoClassifierToStateMachine());
        packagePerspective.addRule(new GoMachineToState());
        packagePerspective.addRule(new GoCompositeStateToSubvertex());
        packagePerspective.addRule(new GoStateToInternalTrans());
        packagePerspective.addRule(new GoStateMachineToTransition());
        packagePerspective.addRule(new GoStateToDoActivity());
        packagePerspective.addRule(new GoStateToEntry());
        packagePerspective.addRule(new GoStateToExit());
        
        ExplorerPerspective diagramPerspective = 
            new ExplorerPerspective("combobox.item.diagram-centric");
        diagramPerspective.addRule(new GoProjectToDiagram());
        diagramPerspective.addRule(new GoDiagramToNode());
        diagramPerspective.addRule(new GoDiagramToEdge());
        diagramPerspective.addRule(new GoUseCaseToExtensionPoint());
        diagramPerspective.addRule(new GoClassifierToStructuralFeature());
        diagramPerspective.addRule(new GoClassifierToBeh());
        
        ExplorerPerspective inheritancePerspective = 
            new ExplorerPerspective("combobox.item.inheritance-centric");
        inheritancePerspective.addRule(new GoProjectToModel());
        inheritancePerspective.addRule(new GoModelToBaseElements());
        inheritancePerspective.addRule(new GoGenElementToDerived());
        
        ExplorerPerspective associationsPerspective = 
            new ExplorerPerspective("combobox.item.class-associations");
        associationsPerspective.addRule(new GoProjectToModel());
        associationsPerspective.addRule(new GoNamespaceToDiagram());
        associationsPerspective.addRule(new GoModelToClass());
        associationsPerspective.addRule(new GoClassToAssociatedClass());
        
        ExplorerPerspective statePerspective = 
            new ExplorerPerspective("combobox.item.state-centric");
        statePerspective.addRule(new GoProjectToStateMachine());
        statePerspective.addRule(new GoMachineDiagram());
        statePerspective.addRule(new GoMachineToState());
        statePerspective.addRule(new GoCompositeStateToSubvertex());
        statePerspective.addRule(new GoStateToIncomingTrans());
        statePerspective.addRule(new GoStateToOutgoingTrans());
        
        ExplorerPerspective transitionsPerspective = 
            new ExplorerPerspective("combobox.item.transitions-centric");
        transitionsPerspective.addRule(new GoProjectToStateMachine());
        transitionsPerspective.addRule(new GoMachineDiagram());
        transitionsPerspective.addRule(new GoMachineToTrans());
        transitionsPerspective.addRule(new GoTransitionToSource());
        transitionsPerspective.addRule(new GoTransitionToTarget());
        
        addPerspective(packagePerspective);
        addPerspective(classPerspective);
        addPerspective(diagramPerspective);
        addPerspective(inheritancePerspective);
        addPerspective(associationsPerspective);
        addPerspective(statePerspective);
        addPerspective(transitionsPerspective);
    }
}
