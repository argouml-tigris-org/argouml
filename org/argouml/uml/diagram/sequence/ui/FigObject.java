// Copyright (c) 1996-2003 The Regents of the University of California. All
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
package org.argouml.uml.diagram.sequence.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.uml.diagram.sequence.ActivationNode;
import org.argouml.uml.diagram.sequence.EmptyNode;
import org.argouml.uml.diagram.sequence.LifeLinePort;
import org.argouml.uml.diagram.sequence.LinkNode;
import org.argouml.uml.diagram.sequence.LinkPort;
import org.argouml.uml.diagram.sequence.Node;
import org.argouml.uml.diagram.sequence.ObjectNode;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;

/**
 * Fig to show an object on a sequence diagram. The fig consists of an upper box
 * that shows the name of the object (the owner) and a lifeline. The lifeline consists
 * of lifeline elements. An element can be a dashed line (no link attached) or a
 * rectangle (link attached).
 * @author jaap.branderhorst@xs4all.nl
 * Aug 11, 2003
 */
public class FigObject extends FigNodeModelElement implements MouseListener {
    /**
    * The width of an activation box
    */
    public final static int WIDTH = 20;

    /**
     * The margin between the outer box and the name and stereotype text box.
     */
    public final static int MARGIN = 10;

    /**
     * The distance between two rows in the object rectangle.
     */
    public final static int ROWDISTANCE = 2;

    /**
     * The defaultheight of the object rectangle. That's 3 times the rowheight + 
     * 3 times a distance of 2 between the rows + the stereoheight. 
     */
    public final static int DEFAULT_HEIGHT =
        (3 * ROWHEIGHT + 3 * ROWDISTANCE + STEREOHEIGHT);

    /**
    * The defaultwidth of the object rectangle
    */
    public final static int DEFAULT_WIDTH = 3 * DEFAULT_HEIGHT / 2;

    /**
     * The outer black rectangle of the object box (object fig without lifeline).
     */
    private FigRect _outerBox;

    /**
     * The filled box for the object box (object fig without lifeline).
     */
    private FigRect _backgroundBox;

    /**
     * The lifeline (dashed line under the object box to which activations are 
     * attached)
     */
    private FigLine _lifeLine;

    /**
     * The owner of the lifeline.
     */
    private LifeLinePort _lifeLinePort;

    /**
     * A linked list where the positions of the links are stored
     */
    private LinkedList _linkPositions = new LinkedList();

    /**
     * The comma seperated list of base names of the classifierRole(s) that this object
     * represents.
     */
    private String _baseNames = "";

    /**
     * The comma seperated list of names of the classifierRole(s) that this object
     * represents.
     */
    private String _classifierRoleNames = "";

    /**
     * The name of the object (the owner of this fig).
     */
    private String _objectName = "";

    /**
     * Default constructor. Constructs the object rectangle, the lifeline, 
     * the name box and the stereotype box.
     */
    public FigObject() {
        super();
        _backgroundBox =
            new FigRect(
                0,
                0,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                Color.white,
                Color.white);
        _backgroundBox.setFilled(true);
        _outerBox = new FigRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        _outerBox.setFilled(false);
        _stereo =
            new FigText(
                DEFAULT_WIDTH / 2,
                ROWHEIGHT + ROWDISTANCE,
                0,
                0,
                Color.black,
                "Dialog",
                12,
                false);
        _stereo.setAllowsTab(false);
        _stereo.setEditable(false);
        _stereo.setFilled(false);
        _stereo.setLineWidth(0);
        _name =
            new FigText(
                DEFAULT_WIDTH / 2,
                2 * ROWDISTANCE + STEREOHEIGHT + ROWHEIGHT,
                0,
                0,
                Color.black,
                "Dialog",
                12,
                false);
        _name.setEditable(false);
        _name.setAllowsTab(false);
        _name.setFilled(false);
        _name.setLineWidth(0);
        _lifeLine =
            new FigLine(
                DEFAULT_WIDTH / 2,
                DEFAULT_HEIGHT,
                DEFAULT_WIDTH / 2,
                1000,
                Color.black);
        _lifeLine.setDashed(true);
        _linkPositions.add(new ObjectNode(this));
        for (int i = 0;
            i <= _lifeLine.getHeight() / SequenceDiagramLayout.LINK_DISTANCE;
            i++) {
            _linkPositions.add(new EmptyNode());
        }
        addFig(_lifeLine);
        addFig(_backgroundBox);
        addFig(_stereo);
        addFig(_name);
        addFig(_outerBox);
    }

    /**
     * @param gm
     * @param node
     */
    public FigObject(Object node) {
        this();
        setOwner(node);
    }

    /**
     * When the mouse button is released, this fig will be moved into position
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        super.mouseReleased(me);
        Layer lay = Globals.curEditor().getLayerManager().getActiveLayer();
        if (lay instanceof SequenceDiagramLayout) {
            ((SequenceDiagramLayout) lay).putInPosition(this);
        }
    }

    /**
     * Constructs the contents of the name text box and upates the name text box
     * accordingly. The contents of the name text box itself are NOT updated.
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        String nameText =
            (_objectName + "/" + _classifierRoleNames + ":" + _baseNames)
                .trim();
        _name.setText(nameText);
        center(_name);
        damage();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {
        super.updateStereotypeText();
        center(_stereo);
    }

    private void center(FigText figText) {
        int newX = this.getX() + this.getHalfWidth() - figText.getHalfWidth();
        if (figText.getX() != newX) {
            figText.setX(newX);
            updateBounds();
        }
    }

    /**
     * Sets the bounds and coordinates of this Fig. The outerbox (the black box around
     * the upper box) and the background box (the white box at the background) are
     * scaled to the given size. The name text box and the stereo text box are
     * moved to a correct position.
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    public void setBounds(int x, int y, int w, int h) {
        Rectangle oldBounds = getBounds();
        if (_name.getWidth() > w) {
            w = _name.getWidth() + 2 * MARGIN;
        }
        if (_stereo.getWidth() > w) {
            w = _stereo.getWidth() + 2 * MARGIN;
        }
        _name.setCenter(
            new Point(
                x + w / 2,
                _name.getY() - oldBounds.y + y + _name.getHalfHeight()));
        _stereo.setCenter(
            new Point(
                x + w / 2,
                _stereo.getY() - oldBounds.y + y + _stereo.getHalfHeight()));
        reSize(_outerBox, x, y, w, h);
        reSize(_backgroundBox, x, y, w, h);
        _lifeLine.setBounds(
            _outerBox.getX() + _outerBox.getHalfWidth(),
            _outerBox.getY() + _outerBox.getHeight(),
            0,
            h - _outerBox.getHeight());
        int factor = h / oldBounds.height;
        calcBounds(); //_x = x; _height = y; _w = w; _h = h;		
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * Scales the given fig that must be part of this FigObject
     * @param f the fig to scale
     * @param x the new x coordinate for the FigObject
     * @param y the new y coordinate for the FigObject 
     * @param w the new w coordinate for the FigObject
     * @param h the new h coordinate for the FigObject
     */
    private void reSize(Fig f, int x, int y, int w, int h) {
        if (f.isDisplayed()) {
            int newX =
                (_w == 0)
                    ? x
                    : x + (int) ((f.getX() - _x) * ((float) w / (float) _w));
            int newY =
                (_h == 0)
                    ? y
                    : y + (int) ((f.getY() - _y) * ((float) h / (float) _h));
            int newW =
                (_w == 0)
                    ? 0
                    : (int) (((float) f.getWidth()) * ((float) w / (float) _w));

            int newH =
                (_h == 0)
                    ? 0
                    : (int) (((float) f.getHeight()) * ((float) h / (float) _h));

            f.setBounds(newX, newY, newW, newH);
        }
    }

    /**
     * Just factored out this method to save me typing. Returns a beautified name
     * to show in the name text box.
     * @param o
     * @return
     */
    private String getBeautifiedName(Object o) {
        String name = ModelFacade.getName(o);
        if (name == null || name.equals("")) {
            name = "(anon " + ModelFacade.getUMLClassName(o) + ")";
        }
        return name;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#calcBounds()
     */
    public void calcBounds() {
        Rectangle bounds = _outerBox.getBounds();
        bounds.add(_lifeLine.getBounds());
        _x = bounds.x;
        _y = bounds.y;
        _h = bounds.height;
        _w = bounds.width;
    }

    private void removeActivations() {
        Iterator it = getFigs().iterator();
        while (it.hasNext()) {
            Fig fig = (Fig) it.next();
            if (fig != _outerBox
                && fig != _backgroundBox
                && fig instanceof FigRect) {
                removeFig(fig);
            }
        }
    }

    private void addActivations() {
        boolean startActivation = false;
        ActivationNode startActivationNode = null;
        ActivationNode endActivationNode = null;
        int x = _lifeLine.getX() - WIDTH / 2;
        for (int i = 0; i < _linkPositions.size(); i++) {
            Node node = (Node) _linkPositions.get(i);
            if (node instanceof ActivationNode
                && ((ActivationNode) node).isStart()) {
                startActivationNode = (ActivationNode) node;
                endActivationNode = null;
            }
            if (node instanceof ActivationNode
                && ((ActivationNode) node).isEnd()) {
                if (startActivationNode == null) {
                    throw new IllegalStateException("End activation node without start activation node");
                }
                endActivationNode = (ActivationNode) node;
                int startPos =
                    getYCoordinateForActivationBox(startActivationNode);
                int endPos = 0;
                if (!endActivationNode.isStart()) {
                    endPos = getYCoordinateForActivationBox(endActivationNode);
                } else {
                    endPos = getYCoordinate(endActivationNode);
                    if (!endActivationNode.isCutOffBottom()) {
                        endPos += SequenceDiagramLayout.LINK_DISTANCE / 2;
                    }
                }
                FigRect activation =
                    new FigRect(
                        x,
                        startPos,
                        WIDTH,
                        endPos - startPos,
                        _outerBox.getLineColor(),
                        _backgroundBox.getFillColor());
                addFig(activation);
            }
        }
    }

    public void updateActivations() {
        removeActivations();
        addActivations();
    }

    /**
     * The width of the FigObject should be equal to the width of the name or stereo
     * text box.
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateBounds()
     */
    protected void updateBounds() {
        Rectangle bounds = getBounds();
        bounds.width =
            Math.max(
                _name.getWidth() + 2 * MARGIN,
                _stereo.getWidth() + 2 * MARGIN);
        setBounds(bounds);
    }

    /**
     * Changing the line width should only change the line width of the outerbox
     * and the lifeline. 0 Values are ignored.
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        if (_outerBox.getLineWidth() != w && w != 0) {
            _outerBox.setLineWidth(w);
            _lifeLine.setLineWidth(w);
            damage();
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        if (col != null && col != _backgroundBox.getFillColor()) {
            _backgroundBox.setFillColor(col);
            damage();
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean filled) {
        if (_backgroundBox.getFilled() != filled) {
            _backgroundBox.setFilled(filled);
            damage();
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return _backgroundBox.getFillColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return _backgroundBox.getFilled();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return _outerBox.getLineColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return _outerBox.getLineWidth();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    protected void updateListeners(Object newOwner) {
        if (ModelFacade.isAInstance(newOwner)) {
            Object oldOwner = getOwner();
            UmlModelEventPump pump = UmlModelEventPump.getPump();
            pump.removeModelEventListener(this, oldOwner);
            pump.addModelEventListener(
                this,
                newOwner,
                new String[] { "name", "stereotype" });
            Iterator it = ModelFacade.getClassifiers(newOwner).iterator();
            while (it.hasNext()) {
                Object classifierRole = it.next();
                pump.removeModelEventListener(this, classifierRole);
                pump.addModelEventListener(
                    this,
                    classifierRole,
                    new String[] { "name", "base" });
                Iterator it2 = ModelFacade.getBases(classifierRole).iterator();
                while (it2.hasNext()) {
                    Object base = it2.next();
                    pump.addModelEventListener(this, base, "name");
                }
            }
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(ru.novosoft.uml.MElementEvent)
     */
    protected void modelChanged(MElementEvent mee) {
        boolean nameChanged = false;
        if (mee.getSource() == getOwner() && mee.getName().equals("name")) {
            updateObjectName();
            nameChanged = true;
        } else if (
            ModelFacade.isAClassifierRole(mee.getSource())
                && mee.getName().equals("name")) {
            updateClassifierRoleNames();
            nameChanged = true;
        } else if (
            mee.getSource() == getOwner()
                && mee.getName().equals("stereotype")) {
            updateStereotypeText();
        } else if (mee.getName().equals("name")) {
            updateBaseNames();
            nameChanged = true;
        }
        if (nameChanged) {
            updateNameText();
        }
    }

    private void updateClassifierRoleNames() {
        String roleNames = "";
        Iterator it = ModelFacade.getClassifiers(getOwner()).iterator();
        while (it.hasNext()) {
            roleNames += getBeautifiedName(it.next());
            if (it.hasNext()) {
                roleNames += ", ";
            }
        }
        _classifierRoleNames = roleNames;
    }

    private void updateBaseNames() {
        String baseNames = "";
        Iterator it = ModelFacade.getClassifiers(getOwner()).iterator();
        while (it.hasNext()) {
            Iterator it2 = ModelFacade.getBases(it.next()).iterator();
            while (it2.hasNext()) {
                baseNames += getBeautifiedName(it2.next());
                if (it2.hasNext()) {
                    baseNames += ",";
                }
            }
            if (it.hasNext()) {
                baseNames += ",";
            }
        }
        _baseNames = baseNames;
    }

    private void updateObjectName() {
        _objectName = getBeautifiedName(getOwner());
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        updateBaseNames();
        updateClassifierRoleNames();
        updateObjectName();
        super.renderingChanged();
    }

    /**
     * Returns the port for a given coordinate pair. Normally deepHitPort returns
     * the owner of the fig in the figgroup that is present at the given coordinate
     * pair (returning figs that are added later first). In this case it returns
     * a LinkPort. This method has a side effect of creating a FigLinkPort if 
     * @see org.tigris.gef.presentation.FigNode#deepHitPort(int, int)
     */
    public Object deepHitPort(int x, int y) {
        Object port = null;
        Rectangle rect = new Rectangle(getX(), y - 16, getWidth(), 32);
        Node foundNode = null;
        if (_lifeLine.intersects(rect)) {
            for (int i = _linkPositions.indexOf(getObjectNode());
                i < _linkPositions.size();
                i++) {
                Node node = (Node) _linkPositions.get(i);
                int position = getYCoordinate(node);
                if (i < _linkPositions.size() - 1) {
                    int nextPosition =
                        getYCoordinate((Node) _linkPositions.get(i + 1));
                    if (nextPosition >= y && position < y) {
                        if ((y - position) < (nextPosition - y)) {
                            foundNode = node;
                        } else {
                            foundNode = (Node) _linkPositions.get(i + 1);
                        }
                        break;
                    }
                } else {
                    foundNode =
                        (Node) _linkPositions.get(_linkPositions.size() - 1);
                    break;
                }
            }
            if (foundNode instanceof LinkPort
                && !(foundNode instanceof ObjectNode)) {
                FigLinkPort figLinkPort =
                    new FigLinkPort(
                        _lifeLine.getX() - WIDTH / 2,
                        getYCoordinate(foundNode),
                        _lifeLine.getX() + WIDTH / 2);
                LinkNode linkNode = new LinkNode(getOwner(), figLinkPort);
                _linkPositions.add(
                    _linkPositions.indexOf(foundNode) + 1,
                    linkNode);
                foundNode = linkNode;
            } else {
                if (!(foundNode instanceof LinkPort)
                    && !(foundNode instanceof ObjectNode)) {
                    FigLinkPort figLinkPort =
                        new FigLinkPort(
                            _lifeLine.getX() - WIDTH / 2,
                            getYCoordinate(foundNode),
                            _lifeLine.getX() + WIDTH / 2);
                    LinkNode linkNode = new LinkNode(getOwner(), figLinkPort);
                    _linkPositions.set(
                        _linkPositions.indexOf(foundNode),
                        foundNode);
                }

            }
            return foundNode;
        } else if (_outerBox.intersects(rect)) {
            ObjectNode objectNode = (ObjectNode) getObjectNode();
            if (objectNode.getFigLinkPort() != null && getFigLink(objectNode.getFigLinkPort()) != null) {
            	return null; // cannot connect here
            }
            if (objectNode.getFigLinkPort() == null) {            
            FigLinkPort figLinkPort =
                new FigLinkPort(
                    _lifeLine.getX() - WIDTH / 2,
                    getYCoordinate(foundNode),
                    _lifeLine.getX() + WIDTH / 2);
                    objectNode.setFigLinkPort(figLinkPort);
            }
            objectNode.setObject(getOwner());
            return objectNode;
        } else {
            return null;
        }       
    }

    private int getYCoordinate(Node node) {
        int index = _linkPositions.indexOf(node);
        int y = 0;
        if (index == 0) {
            y = getY() + _outerBox.getHalfHeight();
        } else {
            y =
                (index - 1) * SequenceDiagramLayout.LINK_DISTANCE
                    + getY()
                    + _outerBox.getHeight();
        }
        return y;
    }

    private int getYCoordinateForActivationBox(Node node) {
        int y = getYCoordinate(node);
        if (node instanceof ActivationNode) {
            if (node instanceof ObjectNode) {
                y = y + _outerBox.getHalfHeight();
            } else {
                ActivationNode activationNode = (ActivationNode) node;
                if (activationNode.isEnd()
                    && !activationNode.isCutOffBottom()) {
                    y += SequenceDiagramLayout.LINK_DISTANCE / 2;
                } else if (
                    activationNode.isStart()
                        && !activationNode.isCutOffTop()) {
                    y -= SequenceDiagramLayout.LINK_DISTANCE / 2;
                }
            }
        }
        return y;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object own) {
        super.setOwner(own);
        bindPort(own, _outerBox);
    }

    /**
     * Gets the FigLink that is attached to the given FigLinkPort
     * @param portFig
     * @return
     */
    public FigLink getFigLink(FigLinkPort portFig) {
        Iterator it = getFigEdges().iterator();
        while (it.hasNext()) {
            FigEdge figEdge = (FigEdge) it.next();
            if (figEdge instanceof FigLink
                && (figEdge.getSourcePortFig() == portFig
                    || figEdge.getDestPortFig() == portFig)) {
                return (FigLink) figEdge;
            }
        }
        return null;
    }

    public FigLine getLifeLine() {
        return _lifeLine;
    }

    /**
     * Returns a list with all nodes that belong to the same activation as the given 
     * node. Does not work for EmptyNode atm.
     * @param node
     * @return
     */
    public List getActivationNodes(Node node) {
        int start = 0;
        int end = 0;
        if (node instanceof ActivationNode) {
            ActivationNode activationNode = (ActivationNode) node;
            if (activationNode.isStart()) {
                start = _linkPositions.indexOf(activationNode);
                ListIterator it =
                    _linkPositions
                        .subList(
                            _linkPositions.indexOf(activationNode),
                            _linkPositions.size())
                        .listIterator();
                while (it.hasNext()) {
                    Node node2 = (Node) it.next();
                    if (node2 instanceof ActivationNode
                        && ((ActivationNode) node2).isEnd()) {
                        end = _linkPositions.indexOf(node2);
                        break;
                    }
                }
            } else if (activationNode.isEnd()) {
                end = _linkPositions.indexOf(activationNode);
                for (int i = end - 1; i >= 1; i--) {
                    Node node2 = (Node) _linkPositions.get(i);
                    if (node2 instanceof ActivationNode
                        && ((ActivationNode) node2).isStart()) {
                        start = _linkPositions.indexOf(node2);
                        break;
                    }
                }
            } else {
                // node is in between activations or does not belong to activation (yet)
                if (!(nextNode(activationNode) instanceof ActivationNode)
                    || !(previousNode(activationNode)
                        instanceof ActivationNode)) {
                    return new ArrayList();
                }
                for (int i = getIndexOf(activationNode) + 1;
                    i < _linkPositions.size();
                    i++) {
                    Node node2 = (Node) _linkPositions.get(i);
                    if (node2 instanceof ActivationNode
                        && ((ActivationNode) node2).isEnd()) {
                        end = getIndexOf(node2);
                        break;
                    }
                }
                for (int i = getIndexOf(activationNode) - 1; i > 0; i--) {
                    Node node2 = (Node) _linkPositions.get(i);
                    if (node2 instanceof ActivationNode
                        && ((ActivationNode) node2).isStart()) {
                        start = getIndexOf(node2);
                        break;
                    }
                }
                if (end == 0 || start == 0) {
                    throw new IllegalStateException("There should be an activation");
                }

            }
            return _linkPositions.subList(start, end);

        }
        return new ArrayList();
    }

    public int getIndexOf(Node node) {
        return _linkPositions.indexOf(node);
    }

    public void makeActivation(Node startNode, Node endNode) {
        int startIndex = _linkPositions.indexOf(startNode);
        int endIndex = _linkPositions.indexOf(endNode);
        makeActivation(startIndex, endIndex);
    }

    public void makeActivation(int startIndex, int endIndex) {
        for (int i = startIndex; i <= endIndex; i++) {
            Object o = _linkPositions.get(i);
            if (!(o instanceof ActivationNode)) {
                _linkPositions.set(i, new ActivationNode());
            }
        }
        ((ActivationNode) _linkPositions.get(startIndex)).setStart(true);
        ((ActivationNode) _linkPositions.get(endIndex)).setEnd(true);
    }

    public Node nextNode(Node node) {
        if (getIndexOf(node) < _linkPositions.size())
            return (Node) _linkPositions.get(getIndexOf(node) + 1);
        else
            return null;
    }

    public Node previousNode(Node node) {
        if (getIndexOf(node) > 0) {
            return (Node) _linkPositions.get(getIndexOf(node) - 1);
        } else
            return null;
    }

    public boolean hasActivations() {
        for (int i = 0; i < _linkPositions.size(); i++) {
            Node node = (Node) _linkPositions.get(i);
            if (node instanceof ActivationNode) {
                if (getActivationNodes(node).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @see org.tigris.gef.presentation.FigNode#getPortFig(java.lang.Object)
     */
    public Fig getPortFig(Object np) {
        if (np instanceof LinkPort) {
            return ((LinkPort) np).getFigLinkPort();
        }
        return null;
    }

    public void setNode(int index, Node node) {
        _linkPositions.set(index, node);
    }

    public ObjectNode getObjectNode() {
        for (int i = 0; i < _linkPositions.size(); i++) {
            if (_linkPositions.get(i) instanceof ObjectNode) {
                return (ObjectNode) _linkPositions.get(i);
            }
        }
        return null;
    }

}