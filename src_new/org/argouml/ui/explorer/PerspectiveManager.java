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
import java.util.List;
import java.util.Iterator;

import org.argouml.kernel.PredAND;
import org.argouml.kernel.PredInstanceOf;
import org.argouml.kernel.PredNotInstanceOf;
import org.argouml.kernel.PredOR;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.NavPerspective;
import org.argouml.uml.diagram.collaboration.ui.GoAssocRoleMessages;
import org.argouml.uml.diagram.collaboration.ui.GoClassifierToCollaboration;
import org.argouml.uml.diagram.collaboration.ui.GoCollaborationDiagram;
import org.argouml.uml.diagram.collaboration.ui.GoCollaborationInteraction;
import org.argouml.uml.diagram.collaboration.ui.GoInteractionMessage;
import org.argouml.uml.diagram.collaboration.ui.GoMessageAction;
import org.argouml.uml.diagram.collaboration.ui.GoModelToCollaboration;
import org.argouml.uml.diagram.collaboration.ui.GoOperationToCollaboration;
import org.argouml.uml.diagram.collaboration.ui.GoProjectToCollaboration;
import org.argouml.uml.diagram.deployment.ui.GoDiagramToNode;
import org.argouml.uml.diagram.sequence.ui.GoLinkStimuli;
import org.argouml.uml.diagram.sequence.ui.GoStimulusToAction;
import org.argouml.uml.diagram.state.PredIsFinalState;
import org.argouml.uml.diagram.state.PredIsStartState;
import org.argouml.uml.diagram.state.ui.GoCompositeStateToSubvertex;
import org.argouml.uml.diagram.state.ui.GoMachineDiagram;
import org.argouml.uml.diagram.state.ui.GoMachineToState;
import org.argouml.uml.diagram.state.ui.GoMachineToTrans;
import org.argouml.uml.diagram.state.ui.GoProjectToStateMachine;
import org.argouml.uml.diagram.state.ui.GoStateMachineToTransition;
import org.argouml.uml.diagram.state.ui.GoStateToDoActivity;
import org.argouml.uml.diagram.state.ui.GoStateToDownstream;
import org.argouml.uml.diagram.state.ui.GoStateToEntry;
import org.argouml.uml.diagram.state.ui.GoStateToExit;
import org.argouml.uml.diagram.state.ui.GoStateToIncomingTrans;
import org.argouml.uml.diagram.state.ui.GoStateToInternalTrans;
import org.argouml.uml.diagram.state.ui.GoStateToOutgoingTrans;
import org.argouml.uml.diagram.state.ui.GoTransitionToSource;
import org.argouml.uml.diagram.state.ui.GoTransitionToTarget;
import org.argouml.uml.diagram.static_structure.ui.GoClassToAssociatedClass;
import org.argouml.uml.diagram.static_structure.ui.GoClassToNavigableClass;
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
import org.argouml.uml.diagram.ui.GoFilteredChildren;
import org.argouml.uml.diagram.ui.GoGenElementToDerived;
import org.argouml.uml.diagram.ui.GoInteractionMessages;
import org.argouml.uml.diagram.ui.GoModelToBaseElements;
import org.argouml.uml.diagram.ui.GoModelToElements;
import org.argouml.uml.diagram.ui.GoNamespaceToDiagram;
import org.argouml.uml.diagram.ui.GoOperationToCollaborationDiagram;
import org.argouml.uml.diagram.ui.GoProjectToDiagram;
import org.argouml.uml.diagram.ui.GoProjectToModel;
import org.argouml.uml.diagram.use_case.ui.GoUseCaseToExtensionPoint;
import org.argouml.uml.ui.behavior.common_behavior.GoSignalToReception;
import org.argouml.uml.ui.foundation.core.GoModelElementToComment;

/**
 *
 * @author  alexb
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
        
        List rules = new ArrayList();
        rules.add(new GoNamespaceToOwnedElements());
        rules.add(new GoNamespaceToDiagram());
        rules.add(new GoClassToSummary());
        rules.add(new GoSummaryToAssociation());
        rules.add(new GoSummaryToAttribute());
        rules.add(new GoSummaryToOperation());
        rules.add(new GoSummaryToInheritance());
        rules.add(new GoSummaryToIncomingDependency());
        rules.add(new GoSummaryToOutgoingDependency());
        
        List rules2 = new ArrayList();
        rules2.add(new GoProjectToModel());
        rules2.add(new GoNamespaceToOwnedElements());
        rules2.add(new GoNamespaceToDiagram());
        rules2.add(new GoModelToCollaboration());
        rules2.add(new GoUseCaseToExtensionPoint());
        rules2.add(new GoClassifierToStructuralFeature());
        rules2.add(new GoClassifierToBeh());
        rules2.add(new GoCollaborationInteraction());
        rules2.add(new GoInteractionMessage());
        rules2.add(new GoMessageAction());
        rules2.add(new GoSignalToReception());
        rules2.add(new GoLinkStimuli());
        rules2.add(new GoStimulusToAction());
        rules2.add(new GoClassifierToCollaboration());
        rules2.add(new GoOperationToCollaboration());
        rules2.add(new GoOperationToCollaborationDiagram());
        rules2.add(new GoBehavioralFeatureToStateMachine());
        rules2.add(new GoBehavioralFeatureToStateDiagram());
        rules2.add(new GoClassifierToStateMachine());
        rules2.add(new GoMachineToState());
        rules2.add(new GoCompositeStateToSubvertex());
        rules2.add(new GoStateToInternalTrans());
        rules2.add(new GoStateMachineToTransition());
        rules2.add(new GoStateToDoActivity());
        rules2.add(new GoStateToEntry());
        rules2.add(new GoStateToExit());
        
        addPerspective(rules.toArray());
        addPerspective(rules2.toArray());
    }
}
