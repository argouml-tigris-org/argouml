// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram.deployment.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Geometry;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCube;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

public abstract class AbstractFigNode extends FigNodeModelElement {

    /**
     * Offset in x & y for depth perspective lines of cube.
     * TODO: This is the same value as the member 'D'in 
     * {@link org.tigris.gef.presentation.FigCube}, but there is
     * nothing enforcing that correspondence.  Things will probably
     * break if they don't match.
     */
    protected static final int DEPTH = 20;
    protected FigCube cover;
    protected int x = 10;
    protected int y = 10;
    protected int width = 200;
    protected int height = 180;

    public AbstractFigNode() {
        super();
        setBigPort(new CubePortFigRect(x, y - DEPTH, width + DEPTH, height
                + DEPTH, DEPTH));
        getBigPort().setFilled(false);
        getBigPort().setLineWidth(0);
        cover = new FigCube(x, y, width, height, Color.black, Color.white);

        getNameFig().setLineWidth(0);
        getNameFig().setFilled(false);
        getNameFig().setJustification(0);

        addFig(getBigPort());
        addFig(cover);
        addFig(getStereotypeFig());
        addFig(getNameFig());
    }
    
    public AbstractFigNode(GraphModel gm, Object node) {
        this();
        setOwner(node);
        if (Model.getFacade().isAClassifier(node)
                && (Model.getFacade().getName(node) != null)) {
            getNameFig().setText(Model.getFacade().getName(node));
        }
    }

    @Override
    public Object clone() {
        FigMNode figClone = (FigMNode) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.cover = (FigCube) it.next();
        it.next();
        figClone.setNameFig((FigText) it.next());
        return figClone;
    }

    @Override
    public void setLineColor(Color c) {
        cover.setLineColor(c);
    }

    @Override
    public void setLineWidth(int w) {
        cover.setLineWidth(w);
    }


    @Override
    public boolean isFilled() {
        return cover.isFilled();
    }

    @Override
    public void setFilled(boolean f) {
        cover.setFilled(f);
    }

    @Override
    public Selection makeSelection() {
        return new SelectionNode(this);
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();
    
        int w = Math.max(stereoDim.width, nameDim.width + 1) + DEPTH;
        int h = stereoDim.height + nameDim.height + DEPTH;
        
        w = Math.max(3 * DEPTH, w); // so it still looks like a cube
        h = Math.max(3 * DEPTH, h);
        return new Dimension(w, h);
    }

    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();
        getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y + DEPTH, w - DEPTH, h - DEPTH);

        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();
        getNameFig().setBounds(
                x + 4, y + DEPTH + stereoDim.height + 1,
                w - DEPTH - 8, nameDim.height);
        getStereotypeFig().setBounds(x + 1, y + DEPTH + 1,
                w - DEPTH - 2, stereoDim.height);
        _x = x;
        _y = y;
        _w = w;
        _h = h;
        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }

    @Override
    protected void updateStereotypeText() {
        getStereotypeFig().setOwner(getOwner());
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        setLineColor(Color.black);
    }

    @Override
    public void setEnclosingFig(Fig encloser) {
        if (encloser == null
                || (encloser != null
                && Model.getFacade().isANode(encloser.getOwner()))) {
            super.setEnclosingFig(encloser);
        }
    
        if (getLayer() != null) {
            // elementOrdering(figures);
            Collection contents = getLayer().getContents();
            Collection bringToFrontList = new ArrayList();
            Iterator it = contents.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof FigEdgeModelElement) {
                    bringToFrontList.add(o);
    
                }
            }
            Iterator bringToFrontIter = bringToFrontList.iterator();
            while (bringToFrontIter.hasNext()) {
                FigEdgeModelElement figEdge =
                        (FigEdgeModelElement) bringToFrontIter.next();
                figEdge.getLayer().bringToFront(figEdge);
            }
        }
    }

    @Override
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp("parsing.help.fig-node");
        }
    }

    @Override
    public boolean getUseTrapRect() {
        return true;
    }

    @Override
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            updateListeners(getOwner(), getOwner());
            damage();
        }
    }

    @Override
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
        }
    }

    @Override
    public Point getClosestPoint(Point anotherPt) {
        Rectangle r = getBounds();
        int[] xs = {
            r.x,
            r.x + DEPTH,
            r.x + r.width,
            r.x + r.width,
            r.x + r.width - DEPTH,
            r.x,
            r.x,
        };
        int[] ys = {
            r.y + DEPTH,
            r.y,
            r.y,
            r.y + r.height - DEPTH,
            r.y + r.height,
            r.y + r.height,
            r.y + DEPTH,
        };
        Point p = Geometry.ptClosestTo(xs, ys, 7, anotherPt);
        return p;
    }

    public AbstractFigNode(Object element, int x, int y) {
        super(element, x, y);
    }

}