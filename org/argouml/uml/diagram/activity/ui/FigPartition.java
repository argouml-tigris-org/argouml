// $Id$
// Copyright (c) 2003-2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram.activity.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import org.tigris.gef.presentation.Handle;

/**
 * This class represents a Partition or Swimlane for Activity diagrams.
 *
 * @author mkl
 */
public class FigPartition extends FigNodeModelElement {

    private FigLine leftLine;
    private FigLine rightLine;
    private FigLine topLine;
    private FigLine bottomLine;
    private boolean placed;

    private static final int PADDING = 8;

    /**
     * Constructor.
     */
    public FigPartition() {
        setBigPort(new FigRect(10, 10, 160, 200, Color.cyan, Color.cyan));
        getBigPort().setFilled(false);
        getBigPort().setLineWidth(0);
        leftLine = new FigLine(10, 10, 10, 300);
        rightLine = new FigLine(150, 10, 160, 300);
        bottomLine = new FigLine(10, 300, 150, 300);
        topLine = new FigLine(10, 10, 150, 10);

        getNameFig().setLineWidth(1);
        getNameFig().setBounds(10 + PADDING, 10, 50 - PADDING * 2, 25);
        getNameFig().setFilled(false);

        addFig(getBigPort());
        addFig(rightLine);
        addFig(leftLine);
        addFig(topLine);
        addFig(bottomLine);
        addFig(getNameFig());

        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * Constructor which hooks the Fig into an existing UML element.
     *
     * @param gm ignored
     * @param node the UML element
     */
    public FigPartition(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /*
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigPartition figClone = (FigPartition) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.rightLine = (FigLine) it.next();
        figClone.leftLine = (FigLine) it.next();
        figClone.bottomLine = (FigLine) it.next();
        figClone.topLine = (FigLine) it.next();
        figClone.setNameFig((FigText) it.next());
        return figClone;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    public void setEnclosingFig(Fig newEncloser) {
        super.setEnclosingFig(newEncloser);
        LayerPerspective layer = (LayerPerspective) getLayer();
        // If the layer is null, then most likely we are being deleted.
        if (newEncloser == null && layer != null) {
            UMLActivityDiagram diagram = 
                (UMLActivityDiagram) getProject().getActiveDiagram();
            Object machine = diagram.getStateMachine();
            Model.getCoreHelper().setModelElementContainer(
                    getOwner(), machine);
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        rightLine.setLineColor(col);
        leftLine.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return rightLine.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        getBigPort().setFillColor(col);
        getNameFig().setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return getBigPort().getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {
        getBigPort().setFilled(f);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return getBigPort().getFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        rightLine.setLineWidth(w);
        leftLine.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return rightLine.getLineWidth();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new Swimlane";
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension nameDim = getNameFig().getMinimumSize();
        int w = nameDim.width + PADDING * 2;
        int h = nameDim.height;

        // we want to maintain a minimum size for the partition
        w = Math.max(64, w);
        h = Math.max(256, h);

        return new Dimension(w, h);
    }

    /**
     * Using a traprect enables us to move containing figs easily.
     *
     * @return <code>true</code>
     *
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    public boolean getUseTrapRect() {
        return true;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setBoundsImpl(int x, int y, int w, int h) {
	
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();

        Rectangle nameBounds = getNameFig().getBounds();
        getNameFig().setBounds(x, y, w, nameBounds.height);

        getBigPort().setBounds(x, y, w, h);
        leftLine.setBounds(x, y, 0, h);
        rightLine.setBounds(x + (w - 1) , y, 0, h);
        topLine.setBounds(x, y, w, 0);
        bottomLine.setBounds(x, y + h, w, 0);

        firePropChange("bounds", oldBounds, getBounds());
        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
    }
    
    public Selection makeSelection() {
	return new SelectionPartition(this);
    }
    
    /**
     * TODO: Create an abstract postPlacement method in Fig and have ModePlace
     * call. This method can then be removed.
     * @param layer The Layer containing this Fig
     */
    public void setLayer(Layer layer) {
	super.setLayer(layer);
	if (!placed && layer != null) {
	    placed = true;
	    postPlacement();
	}
    }
    
    /**
     * On post placement look to see if there are any other
     * FigPartitions. If so place to the right and resize height.
     */
    public void postPlacement() {
	List partitions = getPartitions();
	
	if (partitions.size() > 1) {
            int x = 0;
            int y = 0;
            int height = 0;
	    Iterator it = partitions.iterator();
            Fig f = null;
	    while (it.hasNext()) {
		f = (Fig) it.next();
		if (f != this && f.getX() + f.getWidth() > x) {
		    x = f.getX() + f.getWidth();
		    y = f.getY();
		    height = f.getHeight();
		}
	    }
	    setBounds(x, y, getWidth(), height);
	}
    }
    
    /**
     * Get all the partitions on the same layer as this FigPartition
     * @return th partitions
     */
    private List getPartitions() {
        final List partitions = new ArrayList();
        
        if (getLayer() != null) {
            Iterator it = getLayer().getContents().iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof FigPartition) {
                    partitions.add(o);
                }
            }
        }
        return partitions;
    }
    
    
    /**
     * A specialist Selection class for FigPartitions.
     * This ensures that all swimlanes are the same length (ie height).
     * TODO: Make sure that all swimlanes appear side by side (UML spec
     * states "separated from neighboring swimlanes by vertical solid
     * lines on both sides".
     * TODO: Allow drag of the west and east edge to resize both the selected
     * Fig and the fig connected to that side.
     * TODO: Show NorthWest and SouthWest handle only on leftmost swimlane.
     * TODO: Show NorthEast and SouthEast handle only on rightmost swimlane.
     * @author Bob
     */
    private class SelectionPartition extends Selection {

        private int cx;
        private int cy;
        private int cw;
        private int ch;
        
        /**
         * Construct a new SelectionPartition for the given partition
         */
        public SelectionPartition(FigPartition f) {
            super(f);
        }
        
        /** Return a handle ID for the handle under the mouse, or -1 if
         *  none. Needs-More-Work: in the future, return a Handle instance or
         *  null. <p>
         *  <pre>
         *   0-------1-------2
         *   |               |
         *   3               4
         *   |               |
         *   5-------6-------7
         * </pre>
         */
        public void hitHandle(Rectangle r, Handle h) {
            if (getContent().isResizable()) {
        
                updateHandleBox();
                Rectangle testRect = new Rectangle(0, 0, 0, 0);
                testRect.setBounds(
                    cx - HAND_SIZE / 2,
                    cy - HAND_SIZE / 2,
                    HAND_SIZE,
                    ch + HAND_SIZE / 2);
                boolean leftEdge = r.intersects(testRect);
                testRect.setBounds(
                    cx + cw - HAND_SIZE / 2,
                    cy - HAND_SIZE / 2,
                    HAND_SIZE,
                    ch + HAND_SIZE / 2);
                boolean rightEdge = r.intersects(testRect);
                testRect.setBounds(
                    cx - HAND_SIZE / 2,
                    cy - HAND_SIZE / 2,
                    cw + HAND_SIZE / 2,
                    HAND_SIZE);
                boolean topEdge = r.intersects(testRect);
                testRect.setBounds(
                    cx - HAND_SIZE / 2,
                    cy + ch - HAND_SIZE / 2,
                    cw + HAND_SIZE / 2,
                    HAND_SIZE);
                boolean bottomEdge = r.intersects(testRect);
                // TODO: midpoints for side handles
                if (leftEdge && topEdge) {
                    h.index = Handle.NORTHWEST;
                    h.instructions = "Resize top left";
                } else if (rightEdge && topEdge) {
                    h.index = Handle.NORTHEAST;
                    h.instructions = "Resize top right";
                } else if (leftEdge && bottomEdge) {
                    h.index = Handle.SOUTHWEST;
                    h.instructions = "Resize bottom left";
                } else if (rightEdge && bottomEdge) {
                    h.index = Handle.SOUTHEAST;
                    h.instructions = "Resize bottom right";
                }
                // TODO: side handles
                else {
                    h.index = -1;
                    h.instructions = "Move object(s)";
                }
            } else {
                h.index = -1;
                h.instructions = "Move object(s)";
            }
        
        }
        
        /** Update the private variables cx etc. that represent the rectangle on
        	  whose corners handles are to be drawn.*/
        private void updateHandleBox() {
            final Rectangle cRect = getContent().getHandleBox();
            cx = cRect.x;
            cy = cRect.y;
            cw = cRect.width;
            ch = cRect.height;
        }
        
        /** Paint the handles at the four corners and midway along each edge
         * of the bounding box.  */
        public void paint(Graphics g) {
            final Fig fig = getContent();
            if (getContent().isResizable()) {
        
                updateHandleBox();
                g.setColor(Globals.getPrefs().handleColorFor(fig));
                g.fillRect(
                    cx - HAND_SIZE / 2,
                    cy - HAND_SIZE / 2,
                    HAND_SIZE,
                    HAND_SIZE);
                g.fillRect(
                    cx + cw - HAND_SIZE / 2,
                    cy - HAND_SIZE / 2,
                    HAND_SIZE,
                    HAND_SIZE);
                g.fillRect(
                    cx - HAND_SIZE / 2,
                    cy + ch - HAND_SIZE / 2,
                    HAND_SIZE,
                    HAND_SIZE);
                g.fillRect(
                    cx + cw - HAND_SIZE / 2,
                    cy + ch - HAND_SIZE / 2,
                    HAND_SIZE,
                    HAND_SIZE);
            } else {
                final int x = fig.getX();
                final int y = fig.getY();
                final int w = fig.getWidth();
                final int h = fig.getHeight();
                g.setColor(Globals.getPrefs().handleColorFor(fig));
                g.drawRect(
                    x - BORDER_WIDTH,
                    y - BORDER_WIDTH,
                    w + BORDER_WIDTH * 2 - 1,
                    h + BORDER_WIDTH * 2 - 1);
                g.drawRect(
                    x - BORDER_WIDTH - 1,
                    y - BORDER_WIDTH - 1,
                    w + BORDER_WIDTH * 2 + 2 - 1,
                    h + BORDER_WIDTH * 2 + 2 - 1);
                g.fillRect(x - HAND_SIZE, y - HAND_SIZE, HAND_SIZE, HAND_SIZE);
                g.fillRect(x + w, y - HAND_SIZE, HAND_SIZE, HAND_SIZE);
                g.fillRect(x - HAND_SIZE, y + h, HAND_SIZE, HAND_SIZE);
                g.fillRect(x + w, y + h, HAND_SIZE, HAND_SIZE);
            }
        }
        
        /**
         * Change some attribute of the selected Fig when the user drags one
         * of its handles. Needs-More-Work: someday I might implement resizing
         * that maintains the aspect ratio.
         */
        public void dragHandle(int mX, int mY, int anX, int anY, Handle hand) {
            
            final Fig fig = getContent();
            final List partitions = getPartitions();

            updateHandleBox();
        
            final int x = cx;
            final int y = cy;
            final int w = cw;
            final int h = ch;
            int newX = x, newY = y, newWidth = w, newHeight = h;
            Dimension minSize = fig.getMinimumSize();
            int minWidth = minSize.width, minHeight = minSize.height;
            switch (hand.index) {
            case -1 :
                fig.translate(anX - mX, anY - mY);
                return;
            case Handle.NORTHWEST :
                newWidth = x + w - mX;
                newWidth = (newWidth < minWidth) ? minWidth : newWidth;
                newHeight = y + h - mY;
                newHeight = (newHeight < minHeight) ? minHeight : newHeight;
                newX = x + w - newWidth;
                newY = y + h - newHeight;
                fig.setHandleBox(newX, newY, newWidth, newHeight);
                if ((newX + newWidth) != (x + w)) {
                    newX += (newX + newWidth) - (x + w);
                }
                if ((newY + newHeight) != (y + h)) {
                    newY += (newY + newHeight) - (y + h);
                }
                setHandleBox(partitions, newX, newY, newWidth, newHeight);
                return;
            case Handle.NORTH :
                break;
            case Handle.NORTHEAST :
                newWidth = mX - x;
                newWidth = (newWidth < minWidth) ? minWidth : newWidth;
                newHeight = y + h - mY;
                newHeight = (newHeight < minHeight) ? minHeight : newHeight;
                newY = y + h - newHeight;
                fig.setHandleBox(newX, newY, newWidth, newHeight);
                if ((newY + newHeight) != (y + h)) {
                    newY += (newY + newHeight) - (y + h);
                }
                setHandleBox(partitions, newX, newY, newWidth, newHeight);
                break;
            case Handle.WEST :
                break;
            case Handle.EAST :
                break;
            case Handle.SOUTHWEST :
                newWidth = x + w - mX;
                newWidth = (newWidth < minWidth) ? minWidth : newWidth;
                newHeight = mY - y;
                newHeight = (newHeight < minHeight) ? minHeight : newHeight;
                newX = x + w - newWidth;
                fig.setHandleBox(newX, newY, newWidth, newHeight);
                if ((newX + newWidth) != (x + w)) {
                    newX += (newX + newWidth) - (x + w);
                }
                setHandleBox(partitions, newX, newY, newWidth, newHeight);
                break;
            case Handle.SOUTH :
                break;
            case Handle.SOUTHEAST :
                newWidth = mX - x;
                newWidth = (newWidth < minWidth) ? minWidth : newWidth;
                newHeight = mY - y;
                newHeight = (newHeight < minHeight) ? minHeight : newHeight;
                setHandleBox(partitions, newX, newY, newWidth, newHeight);
                break;
            }
        }
        
        private void setHandleBox(
        	List partitions, int x, int y, int width, int height) {
            Iterator it = partitions.iterator();
            while (it.hasNext()) {
        	Fig f = (Fig) it.next();
        	if (f == getContent()) {
                    f.setHandleBox(x, y, width, height);
        	} else {
                    f.setHandleBox(f.getX(), y, f.getWidth(), height);
        	}
            }
            
        }
    }
}

