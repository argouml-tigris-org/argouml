// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: SelectionWButtons.java
// Classes: SelectionWButtons
// Original Author: jrobbins@ics.uci.edu
// $Id$

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

public abstract class SelectionWButtons extends SelectionNodeClarifiers {
    ////////////////////////////////////////////////////////////////
    // constants
    public static final int IMAGE_SIZE = 22;
    public static final int MARGIN = 2;
    public static final Color PRESSED_COLOR = Color.gray.brighter();

    /**
     * The maximum number of tries to place a fig.
     */
    private static final int MAX_PLACINGS = 1;

    ////////////////////////////////////////////////////////////////
    // static variables
    public static int Num_Button_Clicks = 0;
    public static boolean _showRapidButtons = true;

    ////////////////////////////////////////////////////////////////
    // instance variables
    protected boolean _paintButtons = true;
    protected int _pressedButton = -1;

    /**
     * Counter for counting the number of times there has been a try to place 
     * a fig.
     */
    private int _placeCounter = 0;

    ////////////////////////////////////////////////////////////////
    // constructors

    /** Construct a new SelectionWButtons for the given Fig */
    public SelectionWButtons(Fig f) {
        super(f);
        _paintButtons = _showRapidButtons;
    }

    ////////////////////////////////////////////////////////////////
    // static accessors

    public static void toggleShowRapidButtons() {
        _showRapidButtons = !_showRapidButtons;
    }

    ////////////////////////////////////////////////////////////////
    // interaction utility methods

    public boolean hitAbove(int x, int y, int w, int h, Rectangle r) {
        return intersectsRect(r, x - w / 2, y - h - MARGIN, w, h + MARGIN);
    }

    public boolean hitBelow(int x, int y, int w, int h, Rectangle r) {
        return intersectsRect(r, x - w / 2, y, w, h + MARGIN);
    }

    public boolean hitLeft(int x, int y, int w, int h, Rectangle r) {
        return intersectsRect(r, x, y - h / 2, w + MARGIN, h);
    }

    public boolean hitRight(int x, int y, int w, int h, Rectangle r) {
        return intersectsRect(r, x - w - MARGIN, y - h / 2, w + MARGIN, h);
    }

    public boolean intersectsRect(Rectangle r, int x, int y, int w, int h) {
        return !(
            (r.x + r.width <= x)
                || (r.y + r.height <= y)
                || (r.x >= x + w)
                || (r.y >= y + h));
    }

    ////////////////////////////////////////////////////////////////
    // display methods

    /** Paint the handles at the four corners and midway along each edge
     * of the bounding box.  */
    public void paint(Graphics g) {
        super.paint(g);
        if (!_paintButtons)
            return;
        Editor ce = Globals.curEditor();
        SelectionManager sm = ce.getSelectionManager();
        if (sm.size() != 1)
            return;
        ModeManager mm = ce.getModeManager();
        if (mm.includes(ModeModify.class) && _pressedButton == -1)
            return;
        paintButtons(g);
    }

    public abstract void paintButtons(Graphics g);

    public void paintButtonAbove(Icon i, Graphics g, int x, int y, int hi) {
        paintButton(
            i,
            g,
            x - i.getIconWidth() / 2,
            y - i.getIconHeight() - MARGIN,
            hi);
    }

    public void paintButtonBelow(Icon i, Graphics g, int x, int y, int hi) {
        paintButton(i, g, x - i.getIconWidth() / 2, y + MARGIN, hi);
    }

    public void paintButtonLeft(Icon i, Graphics g, int x, int y, int hi) {
        paintButton(i, g, x + MARGIN, y - i.getIconHeight() / 2, hi);
    }

    public void paintButtonRight(Icon i, Graphics g, int x, int y, int hi) {
        paintButton(
            i,
            g,
            x - i.getIconWidth() - MARGIN,
            y - i.getIconHeight() / 2,
            hi);
    }

    public void paintButton(Icon i, Graphics g, int x, int y, int hi) {
        int w = i.getIconWidth() + 4;
        int h = i.getIconHeight() + 4;

        if (hi == _pressedButton) {
            g.setColor(PRESSED_COLOR);
            g.fillRect(x - 2, y - 2, w, h);
        }
        i.paintIcon(null, g, x, y);

        g.translate(x - 2, y - 2);

        Color handleColor = Globals.getPrefs().handleColorFor(_content);
        g.setColor(handleColor.darker());
        g.drawRect(0, 0, w - 2, h - 2);

        g.setColor(handleColor.brighter().brighter().brighter());
        if (hi != _pressedButton) {
            g.drawLine(1, h - 3, 1, 1);
            g.drawLine(1, 1, w - 3, 1);
        }

        g.drawLine(0, h - 1, w - 1, h - 1);
        g.drawLine(w - 1, h - 1, w - 1, 0);

        g.translate(-x + 2, -y + 2);
    }

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
        _paintButtons = false;
    }

    public void buttonClicked(int buttonCode) {
        if (buttonCode >= 10)
            Num_Button_Clicks++;
        // get a new node (modelelement) that should be added
        Object newNode = getNewNode(buttonCode);
        Object owner = _content.getOwner();

        // get the graphmodel
        Editor ce = Globals.curEditor();
        GraphModel gm = ce.getGraphModel();
        if (!(gm instanceof MutableGraphModel))
            return;
        MutableGraphModel mgm = (MutableGraphModel) gm;

        // check if it is possible to add the fig for the new node and create it if possible
        if (!mgm.canAddNode(newNode))
            return;
        GraphNodeRenderer renderer = ce.getGraphNodeRenderer();
        LayerPerspective lay =
            (LayerPerspective) ce.getLayerManager().getActiveLayer();
        Fig newFC = renderer.getFigNodeFor(gm, lay, newNode);
        
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
                    (int) (fcCenter.x + _content.getSize().getWidth() / 2),
                    fcCenter.y);

            int yoffset = (int) ((_content.getSize().getHeight() / 2));
            edgeShape.addPoint(fcCenter.x, fcCenter.y);
            edgeShape.addPoint(centerRight.x, centerRight.y);
            edgeShape.addPoint(centerRight.x + 30, centerRight.y);
            edgeShape.addPoint(centerRight.x + 30, centerRight.y + yoffset);
            edgeShape.addPoint(centerRight.x, centerRight.y + yoffset);
        }

        // create the new edge (modelelement) between newNode and the owner of _content
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

    public void mousePressed(MouseEvent me) {
        Handle h = new Handle(-1);
        hitHandle(me.getX(), me.getY(), 0, 0, h);
        _pressedButton = h.index;
        Editor ce = Globals.curEditor();
        ce.damaged(this);
    }

    public void mouseReleased(MouseEvent me) {
        if (_pressedButton < 10)
            return;
        Handle h = new Handle(-1);
        hitHandle(me.getX(), me.getY(), 0, 0, h);
        if (_pressedButton == h.index) {
            buttonClicked(_pressedButton);
            me.consume();
        }
        _pressedButton = -1;
        Editor ce = Globals.curEditor();
        ce.damaged(this);
    }

    public void mouseEntered(MouseEvent me) {
        super.mouseEntered(me);
        if (_showRapidButtons)
            _paintButtons = true;
        Editor ce = Globals.curEditor();
        ce.damaged(this);
    }
    public void mouseExited(MouseEvent me) {
        super.mouseExited(me);
        _paintButtons = false;
        Editor ce = Globals.curEditor();
        ce.damaged(this);
    }

    /**
       * Places a fig on the canvas in the correct position. Takes a coordinate pair 
       * x,y and a rectangle that should be avoided because there can be other 
       * figures. If the place action results in x.y coordinates for the fig to place
       * that are not allowed (beyond the borders of the diagram), the operation is
       * repeated with corrected parameters. If it is not possible to add the fig
       * because there are allready to many figs, false is returned and the fig is 
       * not added.
       * @param figToPlace The figure one wishes to place on a diagram
       * @param layerToPlaceOn The layer that contains the figs
       * @param x The x coordinate where one wishes to place the fig
       * @param y The y coordinate where one wishes to place the fig
       * @param bumpRect The rectangle that should be avoided since there can be other figs.
       * @return boolean false if the fig is not placed.
       */
    protected boolean placeFig(
        Fig figToPlace,
        LayerPerspective layerToPlaceOn,
        int x,
        int y,
        Rectangle bumpRect) {
        if (_placeCounter > MAX_PLACINGS)
            return false;
        // to prevent outofmemory errors and stackoverflow errors
        _placeCounter++;
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
                ((Fig) _content).getX() - figToPlace.getWidth() - 100,
                figToPlace.getY(),
                bumpRect);
        } else if (figToPlace.getY() + figToPlace.getHeight() >= 6000) {
            return placeFig(
                figToPlace,
                layerToPlaceOn,
                figToPlace.getX(),
                ((Fig) _content).getY() - figToPlace.getHeight() - 100,
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
     */
    protected abstract Object getNewNode(int buttonCode);
    
    /**
     * Subclasses should override this method if they want to provide a quickbutton above
     * the _content fig. This method returns the edge (modelelement) that should be drawn in the
     * case such a quickbutton was pressed.
     * @param gm
     * @param newNode The node (modelelement) created by pressing the quickbutton
     * @return Object The new edge
     */
    protected Object createEdgeAbove(MutableGraphModel gm, Object newNode) {
        return null;
    }
    
    /**
     * Subclasses should override this method if they want to provide a quickbutton at the left
     * of the _content fig. This method returns the edge (modelelement) that should be drawn in the
     * case such a quickbutton was pressed.
     * @param gm
     * @param newNode The node (modelelement) created by pressing the quickbutton
     * @return Object The new edge
     */
    protected Object createEdgeLeft(MutableGraphModel gm, Object newNode) {
        return null;
    }
    
    /**
     * Subclasses should override this method if they want to provide a quickbutton at the right
     * of the _content fig. This method returns the edge (modelelement) that should be drawn in the
     * case such a quickbutton was pressed.
     * @param gm
     * @param newNode The node (modelelement) created by pressing the quickbutton
     * @return Object The new edge
     */
    protected Object createEdgeRight(MutableGraphModel gm, Object newNode) {
        return null;
    }
    
    /**
     * Subclasses should override this method if they want to provide a quickbutton under
     * the _content fig. This method returns the edge (modelelement) that should be drawn in the
     * case such a quickbutton was pressed.
     * @param gm
     * @param newNode The node (modelelement) created by pressing the quickbutton
     * @return Object The new edge
     */
    protected Object createEdgeUnder(MutableGraphModel gm, Object newNode) {
        return null;
    }
    
    /**
     * Subclasses should override this method if they want to provide a quickbutton for 
     * selfassociation. This method returns the edge (modelelement) that should be drawn in the
     * case such a quickbutton was pressed.
     * @param gm
     * @return Object The new edge
     */
    protected Object createEdgeToSelf(MutableGraphModel gm) {
        return null;
    }

} /* end class SelectionWButtons */
