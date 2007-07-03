// $Id: FigComponentInstance.java 12432 2007-04-23 18:19:17Z mvw $
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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.providers.NotationProvider;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML ComponentInstance in a diagram.
 *
 * @author 5eichler
 */
public class FigComponentInstance extends FigNodeModelElement {

    /**
     * The distance between the left edge of the fig and the left edge of the
     * main rectangle.
     * Originally named BIGPORT_X (which explains what BX stands for).
     */
    private static final int BX = 10;

    private static final int OVERLAP = 0;

    private FigRect cover;
    private FigRect upperRect;
    private FigRect lowerRect;

    private NotationProvider notationProvider;

    /**
     * Constructor.
     */
    public FigComponentInstance() {
        cover = new FigRect(BX, 10, 120, 80, Color.black, Color.white);
        upperRect =
            new FigRect(0, 2 * BX, 2 * BX, BX, Color.black, Color.white);
        lowerRect =
            new FigRect(0, 4 * BX, 2 * BX, BX, Color.black, Color.white);

        getNameFig().setLineWidth(0);
        getNameFig().setFilled(false);
        getNameFig().setUnderline(true);

        addFig(getBigPort());
        addFig(cover);
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(upperRect);
        addFig(lowerRect);
    }

    /**
     * Constructor that hooks the Fig into an existing UML element.
     *
     * @param gm ignored
     * @param node the UML element
     */
    public FigComponentInstance(GraphModel gm, Object node) {
        this();
        setOwner(node);
        if (Model.getFacade().isAClassifier(node)
                && (Model.getFacade().getName(node) != null)) {
            getNameFig().setText(Model.getFacade().getName(node));
        }
        updateBounds();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#initNotationProviders(java.lang.Object)
     */
    protected void initNotationProviders(Object own) {
        super.initNotationProviders(own);
        if (Model.getFacade().isAComponentInstance(own)) {
            notationProvider =
                NotationProviderFactory2.getInstance().getNotationProvider(
                    NotationProviderFactory2.TYPE_COMPONENTINSTANCE, own);
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new ComponentInstance";
    }

    /*
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigComponentInstance figClone = (FigComponentInstance) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.cover = (FigRect) it.next();
        it.next();
        figClone.setNameFig((FigText) it.next());
        figClone.upperRect = (FigRect) it.next();
        figClone.lowerRect = (FigRect) it.next();

        return figClone;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            updateListeners(getOwner(), getOwner());
            damage();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object, java.lang.Object)
     */
    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner != null) {
            removeAllElementListeners();
        }
        if (newOwner != null) {
            // add the listeners to the newOwner
            addElementListener(newOwner);
            Collection c = Model.getFacade().getStereotypes(newOwner);
            Iterator i = c.iterator();
            while (i.hasNext()) {
                Object st = i.next();
                addElementListener(st, "name");
            }
            c = Model.getFacade().getClassifiers(newOwner);
            i = c.iterator();
            while (i.hasNext()) {
                Object st = i.next();
                addElementListener(st, "name");
            }
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color c) {
        cover.setLineColor(c);
        getStereotypeFig().setFilled(false);
        getStereotypeFig().setLineWidth(0);
        getNameFig().setFilled(false);
        getNameFig().setLineWidth(0);
        upperRect.setLineColor(c);
        lowerRect.setLineColor(c);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionComponentInstance(this);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();

        int h = Math.max(stereoDim.height + nameDim.height - OVERLAP, 4 * BX);
        int w = Math.max(stereoDim.width, nameDim.width) + 2 * BX;

        return new Dimension(w, h);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }

        Rectangle oldBounds = getBounds();
        getBigPort().setBounds(x + BX, y, w - BX, h);
        cover.setBounds(x + BX, y, w - BX, h);

        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();
        if (h < (6 * BX)) {
            upperRect.setBounds(x, y + 2 * h / 6, 20, 10);
            lowerRect.setBounds(x, y + 4 * h / 6, 20, 10);
        } else {
            upperRect.setBounds(x, y + 2 * BX, 2 * BX, BX);
            lowerRect.setBounds(x, y + 4 * BX, 2 * BX, BX);
        }

        getStereotypeFig().setBounds(x + 2 * BX + 1,
                y + 1,
                w - 2 * BX - 2,
                stereoDim.height);
        getNameFig().setBounds(x + 2 * BX + 1,
                y + stereoDim.height - OVERLAP + 1,
                w - 2 * BX - 2,
                nameDim.height);
        _x = x;
        _y = y;
        _w = w;
        _h = h;
        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        setLineColor(Color.black);
    }

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
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
                    // TODO: This indicates a more fundamental problem that should
                    // be investigated - tfm - 20061230
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
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    public boolean getUseTrapRect() {
        return true;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        if (ft == getNameFig()) {
            notationProvider.parse(getOwner(), ft.getText());
            ft.setText(notationProvider.toString(getOwner(), null));
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp(notationProvider.getParsingHelp());
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {
        getStereotypeFig().setOwner(getOwner());
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        if (isReadyToEdit()) {
            getNameFig().setText(notationProvider.toString(getOwner(), null));
        }
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getHandleBox()
     *
     * Get the rectangle on whose corners the dragging handles are to
     * be drawn.  Used by Selection Resize.
     */
    public Rectangle getHandleBox() {

        Rectangle r = getBounds();
        return new Rectangle(r.x + BX, r.y, r.width - BX,
                r.height);

    }

    /*
     * @see org.tigris.gef.presentation.Fig#setHandleBox(int, int, int, int)
     */
    public void setHandleBox(int x, int y, int w, int h) {

        setBounds(x - BX, y, w + BX, h);

    }

    /**
     * The UID.
     */
    static final long serialVersionUID = 1647392857462847651L;

} /* end class FigComponentInstance */
