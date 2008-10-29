// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.UmlDiagramRenderer;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.argouml.uml.diagram.ui.UMLDiagram;
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

        FigNode figNode = null;
        ArgoDiagram diag = ProjectManager.getManager().getCurrentProject()
            .getActiveDiagram();
        if (diag instanceof UMLDiagram
                && ((UMLDiagram) diag).doesAccept(node)) {
            figNode = ((UMLDiagram) diag).drop(node, null);
        } else {
            LOG.debug("TODO: StateDiagramRenderer getFigNodeFor");
            return null;
        }
        
        lay.add(figNode);
        return figNode;
    }

    /*
     * @see org.tigris.gef.graph.GraphEdgeRenderer#getFigEdgeFor(
     *      org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer,
     *      java.lang.Object, java.util.Map)
     */
    public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge,
            Map styleAttributes) {
        FigEdge figEdge = null;

        if (Model.getFacade().isATransition(edge)) {
            figEdge = new FigTransition(edge, lay);
        } else if (edge instanceof CommentEdge) {
            figEdge = new FigEdgeNote(edge, lay);
        } else {
            LOG.debug("TODO: StateDiagramRenderer getFigEdgeFor");
            return null;
        }
        
        lay.add(figEdge);
        return figEdge;
    }


    static final long serialVersionUID = 8448809085349795886L;

} /* end class StateDiagramRenderer */
