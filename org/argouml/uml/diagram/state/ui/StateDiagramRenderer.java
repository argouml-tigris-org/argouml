


// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: StateDiagramRenderer.java
// Classes: StateDiagramRenderer
// Original Author: ics125b spring 1998
// $Id$

package org.argouml.uml.diagram.state.ui;

import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.activity.ui.FigActionState;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphEdgeRenderer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;



import ru.novosoft.uml.behavior.state_machines.MPseudostate;

import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;
// could be singleton


/** This class defines a renderer object for UML State Diagrams. In a
 *  State Diagram the following UML objects are displayed with the
 *  following Figs: <p>
 * <pre>
 *  UML Object          ---  Fig
 *  ---------------------------------------
 *  MState              ---  FigSimpleState
 *  MCompositeState     ---  FigCompositeState
 *  MActionState        ---  FigActionState
 *  MFinalState         ---  FigFinalState
 *  MPseudostate        ---  FigPseudostate
 *    Inititial         ---  FigInitialState
 *    Branch            ---  FigBranchState
 *    Fork              ---  FigForkState
 *    Join              ---  FigJoinState
 *    DeepHistory       ---  FigDeepHistoryState
 *    ShallowHistory    ---  FigShallowistoryState
 *  MTransition         ---  FigTransition
 *  more...
 *  </pre>
 */

public class StateDiagramRenderer
    implements GraphNodeRenderer, GraphEdgeRenderer
{
    protected static Logger cat = 
        Logger.getLogger(StateDiagramRenderer.class);

    /** Return a Fig that can be used to represent the given node */
    public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node) {
        if (ModelFacade.isAActionState(node)) {
            return new FigActionState(gm, node);
        }
        else if (org.argouml.model.ModelFacade.isAFinalState(node)) {
            return new FigFinalState(gm, node);
        }
        else if (org.argouml.model.ModelFacade.isACompositeState(node)) {
            return new FigCompositeState(gm, node);
        }
        else if (org.argouml.model.ModelFacade.isAState(node)) {
            return new FigSimpleState(gm, node);
        }
        else if (org.argouml.model.ModelFacade.isAPseudostate(node)) {
            MPseudostate pState = (MPseudostate) node;
            if (pState.getKind() == null) {
                return null;
            }
            if (pState.getKind().equals(MPseudostateKind.INITIAL)) {
                return new FigInitialState(gm, node);
            }
            else if (pState.getKind().equals(MPseudostateKind.BRANCH)) {
                return new FigBranchState(gm, node);
            }
            else if (pState.getKind().equals(MPseudostateKind.FORK)) {
                return new FigForkState(gm, node);
            }
            else if (pState.getKind().equals(MPseudostateKind.JOIN)) {
                return new FigJoinState(gm, node);
            }
            else if (pState.getKind().equals(MPseudostateKind.SHALLOW_HISTORY))
	    {
                return new FigShallowHistoryState(gm, node);
            }
            else if (pState.getKind().equals(MPseudostateKind.DEEP_HISTORY)) {
                return new FigDeepHistoryState(gm, node);     
            }
            else {
                cat.warn("found a type not known");
            }
        }
        cat.debug("TODO StateDiagramRenderer getFigNodeFor");
        return null;
    }

    /** Return a Fig that can be used to represent the given edge */
    public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge) {
	cat.debug("making figedge for " + edge);
	if (org.argouml.model.ModelFacade.isATransition(edge)) {
	    MTransition tr = (MTransition) edge;
	    FigTransition trFig = new FigTransition(tr, lay);
	    return trFig;
	}

	cat.debug("TODO StateDiagramRenderer getFigEdgeFor");
	return null;
    }


    static final long serialVersionUID = 8448809085349795886L;

} /* end class StateDiagramRenderer */