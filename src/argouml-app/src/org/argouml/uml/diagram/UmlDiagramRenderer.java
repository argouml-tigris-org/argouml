// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import java.util.Map;

import org.argouml.model.CoreFactory;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
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
import org.argouml.uml.diagram.deployment.ui.FigNodeInstance;
import org.argouml.uml.diagram.deployment.ui.FigObject;
import org.argouml.uml.diagram.state.ui.FigBranchState;
import org.argouml.uml.diagram.state.ui.FigCompositeState;
import org.argouml.uml.diagram.state.ui.FigConcurrentRegion;
import org.argouml.uml.diagram.state.ui.FigDeepHistoryState;
import org.argouml.uml.diagram.state.ui.FigFinalState;
import org.argouml.uml.diagram.state.ui.FigForkState;
import org.argouml.uml.diagram.state.ui.FigInitialState;
import org.argouml.uml.diagram.state.ui.FigJoinState;
import org.argouml.uml.diagram.state.ui.FigJunctionState;
import org.argouml.uml.diagram.state.ui.FigShallowHistoryState;
import org.argouml.uml.diagram.state.ui.FigSimpleState;
import org.argouml.uml.diagram.state.ui.FigStubState;
import org.argouml.uml.diagram.state.ui.FigSubmachineState;
import org.argouml.uml.diagram.state.ui.FigSynchState;
import org.argouml.uml.diagram.state.ui.FigTransition;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.static_structure.ui.FigDataType;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.argouml.uml.diagram.static_structure.ui.FigEnumeration;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.static_structure.ui.FigLink;
import org.argouml.uml.diagram.static_structure.ui.FigModel;
import org.argouml.uml.diagram.static_structure.ui.FigPackage;
import org.argouml.uml.diagram.static_structure.ui.FigStereotypeDeclaration;
import org.argouml.uml.diagram.static_structure.ui.FigSubsystem;
import org.argouml.uml.diagram.ui.FigAssociation;
import org.argouml.uml.diagram.ui.FigAssociationClass;
import org.argouml.uml.diagram.ui.FigAssociationEnd;
import org.argouml.uml.diagram.ui.FigClassAssociationClass;
import org.argouml.uml.diagram.ui.FigDependency;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
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
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphEdgeRenderer;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * Factory methods to create Figs based an model elements with supplementary
 * data provided by a map of name value pairs.<p>
 *
 * Provides {@link org.tigris.gef.graph.GraphNodeRenderer#getFigNodeFor(
 * Object, int, int, Map)} to implement the {@link GraphNodeRenderer}
 * interface and {@link
 * org.tigris.gef.graph.GraphEdgeRenderer#getFigEdgeFor(Object, Map)}
 * to implement the {@link GraphEdgeRenderer} interface.
 */
public abstract class UmlDiagramRenderer
    implements GraphNodeRenderer, GraphEdgeRenderer {

    /*
     * @see org.tigris.gef.graph.GraphNodeRenderer#getFigNodeFor(
     *         java.lang.Object, int, int, java.util.Map)
     */
    public FigNode getFigNodeFor(
	    Object node, int x, int y,
	    Map styleAttributes) {
        if (node == null) {
            throw new IllegalArgumentException(
                    "A model element must be supplied");
        }
        FigNode figNode = null;
        if (Model.getFacade().isAComment(node)) {
            figNode = new FigComment();
        } else if (Model.getFacade().isAStubState(node)) {
            return new FigStubState();
        } else if (Model.getFacade().isAAssociationClass(node)) {
            figNode = new FigClassAssociationClass(node, x, y, 10, 10);
        } else if (Model.getFacade().isAClass(node)) {
            figNode = new FigClass(node, x, y, 10, 10);
        } else if (Model.getFacade().isAInterface(node)) {
            figNode = new FigInterface();
        } else if (Model.getFacade().isAEnumeration(node)) {
            figNode = new FigEnumeration();
        } else if (Model.getFacade().isAStereotype(node)) {
            figNode = new FigStereotypeDeclaration();
        } else if (Model.getFacade().isADataType(node)) {
            figNode = new FigDataType();
        } else if (Model.getFacade().isAModel(node)) {
            figNode = new FigModel(node, x, y);
        } else if (Model.getFacade().isASubsystem(node)) {
            figNode = new FigSubsystem(node, x, y);
        } else if (Model.getFacade().isAPackage(node)) {
            figNode = new FigPackage(node, x, y);
        } else if (Model.getFacade().isAAssociation(node)) {
            figNode = new FigNodeAssociation();
        } else if (Model.getFacade().isAActor(node)) {
            figNode = new FigActor();
        } else if (Model.getFacade().isAUseCase(node)) {
            figNode = new FigUseCase();
        } else if (Model.getFacade().isAPartition(node)) {
            figNode = new FigPartition();
        } else if (Model.getFacade().isACallState(node)) {
            figNode = new FigCallState();
        } else if (Model.getFacade().isAObjectFlowState(node)) {
            figNode = new FigObjectFlowState();
        } else if (Model.getFacade().isASubactivityState(node)) {
            figNode = new FigSubactivityState();
        } else if (Model.getFacade().isAClassifierRole(node)) {
            figNode = new FigClassifierRole();
        } else if (Model.getFacade().isAMessage(node)) {
            figNode = new FigMessage();
        } else if (Model.getFacade().isANode(node)) {
            figNode = new FigMNode();
        } else if (Model.getFacade().isANodeInstance(node)) {
            figNode = new FigNodeInstance();
        } else if (Model.getFacade().isAComponent(node)) {
            figNode = new FigComponent();
        } else if (Model.getFacade().isAComponentInstance(node)) {
            figNode = new FigComponentInstance();
        } else if (Model.getFacade().isAObject(node)) {
            figNode = new FigObject();
        } else if (Model.getFacade().isAComment(node)) {
            figNode = new FigComment();
        } else if (Model.getFacade().isAActionState(node)) {
            figNode = new FigActionState();
        } else if (Model.getFacade().isAFinalState(node)) {
            figNode = new FigFinalState();
        } else if (Model.getFacade().isASubmachineState(node)) {
            figNode = new FigSubmachineState();
        } else if (Model.getFacade().isAConcurrentRegion(node)) {
            figNode = new FigConcurrentRegion();
        } else if (Model.getFacade().isASynchState(node)) {
            figNode = new FigSynchState();
        } else if (Model.getFacade().isACompositeState(node)) {
            figNode = new FigCompositeState();
        } else if (Model.getFacade().isAState(node)) {
            figNode = new FigSimpleState();
        } else if (Model.getFacade().isAPseudostate(node)) {
            Object pState = node;
            Object kind = Model.getFacade().getKind(pState);
            if (Model.getPseudostateKind().getInitial().equals(kind)) {
                figNode = new FigInitialState();
            } else if (Model.getPseudostateKind().getChoice()
                    .equals(kind)) {
                figNode = new FigBranchState();
            } else if (Model.getPseudostateKind().getJunction()
                    .equals(kind)) {
                figNode = new FigJunctionState();
            } else if (Model.getPseudostateKind().getFork().equals(kind)) {
                figNode = new FigForkState();
            } else if (Model.getPseudostateKind().getJoin().equals(kind)) {
                figNode = new FigJoinState();
            } else if (Model.getPseudostateKind().getShallowHistory()
                    .equals(kind)) {
                figNode = new FigShallowHistoryState();
            } else if (Model.getPseudostateKind().getDeepHistory()
                    .equals(kind)) {
                figNode = new FigDeepHistoryState();
            }
        }

        if (figNode == null) {
            throw new IllegalArgumentException(
                    "Failed to construct a FigNode for " + node);
        }
        setStyleAttributes(figNode, styleAttributes);

        return figNode;
    }

    /**
     * Set the fig style according to attributes.
     *
     * @param fig the fig to style.
     * @param attributeMap a map of name value pairs
     */
    private void setStyleAttributes(Fig fig, Map<String, String> attributeMap) {
        String name;
        String value;
        for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
            name = entry.getKey();
            value = entry.getValue();

            if ("operationsVisible".equals(name)) {
                ((OperationsCompartmentContainer) fig)
                    .setOperationsVisible(value.equalsIgnoreCase("true"));
            } else if ("attributesVisible".equals(name)) {
                ((AttributesCompartmentContainer) fig)
                    .setAttributesVisible(value.equalsIgnoreCase("true"));
            }
        }
    }


    /*
     * @see org.tigris.gef.graph.GraphEdgeRenderer#getFigEdgeFor(java.lang.Object, java.util.Map)
     */
    public FigEdge getFigEdgeFor(Object edge, Map styleAttributes) {
        if (edge == null) {
            throw new IllegalArgumentException("A model edge must be supplied");
        }
        FigEdge newEdge = null;
        if (Model.getFacade().isAAssociationClass(edge)) {
            newEdge = new FigAssociationClass();
        } else if (Model.getFacade().isAAssociationEnd(edge)) {
            newEdge = new FigAssociationEnd();
        } else if (Model.getFacade().isAAssociation(edge)) {
            newEdge = new FigAssociation();
        } else if (Model.getFacade().isALink(edge)) {
            newEdge = new FigLink();
        } else if (Model.getFacade().isAGeneralization(edge)) {
            newEdge = new FigGeneralization();
        } else if (Model.getFacade().isAPackageImport(edge)) {
            newEdge = new FigPermission();
        } else if (Model.getFacade().isAUsage(edge)) {
            newEdge = new FigUsage();
        } else if (Model.getFacade().isADependency(edge)) {
            if (Model.getExtensionMechanismsHelper().hasStereotype(edge, 
                    CoreFactory.REALIZE_STEREOTYPE)) {
                newEdge = new FigRealization();
            } else {
                newEdge = new FigDependency();
            }
        } else if (edge instanceof CommentEdge) {
            newEdge = new FigEdgeNote();
        } else if (Model.getFacade().isAAssociationRole(edge)) {
            newEdge = new FigAssociationRole();
        } else if (Model.getFacade().isATransition(edge)) {
            newEdge = new FigTransition();
        } else if (Model.getFacade().isAExtend(edge)) {
            newEdge = new FigExtend();
        } else if (Model.getFacade().isAInclude(edge)) {
            newEdge = new FigInclude();
        }

        if (newEdge == null) {
            throw new IllegalArgumentException(
                    "Failed to construct a FigEdge for " + edge);
        }

        return newEdge;
    }

    /**
     * Find the Figs in the given layer that should be the source and
     * destination and attach these to either end of the FigEdge
     * @param layer the layer to look for the FigNodes
     * @param newEdge The edge to attach
     */
    protected final void setPorts(Layer layer, FigEdge newEdge) {
        Object modelElement = newEdge.getOwner();
        if (newEdge.getSourcePortFig() == null) {
            Object source;
            if (modelElement instanceof CommentEdge) {
                source = ((CommentEdge) modelElement).getSource();
            } else {
                source = Model.getUmlHelper().getSource(modelElement);
            }
            FigNode sourceNode = getNodePresentationFor(layer, source);
            assert (sourceNode != null) : "No FigNode found for " + source;
            setSourcePort(newEdge, sourceNode);
        }

        if (newEdge.getDestPortFig() == null) {
            Object dest;
            if (modelElement instanceof CommentEdge) {
                dest = ((CommentEdge) modelElement).getDestination();
            } else {
                dest = Model.getUmlHelper().getDestination(newEdge.getOwner());
            }
            setDestPort(newEdge, getNodePresentationFor(layer, dest));
        }
        
        if (newEdge.getSourcePortFig() == null
                || newEdge.getDestPortFig() == null) {
            throw new IllegalStateException("Edge of type "
                + newEdge.getClass().getName()
                + " created with no source or destination port");
        }
    }

    private void setSourcePort(FigEdge edge, FigNode source) {
        edge.setSourcePortFig(source);
        edge.setSourceFigNode(source);
    }

    private void setDestPort(FigEdge edge, FigNode dest) {
        edge.setDestPortFig(dest);
        edge.setDestFigNode(dest);
    }
    
    /**
     * Get the FigNode from the given layer that represents the given
     * model element.
     * The FigNode portion of an association class is returned in preference
     * to the FigEdge portion.
     * If no FigNode is found then a FIgEdge is searched for and the FigNode
     * that acts as its edge port is returned.
     * @param lay the layer containing the Fig
     * @param modelElement the model element to find presentation for
     * @return the FigNode presentation of the model element
     */
    private FigNode getNodePresentationFor(Layer lay, Object modelElement) {
        assert modelElement != null : "A modelElement must be supplied";
        for (Object fig : lay.getContentsNoEdges()) {
 
            if (fig instanceof FigNode
                    && ((FigNode) fig).getOwner().equals(modelElement)) {
                return ((FigNode) fig);
            }
        }
        for (Object fig : lay.getContentsEdgesOnly()) {
            if (fig instanceof FigEdgeModelElement
                    && modelElement.equals(((FigEdgeModelElement) fig)
                            .getOwner())) {
                return ((FigEdgeModelElement) fig).getEdgePort();
            }
        }
        return null;
    }
    

}
