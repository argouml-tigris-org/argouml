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

// $Id$
package org.argouml.uml.diagram.deployment.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.*;

import org.argouml.application.api.Notation;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.generator.ParserDisplay;
import org.argouml.util.CollectionUtil;
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
    private static final int OVERLAP = 4;

    private FigRect cover;
    private FigRect upperRect;
    private FigRect lowerRect;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor.
     */
    public FigComponentInstance() {
        cover = new FigRect(10, 10, 120, 80, Color.black, Color.white);
        upperRect = new FigRect(0, 20, 20, 10, Color.black, Color.white);
        lowerRect = new FigRect(0, 40, 20, 10, Color.black, Color.white);

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
     * Constructor that hooks the Fig into an existing UML element
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

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new ComponentInstance";
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigComponentInstance figClone = (FigComponentInstance) super.clone();
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
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color c) {
        //     super.setLineColor(c);
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
        return new SelectionComponentInstance(this);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();

        int h = stereoDim.height + nameDim.height - OVERLAP;
        int w = Math.max(stereoDim.width, nameDim.width);
        return new Dimension(w, h);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    public void setBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }

        Rectangle oldBounds = getBounds();
        getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y, w, h);

        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();

        if (h < 50) {
            upperRect.setBounds(x - 10, y + h / 6, 20, 10);
            lowerRect.setBounds(x - 10, y + 3 * h / 6, 20, 10);
        } else {
            upperRect.setBounds(x - 10, y + 13, 20, 10);
            lowerRect.setBounds(x - 10, y + 39, 20, 10);
        }

        getStereotypeFig().setBounds(x + 1, y + 1, w - 2, stereoDim.height);
        getNameFig().setBounds(x + 1,
                y + stereoDim.height - OVERLAP + 1,
                w - 2,
                nameDim.height);
        _x = x;
        _y = y;
        _w = w;
        _h = h;
        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }

    ////////////////////////////////////////////////////////////////
    // user interaction methods

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        setLineColor(Color.black);
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent me) {
        super.mousePressed(me);
        Editor ce = Globals.curEditor();
        Selection sel = ce.getSelectionManager().findSelectionFor(this);
        if (sel instanceof SelectionComponentInstance)
            ((SelectionComponentInstance) sel).hideButtons();
    }

    /**
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
                }
                else if (Model.getFacade().isAComponentInstance(nodeOrComp)) {
                    if (Model.getFacade()
                            .getComponentInstance(comp) != nodeOrComp) {
                        Model.getCommonBehaviorHelper()
                                .setComponentInstance(comp, nodeOrComp);
                        super.setEnclosingFig(encloser);
                    }
                }
                else if (Model.getFacade().isANode(nodeOrComp)) {
                    super.setEnclosingFig(encloser);
                }
                
                if (getLayer() != null) {
                    // elementOrdering(figures);
                    List contents = getLayer().getContents();
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
            } else if (encloser == null && getEnclosingFig() != null) {
                if (Model.getFacade().getNodeInstance(comp) != null) {
                    Model.getCommonBehaviorHelper()
                            .setNodeInstance(comp, null);
                }
                if (Model.getFacade().getComponentInstance(comp) != null) {
                    Model.getCommonBehaviorHelper()
                            .setComponentInstance(comp, null);
                }
                super.setEnclosingFig(encloser);
                /*if (getEnclosingFig() instanceof FigNodeModelElement)
                ((FigNodeModelElement) getEnclosingFig())
                .getEnclosedFigs()
                .removeElement(
                this);
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
                if (o instanceof FigComponentInstance) {
                    FigComponentInstance figcomp = (FigComponentInstance) o;
                    figcomp.setEnclosingFig(this);
                }
            }
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    public boolean getUseTrapRect() {
        return true;
    }

    ////////////////////////////////////////////////////////////////
    // internal methods

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        //super.textEdited(ft);
        Object coi = /*(MComponentInstance)*/ getOwner();
        if (ft == getNameFig()) {
            String s = ft.getText().trim();
            //why this???
            //       if (s.length()>0) {
            //         s = s.substring(0, (s.length() - 1));
            //       }
            ParserDisplay.SINGLETON.parseComponentInstance(coi, s);
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {
        Object me = /*(MModelElement)*/ getOwner();
        if (me == null)
            return;
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

    static final long serialVersionUID = 1647392857462847651L;

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        Object coi = /*(MComponentInstance)*/ getOwner();
        if (coi == null)
            return;
        String nameStr = "";
        if (Model.getFacade().getName(coi) != null) {
            nameStr = Model.getFacade().getName(coi).trim();
        }

        // construct bases string (comma separated)
        String baseStr = "";
        Collection col = Model.getFacade().getClassifiers(coi);
        if (col != null && col.size() > 0) {
            Iterator it = col.iterator();
            baseStr = Model.getFacade().getName(it.next());
            while (it.hasNext()) {
                baseStr += ", " + Model.getFacade().getName(it.next());
            }
        }
        if (isReadyToEdit()) {
            if ((nameStr.length() == 0) && (baseStr.length() == 0))
                getNameFig().setText("");
            else
                getNameFig().setText(nameStr.trim() + " : " + baseStr);
        }
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

} /* end class FigComponentInstance */
