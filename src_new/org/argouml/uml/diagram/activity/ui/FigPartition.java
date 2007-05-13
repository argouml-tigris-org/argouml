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
    private FigLine seperator;
    private boolean placed;
    
    private FigPartition previousPartition;
    private FigPartition nextPartition;
    
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

        getNameFig().setLineWidth(0);
        getNameFig().setBounds(10, 10, 50, 25);
        getNameFig().setFilled(false);
        
        seperator = new FigLine(10, 25, 150, 25);

        addFig(getBigPort());
        addFig(rightLine);
        addFig(leftLine);
        addFig(topLine);
        addFig(bottomLine);
        addFig(getNameFig());
        addFig(seperator);

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
//        figClone.seperator = (FigLine) it.next();
        return figClone;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        rightLine.setLineColor(col);
        leftLine.setLineColor(col);
        bottomLine.setLineColor(col);
        topLine.setLineColor(col);
        seperator.setLineColor(col);
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
	// TODO: i18n
        return "new Swimlane";
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension nameDim = getNameFig().getMinimumSize();
        int w = nameDim.width;
        int h = nameDim.height;

        // we want to maintain a minimum size for the partition
        w = Math.max(64, w);
        h = Math.max(256, h);

        return new Dimension(w, h);
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
        topLine.setBounds(x, y, w - 1, 0);
        bottomLine.setBounds(x, y + h, w - 1, 0);
        seperator.setBounds(x, y + nameBounds.height, w - 1, 0);

        firePropChange("bounds", oldBounds, getBounds());
        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
    }
    
    public Selection makeSelection() {
	return new SelectionPartition(this);
    }
    
    /**
     * On post placement look to see if there are any other
     * FigPartitions. If so place to the right and resize height.
     */
    public void appendToPool() {
	List partitions = getPartitions(getLayer());
	
        UMLActivityDiagram diagram = 
            (UMLActivityDiagram) getProject().getActiveDiagram();
        Object machine = diagram.getStateMachine();
        Model.getCoreHelper().setModelElementContainer(
                getOwner(), machine);
	
	if (partitions.size() == 1) {
	    FigPool fp = new FigPool();
	    getLayer().add(fp);
	    fp.setBounds(getBounds());
	    getLayer().bringToFront(this);
	} else if (partitions.size() > 1) {
	    FigPool fp = getFigPool();
	    fp.setWidth(fp.getWidth() + getWidth());
	    
            int x = 0;
	    Iterator it = partitions.iterator();
	    FigPartition f = null;
	    FigPartition previousFig = null;
	    while (it.hasNext()) {
		f = (FigPartition) it.next();
		if (f != this && f.getX() + f.getWidth() > x) {
		    previousFig = f;
		    x = f.getX();
		}
	    }
	    setPreviousPartition(previousFig);
	    previousPartition.setNextPartition(this);
	    setBounds(
		    x + previousFig.getWidth(),
		    previousFig.getY(),
		    getWidth(),
		    previousFig.getHeight());
	}
    }
    
    public void removeFromDiagramImpl() {
	int width = getWidth();
	
	super.removeFromDiagramImpl();
	
	FigPartition next = nextPartition;
	while (next != null) {
	    next.translateWithContents(-width);
            next = next.nextPartition;
	}
	
	if (nextPartition != null) {
	    nextPartition.setPreviousPartition(previousPartition);
	}
	
	if (previousPartition != null) {
	    previousPartition.setNextPartition(nextPartition);
	}
	
	setPreviousPartition(null);
	setNextPartition(null);
	
	getFigPool().setWidth(getFigPool().getWidth() - width);
    }
    
    // TODO: Needs work. Must determine which Figs enclosed
    // in the pool are within the bounds of this Fig
    // and translate those.
    private void translateWithContents(int dx) {
	Iterator it = getFigPool().getEnclosedFigs().iterator();
	while (it.hasNext()) {
	    Fig f = (Fig) it.next();
            f.setX(f.getX() + dx);
	}
	setX(getX() + dx);
	damage();
    }
    
    /**
     * Get all the partitions on the same layer as this FigPartition
     * @return th partitions
     */
    private List getPartitions(Layer layer) {
        final List partitions = new ArrayList();
        
        Iterator it = layer.getContents().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof FigPartition) {
                partitions.add(o);
            }
        }
        
        return partitions;
    }
    
    /**
     * Get all the partitions on the same layer as this FigPartition
     * @return th partitions
     */
    private FigPool getFigPool() {
        Iterator it = getLayer().getContents().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof FigPool) {
                return (FigPool) o;
            }
        }
        
        return null;
    }
    

    /**
     * @param nextPartition The nextPartition to set.
     */
    void setNextPartition(FigPartition next) {
        this.nextPartition = next;
    }

    /**
     * @param previousPartition The previousPartition to set.
     */
    void setPreviousPartition(FigPartition previous) {
        this.previousPartition = previous;
        leftLine.setVisible(previousPartition == null);
    }
    
    /**
     * When dragging this partition drag all other partitions with it.
     * @return all the partitions to drag togther.
     */
    public List getDragDependencies() {
	List dependents = getPartitions(getLayer());
	dependents.add(getFigPool());
	dependents.addAll(getFigPool().getEnclosedFigs());
	return dependents;
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
         * of its handles.
         */
        public void dragHandle(int mX, int mY, int anX, int anY, Handle hand) {
            
            final Fig fig = getContent();

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
                if ((newX + newWidth) != (x + w)) {
                    newX += (newX + newWidth) - (x + w);
                }
                if ((newY + newHeight) != (y + h)) {
                    newY += (newY + newHeight) - (y + h);
                }
                setHandleBox(
                	previousPartition, 
                	newX, 
                	newY, 
                	newWidth, 
                	newHeight);
                return;
            case Handle.NORTH :
                break;
            case Handle.NORTHEAST :
                newWidth = mX - x;
                newWidth = (newWidth < minWidth) ? minWidth : newWidth;
                newHeight = y + h - mY;
                newHeight = (newHeight < minHeight) ? minHeight : newHeight;
                newY = y + h - newHeight;
                if ((newY + newHeight) != (y + h)) {
                    newY += (newY + newHeight) - (y + h);
                }
                setHandleBox(
                	nextPartition, 
                	newX, 
                	newY, 
                	newWidth, 
                	newHeight);
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
                if ((newX + newWidth) != (x + w)) {
                    newX += (newX + newWidth) - (x + w);
                }
                setHandleBox(
                	previousPartition, 
                	newX, 
                	newY, 
                	newWidth, 
                	newHeight);
                break;
            case Handle.SOUTH :
                break;
            case Handle.SOUTHEAST :
                newWidth = mX - x;
                newWidth = (newWidth < minWidth) ? minWidth : newWidth;
                newHeight = mY - y;
                newHeight = (newHeight < minHeight) ? minHeight : newHeight;
                setHandleBox(
                	nextPartition, 
                	newX, 
                	newY, 
                	newWidth, 
                	newHeight);
                break;
            }
        }
        
        private void setHandleBox(
        	FigPartition neighbour, 
        	int x, 
        	int y, 
        	int width, 
        	int height) {
            
            final List partitions = getPartitions(getLayer());
            
            int newNeighbourWidth = 0;
            if (neighbour != null) {
                newNeighbourWidth = 
                    (neighbour.getWidth() + getContent().getWidth()) - width;
        	if (neighbour.getMinimumSize().width > newNeighbourWidth) {
        	    return;
        	}
            }
            
            int lowX = 0;
            int totalWidth = 0;
            Iterator it = partitions.iterator();
            while (it.hasNext()) {
        	Fig f = (Fig) it.next();
        	if (f == getContent()) {
                    f.setHandleBox(x, y, width, height);
        	} else if (f == neighbour && f == previousPartition) {
                    f.setHandleBox(f.getX(), y, newNeighbourWidth, height);
        	} else if (f == neighbour && f == nextPartition) {
                    f.setHandleBox(x + width, y, newNeighbourWidth, height);
        	} else {
                    f.setHandleBox(f.getX(), y, f.getWidth(), height);
        	}
        	if (f.getHandleBox().getX() < lowX || totalWidth == 0) {
        	    lowX = f.getHandleBox().x;
        	}
        	totalWidth += f.getHandleBox().width;
            }
            FigPool pool = getFigPool();
            pool.setBounds(lowX, y, totalWidth, height);
        }
    }
}

