// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml.diagram;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.activity.ui.FigActionState;
import org.argouml.uml.diagram.activity.ui.FigCallState;
import org.argouml.uml.diagram.activity.ui.FigObjectFlowState;
import org.argouml.uml.diagram.activity.ui.FigPartition;
import org.argouml.uml.diagram.activity.ui.FigSubactivityState;
import org.argouml.uml.diagram.collaboration.ui.FigAssociationRole;
import org.argouml.uml.diagram.collaboration.ui.FigClassifierRole;
import org.argouml.uml.diagram.deployment.ui.FigComponent;
import org.argouml.uml.diagram.deployment.ui.FigComponentInstance;
import org.argouml.uml.diagram.deployment.ui.FigMNode;
import org.argouml.uml.diagram.deployment.ui.FigMNodeInstance;
import org.argouml.uml.diagram.deployment.ui.FigObject;
import org.argouml.uml.diagram.state.ui.FigBranchState;
import org.argouml.uml.diagram.state.ui.FigCompositeState;
import org.argouml.uml.diagram.state.ui.FigDeepHistoryState;
import org.argouml.uml.diagram.state.ui.FigFinalState;
import org.argouml.uml.diagram.state.ui.FigForkState;
import org.argouml.uml.diagram.state.ui.FigInitialState;
import org.argouml.uml.diagram.state.ui.FigJoinState;
import org.argouml.uml.diagram.state.ui.FigJunctionState;
import org.argouml.uml.diagram.state.ui.FigShallowHistoryState;
import org.argouml.uml.diagram.state.ui.FigSimpleState;
import org.argouml.uml.diagram.state.ui.FigTransition;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.argouml.uml.diagram.static_structure.ui.FigInstance;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.static_structure.ui.FigLink;
import org.argouml.uml.diagram.static_structure.ui.FigModel;
import org.argouml.uml.diagram.static_structure.ui.FigPackage;
import org.argouml.uml.diagram.static_structure.ui.FigSubsystem;
import org.argouml.uml.diagram.ui.FigAssociation;
import org.argouml.uml.diagram.ui.FigAssociationClass;
import org.argouml.uml.diagram.ui.FigAssociationEnd;
import org.argouml.uml.diagram.ui.FigDependency;
import org.argouml.uml.diagram.ui.FigGeneralization;
import org.argouml.uml.diagram.ui.FigMessage;
import org.argouml.uml.diagram.ui.FigNodeAssociation;
import org.argouml.uml.diagram.ui.FigPermission;
import org.argouml.uml.diagram.ui.FigRealization;
import org.argouml.uml.diagram.ui.FigUsage;
import org.argouml.uml.diagram.use_case.ui.FigActor;
import org.argouml.uml.diagram.use_case.ui.FigExtend;
import org.argouml.uml.diagram.use_case.ui.FigInclude;
import org.argouml.uml.diagram.use_case.ui.FigUseCase;

import org.tigris.gef.graph.GraphEdgeRenderer;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * Factory methods to create Figs based an model elements with supplementary 
 * data provided by a map of name value pairs.
 *
 * <p>Provides {@link #getFigNodeFor} to implement the {@link
 *   GraphNodeRenderer} interface and {@link #getFigEdgeFor} to implement the
 *   {@link GraphEdgeRenderer} interface.</p>
 */
public abstract class UmlDiagramRenderer
    implements GraphNodeRenderer, GraphEdgeRenderer {
    private static final Logger LOG =
        Logger.getLogger(UmlDiagramRenderer.class);

    /**
     * Return a Fig that can be used to represent the given node
     *
     * @see org.tigris.gef.graph.GraphNodeRenderer#getFigNodeFor(
     * org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer,
     * java.lang.Object)
     */
    public FigNode getFigNodeFor(Object node, Map styleAttributes) {
        if (node == null) {
            throw new IllegalArgumentException("A model element must be supplied");
        }
        FigNode figNode = null;
        if (ModelFacade.isAComment(node)) {
            figNode = new FigComment();
        } else if (ModelFacade.isAClass(node)) {
             figNode = new FigClass();
        } else if (ModelFacade.isAInterface(node)) {
            figNode = new FigInterface();
        } else if (ModelFacade.isAInstance(node)) {
            figNode = new FigInstance();
        } else if (ModelFacade.isAModel(node)) {
            figNode = new FigModel();
        } else if (ModelFacade.isASubsystem(node)) {
            figNode = new FigSubsystem();
        } else if (ModelFacade.isAPackage(node)) {
            figNode = new FigPackage();
        } else if (ModelFacade.isAAssociation(node)) {
            figNode = new FigNodeAssociation();
        } else if (ModelFacade.isAActor(node)) {
            figNode = new FigActor();
        } else if (ModelFacade.isAUseCase(node)) {
            figNode = new FigUseCase();
        } else if (ModelFacade.isAPartition(node)) {
            figNode = new FigPartition();
        } else if (ModelFacade.isACallState(node)) {
            figNode = new FigCallState();
        } else if (ModelFacade.isAObjectFlowState(node)) {
            figNode = new FigObjectFlowState();
        } else if (ModelFacade.isASubactivityState(node)) {
            figNode = new FigSubactivityState();
        } else if (ModelFacade.isAClassifierRole(node)) {
            figNode = new FigClassifierRole();
        } else if (ModelFacade.isAMessage(node)) {
            figNode = new FigMessage();
        } else if (ModelFacade.isANode(node)) {
            figNode = new FigMNode();
        } else if (ModelFacade.isANodeInstance(node)) {
            figNode = new FigMNodeInstance();
        } else if (ModelFacade.isAComponent(node)) {
            figNode = new FigComponent();
        } else if (ModelFacade.isAComponentInstance(node)) {
            figNode = new FigComponentInstance();
        } else if (ModelFacade.isAObject(node)) {
            figNode = new FigObject();
        } else if (ModelFacade.isAComment(node)) {
            figNode = new FigComment();
        } else if (ModelFacade.isAActionState(node)) {
            figNode = new FigActionState();
        } else if (org.argouml.model.ModelFacade.isAFinalState(node)) {
            figNode = new FigFinalState();
        } else if (org.argouml.model.ModelFacade.isACompositeState(node)) {
            figNode = new FigCompositeState();
        } else if (org.argouml.model.ModelFacade.isAState(node)) {
            figNode = new FigSimpleState();
        } else if (ModelFacade.isAPseudostate(node)) {
            Object pState = node;
            Object kind = ModelFacade.getKind(pState);
            if (ModelFacade.INITIAL_PSEUDOSTATEKIND.equals(kind)) {
                figNode = new FigInitialState();
            } else if (ModelFacade.BRANCH_PSEUDOSTATEKIND.equals(kind)) {
                figNode = new FigBranchState();
            } else if (ModelFacade.JUNCTION_PSEUDOSTATEKIND.equals(kind)) {
                figNode = new FigJunctionState();
            } else if (ModelFacade.FORK_PSEUDOSTATEKIND.equals(kind)) {
                figNode = new FigForkState();
            } else if (ModelFacade.JOIN_PSEUDOSTATEKIND.equals(kind)) {
                figNode = new FigJoinState();
            } else if (ModelFacade.SHALLOWHISTORY_PSEUDOSTATEKIND.equals(kind)) {
                figNode = new FigShallowHistoryState();
            } else if (ModelFacade.DEEPHISTORY_PSEUDOSTATEKIND.equals(kind)) {
                figNode = new FigDeepHistoryState();
            }
        }
        
        return figNode;
    }

    /**
     * Return a Fig that can be used to represent the given edge.
     *
     * @see org.tigris.gef.graph.GraphEdgeRenderer#getFigEdgeFor(
     * org.tigris.gef.graph.GraphModel,
     * org.tigris.gef.base.Layer, java.lang.Object)
     */
    public FigEdge getFigEdgeFor(Object edge, Map styleAttributes) {
        if (edge == null) {
            throw new IllegalArgumentException("A model edge must be supplied");
        }
        FigEdge newEdge = null;
        if (ModelFacade.isAAssociationClass(edge)) {
            newEdge = new FigAssociationClass();
        } else if (ModelFacade.isAAssociationEnd(edge)) {
            newEdge = new FigAssociationEnd();
        } else if (ModelFacade.isAAssociation(edge)) {
            newEdge = new FigAssociation();
        } else if (ModelFacade.isALink(edge)) {
            newEdge = new FigLink();
        } else if (ModelFacade.isAGeneralization(edge)) {
            newEdge = new FigGeneralization();
        } else if (ModelFacade.isAPermission(edge)) {
            newEdge = new FigPermission();
        } else if (ModelFacade.isAUsage(edge)) {
            newEdge = new FigUsage();
        } else if (ModelFacade.isADependency(edge)) {
            Object stereotype = null;

            if (ModelFacade.getStereotypes(edge).size() > 0) {
                stereotype = ModelFacade.getStereotypes(edge).get(0);
            }
            if (Model.getExtensionMechanismsHelper().isStereotypeInh(
                            stereotype, "realize", "Abstraction")) {
                newEdge = new FigRealization();
            } else {
                newEdge = new FigDependency();
            }
        } else if (edge instanceof CommentEdge) {
            newEdge = new FigEdgeNote();
        } else if (ModelFacade.isAAssociationRole(edge)) {
            newEdge = new FigAssociationRole();
        } else if (ModelFacade.isATransition(edge)) {
            newEdge = new FigTransition();
        } else if (ModelFacade.isAExtend(edge)) {
            newEdge = new FigExtend();
        } else if (ModelFacade.isAInclude(edge)) {
            newEdge = new FigInclude();
        }
        
        if (newEdge == null) {
            throw new IllegalArgumentException("Failed to construct a FigEdge for " + edge);
        }
            
        return newEdge;
    }

} /* end class CollabDiagramRenderer */
