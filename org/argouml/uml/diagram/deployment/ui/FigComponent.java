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

package org.argouml.uml.diagram.deployment.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.*;

import org.argouml.application.api.Notation;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.util.CollectionUtil;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML Component in a diagram.
 *
 * @author 5eichler
 */
public class FigComponent extends FigNodeModelElement {
    /** The distance between the left edge of the fig and the left edge of the
     main rectangle. */
    private static final int BIGPORT_X = 10;

    private static final int OVERLAP = 4;

    private FigRect cover;
    private FigRect upperRect;
    private FigRect lowerRect;


    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor.
     */
    public FigComponent() {
        cover = new FigRect(BIGPORT_X, 10, 120, 80, Color.black, Color.white);
        upperRect = new FigRect(0, 20, 20, 10, Color.black, Color.white);
        lowerRect = new FigRect(0, 40, 20, 10, Color.black, Color.white);

        getNameFig().setLineWidth(0);
        getNameFig().setFilled(false);
        getNameFig().setText(placeString());

        addFig(getBigPort());
        addFig(cover);
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(upperRect);
        addFig(lowerRect);
    }

    // TODO: Why not just super( gm, node ) instead?? (ChL)
    /**
     * The constructor that hooks the Fig into an existing UML element
     * @param gm ignored
     * @param node the UML element
     */
    public FigComponent(GraphModel gm, Object node) {
        this();
        setOwner(node);
        if (Model.getFacade().isAClassifier(node)
                && (Model.getFacade().getName(node) != null)) {
            getNameFig().setText(Model.getFacade().getName(node));
        }
        updateBounds();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new Component";
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigComponent figClone = (FigComponent) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.cover = (FigRect) it.next();
        figClone.setStereotypeFig((FigText) it.next());
        figClone.setNameFig((FigText) it.next());
        figClone.upperRect = (FigRect) it.next();
        figClone.lowerRect = (FigRect) it.next();

        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // acessors

    /**
     * Build a collection of menu items relevant for a right-click popup menu.
     *
     * @param     me     a mouse event
     * @return           a collection of menu items
     *
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        // Modifiers ...
        popUpActions.insertElementAt(
                buildModifierPopUp(ABSTRACT | LEAF | ROOT),
                popUpActions.size() - POPUP_ADD_OFFSET);
        return popUpActions;
    }

    /**
     * @param b switch underline on or off
     */
    public void setUnderline(boolean b) {
        getNameFig().setUnderline(b);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color c) {
        //super.setLineColor(c);
        cover.setLineColor(c);
        getStereotypeFig().setFilled(false);
        getStereotypeFig().setLineWidth(0);
        getNameFig().setFilled(false);
        getNameFig().setLineWidth(0);
        upperRect.setLineColor(c);
        lowerRect.setLineColor(c);
    }


    /**
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionComponent(this);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();

        int h = stereoDim.height + nameDim.height - OVERLAP;
        int w = Math.max(stereoDim.width, nameDim.width) + BIGPORT_X;

        return new Dimension(w, h);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    public void setBounds(int x, int y, int w, int h) {

        Rectangle oldBounds = getBounds();
        getBigPort().setBounds(x + BIGPORT_X, y, w - BIGPORT_X, h);
        cover.setBounds(x + BIGPORT_X, y, w - BIGPORT_X, h);

        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();
        if (h < 50) {
            upperRect.setBounds(x, y + h / 6, 20, 10);
            lowerRect.setBounds(x, y + 3 * h / 6, 20, 10);
        }
        else {
            upperRect.setBounds(x, y + 13, 20, 10);
            lowerRect.setBounds(x, y + 39, 20, 10);
        }

        getStereotypeFig().setBounds(x + BIGPORT_X + 1,
                y + 1,
                w - BIGPORT_X - 2,
                stereoDim.height);
        getNameFig().setBounds(x + BIGPORT_X + 1,
                y + stereoDim.height - OVERLAP + 1,
                w - BIGPORT_X - 2,
                nameDim.height);
        _x = x; _y = y; _w = w; _h = h;
        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }


    ////////////////////////////////////////////////////////////////
    // user interaction methods

    /*
    public void mouseClicked(MouseEvent me) {
    super.mouseClicked(me);
    setLineColor(Color.black);
    }

    public void mousePressed(MouseEvent me) {
    super.mousePressed(me);
    Editor ce = Globals.curEditor();
    Selection sel = ce.getSelectionManager().findSelectionFor(this);
    if (sel instanceof SelectionComponent) {
    ((SelectionComponent) sel).hideButtons();
    }
    }
    */

    /**
     * @see org.tigris.gef.presentation.Fig#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    public void setEnclosingFig(Fig encloser) {

        Object comp = /*(MComponent)*/ getOwner();
        if (encloser != null
                && (Model.getFacade().isANode(encloser.getOwner())
                || Model.getFacade().isAComponent(encloser.getOwner()))
                && getOwner() != null) {
            if (Model.getFacade().isANode(encloser.getOwner())) {
                Object node = /*(MNode)*/ encloser.getOwner();
                if (!Model.getFacade()
                        .getDeploymentLocations(comp).contains(node)) {
                    Model.getCoreHelper().addDeploymentLocation(comp, node);
                }
            }
            super.setEnclosingFig(encloser);

            if (getLayer() != null) {
                // elementOrdering(figures);
                List contents = getLayer().getContents();
                Iterator it = contents.iterator();
                int contentsSize = contents.size();
                while (it.hasNext()) {
                    Object o = it.next();
                    if (o instanceof FigEdgeModelElement) {
                        FigEdgeModelElement figedge = (FigEdgeModelElement) o;
                        figedge.getLayer().bringToFront(figedge);
                    }
                }
            }
        } else {
            if (encloser == null && getEnclosingFig() != null) {
                Object encloserOwner = getEnclosingFig().getOwner();
                if (Model.getFacade().isANode(encloserOwner)
                        && (Model.getFacade().getDeploymentLocations(comp).
                                contains(encloserOwner))) {
                    Model.getCoreHelper()
                            .removeDeploymentLocation(comp, encloserOwner);
                }
                super.setEnclosingFig(encloser);

                /*if (getEnclosingFig() instanceof FigNodeModelElement)
                ((FigNodeModelElement)
                getEnclosingFig()).getEnclosedFigs().removeElement(this);
                _encloser = null;*/
            }
        }
    }

    /**
     * TODO: This is not used anywhere. Can we remove it?
     * @param figures ?
     */
    public void setNode(Vector figures) {
        int size = figures.size();
        if (figures != null && (size > 0)) {
            for (int i = 0; i < size; i++) {
                Object o = figures.elementAt(i);
                if (o instanceof FigComponent) {
                    FigComponent figcomp = (FigComponent) o;
                    figcomp.setEnclosingFig(this);
                }
            }
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    public boolean getUseTrapRect() { return true; }


    ////////////////////////////////////////////////////////////////
    // internal methods

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {
        Object me = /*(MModelElement)*/ getOwner();
        if (me == null) return;
        Object stereo = CollectionUtil.getFirstItemOrNull(
                Model.getFacade().getStereotypes(me));
        if (stereo == null
                || Model.getFacade().getName(stereo) == null
                || Model.getFacade().getName(stereo).length() == 0) {

            setStereotype("");

        } else {

            setStereotype(Notation.generateStereotype(this, stereo));

        }
    }



    /**
     * @see org.tigris.gef.presentation.Fig#getHandleBox()
     *
     * Get the rectangle on whose corners the dragging handles are to
     * be drawn.  Used by Selection Resize.
     */
    public Rectangle getHandleBox() {

        Rectangle r = getBounds();
        return new Rectangle(r.x + BIGPORT_X, r.y, r.width - BIGPORT_X,
                r.height);

    }

    /**
     * @see org.tigris.gef.presentation.Fig#setHandleBox(int, int, int, int)
     */
    public void setHandleBox(int x, int y, int w, int h) {

        setBounds(x - BIGPORT_X, y, w + BIGPORT_X, h);

    }


    static final long serialVersionUID = 1647392857462847651L;

} /* end class FigComponent */
