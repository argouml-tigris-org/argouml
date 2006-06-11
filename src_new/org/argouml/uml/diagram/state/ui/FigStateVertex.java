// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.state.ui;

import java.util.Iterator;

import org.argouml.model.Model;
import org.argouml.uml.diagram.activity.ui.SelectionActionState;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.LayerDiagram;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * Abstract class to with common behavior for nestable nodes in UML Statechart
 * diagrams.
 */
public abstract class FigStateVertex extends FigNodeModelElement {

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The main constructor
     */
    public FigStateVertex() {
        this.allowRemoveFromDiagram(false);
    }

    /** The constructor which hooks the Fig into the UML element
     * @param gm ignored
     * @param node the UML elm
     */
    public FigStateVertex(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    ////////////////////////////////////////////////////////////////
    // nestable nodes

    /**
     * Overriden to make it possible to include a statevertex in a composite
     * state.
     * @see org.tigris.gef.presentation.Fig#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    public void setEnclosingFig(Fig encloser) {
        super.setEnclosingFig(encloser);
        /* If this fig is not visible, do not adapt the UML model!
         * This is used for deleting. See issue 3042. */
        if  (!isVisible())
            return;
        if (!(Model.getFacade().isAStateVertex(getOwner()))) return;
        Object stateVertex = getOwner();
        Object compositeState = null;
        if (encloser != null
                && (Model.getFacade().isACompositeState(encloser.getOwner()))) {
            compositeState = encloser.getOwner();
            ((FigStateVertex) encloser).redrawEnclosedFigs();
        } else {
            compositeState = Model.getStateMachinesHelper().getTop(
                    Model.getStateMachinesHelper()
                            .getStateMachine(stateVertex));
        }
        if (compositeState != null)
            Model.getStateMachinesHelper().setContainer(stateVertex,
                    compositeState);
    }

    /**
     * Method to draw a StateVertex Fig's enclosed figs.
     */
    public void redrawEnclosedFigs() {
        Editor editor = Globals.curEditor();
        if (editor != null && !getEnclosedFigs().isEmpty()) {
            LayerDiagram lay =
                ((LayerDiagram) editor.getLayerManager().getActiveLayer());
            for (int i = 0; i < getEnclosedFigs().size(); i++) {
                Fig f = ((Fig) getEnclosedFigs().elementAt(i));
                lay.bringInFrontOf(f, this);
                if (f instanceof FigNode) {
                    FigNode fn = (FigNode) f;
                    Iterator it = fn.getFigEdges().iterator();
                    while (it.hasNext()) {
                        lay.bringInFrontOf(((FigEdge) it.next()), this);
                    }
                    if (fn instanceof FigStateVertex) {
                        ((FigStateVertex) fn).redrawEnclosedFigs();
                    }
                }
            }
        }
    }

    /**
     * return selectors, depending whether we deal with activity or state
     * diagrams.
     *
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        Object pstate = getOwner();

        if (pstate != null) {

            if (Model.getFacade().isAActivityGraph(
                    Model.getFacade().getStateMachine(
                            Model.getFacade().getContainer(pstate)))) {
                return new SelectionActionState(this);
            }
            return new SelectionState(this);
        }
        return null;
    }

} /* end class FigStateVertex */
