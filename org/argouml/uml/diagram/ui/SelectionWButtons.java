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

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.Icon;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.base.ModeModify;
import org.tigris.gef.base.SelectionManager;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.Handle;

/**
 *
 * @author jrobbins
 */
public abstract class SelectionWButtons extends SelectionNodeClarifiers {
    ////////////////////////////////////////////////////////////////
    // constants
    private static final int IMAGE_SIZE = 22;
    private static final int MARGIN = 2;
    private static final Color PRESSED_COLOR = Color.gray.brighter();

    /**
     * The maximum number of tries to place a fig.
     */
    private static final int MAX_PLACINGS = 1;

    ////////////////////////////////////////////////////////////////
    // static variables
    private static int numButtonClicks = 0;
    
    /**
     * The bool showRapidButtons is only false if the user selected 
     * to never show the buttons. The user can not do this currently.
     */
    private static boolean showRapidButtons = true;

    ////////////////////////////////////////////////////////////////
    // instance variables
    
    /** True if the buttons on selection are currently shown. */
    private boolean paintButtons = true;
    private int pressedButton = -1;

    /**
     * Counter for counting the number of times there has been a try to place
     * a fig.
     */
    private int placeCounter = 0;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Construct a new SelectionWButtons for the given Fig.
     *
     * @param f The given Fig.
     */
    public SelectionWButtons(Fig f) {
        super(f);
        paintButtons = showRapidButtons;
    }

    ////////////////////////////////////////////////////////////////
    // static accessors

    /**
     * Toggle ShowRapidButtons. Use this to switch off the displaying 
     * of the rapid buttons completely. This may be used for a user setting,
     * although the benefit of switching them of is debatable. 
     * See also issue 2492.
     */
    public static void toggleShowRapidButtons() {
        showRapidButtons = !showRapidButtons;
    }

    ////////////////////////////////////////////////////////////////
    // interaction utility methods

    /**
     * @param x x of the selection button icon
     * @param y y of the selection button icon
     * @param w width of the selection button icon
     * @param h height of the selection button icon
     * @param r outer rectangle of the fig
     * @return true if the selection button above the fig was clicked
     */
    public boolean hitAbove(int x, int y, int w, int h, Rectangle r) {
        return intersectsRect(r, x - w / 2, y - h - MARGIN, w, h + MARGIN);
    }

    /**
     * @param x x  of the selection button icon
     * @param y y of the selection button icon
     * @param w width of the selection button icon
     * @param h height of the selection button icon
     * @param r outer rectangle of the fig
     * @return true if the selection button below the fig was clicked
     */
    public boolean hitBelow(int x, int y, int w, int h, Rectangle r) {
        return intersectsRect(r, x - w / 2, y, w, h + MARGIN);
    }

    /**
     * @param x x of the selection button icon
     * @param y y of the selection button icon
     * @param w width of the selection button icon
     * @param h height of the selection button icon
     * @param r outer rectangle of the fig
     * @return true if the selection button left from the fig was clicked
     */
    public boolean hitLeft(int x, int y, int w, int h, Rectangle r) {
        return intersectsRect(r, x, y - h / 2, w + MARGIN, h);
    }

    /**
     * @param x x of the selection button icon
     * @param y y of the selection button icon
     * @param w width of the selection button icon
     * @param h height of the selection button icon
     * @param r outer rectangle of the fig
     * @return true if the selection button right from the fig was clicked
     */
    public boolean hitRight(int x, int y, int w, int h, Rectangle r) {
        return intersectsRect(r, x - w - MARGIN, y - h / 2, w + MARGIN, h);
    }

    /**
     * @param x x of rectangle 2
     * @param y y of rectangle 2
     * @param w width of rectangle 2
     * @param h height of rectangle 2
     * @param r rectangle 1
     * @return true if rectangle 1 intersects with the rectangle 2
     */
    public boolean intersectsRect(Rectangle r, int x, int y, int w, int h) {
        return !(
		 (r.x + r.width <= x)
		 || (r.y + r.height <= y)
		 || (r.x >= x + w)
		 || (r.y >= y + h));
    }

    ////////////////////////////////////////////////////////////////
    // display methods

    /**
     * Paint the handles at the four corners and midway along each edge
     * of the bounding box.
     *
     * @param g The Graphics where we paint this.
     */
    public void paint(Graphics g) {
        super.paint(g);
        if (!paintButtons)
            return;
        Editor ce = Globals.curEditor();
        SelectionManager sm = ce.getSelectionManager();
        if (sm.size() != 1)
            return;
        ModeManager mm = ce.getModeManager();
        if (mm.includes(ModeModify.class) && pressedButton == -1)
            return;
        paintButtons(g);
    }

    /**
     * Paint the handles at the four corners and midway along each edge
     * of the bounding box.
     *
     * @param g The Graphics where to paint the buttons.
     */
    public abstract void paintButtons(Graphics g);

    /**
     * @param i the icon to be painted
     * @param g the graphics to draw on
     * @param x x for the icon
     * @param y y for the icon
     * @param hi the button identifier
     */
    public void paintButtonAbove(Icon i, Graphics g, int x, int y, int hi) {
        paintButton(
		    i,
		    g,
		    x - i.getIconWidth() / 2,
		    y - i.getIconHeight() - MARGIN,
		    hi);
    }

    /**
     * @param i the icon to be painted
     * @param g the graphics to draw on
     * @param x x for the icon
     * @param y y for the icon
     * @param hi the button identifier
     */
    public void paintButtonBelow(Icon i, Graphics g, int x, int y, int hi) {
        paintButton(i, g, x - i.getIconWidth() / 2, y + MARGIN, hi);
    }

    /**
     * @param i the icon to be painted
     * @param g the graphics to draw on
     * @param x x for the icon
     * @param y y for the icon
     * @param hi the button identifier
     */
    public void paintButtonLeft(Icon i, Graphics g, int x, int y, int hi) {
        paintButton(i, g, x + MARGIN, y - i.getIconHeight() / 2, hi);
    }

    /**
     * @param i the icon to be painted
     * @param g the graphics to draw on
     * @param x x for the icon
     * @param y y for the icon
     * @param hi the button identifier
     */
    public void paintButtonRight(Icon i, Graphics g, int x, int y, int hi) {
        paintButton(
		    i,
		    g,
		    x - i.getIconWidth() - MARGIN,
		    y - i.getIconHeight() / 2,
		    hi);
    }

    /**
     * @param i the icon to be painted
     * @param g the graphics to draw on
     * @param x x for the icon
     * @param y y for the icon
     * @param hi the button identifier
     */
    public void paintButton(Icon i, Graphics g, int x, int y, int hi) {
        int w = i.getIconWidth() + 4;
        int h = i.getIconHeight() + 4;

        if (hi == pressedButton) {
            g.setColor(PRESSED_COLOR);
            g.fillRect(x - 2, y - 2, w, h);
        }
        i.paintIcon(null, g, x, y);

        g.translate(x - 2, y - 2);

        Color handleColor = Globals.getPrefs().handleColorFor(_content);
        g.setColor(handleColor.darker());
        g.drawRect(0, 0, w - 2, h - 2);

        g.setColor(handleColor.brighter().brighter().brighter());
        if (hi != pressedButton) {
            g.drawLine(1, h - 3, 1, 1);
            g.drawLine(1, 1, w - 3, 1);
        }

        g.drawLine(0, h - 1, w - 1, h - 1);
        g.drawLine(w - 1, h - 1, w - 1, 0);

        g.translate(-x + 2, -y + 2);
    }

    /**
     * @see org.tigris.gef.base.Selection#getBounds()
     */
    public Rectangle getBounds() {
        return new Rectangle(
			     _content.getX() - IMAGE_SIZE * 2,
			     _content.getY() - IMAGE_SIZE * 2,
			     _content.getWidth() + IMAGE_SIZE * 4,
			     _content.getHeight() + IMAGE_SIZE * 4);
    }

    /** Dont show buttons while the user is moving the Class.  Called
     *  from FigClass when it is translated. */
    public void hideButtons() {
        paintButtons = false;
    }

    /**
     * @param buttonCode the button identifier
     */
    public void buttonClicked(int buttonCode) {
        if (buttonCode >= 10)
            numButtonClicks++;
        // get a new node (modelelement) that should be added
        Object newNode = getNewNode(buttonCode);
        Object owner = _content.getOwner();

        // get the graphmodel
        Editor ce = Globals.curEditor();
        GraphModel gm = ce.getGraphModel();
        if (!(gm instanceof MutableGraphModel))
            return;
        MutableGraphModel mgm = (MutableGraphModel) gm;

        // check if it is possible to add the fig for the new node and
        // create it if possible
        if (!mgm.canAddNode(newNode))
            return;
        GraphNodeRenderer renderer = ce.getGraphNodeRenderer();
        LayerPerspective lay =
            (LayerPerspective) ce.getLayerManager().getActiveLayer();
        Fig newFC = renderer.getFigNodeFor(gm, lay, newNode, null);

        // calculate the position of the newly created fig.
        Rectangle outputRect =
            new Rectangle(
			  Math.max(0, _content.getX() - 200),
			  Math.max(0, _content.getY() - 200),
			  _content.getWidth() + 400,
			  _content.getHeight() + 400);

        // handle the case that it is not a self association
        if (buttonCode >= 10 && buttonCode <= 13) {
            int x = 0;
            int y = 0;
            if (buttonCode == 10) {
                // superclass
                x = _content.getX();
                y = Math.max(0, _content.getY() - 200);
            } else if (buttonCode == 11) {
                x = _content.getX();
                y = _content.getY() + _content.getHeight() + 100;
            } else if (buttonCode == 12) {
                x = _content.getX() + _content.getWidth() + 100;
                y = _content.getY();
            } else if (buttonCode == 13) {
                x = Math.max(0, _content.getX() - 200);
                y = _content.getY();

            }
            // place the fig if it is not a selfassociation
            if (!placeFig(newFC, lay, x, y, outputRect))
                return;
        }

        // add the new node only if required, e.g. not when we add a
        // self association
        if (buttonCode != 14) {
            ce.add(newFC);
            mgm.addNode(newNode);
        }
        FigPoly edgeShape = new FigPoly();
        if (buttonCode != 14) {
            Point fcCenter = _content.center();
            edgeShape.addPoint(fcCenter.x, fcCenter.y);
            Point newFCCenter = newFC.center();
            edgeShape.addPoint(newFCCenter.x, newFCCenter.y);
        } else {
            newFC = _content;
            Point fcCenter = _content.center();
            Point centerRight =
                new Point(
			  (int) (fcCenter.x
				 + _content.getSize().getWidth() / 2),
			  fcCenter.y);

            int yoffset = (int) ((_content.getSize().getHeight() / 2));
            edgeShape.addPoint(fcCenter.x, fcCenter.y);
            edgeShape.addPoint(centerRight.x, centerRight.y);
            edgeShape.addPoint(centerRight.x + 30, centerRight.y);
            edgeShape.addPoint(centerRight.x + 30, centerRight.y + yoffset);
            edgeShape.addPoint(centerRight.x, centerRight.y + yoffset);
        }

        // create the new edge (modelelement) between newNode and the
        // owner of _content
        Object newEdge = null;
        if (buttonCode == 10) newEdge = createEdgeAbove(mgm, newNode);
        else if (buttonCode == 11) newEdge = createEdgeUnder(mgm, newNode);
        else if (buttonCode == 12) newEdge = createEdgeRight(mgm, newNode);
        else if (buttonCode == 13) newEdge = createEdgeLeft(mgm, newNode);
        else if (buttonCode == 14) newEdge = createEdgeToSelf(mgm);

        // place the edge on the layer and update the diagram
        FigEdge fe = (FigEdge) lay.presentationFor(newEdge);
        fe.setBetweenNearestPoints(true);
        edgeShape.setLineColor(Color.black);
        edgeShape.setFilled(false);
        edgeShape._isComplete = true;
        fe.setFig(edgeShape);
        newFC.damage();

        ce.getSelectionManager().select(fe);
        ce.getSelectionManager().select(_content);

    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent me) {
        Handle h = new Handle(-1);
        hitHandle(me.getX(), me.getY(), 0, 0, h);
        pressedButton = h.index;
        Editor ce = Globals.curEditor();
        ce.damaged(this);
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        if (pressedButton < 10)
            return;
        Handle h = new Handle(-1);
        hitHandle(me.getX(), me.getY(), 0, 0, h);
        if (pressedButton == h.index) {
            buttonClicked(pressedButton);
            me.consume();
        }
        pressedButton = -1;
        Editor ce = Globals.curEditor();
        ce.damaged(this);
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent me) {
        super.mouseEntered(me);
        if (showRapidButtons)
            paintButtons = true;
        Editor ce = Globals.curEditor();
        ce.damaged(this);
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent me) {
        super.mouseExited(me);
        paintButtons = false;
        Editor ce = Globals.curEditor();
        ce.damaged(this);
    }

    /**
     * Places a fig on the canvas in the correct position. Takes a
     * coordinate pair x,y and a rectangle that should be avoided
     * because there can be other figures. If the place action results
     * in x.y coordinates for the fig to place that are not allowed
     * (beyond the borders of the diagram), the operation is repeated
     * with corrected parameters. If it is not possible to add the fig
     * because there are allready to many figs, false is returned and
     * the fig is not added.
     * @param figToPlace The figure one wishes to place on a diagram
     * @param layerToPlaceOn The layer that contains the figs
     * @param x The x coordinate where one wishes to place the fig
     * @param y The y coordinate where one wishes to place the fig
     * @param bumpRect The rectangle that should be avoided since
     * there can be other figs.
     * @return boolean false if the fig is not placed.
     */
    protected boolean placeFig(
			       Fig figToPlace,
			       LayerPerspective layerToPlaceOn,
			       int x,
			       int y,
			       Rectangle bumpRect) {
        if (placeCounter > MAX_PLACINGS)
            return false;
        // to prevent outofmemory errors and stackoverflow errors
        placeCounter++;
        figToPlace.setLocation(x, y);
        layerToPlaceOn.bumpOffOtherNodesIn(figToPlace, bumpRect, false, false);
        if (figToPlace.getX() < 0) {
            return placeFig(
			    figToPlace,
			    layerToPlaceOn,
			    ((Fig) _content).getX()
			    + ((Fig) _content).getWidth()
			    + figToPlace.getWidth()
			    + 100,
			    figToPlace.getY(),
			    bumpRect);
        } else if (figToPlace.getX() + figToPlace.getWidth() >= 6000) {
            return placeFig(
			    figToPlace,
			    layerToPlaceOn,
			    (((Fig) _content).getX()
			     - figToPlace.getWidth()
			     - 100),
			    figToPlace.getY(),
			    bumpRect);
        } else if (figToPlace.getY() + figToPlace.getHeight() >= 6000) {
            return placeFig(
			    figToPlace,
			    layerToPlaceOn,
			    figToPlace.getX(),
			    (((Fig) _content).getY()
			     - figToPlace.getHeight()
			     - 100),
			    bumpRect);
        } else if (figToPlace.getY() < 0) {
            return placeFig(
			    figToPlace,
			    layerToPlaceOn,
			    figToPlace.getX(),
			    ((Fig) _content).getY()
			    + ((Fig) _content).getHeight()
			    + figToPlace.getHeight()
			    + 100,
			    bumpRect);
        }
        return true;
    }

    /**
     * Implementors should return a new node for adding via the buttons.
     *
     * @param buttonCode the code (identifier) for the selection button
     *                   that was hit
     * @return a newly created UML element
     */
    protected abstract Object getNewNode(int buttonCode);

    /**
     * Subclasses should override this method if they want to provide
     * a quickbutton above the _content fig. This method returns the
     * edge (modelelement) that should be drawn in the case such a
     * quickbutton was pressed.
     * @param gm the graphmodel
     * @param newNode The node (modelelement) created by pressing the
     * quickbutton
     * @return Object The new edge
     */
    protected Object createEdgeAbove(MutableGraphModel gm, Object newNode) {
        return null;
    }

    /**
     * Subclasses should override this method if they want to provide
     * a quickbutton at the left of the _content fig. This method
     * returns the edge (modelelement) that should be drawn in the
     * case such a quickbutton was pressed.
     * @param gm the graphmodel
     * @param newNode The node (modelelement) created by pressing the
     * quickbutton
     * @return Object The new edge
     */
    protected Object createEdgeLeft(MutableGraphModel gm, Object newNode) {
        return null;
    }

    /**
     * Subclasses should override this method if they want to provide
     * a quickbutton at the right of the _content fig. This method
     * returns the edge (modelelement) that should be drawn in the
     * case such a quickbutton was pressed.
     * @param gm the graphmodel
     * @param newNode The node (modelelement) created by pressing the
     * quickbutton
     * @return Object The new edge
     */
    protected Object createEdgeRight(MutableGraphModel gm, Object newNode) {
        return null;
    }

    /**
     * Subclasses should override this method if they want to provide
     * a quickbutton under the _content fig. This method returns the
     * edge (modelelement) that should be drawn in the case such a
     * quickbutton was pressed.
     * @param gm the graphmodel
     * @param newNode The node (modelelement) created by pressing the
     * quickbutton
     * @return Object The new edge
     */
    protected Object createEdgeUnder(MutableGraphModel gm, Object newNode) {
        return null;
    }

    /**
     * Subclasses should override this method if they want to provide
     * a quickbutton for selfassociation. This method returns the edge
     * (modelelement) that should be drawn in the case such a
     * quickbutton was pressed.
     * @param gm the graphmodel
     * @return Object The new edge
     */
    protected Object createEdgeToSelf(MutableGraphModel gm) {
        return null;
    }

    /**
     * @param paint The _paintButtons to set.
     */
    protected void setPaintButtons(boolean paint) {
        this.paintButtons = paint;
    }

    /**
     * @return Returns the _paintButtons.
     */
    protected boolean isPaintButtons() {
        return paintButtons;
    }

    /**
     * @param pressed the identifier for the pressed Button
     */
    protected void setPressedButton(int pressed) {
        this.pressedButton = pressed;
    }

    /**
     * @return Returns the identifier for the pressed Button.
     */
    protected int getPressedButton() {
        return pressedButton;
    }

} /* end class SelectionWButtons */
