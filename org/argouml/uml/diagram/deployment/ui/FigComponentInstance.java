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

package org.argouml.uml.diagram.deployment.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML ComponentInstance in a diagram.
 *
 * @author 5eichler
 */
public class FigComponentInstance extends AbstractFigComponent {

    private NotationProvider notationProvider;

    /**
     * Construct a default ComponentInstance figure.
     */
    public FigComponentInstance() {
        super();
        getNameFig().setUnderline(true);
    }

    /**
     * Constructor that hooks the Fig into an existing UML element.
     *
     * @param gm ignored
     * @param node the UML element
     */
    public FigComponentInstance(GraphModel gm, Object node) {
        super(gm, node);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#initNotationProviders(java.lang.Object)
     */
    @Override
    protected void initNotationProviders(Object own) {
        super.initNotationProviders(own);
        if (Model.getFacade().isAComponentInstance(own)) {
            notationProvider =
                NotationProviderFactory2.getInstance().getNotationProvider(
                    NotationProviderFactory2.TYPE_COMPONENTINSTANCE, own);
        }
    }

    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        FigComponentInstance figClone = (FigComponentInstance) super.clone();
        // nothing extra to do currently
        return figClone;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object, java.lang.Object)
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

   
    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        return new SelectionComponentInstance(this);
    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        // TODO: What is this needed for? - tfm
        setLineColor(Color.black);
    }

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent me) {
        super.mousePressed(me);
        Editor ce = Globals.curEditor();
        Selection sel = ce.getSelectionManager().findSelectionFor(this);
        if (sel instanceof SelectionComponentInstance) {
            ((SelectionComponentInstance) sel).hideButtons();
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    @Override
    public void setEnclosingFig(Fig encloser) {

        if (getOwner() != null) {
            Object comp = getOwner();
            if (encloser != null) {
                Object nodeOrComp = encloser.getOwner();
                if (Model.getFacade().isANodeInstance(nodeOrComp)) {
                    if (Model.getFacade()
                            .getNodeInstance(comp) != nodeOrComp) {
                        Model.getCommonBehaviorHelper()
                                .setNodeInstance(comp, nodeOrComp);
                        super.setEnclosingFig(encloser);
                    }
                } else if (Model.getFacade().isAComponentInstance(nodeOrComp)) {
                    if (Model.getFacade()
                            .getComponentInstance(comp) != nodeOrComp) {
                        Model.getCommonBehaviorHelper()
                                .setComponentInstance(comp, nodeOrComp);
                        super.setEnclosingFig(encloser);
                    }
                } else if (Model.getFacade().isANode(nodeOrComp)) {
                    super.setEnclosingFig(encloser);
                }

                if (getLayer() != null) {
                    // elementOrdering(figures);
                    List contents = new ArrayList(getLayer().getContents());
                    Iterator it = contents.iterator();
                    while (it.hasNext()) {
                        Object o = it.next();
                        if (o instanceof FigEdgeModelElement) {
                            FigEdgeModelElement figedge =
                                    (FigEdgeModelElement) o;
                            figedge.getLayer().bringToFront(figedge);
                        }
                    }
                }
            } else if (isVisible()
                    // If we are not visible most likely we're being deleted.
                    // TODO: This indicates a more fundamental problem that 
                    // should be investigated - tfm - 20061230
                    && encloser == null && getEnclosingFig() != null) {
                if (Model.getFacade().getNodeInstance(comp) != null) {
                    Model.getCommonBehaviorHelper()
                            .setNodeInstance(comp, null);
                }
                if (Model.getFacade().getComponentInstance(comp) != null) {
                    Model.getCommonBehaviorHelper()
                            .setComponentInstance(comp, null);
                }
                super.setEnclosingFig(encloser);
            }
        }
    }


    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEdited(FigText ft) throws PropertyVetoException {
        if (ft == getNameFig()) {
            notationProvider.parse(getOwner(), ft.getText());
            ft.setText(notationProvider.toString(getOwner(), null));
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp(notationProvider.getParsingHelp());
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    @Override
    protected void updateStereotypeText() {
        getStereotypeFig().setOwner(getOwner());
    }
    
    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    @Override
    protected void updateNameText() {
        if (isReadyToEdit()) {
            getNameFig().setText(notationProvider.toString(getOwner(), null));
        }
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

}
