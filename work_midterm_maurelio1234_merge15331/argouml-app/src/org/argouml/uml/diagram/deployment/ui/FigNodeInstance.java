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

package org.argouml.uml.diagram.deployment.ui;

import java.util.ArrayList;
import java.util.Collection;

import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;

/**
 * Class to display graphics for a UML NodeInstance in a diagram.<p>
 *
 * @author 5eichler@informatik.uni-hamburg.de
 */
public class FigNodeInstance extends AbstractFigNode {

    /**
     * Main constructor - used for file loading.
     */
    public FigNodeInstance() {
        super();
        getNameFig().setUnderline(true);
    }

    /**
     * Constructor which hooks the new Fig into an existing UML element.
     *
     * @param gm ignored
     * @param node the UML element
     */
    public FigNodeInstance(GraphModel gm, Object node) {
        super(gm, node);
        getNameFig().setUnderline(true);
    }

    @Override
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_NODEINSTANCE;
    }

    @Override
    public Object clone() {
        Object clone = super.clone();
        return clone;
    }


    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        return new SelectionNodeInstance(this);
    }
    

    /*
     * @see org.tigris.gef.presentation.Fig#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    @Override
    public void setEnclosingFig(Fig encloser) {
        if (getOwner() != null) {
            Object nod = getOwner();
            if (encloser != null) {
                Object comp = encloser.getOwner();
                if (Model.getFacade().isAComponentInstance(comp)) {
                    if (Model.getFacade().getComponentInstance(nod) != comp) {
                        Model.getCommonBehaviorHelper()
                                .setComponentInstance(nod, comp);
                        super.setEnclosingFig(encloser);
                    }
                } else if (Model.getFacade().isANode(comp)) {
                    super.setEnclosingFig(encloser);
                }
            } else if (encloser == null) {
                if (isVisible()
                        // If we are not visible most likely
                        // we're being deleted.
                        // TODO: This indicates a more fundamental problem that
                        // should be investigated - tfm - 20061230
                        && Model.getFacade().getComponentInstance(nod) != null) {
                    Model.getCommonBehaviorHelper()
                            .setComponentInstance(nod, null);
                    super.setEnclosingFig(encloser);
                }
            }
        }

        if (getLayer() != null) {
            // elementOrdering(figures);
            Collection contents = new ArrayList(getLayer().getContents());
            for (Object o : contents) {
                if (o instanceof FigEdgeModelElement) {
                    FigEdgeModelElement figedge = (FigEdgeModelElement) o;
                    figedge.getLayer().bringToFront(figedge);
                }
            }
        }
    }

     /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
        super.updateListeners(oldOwner, newOwner);
        if (newOwner != null) {
            for (Object classifier 
                    : Model.getFacade().getClassifiers(newOwner)) {
                addElementListener(classifier, "name");
            }
        }
    }

}
