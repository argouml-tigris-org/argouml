// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.state.ui;

import java.util.Map;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.diagram.UmlDiagramRenderer;
import org.argouml.uml.diagram.activity.ui.FigActionState;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * This class defines a renderer object for UML Statechart Diagrams. In a
 * Statechart Diagram the following UML objects are displayed with the
 * following Figs: <p>
 * <pre>
 *  UML Object          ---  Fig
 *  ---------------------------------------
 *  State              ---  FigSimpleState
 *  CompositeState     ---  FigCompositeState
 *  ActionState        ---  FigActionState
 *  FinalState         ---  FigFinalState
 *  Pseudostate        ---  FigPseudostate
 *    Inititial        ---  FigInitialState
 *    Branch (Choice)  ---  FigBranchState
 *    Junction         ---  FigJunctionState
 *    Fork             ---  FigForkState
 *    Join             ---  FigJoinState
 *    DeepHistory      ---  FigDeepHistoryState
 *    ShallowHistory   ---  FigShallowistoryState
 *  SynchState         ---  FigSynchState
 *  Transition         ---  FigTransition
 *  more...
 *  </pre>
 *
 * @author ics125b spring 1998
 */
public class StateDiagramRenderer extends UmlDiagramRenderer {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(StateDiagramRenderer.class);

    /*
     * @see org.tigris.gef.graph.GraphNodeRenderer#getFigNodeFor(
     *      org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer,
     *      java.lang.Object, java.util.Map)
     */
    public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node,
                                 Map styleAttributes) {
        if (Model.getFacade().isAActionState(node)) {
            return new FigActionState(gm, node);
        } else if (Model.getFacade().isAFinalState(node)) {
            return new FigFinalState(gm, node);
        } else if (Model.getFacade().isAStubState(node)) {
            return new FigStubState(gm, node);
        } else if (Model.getFacade().isASubmachineState(node)) {
            return new FigSubmachineState(gm, node);
        } else if (Model.getFacade().isACompositeState(node)) {
            return new FigCompositeState(gm, node);
        } else if (Model.getFacade().isASynchState(node)) {
            return new FigSynchState(gm, node);
        } else if (Model.getFacade().isAState(node)) {
            return new FigSimpleState(gm, node);
        } else if (Model.getFacade().isAComment(node)) {
            return new FigComment(gm, node);
        } else if (Model.getFacade().isAPseudostate(node)) {
            Object pState = node;
            Object kind = Model.getFacade().getKind(pState);
            if (kind == null) {
                return null;
            }
            if (kind.equals(Model.getPseudostateKind().getInitial())) {
                return new FigInitialState(gm, node);
            } else if (kind.equals(
                    Model.getPseudostateKind().getChoice())) {
                return new FigBranchState(gm, node);
            } else if (kind.equals(
                    Model.getPseudostateKind().getJunction())) {
                return new FigJunctionState(gm, node);
            } else if (kind.equals(
                    Model.getPseudostateKind().getFork())) {
                return new FigForkState(gm, node);
            } else if (kind.equals(
                    Model.getPseudostateKind().getJoin())) {
                return new FigJoinState(gm, node);
            } else if (kind.equals(
                    Model.getPseudostateKind().getShallowHistory())) {
                return new FigShallowHistoryState(gm, node);
            } else if (kind.equals(
                    Model.getPseudostateKind().getDeepHistory())) {
                return new FigDeepHistoryState(gm, node);
            } else {
                LOG.warn("found a type not known");
            }
        }
        LOG.debug("TODO: StateDiagramRenderer getFigNodeFor");
        return null;
    }

    /*
     * @see org.tigris.gef.graph.GraphEdgeRenderer#getFigEdgeFor(
     *      org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer,
     *      java.lang.Object, java.util.Map)
     */
    public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge,
            Map styleAttributes) {
        LOG.debug("making figedge for " + edge);
        if (Model.getFacade().isATransition(edge)) {
            FigTransition trFig = new FigTransition(edge, lay);
            return trFig;
        } else if (edge instanceof CommentEdge) {
            return new FigEdgeNote(edge, lay);
        }

        LOG.debug("TODO: StateDiagramRenderer getFigEdgeFor");
        return null;
    }


    static final long serialVersionUID = 8448809085349795886L;

} /* end class StateDiagramRenderer */
